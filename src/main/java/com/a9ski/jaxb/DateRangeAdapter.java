package com.a9ski.jaxb;

import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.time.FastTimeZone;

import com.a9ski.utils.DateRange;
import com.a9ski.utils.DateUtils;
import com.a9ski.utils.Range;

public class DateRangeAdapter extends XmlAdapter<String, DateRange> {

	private final static TimeZone timeZone = FastTimeZone.getGmtTimeZone();

	@Override
	public DateRange unmarshal(String v) throws Exception {
		if (v != null) {
			final String[] items = v.split(";");
			if (items.length != 4) {
				throw new IllegalArgumentException("Invalid string representation");
			}
			final Date start = DateUtils.parseIsoDate(items[0], timeZone, null);
			final Date end = DateUtils.parseIsoDate(items[1], timeZone, null);
			final Date min = DateUtils.parseIsoDate(items[2], timeZone, null);
			final Date max = DateUtils.parseIsoDate(items[3], timeZone, null);
			return new DateRange(start, end, min, max);
		}
		return null;
	}

	@Override
	public String marshal(DateRange v) throws Exception {
		return v != null ? marshalNonNull(v) : null;
	}

	private String marshalNonNull(Range<Date> v) {
		final StringBuilder sb = new StringBuilder();
		append(sb, v.getStart());
		append(sb, v.getEnd());
		append(sb, v.getMinValue());
		append(sb, v.getMaxValue());
		return sb.toString();
	}

	private StringBuilder append(StringBuilder sb, Date v) {
		sb.append(sb.length() > 0 ? ";" : "");
		sb.append(v != null ? DateUtils.formatIsoDate(v, timeZone) : "");
		return sb;
	}

}
