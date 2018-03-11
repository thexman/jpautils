package com.a9ski.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.collections4.comparators.ComparableComparator;

import com.a9ski.utils.NumberUtils;
import com.a9ski.utils.Range;

public class LongRangeAdapter extends XmlAdapter<String, Range<Long>> {

	@Override
	public Range<Long> unmarshal(String v) throws Exception {
		if (v != null) {
			final String[] items = v.split(";");
			if (items.length != 4) {
				throw new IllegalArgumentException("Invalid string representation");
			}
			final Long start = NumberUtils.parseLong(items[0], null);
			final Long end = NumberUtils.parseLong(items[1], null);
			final Long min = NumberUtils.parseLong(items[2], null);
			final Long max = NumberUtils.parseLong(items[3], null);
			return new Range<Long>(ComparableComparator.comparableComparator(), start, end, min, max);
		}
		return null;
	}

	@Override
	public String marshal(Range<Long> v) throws Exception {
		return v != null ? marshalNonNull(v) : null;
	}

	private String marshalNonNull(Range<Long> v) {
		final StringBuilder sb = new StringBuilder();
		append(sb, v.getStart());
		append(sb, v.getEnd());
		append(sb, v.getMinValue());
		append(sb, v.getMaxValue());
		return sb.toString();
	}

	private StringBuilder append(StringBuilder sb, Long v) {
		sb.append(sb.length() > 0 ? ";" : "");
		sb.append(v != null ? v : "");
		return sb;
	}

}
