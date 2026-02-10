package pt.ipp.isep.dei.domain.Graph;

/**
 * Representa um nó genérico de um grafo.
 *
 * Cada nó contém um valor associado e uma chave única
 * utilizada para identificação, comparação e hashing.
 *
 * @param <V> tipo do valor armazenado no nó
 */
public class Node<V> {

    /**
     * Valor armazenado no nó.
     */
    private final V value;

    /**
     * Chave única de identificação do nó.
     */
    private final String key;

    /**
     * Construtor do nó.
     *
     * @param value valor associado ao nó
     * @param key chave única do nó
     */
    public Node(V value, String key) {
        this.value = value;
        this.key = key;
    }

    /**
     * Obtém o valor do nó.
     *
     * @return valor do nó
     */
    public V getValue() {
        return value;
    }

    /**
     * Obtém a chave do nó.
     *
     * @return chave do nó
     */
    public String getKey() {
        return key;
    }

    /**
     * Representação textual do nó.
     *
     * @return string representativa do valor do nó
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * Calcula o hash code do nó com base na sua chave.
     *
     * @return valor do hash code
     */
    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * Compara este nó com outro objeto.
     *
     * Dois nós são considerados iguais se possuírem
     * a mesma chave.
     *
     * @param obj objeto a comparar
     * @return true se forem iguais
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Node<?> other)) return false;
        return this.key.equals(other.key);
    }
}
