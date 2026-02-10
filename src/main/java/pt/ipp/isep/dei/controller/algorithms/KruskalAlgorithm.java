package pt.ipp.isep.dei.controller.algorithms;

import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.Graph;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;

import java.util.*;

/*
    A partir de um grafo nao direcionado, esta classe constroi uma
    Minimum Spanning Tree (MST), ligando todos os vertices com o
    menor custo possivel, sem criar ciclos, utilizando como peso
    a distancia (distance).

    Complexidade temporal:
    O(V) + O(E) + O(E log E) + O(E * V)
    Final (simplificado): O(E * V).

    Obs.:
    - O algoritmo considera apenas ligacoes nao direcionadas unicas,
      evitando duplicacoes logicas como (a,b) e (b,a).
    - Self-loops (a,a) sao ignorados, pois nao fazem sentido numa MST.
*/

public class KruskalAlgorithm {

    /*
      Retorna a Minimum Spanning Tree (MST) de um grafo (g) ponderado e nao direcionado.

      Complexidade : .
     */
    public static Graph<StationEsinf, MetricsStationEdge> kruskal(
                  Graph<StationEsinf, MetricsStationEdge> g) {

        Graph<StationEsinf, MetricsStationEdge> mst = new Graph<>(false);       // Cria um novo Grafo MST nao direcionado. O(1)

        for (StationEsinf v : g.vertices()) {                                           // Adiciona todos os vertices do grafo original dentro do MST. O(V)
            mst.addVertex(v);                                                           // adiciona cada Vertice isolado. O(1)
        }

        // Cria a lista de arestas nao direcionadas unicas, eliminando duplicacoes logicas (a,b) == (b,a).

        List<Edge<StationEsinf, MetricsStationEdge>> edges =  new ArrayList<>();        // Cria uma lista de arestas candidatas. O(1)

        Set<String> visitedEdges =  new HashSet<>();                                    // Cria um conjunto Set para controlar ligacoes ja consideradas. O(1)

        for (Edge<StationEsinf, MetricsStationEdge> edge : g.edges()) {                 // Percorre todas as arestas do grafo original.  O(E)

            StationEsinf vOrig = edge.getVOrig();                                       // Guarda a estacao de origem.  O(1)
            StationEsinf vDest = edge.getVDest();                                       // Guarda a estacao destino.  O(1)

            if (shouldAddUndirectedEdge(vOrig, vDest, visitedEdges)) {
                edges.add(edge);                                                        // Adiciona apenas ligacoes validas e unicas. O(1)
            }
        }

         // Ordena as arestas por distancia (peso), em ordem crescente. O(E log E).
        Collections.sort(edges, new Comparator<Edge<StationEsinf, MetricsStationEdge>>() {
            @Override
            public int compare( Edge<StationEsinf, MetricsStationEdge> e1,
                                Edge<StationEsinf, MetricsStationEdge> e2) {

                return Double.compare(e1.getWeight().getDistance(),
                                      e2.getWeight().getDistance()
                );
            }
        }
        );


        // O(E * V) - Itera sobre as arestas ordenadas e constroi a MST.
        for (Edge<StationEsinf, MetricsStationEdge> edge : edges) {               // Itera sobre as arestas já ordenadas e constroi a MST, evitando ciclos atraves de DFS. O(E)

            StationEsinf vOrig = edge.getVOrig();                                 // Obtem o vertice de origem da aresta atual O(1)
            StationEsinf vDest = edge.getVDest();                                 // Obtem o vertice de destino da aresta atual. O(1)

            Set<StationEsinf> connectedVerts =
                    new HashSet<>();                                              // cria um conjunto para guardar todos os vertices alcancaveis no MST. O(1)

            depthFirstSearch(mst, vOrig, connectedVerts);                         // DFS no MST atual. O(V)

            if (!connectedVerts.contains(vDest)) {                                // Se nao cria ciclo. O(1)
                mst.addEdge(vOrig, vDest, edge.getWeight());                      // Adiciona a aresta ao MST. O(1)
            }
                                                                                  // Caso contrario, a aresta e descartada por criar ciclo
        }

        return mst;
    }


    /*
      Este bloco cria uma identificação única para uma ligação entre dois vértices,
      evita contar a mesma ligação duas vezes em grafos não direcionados
      Garante que (a,b) == (b,a) e ignora self-loops.
     */
    private static boolean shouldAddUndirectedEdge( StationEsinf vOrig, StationEsinf vDest,
                                                    Set<String> visitedEdges) {
        if (vOrig.equals(vDest)) {                           // Ignora self-loops. O(1)
            return false;
        }

        String origId = vOrig.getId();                       // Guarda o id da estacao de origem. O(1)
        String destId = vDest.getId();                       // Guarda o id da estacao destino. O(1)

        String key;                                          // Cria uma variável que irá guardar a representação textual da ligação entre dois vértices. O(1)

        if (origId.compareTo(destId) <= 0) {                 // Compara se o id da estacao de origem é menor ou igual ao id da estacao destino. O(1)
            key = origId + "-" + destId;                     // Guarda em Key a ligacao textual dos vértices em ordem (crescente) dos ids. O(1)
        } else {
            key = destId + "-" + origId;
        }

        if (visitedEdges.contains(key)) {
            return false;                                    // Se esta ligacao entre os dois vertices ja foi considerada, ignora duplicacoes. hashSet.contains == O(1).
        }

        visitedEdges.add(key);                               // Se nao foi considerada, registra a ligacao como visitada.
        return true;
    }


    /*
      Percorre recursivamente o MST para identificar todos os
      vertices alcancaveis a partir de um vertice de origem.
      Complexidade: O(V + E) == O(V + (V-1)) == O(2V - 1) == O(V).
     */
    private static void depthFirstSearch(Graph<StationEsinf, MetricsStationEdge> g,
                                         StationEsinf current, Set<StationEsinf> visited) {

        visited.add(current);                               // Marca vertice como visitado. O(1)

        for (StationEsinf adj : g.adjVertices(current)) {  // Percorre adjacentes. O(E)
            if (!visited.contains(adj)) {                  // O(1)
                depthFirstSearch(g, adj, visited);         // Chamada recursiva.
            }
        }
    }
}




/*
Resumo rapido
=============

StationEsinf → Vertice do grafo (estacao ferroviaria), identificado unicamente por getId()

g → grafo original nao direcionado (rede ferroviaria completa)
mst → grafo da Minimum Spanning Tree (resultado final)

edges → lista de arestas nao direcionadas unicas, sem duplicacoes logicas

visitedEdges → conjunto auxiliar que garante que (a,b) e (b,a)
               sejam tratadas como a mesma ligacao

shouldAddUndirectedEdge(...) →
    - ignora self-loops
    - normaliza ligacoes nao direcionadas
    - controla duplicacoes de arestas

Ordenacao →
    - arestas ordenadas por distancia crescente (peso)

DFS →
    - usado para verificar se uma nova aresta criaria ciclo no MST

Resultado →
    - MST conexa
    - sem ciclos
    - com V - 1 arestas
    - comprimento total minimo
*/
