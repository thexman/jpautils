package com.a9ski.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.a9ski.utils.TimeZoneList;
import com.a9ski.utils.TimeZoneList.NamedTimeZone;

@Converter
public class NamedTimeZoneConverter implements AttributeConverter<NamedTimeZone, String> {

	@Override
	public String convertToDatabaseColumn(NamedTimeZone ntz) {
		return ntz != null ? ntz.getTimeZone().getID() : null;
	}

	@Override
	public NamedTimeZone convertToEntityAttribute(String dbData) {
		return dbData != null ? TimeZoneList.getInstance().getNamedTimeZone(dbData) : null;
	}

}
