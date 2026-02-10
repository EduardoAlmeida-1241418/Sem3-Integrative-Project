package pt.ipp.isep.dei.controller.algorithms;

import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint3.RailwayLineEsinfRepository;
import pt.ipp.isep.dei.data.repository.sprint3.StationEsinf3Repository;
import pt.ipp.isep.dei.domain.ESINF.RailwayLineEsinf;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Graph;

public class GraphGenerator {

    private Graph<StationEsinf, Double> graph;

    StationEsinf3Repository stationRepository;
    RailwayLineEsinfRepository lineRepository;

    public GraphGenerator() {
        stationRepository = Repositories.getInstance().getStationEsinf3Repository();
        lineRepository = Repositories.getInstance().getRailwayLineEsinfRepository();

    }

    public Graph<StationEsinf, Double> createGraph() {
        graph = new Graph<>(false);
        addVertices();
        addEdges();

        return graph;
    }

    private void addVertices() {
        for (StationEsinf station : stationRepository.findAll()) {
            graph.addVertex(station);
        }
    }

    private void addEdges() {
        for (RailwayLineEsinf line : lineRepository.findAll()) {
            graph.addEdge(line.getDepartureStation(), line.getArrivalStation(), line.getDistanceKm());
        }
    }

}
