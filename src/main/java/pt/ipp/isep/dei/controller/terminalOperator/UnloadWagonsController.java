package pt.ipp.isep.dei.controller.terminalOperator;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.data.repository.*;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.sprint1.WagonRepository;
import pt.ipp.isep.dei.data.repository.sprint1.WarehouseRepository;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.wagonRelated.Wagon;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.util.*;

/**
 * Controller responsible for managing the unloading of wagons
 * into warehouse bays by the Terminal Operator.
 * <p>
 * Provides functionality for listing wagons, selecting specific wagons
 * to unload, aggregating SKU details, and executing the unloading process
 * while logging all operations performed.
 */
public class UnloadWagonsController {

    private WagonRepository wagonESINFRepository;
    private WarehouseRepository warehouseRepository;
    private BoxRepository boxRepository;
    private BayRepository bayRepository;

    private String warehouseId;

    private List<WagonSelection> wagonListSelected;

    /**
     * Initializes the controller and loads required repositories.
     */
    public UnloadWagonsController() {
        wagonESINFRepository = Repositories.getInstance().getWagonRepository();
        warehouseRepository = Repositories.getInstance().getWarehouseRepository();
        boxRepository = Repositories.getInstance().getBoxRepository();
        bayRepository = Repositories.getInstance().getBayRepository();
    }

    /** @return the selected warehouse ID. */
    public String getWarehouseId() {
        return warehouseId;
    }

    /** @param warehouseId sets the current warehouse ID. */
    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    /**
     * Converts all wagons into a selectable structure for UI interaction.
     * Only wagons that contain boxes are included.
     */
    private void getWagonToWagonSelection() {
        wagonListSelected = new ArrayList<>();
        Collection<Wagon> allWagons = wagonESINFRepository.findAll();
        for (Wagon wagon : allWagons) {
            if (wagon.getBoxIds().isEmpty()) continue;
            wagonListSelected.add(new WagonSelection(wagon));
        }
    }

    /**
     * Retrieves all wagons with their selection metadata for the UI.
     *
     * @return an observable list of selectable wagons.
     */
    public ObservableList<WagonSelection> getAllWagons() {
        getWagonToWagonSelection();
        ObservableList<WagonSelection> wagons = FXCollections.observableArrayList();
        wagons.addAll(wagonListSelected);
        return wagons;
    }

    /**
     * Retrieves a mapping between SKU identifiers and the number of items per SKU
     * present in a given wagon.
     *
     * @param wagonId the ID of the wagon.
     * @return a map of SKU to item count.
     */
    public Map<String, Integer> getWagonNumSkus(int wagonId) {
        Wagon wagon = wagonESINFRepository.findById(wagonId);
        if (wagon == null) {
            UIUtils.addLog("Wagon with ID " + wagonId + " not found", LogType.ERROR, RoleType.TERMINAL_OPERATOR);
        }
        Map<String, Integer> skuCountMap = new HashMap<>();
        for (String boxId : wagon.getBoxIds()) {
            Box box = boxRepository.findById(boxId);
            if (box == null) continue;
            skuCountMap.put(box.getSkuItem(), box.getQuantity());
        }
        return skuCountMap;
    }

    /**
     * Retrieves all warehouse IDs for UI selection.
     *
     * @return an observable list of warehouse IDs.
     */
    public ObservableList<String> getWarehouseIds() {
        ObservableList<String> warehouseIds = FXCollections.observableArrayList();
        for (Warehouse warehouse : warehouseRepository.findAll()) {
            warehouseIds.add(warehouse.getWarehouseID());
        }
        return warehouseIds;
    }

    /**
     * Collects the IDs of all wagons currently selected by the user.
     *
     * @return a list of selected wagon IDs.
     */
    private List<String> getWagonsSelected() {
        List<String> selectedWagonIds = new ArrayList<>();
        for (WagonSelection wagonSelection : wagonListSelected) {
            if (wagonSelection.isSelected()) {
                selectedWagonIds.add(wagonSelection.getWagonId());
            }
        }
        return selectedWagonIds;
    }

    /** @return true if any wagons are selected for unloading. */
    public boolean hasSelectedWagons() {
        return !getWagonsSelected().isEmpty();
    }

