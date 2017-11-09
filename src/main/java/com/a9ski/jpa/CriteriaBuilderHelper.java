package com.a9ski.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.apache.commons.collections4.CollectionUtils;

import com.a9ski.entities.filters.FilterStringField;
import com.a9ski.utils.DateRange;
import com.a9ski.utils.ExtCollectionUtils;
import com.a9ski.utils.Range;
import com.a9ski.utils.StringUtils;

/**
 * Helper object for simplifying usage of CriteriaBuilder
 *
 * @author Kiril Arabadzhiyski
 *
 */
public class CriteriaBuilderHelper {

	private static final char ESCAPE_CHAR = '\\';

	private final CriteriaBuilder cb;
	private final Locale locale;
	private final List<Predicate> predicates;
	private final List<CriteriaBuilderHelper> ors = new ArrayList<CriteriaBuilderHelper>();
	private final int maxNumberOfInElements;

	/**
	 * Creates a new helper object
	 * 
	 * @param cb
	 *            the criteria builder
	 * @param locale
	 *            the locale used for string comparison
	 */
	public CriteriaBuilderHelper(final CriteriaBuilder cb, final Locale locale) {
		this(cb, locale, new ArrayList<Predicate>());
	}

	/**
	 * Creates a new helper object
	 * 
	 * @param cb
	 *            the criteria builder
	 * @param locale
	 *            the locale used for string comparison
	 * @param predicates
	 *            the initial predicates
	 */
	public CriteriaBuilderHelper(final CriteriaBuilder cb, final Locale locale, final List<Predicate> predicates) {
		this(cb, locale, predicates, 100);
	}

	/**
	 * Creates a new helper object
	 * 
	 * @param cb
	 *            the criteria builder
	 * @param locale
	 *            the locale used for string comparison
	 * @param predicates
	 *            the initial predicates
	 * @param maxNumberOfInElements
	 *            the maximum number of elements in IN predicate. If adding IN predicate with more than <tt>maxNumberOfInElements</tt> the IN clause is split into several IN clauses joined with AND predicate
	 */
	public CriteriaBuilderHelper(final CriteriaBuilder cb, final Locale locale, final List<Predicate> predicates, final int maxNumberOfInElements) {
		this.cb = cb;
		this.locale = (locale != null ? locale : Locale.getDefault());
		this.predicates = predicates; // NOSONAR
		this.maxNumberOfInElements = maxNumberOfInElements;
	}

	/**
	 * Returns the maximum number of elements in IN predicate. If adding IN predicate with more than <tt>maxNumberOfInElements</tt> the IN clause is split into several IN clauses joined with AND predicate
	 * 
	 * @return the maximum number of elements in IN predicate.
	 */
	public int getMaxNumberOfInElements() {
		return maxNumberOfInElements;
	}

	/**
	 * Creates a <b>LIKE</b> predicate
	 * 
	 * @param field
	 *            the entity field
	 * @param patternValue
	 *            the pattern value is a string literal or a string-valued input parameter in which an underscore (_) stands for any single character, a percent (%) character stands for any sequence of characters (including the empty sequence), and all other characters stand for themselves.
	 * @param escapeCharacter
	 *            escape character
	 * @return the helper class, useful for method chaining
	 */
	public CriteriaBuilderHelper like(final Expression<String> field, final String patternValue, char escapeCharacter) {
		if (StringUtils.isNotBlank(patternValue)) {
			predicates.add(cb.like(cb.lower(field), toIgnoreCaseString(patternValue), escapeCharacter));
		}
		return this;
	}

	/**
	 * Creates a <b>LIKE</b> predicate
	 * 
	 * @param field
	 *            the entity field
	 * @param text
	 *            the text which will be converted to pattern. See {@link #createLikePattern(String, char)}
	 * @return the helper class, useful for method chaining
	 */
	public CriteriaBuilderHelper likeText(final Expression<String> field, final String text) {
		if (StringUtils.isNotBlank(text)) {
			like(field, createLikePattern(text, ESCAPE_CHAR), ESCAPE_CHAR);
		}
		return this;
	}

