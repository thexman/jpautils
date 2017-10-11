package com.a9ski.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Locale;

import org.junit.Test;

public class CriteriaBuilderHelperTest {

	private final static Locale LOCALE_TR = new Locale("tr", "TR");

	@Test
	public void testToIgnoreCaseString() {
		final String quitUpperCase = "QUIT";
		final String quitLowerCase = "quit";
		final String bullshxxx = "schei\u00dfe";

		final CriteriaBuilderHelper cphTr = new CriteriaBuilderHelper(null, LOCALE_TR);
		final CriteriaBuilderHelper cphEn = new CriteriaBuilderHelper(null, Locale.ENGLISH);
		final CriteriaBuilderHelper cphDe = new CriteriaBuilderHelper(null, Locale.GERMANY);

		// turkish I (dotless) and i (dotless)
		assertNotEquals(quitLowerCase, cphTr.toIgnoreCaseString(quitUpperCase));
		assertEquals(quitLowerCase, cphEn.toIgnoreCaseString(quitUpperCase));

		// german sharp-s and SS
		assertNotEquals(bullshxxx, cphDe.toIgnoreCaseString(bullshxxx.toUpperCase(Locale.GERMANY)));
		assertEquals(bullshxxx, cphDe.toIgnoreCaseString("SCHEI\u00dfE"));
	}

	@Test
	public void testCreateLikePattern() {
		final CriteriaBuilderHelper cph = new CriteriaBuilderHelper(null, Locale.getDefault());
		assertEquals("%hello%\\\\\\%\\_%world%", cph.createLikePattern("   Hello    \\%_   world   ", '\\'));
		assertEquals("%simple%search%text%", cph.createLikePattern("simple search text", '\\'));
	}

	@Test
	public void testCreateStartsWithPattern() {
		final CriteriaBuilderHelper cph = new CriteriaBuilderHelper(null, Locale.getDefault());
		assertEquals("hello%\\\\\\%\\_%world%", cph.createStartsWithPattern("   Hello    \\%_   world   ", '\\'));
		assertEquals("simple%search%text%", cph.createStartsWithPattern("simple search text", '\\'));
	}

	@Test
	public void testCreateEndsWithPattern() {
		final CriteriaBuilderHelper cph = new CriteriaBuilderHelper(null, Locale.getDefault());
		assertEquals("%hello%\\\\\\%\\_%world\\%", cph.createEndsWithPattern("   Hello    \\%_   world%   ", '\\'));
		assertEquals("%simple%search%text\\%", cph.createEndsWithPattern("simple search text%", '\\'));
	}

}
