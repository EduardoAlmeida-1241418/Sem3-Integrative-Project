package pt.ipp.isep.dei.controller.routePlanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.graphstream.graph.implementations.SingleGraph;
import pt.ipp.isep.dei.controller.algorithms.BellmanFordAlgorithm;
import pt.ipp.isep.dei.controller.graphviz.GraphvizCreateFiles;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint3.EdgeEsinfRepository;
import pt.ipp.isep.dei.data.repository.sprint3.NodeEsinfRepository;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.Graph;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;
import pt.ipp.isep.dei.domain.Graph.Node;

import java.io.IOException;
import java.util.*;

/**
 * Controller responsável pelo cálculo do caminho mais curto
 * entre duas estações da rede ferroviária.
 *
 * Utiliza o algoritmo de Bellman-Ford, permitindo detetar ciclos
 * negativos e gerar representações gráficas dos resultados.
 */
public class ShortestPathController {

    /**
     * Repositório de arestas ESINF.
     */
    private EdgeEsinfRepository edgeEsinfRepository;

    /**
     * Repositório de nós ESINF.
     */
    private NodeEsinfRepository nodeEsinfRepository;

    /**
     * Grafo direcionado da rede ferroviária.
     */
    private Graph<StationEsinf, MetricsStationEdge> graph;

    /**
     * Subgrafo resultante do caminho mais curto.
     */
    private Graph<StationEsinf, MetricsStationEdge> subgraphResult;

    /**
     * Lista de arestas pertencentes a ciclos negativos.
     */
    private List<Edge<StationEsinf, MetricsStationEdge>> negativeCycleEdges;

    /**
     * Identificador da estação de origem.
     */
    private String vOrigKey;

    /**
     * Identificador da estação de destino.
     */
    private String vDestKey;

    /**
     * Construtor do controller.
     *
     * Inicializa os repositórios e constrói o grafo.
     */
    public ShortestPathController() {
        edgeEsinfRepository = Repositories.getInstance().getEdgeEsinfRepository();
        nodeEsinfRepository = Repositories.getInstance().getNodeEsinfRepository();
        createGraph();
    }

    public String getvOrigKey() {
        return vOrigKey;
    }

    public void setvOrigKey(String vOrigKey) {
        this.vOrigKey = vOrigKey;
    }

    public String getvDestKey() {
        return vDestKey;
    }

    public void setvDestKey(String vDestKey) {
        this.vDestKey = vDestKey;
    }

    /**
     * Verifica se uma estação existe no grafo.
     *
     * @param stationKey chave da estação
     * @return true se existir, false caso contrário
     */
    public boolean existsStation(String stationKey) {
        return graph.existVertexByKey(stationKey);
    }

    public Graph<StationEsinf, MetricsStationEdge> getGraph() {
        return graph;
    }

    /**
     * Cria o grafo direcionado da rede ferroviária.
     */
    public void createGraph() {
        graph = new Graph<>(true);
        addVertices();
        addEdges();
    }

    private void addVertices() {
        for (Node<StationEsinf> node : nodeEsinfRepository.getAllNodes()) {
            graph.addVertex(node.getValue(), node.getKey());
        }
    }

    private void addEdges() {
        for (Edge<StationEsinf, MetricsStationEdge> edge : edgeEsinfRepository.getAllEdges()) {
            graph.addEdge(edge.getVOrig(), edge.getVDest(), edge.getWeight());
        }
    }

    /**
     * Calcula e define o subgrafo resultante do caminho mais curto.
     */
    public void setSubgraphResult() {
        this.subgraphResult = computeShortestPath();
    }

    public Graph<StationEsinf, MetricsStationEdge> getSubgraphResult() {
        return subgraphResult;
    }

    /**
     * Executa o algoritmo de Bellman-Ford e constrói o subgrafo do caminho mais curto.
     *
     * @return subgrafo resultante
     */
    private Graph<StationEsinf, MetricsStationEdge> computeShortestPath() {
        StationEsinf origin = graph.vertexByKey(vOrigKey);
        StationEsinf dest   = graph.vertexByKey(vDestKey);

        if (origin == null || dest == null) {
            return new Graph<>(true);
        }

        int[] path = new int[graph.numVertices()];
        double[] dist = new double[graph.numVertices()];

        negativeCycleEdges = BellmanFordAlgorithm.bellmanFord(graph, origin, path, dist);

        negativeCycleEdges = orderEdgesList(negativeCycleEdges);

        return createSubgraphFromPath(path, origin, dest);
    }

