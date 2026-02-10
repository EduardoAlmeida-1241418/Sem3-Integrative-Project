package ESINF.sprint3.USEI14;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.algorithms.EdmondsKarpAlgorithm;

import static org.junit.jupiter.api.Assertions.*;

public class MaxFlowTest {

    /**
     * test 1: caminho simples
     *
     * edges:
     * 1088 -> 906 (capacidade 15)
     * 906  -> 151 (capacidade 13)
     *
     * path:
     * 1088 -> 906 -> 151
     *
     * max flow = min(15, 13) = 13
     */
    @Test
    void test1_SimplePath() {

        int[][] capacity = new int[2000][2000];

        capacity[1088][906] = 15;
        capacity[906][151] = 13;

        int source = 1088;
        int sink = 151;

        int result = EdmondsKarpAlgorithm.edmondsKarp(capacity, source, sink);

        System.out.println("\n[Test 1 - Simple Path]");
        System.out.println("Expected max flow: 13");
        System.out.println("Actual max flow:   " + result);

        assertEquals(13, result);
    }

    /**
     * test 2: vários caminhos
     *
     * edges:
     * 219 -> 1663 (capacidade 24)
     * 219 -> 810 (capacidade 6)
     * 810 -> 1663 (capacidade6)
     * 219 -> 1048 (capacidade 11)
     * 1048 -> 1663 (capacidade 18)
     *
     * paths:
     * 219 -> 1663 = 24
     * 219 -> 810 -> 1663 = 6
     * 219 -> 1048 -> 1663 = 11
     *
     * total max flow = 41
     */
    @Test
    void test2_MultiplePaths() {

        int[][] capacity = new int[2000][2000];

        capacity[219][1663] = 24;
        capacity[219][810]  = 6;
        capacity[810][1663] = 6;
        capacity[219][1048] = 11;
        capacity[1048][1663] = 18;

        int source = 219;
        int sink = 1663;

        int result = EdmondsKarpAlgorithm.edmondsKarp(capacity, source, sink);

        System.out.println("\n[Test 2 - Multiple Paths]");
        System.out.println("Expected max flow: 41");
        System.out.println("Actual max flow:   " + result);

        assertEquals(41, result);
    }

    /**
     * teste 3: sem caminho entre a estação source e a estação sink
     *
     * Edges:
     * 906 -> 151 (capacidade 13)
     * 219 -> 1663 (capacidade 24)
     *
     * Expected max flow = 0
     */
    @Test
    void test3_NoPath() {

        int[][] capacity = new int[2000][2000];

        capacity[906][151] = 13;
        capacity[219][1663] = 24;

        int source = 906;
        int sink = 1663;

        int result = EdmondsKarpAlgorithm.edmondsKarp(capacity, source, sink);

        System.out.println("\n[Test 3 - No Path]");
        System.out.println("Expected max flow: 0");
        System.out.println("Actual max flow:   " + result);

        assertEquals(0, result);
    }
}

