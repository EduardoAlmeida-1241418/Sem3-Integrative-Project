package ESINF.sprint3.USEI13;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.algorithms.CalculateBetweennessAlgorithm;
import pt.ipp.isep.dei.controller.algorithms.GraphGenerator;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint3.RailwayLineEsinfRepository;
import pt.ipp.isep.dei.data.repository.sprint3.StationEsinf3Repository;
import pt.ipp.isep.dei.domain.ESINF.RailwayLineEsinf;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Graph;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BetweennessAlgorithm_Tests {

    private StationEsinf A;
    private StationEsinf B;
    private StationEsinf C;
    private StationEsinf D;

    private StationEsinf3Repository stationRepository;
    private RailwayLineEsinfRepository lineRepository;

    private CalculateBetweennessAlgorithm algorithm;

    @BeforeEach
    void setUp() {
        stationRepository = Repositories.getInstance().getStationEsinf3Repository();
        lineRepository = Repositories.getInstance().getRailwayLineEsinfRepository();

        stationRepository.clear();
        lineRepository.clear();

        A = new StationEsinf("A", "Station A", 0.0, 0.0);
        B = new StationEsinf("B", "Station B", 1.0, 1.0);
        C = new StationEsinf("C", "Station C", 2.0, 2.0);
        D = new StationEsinf("D", "Station D", 3.0, 3.0);

        stationRepository.add(A);
        stationRepository.add(B);
        stationRepository.add(C);
        stationRepository.add(D);

        RailwayLineEsinf lineAB = new RailwayLineEsinf(A, B, 20, 10, 1.0);
        RailwayLineEsinf lineAC = new RailwayLineEsinf(A, C, 10, 10, 1.0);
        RailwayLineEsinf lineBC = new RailwayLineEsinf(B, C, 20, 10, 1.0);
        RailwayLineEsinf lineCD = new RailwayLineEsinf(C, D, 20, 10, 1.0);

        lineRepository.add(lineAB);
        lineRepository.add(lineAC);
        lineRepository.add(lineBC);
        lineRepository.add(lineCD);

        Graph<StationEsinf, Double> graph = new GraphGenerator().createGraph();

        algorithm = new CalculateBetweennessAlgorithm(graph);
    }

    @Test
    void calculateBetweennessValue() {

        Map<StationEsinf, Double> betweennessMap = algorithm.calculateBetweenness();

        // Expected values
        // A = 0
        // B = 0
        // C = 2 / 3
        // D = 0

        System.out.println("=========================================");
        System.out.printf("%-25s | %10s%n", "Station", "Betweenness");
        System.out.println("=========================================");

        for (StationEsinf station : betweennessMap.keySet()) {
            System.out.printf(
                    "%-25s | %10.4f%n",
                    station.getStationName(),
                    betweennessMap.get(station)
            );
        }

        System.out.println("=========================================");


        assertEquals(0, betweennessMap.get(A));
        assertEquals(0, betweennessMap.get(B));
        assertEquals((double) 2 / 3, betweennessMap.get(C));
        assertEquals(0, betweennessMap.get(D));

    }

}
