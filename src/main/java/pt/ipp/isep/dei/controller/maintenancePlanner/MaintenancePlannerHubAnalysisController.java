package pt.ipp.isep.dei.controller.maintenancePlanner;

import pt.ipp.isep.dei.controller.algorithms.CalculateBetweennessAlgorithm;
import pt.ipp.isep.dei.controller.algorithms.CalculateHarmonicClosenessAlgorithm;
import pt.ipp.isep.dei.controller.algorithms.StrengthCalculatorAlgorithm;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint3.RailwayLineEsinfRepository;
import pt.ipp.isep.dei.data.repository.sprint3.StationEsinf3Repository;
import pt.ipp.isep.dei.domain.ESINF.RailwayLineEsinf;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Graph;

import java.util.*;

/**
 * Controller responsável pela análise de hubs para planeamento de manutenção.
 *
 * Constrói um grafo não direcionado da rede ferroviária e calcula métricas
 * como betweenness, strength, harmonic closeness e hub score para cada estação.
 */
public class MaintenancePlannerHubAnalysisController {

    /**
     * Repositório de estações.
     */
    private final StationEsinf3Repository stationRepository;

    /**
     * Repositório de linhas ferroviárias.
     */
    private final RailwayLineEsinfRepository lineRepository;

    /**
     * Grafo não direcionado utilizado na análise.
     */
    private Graph<StationEsinf, Double> graph;

    /**
     * Mapa de betweenness por estação.
     */
    private Map<StationEsinf, Double> betweennessMap;

    /**
     * Mapa de strength por estação.
     */
    private Map<StationEsinf, Double> strengthMap;

    /**
     * Mapa de harmonic closeness por estação.
     */
    private Map<StationEsinf, Double> harmonicClosenessMap;

    /**
     * Mapa de hub score por estação.
     */
    private Map<StationEsinf, Double> hubScoreMap;

    /**
     * Construtor do controller.
     *
     * Inicializa os repositórios, constrói o grafo
     * e calcula todas as métricas necessárias.
     */
    public MaintenancePlannerHubAnalysisController() {
        stationRepository = Repositories.getInstance().getStationEsinf3Repository();
        lineRepository = Repositories.getInstance().getRailwayLineEsinfRepository();

        createGraph();

        generateStrengthMap();
        generateBetweennessMap();
        generateHarmonicClosenessMap();
        generateHubScoreMap();
    }

    // ===================== GRAPH =====================

    /**
     * Cria o grafo não direcionado da rede ferroviária.
     */
    private void createGraph() {
        graph = new Graph<>(false);
        addVertices();
        addEdges();
    }

    /**
     * Adiciona todas as estações como vértices do grafo.
     */
    private void addVertices() {
        for (StationEsinf station : stationRepository.findAll()) {
            graph.addVertex(station);
        }
    }

    /**
     * Adiciona todas as linhas ferroviárias como arestas do grafo.
     */
    private void addEdges() {
        for (RailwayLineEsinf line : lineRepository.findAll()) {
            graph.addEdge(line.getDepartureStation(), line.getArrivalStation(), line.getDistanceKm());
        }
    }

    // ===================== Hub Score ==================

    /**
     * Calcula o hub score de cada estação com base numa combinação ponderada
     * de betweenness, harmonic closeness e strength.
     */
    private void generateHubScoreMap() {
        hubScoreMap = new HashMap<>();
        for (StationEsinf station : stationRepository.findAll()) {

            double hubscoreVal = findBetweenness(station) * 0.35 + findHarmonicCloseness(station) * 0.35 + findStrength(station) * 0.3;

            hubScoreMap.put(station, hubscoreVal);
        }
    }

    // ===================== BETWENNESS =====================

    /**
     * Calcula o betweenness de todas as estações do grafo.
     */
    private void generateBetweennessMap() {
        CalculateBetweennessAlgorithm algorithm = new CalculateBetweennessAlgorithm(graph);

        betweennessMap = algorithm.calculateBetweenness();
    }

    // ===================== STRENGTH =====================

    /**
     * Calcula e normaliza o strength de todas as estações.
     */
    private void generateStrengthMap() {
        StrengthCalculatorAlgorithm strengthAlgorithm = new StrengthCalculatorAlgorithm();

        Map<StationEsinf, Double> rawStrength = new HashMap<>();

        for (StationEsinf s : stationRepository.findAll()) {
            rawStrength.put(s, (double) strengthAlgorithm.calculateStrength(s));
        }

        strengthMap = normalizeStrength(rawStrength);
    }

    /**
     * Normaliza os valores de strength para o intervalo [0,1].
     *
     * @param values mapa de valores brutos
     * @return mapa normalizado
     */
    private Map<StationEsinf, Double> normalizeStrength(Map<StationEsinf, Double> values) {

        Map<StationEsinf, Double> result = new HashMap<>();

        if (values.isEmpty()) return result;

        double max = Collections.max(values.values());

        if (max == 0) {
            for (StationEsinf s : values.keySet()) {
                result.put(s, 0.0);
            }
            return result;
        }

        for (Map.Entry<StationEsinf, Double> e : values.entrySet()) {
            result.put(e.getKey(), e.getValue() / max);
        }

        return result;
    }

    // ===================== HARMONIC CLOSENESS =====================

    /**
     * Calcula o harmonic closeness de todas as estações.
     */
    private void generateHarmonicClosenessMap() {
        harmonicClosenessMap = CalculateHarmonicClosenessAlgorithm.calculateHarmonicCloseness(graph);
    }

    // ===================== GETTERS =====================

    /**
     * Obtém a lista de todas as estações.
     *
     * @return lista de estações
     */
    public List<StationEsinf> getStations() {
        return new ArrayList<>(stationRepository.findAll());
    }

    /**
     * Obtém o betweenness de uma estação.
     *
     * @param station estação
     * @return valor de betweenness
     */
    public double findBetweenness(StationEsinf station) {
        return betweennessMap.get(station);
    }

    /**
     * Obtém o strength de uma estação.
     *
     * @param station estação
     * @return valor de strength
     */
    public double findStrength(StationEsinf station) {
        return strengthMap.get(station);
    }

    /**
     * Obtém o harmonic closeness de uma estação.
     *
     * @param station estação
     * @return valor de harmonic closeness
     */
    public double findHarmonicCloseness(StationEsinf station) {
        return harmonicClosenessMap.get(station);
    }

    /**
     * Obtém o hub score de uma estação.
     *
     * @param value estação
     * @return valor de hub score
     */
    public double findHubScore(StationEsinf value) {
        return hubScoreMap.get(value);
    }

    /**
     * Obtém a lista de estações ordenadas por hub score (decrescente).
     *
     * @return lista de estações ordenada por importância
     */
    public List<StationEsinf> getFacilities() {

        List<StationEsinf> stations = new ArrayList<>(stationRepository.findAll());

        Collections.sort(stations, new Comparator<StationEsinf>() {
            @Override
            public int compare(StationEsinf s1, StationEsinf s2) {

                double h1 = hubScoreMap.get(s1);
                double h2 = hubScoreMap.get(s2);

                return Double.compare(h2, h1);
            }
        });

        return stations;
    }

}
