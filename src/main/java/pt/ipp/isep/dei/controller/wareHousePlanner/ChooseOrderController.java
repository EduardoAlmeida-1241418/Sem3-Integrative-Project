package pt.ipp.isep.dei.controller.wareHousePlanner;

import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.OrderRelated.LineState;
import pt.ipp.isep.dei.domain.OrderRelated.Order;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.sprint1.OrderLineRepository;
import pt.ipp.isep.dei.data.repository.sprint1.OrderRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Controller responsible for managing order selection and inspection
 * in the Warehouse Planner module.
 * <p>
 * Handles the retrieval, organization, and processing of orders and their lines,
 * including allocation of boxes and line state updates.
 */
public class ChooseOrderController {

    private OrderRepository orderRepository;
    private OrderLineRepository lineRepository;
    private BoxRepository boxRepository;
    private List<Order> orderList;

    /**
     * Default constructor.
     * Initializes the controller with no data until {@link #loadInfo()} is called.
     */
    public ChooseOrderController() {
    }

    /**
     * Loads repository instances and initializes the internal order list.
     */
    public void loadInfo() {
        orderRepository = Repositories.getInstance().getOrderRepository();
        lineRepository = Repositories.getInstance().getOrderLineRepository();
        boxRepository = Repositories.getInstance().getBoxRepository();
        organizeData();
    }

    /**
     * Retrieves all orders sorted according to repository-defined order.
     */
    public void organizeData() {
        orderList = new ArrayList<>(orderRepository.findAllSorted());
    }

    /**
     * Returns the current list of orders.
     *
     * @return a list of all retrieved orders.
     */
    public List<Order> getOrderList() {
        return orderList;
    }

    /**
     * Returns the total number of lines in a given order.
     *
     * @param order the order to inspect.
     * @return number of order lines.
     */
    public int getLineNumber(Order order) {
        return order.getOrderLineIds().size();
    }

    /**
     * Returns the number of lines within a specific order that are in a given state.
     *
     * @param order      the order to analyze.
     * @param lineState  the target line state.
     * @return count of order lines in that state, as a string.
     */
    public String getQuantityOfStateLines(Order order, LineState lineState) {
        loadLineInfo(order);

        int count = 0;
        for (String lineId : order.getOrderLineIds()) {
            if (lineRepository.findByOrderId(lineId).getPossibleState().equals(lineState)) {
                count++;
            }
        }

        return String.valueOf(count);
    }

    /**
     * Loads all order lines for a specific order, sorts them,
     * and updates their allocation and state.
     *
     * @param order the order whose lines will be processed.
     */
    public void loadLineInfo(Order order) {
        List<OrderLine> lines = new ArrayList<>();
        for (String lineID : order.getOrderLineIds()) {
            OrderLine line = lineRepository.findByOrderId(lineID);
            if (line != null) {
                lines.add(line);
            }
        }

        // Sorts lines numerically by their line number
        Collections.sort(lines, Comparator.comparingInt(OrderLine::getLineNumber));

        // Processes each line sequentially
        for (OrderLine line : lines) {
            findPossibleAllocations(line);
        }
    }

    /**
     * Finds and assigns possible box allocations for a given order line
     * based on SKU compatibility and box availability.
     *
     * @param line the order line to analyze.
     */
    private void findPossibleAllocations(OrderLine line) {
        for (Box box : boxRepository.findBySkuItem(line.getSkuItem())) {
            if (box.getBayId() == null) {
                continue;
            }
            line.getPossibleBox().put(box.getBoxId(), box);
        }
        line.updateStates();
    }
}
