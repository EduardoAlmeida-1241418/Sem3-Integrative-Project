package pt.ipp.isep.dei.controller.algorithms;

import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.Graph;

import java.util.*;

/**
 * Dijkstra adaptado para o algoritmo de Brandes
 * (grafos ponderados, pesos não negativos)
 */
public class DijkstraBrandesAlgorithm {

    /**
     * Executa Dijkstra modificado para Brandes
     *
     * @param g              grafo
     * @param vOrig          vértice origem
     * @param dist           distâncias mínimas
     * @param sigma          número de caminhos mínimos
     * @param predecessors   predecessores em caminhos mínimos
     * @param visitedOrder   ordem de visita (stack lógica)
     */

    // Complexity O(E * log V) + O(log(V)) = O(E * log V)
    public static <V> void shortestPathDijkstraBrandes(Graph<V, Double> g, V vOrig, double[] dist, double[] sigma, Map<Integer, Set<Integer>> predecessors, List<Integer> visitedOrder) {

        int n = g.numVertices();
        int sourceIdx = g.key(vOrig);
        if (sourceIdx == -1) return;

        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingDouble(i -> dist[i]));

        pq.offer(sourceIdx);    // Complexity O(log(V))

        // Complexity O(E * log(V))
        // Ao longo do algoritmo, cada aresta é processada uma vez no total por isso nao fica O(E^2 * log(V))

        while (!pq.isEmpty()) {          // Complexity O(E)
            int u = pq.poll();

            // ignora entradas obsoletas
            if (dist[u] == Double.POSITIVE_INFINITY) continue;

            visitedOrder.add(u);
            V uNode = g.vertex(u);

            // Complexity O(E * long(V))
            for (Edge<V, Double> edge : g.outgoingEdges(uNode)) {   // Complexity O(E)

                V vNode = edge.getDestination().getValue();
                int v = g.key(vNode);

                double newDist = dist[u] + edge.getWeight();

                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    sigma[v] = sigma[u];
                    predecessors.get(v).clear();
                    predecessors.get(v).add(u);
                    pq.offer(v);                                    // Complexity O(log(V))
                }
                else if (Double.compare(newDist, dist[v]) == 0) {
                    sigma[v] += sigma[u];
                    predecessors.get(v).add(u);
                }
            }
        }
    }
}
