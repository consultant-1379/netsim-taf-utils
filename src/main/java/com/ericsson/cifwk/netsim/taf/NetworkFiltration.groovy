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
package com.ericsson.cifwk.netsim.taf

import org.apache.log4j.Logger

import com.ericsson.cifwk.taf.handlers.netsim.NetSimCommandHandler
import com.ericsson.cifwk.taf.handlers.netsim.NetSimContext
import com.ericsson.cifwk.taf.handlers.netsim.NetSimResult
import com.ericsson.cifwk.taf.handlers.netsim.commands.NetSimCommands
import com.ericsson.cifwk.taf.handlers.netsim.domain.NeGroup
import com.ericsson.cifwk.taf.handlers.netsim.domain.NetworkElement

public class NetworkFiltration {

	private static final Logger log = Logger.getLogger(NetworkFiltration.class)

	/**
	 * Method to get a list of the started NEs in an NEGroup
	 * @param netSimCommandHandler A NetSimCommandHandler for all the hosts that cover {@param allNEs}
	 * @param allNEs The NEGroup to get the started NEs from
	 * @return NeGroup of all started nodes in the simulated network
	 */
	public static NeGroup getAllStartedNEs(NetSimCommandHandler netSimCommandHandler, NeGroup allNEs) {
		/* TODO Remove this method when http://jira-oss.lmera.ericsson.se/browse/CIP-4043 is Done */

		NeGroup startedNEs = new NeGroup()
		Map<NetSimContext, NetSimResult> startedCmdResults = netSimCommandHandler.exec(NetSimCommands.showStarted())
		Collection<NetSimResult> neResults = startedCmdResults.values()

		neResults.each { neResult ->
			neResult.getOutput().each { commandOutput ->
				commandOutput.asSections().each { sectionHeading, rowsOfStartedNodesInfo ->
					rowsOfStartedNodesInfo.each { rowOfStartedNodeInfo ->
						String networkElementName = rowOfStartedNodeInfo.get("NE")
						NetworkElement networkElement = allNEs.get(networkElementName)
						if (networkElement != null) {
							startedNEs.add(networkElement)
						}
					}
				}
			}
		}
		return startedNEs
	}

	/**
	 * Return an NeGroup of all NetworkElements within neGroup, whose 'nodeType' property is equal to the provided filter
	 * @param neGroup An neGroup instance to filter
	 * @param filter The string that needs to be present in the 'nodeType' property of each NetworkElement in the returned NeGroup
	 * @return An NeGroup
	 */
	public static NeGroup filterNEsByNodeType(NeGroup neGroup, String nodeType) {
		return neGroup.getNetworkElements().findAll {
			it.getNodeType() == nodeType
		}
	}

	/**
	 * The requested percent of Network Elements of the requested node type ensuring they are started
	 * @param nodeType Type of NE to filter on
	 * @param percentage Amount of NEs of type nodeType to return
	 * @return NeGroup
	 */
	public static NeGroup getStartedNEsOfNodeType(NetSimCommandHandler netSimCommandHandler, NeGroup allNEs,
			String nodeType, double percentage, int offset) {
		NeGroup startedNEs = getAllStartedNEs(netSimCommandHandler, allNEs)
		NeGroup allNodeTypeNEs = filterNEsByNodeType(allNEs, nodeType)
		NeGroup startedNodeTypeNEs = filterNEsByNodeType(startedNEs, nodeType)
		NeGroup percentageStartedNodeTypeNEs = new NeGroup()
		double requiredPercentageOfStarted

		if (startedNodeTypeNEs.size() < allNodeTypeNEs.size()) {
			log.warn("Not all nodes are started - ${startedNodeTypeNEs.size()} out of a possible " +
					 "${allNodeTypeNEs.size()} are started. Filtering on started nodes only")
		}

		if (startedNodeTypeNEs.size() > 0) {
			requiredPercentageOfStarted = percentage * (allNodeTypeNEs.size() / startedNodeTypeNEs.size())
		}

		if (requiredPercentageOfStarted > 100 || startedNodeTypeNEs.size() == 0) {
			log.error("Not enough Network Elements started to fulfill test case requirements")
		} else {
			List<NetworkElement> filtered = ListFiltration.filterEvenDistributionPercentage(startedNodeTypeNEs.getNetworkElements(), requiredPercentageOfStarted, offset)
			percentageStartedNodeTypeNEs = new NeGroup(filtered)
		}
		return percentageStartedNodeTypeNEs
	}
}
