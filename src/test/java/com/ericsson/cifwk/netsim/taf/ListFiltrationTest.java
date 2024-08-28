/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.cifwk.netsim.taf;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ListFiltrationTest {

	ArrayList<Integer> integers;
	LinkedList<String> strings;

	@Before
	public void setup() {
		integers = new ArrayList<>();
		strings = new LinkedList<>();

		for (int i=0; i<200; i++) {
			integers.add(i);
			strings.add(Integer.toHexString(i));
		}
	}

	@Test
	public void testFilterEvenDistributionPercentageWithIntPercent() {
		List<Integer> halfOfIntegers = ListFiltration.filterEvenDistributionPercentage(integers, 50);
		List<Integer> halfOfIntegersOffset = ListFiltration.filterEvenDistributionPercentage(integers, 50, 1);

		List<Integer> evenInts = new ArrayList<>();
		List<Integer> oddInts = new ArrayList<>();
		for (Integer i : integers) {
			if (i % 2 == 0) {
				evenInts.add(i);
			} else {
				oddInts.add(i);
			}
		}

		assertEquals(integers.size() / 2, new HashSet<>(halfOfIntegers).size());
		assertEquals(integers.size() / 2, new HashSet<>(halfOfIntegersOffset).size());

		assertTrue(halfOfIntegers.containsAll(evenInts));
		assertTrue(halfOfIntegersOffset.containsAll(oddInts));

		// Equals won't work before sort when offsetted, because of rotation
		Collections.sort(halfOfIntegersOffset);
		assertEquals(oddInts, halfOfIntegersOffset);
	}

	@Test
	public void testFilterEvenDistributionPercentageWithDoublePercent() {
		final double PERCENT = 1.01;
		List<String> quartOfStrings = ListFiltration.filterEvenDistributionPercentage(strings, PERCENT, 2);

		assertEquals((int)Math.ceil(strings.size() * (PERCENT/100)), new HashSet<>(quartOfStrings).size());
	}
}
