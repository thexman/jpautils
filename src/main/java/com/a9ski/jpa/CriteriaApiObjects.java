package com.a9ski.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;

/**
 * Holder class that contains all Criteria API objects needed for creating query predicates.
 *
 * @author Kiril Arabadzhiyski
 *
 * @param <E>
 *            the entity class
 */
public class CriteriaApiObjects<E> {
	private final CriteriaBuilder criteriaBuilder;
	private final CriteriaQuery<?> criteriaQuery;
	private final Path<E> path;

	/**
	 * Creates a new holder object
	 *
	 * @param criteriaBuilder
	 *            the criteria builder
	 * @param criteriaQuery
	 *            the criteria query
	 * @param path
	 *            the JPA entity path
	 */
	public CriteriaApiObjects(final CriteriaBuilder criteriaBuilder, final CriteriaQuery<?> criteriaQuery, final Path<E> path) {
		super();
		this.criteriaBuilder = criteriaBuilder;
		this.criteriaQuery = criteriaQuery;
		this.path = path;
	}

	/**
	 * Returns the criteria builder
	 *
	 * @return the criteria builder
	 */
	public CriteriaBuilder getCriteriaBuilder() {
		return criteriaBuilder;
	}

	/**
	 * Returns the criteria query
	 *
	 * @return the criteria query
	 */
	public CriteriaQuery<?> getCriteriaQuery() {
		return criteriaQuery;
	}

	/**
	 * Returns JPA entity path
	 *
	 * @return JPA entity path
	 */
	public Path<E> getPath() {
		return path;
	}
}
