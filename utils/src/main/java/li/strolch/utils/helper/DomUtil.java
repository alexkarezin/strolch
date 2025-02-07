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
package li.strolch.utils.helper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.text.MessageFormat;

/**
 * @author Robert von Burg &lt;eitch@eitchnet.ch&gt;
 */
public class DomUtil {

	public static DocumentBuilder createDocumentBuilder() {
		try {
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			return dbfac.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			String msg = "No Xml Parser could be loaded: {0}";
			msg = MessageFormat.format(msg, e.getMessage());
			throw new RuntimeException(msg, e);
		}
	}
}
