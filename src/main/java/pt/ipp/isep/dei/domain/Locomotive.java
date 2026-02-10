package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.domain.trainRelated.Train;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a locomotive.
 * A locomotive is associated with a locomotive model, an operator,
 * and may be assigned to multiple trains throughout its service life.
 */
public class Locomotive {

    /** Unique identifier of the locomotive */
    private int id;
    /** Name of the locomotive */
    private String name;
    /** Date when the locomotive started service */
    private Date startingDate;
    /** Year when the locomotive started service */
    private int startYearService;
    /** Model of the locomotive */
    private LocomotiveModel locomotiveModel;
    /** Operator responsible for the locomotive */
    private Operator operator;
    /** Facility where the locomotive started service */
    private Facility startFacility;

    /** List of trains associated with the locomotive */
    private List<Train> trains;

    /**
     * Constructs a locomotive with a service start year.
     *
     * @param id locomotive identifier
     * @param name locomotive name
     * @param startYearService year the locomotive entered service
     * @param locomotiveModel locomotive model
     * @param operator operator responsible for the locomotive
     */
    public Locomotive(int id, String name, int startYearService, LocomotiveModel locomotiveModel, Operator operator) {
        this.id = id;
        this.name = name;
        this.locomotiveModel = locomotiveModel;
        this.operator = operator;
        this.startYearService = startYearService;
    }

    /**
     * Constructs a locomotive with a specific starting date.
     *
     * @param id locomotive identifier
     * @param name locomotive name
     * @param startingDate starting date of service
     * @param locomotiveModel locomotive model
     * @param operator operator responsible for the locomotive
     */
    public Locomotive(int id, String name, Date startingDate, LocomotiveModel locomotiveModel, Operator operator) {
        this.id = id;
        this.name = name;
        this.startingDate = startingDate;
        this.locomotiveModel = locomotiveModel;
        this.operator = operator;
        this.trains = new ArrayList<>();
    }

    /**
     * Constructs a locomotive with a starting facility.
     *
     * @param id locomotive identifier
     * @param name locomotive name
     * @param startingDate starting date of service
     * @param locomotiveModel locomotive model
     * @param operator operator responsible for the locomotive
     * @param startFacility facility where service started
     */
    public Locomotive(int id, String name, Date startingDate, LocomotiveModel locomotiveModel, Operator operator, Facility startFacility) {
        this.id = id;
        this.name = name;
        this.startingDate = startingDate;
        this.locomotiveModel = locomotiveModel;
        this.operator = operator;
        this.startFacility = startFacility;
    }

    /**
     * Returns the year the locomotive started service.
     *
     * @return start year of service
     */
    public int getStartYearService() {
        return startYearService;
    }

    /**
     * Sets the year the locomotive started service.
     *
     * @param startYearService start year of service
     */
    public void setStartYearService(int startYearService) {
        this.startYearService = startYearService;
    }

    /**
     * Associates a train with this locomotive.
     *
     * @param train train to add
     */
    public void addTrain(Train train) {
        this.trains.add(train);
    }

    /**
     * Returns the list of trains associated with this locomotive.
     *
     * @return list of trains
     */
    public List<Train> getTrains() {
        return new ArrayList<>(trains);
    }

    /**
     * Sets the list of trains associated with this locomotive.
     *
     * @param trains list of trains
     */
    public void setTrains(List<Train> trains) {
        this.trains = new ArrayList<>(trains);
    }

    /**
     * Returns the locomotive identifier.
     *
     * @return locomotive id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the locomotive identifier.
     *
     * @param id locomotive id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the locomotive name.
     *
     * @return locomotive name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the locomotive name.
     *
     * @param name locomotive name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the starting date of service.
     *
     * @return starting date
     */
    public Date getStartingDate() {
        return startingDate;
    }

    /**
     * Sets the starting date of service.
     *
     * @param startingDate starting date
     */
    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    /**
     * Returns the locomotive model.
     *
     * @return locomotive model
     */
    public LocomotiveModel getLocomotiveModel() {
        return locomotiveModel;
    }

    /**
     * Sets the locomotive model.
     *
     * @param locomotiveModel locomotive model
     */
    public void setLocomotiveModel(LocomotiveModel locomotiveModel) {
        this.locomotiveModel = locomotiveModel;
    }

    /**
     * Returns the operator responsible for the locomotive.
     *
     * @return operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Sets the operator responsible for the locomotive.
     *
     * @param operator operator
     */
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    /**
     * Returns the facility where the locomotive started service.
     *
     * @return start facility
     */
    public Facility getStartFacility() {
        return startFacility;
    }

    /**
     * Sets the facility where the locomotive started service.
     *
     * @param startFacility start facility
     */
    public void setStartFacility(Facility startFacility) {
        this.startFacility = startFacility;
    }
}
