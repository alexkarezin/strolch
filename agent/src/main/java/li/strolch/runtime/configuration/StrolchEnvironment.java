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
package li.strolch.runtime.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import li.strolch.runtime.StrolchConstants;
import li.strolch.utils.dbc.DBC;
import li.strolch.utils.helper.StringHelper;

/**
 * @author Robert von Burg <eitch@eitchnet.ch>
 */
public class StrolchEnvironment {

	public static final String ENV_PROPERTIES_FILE = "ENV.properties"; //$NON-NLS-1$

	private static final Logger logger = LoggerFactory.getLogger(StrolchEnvironment.class);

	public static String getEnvironmentFromSystemProperties() {
		String environment = System.getProperties().getProperty(StrolchConstants.ENV_STROLCH);
		if (StringHelper.isEmpty(environment)) {
			String msg = "The system property {0} is missing!"; //$NON-NLS-1$
			throw new StrolchConfigurationException(MessageFormat.format(msg, StrolchConstants.ENV_STROLCH));
		}

		return environment;
	}

	public static String getEnvironmentFromEnvProperties(File rootPath) {
		File envF = new File(rootPath, ENV_PROPERTIES_FILE);
		DBC.PRE.assertExists(
				MessageFormat.format("{0} does not exist in {1}", ENV_PROPERTIES_FILE, rootPath.getAbsolutePath()), //$NON-NLS-1$
				envF);
		Properties envP = new Properties();
		try (InputStream fin = Files.newInputStream(envF.toPath())) {
			envP.load(fin);
		} catch (Exception e) {
			throw new StrolchConfigurationException(
					MessageFormat.format("Failed to load {0} in {1}", ENV_PROPERTIES_FILE, rootPath), e); //$NON-NLS-1$
		}

		String environment = envP.getProperty(StrolchConstants.ENV_STROLCH);
		if (StringHelper.isEmpty(environment)) {
			String msg = "The property {0} does not exist in {1}"; //$NON-NLS-1$
			msg = MessageFormat.format(msg, StrolchConstants.ENV_STROLCH, envF.getAbsolutePath());
			throw new StrolchConfigurationException(msg);
		}

		return environment;
	}

	public static String getEnvironmentFromResourceEnv(Class<?> clazz) {
		InputStream stream = clazz.getResourceAsStream("/" + ENV_PROPERTIES_FILE);
		DBC.PRE.assertNotNull(
				MessageFormat.format("{0} does not exist as root resource for class {1}", ENV_PROPERTIES_FILE, clazz),
				stream);
		Properties envP = new Properties();
		try {
			envP.load(stream);
		} catch (Exception e) {
			throw new StrolchConfigurationException(MessageFormat.format("Failed to load {0}", ENV_PROPERTIES_FILE), e); //$NON-NLS-1$
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				logger.error("Failed to close InputStream!", e);
			}
		}

		String environment = envP.getProperty(StrolchConstants.ENV_STROLCH);
		if (StringHelper.isEmpty(environment)) {
			String msg = "The property {0} does not exist in {1}"; //$NON-NLS-1$
			msg = MessageFormat.format(msg, StrolchConstants.ENV_STROLCH, ENV_PROPERTIES_FILE);
			throw new StrolchConfigurationException(msg);
		}

		return environment;
	}
}
