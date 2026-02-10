package pt.ipp.isep.dei.controller.infrastructurePlanner;

import pt.ipp.isep.dei.controller.algorithms.KahnAlgorithm;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint3.EdgeEsinfRepository;
import pt.ipp.isep.dei.data.repository.sprint3.NodeEsinfRepository;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.Graph;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;
import pt.ipp.isep.dei.domain.Graph.Node;

import java.util.*;

/**
 * Controller responsável pelo planeamento de upgrades da rede ferroviária.
 *
 * Permite analisar dependências entre estações utilizando ordenação topológica,
 * identificar ciclos e determinar uma ordem válida de execução dos upgrades.
 */
public class RailwayUpgradePlanningController {

    /**
     * Grafo direcionado que representa as dependências entre estações.
     */
    private Graph<StationEsinf, MetricsStationEdge> graph;

    /**
     * Resultado do cálculo de ordenação topológica.
     */
    private TopologicalResult<StationEsinf> result;

    /**
     * Construtor do controller.
     *
     * Inicializa o grafo a partir dos repositórios.
     */
    public RailwayUpgradePlanningController() {
        createGraph();
    }

    /**
     * Indica se o último plano calculado contém ciclos.
     *
     * @return true se existir ciclo, false caso contrário
     */
    public boolean hasCycle() {
        return result.hasCycle();
    }

    /**
     * Obtém a ordem de upgrade calculada.
     *
     * @return lista ordenada de estações
     */
    public List<StationEsinf> getUpgradeOrder() {
        return result.getOrder();
    }

    /**
     * Obtém o conjunto de estações envolvidas em ciclos.
     *
     * @return conjunto de estações em ciclo
     */
    public Set<StationEsinf> getCycleStations() {
        return result.getCycleStations();
    }

    /**
     * Obtém o conjunto de arestas que fazem parte dos ciclos.
     *
     * @return conjunto de representações textuais das arestas em ciclo
     */
    public Set<String> getCycleEdges() {
        return result.getCycleEdges();
    }

    /**
     * Classe auxiliar que encapsula o resultado da ordenação topológica.
     *
     * @param <V> tipo do vértice
     */
    public static class TopologicalResult<V> {

        private final boolean hasCycle;
        private final List<V> order;
        private final Set<V> cycleStations;
        private final Set<String> cycleEdges;

        /**
         * Construtor do resultado topológico.
         *
         * @param hasCycle indica se existe ciclo
         * @param order ordem topológica calculada
         * @param cycleStations conjunto de vértices em ciclo
         * @param cycleEdges conjunto de arestas em ciclo
         */
        public TopologicalResult(boolean hasCycle, List<V> order, Set<V> cycleStations, Set<String> cycleEdges) {
            this.hasCycle = hasCycle;
            this.order = order;
            this.cycleStations = cycleStations;
            this.cycleEdges = cycleEdges;
        }

        public boolean hasCycle() {
            return hasCycle;
        }

        public List<V> getOrder() {
            return order;
        }

        public Set<V> getCycleStations() {
            return cycleStations;
        }

        public Set<String> getCycleEdges() {
            return cycleEdges;
        }
    }

    /**
     * Cria o grafo direcionado com base nos repositórios de nós e arestas.
     */
    // Complexity O(V) + O(E) = O(V+E)
    public void createGraph() {
        graph = new Graph<>(true);

        NodeEsinfRepository nodeRepo = Repositories.getInstance().getNodeEsinfRepository();
        EdgeEsinfRepository edgeRepo = Repositories.getInstance().getEdgeEsinfRepository();

        for (Node<StationEsinf> node : nodeRepo.getAllNodes()) {
            graph.addVertex(node.getValue(), node.getKey()); // Complexity O(V)
        }

        for (Edge<StationEsinf, MetricsStationEdge> edge : edgeRepo.getAllEdges()) {
            graph.addEdge(edge.getVOrig(), edge.getVDest(), edge.getWeight()); // Complexity O(E)
        }
    }

