package ESINF.sprint3.USEI12;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.algorithms.KruskalAlgorithm;
import pt.ipp.isep.dei.controller.graphviz.GraphvizMSTExporter;
import pt.ipp.isep.dei.controller.graphviz.GraphvizNative;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.Graph;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

/*
   Teste de geração de gráficos da USEI12.

   Objetivo:
   - Gerar ficheiros .dot e .svg a partir de um grafo de teste com ciclos
   - Validar a integração Kruskal + Graphviz
 */
public class KruskalGraphExportTest {

    @Test
    void generateMSTGraphFromCycleGraph() throws Exception {

        // 1) Criar grafo não direcionado (com ciclos)
        Graph<StationEsinf, MetricsStationEdge> g = new Graph<>(false);

        StationEsinf A = s("A", 10, 40);
        StationEsinf B = s("B", 10, 20);
        StationEsinf C = s("C", 30, 30);
        StationEsinf D = s("D", 50, 30);
        StationEsinf E = s("E", 70, 20);
        StationEsinf F = s("F", 70, 40);
        StationEsinf G = s("G", 50, 50);
        StationEsinf H = s("H", 30, 50);
        StationEsinf I = s("I", 90, 30);
        StationEsinf J = s("J", 110, 30);

        g.addVertex(A); g.addVertex(B); g.addVertex(C); g.addVertex(D); g.addVertex(E);
        g.addVertex(F); g.addVertex(G); g.addVertex(H); g.addVertex(I); g.addVertex(J);

        g.addEdge(A, B, w(4));
        g.addEdge(B, C, w(2));
        g.addEdge(C, A, w(6));   // ciclo

        g.addEdge(B, D, w(3));
        g.addEdge(C, E, w(2));
        g.addEdge(D, E, w(1));
        g.addEdge(D, F, w(5));
        g.addEdge(F, E, w(7));   // ciclo

        g.addEdge(F, G, w(2));
        g.addEdge(G, H, w(3));
        g.addEdge(H, F, w(8));   // ciclo

        g.addEdge(H, I, w(3));
        g.addEdge(I, J, w(1));

        // 2) Calcular MST
        Graph<StationEsinf, MetricsStationEdge> mst =
                KruskalAlgorithm.kruskal(g);

        Collection<StationEsinf> vertices = mst.vertices();
        Collection<Edge<StationEsinf, MetricsStationEdge>> edges = mst.edges();

        // 3) Gerar DOT e SVG
        String baseOriginal = "output/ESINF/Specific Graphs/Original/";
        String baseAdapted  = "output/ESINF/Specific Graphs/Adapted/";

        new File(baseOriginal).mkdirs();
        new File(baseAdapted).mkdirs();

        String dotOriginal =
                GraphvizMSTExporter.exportToDotOriginal(
                        baseOriginal + "usei12_cycle_test_mst.dot",
                        vertices,
                        edges
                );

        GraphvizNative.generateSvgOriginal(dotOriginal);

        String dotAdapted =
                GraphvizMSTExporter.exportToDotAdapted(
                        baseAdapted + "usei12_cycle_test_mst.dot",
                        vertices,
                        edges
                );

        GraphvizNative.generateSvgAdapted(dotAdapted);

        // 4) Verificação dos ficheiros gerados.
        File originalSvg = new File(
                "output/ESINF/Specific Graphs/Original/usei12_cycle_test_mst.svg"
        );

        File adaptedSvg = new File(
                "output/ESINF/Specific Graphs/Adapted/usei12_cycle_test_mst.svg"
        );

        assertTrue(originalSvg.exists(), "SVG Original não foi gerado");
        assertTrue(adaptedSvg.exists(), "SVG Adaptado não foi gerado");
    }


    // métodos auxiliares.

    private static StationEsinf s(String id, double x, double y) {
        return new StationEsinf(id, id, 0, 0, x, y);
    }

    private static MetricsStationEdge w(double d) {
        return new MetricsStationEdge(d, 0, 0);
    }
}
