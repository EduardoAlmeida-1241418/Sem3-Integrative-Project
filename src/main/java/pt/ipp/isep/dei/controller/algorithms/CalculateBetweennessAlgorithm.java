package pt.ipp.isep.dei.controller.algorithms;

import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Graph;

import java.util.*;

public class CalculateBetweennessAlgorithm {

    private Graph<StationEsinf, Double> graph;

    public CalculateBetweennessAlgorithm(Graph<StationEsinf, Double> graph) {
        this.graph = graph;
    }


    public Map<StationEsinf, Double> calculateBetweenness() {

        Map<StationEsinf, Double> betweenness = new HashMap<>();
        List<StationEsinf> vertices = graph.vertices();
        int n = vertices.size();

        // Complexity = O(V)
        for (StationEsinf v : vertices) {
            betweenness.put(v, 0.0);
        }

        // Complexity = O(V) * ( O(E * log V ) + O(V + E) ) = O(V * E * log V)
        for (StationEsinf s : vertices) {   // Complexity = O(V)

            double[] dist = new double[n];
            double[] sigma = new double[n];
            double[] delta = new double[n];

            Map<Integer, Set<Integer>> predecessors = new HashMap<>();
            List<Integer> visitedOrder = new ArrayList<>();

            for (int i = 0; i < n; i++) { // Complexity = O(V)
                dist[i] = Double.POSITIVE_INFINITY;
                sigma[i] = 0.0;
                delta[i] = 0.0;
                predecessors.put(i, new HashSet<>());
            }

            int sIdx = graph.key(s);
            dist[sIdx] = 0.0;
            sigma[sIdx] = 1.0;

            DijkstraBrandesAlgorithm.shortestPathDijkstraBrandes(graph, s, dist, sigma, predecessors, visitedOrder);    // Complexity = O(E * log V)


            // -------- Back-propagation --------
            // Complexity = O(V + E)
            for (int i = visitedOrder.size() - 1; i >= 0; i--) {    // Complexity = O(V)
                int w = visitedOrder.get(i);

                // Complexity = O(E)
                for (int v : predecessors.get(w)) {
                    if (sigma[w] != 0) {
                        delta[v] += (sigma[v] / sigma[w]) * (1.0 + delta[w]);
                    }
                }

                if (w != sIdx) {
                    StationEsinf wNode = graph.vertex(w);
                    betweenness.put( wNode, betweenness.get(wNode) + delta[w]);
                }
            }
        }

        // grafo NÃO dirigido → dividir por 2
        for (StationEsinf v : betweenness.keySet()) {   // Complexity = O(V)
            betweenness.put(v, betweenness.get(v) / 2.0);
        }

        return normalizeBetweenness(betweenness);   // Complexity = O(V)
    }

    // Complexity = O(V)
    private Map<StationEsinf, Double> normalizeBetweenness(Map<StationEsinf, Double> betweenness) {

        Map<StationEsinf, Double> result = new HashMap<>();

        int n = betweenness.size();
        if (n < 3) return betweenness;

        double normalizer = (n - 1) * (n - 2) / 2.0;

        for (Map.Entry<StationEsinf, Double> e : betweenness.entrySet()) { // Complexity = O(V)
            result.put(e.getKey(), e.getValue() / normalizer);
        }

        return result;
    }

}