	/**
	 * Create a <b>EQUAL</b> OR <b>LIKE</b> predicate depending on the matching
	 * 
	 * @param field
	 *            the entity field
	 * @param value
	 *            the value
	 * @return the helper class, useful for method chaining
	 */
	public CriteriaBuilderHelper add(final Expression<String> field, final FilterStringField value) { // NOSONAR
		if (value != null && value.getMatching() != null && StringUtils.isNotBlank(value.getValue())) {
			switch (value.getMatching()) {
				case STARTS_WITH:
					like(field, createStartsWithPattern(value.getValue(), ESCAPE_CHAR), ESCAPE_CHAR);
					break;
				case ENDS_WITH:
					like(field, createEndsWithPattern(value.getValue(), ESCAPE_CHAR), ESCAPE_CHAR);
					break;
				case LIKE:
					likeText(field, value.getValue());
					break;
				case EXACT:
					equal(field, value.getValue());
					break;
				case CUSTOM:
					like(field, value.getValue(), ESCAPE_CHAR);
					break;
			}
		}
		return this;
	}

	/**
	 * Create a <b>EQUAL</b> predicate
	 * 
	 * @param field
	 *            the entity field
	 * @param value
	 *            the value
	 * @return the helper class, useful for method chaining
	 */
	public <T> CriteriaBuilderHelper equal(final Expression<T> field, final T value) { // NOSONAR
		if (value != null) {
			predicates.add(cb.equal(field, value));
		}
		return this;
	}

	/**
	 * Create a <b>NOT EQUAL</b> predicate
	 * 
	 * @param field
	 *            the entity field
	 * @param value
	 *            the value
	 * @return the helper class, useful for method chaining
	 */
	public <T> CriteriaBuilderHelper notEqual(final Expression<T> field, final T value) {
		if (value != null) {
			predicates.add(cb.notEqual(field, value));
		}
		return this;
	}

	/**
	 * Create a <b>EQUAL</b> predicate for two entity fields
	 * 
	 * @param field1
	 *            an entity field
	 * @param field2
	 *            an entity field
	 * @return the helper class, useful for method chaining
	 */
	public <T> CriteriaBuilderHelper equal(final Expression<T> field1, final Expression<T> field2) { // NOSONAR
		predicates.add(cb.equal(field1, field2));
		return this;
	}

	/**
	 * Create a <b>NOT EQUAL</b> predicate for two entity fields
	 * 
	 * @param field1
	 *            an entity field
	 * @param field2
	 *            an entity field
	 * @return the helper class, useful for method chaining
	 */
	public <T> CriteriaBuilderHelper notEqual(final Expression<T> field1, final Expression<T> field2) {
		predicates.add(cb.notEqual(field1, field2));
		return this;
	}

	/**
	 * Create a <b>IN</b> predicate.
	 * <p>
	 * If the number of values is bigger than {@link #getMaxNumberOfInElements()} then the IN clause is split into several IN clauses joined with AND predicate
	 * 
	 * @param field
	 *            an entity field
	 * @param values
	 *            array of values
	 * @return the helper class, useful for method chaining
	 */
	@SuppressWarnings("unchecked")
	public <T> CriteriaBuilderHelper in(final Expression<T> field, final T... values) {
		return in(field, Arrays.asList(values));
	}

	/**
	 * Create a <b>IN</b> predicate
	 * <p>
	 * If the number of values is bigger than {@link #getMaxNumberOfInElements()} then the IN clause is split into several IN clauses joined with AND predicate
	 * 
	 * @param field
	 *            an entity field
	 * @param values
	 *            collection of values
	 * @return the helper class, useful for method chaining
	 */
	public <T> CriteriaBuilderHelper in(final Expression<T> field, final Collection<T> values) {
		if (ExtCollectionUtils.isNotEmpty(values)) {
			if (values.size() == 1) {
				equal(field, values.iterator().next());
			} else if (values.size() <= maxNumberOfInElements) {
				predicates.add(field.in(values));
			} else {
				final CriteriaBuilderHelper or = or();
				for (final List<T> subSearchIn : ExtCollectionUtils.split(values, maxNumberOfInElements, ArrayList::new)) {
					or.in(field, subSearchIn);
				}
			}
		}
		return this;
	}

	/**
	 * Create a <b>NOT IN</b> predicate. IF the number of values is bigger than
	 * <p>
	 * If the number of values is bigger than {@link #getMaxNumberOfInElements()} then the NOT IN clause is split into several NOT IN clauses joined with AND predicate
	 * 
	 * @param field
	 *            an entity field
	 * @param values
	 *            array of values
	 * @return the helper class, useful for method chaining
	 */
	@SuppressWarnings("unchecked")
	public <T> CriteriaBuilderHelper notIn(final Expression<T> field, final T... values) {
		return notIn(field, Arrays.asList(values));
	}

