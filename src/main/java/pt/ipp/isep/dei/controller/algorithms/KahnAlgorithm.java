package pt.ipp.isep.dei.controller.algorithms;

import pt.ipp.isep.dei.domain.Graph.Graph;

import java.util.*;

public class KahnAlgorithm {


    // Complexity O(V + E) + O(V) = O(V + E)
    public static <V, E> boolean kahn(Graph<V, E> g, List<V> topSort) {

        Map<V, Integer> degreeIn = new HashMap<>();
        Queue<V> queueAux = new LinkedList<>();

        // Complexity O(V)
        for (V v : g.vertices()) {
            int indeg = g.inDegree(v);
            degreeIn.put(v, indeg);

            if (indeg == 0) {
                queueAux.add(v);
            }
        }

        int numVerts = 0;

        // Complexity O(V + E)
        while (!queueAux.isEmpty()) {   // Complexity O(V)
            V vOrig = queueAux.poll();
            topSort.add(vOrig);
            numVerts++;

            for (V vAdj : g.adjVertices(vOrig)) {   // Complexity O(E)
                int newDeg = degreeIn.get(vAdj) - 1;
                degreeIn.put(vAdj, newDeg);

                if (newDeg == 0) {
                    queueAux.add(vAdj);
                }
            }
        }

        return numVerts == g.numVertices();
    }
}