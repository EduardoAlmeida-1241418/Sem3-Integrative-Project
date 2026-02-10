package pt.ipp.isep.dei.domain.transportationRelated;

import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.wagonRelated.Wagon;

import java.util.List;

/**
 * Represents a freight transportation unit.
 * A freight is composed of wagons and is transported between two facilities
 * with an associated date and current status.
 */
public class Freight {

    /** Unique identifier of the freight */
    private int id;
    /** List of wagons associated with the freight */
    private List<Wagon> wagons;
    /** Origin facility of the freight */
    private Facility originFacility;
    /** Destination facility of the freight */
    private Facility destinationFacility;
    /** Date associated with the freight */
    private Date date;
    /** Current status of the freight */
    private Freight_Status status;

    /**
     * Constructs a freight with full information.
     * The initial status is set to PENDING.
     *
     * @param id freight identifier
     * @param wagons list of wagons
     * @param originStation origin facility
     * @param destinationStation destination facility
     * @param data date associated with the freight
     */
    public Freight(int id, List<Wagon> wagons, Facility originStation, Facility destinationStation, Date data) {
        this.id = id;
        this.wagons = wagons;
        this.originFacility = originStation;
        this.destinationFacility = destinationStation;
        this.date = data;
        this.status = Freight_Status.PENDING;
    }

    /**
     * Constructs a freight with only an identifier.
     *
     * @param id freight identifier
     */
    public Freight(int id) {
        this.id = id;
    }

    /**
     * Returns the freight identifier.
     *
     * @return freight id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the freight identifier.
     *
     * @param id new freight id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the list of wagons.
     *
     * @return wagons list
     */
    public List<Wagon> getWagons() {
        return wagons;
    }

    /**
     * Sets the list of wagons.
     *
     * @param wagons wagons list
     */
    public void setWagons(List<Wagon> wagons) {
        this.wagons = wagons;
    }

    /**
     * Returns the origin facility.
     *
     * @return origin facility
     */
    public Facility getOriginFacility() {
        return originFacility;
    }

    /**
     * Sets the origin facility.
     *
     * @param originFacility origin facility
     */
    public void setOriginFacility(Facility originFacility) {
        this.originFacility = originFacility;
    }

    /**
     * Returns the destination facility.
     *
     * @return destination facility
     */
    public Facility getDestinationFacility() {
        return destinationFacility;
    }

    /**
     * Sets the destination facility.
     *
     * @param destinationFacility destination facility
     */
    public void setDestinationFacility(Facility destinationFacility) {
        this.destinationFacility = destinationFacility;
    }

    /**
     * Returns the date associated with the freight.
     *
     * @return freight date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date associated with the freight.
     *
     * @param date freight date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Returns the current status of the freight.
     *
     * @return freight status
     */
    public Freight_Status getStatus() {
        return status;
    }

    /**
     * Sets the current status of the freight.
     *
     * @param status freight status
     */
    public void setStatus(Freight_Status status) {
        this.status = status;
    }
}