	/**
	 * Create a <b>NOT IN</b> predicate
	 * <p>
	 * If the number of values is bigger than {@link #getMaxNumberOfInElements()} then the NOT IN clause is split into several NOT IN clauses joined with AND predicate
	 * 
	 * @param field
	 *            an entity field
	 * @param values
	 *            collection of values
	 * @return the helper class, useful for method chaining
	 */
	public <T> CriteriaBuilderHelper notIn(final Expression<T> field, final Collection<T> values) {
		if (!CollectionUtils.isEmpty(values)) {
			if (values.size() == 1) {
				notEqual(field, values.iterator().next());
			} else if (values.size() <= maxNumberOfInElements) {
				predicates.add(cb.not(field.in(values)));
			} else {
				final CriteriaBuilderHelper or = or();
				for (final List<T> subSearchIn : ExtCollectionUtils.split(values, maxNumberOfInElements, ArrayList::new)) {
					or.notIn(field, subSearchIn);
				}
			}
		}
		return this;
	}

	/**
	 * Create a <b>IS NOT NULL</b> predicate
	 * 
	 * @param field
	 *            the entity field
	 * @return the helper class, useful for method chaining
	 */
	public <T> CriteriaBuilderHelper isNotNull(final Expression<T> field) {
		predicates.add(field.isNotNull());
		return this;
	}

	/**
	 * Create a <b>IS NULL</b> predicate
	 * 
	 * @param field
	 *            the entity field
	 * @return the helper class, useful for method chaining
	 */
	public <T> CriteriaBuilderHelper isNull(final Expression<T> field) {
		predicates.add(field.isNull());
		return this;
	}

	/**
	 * Create a <b>EQUAL</b> predicate for boolean. Threats database NULL values as FALSE
	 * 
	 * @param field
	 *            the entity field
	 * @param value
	 *            the value
	 * @return the helper class, useful for method chaining
	 */
	public CriteriaBuilderHelper equalBool(final Expression<Boolean> field, final Boolean value) {
		if (value != null) {
			if (!value) {
				predicates.add(cb.or(field.isNull(), cb.equal(field, value)));
			} else {
				predicates.add(cb.equal(field, value));
			}
		}
		return this;
	}

	/**
	 * Create a <b>NOT EQUAL</b> predicate for boolean. Threats database NULL values as FALSE
	 * 
	 * @param field
	 *            the entity field
	 * @param value
	 *            the value
	 * @return the helper class, useful for method chaining
	 */
	public CriteriaBuilderHelper notEqualBool(final Expression<Boolean> field, final Boolean value) {
		if (value != null) {
			if (!value) {
				predicates.add(cb.not(cb.or(field.isNull(), cb.equal(field, value))));
			} else {
				predicates.add(cb.not(cb.equal(field, value)));
			}
		}
		return this;
	}

	/**
	 * Create a <b>GREATER THAN OR EQUAL</b> predicate
	 * 
	 * @param field
	 *            the entity field
	 * @param value
	 *            the value
	 * @return the helper class, useful for method chaining
	 */
	public <T extends Comparable<T>> CriteriaBuilderHelper greaterThanOrEqualTo(final Expression<T> field, final T value) {
		if (value != null) {
			predicates.add(cb.greaterThanOrEqualTo(field, value));
		}
		return this;
	}

	/**
	 * Create a <b>LESS THAN OR EQUAL</b> predicate
	 * 
	 * @param field
	 *            the entity field
	 * @param value
	 *            the value
	 * @return the helper class, useful for method chaining
	 */
	public <T extends Comparable<T>> CriteriaBuilderHelper lessThanOrEqualTo(final Expression<T> field, final T value) {
		if (value != null) {
			predicates.add(cb.lessThanOrEqualTo(field, value));
		}
		return this;
	}

	/**
	 * Create a <b>GREATER THAN</b> predicate
	 * 
	 * @param field
	 *            the entity field
	 * @param value
	 *            the value
	 * @return the helper class, useful for method chaining
	 */
	public <T extends Comparable<? super T>> CriteriaBuilderHelper greaterThan(final Expression<T> field, final T value) {
		if (value != null) {
			predicates.add(cb.greaterThan(field, value));
		}
		return this;
	}