    /**
     * Aggregates total box and quantity information per SKU across selected wagons.
     *
     * @return an observable list of aggregated SKU details.
     */
    public ObservableList<SkuTotalDetails> getSkuTotalDetails() {
        List<String> wagonIds = getWagonsSelected();
        Map<String, Integer> skuToTotalBoxes = new HashMap<>();
        Map<String, Integer> skuToTotalQuantity = new HashMap<>();

        for (String wagonId : wagonIds) {
            Wagon wagon = wagonESINFRepository.findById(Integer.parseInt(wagonId));
            if (wagon == null) continue;

            for (String boxId : wagon.getBoxIds()) {
                Box box = boxRepository.findById(boxId);
                if (box == null) continue;

                String sku = box.getSkuItem();
                if (!skuToTotalBoxes.containsKey(sku)) {
                    skuToTotalBoxes.put(sku, 1);
                    skuToTotalQuantity.put(sku, box.getQuantity());
                } else {
                    skuToTotalBoxes.put(sku, skuToTotalBoxes.get(sku) + 1);
                    skuToTotalQuantity.put(sku, skuToTotalQuantity.get(sku) + box.getQuantity());
                }
            }
        }

        ObservableList<SkuTotalDetails> skuTotalDetailsList = FXCollections.observableArrayList();
        for (String sku : skuToTotalBoxes.keySet()) {
            int totalBoxes = skuToTotalBoxes.get(sku);
            int totalQuantity = skuToTotalQuantity.get(sku);
            skuTotalDetailsList.add(new SkuTotalDetails(sku, totalBoxes, totalQuantity));
        }
        return skuTotalDetailsList;
    }

    /**
     * Executes unloading for all selected wagons.
     */
    public void unloadSelectedWagons() {
        List<String> wagonIds = getWagonsSelected();
        for (String wagonId : wagonIds) {
            unloadWagon(wagonId);
        }
    }

    /**
     * Unloads a single wagon into the specified warehouse.
     * <p>
     * Each box is relocated to a compatible free bay when possible.
     * Operations are logged through {@link UIUtils#addLog(String, LogType, RoleType)}.
     *
     * @param wagonId the ID of the wagon to unload.
     */
    public void unloadWagon(String wagonId) {
        Wagon wagon = wagonESINFRepository.findById(Integer.parseInt(wagonId));
        if (wagon == null) {
            UIUtils.addLog("Wagon with ID " + wagonId + " not found", LogType.ERROR, RoleType.TERMINAL_OPERATOR);
            return;
        }

        Warehouse warehouse = warehouseRepository.findByKey(warehouseId);
        if (warehouse == null) {
            UIUtils.addLog("Warehouse with ID " + warehouseId + " not found", LogType.ERROR, RoleType.TERMINAL_OPERATOR);
            return;
        }

        List<String> boxIds = List.copyOf(wagon.getBoxIds());
        if (boxIds.isEmpty()) {
            UIUtils.addLog("Wagon " + wagonId + " is already empty", LogType.INFO, RoleType.TERMINAL_OPERATOR);
            return;
        }

        for (String boxId : boxIds) {
            String bayId = warehouse.getFreeBayWithItem(boxId);
            if (bayId != null) {
                Bay bay = bayRepository.findByKey(bayId);
                bay.addBox(boxId, RoleType.TERMINAL_OPERATOR);
                wagon.removeBox(boxId);
                UIUtils.addLog("Box " + boxId + " unloaded from wagon " + wagonId + " to bay " + bayId, LogType.INFO, RoleType.TERMINAL_OPERATOR);
            }
        }
    }

    /**
     * Wrapper class representing a selectable wagon entry.
     * Used for UI data binding.
     */
    public static class WagonSelection {
        private final SimpleStringProperty wagonId;
        private final SimpleIntegerProperty numBoxes;
        private final SimpleBooleanProperty selected;

        public WagonSelection(Wagon wagon) {
            this.wagonId = new SimpleStringProperty(wagon.getWagonID() + "");
            this.numBoxes = new SimpleIntegerProperty(wagon.getBoxIds().size());
            this.selected = new SimpleBooleanProperty(false);
        }

        public String getWagonId() { return wagonId.get(); }
        public int getNumBoxes() { return numBoxes.get(); }

        public boolean isSelected() { return selected.get(); }
        public void setSelected(boolean value) { selected.set(value); }
        public SimpleBooleanProperty selectedProperty() { return selected; }
    }

    /**
     * Wrapper class representing aggregated SKU details
     * for display in the unloading summary table.
     */
    public static class SkuTotalDetails {
        private final SimpleStringProperty sku;
        private final SimpleIntegerProperty totalBoxes;
        private final SimpleIntegerProperty totalQuantity;

        public SkuTotalDetails(String sku, int totalBoxes, int totalQuantity) {
            this.sku = new SimpleStringProperty(sku);
            this.totalBoxes = new SimpleIntegerProperty(totalBoxes);
            this.totalQuantity = new SimpleIntegerProperty(totalQuantity);
        }

        public String getSku() { return sku.get(); }
        public SimpleStringProperty skuProperty() { return sku; }

        public int getTotalBoxes() { return totalBoxes.get(); }
        public SimpleIntegerProperty totalBoxesProperty() { return totalBoxes; }

        public int getTotalQuantity() { return totalQuantity.get(); }
        public SimpleIntegerProperty totalQuantityProperty() { return totalQuantity; }
    }
}
