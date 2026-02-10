package pt.ipp.isep.dei.data.repository.sprint3;

import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;

import java.util.ArrayList;
import java.util.List;

public class EdgeEsinfRepository {
    private List<Edge<StationEsinf, MetricsStationEdge>> edgeList = new ArrayList<>();

    public void addEdge(Edge<StationEsinf, MetricsStationEdge> edge) {
        edgeList.add(edge);
    }

    public void removeEdge(Edge<StationEsinf, MetricsStationEdge> edge) {
        edgeList.remove(edge);
    }

    public List<Edge<StationEsinf, MetricsStationEdge>> getAllEdges() {
        return edgeList;
    }

    public int getEdgeCount() {
        return edgeList.size();
    }

    public void clear() {
        edgeList.clear();
    }
}
