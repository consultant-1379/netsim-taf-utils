/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.cifwk.netsim.taf

public class ListFiltration {

	/**
	 * @param listToBeEvenlyDistributed A List of items to be evenly distributed
	 * @param percentage The percentage of items that should be evenly distributed
	 * @param offset The index of the even distribution
	 * @return Evenly distributed List of items of appropriate type
	 */
	public static <T> List<T> filterEvenDistributionPercentage(List<T> listToBeEvenlyDistributed, double percentage, int offset=0) {
		List<T> filteredList = []
		int indexOfList = 0
		List<T> tempList = new ArrayList<T>(listToBeEvenlyDistributed)
		Collections.rotate(tempList,offset)

		for(int i=1; indexOfList<tempList.size(); i++) {
			filteredList.add(tempList.get(indexOfList))
			indexOfList = (int) Math.round((1 / (percentage / 100)) * i)
		}
		return filteredList
	}
}
