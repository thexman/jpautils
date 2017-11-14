package com.a9ski.jpa;

import java.util.TimeZone;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TimeZoneConverter implements AttributeConverter<TimeZone, String> {

	@Override
	public String convertToDatabaseColumn(final TimeZone tz) {
		return tz != null ? tz.getID() : null;
	}

	@Override
	public TimeZone convertToEntityAttribute(final String dbData) {
		return dbData != null ? TimeZone.getTimeZone(dbData) : null;
	}

}
