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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.cifwk.taf.handlers.netsim.domain.NeGroup;
import com.ericsson.cifwk.taf.handlers.netsim.domain.NetworkElement;

public class NetworkFiltrationTest {

	NeGroup allNEs;

	@Before
	public void setup() {

		NetworkElement ne1 = mock(NetworkElement.class);
		NetworkElement ne2 = mock(NetworkElement.class);
		NetworkElement ne3 = mock(NetworkElement.class);
		NetworkElement ne4 = mock(NetworkElement.class);

		when(ne1.getNodeType()).thenReturn("RNC");
		when(ne2.getNodeType()).thenReturn("ERBS");
		when(ne3.getNodeType()).thenReturn("RBS");
		when(ne4.getNodeType()).thenReturn("ERBS");

		allNEs = new NeGroup(ne1, ne2, ne3, ne4);
	}

	@Test
	public void testFilterNEsByNodeType() {
		NeGroup filteredByTypeERBS = NetworkFiltration.filterNEsByNodeType(allNEs, "ERBS");
		assert 2 == filteredByTypeERBS.size();
	}

}
