package pt.ipp.isep.dei.domain.Graph;

/**
 * Representa uma aresta de um grafo genérico.
 *
 * Uma aresta liga dois nós, podendo ser direcionada ou não,
 * e opcionalmente possuir um peso associado.
 *
 * @param <V> tipo do valor armazenado nos nós
 * @param <E> tipo do peso associado à aresta
 */
public class Edge<V, E> {

    /**
     * Nó de origem da aresta.
     */
    private final Node<V> origin;

    /**
     * Nó de destino da aresta.
     */
    private final Node<V> destination;

    /**
     * Peso associado à aresta.
     */
    private final E weight;

    /**
     * Indica se a aresta é direcionada.
     */
    private final boolean directed;

    /**
     * Construtor da aresta.
     *
     * @param origin nó de origem
     * @param destination nó de destino
     * @param weight peso da aresta
     * @param directed indica se a aresta é direcionada
     */
    public Edge(Node<V> origin, Node<V> destination, E weight, boolean directed) {
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
        this.directed = directed;
    }

    /**
     * Obtém o nó de origem.
     *
     * @return nó de origem
     */
    public Node<V> getOrigin() {
        return origin;
    }

    /**
     * Obtém o nó de destino.
     *
     * @return nó de destino
     */
    public Node<V> getDestination() {
        return destination;
    }

    /**
     * Obtém o peso da aresta.
     *
     * @return peso da aresta
     */
    public E getWeight() {
        return weight;
    }

    /**
     * Indica se a aresta é direcionada.
     *
     * @return true se for direcionada
     */
    public boolean isDirected() {
        return directed;
    }

    /**
     * Indica se a aresta possui peso associado.
     *
     * @return true se for ponderada
     */
    public boolean isWeighted() {
        return weight != null;
    }

    /**
     * Obtém o valor do nó de origem.
     *
     * @return valor do nó de origem
     */
    public V getVOrig() {
        return origin.getValue();
    }

    /**
     * Obtém o valor do nó de destino.
     *
     * @return valor do nó de destino
     */
    public V getVDest() {
        return destination.getValue();
    }

    /**
     * Representação textual da aresta.
     *
     * @return string representativa da aresta
     */
    @Override
    public String toString() {
        String arrow = directed ? " -> " : " -- ";
        return origin + arrow + destination +
                (isWeighted() ? ("  [weight=" + weight + "]") : "");
    }
}
