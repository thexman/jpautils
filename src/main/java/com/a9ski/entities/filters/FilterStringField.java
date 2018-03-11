package com.a9ski.entities.filters;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.a9ski.jaxb.FilterStringFieldAdapter;

@XmlAccessorType(XmlAccessType.NONE)
@XmlJavaTypeAdapter(FilterStringFieldAdapter.class)
public class FilterStringField {
	private final String value;
	private final Matching matching;

	@SuppressWarnings("unused")
	private FilterStringField() {
		this(null, null);
	}

	public FilterStringField(final String value, final Matching matching) {
		super();
		this.value = value;
		this.matching = matching != null ? matching : Matching.EXACT;
	}

	public String getValue() {
		return value;
	}

	public Matching getMatching() {
		return matching;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matching == null) ? 0 : matching.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FilterStringField)) {
			return false;
		}
		final FilterStringField other = (FilterStringField) obj;
		if (matching != other.matching) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}
}
