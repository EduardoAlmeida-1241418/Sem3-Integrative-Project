package pt.ipp.isep.dei.controller.operationsAnalyst;

import pt.ipp.isep.dei.controller.algorithms.EdmondsKarpAlgorithm;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint3.EdgeEsinfRepository;
import pt.ipp.isep.dei.data.repository.sprint3.NodeEsinfRepository;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;
import pt.ipp.isep.dei.domain.Graph.Node;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller used by the Operations Analyst UI to calculate network metrics such as max-flow.
 * <p>
 * This class mediates between the UI and the data repositories. It resolves station inputs
 * (either typed IDs or table selections), builds the capacity matrix from repository edges
 * and delegates the max-flow computation to the Edmonds-Karp implementation.
 */
public class OperationsAnalystController {

    /** Repository providing graph nodes (stations) for the ESINF graph representation. */
    private final NodeEsinfRepository nodeRepository;
    /** Repository providing graph edges (with metrics) for the ESINF graph representation. */
    private final EdgeEsinfRepository edgeRepository;

    /** Currently selected source station. */
    private StationEsinf source;
    /** Currently selected target station. */
    private StationEsinf target;

    /**
     * Default constructor. Retrieves repositories from the shared Repositories singleton.
     */
    public OperationsAnalystController() {
        this.nodeRepository = Repositories.getInstance().getNodeEsinfRepository();
        this.edgeRepository = Repositories.getInstance().getEdgeEsinfRepository();
    }

    /**
     * Calculates the maximum flow between two stations using the Edmonds-Karp algorithm.
     *
     * @param source the source station (must not be null)
     * @param target the target station (must not be null and must be different from source)
     * @return the computed maximum flow value as an int
     * @throws IllegalArgumentException if either parameter is null, if they are equal, or
     *                                  if either station cannot be mapped to a node index
     */
    public int calculateMaxFlow(StationEsinf source, StationEsinf target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target must not be null.");
        }
        if (source.equals(target)) {
            throw new IllegalArgumentException("Source and target must be different.");
        }

        List<Node<StationEsinf>> nodes = nodeRepository.getAllNodes();
        int n = nodes.size();

