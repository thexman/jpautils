package com.a9ski.jpa;

import java.util.Locale;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LocaleConverter implements AttributeConverter<Locale, String> {

	@Override
	public String convertToDatabaseColumn(final Locale locale) {
		return locale != null ? locale.toLanguageTag() : null;
	}

	@Override
	public Locale convertToEntityAttribute(final String dbData) {
		return dbData != null ? Locale.forLanguageTag(dbData) : null;
	}

}