	/**
	 * Create a <b>LESS THAN</b> predicate
	 * 
	 * @param field
	 *            the entity field
	 * @param value
	 *            the value
	 * @return the helper class, useful for method chaining
	 */
	public <T extends Comparable<? super T>> CriteriaBuilderHelper lessThan(final Expression<T> field, final T value) {
		if (value != null) {
			predicates.add(cb.lessThan(field, value));
		}
		return this;
	}

	/**
	 * Create a <b>GREATER THAN OR EQUAL</b> predicate for date
	 * 
	 * @param field
	 *            the entity field
	 * @param value
	 *            the value
	 * @return the helper class, useful for method chaining
	 */
	public <T extends Date> CriteriaBuilderHelper greaterThanOrEqualTo(final Expression<T> field, final Date value) {
		if (value != null) {
			predicates.add(cb.greaterThanOrEqualTo(field, value));
		}
		return this;
	}

	/**
	 * Create a <b>LESS THAN OR EQUAL</b> predicate for date
	 * 
	 * @param field
	 *            the entity field
	 * @param value
	 *            the value
	 * @return the helper class, useful for method chaining
	 */
	public <T extends Date> CriteriaBuilderHelper lessThanOrEqualTo(final Expression<T> field, final Date value) {
		if (value != null) {
			predicates.add(cb.lessThanOrEqualTo(field, value));
		}
		return this;
	}

	/**
	 * Create a <b>GREATER THAN</b> predicate for date
	 * 
	 * @param field
	 *            the entity field
	 * @param value
	 *            the value
	 * @return the helper class, useful for method chaining
	 */
	public <T extends Date> CriteriaBuilderHelper greaterThan(final Expression<T> field, final Date value) {
		if (value != null) {
			predicates.add(cb.greaterThan(field, value));
		}
		return this;
	}

	/**
	 * Create a <b>LESS THAN</b> predicate for date
	 * 
	 * @param field
	 *            the entity field
	 * @param value
	 *            the value
	 * @return the helper class, useful for method chaining
	 */
	public <T extends Date> CriteriaBuilderHelper lessThan(final Expression<T> field, final Date value) {
		if (value != null) {
			predicates.add(cb.lessThan(field, value));
		}
		return this;
	}

	/**
	 * Create a <b>BETWEEN</b> predicate for date range
	 * 
	 * @param field
	 *            the entity field
	 * @param range
	 *            the value range
	 * @return the helper class, useful for method chaining
	 */
	public CriteriaBuilderHelper between(final Expression<? extends Date> field, final DateRange range) {
		if (range != null) {
			between(field, range.getStart(), range.getEnd());
		}
		return this;
	}

	/**
	 * Create a <b>BETWEEN</b> predicate for range
	 * 
	 * @param field
	 *            the entity field
	 * @param range
	 *            the value range
	 * @return the helper class, useful for method chaining
	 */
	public <N extends Number & Comparable<N>> CriteriaBuilderHelper between(final Path<N> field, final Range<N> range) {
		if (range != null && !range.isEmpty()) {
			between(field, range.getStart(), range.getEnd());
		}
		return this;
	}

	/**
	 * Create a <b>BETWEEN</b> predicate
	 * 
	 * @param field
	 *            the entity field
	 * @param start
	 *            the lower bound value (inclusive)
	 * @param end
	 *            the upper bound value (inclusive)
	 * @return the helper class, useful for method chaining
	 */
	public <N extends Number & Comparable<N>> CriteriaBuilderHelper between(final Expression<N> field, final N start, final N end) {
		if (start != null && end != null && start.longValue() == end.longValue()) {
			equal(field, start);
		} else {
			if (start != null) {
				greaterThanOrEqualTo(field, start);
			}
			if (end != null) {
				lessThanOrEqualTo(field, end);
			}
		}
		return this;
	}

	/**
	 * Create a <b>BETWEEN</b> predicate for dates
	 * 
	 * @param field
	 *            the entity field
	 * @param from
	 *            the start date (inclusive)
	 * @param to
	 *            the end date (inclusive)
	 * @return the helper class, useful for method chaining
	 */
	public CriteriaBuilderHelper between(final Expression<? extends Date> field, final Date from, final Date to) {
		greaterThanOrEqualTo(field, from);
		lessThanOrEqualTo(field, to);
		return this;
	}

	/**
	 * Joins provided predicates in <b>OR</b> clause.
	 * 
	 * @param field
	 *            the entity field
	 * @return
	 */
	public CriteriaBuilderHelper or(final Predicate... p) {
		if (p != null && p.length > 0) {
			predicates.add(cb.or(p));
		}
		return this;
	}

