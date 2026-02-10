package pt.ipp.isep.dei.domain.Graph;

import java.util.*;

/**
 * Implementação genérica de um grafo.
 *
 * O grafo pode ser direcionado ou não direcionado e suporta
 * operações de adição e remoção de vértices e arestas,
 * bem como consultas estruturais e de adjacência.
 *
 * @param <V> tipo do valor armazenado nos vértices
 * @param <E> tipo do peso associado às arestas
 */
public class Graph<V, E> {

    /**
     * Indica se o grafo é direcionado.
     */
    private final boolean directed;

    /**
     * Estrutura de adjacência que associa cada nó
     * às suas arestas de saída.
     */
    private final Map<Node<V>, Map<Node<V>, Edge<V, E>>> adj;

    /**
     * Lista de nós (vértices) do grafo.
     */
    private final List<Node<V>> vertices;

    /**
     * Construtor do grafo.
     *
     * @param directed true se o grafo for direcionado
     */
    public Graph(boolean directed) {
        this.directed = directed;
        this.adj = new HashMap<>();
        this.vertices = new ArrayList<>();
    }

    /**
     * Indica se o grafo é direcionado.
     *
     * @return true se for direcionado
     */
    public boolean isDirected() {
        return directed;
    }

    /**
     * Adiciona um vértice ao grafo com uma chave explícita.
     *
     * @param value valor do vértice
     * @param key chave associada ao vértice
     * @return true se o vértice foi adicionado
     */
    public boolean addVertex(V value, String key) {
        Node<V> n = new Node<>(value, key);
        if (adj.containsKey(n)) return false;
        adj.put(n, new HashMap<>());
        vertices.add(n);
        return true;
    }

    /**
     * Adiciona um vértice ao grafo usando o valor
     * como chave por defeito.
     *
     * @param value valor do vértice
     * @return true se o vértice foi adicionado
     */
    public boolean addVertex(V value) {
        return addVertex(value, value.toString());
    }

    /**
     * Adiciona uma aresta entre dois vértices.
     *
     * @param orig vértice de origem
     * @param dest vértice de destino
     * @param weight peso da aresta
     * @return true se a aresta foi adicionada
     */
    public boolean addEdge(V orig, V dest, E weight) {
        Node<V> u = getNode(orig);
        Node<V> v = getNode(dest);
        if (u == null || v == null) return false;

        Edge<V, E> e = new Edge<>(u, v, weight, directed);
        adj.get(u).put(v, e);

        if (!directed) {
            Edge<V, E> rev = new Edge<>(v, u, weight, false);
            adj.get(v).put(u, rev);
        }

        return true;
    }

    /**
     * Remove um vértice do grafo.
     *
     * @param value valor do vértice
     * @return true se o vértice foi removido
     */
    public boolean removeVertex(V value) {
        Node<V> n = getNode(value);
        if (n == null) return false;

        adj.remove(n);

        for (Map<Node<V>, Edge<V, E>> m : adj.values()) {
            m.remove(n);
        }

        vertices.remove(n);
        return true;
    }

    /**
     * Remove uma aresta entre dois vértices.
     *
     * @param orig vértice de origem
     * @param dest vértice de destino
     * @return true se a aresta foi removida
     */
    public boolean removeEdge(V orig, V dest) {
        Node<V> u = getNode(orig);
        Node<V> v = getNode(dest);

        if (u == null || v == null) return false;

        boolean removed = adj.get(u).remove(v) != null;

        if (!directed) adj.get(v).remove(u);

        return removed;
    }

    /**
     * Obtém o número de vértices do grafo.
     *
     * @return número de vértices
     */
    public int numVertices() {
        return vertices.size();
    }

    /**
     * Obtém o número de arestas do grafo.
     *
     * @return número de arestas
     */
    public int numEdges() {
        int count = 0;
        for (var m : adj.values()) count += m.size();
        return count;
    }

    // Complexity O(V)
    /**
     * Obtém a lista de valores dos vértices.
     *
     * @return lista de vértices
     */
    public ArrayList<V> vertices() {
        ArrayList<V> list = new ArrayList<>();
        for (Node<V> n : vertices) list.add(n.getValue()); // Complexity O(V)
        return list;
    }

    /**
     * Obtém a lista de nós do grafo.
     *
     * @return lista de nós
     */
    public List<Node<V>> getVerticesNodes() {
        return vertices;
    }

    /**
     * Verifica se um vértice é válido.
     *
     * @param vert vértice
     * @return true se existir no grafo
     */
    public boolean validVertex(V vert) {
        return getNode(vert) != null;
    }

    /**
     * Obtém a chave (índice) de um vértice.
     *
     * @param vert vértice
     * @return índice do vértice ou -1
     */
    public int key(V vert) {
        Node<V> n = getNode(vert);
        if (n == null) return -1;
        return vertices.indexOf(n);
    }

    /**
     * Obtém o valor do vértice por índice.
     *
     * @param key índice
     * @return valor do vértice
     */
    public V vertex(int key) {
        if (key < 0 || key >= vertices.size()) return null;
        return vertices.get(key).getValue();
    }

