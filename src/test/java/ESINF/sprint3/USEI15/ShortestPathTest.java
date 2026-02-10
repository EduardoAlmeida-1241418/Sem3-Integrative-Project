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

class ShortestPathTest {

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
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("3"), nodeRepo.getNodeByKey("2"), new MetricsStationEdge(0, 0, -2), true)); // C -> B
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("4"), nodeRepo.getNodeByKey("1"), new MetricsStationEdge(0, 0, -4), true)); // D -> A
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("4"), nodeRepo.getNodeByKey("3"), new MetricsStationEdge(0, 0, -1), true)); // D -> C
        edgeRepo.addEdge(new Edge<>(nodeRepo.getNodeByKey("5"), nodeRepo.getNodeByKey("4"), new MetricsStationEdge(0, 0, 1), true)); // E -> D

        controller.createGraph();
    }

    /**
     * Test 1 – Simple path A -> C
     */
    @Test
    void test1_FindPathSingleEdge() {
        System.out.println("\nRunning Test 1 – Simple path A -> C");

        controller.setvOrigKey("1");
        controller.setvDestKey("3");
        controller.setSubgraphResult();

        List<Edge<StationEsinf, MetricsStationEdge>> resultEdges = controller.getEdgesOfSubgraph();

        assertEquals(1, resultEdges.size());
        System.out.println("\nExpected nº of edges: 1");
        System.out.println("Result nº of edges: " + resultEdges.size());

        String resultPath;
        assertEquals("1", resultEdges.getFirst().getOrigin().getKey());
        resultPath = resultEdges.getFirst().getOrigin().getValue().getStationName();
        assertEquals("3", resultEdges.getFirst().getDestination().getKey());
        resultPath += " -> " + resultEdges.getFirst().getDestination().getValue().getStationName();
        System.out.println("\nExpected path: A -> C");
        System.out.println("Result path: " + resultPath);

        assertEquals(2, controller.getTotalCostOfSubgraph());
        System.out.println("\nExpected cost: 2.0");
        System.out.println("Total cost: " + controller.getTotalCostOfSubgraph());
    }


    /**
     * Test 2 – Find path A -> C -> B
     */
    @Test
    void test2_FindPathTwoEdges() {
        System.out.println("\nRunning Test 2 – Find path A -> C -> B");

        controller.setvOrigKey("1");
        controller.setvDestKey("2");
        controller.setSubgraphResult();

        List<Edge<StationEsinf, MetricsStationEdge>> resultEdges = controller.getEdgesOfSubgraph();

        assertEquals(2, resultEdges.size());
        System.out.println("\nExpected nº of edges: 2");
        System.out.println("Result nº of edges: " + resultEdges.size());

        assertEquals("1", resultEdges.getFirst().getOrigin().getKey());
        assertEquals("3", resultEdges.getFirst().getDestination().getKey());
        assertEquals("3", resultEdges.get(1).getOrigin().getKey());
        assertEquals("2", resultEdges.get(1).getDestination().getKey());

        String resultPath = "";
        for (Edge<StationEsinf, MetricsStationEdge> edge : resultEdges) {
            resultPath += edge.getOrigin().getValue().getStationName() + " -> ";
        }
        resultPath += resultEdges.getLast().getDestination().getValue().getStationName();
        System.out.println("\nExpected path: A -> C -> B");
        System.out.println("Result path: " + resultPath);

        assertEquals(0, controller.getTotalCostOfSubgraph());
        System.out.println("\nExpected cost: 0.0");
        System.out.println("Total cost: " + controller.getTotalCostOfSubgraph());
    }

    /**
     * Test 3 – Choosing the shortest route (S -> E -> D -> A -> C)
     */
    @Test
    void test3_FindShortestPathChosen() {
        System.out.println("\nRunning Test 3 – Choosing the shortest route (S -> E -> D -> A -> C)");

        controller.setvOrigKey("0");
        controller.setvDestKey("3");
        controller.setSubgraphResult();

        List<Edge<StationEsinf, MetricsStationEdge>> resultEdges = controller.getEdgesOfSubgraph();

        assertEquals(4, resultEdges.size());
        System.out.println("\nExpected nº of edges: 4");
        System.out.println("Result nº of edges: " + resultEdges.size());

        assertEquals("0", resultEdges.getFirst().getOrigin().getKey());
        assertEquals("5", resultEdges.getFirst().getDestination().getKey());
        assertEquals("5", resultEdges.get(1).getOrigin().getKey());
        assertEquals("4", resultEdges.get(1).getDestination().getKey());
        assertEquals("4", resultEdges.get(2).getOrigin().getKey());
        assertEquals("1", resultEdges.get(2).getDestination().getKey());
        assertEquals("1", resultEdges.get(3).getOrigin().getKey());
        assertEquals("3", resultEdges.get(3).getDestination().getKey());

        String resultPath = "";
        for (Edge<StationEsinf, MetricsStationEdge> edge : resultEdges) {
            resultPath += edge.getOrigin().getValue().getStationName() + " -> ";
        }
        resultPath += resultEdges.getLast().getDestination().getValue().getStationName();
        System.out.println("\nExpected path: S -> E -> D -> A -> C");
        System.out.println("Result path: " + resultPath);

        assertEquals(7, controller.getTotalCostOfSubgraph());
        System.out.println("\nExpected cost: 7.0");
        System.out.println("Total cost: " + controller.getTotalCostOfSubgraph());
    }

    /**
     * Test 4 – Cost when there isn't path
     */
    @Test
    void test4_NoPathCostIsZero() {
        System.out.println("\nRunning Test 4 – Cost when there isn't path");

        controller.setvOrigKey("4");
        controller.setvDestKey("0");
        controller.setSubgraphResult();

        List<Edge<StationEsinf, MetricsStationEdge>> resultEdges = controller.getEdgesOfSubgraph();

        assertEquals(0, resultEdges.size());
        System.out.println("\nExpected nº of edges: 0");
        System.out.println("Result nº of edges: " + resultEdges.size());

        assertEquals(0, controller.getTotalCostOfSubgraph());
        System.out.println("\nExpected cost: 0.0");
        System.out.println("Total cost: " + controller.getTotalCostOfSubgraph());
    }

    /**
     * Test 5 – Cost with only one station (origin = destination)
     */
    @Test
    void test5_SameOriginAndDestinationCost() {
        System.out.println("\nRunning Test 5 – Cost with only one station (origin = destination)");

        controller.setvOrigKey("2");
        controller.setvDestKey("2");
        controller.setSubgraphResult();

        List<Edge<StationEsinf, MetricsStationEdge>> resultEdges = controller.getEdgesOfSubgraph();

        assertEquals(0, resultEdges.size());
        System.out.println("\nExpected nº of edges: 0");
        System.out.println("Result nº of edges: " + resultEdges.size());

        assertEquals(0, controller.getTotalCostOfSubgraph());
        System.out.println("\nExpected cost: 0.0");
        System.out.println("Total cost: " + controller.getTotalCostOfSubgraph());
    }
}
