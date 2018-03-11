package com.a9ski.jaxb;

import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocaleAdapter extends XmlAdapter<String, Locale> {

	@Override
	public String marshal(Locale v) throws Exception {
		return (v != null ? v.toLanguageTag() : null);
	}

	@Override
	public Locale unmarshal(String v) throws Exception {
		return (v != null ? Locale.forLanguageTag(v) : null);
	}

}
