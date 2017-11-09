package com.a9ski.entities.filters;

import java.io.Serializable;
import java.util.Locale;
import java.util.Set;

/**
 * Filter for querying identifiable entities. All derived entities must have filter which is subclass of this one.
 * 
 * @author Kiril Arabadzhiyski
 *
 */
public class IdentifiableEntityFilter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2464905393270229226L;

	private Set<Long> ids;

	private boolean distinct;

	private int firstResult;

	private int maxResults;

	private Locale locale;

	/**
	 * Returns a set of entity IDs to be filtered
	 * 
	 * @return the entity ids to be filtered
	 */
	public Set<Long> getIds() {
		return ids;
	}

	/**
	 * Sets entity IDs to be filtered
	 * 
	 * @param ids
	 *            entity IDs to be filtered
	 */
	public void setIds(final Set<Long> ids) {
		this.ids = ids;
	}

	/**
	 * Flag indicating that only distinct entities must be returned
	 * 
	 * @return flag indicating that only distinct entities must be returned
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * Sets flag indicating that only distinct entities must be returned
	 * 
	 * @param distinct
	 *            flag indicating that only distinct entities must be returned
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * Gets the position of the first result to retrieve
	 * 
	 * @return the position of the first result to retrieve
	 */
	public int getFirstResult() {
		return firstResult;
	}

	/**
	 * Sets the position of the first result to retrieve
	 * 
	 * @param firstResult
	 *            the position of the first result to retrieve
	 */
	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	/**
	 * Gets the maximum number of results to retrieve.
	 * 
	 * @return the maximum number of results to retrieve.
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * Sets the maximum number of results to retrieve.
	 * 
	 * @param maxResults
	 *            the maximum number of results to retrieve.
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public Locale getLocale() {
		return locale != null ? locale : Locale.getDefault();
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
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
		result = prime * result + (distinct ? 1231 : 1237);
		result = prime * result + firstResult;
		result = prime * result + ((ids == null) ? 0 : ids.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + maxResults;
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
		if (!(obj instanceof IdentifiableEntityFilter)) {
			return false;
		}
		IdentifiableEntityFilter other = (IdentifiableEntityFilter) obj;
		if (distinct != other.distinct) {
			return false;
		}
		if (firstResult != other.firstResult) {
			return false;
		}
		if (ids == null) {
			if (other.ids != null) {
				return false;
			}
		} else if (!ids.equals(other.ids)) {
			return false;
		}
		if (locale == null) {
			if (other.locale != null) {
				return false;
			}
		} else if (!locale.equals(other.locale)) {
			return false;
		}
		if (maxResults != other.maxResults) {
			return false;
		}
		return true;
	}
}
