package com.a9ski.entities.filters;

import java.util.Locale;

/**
 * Filter for querying pages. All derived entities must have filter which is subclass of this one.
 *
 */
public class PageableFilter implements Filter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8665041779451559996L;

	private boolean distinct;

	private int firstResult;

	private int maxResults;

	private Locale locale;

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

	/**
	 * Gets the locale used for filtering
	 * 
	 * @return the locale used for filtering
	 */
	public Locale getLocale() {
		return locale != null ? locale : Locale.getDefault();
	}

	/**
	 * Sets the locale used for filtering
	 * 
	 * @param locale
	 *            the locale for filtering
	 */
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PageableFilter other = (PageableFilter) obj;
		if (distinct != other.distinct)
			return false;
		if (firstResult != other.firstResult)
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		if (maxResults != other.maxResults)
			return false;
		return true;
	}
}