	/**
	 * Creates a new predicate helper whose predicates are joined by <b>OR</b> clause
	 * <p>
	 * Example:
	 * 
	 * <pre>
	* {@code
	* final Path&lt;AuditableEntity&gt; path = ...;
	* final CriteriaBuilder cb = ...;
	* final CriteriaBuilderHelper cbh = new CriteriaBuilderHelper(cb, Locale.getDefault());
	* cbh.equal(path.get(AuditableEntity_.version), 1);
	* cbh.or()
	*         .equal(path.get(AuditableEntity_.id), 42)
	*         .equal(path.get(AuditableEntity_.id), 24);
	* final Predicate[] p1 = cbh.getPredicatesArray();
	* final Predicate[] p2 = new Predicate[] { 
	*         cb.and(
	*                cb.equal(path.get(AuditableEntity_.version), 1), 
	*                cb.or(
	*                      cb.equal(path.get(AuditableEntity_.id), 42), 
	*                      cb.equal(path.get(AuditableEntity_.id), 24)
	*                      )
	*                )};
	* //p1 and p2 have identical predicates  
	* }
	 * </pre>
	 * 
	 * @return the new predicate helper whose predicates are joined by <b>OR</b> clause
	 */
	public CriteriaBuilderHelper or() {
		final CriteriaBuilderHelper cpbOr = new CriteriaBuilderHelper(cb, locale);
		ors.add(cpbOr);
		return cpbOr;
	}

	/**
	 * Converts string to ignore case string (lower case)
	 * 
	 * @param s
	 *            the string to be converter
	 * @return ignore case string (lower case)
	 */
	public String toIgnoreCaseString(final String s) {
		// see http://mattryall.net/blog/2009/02/the-infamous-turkish-locale-bug
		// In the Turkish alphabet there are two letters for 'i', dotless and dotted. The problem is that the dotless 'i' in
		// lowercase becomes the dotless in uppercase. At first glance this wouldn't appear to be a problem; however, the
		// problem lies in what programmers do with upper- and lowercases in their code.
		//
		// The two lowercase letters are \u0069 (dotted 'i') and \u0131 (dotless 'i') and are totally unrelated.
		// Their uppercase versions are \u0130 (capital letter 'I' with dot above it) and \u0049 (capital letter 'I' without dot).
		// The issue is that this behavior does not occur in English where the single lowercase dotted 'i' becomes an uppercase dotless 'I'.
		//
		// With the statement String.toUppercase(), most Java programmers try to effectively neutralize case. Consider a HashMap with string
		// keys and you have a key that you want to look up. If you want to ignore case, you�ll probably uppercase everything going into the map,
		// its entries, and the string you�re doing the lookup with. This works fine for English, but not for Turkish, where lower dotless becomes capital dotless.
		//
		// Changing the word 'quit' to uppercase in the Turkish locale will result in 'QU[dotted capital I]T', not 'QU[dotless capital I]T' (as in English).
		//
		// Another special case is German 'sharp S' (Eszett) \u00df, which when converted to upper case is represented with double S 'SS'.
		// "\u00df".toUpperCase(Locale.GERMANY) is "SS". That is why when comparing with ignore case it is better to compare with lower case.
		return StringUtils.isEmpty(s) ? null : s.toLowerCase(locale);
	}

	private String replaceSpacesWithWildcard(final String s) {
		return StringUtils.replaceWithSingleSpace(StringUtils.defaultString(s)).replace(' ', '%');
	}

	private String escapeLikeTerms(final String s, final char escapeChar) {
		// @formatter:off
		return StringUtils.replaceWithSingleSpace(StringUtils.defaultString(s))
				.replace(escapeChar + "", escapeChar + "" + escapeChar)
				.replace("%", escapeChar + "%")
				.replace("_", escapeChar + "_");
		// @formatter:on
	}

	private String prepareLikePattern(final String text, char escapeChar) {
		if (escapeChar == '%' || escapeChar == '_') {
			throw new IllegalArgumentException("Escape character cannot be '%' or '_'");
		}
		return replaceSpacesWithWildcard(escapeLikeTerms(toIgnoreCaseString(text).trim(), escapeChar));
	}

