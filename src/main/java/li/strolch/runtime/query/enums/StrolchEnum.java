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
package li.strolch.runtime.query.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Robert von Burg <eitch@eitchnet.ch>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "StrolchEnum")
@XmlType(propOrder = { "name", "locale", "values" })
public class StrolchEnum {

	@XmlAttribute(name = "name")
	private String name;

	@XmlAttribute(name = "locale")
	private String locale;

	@XmlElement(name = "values")
	private List<EnumValue> values;

	@XmlTransient
	private Locale localeL;

	public StrolchEnum() {
		// no-arg constructor for JAXB
	}

	/**
	 * @param name
	 * @param locale
	 * @param values
	 */
	public StrolchEnum(String name, Locale locale, List<EnumValue> values) {
		this.name = name;
		this.locale = locale.toString();
		this.localeL = locale;
		this.values = values;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the locale as string
	 */
	public String getLocale() {
		return this.locale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(String locale) {
		this.localeL = new Locale(locale);
		this.locale = locale;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocaleL() {
		return this.localeL;
	}

	/**
	 * @param localeL
	 *            the localeL to set
	 */
	public void setLocaleL(Locale localeL) {
		this.locale = localeL.getLanguage() + "_" + localeL.getCountry();
		this.localeL = localeL;
	}

	/**
	 * @return the values
	 */
	public List<EnumValue> getValues() {
		return this.values;
	}

	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(List<EnumValue> values) {
		this.values = values;
	}

	/**
	 * @return the list of {@link EnumValue#getId()}
	 */
	public List<String> getEnumValueIds() {
		List<String> values = new ArrayList<>(this.values.size());
		for (EnumValue enumValue : this.values) {
			values.add(enumValue.getId());
		}

		return values;
	}

	/**
	 * @return the list of {@link EnumValue#getValue()}
	 */
	public List<String> getEnumValues() {
		List<String> values = new ArrayList<>(this.values.size());
		for (EnumValue enumValue : this.values) {
			values.add(enumValue.getValue());
		}

		return values;
	}
}
