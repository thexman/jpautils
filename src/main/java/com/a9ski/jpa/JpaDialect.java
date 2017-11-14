package com.a9ski.jpa;

import javax.persistence.TypedQuery;

/**
 * Interface representing a JPA dialect - some JPA provider specific functionality
 *
 * @author Kiril Arabadzhiyski
 *
 */
public interface JpaDialect {
	/**
	 * Gets query SQL string
	 *
	 * @param q
	 *            the query
	 * @return the query SQL string
	 */
	public String getSql(final TypedQuery<?> q);
}
