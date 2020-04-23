/*
 * Copyright 2015 Robert von Burg <eitch@eitchnet.ch>
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
package li.strolch.rest.filters;

import static li.strolch.rest.StrolchRestfulConstants.STROLCH_CERTIFICATE;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

import li.strolch.privilege.model.Certificate;
import li.strolch.rest.RestfulStrolchComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Reto Breitenmoser <reto.breitenmoser@4trees.ch>
 * @author Robert von Burg <eitch@eitchnet.ch>
 */
@Provider
public class AuthenticationResponseFilter implements ContainerResponseFilter {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationResponseFilter.class);

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {

		Certificate cert = (Certificate) requestContext.getProperty(STROLCH_CERTIFICATE);
		if (cert == null)
			return;

		if (cert.getUsage().isSingle()) {
			logger.info("Invalidating single usage certificate for " + cert.getUsername());
			RestfulStrolchComponent.getInstance().getSessionHandler().invalidate(cert);
		}
	}
}
