package pt.ipp.isep.dei.controller.infrastructurePlanner;

import pt.ipp.isep.dei.controller.algorithms.KruskalAlgorithm;
import pt.ipp.isep.dei.controller.graphviz.GraphvizMSTExporter;
import pt.ipp.isep.dei.controller.graphviz.GraphvizNative;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.Graph;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Controller responsável pelo cálculo do backbone da rede ferroviária
 * através da obtenção da árvore geradora mínima (MST).
 *
 * Este controller permite calcular a MST do grafo global
 * e gerar os respetivos ficheiros gráficos.
 */
/*
   Controller responsável por calcular o backbone (MST)
   e gerar os respetivos gráficos.
 */
public class BackboneController {

    /**
     * Diretório de saída para os gráficos do backbone na versão original.
     */
    private static final String OUTPUT_DIR_ORIGINAL =
            "output/ESINF/Specific Graphs/Original/";

    /**
     * Diretório de saída para os gráficos do backbone na versão adaptada.
     */
    private static final String OUTPUT_DIR_ADAPTED =
            "output/ESINF/Specific Graphs/Adapted/";

    /**
     * Gera os gráficos do backbone global a partir do grafo completo.
     *
     * @param baseFilename nome base dos ficheiros a gerar
     * @throws IOException caso ocorra erro na geração dos ficheiros
     */
    /*
       Gera os gráficos do backbone a partir do grafo global.
     */
    public void generateGlobalBackbone(String baseFilename) throws IOException {

        // 1) Construir o grafo global
        GenerateGlobalGraph generator = new GenerateGlobalGraph();
        Graph<StationEsinf, MetricsStationEdge> globalGraph =
                generator.buildGlobalGraph();

        // 2) Calcular a MST
        Graph<StationEsinf, MetricsStationEdge> mst =
                KruskalAlgorithm.kruskal(globalGraph);

        Collection<StationEsinf> vertices = mst.vertices();
        Collection<Edge<StationEsinf, MetricsStationEdge>> edges = mst.edges();

        // 3) Garantir diretórios
        createDirIfNeeded(OUTPUT_DIR_ORIGINAL);
        createDirIfNeeded(OUTPUT_DIR_ADAPTED);

        // 4) Exportar versão Original
        String dotOriginal =
                GraphvizMSTExporter.exportToDotOriginal(
                        OUTPUT_DIR_ORIGINAL + baseFilename + "_mst.dot",
                        vertices,
                        edges
                );

        GraphvizNative.generateSvgOriginal(dotOriginal);

        // 5) Exportar versão Adaptada
        String dotAdapted =
                GraphvizMSTExporter.exportToDotAdapted(
                        OUTPUT_DIR_ADAPTED + baseFilename + "_mst.dot",
                        vertices,
                        edges
                );

        GraphvizNative.generateSvgAdapted(dotAdapted);
    }

    /**
     * Cria um diretório caso este ainda não exista.
     *
     * @param path caminho do diretório
     */
    private void createDirIfNeeded(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Obtém todas as estações do grafo global.
     *
     * @return coleção de estações globais
     */
    //Metodo para a UI popular a tabela das estacoes Belga.
    public Collection<StationEsinf> getGlobalStations() {

        GenerateGlobalGraph generator = new GenerateGlobalGraph();
        Graph<StationEsinf, MetricsStationEdge> graph =
                generator.buildGlobalGraph();

        return graph.vertices();
    }

}
