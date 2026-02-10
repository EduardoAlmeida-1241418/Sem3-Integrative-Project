package pt.ipp.isep.dei.controller.wareHousePlanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.data.repository.sprint1.OrderLineRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

/**
 * Controller responsible for managing and displaying order line allocations
 * within the Warehouse Planner module.
 * <p>
 * Provides functionality to retrieve all order lines and calculate
 * total allocated units for each line.
 */
public class OrderAllocationsController {

    private OrderLineRepository orderLineRepository;

    /**
     * Initializes the controller and loads repository instances.
     */
    public OrderAllocationsController() {
        initRepos();
    }

    /**
     * Initializes the repository references from the repository singleton.
     */
    private void initRepos() {
        orderLineRepository = Repositories.getInstance().getOrderLineRepository();
    }

    /**
     * Retrieves all order lines available in the system.
     *
     * @return an observable list of {@link OrderLine} objects.
     */
    public ObservableList<OrderLine> getOrderLines() {
        return FXCollections.observableArrayList(orderLineRepository.findAll());
    }

    /**
     * Returns the total number of allocated units for a given order line.
     *
     * @param selectedLine the order line to analyze.
     * @return the total count of allocated units.
     */
    public int getTotalAllocatedUnits(OrderLine selectedLine) {
        return selectedLine.getAllocatedBoxCount();
    }
}
