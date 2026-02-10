package pt.ipp.isep.dei.domain.Graph;

/**
 * Interface responsável por fornecer um valor de peso.
 * <p>
 * Esta interface deve ser implementada por classes que representem
 * um elemento capaz de disponibilizar um valor numérico de peso,
 * normalmente utilizado em contextos como grafos, arestas ou custos.
 * </p>
 */
public interface WeightProvider {

    /**
     * Obtém o valor do peso associado à implementação.
     *
     * @return valor do peso sob a forma de um {@code double}
     */
    double getWeightValue();
}
