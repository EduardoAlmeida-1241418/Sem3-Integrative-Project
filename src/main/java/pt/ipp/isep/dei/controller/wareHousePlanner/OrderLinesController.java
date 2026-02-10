package pt.ipp.isep.dei.controller.wareHousePlanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.OrderRelated.Order;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.domain.OrderRelated.ViewMode;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.sprint1.OrderLineRepository;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing and allocating order lines in the Warehouse Planner module.
 * <p>
 * Handles the retrieval of order lines, association with available boxes, and
 * allocation logic between boxes and order lines.
 */
public class OrderLinesController {

    private Order order;
    private OrderLineRepository lineRepository;
    private BoxRepository boxRepository;

    /**
     * Default constructor that initializes repository instances.
     */
    public OrderLinesController() {
        loadInfo();
    }

    /**
     * Initializes repository references.
     */
    public void loadInfo() {
        lineRepository = Repositories.getInstance().getOrderLineRepository();
        boxRepository = Repositories.getInstance().getBoxRepository();
    }

    /**
     * Loads line information for the current order by populating
     * each line with possible box allocations.
     */
    public void loadLineInfo() {
        for (String lineID : order.getOrderLineIds()) {
            OrderLine line = lineRepository.findByOrderId(lineID);
            if (line != null) {
                findPossibleAllocations(line);
            }
        }
    }

    /**
     * Determines possible boxes that can fulfill an order line based on SKU match
     * and box availability.
     *
     * @param line the order line to analyze for possible allocations.
     */
    private void findPossibleAllocations(OrderLine line) {
        line.getPossibleBox().clear();
        for (Box box : boxRepository.findBySkuItem(line.getSkuItem())) {
            if (box.getBayId() != null) {
                line.getPossibleBox().put(box.getBoxId(), box);
            }
        }
        line.updateStates();
    }

    /**
     * Gets the current order being processed.
     *
     * @return the current {@link Order}.
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Sets the order to be processed.
     *
     * @param order the {@link Order} to associate with this controller.
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * Retrieves all {@link OrderLine} objects associated with the current order.
     *
     * @return a list of order lines for the current order.
     */
    public List<OrderLine> getOrderLines() {
        List<OrderLine> lines = new ArrayList<>();
        for (String lineId : order.getOrderLineIds()) {
            OrderLine line = lineRepository.findByOrderId(lineId);
            if (line != null) lines.add(line);
        }
        return lines;
    }

    /**
     * Returns the total quantity requested for a specific order line.
     *
     * @param line the order line to inspect.
     * @return requested quantity as a string.
     */
    public String getRequestedQuantity(OrderLine line) {
        return String.valueOf(line.getQuantity());
    }

    /**
     * Returns the quantity currently allocated to a specific order line.
     *
     * @param line the order line to inspect.
     * @return allocated quantity as a string.
     */
    public String getAllocatedQuantity(OrderLine line) {
        return String.valueOf(line.getAllocatedQuantity());
    }

    /**
     * Retrieves all possible view modes for displaying order lines.
     *
     * @return an observable list of {@link ViewMode} enumeration values.
     */
    public ObservableList<ViewMode> getViewModeValues() {
        return FXCollections.observableArrayList(ViewMode.values());
    }

    /**
     * Allocates available boxes to fulfill an order line based on SKU compatibility
     * and remaining available quantities.
     * <p>
     * Logs allocation operations for traceability.
     *
     * @param line the order line to allocate.
     * @return a message describing the result of the allocation process.
     */
    public String allocateOrderLine(OrderLine line) {
        int missingQuantity = line.getMissingQuantity();

        for (Box box : line.getPossibleBox().values()) {
            if (missingQuantity <= 0) break;

            int boxFreeQuantity = box.getFreeQuantity();
            if (boxFreeQuantity <= 0) continue;

            int quantityToAllocate = Math.min(boxFreeQuantity, missingQuantity);

            box.setAllocatedQuantity(box.getAllocatedQuantity() + quantityToAllocate);
            line.addAllocatedBox(box.getBoxId(), quantityToAllocate);

            UIUtils.addLog(
                    "Allocated " + quantityToAllocate + " items from Box:" + box.getBoxId() +
                            " to OrderLine:" + line.getOrderLineId() +
                            " in Order:" + order.getOrderId(),
                    LogType.INFO_ALLOCATION, RoleType.WAREHOUSE_PLANNER
            );

            missingQuantity -= quantityToAllocate;
        }

        line.updateStates();

        if (missingQuantity <= 0) {
            return "Allocation Completed";
        } else {
            return "Partial Allocation — Missing " + missingQuantity + " units.";
        }
    }

    /**
     * Checks if there are any remaining quantities to be allocated for an order line.
     *
     * @param line the order line to verify.
     * @return {@code true} if no quantities are left to allocate, {@code false} otherwise.
     */
    public boolean verifyQuantityLeft(OrderLine line) {
        return line.getEligibleQuantity() == 0;
    }
}

