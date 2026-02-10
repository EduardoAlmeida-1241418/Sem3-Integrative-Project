package ESINF.sprint3.USEI13;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.algorithms.StrengthCalculatorAlgorithm;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint3.RailwayLineEsinfRepository;
import pt.ipp.isep.dei.domain.ESINF.RailwayLineEsinf;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StrengthCalculator_Tests {

    private StationEsinf A;
    private StationEsinf B;
    private StationEsinf C;

    private StrengthCalculatorAlgorithm algorithm;
    private RailwayLineEsinfRepository lineRepository;

    @BeforeEach
    void setUp() {
        lineRepository = Repositories.getInstance().getRailwayLineEsinfRepository();
        lineRepository.clear();

        A = new StationEsinf("A", "Station A", 0.0, 0.0);
        B = new StationEsinf("B", "Station B", 1.0, 1.0);
        C = new StationEsinf("C", "Station C", 2.0, 2.0);

        algorithm = new StrengthCalculatorAlgorithm();
    }

    @Test
    void shouldReturnZeroWhenStationHasNoConnections() {
        assertEquals(0, algorithm.calculateStrength(A));
    }

    @Test
    void shouldReturnCapacityWhenStationHasOneConnection() {
        RailwayLineEsinf lineAB = new RailwayLineEsinf(A, B, 100.0, 10, 1.0);

        lineRepository.add(lineAB);

        assertEquals(10, algorithm.calculateStrength(A));
        assertEquals(10, algorithm.calculateStrength(B));
    }

    @Test
    void shouldSumCapacitiesWithMultipleConnections() {
        lineRepository.add(new RailwayLineEsinf(A, B, 100.0, 10, 1.0));
        lineRepository.add(new RailwayLineEsinf(A, C, 120.0, 8, 1.2));
        lineRepository.add(new RailwayLineEsinf(B, C, 90.0, 6, 0.9));

        assertEquals(18, algorithm.calculateStrength(A)); // 10 + 8
        assertEquals(16, algorithm.calculateStrength(B)); // 10 + 6
        assertEquals(14, algorithm.calculateStrength(C)); // 8 + 6
    }

    @Test
    void shouldCountCapacityIndependentlyOfDirection() {
        RailwayLineEsinf lineBA = new RailwayLineEsinf(B, A, 80.0, 5, 0.8);

        lineRepository.add(lineBA);

        assertEquals(5, algorithm.calculateStrength(A));
        assertEquals(5, algorithm.calculateStrength(B));
    }
}
