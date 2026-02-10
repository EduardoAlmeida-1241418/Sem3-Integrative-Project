package pt.ipp.isep.dei.domain.wagonRelated;

import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Facility;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a wagon that carries boxes within the railway system.
 * Each wagon is uniquely identified and contains a collection of box IDs.
 */
public class Wagon {

    /** Unique identifier for the wagon. */
    private int wagonID;
    private WagonModel wagonModel;
    private String operator;
    private Date serviceDate;
    private Facility startFacility;

    /** Set of box IDs currently assigned to this wagon. */
    private Set<String> boxIds = new HashSet<>();

    public Wagon(int wagonID, WagonModel wagonModel, String operator, Date serviceDate) {
        this.wagonID = wagonID;
        this.wagonModel = wagonModel;
        this.operator = operator;
        this.serviceDate = serviceDate;
    }

    public Wagon(int wagonID, WagonModel wagonModel, String operator, Date serviceDate, Facility startFacility) {
        this.wagonID = wagonID;
        this.wagonModel = wagonModel;
        this.operator = operator;
        this.serviceDate = serviceDate;
        this.startFacility = startFacility;
    }

    /**
     * Constructs a Wagon instance.
     *
     * @param wagonID unique identifier of the wagon
     */
    public Wagon(int wagonID) {
        this.wagonID = wagonID;
    }

    public WagonModel getWagonModel() {
        return wagonModel;
    }

    public void setWagonModel(WagonModel wagonModel) {
        this.wagonModel = wagonModel;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    /** @return the wagon's unique ID */
    public int getWagonID() {
        return wagonID;
    }

    /** @param wagonID sets the wagon's unique ID */
    public void setWagonID(int wagonID) {
        this.wagonID = wagonID;
    }

    /** @return the set of box IDs contained in the wagon */
    public Set<String> getBoxIds() {
        return boxIds;
    }

    /** @param boxIds sets the set of box IDs contained in the wagon */
    public void setBoxIds(Set<String> boxIds) {
        this.boxIds = boxIds;
    }

    /**
     * Adds a box to the wagon.
     *
     * @param boxId ID of the box to add
     */
    public void addBox(String boxId) {
        boxIds.add(boxId);
    }

    /**
     * Removes a box from the wagon.
     *
     * @param boxId ID of the box to remove
     */
    public void removeBox(String boxId) {
        boxIds.remove(boxId);
    }

    public Facility getStartFacility() {
        return startFacility;
    }

    public void setStartFacility(Facility startFacility) {
        this.startFacility = startFacility;
    }

    /**
     * Compares this wagon to another object for equality.
     * Two wagons are considered equal if they have the same ID and set of box IDs.
     *
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wagon wagon = (Wagon) o;
        return Objects.equals(wagonID, wagon.wagonID) && Objects.equals(boxIds, wagon.boxIds);
    }

    /** @return the hash code of the wagon based on its ID and box set */
    @Override
    public int hashCode() {
        return Objects.hash(wagonID, boxIds);
    }

    /**
     * Returns a string representation of the wagon, including its ID and contained box IDs.
     *
     * @return formatted string of the wagon and its boxes
     */
    @Override
    public String toString() {
        String string = wagonID + "\n";
        for (String boxId : boxIds) {
            string += boxId + ", ";
        }
        return string;
    }
}
