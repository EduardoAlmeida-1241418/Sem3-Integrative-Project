package pt.ipp.isep.dei.controller.graphviz;

import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;
import pt.ipp.isep.dei.domain.Graph.Node;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

/**
 * Classe responsável por exportar grafos para ficheiros no formato DOT
 * compatíveis com o Graphviz.
 *
 * Permite gerar representações distintas do grafo ferroviário,
 * nomeadamente uma versão original e uma versão adaptada.
 */
public class GraphvizExporter {


    /**
     * Exporta o grafo para um ficheiro DOT na sua versão original.
     *
     * Esta versão utiliza posições fixas dos nós e um layout
     * mais próximo da representação geográfica original.
     *
     * @param filename caminho e nome do ficheiro DOT a gerar
     * @param nodes coleção de nós do grafo
     * @param edges coleção de arestas do grafo
     * @return caminho absoluto do ficheiro gerado
     * @throws IOException caso ocorra um erro de escrita no ficheiro
     */
    public static String exportToDotOriginal(
            String filename,
            Collection<Node<StationEsinf>> nodes,
            Collection<Edge<StationEsinf, MetricsStationEdge>> edges)
            throws IOException {

        File file = new File(filename);

        try (FileWriter fw = new FileWriter(file)) {

            fw.write("digraph railway {\n");
            fw.write("  layout=neato;\n");
            fw.write("  overlap=false;\n");
            fw.write("  splines=false;\n");
            fw.write("  node [shape=circle];\n");

            double scale = 5.0;

            for (Node<StationEsinf> node : nodes) {
                StationEsinf st = node.getValue();
                fw.write(String.format(
                        Locale.US,
                        "  \"%s\" [label=\"%s\", pos=\"%f,%f!\"];\n",
                        st.getId(),
                        st.getStationName(),
                        st.getCoordX() * scale,
                        st.getCoordY() * scale
                ));
            }

            for (Edge<StationEsinf, MetricsStationEdge> edge : edges) {
                StationEsinf a = edge.getOrigin().getValue();
                StationEsinf b = edge.getDestination().getValue();

                double cost = edge.getWeight().getCost();
                String color = cost < 0 ? "red" : "black";

                fw.write(String.format(
                        Locale.US,
                        "  \"%s\" -> \"%s\" [label=\"%.2f\", color=\"%s\", fontcolor=\"%s\", penwidth=3.0];\n",
                        a.getId(),
                        b.getId(),
                        cost,
                        color,
                        color
                ));
            }

            fw.write("}\n");
        }

        return file.getAbsolutePath();
    }


    /**
     * Exporta o grafo para um ficheiro DOT numa versão adaptada.
     *
     * Esta versão utiliza um layout mais flexível, com separação
     * automática de nós e melhor legibilidade visual.
     *
     * @param filename caminho e nome do ficheiro DOT a gerar
     * @param nodes coleção de nós do grafo
     * @param edges coleção de arestas do grafo
     * @return caminho absoluto do ficheiro gerado
     * @throws IOException caso ocorra um erro de escrita no ficheiro
     */
    public static String exportToDotAdapted(
            String filename,
            Collection<Node<StationEsinf>> nodes,
            Collection<Edge<StationEsinf, MetricsStationEdge>> edges)
            throws IOException {

        File file = new File(filename);

        try (FileWriter fw = new FileWriter(file)) {

            fw.write("digraph railway {\n");
            fw.write("  layout=neato;\n");
            fw.write("  overlap=prism;\n");
            fw.write("  sep=1.2;\n");
            fw.write("  splines=true;\n");
            fw.write("  outputorder=edgesfirst;\n");
            fw.write("  node [shape=ellipse, fontsize=10, margin=\"0.25,0.15\"];\n");
            fw.write("  edge [fontsize=9, penwidth=2.0, arrowsize=0.8];\n");

            for (Node<StationEsinf> node : nodes) {
                StationEsinf st = node.getValue();
                fw.write(String.format(
                        "  \"%s\" [label=\"%s\"];\n",
                        st.getId(),
                        st.getStationName()
                ));
            }

            for (Edge<StationEsinf, MetricsStationEdge> edge : edges) {
                StationEsinf a = edge.getOrigin().getValue();
                StationEsinf b = edge.getDestination().getValue();

                double cost = edge.getWeight().getCost();
                String color = cost < 0 ? "red" : "black";

                fw.write(String.format(
                        Locale.US,
                        "  \"%s\" -> \"%s\" [label=\"%.2f\", color=\"%s\", fontcolor=\"%s\", len=4.0];\n",
                        a.getId(),
                        b.getId(),
                        cost,
                        color,
                        color
                ));
            }

            fw.write("}\n");
        }

        return file.getAbsolutePath();
    }
}