        Map<StationEsinf, Integer> nodeIndexMap = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            nodeIndexMap.put(nodes.get(i).getValue(), i);
        }

        int[][] capacity = new int[n][n];

        List<Edge<StationEsinf, MetricsStationEdge>> edges = edgeRepository.getAllEdges();
        for (Edge<StationEsinf, MetricsStationEdge> edge : edges) {
            StationEsinf uNode = edge.getVOrig();
            StationEsinf vNode = edge.getVDest();
            Integer startNode = nodeIndexMap.get(uNode);
            Integer endNode = nodeIndexMap.get(vNode);
            if (startNode != null && endNode != null) {
                capacity[startNode][endNode] += edge.getWeight().getCapacity();
            }
        }

        Integer sourceIndex = nodeIndexMap.get(source);
        Integer targetIndex = nodeIndexMap.get(target);
        if (sourceIndex == null || targetIndex == null) {
            throw new IllegalArgumentException("Invalid source or target station.");
        }

        return EdmondsKarpAlgorithm.edmondsKarp(capacity, sourceIndex, targetIndex);
    }

    /**
     * Returns a list with all stations currently present in the node repository.
     *
     * @return a List of StationEsinf instances
     */
    public List<StationEsinf> getStations() {
        List<StationEsinf> list = new ArrayList<>();
        List<Node<StationEsinf>> nodes = nodeRepository.getAllNodes();
        for (Node<StationEsinf> graphNode : nodes) {
            list.add(graphNode.getValue());
        }
        return list;
    }

    /**
     * Finds a station by its identifier string.
     *
     * @param stationId the station id to look for (may be null)
     * @return the matching StationEsinf or null if not found or if stationId is null
     */
    public StationEsinf getStationById(String stationId) {
        if (stationId == null) return null;
        for (StationEsinf s : getStations()) {
            if (stationId.equals(s.getId())) return s;
        }
        return null;
    }


    /**
     * Convenience method that accepts a mix of textual IDs and table-selected stations,
     * resolves them to concrete StationEsinf instances, performs validation and computes the max flow.
     *
     * The resolution rules are:
     * - if a non-empty ID string is provided it is used (and must resolve to an existing station)
     * - otherwise, if a table-selected StationEsinf is provided it is used
     * - otherwise, if the controller already has a previously selected station it is used
     * - otherwise an IllegalArgumentException is thrown requesting selection or input
     *
     * @param srcId      optional textual ID for the source station
     * @param tgtId      optional textual ID for the target station
     * @param srcSelected optional selected StationEsinf for the source (may be null)
     * @param tgtSelected optional selected StationEsinf for the target (may be null)
     * @return the computed max flow value
     * @throws IllegalArgumentException if validation fails or if both stations are the same
     */
    public int calculateMaxFlowFromInputs(String srcId, String tgtId, StationEsinf srcSelected, StationEsinf tgtSelected) {
        final StationEsinf resolvedSource = resolveStation(srcId, srcSelected, this.source, "Initial");
        final StationEsinf resolvedTarget = resolveStation(tgtId, tgtSelected, this.target, "Final");

        if (resolvedSource.getId().equals(resolvedTarget.getId())) {
            throw new IllegalArgumentException("Initial and final stations cannot be the same.");
        }

        this.source = resolvedSource;
        this.target = resolvedTarget;

        int result = calculateMaxFlow(resolvedSource, resolvedTarget);

        UIUtils.addLog(String.format("Calculated max flow from %s(%s) to %s(%s) = %d",
                resolvedSource.getStationName(), resolvedSource.getId(),
                resolvedTarget.getStationName(), resolvedTarget.getId(),
                result),
            LogType.INFO, RoleType.OPERATIONS_ANALYST);

        return result;
    }

    /**
     * Resolves a station from the combination of an optional textual ID, an optional selected station
     * and an optional previously stored station.
     *
     * @param id      the optional textual ID to resolve (may be null or empty)
     * @param selected the optional station selected in the UI (may be null)
     * @param previous the previously stored station in the controller (may be null)
     * @param roleNameForError a short label used in the error message (e.g. "Initial" or "Final")
     * @return the resolved StationEsinf instance
     * @throws IllegalArgumentException if the textual id does not exist or if no input is available
     */
    private StationEsinf resolveStation(String id, StationEsinf selected, StationEsinf previous, String roleNameForError) {
        if (id != null && !id.isEmpty()) {
            StationEsinf s = getStationById(id);
            if (s == null) {
                throw new IllegalArgumentException(roleNameForError + " station doesn't exist.");
            }
            return s;
        }
        if (selected != null) return selected;
        if (previous != null) return previous;
        throw new IllegalArgumentException("Select source and target stations or enter their IDs.");
    }

    /**
     * Sets the controller's source station.
     *
     * @param source the StationEsinf to set as source (may be null)
     */
    public void setSource(StationEsinf source) {
        this.source = source;
    }

    /**
     * Returns the currently stored source station.
     *
     * @return the source StationEsinf or null if none set
     */
    public StationEsinf getSource() {
        return this.source;
    }

    /**
     * Sets the controller's target station.
     *
     * @param target the StationEsinf to set as target (may be null)
     */
    public void setTarget(StationEsinf target) {
        this.target = target;
    }

    /**
     * Returns the currently stored target station.
     *
     * @return the target StationEsinf or null if none set
     */
    public StationEsinf getTarget() {
        return this.target;
    }

    /**
     * Returns whether both source and target stations have been selected.
     *
     * @return true if both source and target are not null, false otherwise
     */
    public boolean hasSelectedSourceAndTarget() {
        return this.source != null && this.target != null;
    }
}
