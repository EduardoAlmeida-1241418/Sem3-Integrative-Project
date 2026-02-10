package pt.ipp.isep.dei.controller.algorithms;

import pt.ipp.isep.dei.domain.Graph.Graph;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.WeightProvider;

import java.util.*;

public class BellmanFordAlgorithm {

    // O(V * E), [O(V) + O(V * E) + O(E) = O(V * E)]
    public static <V, E> List<Edge<V,E>> bellmanFord(Graph<V, E> g, V vOrig, int[] path, double[] dist) {

        int n = g.numVertices();    // O(1)

        // O(V)
        for (V v : g.vertices()) {  // O(V), [O(V) + O(1) = O(V)]
            int key = g.key(v);                     // O(1)
            dist[key] = Double.POSITIVE_INFINITY;   // O(1), [O(1) + O(1) = O(1)]
            path[key] = -1;                         // O(1), [O(1) + O(1) = O(1)]
        }

        dist[g.key(vOrig)] = 0;     // O(1), [O(1) + O(V) = O(V)]

        // O(V * E)
        for (int i = 1; i < n; i++) {   // O(V), [O(1) + O(V) = O(V)]
            boolean updated = false;    // O(1)

            for (Edge<V, E> edge : g.edges()) { // O(E), [O(E) * O(V) + O(1) = O(V * E)]
                int u = g.key(edge.getVOrig()); // O(1)
                int v = g.key(edge.getVDest()); // O(1), [O(1) + O(1) = O(1)]
                double w = ((WeightProvider) edge.getWeight()).getWeightValue();    // O(1), [O(1) + O(1) = O(1)]

                if (dist[u] != Double.POSITIVE_INFINITY && dist[v] > dist[u] + w) { // O(1), [O(1) + O(1) + O(1) + O(1) = O(1)]
                    dist[v] = dist[u] + w;        // O(1), [O(1) + O(1) = O(1)]
                    path[v] = u;                  // O(1), [O(1) + O(1) = O(1)]
                    updated = true;               // O(1), [O(1) + O(1) = O(1)
                }
            }

            if (!updated) break;    // O(1), [O(1) + O(V * E) = O(V * E)]
        }

        // O(E) OU O(V * E)
        for (Edge<V, E> edge : g.edges()) { // O(E)
            int u = g.key(edge.getVOrig());         // O(1)
            int v = g.key(edge.getVDest());         // O(1), [O(1) + O(1) = O(1)]
            double w = ((WeightProvider) edge.getWeight()).getWeightValue();    // O(1), [O(1) + O(1) = O(1)]

            if (dist[u] != Double.POSITIVE_INFINITY && dist[v] > dist[u] + w) {     // O(1), [O(1) + O(1) + O(1) + O(1) = O(1)]
                // Negative cycle found
                return extractNegativeCycleEdges(g, path, edge.getVDest()); // O(V)
            }

        }

        return null; // no negative cycle
    }

    // O(V)
    public static <V, E> List<Edge<V,E>> extractNegativeCycleEdges (Graph<V,E> g, int[] path, V startVertex) {
        List<V> vertices = extractNegativeCycleVertices(g, path, startVertex);  // O(V)
        List<Edge<V,E>> edges = new ArrayList<>();  // O(1), [O(V) + O(1) = O(V)]

        for (int i = 0; i < vertices.size() - 1; i++) {   // O(V), [O(V) * O(1) = O(V)]
            V a = vertices.get(i);         // O(1)
            V b = vertices.get(i + 1);     // O(1), [O(1) + O(1) = O(1)]
            edges.add(g.edgeNotDirect(a, b));       // O(1), [O(1) + O(1) = O(1)]
        }

        return edges;   // O(1), [O(1) + O(V) = O(V)]
    }

    // O(V)
    public static <V, E> List<V> extractNegativeCycleVertices(Graph<V,E> g, int[] path, V startVertex) {
        int n = g.numVertices();               // O(1)
        int v = g.key(startVertex);            // O(1), [O(1) + O(1) = O(1)]

        for (int i = 0; i < n; i++) {          // O(V)
            v = path[v];                       // O(1), [O(1) * O(V) = O(V)]
        }

        List<V> cycle = new ArrayList<>();     // O(1), [O(1) + O(V) = O(V)]
        int current = v;                        // O(1), [O(1) + O(V) = O(V)]

        do {                                   // O(V)
            cycle.add(g.vertex(current));      // O(1), [O(1) + O(V) = O(V)]
            current = path[current];           // O(1), [O(1) + O(V) = O(V)]
        } while (current != v);

        cycle.add(g.vertex(v));                // O(1), [O(1) + O(V) = O(V)]
        return cycle;                          // O(1), [O(1) + O(V) = O(V)]
    }
}
