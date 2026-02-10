package pt.ipp.isep.dei.controller.terminalOperator;

import pt.ipp.isep.dei.comparator.BayOrderComparator;
import pt.ipp.isep.dei.domain.Aisle;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.data.repository.sprint1.AisleRepository;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint1.WarehouseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Controller responsible for managing bay-related operations
 * for the Terminal Operator in the Relocate Box workflow.
 * <p>
 * It provides methods to retrieve warehouse and bay data,
 * determine occupancy-based bay colors, and verify bay compatibility
 * when relocating boxes.
 */
public class RelocateBox_Bay_Controller {

    private WarehouseRepository warehouseRepository;
    private AisleRepository aisleRepository;
    private BayRepository bayRepository;

    private String warehouseId;

    private Map<String, String> bayColors = new TreeMap<>(new BayOrderComparator());

    /**
     * Initializes the controller and retrieves repository instances.
     */
    public RelocateBox_Bay_Controller() {
        bayRepository = Repositories.getInstance().getBayRepository();
        warehouseRepository = Repositories.getInstance().getWarehouseRepository();
        aisleRepository = Repositories.getInstance().getAisleRepository();
    }

    /** @return the currently selected warehouse ID. */
    public String getWarehouseId() {
        return warehouseId;
    }

    /**
     * Sets the active warehouse ID and updates its corresponding bay colors.
     *
     * @param warehouseId the warehouse ID to set.
     */
    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
        setBayColors();
    }

    /**
     * Retrieves all warehouse IDs available in the system.
     *
     * @return a list of warehouse IDs.
     */
    public List<String> getWarehousesIds() {
        List<String> warehouseIds = new ArrayList<>();
        for (var warehouse : warehouseRepository.findAll()) {
            warehouseIds.add(warehouse.getWarehouseID());
        }
        return warehouseIds;
    }

    /**
     * Retrieves all aisles belonging to the currently selected warehouse.
     *
     * @return a list of aisles in the selected warehouse.
     */
    public List<Aisle> getAisles() {
        List<Aisle> aisles = new ArrayList<>();
        for (Aisle aisle : aisleRepository.findAll()) {
            if (aisle.getWarehouseID().equals(warehouseId)) {
                aisles.add(aisle);
            }
        }
        return aisles;
    }

    /**
     * Updates the internal map of bay colors for the current warehouse,
     * based on occupancy levels.
     */
    public void setBayColors() {
        bayColors.clear();
        for (Bay bay : bayRepository.findAll()) {
            if (bay.getWarehouseID().equals(warehouseId)) {
                bayColors.put(bay.getBayId(), colorByBayOccupancy(bay.getBayId()));
            }
        }
    }

    /**
     * Retrieves the color code representing a bay's occupancy.
     *
     * @param bayId the ID of the bay.
     * @return the color code in hexadecimal format.
     */
    public String getBayColor(String bayId) {
        if (!bayColors.containsKey(bayId)) {
            return "0";
        }
        return bayColors.get(bayId);
    }

    /**
     * Determines the color representing the occupancy level of a bay.
     * <ul>
     *   <li>0–20% → Green (#2ECC71)</li>
     *   <li>21–40% → Lime (#A3E635)</li>
     *   <li>41–60% → Yellow (#FACC15)</li>
     *   <li>61–80% → Orange (#F97316)</li>
     *   <li>81–99% → Red (#EF4444)</li>
     *   <li>100% or more → Dark Red (#8B0000)</li>
     * </ul>
     *
     * @param bayId the ID of the bay.
     * @return the corresponding color code.
     */
    public String colorByBayOccupancy(String bayId) {
        Bay bay = bayRepository.findByKey(bayId);
        if (bay != null) {
            double occupancy = (double) (bay.getNBoxesStorage() * 100) / bay.getMaxCapacityBoxes();
            if (occupancy >= 0 && occupancy <= 20) {
                return "#2ECC71"; // Green
            } else if (occupancy > 20 && occupancy <= 40) {
                return "#A3E635"; // Lime
            } else if (occupancy > 40 && occupancy <= 60) {
                return "#FACC15"; // Yellow
            } else if (occupancy > 60 && occupancy <= 80) {
                return "#F97316"; // Orange
            } else if (occupancy > 80 && occupancy < 100) {
                return "#EF4444"; // Red
            } else return "#8B0000"; // Dark Red
        }
        return "#E0E0E0"; // Default gray if bay not found
    }

    /**
     * Retrieves the SKU associated with a specific bay.
     *
     * @param bayId the bay ID.
     * @return the SKU stored in that bay, or an empty string if unavailable.
     */
    public String getSkuByBayId(String bayId) {
        Bay bay = bayRepository.findByKey(bayId);
        if (bay != null) {
            return bay.getSkuItem();
        }
        return "";
    }

    /**
     * Retrieves a bay instance by its ID.
     *
     * @param bayId the bay ID.
     * @return the corresponding Bay object, or null if not found.
     */
    public Bay getBayById(String bayId) {
        return bayRepository.findByKey(bayId);
    }

    /**
     * Checks whether two bays contain different SKUs.
     *
     * @param sourceBayId the source bay ID.
     * @param targetBayId the target bay ID.
     * @return true if SKUs differ; false otherwise.
     */
    public boolean baysWithDifferentSkus(String sourceBayId, String targetBayId) {
        Bay sourceBay = bayRepository.findByKey(sourceBayId);
        Bay targetBay = bayRepository.findByKey(targetBayId);

        if (sourceBay != null && targetBay != null) {
            if (sourceBay.getSkuItem() == null || targetBay.getSkuItem() == null) {
                return false;
            }
            return !sourceBay.getSkuItem().equals(targetBay.getSkuItem());
        }
        return false;
    }

    /**
     * Determines if a given bay has reached its maximum capacity.
     *
     * @param bayId the bay ID.
     * @return true if the bay is full; false otherwise.
     */
    public boolean isBayFull(String bayId) {
        Bay bay = bayRepository.findByKey(bayId);
        if (bay != null) {
            return bay.getNBoxesStorage() >= bay.getMaxCapacityBoxes();
        }
        return false;
    }

    /**
     * Determines if a given bay is empty (contains no boxes).
     *
     * @param bayId the bay ID.
     * @return true if the bay is empty; false otherwise.
     */
    public boolean isBayEmpty(String bayId) {
        Bay bay = bayRepository.findByKey(bayId);
        if (bay != null) {
            return bay.getNBoxesStorage() == 0;
        }
        return false;
    }
}
