package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.comparator.BayOrderComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents an aisle in a warehouse.
 * Each aisle belongs to a specific warehouse, contains a set of bays,
 * and has an automatically derived numerical identifier.
 */
public class Aisle {
    private String aisleID;
    private String warehouseID;
    private int number;

    private Set<String> baysId = new TreeSet<>(new BayOrderComparator());

    /**
     * Constructs an {@code Aisle} with the given IDs.
     * Automatically extracts the aisle number from the identifier.
     *
     * @param aisleID     the unique identifier of the aisle
     * @param warehouseID the identifier of the warehouse containing this aisle
     */
    public Aisle(String aisleID, String warehouseID) {
        this.aisleID = aisleID;
        this.warehouseID = warehouseID;
        this.number = extractNumberFromId(aisleID);
    }

    /**
     * @return the unique identifier of this aisle
     */
    public String getAisleID() {
        return aisleID;
    }

    /**
     * Sets the aisle ID and updates the numeric representation accordingly.
     *
     * @param aisleID the new aisle ID
     */
    public void setAisleID(String aisleID) {
        this.aisleID = aisleID;
        this.number = extractNumberFromId(aisleID);
    }

    /**
     * @return the identifier of the warehouse this aisle belongs to
     */
    public String getWarehouseID() {
        return warehouseID;
    }

    /**
     * @param warehouseID the warehouse ID to set
     */
    public void setWarehouseID(String warehouseID) {
        this.warehouseID = warehouseID;
    }

    /**
     * @return the numeric representation of this aisle (extracted from its ID)
     */
    public int getNumber() {
        return number;
    }

    /**
     * Extracts the aisle number from its identifier string.
     * Assumes the ID contains the letter 'A' followed by a number.
     *
     * @param aisleID the aisle ID string
     * @return the extracted aisle number, or 0 if not found
     */
    private int extractNumberFromId(String aisleID) {
        int index = aisleID.lastIndexOf('A');
        if (index != -1 && index < aisleID.length() - 1) {
            return Integer.parseInt(aisleID.substring(index + 1));
        }
        return 0;
    }

    /**
     * @return a sorted set of bay identifiers within this aisle
     */
    public Set<String> getBaysId() {
        return baysId;
    }

    /**
     * @return a list containing all bay identifiers in order
     */
    public List<String> getBaysIdAsList() {
        return new ArrayList<>(baysId);
    }

    /**
     * @param baysId the set of bay identifiers to set for this aisle
     */
    public void setBaysId(Set<String> baysId) {
        this.baysId = baysId;
    }

    /**
     * Adds a bay identifier to this aisle.
     *
     * @param bayId the bay identifier to add
     */
    public void addBay(String bayId) {
        baysId.add(bayId);
    }

    /**
     * @return a string representation of the aisle and its contained bays
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(aisleID + "\n");
        for (String bayId : baysId) {
            string.append(bayId).append(", ");
        }
        return string.toString();
    }
}
