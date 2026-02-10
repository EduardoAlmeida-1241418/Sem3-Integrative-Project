package pt.ipp.isep.dei.controller.terminalOperator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller that handles the relocation of boxes between bays
 * within the same warehouse by the Terminal Operator.
 * <p>
 * Provides methods to select bays, retrieve boxes from bays,
 * verify relocation conditions, and execute box transfers.
 */
public class RelocateBox_Box_Controller {

    private BoxRepository boxRepository;
    private BayRepository bayRepository;

    private Bay actualBay;
    private Bay newBay;
    private Box selectedBox;

    /**
     * Initializes repositories for bay and box management.
     */
    public RelocateBox_Box_Controller() {
        boxRepository = Repositories.getInstance().getBoxRepository();
        bayRepository = Repositories.getInstance().getBayRepository();
    }

    /** @return the currently selected source bay. */
    public Bay getActualBay() {
        return actualBay;
    }

    /** @param actualBay the current bay from which the box will be relocated. */
    public void setActualBay(Bay actualBay) {
        this.actualBay = actualBay;
    }

    /** @return the target bay for relocation. */
    public Bay getNewBay() {
        return newBay;
    }

    /** @param newBay the destination bay for the relocation. */
    public void setNewBay(Bay newBay) {
        this.newBay = newBay;
    }

    /**
     * Sets the source and destination bays by their identifiers.
     *
     * @param actualBayId ID of the source bay.
     * @param newBayId    ID of the destination bay.
     */
    public void setSelectedBaysById(String actualBayId, String newBayId) {
        this.actualBay = bayRepository.findByKey(actualBayId);
        this.newBay = bayRepository.findByKey(newBayId);
    }

    /** @return the currently selected box. */
    public Box getSelectedBox() {
        return selectedBox;
    }

    /** @param selectedBox the box selected for relocation. */
    public void setSelectedBox(Box selectedBox) {
        this.selectedBox = selectedBox;
    }

    /**
     * Sets the selected box by its ID.
     *
     * @param boxId ID of the box to be selected.
     */
    public void setSelectedBoxById(String boxId) {
        this.selectedBox = boxRepository.findById(boxId);
    }

    /**
     * Retrieves all boxes currently stored in the selected source bay.
     *
     * @return a list of boxes in the current bay.
     */
    public List<Box> getBoxes() {
        if (actualBay == null) {
            return List.of();
        }
        List<Box> boxesInBay = new ArrayList<>();
        for (String boxId : actualBay.getBoxIds()) {
            Box box = boxRepository.findById(boxId);
            if (box != null) {
                boxesInBay.add(box);
            }
        }
        return boxesInBay;
    }

    /**
     * Retrieves all boxes in the current bay as an observable list.
     *
     * @return an observable list of boxes for UI binding.
     */
    public ObservableList<Box> getBoxesObservableList() {
        ObservableList<Box> observableList = FXCollections.observableArrayList();
        observableList.addAll(getBoxes());
        return observableList;
    }

    /**
     * Checks if the current bay is empty (contains no boxes).
     *
     * @return true if the bay is empty, false otherwise.
     */
    public boolean bayIsEmpty() {
        return getBoxes().isEmpty();
    }

    /**
     * Performs relocation of the selected box from its current bay to a new bay.
     * <p>
     * Validation steps:
     * <ul>
     *   <li>Ensures the box and bays exist.</li>
     *   <li>Prevents relocation to the same bay.</li>
     *   <li>Checks if both bays belong to the same warehouse.</li>
     *   <li>Ensures the destination bay has space available.</li>
     * </ul>
     *
     * @return a string message describing the relocation result.
     */
    public String relocateBox() {
        if (selectedBox == null) {
            return "Box not found";
        }

        if (actualBay == null) {
            return "Actual bay not found";
        }

        if (newBay == null) {
            return "New bay not found";
        }

        if (actualBay.getBayId().equals(newBay.getBayId())) {
            return "New bay is the same as the actual bay";
        }

        if (!newBay.getWarehouseID().equals(actualBay.getWarehouseID())) {
            return "New bay is in a different warehouse";
        }

        if (!newBay.addBox(selectedBox.getBoxId(), RoleType.TERMINAL_OPERATOR)) {
            return "New bay is full or cannot accommodate the box";
        }

        actualBay.removeBox(selectedBox.getBoxId(), RoleType.TERMINAL_OPERATOR);
        selectedBox.setBayId(newBay.getBayId());
        return "Box relocated successfully";
    }

    /**
     * Generates a success log message for a completed relocation.
     *
     * @return formatted success message.
     */
    public String getLogMessageRelocateBoxSuccess() {
        return "Box " + selectedBox.getBoxId() + " relocated from Bay " + actualBay.getBayId() + " to Bay " + newBay.getBayId();
    }

    /**
     * Generates a failure log message describing the reason for relocation failure.
     *
     * @param reason description of why the relocation failed.
     * @return formatted failure message.
     */
    public String getLogMessageRelocateBoxFailure(String reason) {
        return "Failed to relocate Box " + selectedBox.getBoxId() + " to Bay " + newBay.getBayId() + " (" + reason + ")";
    }
}
