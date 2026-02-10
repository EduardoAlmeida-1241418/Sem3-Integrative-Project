package pt.ipp.isep.dei.controller.graphviz;

import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

/**
 * Exporter responsável por gerar representações Graphviz do
 * backbone da rede ferroviária (Minimum Spanning Tree).
 *
 * Diferença para o GraphvizExporter:
 * - Grafo não direcionado (graph / --)
 * - Peso usado: distância (distance)
 */
public class GraphvizMSTExporter {

    /**
     * Exporta o MST no formato original (com coordenadas fixas).
     */
    public static String exportToDotOriginal(
            String filename,
            Collection<StationEsinf> nodes,
            Collection<Edge<StationEsinf, MetricsStationEdge>> edges)
            throws IOException {

        String fileFinalName = filename;

        File testFile = new File(filename);
        if (testFile.exists()) {
            fileFinalName = filename.replace(".dot", "") + "_.dot";
        }

        File file = new File(fileFinalName);

        try (FileWriter fw = new FileWriter(file)) {

            fw.write("graph mst {\n");
            fw.write("  layout=neato;\n");
            fw.write("  overlap=false;\n");
            fw.write("  splines=false;\n");
            fw.write("  node [shape=circle];\n");

            double scale = 5.0;

            // Escrita dos vértices com coordenadas fixas
            for (StationEsinf st : nodes) {
                fw.write(String.format(
                        Locale.US,
                        "  \"%s\" [label=\"%s\", pos=\"%.2f,%.2f!\"];\n",
                        st.getId(),
                        st.getStationName(),
                        st.getCoordX() * scale,
                        st.getCoordY() * scale
                ));
            }

            // Escrita das arestas do MST (não direcionadas)
            for (Edge<StationEsinf, MetricsStationEdge> edge : edges) {

                StationEsinf a = edge.getVOrig();
                StationEsinf b = edge.getVDest();

                // Evita duplicação lógica (a-b / b-a)
                if (a.getId().compareTo(b.getId()) >= 0) {
                    continue;
                }

                double distance = edge.getWeight().getDistance();

                fw.write(String.format(
                        Locale.US,
                        "  \"%s\" -- \"%s\" [label=\"%.2f km\", penwidth=3.0];\n",
                        a.getId(),
                        b.getId(),
                        distance
                ));
            }

            fw.write("}\n");
        }

        return file.getAbsolutePath();
    }

    /**
     * Exporta o MST no formato adaptado (visualização otimizada).
     */
    public static String exportToDotAdapted(
            String filename,
            Collection<StationEsinf> nodes,
            Collection<Edge<StationEsinf, MetricsStationEdge>> edges)
            throws IOException {

        String fileFinalName = filename;

        File testFile = new File(filename);
        if (testFile.exists()) {
            fileFinalName = filename.replace(".dot", "") + "_.dot";
        }

        File file = new File(fileFinalName);

        try (FileWriter fw = new FileWriter(file)) {

            fw.write("graph mst {\n");
            fw.write("  layout=neato;\n");
            fw.write("  overlap=prism;\n");
            fw.write("  sep=1.2;\n");
            fw.write("  splines=true;\n");
            fw.write("  outputorder=edgesfirst;\n");
            fw.write("  node [shape=ellipse, fontsize=10, margin=\"0.25,0.15\"];\n");
            fw.write("  edge [fontsize=9, penwidth=2.0];\n");

            // Escrita dos vértices (sem coordenadas fixas)
            for (StationEsinf st : nodes) {
                fw.write(String.format(
                        "  \"%s\" [label=\"%s\"];\n",
                        st.getId(),
                        st.getStationName()
                ));
            }

            // Escrita das arestas do MST
            for (Edge<StationEsinf, MetricsStationEdge> edge : edges) {

                StationEsinf a = edge.getVOrig();
                StationEsinf b = edge.getVDest();

                if (a.getId().compareTo(b.getId()) >= 0) {
                    continue;
                }

                double distance = edge.getWeight().getDistance();

                fw.write(String.format(
                        Locale.US,
                        "  \"%s\" -- \"%s\" [label=\"%.2f km\", len=4.0];\n",
                        a.getId(),
                        b.getId(),
                        distance
                ));
            }

            fw.write("}\n");
        }

        return file.getAbsolutePath();
    }
}
