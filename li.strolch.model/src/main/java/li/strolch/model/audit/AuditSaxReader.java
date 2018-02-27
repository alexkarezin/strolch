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
package li.strolch.model.audit;

import java.text.MessageFormat;
import java.util.function.Consumer;

import li.strolch.model.Tags;
import li.strolch.utils.iso8601.ISO8601FormatFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Robert von Burg <eitch@eitchnet.ch>
 */
public class AuditSaxReader extends DefaultHandler {

	private final Consumer<Audit> auditConsumer;

	private Audit currentAudit;
	private StringBuilder sb = new StringBuilder();

	public AuditSaxReader(Consumer<Audit> auditConsumer) {
		this.auditConsumer = auditConsumer;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch (qName) {

		case Tags.AUDIT:
			this.currentAudit = new Audit();
			this.currentAudit.setId(Long.valueOf(attributes.getValue(Tags.Audit.ID)));
			break;

		case Tags.Audit.USERNAME:
		case Tags.Audit.FIRSTNAME:
		case Tags.Audit.LASTNAME:
		case Tags.Audit.DATE:
		case Tags.Audit.ELEMENT_TYPE:
		case Tags.Audit.ELEMENT_SUB_TYPE:
		case Tags.Audit.ELEMENT_ACCESSED:
		case Tags.Audit.NEW_VERSION:
		case Tags.Audit.ACTION:
		case Tags.Audit.ACCESS_TYPE:
			this.sb = new StringBuilder();
			break;

		default:
			throw new IllegalArgumentException(
					MessageFormat.format("The element ''{0}'' is unhandled!", qName)); //$NON-NLS-1$
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		switch (qName) {

		case Tags.AUDIT:
			this.auditConsumer.accept(this.currentAudit);
			this.currentAudit = null;
			break;

		case Tags.Audit.USERNAME:
			this.currentAudit.setUsername(this.sb.toString());
			this.sb = null;
			break;

		case Tags.Audit.FIRSTNAME:
			this.currentAudit.setFirstname(this.sb.toString());
			this.sb = null;
			break;

		case Tags.Audit.LASTNAME:
			this.currentAudit.setLastname(this.sb.toString());
			this.sb = null;
			break;

		case Tags.Audit.DATE:
			this.currentAudit.setDate(ISO8601FormatFactory.getInstance().parseDate(this.sb.toString()));
			this.sb = null;
			break;

		case Tags.Audit.ELEMENT_TYPE:
			this.currentAudit.setElementType(this.sb.toString());
			this.sb = null;
			break;

		case Tags.Audit.ELEMENT_SUB_TYPE:
			this.currentAudit.setElementSubType(this.sb.toString());
			this.sb = null;
			break;

		case Tags.Audit.ELEMENT_ACCESSED:
			this.currentAudit.setElementAccessed(this.sb.toString());
			this.sb = null;
			break;

		case Tags.Audit.NEW_VERSION:
			this.currentAudit.setNewVersion(ISO8601FormatFactory.getInstance().parseDate(this.sb.toString()));
			this.sb = null;
			break;

		case Tags.Audit.ACTION:
			this.currentAudit.setAction(this.sb.toString());
			this.sb = null;
			break;

		case Tags.Audit.ACCESS_TYPE:
			this.currentAudit.setAccessType(AccessType.valueOf(this.sb.toString()));
			this.sb = null;
			break;

		default:
			throw new IllegalArgumentException(
					MessageFormat.format("The element ''{0}'' is unhandled!", qName)); //$NON-NLS-1$
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (this.sb != null)
			this.sb.append(ch, start, length);
	}
}
