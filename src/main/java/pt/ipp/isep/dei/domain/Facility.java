package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.domain.trackRelated.TrackLocation;

import java.util.Objects;

/**
 * Represents a facility.
 * A facility is a track location that can be intermodal and
 * has an associated type.
 */
public class Facility implements TrackLocation {

    /** Unique identifier of the facility */
    private int id;
    /** Name of the facility */
    private String name;
    /** Indicates whether the facility is intermodal */
    private boolean intermodal;
    /** Type of the facility */
    private FacilityType facilityType;

    /**
     * Constructs a facility with full information.
     *
     * @param id facility identifier
     * @param name facility name
     * @param intermodal indicates if the facility is intermodal
     * @param facilityType facility type
     */
    public Facility(int id, String name, boolean intermodal, FacilityType facilityType) {
        this.id = id;
        this.name = name;
        this.intermodal = intermodal;
        this.facilityType = facilityType;
    }

    /**
     * Constructs a facility with only an identifier.
     *
     * @param id facility identifier
     */
    public Facility(int id) {
        this.id = id;
    }

    /**
     * Constructs a facility with an identifier and name.
     *
     * @param id facility identifier
     * @param name facility name
     */
    public Facility(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the facility identifier.
     *
     * @return facility id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the facility identifier.
     *
     * @param id facility id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the facility name.
     *
     * @return facility name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the facility name.
     *
     * @param name facility name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Indicates whether the facility is intermodal.
     *
     * @return true if intermodal
     */
    public boolean isIntermodal() {
        return intermodal;
    }

    /**
     * Sets the intermodal status of the facility.
     *
     * @param intermodal intermodal status
     */
    public void setIntermodal(boolean intermodal) {
        this.intermodal = intermodal;
    }

    /**
     * Returns the facility type.
     *
     * @return facility type
     */
    public FacilityType getFacilityType() {
        return facilityType;
    }

    /**
     * Sets the facility type.
     *
     * @param facilityType facility type
     */
    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }

    /**
     * Returns the name of the track location.
     *
     * @return facility name
     */
    @Override
    public String name() {
        return this.name;
    }

    /**
     * Checks equality based on the facility identifier.
     *
     * @param o object to compare
     * @return true if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Facility)) return false;
        Facility f = (Facility) o;
        return id == f.id;
    }

    /**
     * Computes the hash code based on the facility identifier.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns the string representation of the facility.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return name + " (#" + id + ")";
    }
}
