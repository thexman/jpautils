package com.a9ski.jpa;

/**
 * Interface representing a database dialect - some database specific functionality
 * 
 * @author Kiril Arabadzhiyski
 *
 */
public interface DatabaseDialect {
	/**
	 * Generates the next value of a sequence
	 * 
	 * @param sequenceName
	 *            the sequence name
	 * @return the next value of the sequence
	 */
	public String createSequenceNextValueSql(final String sequenceName);
}
