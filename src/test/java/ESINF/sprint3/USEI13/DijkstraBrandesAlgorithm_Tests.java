package ESINF.sprint3.USEI13;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.algorithms.DijkstraBrandesAlgorithm;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Graph;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraBrandesAlgorithm_Tests {

    @Test
    void testDijkstraBrandesWithStationsAndLines() {

        Graph<StationEsinf, Double> graph = new Graph<>(false);

        StationEsinf A = new StationEsinf("A", "Station A", 0.0, 0.0);
        StationEsinf B = new StationEsinf("B", "Station B", 1.0, 1.0);
        StationEsinf C = new StationEsinf("C", "Station C", 2.0, 2.0);
        StationEsinf D = new StationEsinf("D", "Station D", 3.0, 3.0);

        graph.addVertex(A);
        graph.addVertex(B);
        graph.addVertex(C);
        graph.addVertex(D);

        graph.addEdge(A, B, 1.0);
        graph.addEdge(A, C, 1.0);
        graph.addEdge(B, C, 1.0);
        graph.addEdge(C, D, 1.0);

        int n = graph.numVertices();

        double[] dist = new double[n];
        double[] sigma = new double[n];
        Map<Integer, Set<Integer>> predecessors = new HashMap<>();
        List<Integer> visitedOrder = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
            sigma[i] = 0;
            predecessors.put(i, new HashSet<>());
        }

        int sourceIdx = graph.key(A);
        dist[sourceIdx] = 0;
        sigma[sourceIdx] = 1;

        DijkstraBrandesAlgorithm.shortestPathDijkstraBrandes(graph, A, dist, sigma, predecessors, visitedOrder);

        int idxA = graph.key(A);
        int idxB = graph.key(B);
        int idxC = graph.key(C);
        int idxD = graph.key(D);

        System.out.println("==============================================");
        System.out.printf("%-10s | %-10s | %-10s%n", "Station", "Expected", "Received");
        System.out.println("==============================================");

        System.out.printf("%-10s | %-10.1f | %-10.1f%n", "A", 0.0, dist[idxA]);
        System.out.printf("%-10s | %-10.1f | %-10.1f%n", "B", 1.0, dist[idxB]);
        System.out.printf("%-10s | %-10.1f | %-10.1f%n", "C", 1.0, dist[idxC]);
        System.out.printf("%-10s | %-10.1f | %-10.1f%n", "D", 2.0, dist[idxD]);

        System.out.println("==============================================");

        assertEquals(0.0, dist[idxA]);
        assertEquals(1.0, dist[idxB]);
        assertEquals(1.0, dist[idxC]);
        assertEquals(2.0, dist[idxD]);

        assertEquals(1.0, sigma[idxA]);
        assertEquals(1.0, sigma[idxB]);
        assertEquals(1.0, sigma[idxC]);
        assertEquals(1.0, sigma[idxD]);

        assertTrue(predecessors.get(idxB).contains(idxA));
        assertTrue(predecessors.get(idxC).contains(idxA));
        assertTrue(predecessors.get(idxD).contains(idxC));

        assertEquals(idxA, visitedOrder.get(0));
    }

    @Test
    void testDijkstraBrandesWithDisconnectedStation() {

        Graph<StationEsinf, Double> graph = new Graph<>(false);

        StationEsinf A = new StationEsinf("A", "Station A", 0.0, 0.0);
        StationEsinf B = new StationEsinf("B", "Station B", 1.0, 1.0);
        StationEsinf C = new StationEsinf("C", "Station C", 2.0, 2.0);
        StationEsinf D = new StationEsinf("D", "Station D", 3.0, 3.0);
        StationEsinf E = new StationEsinf("E", "Station E (Isolated)", 4.0, 4.0);

        graph.addVertex(A);
        graph.addVertex(B);
        graph.addVertex(C);
        graph.addVertex(D);
        graph.addVertex(E); // estação isolada

        graph.addEdge(A, B, 1.0);
        graph.addEdge(A, C, 1.0);
        graph.addEdge(B, C, 1.0);
        graph.addEdge(C, D, 1.0);

        int n = graph.numVertices();

        double[] dist = new double[n];
        double[] sigma = new double[n];
        Map<Integer, Set<Integer>> predecessors = new HashMap<>();
        List<Integer> visitedOrder = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
            sigma[i] = 0;
            predecessors.put(i, new HashSet<>());
        }

        int sourceIdx = graph.key(A);
        dist[sourceIdx] = 0;
        sigma[sourceIdx] = 1;

        DijkstraBrandesAlgorithm.shortestPathDijkstraBrandes(graph, A, dist, sigma, predecessors, visitedOrder);

        int idxA = graph.key(A);
        int idxB = graph.key(B);
        int idxC = graph.key(C);
        int idxD = graph.key(D);
        int idxE = graph.key(E);

        System.out.println("=========================================================");
        System.out.printf("%-10s | %-15s | %-15s%n", "Station", "Expected", "Received");
        System.out.println("=========================================================");

        System.out.printf("%-10s | %-15s | %-15.1f%n", "A", "0.0", dist[idxA]);
        System.out.printf("%-10s | %-15s | %-15.1f%n", "B", "1.0", dist[idxB]);
        System.out.printf("%-10s | %-15s | %-15.1f%n", "C", "1.0", dist[idxC]);
        System.out.printf("%-10s | %-15s | %-15.1f%n", "D", "2.0", dist[idxD]);
        System.out.printf("%-10s | %-15s | %-15s%n", "E", "∞", dist[idxE]);

        System.out.println("=========================================================");

        assertEquals(0.0, dist[idxA]);
        assertEquals(1.0, dist[idxB]);
        assertEquals(1.0, dist[idxC]);
        assertEquals(2.0, dist[idxD]);
        assertEquals(Double.POSITIVE_INFINITY, dist[idxE]);

        assertEquals(1.0, sigma[idxA]);
        assertEquals(1.0, sigma[idxB]);
        assertEquals(1.0, sigma[idxC]);
        assertEquals(1.0, sigma[idxD]);
        assertEquals(0.0, sigma[idxE]);

        assertTrue(predecessors.get(idxB).contains(idxA));
        assertTrue(predecessors.get(idxC).contains(idxA));
        assertTrue(predecessors.get(idxD).contains(idxC));
        assertTrue(predecessors.get(idxE).isEmpty());

        assertFalse(visitedOrder.contains(idxE));
        assertEquals(idxA, visitedOrder.get(0));
    }
}