    /**
     * Cria um subgrafo a partir do vetor de predecessores.
     *
     * @param path vetor de predecessores
     * @param vOrig estação de origem
     * @param vDest estação de destino
     * @return subgrafo correspondente ao caminho
     */
    public Graph<StationEsinf, MetricsStationEdge> createSubgraphFromPath(int[] path, StationEsinf vOrig, StationEsinf vDest) {

        Graph<StationEsinf, MetricsStationEdge> sub = new Graph<>(true);

        int dest = graph.key(vDest);
        int orig = graph.key(vOrig);

        if (dest == -1 || orig == -1) return sub;

        List<Integer> order = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        int curr = dest;

        while (curr != -1 && !visited.contains(curr)) {
            visited.add(curr);
            order.add(curr);
            if (curr == orig) break;
            curr = path[curr];
        }

        if (curr != orig) return sub;

        Collections.reverse(order);

        for (int k : order) {
            StationEsinf st = graph.vertex(k);
            sub.addVertex(st, st.getId());
        }

        for (int i = 0; i < order.size() - 1; i++) {
            StationEsinf u = graph.vertex(order.get(i));
            StationEsinf v = graph.vertex(order.get(i + 1));
            MetricsStationEdge w = graph.edge(u, v).getWeight();
            sub.addEdge(u, v, w);
        }

        return sub;
    }

    /**
     * Constrói uma representação GraphStream do grafo.
     *
     * @param graph grafo a representar
     * @return grafo GraphStream
     */
    public SingleGraph getGraphStreamRepresentation(Graph<StationEsinf, MetricsStationEdge> graph) {
        if (graph == null || graph.getEdgesList().isEmpty()) {
            return new SingleGraph("Empty Graph");
        }

        SingleGraph singleGraph = new SingleGraph("Shortest Path Graph");

        for (Node<StationEsinf> node : graph.getVerticesNodes()) {
            singleGraph.addNode(node.getKey())
                    .setAttribute("ui.label", node.getValue().getStationName());
        }

        for (Edge<StationEsinf, MetricsStationEdge> edge : graph.getEdgesList()) {
            String edgeId = edge.getVOrig().getId() + "-" + edge.getVDest().getId();
            String cost = String.format("%.3f", edge.getWeight().getCost());
            singleGraph.addEdge(edgeId, edge.getVOrig().getId(), edge.getVDest().getId(), true)
                    .setAttribute("ui.label", cost);
        }

        return singleGraph;
    }

    /**
     * Obtém as arestas do subgrafo ordenadas a partir da origem.
     *
     * @return lista observável de arestas
     */
    public ObservableList<Edge<StationEsinf, MetricsStationEdge>> getEdgesOfSubgraph() {
        if (subgraphResult == null || subgraphResult.getEdgesList().isEmpty()) {
            return FXCollections.observableArrayList();
        }

        List<Edge<StationEsinf, MetricsStationEdge>> edges = new ArrayList<>(subgraphResult.getEdgesList());
        List<Edge<StationEsinf, MetricsStationEdge>> sortedEdges = new ArrayList<>();
        String currentKey = vOrigKey;
        for (int i = 0; i < edges.size(); i++) {
            for (Edge<StationEsinf, MetricsStationEdge> edge : edges) {
                if (edge.getVOrig().getId().equals(currentKey)) {
                    sortedEdges.add(edge);
                    currentKey = edge.getVDest().getId();
                    break;
                }
            }
        }
        return FXCollections.observableArrayList(sortedEdges);
    }

    /**
     * Obtém as estações pertencentes ao subgrafo do caminho.
     *
     * @return lista observável de estações
     */
    public ObservableList<StationEsinf> getStationsOfSubgraph() {
        if (subgraphResult == null || subgraphResult.getEdgesList().isEmpty()) {
            return FXCollections.observableArrayList();
        }

        List<StationEsinf> stations = new ArrayList<>();
        for (Edge<StationEsinf, MetricsStationEdge> edge : getEdgesOfSubgraph()) {
            stations.add(edge.getVOrig());
        }
        stations.add(subgraphResult.vertexByKey(String.valueOf(vDestKey)));
        return FXCollections.observableArrayList(stations);
    }

