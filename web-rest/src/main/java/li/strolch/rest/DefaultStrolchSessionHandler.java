/*
 * Copyright 2013 Robert von Burg <eitch@eitchnet.ch>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package li.strolch.rest;

import static java.util.function.Function.identity;
import static li.strolch.runtime.StrolchConstants.StrolchPrivilegeConstants.PRIVILEGE_GET_SESSION;
import static li.strolch.runtime.StrolchConstants.StrolchPrivilegeConstants.PRIVILEGE_INVALIDATE_SESSION;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import li.strolch.agent.api.ComponentContainer;
import li.strolch.agent.api.StrolchComponent;
import li.strolch.exception.StrolchNotAuthenticatedException;
import li.strolch.privilege.base.AccessDeniedException;
import li.strolch.privilege.base.PrivilegeException;
import li.strolch.privilege.model.Certificate;
import li.strolch.privilege.model.PrivilegeContext;
import li.strolch.privilege.model.SimpleRestrictable;
import li.strolch.privilege.model.Usage;
import li.strolch.rest.model.UserSession;
import li.strolch.runtime.configuration.ComponentConfiguration;
import li.strolch.runtime.privilege.PrivilegeHandler;
import li.strolch.utils.dbc.DBC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Robert von Burg <eitch@eitchnet.ch>
 */
public class DefaultStrolchSessionHandler extends StrolchComponent implements StrolchSessionHandler {

	public static final String PARAM_SESSION_TTL_MINUTES = "session.ttl.minutes";
	public static final String PARAM_SESSION_MAX_KEEP_ALIVE_MINUTES = "session.maxKeepAlive.minutes";
	public static final String PARAM_SESSION_RELOAD_SESSIONS = "session.reload";

	private static final Logger logger = LoggerFactory.getLogger(DefaultStrolchSessionHandler.class);
	private PrivilegeHandler privilegeHandler;
	private final Map<String, Certificate> certificateMap;
	private boolean reloadSessions;
	private int sessionTtlMinutes;
	private int maxKeepAliveMinutes;

	private ScheduledFuture<?> validateSessionsTask;
	private Future<?> persistSessionsTask;

	public DefaultStrolchSessionHandler(ComponentContainer container, String componentName) {
		super(container, componentName);
		this.certificateMap = new ConcurrentHashMap<>();
	}

	@Override
	public int getSessionTtlMinutes() {
		return this.sessionTtlMinutes;
	}

	@Override
	public int getSessionMaxKeepAliveMinutes() {
		return this.maxKeepAliveMinutes;
	}

	@Override
	public boolean isRefreshAllowed() {
		return this.privilegeHandler.isRefreshAllowed();
	}

	@Override
	public void initialize(ComponentConfiguration configuration) throws Exception {
		this.sessionTtlMinutes = configuration.getInt(PARAM_SESSION_TTL_MINUTES, 30);
		this.maxKeepAliveMinutes = configuration.getInt(PARAM_SESSION_MAX_KEEP_ALIVE_MINUTES,
				Math.max(this.sessionTtlMinutes, 30));
		this.reloadSessions = configuration.getBoolean(PARAM_SESSION_RELOAD_SESSIONS, false);
		super.initialize(configuration);
	}

	@Override
	public void start() throws Exception {
		this.privilegeHandler = getContainer().getComponent(PrivilegeHandler.class);
		this.certificateMap.clear();

		if (this.reloadSessions) {
			Map<String, Certificate> certificates = runAsAgentWithResult(ctx -> {
				Certificate cert = ctx.getCertificate();
				return this.privilegeHandler.getPrivilegeHandler()
						.getCertificates(cert)
						.stream()
						.filter(c -> !c.getUserState().isSystem())
						.collect(Collectors.toMap(Certificate::getAuthToken, identity()));
			});
			this.certificateMap.putAll(certificates);

			checkSessionsForTimeout();
			logger.info("Restored " + certificates.size() + " sessions of which " + (certificates.size()
					- this.certificateMap.size()) + " had timed out and were removed.");
		}

		this.validateSessionsTask = getScheduledExecutor("SessionHandler").scheduleWithFixedDelay(
				this::checkSessionsForTimeout, 5, 1, TimeUnit.MINUTES);

		super.start();
	}

