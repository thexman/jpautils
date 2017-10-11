package com.a9ski.jpa;

import java.util.List;

import javax.persistence.Parameter;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.tuple.Pair;

import com.a9ski.utils.ExtCollectionUtils;

/**
 * Class representing query configuration.
 * 
 * @author Kiril Arabadzhiyski
 *
 */
public class QueryConfig {

	private final List<Order> sortOrders;

	private final List<Pair<Parameter<Object>, Object>> parameters;

	private final List<Predicate> predicates;

	private final boolean distinct;

	/**
	 * Creates a new query configuration object
	 * 
	 * @param predicates
	 *            the predicates used in the query
	 * @param parameters
	 *            the parameter mapping used in the query
	 * @param sortOrders
	 *            the sort order used in the query
	 * @param distinct
	 *            flag indicating that only distinc entities must be returned
	 */
	public QueryConfig(final List<Predicate> predicates, final List<Pair<Parameter<Object>, Object>> parameters, final List<Order> sortOrders, final boolean distinct) {
		super();
		this.predicates = ExtCollectionUtils.defaultList(predicates);
		this.parameters = parameters;
		this.sortOrders = sortOrders;
		this.distinct = distinct;
	}

	/**
	 * Returns the parameter mapping used in the query
	 * 
	 * @return the parameter mapping used in the query
	 */
	public List<Pair<Parameter<Object>, Object>> getParameters() {
		return parameters;
	}

	/**
	 * Returns the sort order used in the query
	 * 
	 * @return the sort order used in the query
	 */
	public List<Order> getSortOrders() {
		return sortOrders;
	}

	/**
	 * Returns true if the only distinct object must be returned by the query.
	 * 
	 * @return true if the only distinct object must be returned by the query.
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * Gets a list of the predicates used in the query
	 * 
	 * @return list of the predicates used in the query
	 */
	public List<Predicate> getPredicates() {
		return predicates;
	}

	/**
	 * Gets an array of the predicates used in the query
	 * 
	 * @return an array of the predicates used in the query
	 */
	public Predicate[] getPredicatesArray() {
		return getPredicates().stream().toArray(Predicate[]::new);
	}
}
