package pt.ipp.isep.dei.controller.graphviz;

import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;
import pt.ipp.isep.dei.domain.Graph.Node;

import java.io.IOException;
import java.util.List;

/**
 * Classe responsável pela criação de ficheiros Graphviz
 * a partir de uma lista de nós e arestas.
 *
 * Gera ficheiros no formato DOT e os respetivos SVG
 * para versões original e adaptada do grafo.
 */
public class GraphvizCreateFiles {

    /**
     * Cria os ficheiros Graphviz (DOT e SVG) a partir dos dados do grafo.
     *
     * O método trata da normalização do caminho do diretório,
     * gera os ficheiros DOT e invoca a conversão para SVG
     * nas versões original e adaptada.
     *
     * @param directoryPath caminho base onde os ficheiros serão criados
     * @param fileName nome base do ficheiro (sem extensão)
     * @param nodeList lista de nós do grafo
     * @param edgeList lista de arestas do grafo
     */
    public void create(String directoryPath, String fileName, List<Node<StationEsinf>> nodeList, List<Edge<StationEsinf, MetricsStationEdge>> edgeList) {
        fileName = fileName + ".dot";
        directoryPath = directoryPath.replace("\\","/");
        directoryPath = directoryPath.replace("\\\\","/");
        if (directoryPath.startsWith("/")) {
            directoryPath = directoryPath.substring(1);
        }
        if (!directoryPath.endsWith("/")) {
            directoryPath = directoryPath + "/";
        }

        try {
            String finalPath = GraphvizExporter.exportToDotOriginal( directoryPath + "Original/" + fileName, nodeList, edgeList);

            GraphvizNative.generateSvgOriginal(finalPath);

            finalPath = GraphvizExporter.exportToDotAdapted( directoryPath + "Adapted/" + fileName, nodeList, edgeList);

            GraphvizNative.generateSvgAdapted(finalPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