    /**
     * Verifica se existe um vértice com determinada chave.
     *
     * @param key chave
     * @return true se existir
     */
    public boolean existVertexByKey(String key) {
        for (Node<V> n : vertices) {
            if (n.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtém o valor de um vértice pela chave.
     *
     * @param key chave
     * @return valor do vértice
     */
    public V vertexByKey(String key) {
        for (Node<V> n : vertices) {
            if (n.getKey().equals(key)) {
                return n.getValue();
            }
        }
        return null;
    }

    /**
     * Obtém o valor de um vértice pela chave numérica.
     *
     * @param id identificador
     * @return valor do vértice
     */
    public V vertexByKey(int id) {
        return vertexByKey(String.valueOf(id));
    }

    // Complexity O(E)
    /**
     * Obtém os vértices adjacentes a um vértice.
     *
     * @param vert vértice
     * @return coleção de vértices adjacentes
     */
    public Collection<V> adjVertices(V vert) {
        Node<V> n = getNode(vert);
        if (n == null) return List.of();
        ArrayList<V> list = new ArrayList<>();
        for (Node<V> v : adj.get(n).keySet()) list.add(v.getValue()); // Complexity O(E)
        return list;
    }

    /**
     * Obtém todas as arestas do grafo.
     *
     * @return coleção de arestas
     */
    public Collection<Edge<V, E>> edges() {
        ArrayList<Edge<V, E>> list = new ArrayList<>();
        for (var m : adj.values()) list.addAll(m.values());
        return list;
    }

    /**
     * Obtém a lista de arestas do grafo.
     *
     * @return lista de arestas
     */
    public ArrayList<Edge<V, E>> getEdgesList() {
        ArrayList<Edge<V, E>> list = new ArrayList<>();
        for (var m : adj.values()) list.addAll(m.values());
        return list;
    }

    /**
     * Obtém a aresta entre dois vértices.
     *
     * @param vOrig vértice de origem
     * @param vDest vértice de destino
     * @return aresta correspondente
     */
    public Edge<V, E> edge(V vOrig, V vDest) {
        Node<V> u = getNode(vOrig);
        Node<V> v = getNode(vDest);
        if (u == null || v == null) return null;
        return adj.get(u).get(v);
    }

    /**
     * Obtém uma aresta independentemente da direção.
     *
     * @param vOrig vértice de origem
     * @param vDest vértice de destino
     * @return aresta encontrada
     */
    public Edge<V, E> edgeNotDirect(V vOrig, V vDest) {
        Node<V> u = getNode(vOrig);
        Node<V> v = getNode(vDest);
        if (u == null || v == null) return null;
        if (adj.get(v).get(u) != null) return adj.get(v).get(u);
        return adj.get(u).get(v);
    }

    /**
     * Obtém a aresta por índices dos vértices.
     *
     * @param origKey índice de origem
     * @param destKey índice de destino
     * @return aresta correspondente
     */
    public Edge<V, E> edge(int origKey, int destKey) {
        V vo = vertex(origKey);
        V vd = vertex(destKey);
        return edge(vo, vd);
    }

    /**
     * Obtém o grau de saída de um vértice.
     *
     * @param vert vértice
     * @return grau de saída
     */
    public int outDegree(V vert) {
        Node<V> n = getNode(vert);
        if (n == null) return -1;
        return adj.get(n).size();
    }

    // Complexity O(V)
    /**
     * Obtém o grau de entrada de um vértice.
     *
     * @param vert vértice
     * @return grau de entrada
     */
    public int inDegree(V vert) {
        Node<V> v = getNode(vert);
        int count = 0;
        for (var m : adj.values()) if (m.containsKey(v)) count++; // Complexity O(V)
        return count;
    }

    /**
     * Obtém as arestas de saída de um vértice.
     *
     * @param vert vértice
     * @return coleção de arestas
     */
    public Collection<Edge<V, E>> outgoingEdges(V vert) {
        Node<V> n = getNode(vert);
        if (n == null) return List.of();
        return adj.get(n).values();
    }

    /**
     * Obtém as arestas de entrada de um vértice.
     *
     * @param vert vértice
     * @return coleção de arestas
     */
    public Collection<Edge<V, E>> incomingEdges(V vert) {
        Node<V> v = getNode(vert);
        ArrayList<Edge<V, E>> list = new ArrayList<>();
        for (var m : adj.values()) {
            for (var e : m.values()) {
                if (e.getDestination().equals(v)) list.add(e);
            }
        }
        return list;
    }

    /**
     * Cria uma cópia do grafo.
     *
     * @return novo grafo clonado
     */
    public Graph<V, E> clone() {
        Graph<V, E> g2 = new Graph<>(directed);
        for (Node<V> n : vertices) {
            g2.addVertex(n.getValue(), n.getKey());
        }
        for (Edge<V, E> e : edges()) {
            g2.addEdge(e.getOrigin().getValue(), e.getDestination().getValue(), e.getWeight());
        }
        return g2;
    }

    /**
     * Obtém o nó correspondente a um valor.
     *
     * @param value valor do nó
     * @return nó correspondente
     */
    private Node<V> getNode(V value) {
        for (Node<V> n : vertices) {
            if (n.getValue().equals(value)) return n;
        }
        return null;
    }
}
