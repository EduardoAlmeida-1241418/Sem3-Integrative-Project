package pt.ipp.isep.dei.controller.infrastructurePlanner;

import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint3.EdgeEsinfRepository;
import pt.ipp.isep.dei.data.repository.sprint3.NodeEsinfRepository;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.Graph;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;
import pt.ipp.isep.dei.domain.Graph.Node;

/**
 * Controller responsável pela criação do grafo global da rede ferroviária.
 *
 * Este grafo é não direcionado e contém todas as estações e
 * as conexões existentes entre elas.
 */
/*
   Cria um Grafo nao direcionado com todas as estacoes e conecções entre elas.
 */
public class GenerateGlobalGraph {

    /**
     * Constrói o grafo global da rede ferroviária.
     *
     * O grafo é criado com base nos repositórios de nós e arestas,
     * adicionando todas as estações como vértices e todas as ligações
     * como arestas.
     *
     * @return grafo global não direcionado
     */
    /*
      O grafo é criado a partir dos repositorios Node e Edge.
     */
    public Graph<StationEsinf, MetricsStationEdge> buildGlobalGraph() {

        Graph<StationEsinf, MetricsStationEdge> graph =
                new Graph<>(false); // non-directed

        Repositories repos = Repositories.getInstance();
        NodeEsinfRepository nodeRepo = repos.getNodeEsinfRepository();
        EdgeEsinfRepository edgeRepo = repos.getEdgeEsinfRepository();

        // Adiciona os Vertices.
        for (Node<StationEsinf> node : nodeRepo.getAllNodes()) {
            graph.addVertex(node.getValue());
        }

        // Adiciona as Arestas.
        for (Edge<StationEsinf, MetricsStationEdge> edge : edgeRepo.getAllEdges()) {
            graph.addEdge(
                    edge.getVOrig(),
                    edge.getVDest(),
                    edge.getWeight()
            );
        }

        return graph;
    }
}