	@Override
	public void stop() throws Exception {

		if (this.validateSessionsTask != null)
			this.validateSessionsTask.cancel(true);

		if (this.reloadSessions) {

			if (this.privilegeHandler != null)
				persistSessions();

		} else {
			Map<String, Certificate> certificateMap;
			synchronized (this.certificateMap) {
				certificateMap = new HashMap<>(this.certificateMap);
				this.certificateMap.clear();
			}
			for (Certificate certificate : certificateMap.values()) {
				try {
					this.privilegeHandler.invalidate(certificate);
				} catch (Exception e) {
					logger.error("Failed to invalidate certificate " + certificate, e);
				}
			}

		}

		this.privilegeHandler = null;
		super.stop();
	}

	@Override
	public void destroy() throws Exception {
		this.certificateMap.clear();
		super.destroy();
	}

	@Override
	public Certificate authenticate(String username, char[] password, String source, Usage usage, boolean keepAlive) {
		DBC.PRE.assertNotEmpty("Username must be set!", username);
		DBC.PRE.assertNotNull("Passwort must be set", password);

		Certificate certificate = this.privilegeHandler.authenticate(username, password, source, usage, keepAlive);

		this.certificateMap.put(certificate.getAuthToken(), certificate);
		logger.info(MessageFormat.format("{0} sessions currently active.", this.certificateMap.size()));

		return certificate;
	}

	@Override
	public Certificate authenticateSingleSignOn(Object data) {
		Certificate certificate = this.privilegeHandler.authenticateSingleSignOn(data);

		this.certificateMap.put(certificate.getAuthToken(), certificate);
		logger.info(MessageFormat.format("{0} sessions currently active.", this.certificateMap.size()));

		return certificate;
	}

	@Override
	public Certificate authenticateSingleSignOn(Object data, String source) {
		Certificate certificate = this.privilegeHandler.authenticateSingleSignOn(data, source);

		this.certificateMap.put(certificate.getAuthToken(), certificate);
		logger.info(MessageFormat.format("{0} sessions currently active.", this.certificateMap.size()));

		return certificate;
	}

	@Override
	public Certificate refreshSession(Certificate certificate, String source) {
		Certificate refreshedSession = this.privilegeHandler.refreshSession(certificate, source);

		invalidate(certificate);
		this.certificateMap.put(refreshedSession.getAuthToken(), refreshedSession);
		logger.info(MessageFormat.format("{0} sessions currently active.", this.certificateMap.size()));

		return refreshedSession;
	}

	@Override
	public boolean isSessionKnown(String authToken) {
		DBC.PRE.assertNotEmpty("authToken must be set!", authToken);
		return this.certificateMap.containsKey(authToken);
	}

	@Override
	public Certificate validate(String authToken) throws StrolchNotAuthenticatedException {
		DBC.PRE.assertNotEmpty("authToken must be set!", authToken);

		Certificate certificate = this.certificateMap.get(authToken);
		if (certificate == null)
			throw new StrolchNotAuthenticatedException(
					MessageFormat.format("No certificate exists for sessionId {0}", authToken));

		return validate(certificate).getCertificate();
	}

	@Override
	public Certificate validate(String authToken, String source) throws StrolchNotAuthenticatedException {
		DBC.PRE.assertNotEmpty("authToken must be set!", authToken);

		Certificate certificate = this.certificateMap.get(authToken);
		if (certificate == null)
			throw new StrolchNotAuthenticatedException(
					MessageFormat.format("No certificate exists for sessionId {0}", authToken));

		return validate(certificate, source).getCertificate();
	}

