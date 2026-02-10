package pt.ipp.isep.dei.controller.terminalOperator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

/**
 * Controller responsible for handling manual box relocation operations performed by the Terminal Operator.
 * <p>
 * This includes moving boxes between bays, verifying relocation validity, and retrieving bay or box information.
 */
public class ManualRelocationBoxController {

    private BayRepository bayRepository;
    private BoxRepository boxRepository;

    private String boxId;
    private String newBayId;

    private Bay selectedBay;

    /**
     * Initializes repositories for bay and box data.
     */
    public ManualRelocationBoxController() {
        bayRepository = Repositories.getInstance().getBayRepository();
        boxRepository = Repositories.getInstance().getBoxRepository();
    }

    /** @return the ID of the box to relocate. */
    public String getBoxId() {
        return boxId;
    }

    /** @param boxId the ID of the box to relocate. */
    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    /** @return the ID of the new bay where the box will be relocated. */
    public String getNewBayId() {
        return newBayId;
    }

    /** @param newBayId the ID of the new bay where the box will be relocated. */
    public void setNewBayId(String newBayId) {
        this.newBayId = newBayId;
    }

    /**
     * Retrieves all available bays in the system.
     *
     * @return an observable list containing all bays.
     */
    public ObservableList<Bay> getAllBays() {
        return FXCollections.observableArrayList(bayRepository.findAll());
    }

    /** @return the currently selected bay. */
    public Bay getSelectedBay() {
        return selectedBay;
    }

    /** @param selectedBay the bay selected for relocation operations. */
    public void setSelectedBay(Bay selectedBay) {
        this.selectedBay = selectedBay;
    }

    /**
     * Retrieves all boxes currently stored in the selected bay.
     *
     * @return an observable list of boxes in the selected bay.
     */
    public ObservableList<Box> getBoxesFromSelectedBay() {
        BoxRepository boxRepository = Repositories.getInstance().getBoxRepository();
        ObservableList<Box> boxesInBay = FXCollections.observableArrayList();
        for (String boxId : selectedBay.getBoxIds()) {
            Box box = boxRepository.findById(boxId);
            if (box != null) boxesInBay.add(box);
        }
        return boxesInBay;
    }

    /**
     * Relocates a box from its current bay to a new bay after verifying constraints.
     * <p>
     * Validations include:
     * <ul>
     *   <li>Box existence.</li>
     *   <li>Bay existence.</li>
     *   <li>Warehouse consistency (both bays must belong to the same warehouse).</li>
     *   <li>Space availability in the new bay.</li>
     * </ul>
     *
     * @return a message describing the result of the relocation attempt.
     */
    public String relocateBox() {
        Box box = boxRepository.findById(boxId);
        if (box == null) {
            return "Box not found";
        }

        Bay oldBay = bayRepository.findByKey(box.getBayId());
        if (oldBay == null) {
            return "Old bay not found";
        }

        Bay newBay = bayRepository.findByKey(newBayId);
        if (newBay == null) {
            return "New bay not found";
        }

        if (oldBay.getBayId().equals(newBay.getBayId())) {
            return "New bay is the same as the old bay";
        }

        if (!newBay.getWarehouseID().equals(oldBay.getWarehouseID())) {
            return "New bay is in a different warehouse";
        }

        if (!newBay.addBox(boxId, RoleType.TERMINAL_OPERATOR)) {
            return "New bay is full or cannot accommodate the box";
        }

        oldBay.removeBox(boxId, RoleType.TERMINAL_OPERATOR);
        box.setBayId(newBayId);
        return "Box relocated successfully";
    }

    /**
     * Builds a success log message for a box relocation.
     *
     * @return formatted log message describing a successful relocation.
     */
    public String getLogMessageRelocateBoxSuccess() {
        return "Box " + boxId + " relocated from Bay " + selectedBay.getBayId() + " to Bay " + newBayId;
    }

    /**
     * Builds a failure log message for a box relocation attempt.
     *
     * @param reason textual explanation of why relocation failed.
     * @return formatted log message describing the failed relocation.
     */
    public String getLogMessageRelocateBoxFailure(String reason) {
        return "Failed to relocate Box " + boxId + " to Bay " + newBayId + " (" + reason + ")";
    }
}
