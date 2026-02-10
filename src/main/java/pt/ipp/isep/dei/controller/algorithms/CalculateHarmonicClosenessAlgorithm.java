package pt.ipp.isep.dei.controller.algorithms;

import pt.ipp.isep.dei.domain.Graph.Graph;

import java.util.*;

/**
 * Calcula Harmonic Closeness Centrality
 * HC(v) = sum_{u != v} (1 / d(v,u))
 * onde d(v,u) é a distância mínima (shortest path)
 */
public class CalculateHarmonicClosenessAlgorithm {

    public static <V> Map<V, Double> calculateHarmonicCloseness(Graph<V, Double> graph) {

        Map<V, Double> closenessMap = new HashMap<>();

        int n = graph.numVertices();

        // Total Complexity = O(V * E * log V)
        for (V source : graph.vertices()) { // Complexity = O(V)

            int sourceIdx = graph.key(source);

            double[] dist = new double[n];
            double[] sigma = new double[n];
            Map<Integer, Set<Integer>> predecessors = new HashMap<>();
            List<Integer> visitedOrder = new ArrayList<>();

            Arrays.fill(dist, Double.POSITIVE_INFINITY);
            Arrays.fill(sigma, 0);

            for (int i = 0; i < n; i++) { // Complexity = O(V)
                predecessors.put(i, new HashSet<>());
            }

            dist[sourceIdx] = 0.0;
            sigma[sourceIdx] = 1.0;

            DijkstraBrandesAlgorithm.shortestPathDijkstraBrandes(graph, source, dist, sigma, predecessors, visitedOrder); // Complexity = O(E * log V)

            double sum = 0.0;

            // Complexity = O(V)
            for (int i = 0; i < n; i++) {
                if (i != sourceIdx && dist[i] != Double.POSITIVE_INFINITY && dist[i] > 0.0) {

                    sum += 1.0 / dist[i];

                }
            }

            double hc = (n > 1) ? sum / (n - 1) : 0.0;

            closenessMap.put(source, hc);
        }

        return closenessMap;
    }
}