    /**
     * Obtém as arestas pertencentes a ciclos negativos.
     *
     * @return lista observável de arestas
     */
    public ObservableList<Edge<StationEsinf, MetricsStationEdge>> getEdgesOfNegativeCycle() {
        if (negativeCycleEdges == null) {
            return FXCollections.observableArrayList();
        }

        List<Edge<StationEsinf, MetricsStationEdge>> edges = new ArrayList<>(negativeCycleEdges);
        return FXCollections.observableArrayList(edges);
    }

    /**
     * Obtém as estações pertencentes a ciclos negativos.
     *
     * @return lista observável de estações
     */
    public ObservableList<StationEsinf> getStationsOfNegativeCycle() {
        List<StationEsinf> stations = new ArrayList<>();
        for (Edge<StationEsinf, MetricsStationEdge> edge : getEdgesOfNegativeCycle()) {
            stations.add(edge.getVOrig());
        }
        return FXCollections.observableArrayList(stations);
    }

    /**
     * Calcula o custo total do subgrafo.
     *
     * @return custo total
     */
    public double getTotalCostOfSubgraph() {
        if (subgraphResult == null || subgraphResult.getEdgesList().isEmpty()) {
            return 0.0;
        }

        double totalCost = 0.0;
        for (Edge<StationEsinf, MetricsStationEdge> edge : subgraphResult.getEdgesList()) {
            totalCost += edge.getWeight().getCost();
        }
        return totalCost;
    }

    /**
     * Calcula a distância total do subgrafo.
     *
     * @return distância total
     */
    public double getTotalDistanceOfSubgraph() {
        if (subgraphResult == null || subgraphResult.getEdgesList().isEmpty()) {
            return 0.0;
        }

        double totalDistance = 0.0;
        for (Edge<StationEsinf, MetricsStationEdge> edge : subgraphResult.getEdgesList()) {
            totalDistance += edge.getWeight().getDistance();
        }
        return totalDistance;
    }

    /**
     * Ordena uma lista de arestas de forma sequencial.
     *
     * @param edgesList lista de arestas
     * @return lista ordenada
     */
    private List<Edge<StationEsinf, MetricsStationEdge>> orderEdgesList(List<Edge<StationEsinf, MetricsStationEdge>> edgesList) {
        if (edgesList == null || edgesList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Edge<StationEsinf, MetricsStationEdge>> orderedEdges = new ArrayList<>();
        String currentKey = findEdgeWithLeastOriginKey(edgesList).getVOrig().getId();
        for (int i = 0; i < edgesList.size(); i++) {
            for (Edge<StationEsinf, MetricsStationEdge> edge : edgesList) {
                if (edge.getVOrig().getId().equals(currentKey)) {
                    orderedEdges.add(edge);
                    currentKey = edge.getVDest().getId();
                    break;
                }
            }
        }
        return orderedEdges;
    }

    /**
     * Encontra a aresta com menor chave de origem.
     *
     * @param edgesList lista de arestas
     * @return aresta com menor chave de origem
     */
    private Edge<StationEsinf, MetricsStationEdge> findEdgeWithLeastOriginKey(List<Edge<StationEsinf, MetricsStationEdge>> edgesList) {
        if (edgesList == null || edgesList.isEmpty()) {
            return null;
        }

        Edge<StationEsinf, MetricsStationEdge> minEdge = edgesList.getFirst();
        for (Edge<StationEsinf, MetricsStationEdge> edge : edgesList) {
            if (edge.getVOrig().getId().compareTo(minEdge.getVOrig().getId()) < 0) {
                minEdge = edge;
            }
        }
        return minEdge;
    }

    /**
     * Gera o ficheiro SVG do grafo resultante.
     *
     * @throws IOException caso ocorra erro de escrita
     */
    public void createResultSvgGraph() throws IOException {
        String fileName = String.format("%s - %s (%.2f cost)", vOrigKey, vDestKey, getTotalCostOfSubgraph());
        new GraphvizCreateFiles().create("output/ESINF/Specific Graphs/", fileName,
                subgraphResult.getVerticesNodes(),
                subgraphResult.getEdgesList());
    }
}
