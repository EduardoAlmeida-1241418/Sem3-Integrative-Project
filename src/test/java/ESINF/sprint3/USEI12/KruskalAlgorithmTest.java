package ESINF.sprint3.USEI12;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.algorithms.KruskalAlgorithm;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.Graph;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes da USEI12 – Algoritmo de Kruskal (MST)
 *
 * Cada teste valida:
 * - Impressão clara das ligações com pesos
 * - E = V - 1 na MST
 * - Eliminação correta de ciclos
 * - Ignorar self-loops
 * - Resolução correta de arestas duplas
 */
public class KruskalAlgorithmTest {

    /* =========================
       TESTE 1 – CICLOS
       ========================= */
    @Test
    void testMSTWithCycles() {

        Graph<StationEsinf, MetricsStationEdge> g = createBaseGraph();

        // Ciclos explícitos
        g.addEdge(A, B, w(4));
        g.addEdge(B, C, w(2));
        g.addEdge(C, A, w(6)); // ciclo 1
        g.addEdge(B, D, w(3));
        g.addEdge(C, E, w(2));

        g.addEdge(D, F, w(5));
        g.addEdge(D, E, w(1));
        g.addEdge(F, E, w(7)); // ciclo 2
        g.addEdge(F, H, w(5));
        g.addEdge(E, G, w(7));

        g.addEdge(H, G, w(2));
        g.addEdge(H, I, w(3));
        g.addEdge(G, I, w(8)); // ciclo 3
        g.addEdge(H, J, w(5));

        Graph<StationEsinf, MetricsStationEdge> mst =
                KruskalAlgorithm.kruskal(g);

        printGraph("TESTE DE CICLOS - GRAFO ORIGINAL", g);
        printGraph("TESTE DE CICLOS - MST", mst);

        assertEquals(
                mst.vertices().size() - 1,
                countUndirectedEdges(mst),
                "MST deve ter V-1 arestas"
        );
    }

    /* =========================
       TESTE 2 – SELF-LOOPS
       ========================= */
    @Test
    void testMSTWithSelfLoops() {

        Graph<StationEsinf, MetricsStationEdge> g = createBaseGraph();

        // Self-loops (3 ocorrências)
        g.addEdge(A, A, w(9.5));
        g.addEdge(D, D, w(5));
        g.addEdge(F, F, w(1));

        // Arestas normais
        g.addEdge(A, B, w(3.5));
        g.addEdge(A, C, w(5));
        g.addEdge(B, C, w(4));
        g.addEdge(C, D, w(2));
        g.addEdge(D, E, w(2));
        g.addEdge(D, B, w(5));
        g.addEdge(E, F, w(6.5));
        g.addEdge(F, G, w(1));
        g.addEdge(G, H, w(2));
        g.addEdge(H, I, w(3));
        g.addEdge(J, H, w(4));
        g.addEdge(B, H, w(7));
        g.addEdge(E, G, w(6));

        Graph<StationEsinf, MetricsStationEdge> mst =
                KruskalAlgorithm.kruskal(g);

        printGraph("TESTE DE SELF-LOOPS - GRAFO ORIGINAL", g);
        printGraph("TESTE DE SELF-LOOPS - MST", mst);

        // Garantir que não há self-loops na MST
        for (Edge<StationEsinf, MetricsStationEdge> e : mst.edges()) {
            assertNotEquals(
                    e.getVOrig(),
                    e.getVDest(),
                    "MST não pode conter self-loops"
            );
        }

        assertEquals(
                mst.vertices().size() - 1,
                countUndirectedEdges(mst)
        );
    }

    /* =========================
       TESTE 3 – ARESTAS DUPLAS
       ========================= */
    @Test
    void testMSTWithDuplicateEdges() {

        Graph<StationEsinf, MetricsStationEdge> g = createBaseGraph();

        // Arestas duplas (mesmos vértices, pesos diferentes)
        g.addEdge(A, B, w(10));
        g.addEdge(A, B, w(3));   // deve ganhar

        g.addEdge(B, D, w(8));
        g.addEdge(B, D, w(2));   // deve ganhar

        g.addEdge(E, F, w(5));
        g.addEdge(E, F, w(5));   // pesos iguais

        // Ligações adicionais
        g.addEdge(A, C, w(4));
        g.addEdge(A, B, w(3));
        g.addEdge(B, C, w(2));
        g.addEdge(D, F, w(4));
        g.addEdge(D, E, w(5));
        g.addEdge(E, G, w(6));
        g.addEdge(G, H, w(3));
        g.addEdge(H, E, w(2));
        g.addEdge(H, I, w(7));
        g.addEdge(G, I, w(4));
        g.addEdge(I, J, w(1));

        Graph<StationEsinf, MetricsStationEdge> mst =
                KruskalAlgorithm.kruskal(g);

        printGraph("TESTE DE ARESTAS DUPLAS - GRAFO ORIGINAL", g);
        printGraph("TESTE DE ARESTAS DUPLAS - MST", mst);

        assertEquals(
                mst.vertices().size() - 1,
                countUndirectedEdges(mst)
        );
    }

    /* =========================
       MÉTODOS AUXILIARES
       ========================= */

    private static StationEsinf A,B,C,D,E,F,G,H,I,J;

    private Graph<StationEsinf, MetricsStationEdge> createBaseGraph() {

        Graph<StationEsinf, MetricsStationEdge> g = new Graph<>(false);

        A = s("A"); B = s("B"); C = s("C"); D = s("D"); E = s("E");
        F = s("F"); G = s("G"); H = s("H"); I = s("I"); J = s("J");

        g.addVertex(A); g.addVertex(B); g.addVertex(C); g.addVertex(D);
        g.addVertex(E); g.addVertex(F); g.addVertex(G); g.addVertex(H);
        g.addVertex(I); g.addVertex(J);

        return g;
    }

    private static StationEsinf s(String id) {
        return new StationEsinf(id, id, 0, 0, 0, 0);
    }

    private static MetricsStationEdge w(double d) {
        return new MetricsStationEdge(d, 0, 0);
    }

    private void printGraph(String title, Graph<StationEsinf, MetricsStationEdge> g) {

        System.out.println("\n=== " + title + " ===");

        Set<String> printed = new HashSet<>();

        for (Edge<StationEsinf, MetricsStationEdge> e : g.edges()) {

            String a = e.getVOrig().getId();
            String b = e.getVDest().getId();

            String key = a.compareTo(b) <= 0 ? a + "-" + b : b + "-" + a;
            if (printed.contains(key)) continue;

            printed.add(key);

            System.out.printf(
                    "%s --%.1f-- %s%n",
                    a,
                    e.getWeight().getDistance(),
                    b
            );
        }

        System.out.println(
                "Vertices (V): " + g.vertices().size() +
                        " | Arestas (E): " + countUndirectedEdges(g)
        );
    }

    private int countUndirectedEdges(Graph<StationEsinf, MetricsStationEdge> g) {
        Set<String> set = new HashSet<>();
        for (Edge<StationEsinf, MetricsStationEdge> e : g.edges()) {
            String a = e.getVOrig().getId();
            String b = e.getVDest().getId();
            set.add(a.compareTo(b) <= 0 ? a + "-" + b : b + "-" + a);
        }
        return set.size();
    }
}
