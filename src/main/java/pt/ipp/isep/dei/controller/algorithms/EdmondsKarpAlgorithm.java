package pt.ipp.isep.dei.controller.algorithms;

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;

public class EdmondsKarpAlgorithm {

    public static int edmondsKarp(int[][] capacity, int source, int sink) { // O(1)
        int n = capacity.length; // O(1)
        List<List<Edge>> graph = new ArrayList<>(n); // O(1)

        // lista de adjacência
        for (int i = 0; i < n; i++) { // O(V)
            graph.add(new ArrayList<>()); // O(1) por iteração = total O(V)
        }

        // construir grafo a partir da matriz de capacidades
        for (int i = 0; i < n; i++) { // O(V)
            for (int j = 0; j < n; j++) { // O(V)
                if (capacity[i][j] > 0) { // O(1)
                    addEdge(graph, i, j, capacity[i][j]); // O(1)
                }
            }
        }
        // construção do grafo: O(V^2) no pior caso (matriz cheia)

        int maxFlow = 0; // O(1)
        int[] previousNode = new int[n]; // O(V)
        int[] previousEdge = new int[n]; // O(V)

        // loop principal: cada iteração encontra um caminho de aumento
        // número máximo de iterações do while: O(V*E) no pior caso
        while (bfs(graph, source, sink, previousNode, previousEdge)) { // O(V*E) chamadas no pior caso
            int pathFlow = Integer.MAX_VALUE; // O(1)
            int endNode = sink; // O(1)

            // encontrar fluxo mínimo no caminho de aumento
            while (endNode != source) { // O(V) iterações (caminho ≤ V-1)
                int startNode = previousNode[endNode]; // O(1)
                Edge edge = graph.get(startNode).get(previousEdge[endNode]); // O(1) + O(1) = O(1)
                pathFlow = Math.min(pathFlow, edge.capacity); // O(1)
                endNode = startNode; // O(1)
            }
            // total do loop por iteração do while: O(V)

            // atualizar capacidades ao longo do caminho
            endNode = sink; // O(1)
            while (endNode != source) { // O(V) iterações
                int startNode = previousNode[endNode]; // O(1)
                Edge edge = graph.get(startNode).get(previousEdge[endNode]); // O(1) + O(1) = O(1)
                Edge reverseEdge = graph.get(endNode).get(edge.reverseEdgeIndex); // O(1) + O(1) = O(1)
                edge.capacity -= pathFlow; // O(1)
                reverseEdge.capacity += pathFlow; // O(1)
                endNode = startNode; // O(1)
            }
            // total do loop por iteração do while: O(V)

            maxFlow += pathFlow; // O(1)
        }
        // complexidade total do while principal:
        // número máximo de iterações = O(V*E)
        // em cada iteração: BFS O(E) + percursos do caminho O(V) ≈ O(E)
        // = O(V*E) * O(E) = O(V*E^2)

        return maxFlow; // O(1)
    }

    private static class Edge {
        int targetNode; // O(1)
        int reverseEdgeIndex; // O(1)
        int capacity; // O(1)

        Edge(int targetNode, int reverseEdgeIndex, int capacity) { // O(1)
            this.targetNode = targetNode; // O(1)
            this.reverseEdgeIndex = reverseEdgeIndex; // O(1)
            this.capacity = capacity; // O(1)
        }
    }

    // adiciona uma aresta com capacidade e a sua reversa
    private static void addEdge(List<List<Edge>> graph, int startNode, int endNode, int capacity) { // O(1)
        Edge forwardEdge = new Edge(endNode, graph.get(endNode).size(), capacity); // O(1) + O(1) + O(1) = O(1)
        Edge reverseEdge = new Edge(startNode, graph.get(startNode).size(), 0); // O(1) + O(1) + O(1) = O(1)
        graph.get(startNode).add(forwardEdge); // O(1)
        graph.get(endNode).add(reverseEdge); // O(1)
    }

    // BFS para encontrar o caminho de aumento
    private static boolean bfs(List<List<Edge>> graph, int source, int sink, int[] previousNode, int[] previousEdge) {
        Arrays.fill(previousNode, -1); // O(V)
        Arrays.fill(previousEdge, -1); // O(V)
        boolean[] visitedNodes = new boolean[graph.size()]; // O(1) + O(V) = O(V)
        Queue<Integer> queue = new LinkedList<>(); // O(1)

        queue.add(source); // O(1)
        visitedNodes[source] = true; // O(1)

        while (!queue.isEmpty()) { // O(V) iterações de vértices, soma sobre todas as arestas: O(E)
            int startNode = queue.poll(); // O(1)
            for (int i = 0; i < graph.get(startNode).size(); i++) { // soma sobre todas arestas: O(E)
                Edge edge = graph.get(startNode).get(i); // O(1) + O(1) = O(1)
                int endNode = edge.targetNode; // O(1)
                if (!visitedNodes[endNode] && edge.capacity > 0) { // O(1)
                    visitedNodes[endNode] = true; // O(1)
                    previousNode[endNode] = startNode; // O(1)
                    previousEdge[endNode] = i; // O(1)
                    if (endNode == sink) { // O(1)
                        return true; // O(1)
                    }
                    queue.add(endNode); // O(1)
                }
            }
        }
        return false; // O(1)
    }
}
