package pt.ipp.isep.dei.domain.Graph;

/**
 * Representa as métricas associadas a uma aresta entre estações.
 *
 * Esta classe encapsula informações de distância, capacidade
 * e custo, sendo utilizada como peso das arestas no grafo.
 */
public class MetricsStationEdge implements WeightProvider {

    /**
     * Distância entre estações.
     */
    private final double distance;

    /**
     * Capacidade da ligação.
     */
    private final int capacity;

    /**
     * Custo associado à ligação.
     */
    private final double cost;

    /**
     * Construtor das métricas da aresta.
     *
     * @param distance distância entre estações
     * @param capacity capacidade da ligação
     * @param cost custo da ligação
     */
    public MetricsStationEdge(double distance, int capacity, double cost) {
        this.distance = distance;
        this.capacity = capacity;
        this.cost = cost;
    }

    /**
     * Obtém a distância associada à aresta.
     *
     * @return distância
     */
    public double getDistance() { return distance; }

    /**
     * Obtém a capacidade associada à aresta.
     *
     * @return capacidade
     */
    public int getCapacity() { return capacity; }

    /**
     * Obtém o custo associado à aresta.
     *
     * @return custo
     */
    public double getCost() { return cost; }

    /**
     * Obtém o valor do peso da aresta.
     *
     * O custo é utilizado como valor de peso
     * para algoritmos de grafos.
     *
     * @return valor do peso
     */
    @Override
    public double getWeightValue() {
        return cost;
    }
}
