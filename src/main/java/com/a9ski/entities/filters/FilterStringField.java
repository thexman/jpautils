package com.a9ski.entities.filters;

public class FilterStringField {
	private final String value;
	private final Matching matching;

	public FilterStringField(final String value, final Matching matching) {
		super();
		this.value = value;
		this.matching = matching;
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
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FilterStringField)) {
			return false;
		}
		FilterStringField other = (FilterStringField) obj;
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
