package ESINF.sprint3.USEI15;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.routePlanner.ShortestPathController;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint3.EdgeEsinfRepository;
import pt.ipp.isep.dei.data.repository.sprint3.NodeEsinfRepository;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.Graph;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;
import pt.ipp.isep.dei.domain.Graph.Node;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NegativeCycleTest {

    private ShortestPathController controller;
    private NodeEsinfRepository nodeRepo;
    private EdgeEsinfRepository edgeRepo;

    private Graph<StationEsinf, MetricsStationEdge> graph;

    @BeforeEach
    void setUp() {
        controller = new ShortestPathController();

        nodeRepo = Repositories.getInstance().getNodeEsinfRepository();
        edgeRepo = Repositories.getInstance().getEdgeEsinfRepository();
        nodeRepo.clear();
        edgeRepo.clear();

        StationEsinf s = new StationEsinf("0","S", 0,0); // Station S
        StationEsinf a = new StationEsinf("1","A", 0,0); // Station A
        StationEsinf b = new StationEsinf("2","B", 0,0); // Station B
        StationEsinf c = new StationEsinf("3","C", 0,0); // Station C
        StationEsinf d = new StationEsinf("4","D", 0,0); // Station D
        StationEsinf e = new StationEsinf("5","E", 0,0); // Station E

        nodeRepo.addNode(new Node<>(s, s.getId()));
        nodeRepo.addNode(new Node<>(a, a.getId()));
        nodeRepo.addNode(new Node<>(b, b.getId()));
        nodeRepo.addNode(new Node<>(c, c.getId()));
        nodeRepo.addNode(new Node<>(d, d.getId()));
        nodeRepo.addNode(new Node<>(e, e.getId()));

        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("0"), nodeRepo.getNodeByKey("1"), new MetricsStationEdge(0, 0, 10), true)); // S -> A
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("0"), nodeRepo.getNodeByKey("5"), new MetricsStationEdge(0, 0, 8), true)); // S -> E
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("1"), nodeRepo.getNodeByKey("3"), new MetricsStationEdge(0, 0, 2), true)); // A -> C
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("2"), nodeRepo.getNodeByKey("1"), new MetricsStationEdge(0, 0, 1), true)); // B -> A
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("3"), nodeRepo.getNodeByKey("2"), new MetricsStationEdge(0, 0, -5), true)); // C -> B
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("4"), nodeRepo.getNodeByKey("1"), new MetricsStationEdge(0, 0, -4), true)); // D -> A
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("4"), nodeRepo.getNodeByKey("3"), new MetricsStationEdge(0, 0, -1), true)); // D -> C
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("5"), nodeRepo.getNodeByKey("4"), new MetricsStationEdge(0, 0, 1), true)); // E -> D

        controller.createGraph();
    }

    /**
     * Test 1 – Negative cycle E -> S, no path
     */
    @Test
    void test1_NegativeCycleNoPath() {
        System.out.println("\nRunning Test 1 – Negative cycle E -> S, no path");

        controller.setvOrigKey("5");
        controller.setvDestKey("0");
        controller.setSubgraphResult();

        List<Edge<StationEsinf, MetricsStationEdge>> negativeEdges = controller.getEdgesOfNegativeCycle();

        assertEquals(3, negativeEdges.size());
        System.out.println("\nExpected nº of edges NC: 3");
        System.out.println("Result nº of edges NC: " + negativeEdges.size());

        String resultPath;
        assertEquals("1", negativeEdges.getFirst().getOrigin().getKey());
        resultPath = negativeEdges.getFirst().getOrigin().getValue().getStationName();
        assertEquals("3", negativeEdges.getFirst().getDestination().getKey());
        resultPath += " -> " + negativeEdges.getFirst().getDestination().getValue().getStationName();
        assertEquals("2", negativeEdges.get(1).getDestination().getKey());
        resultPath += " -> " + negativeEdges.get(1).getDestination().getValue().getStationName();
        assertEquals("1", negativeEdges.get(2).getDestination().getKey());
        resultPath += " -> " + negativeEdges.get(2).getDestination().getValue().getStationName();
        System.out.println("\nExpected path NC: A -> C -> B -> A");
        System.out.println("Result path NC: " + resultPath);

        List<Edge<StationEsinf, MetricsStationEdge>> resultEdges = controller.getEdgesOfSubgraph();

        assertEquals(0, resultEdges.size());
        System.out.println("\nExpected nº of edges: 0");
        System.out.println("Result nº of edges: " + resultEdges.size());
    }


    /**
     * Test 2 – Negative cycle unrelated the path
     */
    @Test
    void test2_NegativeCycleUnrelatedThePath() {
        System.out.println("\nRunning Test 2 – Negative cycle unrelated the path");

        controller.setvOrigKey("5");
        controller.setvDestKey("3");
        controller.setSubgraphResult();

        List<Edge<StationEsinf, MetricsStationEdge>> negativeEdges = controller.getEdgesOfNegativeCycle();

        assertEquals(3, negativeEdges.size());
        System.out.println("\nExpected nº of edges: 3");
        System.out.println("Result nº of edges: " + negativeEdges.size());

        String resultPath;
        assertEquals("1", negativeEdges.getFirst().getOrigin().getKey());
        resultPath = negativeEdges.getFirst().getOrigin().getValue().getStationName();
        assertEquals("3", negativeEdges.getFirst().getDestination().getKey());
        resultPath += " -> " + negativeEdges.getFirst().getDestination().getValue().getStationName();
        assertEquals("2", negativeEdges.get(1).getDestination().getKey());
        resultPath += " -> " + negativeEdges.get(1).getDestination().getValue().getStationName();
        assertEquals("1", negativeEdges.get(2).getDestination().getKey());
        resultPath += " -> " + negativeEdges.get(2).getDestination().getValue().getStationName();
        System.out.println("\nExpected path: A -> C -> B -> A");
        System.out.println("Result path: " + resultPath);
    }

    /**
     * Test 3 – Cost with negative cycle but shortest distance chosen, A -> C -> B
     */
    @Test
    void test3_CostPathWithNegativeCycle() {
        System.out.println("\nRunning Test 3 – Cost with negative cycle but shortest distance chosen, A -> C -> B");

        controller.setvOrigKey("1");
        controller.setvDestKey("2");
        controller.setSubgraphResult();

        List<Edge<StationEsinf, MetricsStationEdge>> negativeEdges = controller.getEdgesOfNegativeCycle();

        assertEquals(3, negativeEdges.size());
        System.out.println("\nExpected nº of edges NC: 3");
        System.out.println("Result nº of edges NC: " + negativeEdges.size());

        String resultPath;
        assertEquals("1", negativeEdges.getFirst().getOrigin().getKey());
        resultPath = negativeEdges.getFirst().getOrigin().getValue().getStationName();
        assertEquals("3", negativeEdges.getFirst().getDestination().getKey());
        resultPath += " -> " + negativeEdges.getFirst().getDestination().getValue().getStationName();
        assertEquals("2", negativeEdges.get(1).getDestination().getKey());
        resultPath += " -> " + negativeEdges.get(1).getDestination().getValue().getStationName();
        assertEquals("1", negativeEdges.get(2).getDestination().getKey());
        resultPath += " -> " + negativeEdges.get(2).getDestination().getValue().getStationName();
        System.out.println("\nExpected path NC: A -> C -> B -> A");
        System.out.println("Result path NC: " + resultPath);

        List<Edge<StationEsinf, MetricsStationEdge>> resultEdges = controller.getEdgesOfSubgraph();

        assertEquals(2, resultEdges.size());
        System.out.println("\nExpected nº of edges: 2");
        System.out.println("Result nº of edges: " + resultEdges.size());

        assertEquals("1", resultEdges.getFirst().getOrigin().getKey());
        assertEquals("3", resultEdges.getFirst().getDestination().getKey());
        assertEquals("3", resultEdges.get(1).getOrigin().getKey());
        assertEquals("2", resultEdges.get(1).getDestination().getKey());
        String resultPathSP = "";
        for (Edge<StationEsinf, MetricsStationEdge> edge : resultEdges) {
            resultPathSP += edge.getOrigin().getValue().getStationName() + " -> ";
        }
        resultPathSP += resultEdges.getLast().getDestination().getValue().getStationName();
        System.out.println("\nExpected path: A -> C -> B");
        System.out.println("Result path: " + resultPathSP);

        assertEquals(-3, controller.getTotalCostOfSubgraph());
        System.out.println("\nExpected cost: -3");
        System.out.println("Total cost: " + controller.getTotalCostOfSubgraph());
    }

    /**
     * Test 4 – No Negative Cycle, unattainable
     */
    @Test
    void test4_NegativeCycleUnattainable() {
        System.out.println("\nRunning Test 4 – No Negative Cycle, unattainable");

        StationEsinf f = new StationEsinf("6","F", 0,0); // Station F
        StationEsinf g = new StationEsinf("7","G", 0,0); // Station G

        nodeRepo.addNode(new Node<>(f, f.getId()));
        nodeRepo.addNode(new Node<>(g, g.getId()));

        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("4"), nodeRepo.getNodeByKey("6"), new MetricsStationEdge(0, 0, 3), true)); // D -> F
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("6"), nodeRepo.getNodeByKey("7"), new MetricsStationEdge(0, 0, 2), true)); // F -> G

        controller.setvOrigKey("6");
        controller.setvDestKey("7");
        controller.setSubgraphResult();

        List<Edge<StationEsinf, MetricsStationEdge>> negativeEdges = controller.getEdgesOfNegativeCycle();

        assertEquals(0, negativeEdges.size());
        System.out.println("\nExpected nº of edges NC: 0");
        System.out.println("Result nº of edges NC: " + negativeEdges.size());
    }

    /**
     * Test 5 – Negative Cycle with same origin and destination
     */
    @Test
    void test5_NegativeCycleSameStation() {
        System.out.println("\nRunning Test 5 – Negative Cycle with same origin and destination");

        controller.setvOrigKey("1");
        controller.setvDestKey("1");
        controller.setSubgraphResult();

        List<Edge<StationEsinf, MetricsStationEdge>> negativeEdges = controller.getEdgesOfNegativeCycle();

        assertEquals(3, negativeEdges.size());
        System.out.println("\nExpected nº of edges NC: 3");
        System.out.println("Result nº of edges NC: " + negativeEdges.size());

        String resultPath;
        assertEquals("1", negativeEdges.getFirst().getOrigin().getKey());
        resultPath = negativeEdges.getFirst().getOrigin().getValue().getStationName();
        assertEquals("3", negativeEdges.getFirst().getDestination().getKey());
        resultPath += " -> " + negativeEdges.getFirst().getDestination().getValue().getStationName();
        assertEquals("2", negativeEdges.get(1).getDestination().getKey());
        resultPath += " -> " + negativeEdges.get(1).getDestination().getValue().getStationName();
        assertEquals("1", negativeEdges.get(2).getDestination().getKey());
        resultPath += " -> " + negativeEdges.get(2).getDestination().getValue().getStationName();
        System.out.println("\nExpected path NC: A -> C -> B -> A");
        System.out.println("Result path NC: " + resultPath);
    }
}
