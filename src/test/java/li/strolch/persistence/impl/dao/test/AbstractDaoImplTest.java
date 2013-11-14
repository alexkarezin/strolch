/*
 * Copyright (c) 2012, Robert von Burg
 *
 * All rights reserved.
 *
 * This file is part of the XXX.
 *
 *  XXX is free software: you can redistribute 
 *  it and/or modify it under the terms of the GNU General Public License as 
 *  published by the Free Software Foundation, either version 3 of the License, 
 *  or (at your option) any later version.
 *
 *  XXX is distributed in the hope that it will 
 *  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with XXX.  If not, see 
 *  <http://www.gnu.org/licenses/>.
 */
package li.strolch.persistence.impl.dao.test;

import java.io.File;

import li.strolch.persistence.api.StrolchPersistenceHandler;
import li.strolch.testbase.runtime.RuntimeMock;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * @author Robert von Burg <eitch@eitchnet.ch>
 * 
 */
public abstract class AbstractDaoImplTest extends RuntimeMock {

	private static final String RUNTIME_PATH = "target/strolchRuntime/"; //$NON-NLS-1$
	private static final String DB_STORE_PATH_DIR = "dbStore"; //$NON-NLS-1$
	private static final String CONFIG_SRC = "src/test/resources/runtime/config"; //$NON-NLS-1$
	protected static StrolchPersistenceHandler persistenceHandler;

	@BeforeClass
	public static void beforeClass() {

		File rootPath = new File(RUNTIME_PATH);
		File configSrc = new File(CONFIG_SRC);
		RuntimeMock.mockRuntime(rootPath, configSrc);
		new File(rootPath, DB_STORE_PATH_DIR).mkdir();
		RuntimeMock.startContainer(rootPath);

		// initialize the component configuration
		persistenceHandler = getContainer().getComponent(StrolchPersistenceHandler.class);
	}

	@AfterClass
	public static void afterClass() {
		RuntimeMock.destroyRuntime();
	}
}