	/**
	 * Creates a like pattern that can be used for {@link #like(Expression, String)} predicates for matching the <tt>text</tt>
	 * <p>
	 * The algorithm for creating the pattern is following:
	 * <ol>
	 * <li>Escape all '%'</li>
	 * <li>Escape all '_'</li>
	 * <li>Reduce all spaces in <tt>text</tt> to a single space</li>
	 * <li>Replace all spaces with '%'</li>
	 * <li>Returns the string concatenated with '%' at the beginning and the end</li>
	 * </ol>
	 * Example:
	 * 
	 * <pre>
	 * {@code
	 * final CriteriaBuilderHelper cbh = ...;
	 * final String pattern1 = cbh.createLikePattern("   hello    \\%_   world   ", '\\');
	 * final String pattern2 = "%hello%\\\\\\%\\_%world%";
	 * // pattern1 is the same as pattern2
	 * }
	 * </pre>
	 * 
	 * @param text
	 *            text to converted to pattern
	 * @param escapeChar
	 *            the escape character
	 * @return like pattern
	 */
	public String createLikePattern(final String text, char escapeChar) {
		return StringUtils.isBlank(text) ? "" : "%" + prepareLikePattern(text, escapeChar) + "%";
	}

	/**
	 * Creates a like pattern that can be used for {@link #like(Expression, String)} predicates for matching the strings starting with <tt>text</tt>
	 * <p>
	 * The algorithm for creating the pattern is following:
	 * <ol>
	 * <li>Escape all '%'</li>
	 * <li>Escape all '_'</li>
	 * <li>Reduce all spaces in <tt>text</tt> to a single space</li>
	 * <li>Replace all spaces with '%'</li>
	 * <li>Returns the string concatenated with '%' at the end</li>
	 * </ol>
	 * Example:
	 * 
	 * <pre>
	 * {@code
	 * final CriteriaBuilderHelper cbh = ...;
	 * final String pattern1 = cbh.createLikePattern("   hello    \\%_   world   ", '\\');
	 * final String pattern2 = "hello%\\\\\\%\\_%world%";
	 * // pattern1 is the same as pattern2
	 * }
	 * </pre>
	 * 
	 * @param text
	 *            text to converted to pattern
	 * @param escapeChar
	 *            the escape character
	 * @return like pattern
	 */
	public String createStartsWithPattern(final String text, char escapeChar) {
		return StringUtils.isBlank(text) ? "" : prepareLikePattern(text, escapeChar) + "%";
	}

	/**
	 * Creates a like pattern that can be used for {@link #like(Expression, String)} predicates for matching the strings ending with <tt>text</tt>
	 * <p>
	 * The algorithm for creating the pattern is following:
	 * <ol>
	 * <li>Escape all '%'</li>
	 * <li>Escape all '_'</li>
	 * <li>Reduce all spaces in <tt>text</tt> to a single space</li>
	 * <li>Replace all spaces with '%'</li>
	 * <li>Returns the string concatenated with '%' at the beginning</li>
	 * </ol>
	 * Example:
	 * 
	 * <pre>
	 * {@code
	 * final CriteriaBuilderHelper cbh = ...;
	 * final String pattern1 = cbh.createLikePattern("   hello    \\%_   world   ", '\\');
	 * final String pattern2 = "%hello%\\\\\\%\\_%world";
	 * // pattern1 is the same as pattern2
	 * }
	 * </pre>
	 * 
	 * @param text
	 *            text to converted to pattern
	 * @param escapeChar
	 *            the escape character
	 * @return like pattern
	 */
	public String createEndsWithPattern(final String text, char escapeChar) {
		return StringUtils.isBlank(text) ? "" : "%" + prepareLikePattern(text, escapeChar);
	}

	/**
	 * Returns a list of predicates
	 * 
	 * @return a list of predicates
	 */
	public List<Predicate> getPredicates() {
		for (final CriteriaBuilderHelper cpbOr : ors) {
			or(cpbOr.getPredicatesArray());
		}
		ors.clear();
		return predicates; // NOSONAR this is supposed to be modifiable from
	}

	/**
	 * Returns an array of predicates
	 * 
	 * @return an array of predicates
	 */
	public Predicate[] getPredicatesArray() {
		return getPredicates().stream().toArray(Predicate[]::new);
	}

	/**
	 * Returns the locale
	 * 
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Returns the criteria builder
	 * 
	 * @return the criteria builder
	 */
	public CriteriaBuilder getCriteriaBuilder() {
		return cb;
	}

}
