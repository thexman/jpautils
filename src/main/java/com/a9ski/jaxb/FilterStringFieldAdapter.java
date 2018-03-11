package com.a9ski.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.a9ski.entities.filters.FilterStringField;
import com.a9ski.entities.filters.Matching;

public class FilterStringFieldAdapter extends XmlAdapter<String, FilterStringField> {

	@Override
	public FilterStringField unmarshal(String v) throws Exception {
		if (v != null) {
			int pos = v.indexOf(':');
			if (pos == -1) {
				throw new IllegalArgumentException("Invalid string representation");
			}
			final Matching m = Matching.valueOf(v.substring(0, pos));
			final String value = (pos < v.length() - 1 ? v.substring(pos + 2) : null);
			return new FilterStringField(value, m);
		}
		return null;
	}

	@Override
	public String marshal(FilterStringField v) throws Exception {
		return (v != null ? marshalNonNull(v) : null);
	}

	private String marshalNonNull(FilterStringField v) {
		return v.getMatching().name() + ":" + (v.getValue() != null ? " " + v.getValue() : "");
	}

}
