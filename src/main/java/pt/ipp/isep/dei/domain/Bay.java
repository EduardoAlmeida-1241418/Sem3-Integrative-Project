package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.comparator.FefoBoxComparator;
import pt.ipp.isep.dei.comparator.FifoBoxComparator;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a bay within an aisle of a warehouse.
 * Each bay stores boxes of a single SKU and enforces capacity and consistency rules.
 */
public class Bay {
    private String warehouseID;
    private String aisleId;
    private String bayId;
    private String skuItem;
    private int bay;
    private int maxCapacityBoxes;
    private int nBoxesStorage;

    private Comparator<String> fefoComparator = new FefoBoxComparator();
    private Comparator<String> fifoComparator = new FifoBoxComparator();
    private Comparator<String> boxComparator = fefoComparator.thenComparing(fifoComparator);
    private Set<String> boxIds = new TreeSet<>(boxComparator);

    /**
     * Constructs a {@code Bay} with the given aisle ID, bay number, and capacity.
     * Automatically generates the bay ID and associates it with the corresponding warehouse.
     *
     * @param aisleId          the identifier of the aisle this bay belongs to
     * @param bay              the numerical identifier of this bay within the aisle
     * @param maxCapacityBoxes the maximum number of boxes the bay can hold
     */
    public Bay(String aisleId, int bay, int maxCapacityBoxes) {
        this.warehouseID = aisleId.split("A")[0];
        this.bayId = aisleId + "B" + bay;
        this.aisleId = aisleId;
        this.bay = bay;
        this.maxCapacityBoxes = maxCapacityBoxes;
    }

    public String getAisleId() { return aisleId; }
    public void setAisleId(String aisleId) { this.aisleId = aisleId; }
    public String getWarehouseID() { return warehouseID; }
    public void setWarehouseID(String warehouseID) { this.warehouseID = warehouseID; }
    public int getBay() { return bay; }
    public void setBay(int bay) { this.bay = bay; }
    public int getMaxCapacityBoxes() { return maxCapacityBoxes; }
    public void setMaxCapacityBoxes(int maxCapacityBoxes) { this.maxCapacityBoxes = maxCapacityBoxes; }
    public int getNBoxesStorage() { return nBoxesStorage; }
    public void setNBoxesStorage(int nBoxesStorage) { this.nBoxesStorage = nBoxesStorage; }
    public String getSkuItem() { return skuItem; }

    /**
     * @return the SKU identifier if set, otherwise "Empty"
     */
    public String getSkuItemSafe() {
        return skuItem == null ? "Empty" : skuItem;
    }

    public void setSkuItem(String skuItem) { this.skuItem = skuItem; }
    public String getBayId() { return bayId; }
    public void setBayId(String bayId) { this.bayId = bayId; }
    public Set<String> getBoxIds() { return boxIds; }

    /**
     * Adds a box to the bay if capacity allows and SKU consistency is maintained.
     *
     * @param boxId    the identifier of the box to add
     * @param roleType the role of the user performing the operation
     * @return true if the box was successfully added, false otherwise
     */
    public boolean addBox(String boxId, RoleType roleType) {
        BoxRepository boxRepository = Repositories.getInstance().getBoxRepository();
        Box box = boxRepository.findById(boxId);

        if (box == null) {
            UIUtils.addLog("Box not found", LogType.ERROR, roleType);
            return false;
        }

        if (skuItem != null && !Objects.equals(skuItem, box.getSkuItem())) {
            UIUtils.addLog("Box SKU does not match Bay SKU", LogType.ERROR, roleType);
            return false;
        }

        if (nBoxesStorage < maxCapacityBoxes) {
            boxIds.add(boxId);
            nBoxesStorage++;
            skuItem = box.getSkuItem();
            box.setBayId(bayId);
        } else {
            UIUtils.addLog("Bay is full", LogType.ERROR, roleType);
            return false;
        }

        reloadInformation(roleType);
        return true;
    }


    /**
     * Removes a box from the bay if it exists.
     *
     * @param boxId    the identifier of the box to remove
     * @param roleType the role of the user performing the operation
     * @return true if the box was removed successfully, false otherwise
     */
    public boolean removeBox(String boxId, RoleType roleType) {
        BoxRepository boxRepository = Repositories.getInstance().getBoxRepository();
        Box box = boxRepository.findById(boxId);

        if (box == null) {
            UIUtils.addLog("Box not found", LogType.ERROR, roleType);
            return false;
        }

        if (boxIds.remove(boxId)) {
            nBoxesStorage--;
            box.setBayId(null);
            reloadInformation(roleType);
            return true;
        } else {
            UIUtils.addLog("Box not found in this Bay", LogType.ERROR, roleType);
            return false;
        }
    }

    /**
     * Calculates the total quantity of items stored in all boxes within the bay.
     *
     * @return the total quantity of items
     */
    public int getQuantityItems() {
        int totalQuantity = 0;
        for (String boxId : boxIds) {
            BoxRepository boxRepository = Repositories.getInstance().getBoxRepository();
            Box box = boxRepository.findById(boxId);
            if (box != null) {
                totalQuantity += box.getQuantity();
            }
        }
        return totalQuantity;
    }

    /**
     * @return true if the bay is full (reached maximum capacity), false otherwise
     */
    public boolean isFull() {
        return nBoxesStorage == maxCapacityBoxes;
    }

    /**
     * Reloads bay information and ensures consistency between bay and box records.
     * Removes non-existent or empty boxes and updates SKU and storage counters.
     *
     * @param roleType the role of the user performing the operation
     */
    private void reloadInformation(RoleType roleType) {
        for (String boxId : boxIds) {
            BoxRepository boxRepository = Repositories.getInstance().getBoxRepository();
            Box box = boxRepository.findById(boxId);
            if (box == null) {
                boxIds.remove(boxId);
                UIUtils.addLog("Box " + boxId + " removed from Bay " + bayId + " because it does not exist.", LogType.WARNING, roleType);
            } else if (box.boxIsEmpty()) {
                boxIds.remove(boxId);
                UIUtils.addLog("Box " + boxId + " removed from Bay " + bayId + " because it is empty.", LogType.INFO, roleType);
            }
        }
        if (boxIds.isEmpty()) {
            skuItem = null;
            nBoxesStorage = 0;
            UIUtils.addLog("Bay " + bayId + " is now empty.", LogType.INFO, roleType);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bay bay1 = (Bay) o;
        return bay == bay1.bay && maxCapacityBoxes == bay1.maxCapacityBoxes &&
                nBoxesStorage == bay1.nBoxesStorage && Objects.equals(warehouseID, bay1.warehouseID) &&
                Objects.equals(aisleId, bay1.aisleId) && Objects.equals(bayId, bay1.bayId) &&
                Objects.equals(skuItem, bay1.skuItem) && Objects.equals(boxIds, bay1.boxIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(warehouseID, aisleId, bayId, skuItem, bay, maxCapacityBoxes, nBoxesStorage, boxIds);
    }

    /**
     * @return a string representation of the bay, including IDs and box list
     */
    @Override
    public String toString() {
        String string = aisleId + " - " + bayId + " - " + skuItem + " - " + bay +
                " - " + maxCapacityBoxes + " - " + nBoxesStorage + "\n";
        for (String boxId : boxIds) {
            string += boxId + ", ";
        }
        return string;
    }
}
