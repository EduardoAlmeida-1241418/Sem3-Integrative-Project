package ESINF.sprint3.USEI11;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.infrastructurePlanner.RailwayUpgradePlanningController;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint3.EdgeEsinfRepository;
import pt.ipp.isep.dei.data.repository.sprint3.NodeEsinfRepository;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;
import pt.ipp.isep.dei.domain.Graph.Node;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RailwayUpgradePlanningTest {

    private NodeEsinfRepository nodeRepo;
    private EdgeEsinfRepository edgeRepo;

    private StationEsinf A, B, C, D;

    @BeforeEach
    void setUp() {

        nodeRepo = Repositories.getInstance().getNodeEsinfRepository();
        edgeRepo = Repositories.getInstance().getEdgeEsinfRepository();

        nodeRepo.clear();
        edgeRepo.clear();

        A = new StationEsinf("1", "A", 0, 0);
        B = new StationEsinf("2", "B", 0, 0);
        C = new StationEsinf("3", "C", 0, 0);
        D = new StationEsinf("4", "D", 0, 0);

        nodeRepo.addNode(new Node<>(A, A.getId()));
        nodeRepo.addNode(new Node<>(B, B.getId()));
        nodeRepo.addNode(new Node<>(C, C.getId()));
        nodeRepo.addNode(new Node<>(D, D.getId()));
    }

    private MetricsStationEdge w() {
        return new MetricsStationEdge(0, 0, 1);
    }

    private void printOrder(List<StationEsinf> order) {
        System.out.print("Upgrade order: ");
        if (order == null || order.isEmpty()) {
            System.out.println("(empty)");
            return;
        }
        for (int i = 0; i < order.size(); i++) {
            System.out.print(order.get(i).getStationName());
            if (i < order.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
    }

    /**
     * Test 1 – DEFAULT (sem ciclos) -> ordem completa dos 4 vértices
     *
     * Ligações:
     * A -> B
     * B -> C
     * B -> D
     * C -> D
     *
     * Esperado (User Story):
     * - sem ciclo
     * - ordem contém TODOS os vértices (4)
     * - respeita todas as dependências
     */
    @Test
    void test1_Default_NoCycle() {

        // A -> B
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("1"), nodeRepo.getNodeByKey("2"), w(), true));

        // B -> C
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("2"), nodeRepo.getNodeByKey("3"), w(), true));

        // B -> D
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("2"), nodeRepo.getNodeByKey("4"), w(), true));

        // C -> D
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("3"), nodeRepo.getNodeByKey("4"), w(), true));

        RailwayUpgradePlanningController controller = new RailwayUpgradePlanningController();

        controller.computeUpgradePlan("DEFAULT", null, null, null);

        List<StationEsinf> order = controller.getUpgradeOrder();

        System.out.println("\n[Test 1 – DEFAULT no cycle]");
        System.out.println("Has cycle: " + controller.hasCycle());
        printOrder(order);

        assertFalse(controller.hasCycle());

        assertEquals(4, order.size());
        assertTrue(order.contains(A));
        assertTrue(order.contains(B));
        assertTrue(order.contains(C));
        assertTrue(order.contains(D));

        assertTrue(order.indexOf(A) < order.indexOf(B)); // A -> B
        assertTrue(order.indexOf(B) < order.indexOf(C)); // B -> C
        assertTrue(order.indexOf(B) < order.indexOf(D)); // B -> D
        assertTrue(order.indexOf(C) < order.indexOf(D)); // C -> D
    }


    /**
     * Test 2 – ID Interval [2,4]
     *
     * A -> B -> C -> D
     * intervalo [B,C,D]
     *
     * Esperado:
     * - sem ciclo
     * - ordem contém B, C e D (3 vértices do subgrafo)
     * - respeita dependências (B antes de C antes de D)
     */
    @Test
    void test2_IdInterval_NoCycle() {

        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("1"), nodeRepo.getNodeByKey("2"), w(), true)); // A -> B
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("2"), nodeRepo.getNodeByKey("3"), w(), true)); // B -> C
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("3"), nodeRepo.getNodeByKey("4"), w(), true)); // C -> D

        RailwayUpgradePlanningController controller = new RailwayUpgradePlanningController();
        controller.computeUpgradePlan("ID Interval", "2", "4", null);

        List<StationEsinf> order = controller.getUpgradeOrder();

        System.out.println("\n[Test 2 – ID Interval 2..4]");
        System.out.println("Has cycle: " + controller.hasCycle());
        printOrder(order);

        assertFalse(controller.hasCycle());

        assertEquals(3, order.size());
        assertTrue(order.contains(B));
        assertTrue(order.contains(C));
        assertTrue(order.contains(D));
        assertFalse(order.contains(A));

        assertTrue(order.indexOf(B) < order.indexOf(C));
        assertTrue(order.indexOf(C) < order.indexOf(D));
    }

    /**
     * Test 3 – Single Station Analysis (A)
     *
     * A -> B
     * A -> C
     * A -> D
     *
     * Esperado:
     * - sem ciclo
     * - ordem contém A,B,C,D
     * - A antes de B/C/D
     */
    @Test
    void test3_SingleStationAnalysis_A() {

        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("1"), nodeRepo.getNodeByKey("2"), w(), true)); // A -> B
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("1"), nodeRepo.getNodeByKey("3"), w(), true)); // A -> C
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("1"), nodeRepo.getNodeByKey("4"), w(), true)); // A -> D

        RailwayUpgradePlanningController controller = new RailwayUpgradePlanningController();
        controller.computeUpgradePlan("Single Station Analysis", null, null, "1");

        List<StationEsinf> order = controller.getUpgradeOrder();

        System.out.println("\n[Test 3 – Single Station Analysis (A)]");
        System.out.println("Has cycle: " + controller.hasCycle());
        printOrder(order);

        assertFalse(controller.hasCycle());

        assertEquals(4, order.size());
        assertTrue(order.contains(A));
        assertTrue(order.contains(B));
        assertTrue(order.contains(C));
        assertTrue(order.contains(D));

        assertTrue(order.indexOf(A) < order.indexOf(B));
        assertTrue(order.indexOf(A) < order.indexOf(C));
        assertTrue(order.indexOf(A) < order.indexOf(D));
    }

    /**
     * Test 4 – DEFAULT com ciclo
     *
     * A -> B -> C -> A (ciclo)
     * D isolado (sem arestas)
     *
     * Esperado:
     * - com ciclo
     * - cycleEdges não vazio
     */
    @Test
    void test4_Default_WithCycle() {

        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("1"), nodeRepo.getNodeByKey("2"), w(), true)); // A -> B
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("2"), nodeRepo.getNodeByKey("3"), w(), true)); // B -> C
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("3"), nodeRepo.getNodeByKey("1"), w(), true)); // C -> A


        RailwayUpgradePlanningController controller = new RailwayUpgradePlanningController();
        controller.computeUpgradePlan("DEFAULT", null, null, null);

        List<StationEsinf> order = controller.getUpgradeOrder();
        Set<String> cycleEdges = controller.getCycleEdges();

        System.out.println("\n[Test 4 – DEFAULT with cycle]");
        System.out.println("Has cycle: " + controller.hasCycle());
        printOrder(order);
        System.out.println("Cycle edges: " + cycleEdges);

        assertTrue(controller.hasCycle());
        assertFalse(cycleEdges.isEmpty());

        assertTrue(
                cycleEdges.contains("A -> B") ||
                        cycleEdges.contains("B -> C") ||
                        cycleEdges.contains("C -> A")
        );
    }
}
