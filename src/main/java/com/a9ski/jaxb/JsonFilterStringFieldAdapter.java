package com.a9ski.jaxb;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.a9ski.entities.filters.FilterStringField;
import com.a9ski.entities.filters.Matching;

public class JsonFilterStringFieldAdapter extends XmlAdapter<String, FilterStringField> {

	private static final String JSON_KEY_VALUE = "value";
	private static final String JSON_KEY_MATCHING = "matching";

	@Override
	public FilterStringField unmarshal(String v) throws Exception {
		final JsonObject j;
		try (final JsonReader r = Json.createReader(new StringReader(v))) {
			j = r.readObject();
		}
		final Matching m = Matching.valueOf(j.getString(JSON_KEY_MATCHING, Matching.EXACT.name()));
		return new FilterStringField(j.getString(JSON_KEY_VALUE, null), m);
	}

	@Override
	public String marshal(FilterStringField v) throws Exception {
		final JsonObjectBuilder b = Json.createObjectBuilder();
		if (v.getValue() != null) {
			b.add(JSON_KEY_VALUE, v.getValue());
		} else {
			b.addNull(JSON_KEY_VALUE);
		}
		b.add(JSON_KEY_MATCHING, v.getMatching().name());
		return b.build().toString();
	}

}
