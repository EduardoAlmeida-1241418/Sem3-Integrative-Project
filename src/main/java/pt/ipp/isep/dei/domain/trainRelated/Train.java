package pt.ipp.isep.dei.domain.trainRelated;

import pt.ipp.isep.dei.data.memory.TrainStoreInMemory;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.transportationRelated.Route;
import pt.ipp.isep.dei.domain.wagonRelated.Wagon;

import java.util.List;

/**
 * Represents a train.
 * A train is composed of locomotives, operates on a route,
 * has an associated operator and scheduled date and time.
 */
public class Train implements Comparable<Train> {

    /** In-memory store for train identifiers */
    private TrainStoreInMemory trainStoreInMemory = new TrainStoreInMemory();

    /** Unique identifier of the train */
    private int id;
    /** Operator responsible for the train */
    private Operator operator;
    /** Date and time associated with the train */
    private DateTime dateTime;
    /** Route followed by the train */
    private Route route;
    /** List of locomotives composing the train */
    private List<Locomotive> locomotives;
    /** Indicates if the train has been dispatched */
    private boolean dispatched;

    /**
     * Constructs a train with a specific identifier.
     * Validates if the identifier already exists.
     *
     * @param id train identifier
     * @param operator train operator
     * @param dateTime scheduled date and time
     * @param route train route
     * @param locomotives list of locomotives
     * @param dispatched dispatch status
     * @throws IllegalArgumentException if the id already exists
     */
    public Train(int id, Operator operator, DateTime dateTime, Route route, List<Locomotive> locomotives, boolean dispatched) {
        this.id = id;

        if (trainStoreInMemory.exists(id)) {
            throw new IllegalArgumentException("Train with ID " + id + " already exists.");
        }

        this.operator = operator;
        this.dateTime = dateTime;
        this.route = route;
        this.locomotives = locomotives;
        this.dispatched = dispatched;
    }

    /**
     * Constructs a train with an automatically generated identifier.
     *
     * @param operator train operator
     * @param dateTime scheduled date and time
     * @param route train route
     * @param locomotives list of locomotives
     */
    public Train(Operator operator, DateTime dateTime, Route route, List<Locomotive> locomotives) {
        this.id = trainStoreInMemory.getNextId();
        this.operator = operator;
        this.dateTime = dateTime;
        this.route = route;
        this.locomotives = locomotives;
    }

    /**
     * Copy constructor.
     *
     * @param train train to copy
     */
    public Train(Train train) {
        this.id = train.getId();
        this.operator = train.getOperator();
        this.dateTime = train.getDateTime();
        this.route = train.getRoute();
        this.locomotives = train.getLocomotives();
    }

    /**
     * Compares trains by their identifiers.
     *
     * @param other other train to compare
     * @return comparison result
     */
    @Override
    public int compareTo(Train other) {
        return Integer.compare(this.getId(), other.getId());
    }

    /**
     * Returns the train identifier.
     *
     * @return train id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the train operator.
     *
     * @return operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Sets the train operator.
     *
     * @param operator train operator
     */
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    /**
     * Returns the date and time of the train.
     *
     * @return date and time
     */
    public DateTime getDateTime() {
        return dateTime;
    }

    /**
     * Sets the date and time of the train.
     *
     * @param dateTime date and time
     */
    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Returns the train route.
     *
     * @return route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * Sets the train route.
     *
     * @param route train route
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     * Returns the list of locomotives.
     *
     * @return locomotives list
     */
    public List<Locomotive> getLocomotives() {
        return locomotives;
    }

    /**
     * Sets the list of locomotives.
     *
     * @param locomotives locomotives list
     */
    public void setLocomotives(List<Locomotive> locomotives) {
        this.locomotives = locomotives;
    }

    /**
     * Indicates if the train has been dispatched.
     *
     * @return true if dispatched
     */
    public boolean isDispatched() {
        return dispatched;
    }

    /**
     * Sets the dispatch status of the train.
     *
     * @param dispatched dispatch status
     */
    public void setDispatched(boolean dispatched) {
        this.dispatched = dispatched;
    }

    /**
     * Sets the train identifier.
     *
     * @param id train id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Calculates the total weight of the train.
     *
     * @return total weight
     */
    public double getTotalWeigh(){
        double totalWeight = 0;

        for(Locomotive locomotive : locomotives){
            totalWeight += locomotive.getLocomotiveModel().getMaximumWeight();
        }

        for(Freight freight : route.getFreights()){
            for (Wagon wagon : freight.getWagons()){
                totalWeight += wagon.getWagonModel().getDimensions().getWeightTare();
                //totalWeight += wagon.getCargoWeight();
            }
        }

        return totalWeight;
    }

    /**
     * Calculates the total power of the train.
     *
     * @return total power
     */
    public double getTotalPower() {
        double power = 0;

        for (Locomotive locomotive : locomotives) {
            power += locomotive.getLocomotiveModel().getPower();
        }

        return power;
    }

    /**
     * Returns the maximum allowed speed based on the locomotives.
     *
     * @return maximum allowed speed
     */
    public double getMaxAllowedSpeed() {
        double maxAllowedSpeed = 0;

        for (Locomotive locomotive : locomotives) {
            maxAllowedSpeed = Math.max(maxAllowedSpeed, locomotive.getLocomotiveModel().getMaxSpeed());
        }

        return maxAllowedSpeed;
    }

    /**
     * Returns the string representation of the train.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Train: " + id;
    }

}