	@Override
	public PrivilegeContext validate(Certificate certificate) throws StrolchNotAuthenticatedException {
		try {
			PrivilegeContext privilegeContext = this.privilegeHandler.validate(certificate);

			if (this.persistSessionsTask != null)
				this.persistSessionsTask = getScheduledExecutor("SessionHandler").schedule(this::persistSessions, 5,
						TimeUnit.SECONDS);

			return privilegeContext;
		} catch (PrivilegeException e) {
			throw new StrolchNotAuthenticatedException(e.getMessage(), e);
		}
	}

	@Override
	public PrivilegeContext validate(Certificate certificate, String source) throws StrolchNotAuthenticatedException {
		try {
			PrivilegeContext privilegeContext = this.privilegeHandler.validate(certificate, source);

			if (this.persistSessionsTask != null)
				this.persistSessionsTask = getScheduledExecutor("SessionHandler").schedule(this::persistSessions, 5,
						TimeUnit.SECONDS);

			return privilegeContext;
		} catch (PrivilegeException e) {
			throw new StrolchNotAuthenticatedException(e.getMessage(), e);
		}
	}

	private void persistSessions() {
		try {
			runAsAgent(ctx -> this.privilegeHandler.getPrivilegeHandler()
					.persistSessions(ctx.getCertificate(), ctx.getCertificate().getSource()));
		} catch (Exception e) {
			logger.error("Failed to persist sessions", e);
		} finally {
			this.persistSessionsTask = null;
		}
	}

	@Override
	public void invalidate(Certificate certificate) {
		DBC.PRE.assertNotNull("Certificate must be given!", certificate);

		Certificate removedCert = this.certificateMap.remove(certificate.getAuthToken());
		if (removedCert == null)
			logger.error(MessageFormat.format("No session was registered with token {0}", certificate.getAuthToken()));

		this.privilegeHandler.invalidate(certificate);
	}

	@Override
	public void initiateChallengeFor(Usage usage, String username) {
		this.privilegeHandler.getPrivilegeHandler().initiateChallengeFor(usage, username);
	}

	@Override
	public void initiateChallengeFor(Usage usage, String username, String source) {
		this.privilegeHandler.getPrivilegeHandler().initiateChallengeFor(usage, username, source);
	}

	@Override
	public Certificate validateChallenge(String username, String challenge) throws PrivilegeException {
		DBC.PRE.assertNotEmpty("username must be set!", username);
		DBC.PRE.assertNotEmpty("challenge must be set", challenge);

		Certificate certificate = this.privilegeHandler.getPrivilegeHandler().validateChallenge(username, challenge);

		this.certificateMap.put(certificate.getAuthToken(), certificate);
		logger.info(MessageFormat.format("{0} sessions currently active.", this.certificateMap.size()));

		return certificate;
	}

	@Override
	public Certificate validateChallenge(String username, String challenge, String source) throws PrivilegeException {
		DBC.PRE.assertNotEmpty("username must be set!", username);
		DBC.PRE.assertNotEmpty("challenge must be set", challenge);

		Certificate certificate = this.privilegeHandler.getPrivilegeHandler()
				.validateChallenge(username, challenge, source);

		this.certificateMap.put(certificate.getAuthToken(), certificate);
		logger.info(MessageFormat.format("{0} sessions currently active.", this.certificateMap.size()));

		return certificate;
	}

	private void checkSessionsForTimeout() {
		ZonedDateTime maxKeepAliveTime = ZonedDateTime.now().minus(this.maxKeepAliveMinutes, ChronoUnit.MINUTES);
		ZonedDateTime timeOutTime = ZonedDateTime.now().minus(this.sessionTtlMinutes, ChronoUnit.MINUTES);

		Map<String, Certificate> certificateMap = getCertificateMapCopy();
		for (Certificate certificate : certificateMap.values()) {
			if (certificate.isKeepAlive()) {

				if (maxKeepAliveTime.isAfter(certificate.getLoginTime())) {
					String msg = "KeepAlive for session {0} for user {1} has expired, invalidating session...";
					logger.info(MessageFormat.format(msg, certificate.getSessionId(), certificate.getUsername()));
					sessionTimeout(certificate);
				}

			} else {
				if (timeOutTime.isAfter(certificate.getLastAccess())) {
					String msg = "Session {0} for user {1} has expired, invalidating session...";
					logger.info(MessageFormat.format(msg, certificate.getSessionId(), certificate.getUsername()));
					sessionTimeout(certificate);
				}
			}
		}
	}