    /**
     * Calcula o plano de upgrade da rede ferroviária.
     *
     * O cálculo pode ser feito sobre o grafo completo,
     * um intervalo de IDs ou uma única estação.
     *
     * @param mode modo de análise
     * @param fromId ID inicial (modo intervalo)
     * @param toId ID final (modo intervalo)
     * @param singleId ID da estação (modo estação única)
     */
    // Complexity O(V+E) + O(V^2) + O(E) = O(V^2 + E)
    public void computeUpgradePlan(String mode, String fromId, String toId, String singleId) {

        Graph<StationEsinf, MetricsStationEdge> target;

        if ("ID Interval".equals(mode)) {
            target = buildIdIntervalGraph(fromId, toId);
        } else if ("Single Station Analysis".equals(mode)) {
            target = buildSingleStationGraph(singleId);
        } else {
            target = graph;
        }

        List<StationEsinf> order = new ArrayList<>();
        boolean acyclic = KahnAlgorithm.kahn(target, order); // Complexity O(V+E)

        Set<StationEsinf> cycleStations = new HashSet<>();
        Set<String> cycleEdges = new HashSet<>();

        if (!acyclic) {

            // Complexity O(V^2)
            for (StationEsinf v : target.vertices()) {  // Complexity O(V)
                if (!order.contains(v)) {               // Complexity O(V)
                    cycleStations.add(v);
                }
            }

            // Outer loop: O(V)
            // Inner loop over adjacencies (total over all u): O(E)
            // Total: O(V + E)
            for (StationEsinf u : cycleStations) {
                for (StationEsinf v : target.adjVertices(u)) {
                    if (cycleStations.contains(v)) {
                        cycleEdges.add(u.getStationName() + " -> " + v.getStationName());
                    }
                }
            }
        }

        result = new TopologicalResult<>(!acyclic, order, cycleStations, cycleEdges);
    }

    /**
     * Constrói um subgrafo baseado num intervalo de IDs.
     *
     * @param fromId ID inicial
     * @param toId ID final
     * @return grafo filtrado por intervalo
     */
    private Graph<StationEsinf, MetricsStationEdge> buildIdIntervalGraph(String fromId, String toId) {
        Graph<StationEsinf, MetricsStationEdge> g = new Graph<>(true);

        if (fromId == null || toId == null || fromId.isEmpty() || toId.isEmpty()) {
            return g;
        }

        int min = Integer.parseInt(fromId);
        int max = Integer.parseInt(toId);

        for (StationEsinf v : graph.vertices()) { // Complexity O(V)
            int id = Integer.parseInt(v.getId());
            if (id >= min && id <= max) {
                g.addVertex(v, v.getId());
            }
        }

        // Complexity O(V+E)
        for (StationEsinf u : g.vertices()) { // Complexity O(V)
            for (StationEsinf v : graph.adjVertices(u)) { // Complexity O(E)
                if (g.vertexByKey(v.getId()) != null) {
                    g.addEdge(u, v, graph.edge(u, v).getWeight());
                }
            }
        }

        return g;
    }

    /**
     * Constrói um subgrafo centrado numa única estação.
     *
     * @param id ID da estação central
     * @return grafo centrado na estação
     */
    private Graph<StationEsinf, MetricsStationEdge> buildSingleStationGraph(String id) { // Complexity O(E)
        Graph<StationEsinf, MetricsStationEdge> g = new Graph<>(true);

        if (id == null || id.isEmpty()) {
            return g;
        }

        StationEsinf center = graph.vertexByKey(id);
        if (center == null) {
            return g;
        }

        g.addVertex(center, center.getId());

        for (StationEsinf v : graph.adjVertices(center)) { // Complexity O(E)
            g.addVertex(v, v.getId());
            g.addEdge(center, v, graph.edge(center, v).getWeight());
        }

        return g;
    }
}
