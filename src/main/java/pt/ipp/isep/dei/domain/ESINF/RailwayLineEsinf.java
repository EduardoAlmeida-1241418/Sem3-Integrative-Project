package pt.ipp.isep.dei.domain.ESINF;

/**
 * Representa uma linha ferroviária no contexto ESINF.
 *
 * Uma linha ferroviária liga duas estações, possuindo
 * informações associadas à distância, capacidade e custo.
 */
public class RailwayLineEsinf {

    /**
     * Estação de partida da linha ferroviária.
     */
    private StationEsinf departureStation;

    /**
     * Estação de chegada da linha ferroviária.
     */
    private StationEsinf arrivalStation;

    /**
     * Distância da linha ferroviária em quilómetros.
     */
    private double distanceKm;

    /**
     * Capacidade máxima da linha ferroviária.
     */
    private int capacity;

    /**
     * Custo associado à utilização da linha ferroviária.
     */
    private double cost;

    /**
     * Construtor da linha ferroviária.
     *
     * @param departureStation estação de partida
     * @param arrivalStation estação de chegada
     * @param distanceKm distância em quilómetros
     * @param capacity capacidade da linha
     * @param cost custo da linha
     */
    public RailwayLineEsinf(StationEsinf departureStation, StationEsinf arrivalStation, double distanceKm, int capacity, double cost) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.distanceKm = distanceKm;
        this.capacity = capacity;
        this.cost = cost;
    }

    /**
     * Obtém a estação de partida.
     *
     * @return estação de partida
     */
    public StationEsinf getDepartureStation() {
        return departureStation;
    }

    /**
     * Define a estação de partida.
     *
     * @param departureStation estação de partida
     */
    public void setDepartureStation(StationEsinf departureStation) {
        this.departureStation = departureStation;
    }

    /**
     * Obtém a estação de chegada.
     *
     * @return estação de chegada
     */
    public StationEsinf getArrivalStation() {
        return arrivalStation;
    }

    /**
     * Define a estação de chegada.
     *
     * @param arrivalStation estação de chegada
     */
    public void setArrivalStation(StationEsinf arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    /**
     * Obtém a distância da linha em quilómetros.
     *
     * @return distância em km
     */
    public double getDistanceKm() {
        return distanceKm;
    }

    /**
     * Define a distância da linha em quilómetros.
     *
     * @param distanceKm distância em km
     */
    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    /**
     * Obtém a capacidade da linha ferroviária.
     *
     * @return capacidade
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Define a capacidade da linha ferroviária.
     *
     * @param capacity capacidade
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Obtém o custo da linha ferroviária.
     *
     * @return custo
     */
    public double getCost() {
        return cost;
    }

    /**
     * Define o custo da linha ferroviária.
     *
     * @param cost custo
     */
    public void setCost(double cost) {
        this.cost = cost;
    }
}
