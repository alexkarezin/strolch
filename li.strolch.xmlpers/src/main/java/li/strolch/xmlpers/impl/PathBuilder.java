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
package li.strolch.xmlpers.impl;

import java.io.File;

import li.strolch.xmlpers.objref.ObjectRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Robert von Burg <eitch@eitchnet.ch>
 */
public class PathBuilder {

	private static final Logger logger = LoggerFactory.getLogger(PathBuilder.class);

	public static final String FILE_EXT = ".xml"; //$NON-NLS-1$
	public static final int EXT_LENGTH = PathBuilder.FILE_EXT.length();

	private final File basePath;

	public PathBuilder(File basePath) {
		this.basePath = basePath;
	}

	private String getFilename(String id) {
		return id.concat(PathBuilder.FILE_EXT);
	}

	public File getRootPath() {
		return this.basePath;
	}

	public File getPath(ObjectRef objectRef) {
		if (objectRef.isLeaf())
			return new File(this.basePath, getFilename(objectRef.getName()));
		else
			return new File(this.basePath, objectRef.getName());
	}
}
