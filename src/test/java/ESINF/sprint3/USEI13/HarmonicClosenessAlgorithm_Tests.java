package ESINF.sprint3.USEI13;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.algorithms.CalculateBetweennessAlgorithm;
import pt.ipp.isep.dei.controller.algorithms.CalculateHarmonicClosenessAlgorithm;
import pt.ipp.isep.dei.controller.algorithms.GraphGenerator;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint3.RailwayLineEsinfRepository;
import pt.ipp.isep.dei.data.repository.sprint3.StationEsinf3Repository;
import pt.ipp.isep.dei.domain.ESINF.RailwayLineEsinf;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Graph;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HarmonicClosenessAlgorithm_Tests {

    private static final double DELTA = 1e-9; // Corrigir erros de Double

    private StationEsinf A;
    private StationEsinf B;
    private StationEsinf C;
    private StationEsinf D;
    private StationEsinf E;

    private StationEsinf3Repository stationRepository;
    private RailwayLineEsinfRepository lineRepository;

    private CalculateHarmonicClosenessAlgorithm algorithm;
    private Graph<StationEsinf, Double> graph;

    @BeforeEach
    void setUp() {

        stationRepository = Repositories.getInstance().getStationEsinf3Repository();
        lineRepository = Repositories.getInstance().getRailwayLineEsinfRepository();

        lineRepository.clear();
        stationRepository.clear();

        A = new StationEsinf("A", "Station A", 0.0, 0.0);
        B = new StationEsinf("B", "Station B", 1.0, 1.0);
        C = new StationEsinf("C", "Station C", 2.0, 2.0);
        D = new StationEsinf("D", "Station D", 3.0, 3.0);
        E = new StationEsinf("E", "Station E", 4.0, 4.0);

        stationRepository.add(A);
        stationRepository.add(B);
        stationRepository.add(C);
        stationRepository.add(D);
        stationRepository.add(E);


        algorithm = new CalculateHarmonicClosenessAlgorithm();
    }

    @Test
    void calculateHarmonicClosenessValueAllConnected() {

        RailwayLineEsinf lineAE = new RailwayLineEsinf(A, E, 3, 10, 1.0);
        RailwayLineEsinf lineAD = new RailwayLineEsinf(A, D, 12, 10, 1.0);
        RailwayLineEsinf lineDE = new RailwayLineEsinf(D, E, 5, 10, 1.0);
        RailwayLineEsinf lineEB = new RailwayLineEsinf(E, B, 10, 10, 1.0);
        RailwayLineEsinf lineDC = new RailwayLineEsinf(D, C, 10, 10, 1.0);

        lineRepository.add(lineAE);
        lineRepository.add(lineAD);
        lineRepository.add(lineDE);
        lineRepository.add(lineEB);
        lineRepository.add(lineDC);

        graph = new GraphGenerator().createGraph();

        Map<StationEsinf, Double> closenessMap = algorithm.calculateHarmonicCloseness(graph);

        // Expected values
        //
        // A = 553 / 3744
        // B = 553 / 7800
        // C = 59  / 900
        // D = 59  / 480
        // E = 7   / 40

        System.out.println("===============================================================");
        System.out.printf("%-10s | %-15s | %-15s%n", "Station", "Expected", "Real");
        System.out.println("===============================================================");

        System.out.printf("%-10s | %-15.6f | %-15.6f%n", "A", 553.0/3744.0, closenessMap.get(A));
        System.out.printf("%-10s | %-15.6f | %-15.6f%n", "B", 553.0/7800.0, closenessMap.get(B));
        System.out.printf("%-10s | %-15.6f | %-15.6f%n", "C", 59.0/900.0,  closenessMap.get(C));
        System.out.printf("%-10s | %-15.6f | %-15.6f%n", "D", 59.0/480.0,  closenessMap.get(D));
        System.out.printf("%-10s | %-15.6f | %-15.6f%n", "E", 7.0/40.0,    closenessMap.get(E));

        System.out.println("===============================================================");


        assertEquals(553.0 / 3744.0, closenessMap.get(A), DELTA);
        assertEquals(553.0 / 7800.0, closenessMap.get(B), DELTA);
        assertEquals(59.0 / 900.0, closenessMap.get(C), DELTA);
        assertEquals(59.0 / 480.0, closenessMap.get(D), DELTA);
        assertEquals(7.0 / 40.0, closenessMap.get(E), DELTA );
    }

    @Test
    void calculateHarmonicClosenessValue1VertexDisconnected() {
        lineRepository.clear();

        RailwayLineEsinf lineAE = new RailwayLineEsinf(A, E, 3, 10, 1.0);
        RailwayLineEsinf lineAD = new RailwayLineEsinf(A, D, 12, 10, 1.0);
        RailwayLineEsinf lineDE = new RailwayLineEsinf(D, E, 5, 10, 1.0);
        RailwayLineEsinf lineEB = new RailwayLineEsinf(E, B, 10, 10, 1.0);

        lineRepository.add(lineAE);
        lineRepository.add(lineAD);
        lineRepository.add(lineDE);
        lineRepository.add(lineEB);

        graph = new GraphGenerator().createGraph();

        Map<StationEsinf, Double> closenessMap = algorithm.calculateHarmonicCloseness(graph);

        // Expected values
        //
        // A = 167 / 1248
        // B = 19  / 312
        // C = 0
        // D = 47  / 480
        // E = 19  / 120

        System.out.println("===============================================================");
        System.out.printf("%-10s | %-15s | %-15s%n", "Station", "Expected", "Real");
        System.out.println("===============================================================");

        System.out.printf("%-10s | %-15.6f | %-15.6f%n", "A", 167.0 / 1248.0, closenessMap.get(A));
        System.out.printf("%-10s | %-15.6f | %-15.6f%n", "B", 19.0  / 312.0, closenessMap.get(B));
        System.out.printf("%-10s | %-15.6f | %-15.6f%n", "C", 0.0,  closenessMap.get(C));
        System.out.printf("%-10s | %-15.6f | %-15.6f%n", "D", 47.0  / 480.0,  closenessMap.get(D));
        System.out.printf("%-10s | %-15.6f | %-15.6f%n", "E", 19.0  / 120.0,    closenessMap.get(E));

        System.out.println("===============================================================");


        assertEquals(167.0 / 1248.0, closenessMap.get(A), DELTA);
        assertEquals(19.0  / 312.0, closenessMap.get(B), DELTA);
        assertEquals(0.0, closenessMap.get(C), DELTA);
        assertEquals(47.0  / 480.0, closenessMap.get(D), DELTA);
        assertEquals(19.0  / 120.0, closenessMap.get(E), DELTA );
    }
}