	private void sessionTimeout(Certificate certificate) {
		DBC.PRE.assertNotNull("Certificate must be given!", certificate);

		Certificate removedCert = this.certificateMap.remove(certificate.getAuthToken());
		if (removedCert == null)
			logger.error(MessageFormat.format("No session was registered with token {0}", certificate.getAuthToken()));

		this.privilegeHandler.sessionTimeout(certificate);
	}

	@Override
	public UserSession getSession(Certificate certificate, String source, String sessionId) throws PrivilegeException {
		PrivilegeContext ctx = this.privilegeHandler.validate(certificate, source);
		ctx.assertHasPrivilege(PRIVILEGE_GET_SESSION);

		Map<String, Certificate> certificateMap = getCertificateMapCopy();
		for (Certificate cert : certificateMap.values()) {
			if (cert.getSessionId().equals(sessionId)) {
				ctx.validateAction(new SimpleRestrictable(PRIVILEGE_GET_SESSION, cert));
				return new UserSession(cert);
			}
		}

		throw new PrivilegeException("No Session exists with the id " + sessionId);
	}

	@Override
	public List<UserSession> getSessions(Certificate certificate, String source) {
		PrivilegeContext ctx = this.privilegeHandler.validate(certificate, source);
		ctx.assertHasPrivilege(PRIVILEGE_GET_SESSION);
		List<UserSession> sessions = new ArrayList<>(this.certificateMap.size());

		Map<String, Certificate> certificateMap = getCertificateMapCopy();
		for (Certificate cert : certificateMap.values()) {
			try {
				ctx.validateAction(new SimpleRestrictable(PRIVILEGE_GET_SESSION, cert));
				sessions.add(new UserSession(cert));
			} catch (AccessDeniedException e) {
				// no, user may not get this session
			}
		}

		return sessions;
	}

	@Override
	public void invalidate(Certificate certificate, String sessionId) {
		PrivilegeContext ctx = this.privilegeHandler.validate(certificate);
		ctx.assertHasPrivilege(PRIVILEGE_INVALIDATE_SESSION);

		boolean ok = false;
		Map<String, Certificate> certificateMap = getCertificateMapCopy();
		for (Certificate cert : certificateMap.values()) {
			if (cert.getSessionId().equals(sessionId)) {
				ctx.validateAction(new SimpleRestrictable(PRIVILEGE_INVALIDATE_SESSION, cert));
				invalidate(cert);
				ok = true;
				break;
			}
		}

		if (!ok) {
			throw new PrivilegeException("Can not invalidate session as no session exists with the id " + sessionId);
		}
	}

	@Override
	public void setSessionLocale(Certificate certificate, String sessionId, Locale locale) {
		if (!certificate.getSessionId().equals(sessionId)) {
			String msg = "Users can only change their own session locale: {0} may not change locale of session {1}";
			throw new AccessDeniedException(MessageFormat.format(msg, certificate.getUsername(), sessionId));
		}

		Map<String, Certificate> certificateMap = getCertificateMapCopy();
		for (Certificate cert : certificateMap.values()) {
			if (cert.getSessionId().equals(sessionId)) {
				if (!cert.getLocale().equals(locale)) {
					cert.setLocale(locale);
					persistSessions();
				}
				break;
			}
		}
	}

	private Map<String, Certificate> getCertificateMapCopy() {
		synchronized (this.certificateMap) {
			return new HashMap<>(this.certificateMap);
		}
	}
}
