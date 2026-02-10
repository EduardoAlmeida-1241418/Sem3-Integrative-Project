package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.comparator.AisleOrderComparator;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.data.repository.sprint1.AisleRepository;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a warehouse that contains a set of aisles and manages storage organization.
 * Provides methods to retrieve bays, locate free storage spaces, and manage aisles.
 */
public class Warehouse {

    /** Repository for accessing aisle data. */
    private AisleRepository aisleRepository;

    /** Unique identifier of the warehouse. */
    private String warehouseID;

    /** Ordered set of aisle identifiers in the warehouse. */
    private Set<String> aislesIds = new TreeSet<>(new AisleOrderComparator());

    /**
     * Constructs a Warehouse instance with the given identifier.
     *
     * @param warehouseID unique identifier of the warehouse
     */
    public Warehouse(String warehouseID) {
        this.warehouseID = warehouseID;
        aisleRepository = Repositories.getInstance().getAisleRepository();
    }

    /** @return the warehouse ID */
    public String getWarehouseID() {
        return warehouseID;
    }

    /** @param warehouseID sets the warehouse ID */
    public void setWarehouseID(String warehouseID) {
        this.warehouseID = warehouseID;
    }

    /** @return the set of aisle identifiers */
    public Set<String> getAislesIds() {
        return aislesIds;
    }

    /** @param aislesIds sets the set of aisle identifiers */
    public void setAislesIds(Set<String> aislesIds) {
        this.aislesIds = aislesIds;
    }

    /**
     * Checks if a specific aisle exists in the warehouse.
     *
     * @param aisleId ID of the aisle to check
     * @return true if the aisle exists, false otherwise
     */
    public boolean exitsAisle(String aisleId) {
        return aislesIds.contains(aisleId);
    }

    /**
     * Retrieves all bay identifiers from all aisles in the warehouse.
     *
     * @return list of bay IDs
     */
    public List<String> getBaysIds() {
        List<String> baysIds = new ArrayList<>();
        for (String aisleId : aislesIds) {
            Aisle aisle = aisleRepository.findByKey(aisleId);
            baysIds.addAll(aisle.getBaysId());
        }
        return baysIds;
    }

    /**
     * Finds a free bay suitable for storing a specific item.
     * The bay must be either empty or contain the same SKU as the box and not be full.
     *
     * @param boxId ID of the box to be stored
     * @return ID of a suitable bay, or null if none is available
     */
    public String getFreeBayWithItem(String boxId) {
        BayRepository bayRepository = Repositories.getInstance().getBayRepository();
        Box box = Repositories.getInstance().getBoxRepository().findById(boxId);
        for (String bayId : getBaysIds()) {
            Bay bay = bayRepository.findByKey(bayId);
            if ((bay.getSkuItem() == null || bay.getSkuItem().equals(box.getSkuItem())) && !bay.isFull()) {
                return bayId;
            }
        }
        return null;
    }

    /**
     * Adds a new aisle to the warehouse.
     *
     * @param aisleId ID of the aisle to add
     */
    public void addAisle(String aisleId) {
        aislesIds.add(aisleId);
    }

    /**
     * Returns a string representation of the warehouse,
     * including its ID and list of aisle IDs.
     *
     * @return formatted string of warehouse details
     */
    @Override
    public String toString() {
        String string = warehouseID + "\n";
        for (String aisleId : aislesIds) {
            string += aisleId + ", ";
        }
        return string;
    }
}
