package ESINF.sprint3.USEI13;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.algorithms.DegreeCalculatorAlgorithm;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint3.RailwayLineEsinfRepository;
import pt.ipp.isep.dei.domain.ESINF.RailwayLineEsinf;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DegreeCalculator_Tests {

    private StationEsinf A;
    private StationEsinf B;
    private StationEsinf C;

    private DegreeCalculatorAlgorithm algorithm;
    private RailwayLineEsinfRepository lineRepository;

    @BeforeEach
    void setUp() {
        lineRepository = Repositories.getInstance().getRailwayLineEsinfRepository();
        lineRepository.clear();

        A = new StationEsinf("A", "Station A", 0.0, 0.0);
        B = new StationEsinf("B", "Station B", 1.0, 1.0);
        C = new StationEsinf("C", "Station C", 2.0, 2.0);

        algorithm = new DegreeCalculatorAlgorithm();
    }

    @Test
    void shouldReturnZeroWhenStationHasNoConnections() {
        assertEquals(0, algorithm.calculateDegree(A));
    }

    @Test
    void shouldReturnOneWhenStationHasOneConnection() {
        RailwayLineEsinf lineAB = new RailwayLineEsinf(A, B, 100.0, 10, 1.0);

        lineRepository.add(lineAB);

        assertEquals(1, algorithm.calculateDegree(A));
        assertEquals(1, algorithm.calculateDegree(B));
    }

    @Test
    void shouldReturnCorrectDegreeWithMultipleConnections() {
        lineRepository.add(new RailwayLineEsinf(A, B, 100.0, 10, 1.0));
        lineRepository.add(new RailwayLineEsinf(A, C, 120.0, 8, 1.2));
        lineRepository.add(new RailwayLineEsinf(B, C, 90.0, 6, 0.9));

        assertEquals(2, algorithm.calculateDegree(A));
        assertEquals(2, algorithm.calculateDegree(B));
        assertEquals(2, algorithm.calculateDegree(C));
    }

    @Test
    void shouldCountLinesIndependentlyOfDirection() {
        RailwayLineEsinf lineBA = new RailwayLineEsinf(B, A, 80.0, 5, 0.8);

        lineRepository.add(lineBA);

        assertEquals(1, algorithm.calculateDegree(A));
        assertEquals(1, algorithm.calculateDegree(B));
    }
}
