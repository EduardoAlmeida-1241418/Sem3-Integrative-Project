package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;

import java.util.*;

/**
 * Repository responsible for managing {@link OrderLine} entities.
 * Provides CRUD operations and ensures unique order line identifiers.
 */
public class OrderLineRepository {

    /** Internal map storing order lines, indexed by their unique IDs. */
    private final Map<String, OrderLine> orderLines = new TreeMap<>();

    /**
     * Adds a new order line to the repository.
     *
     * @param orderLine the order line to add
     * @throws IllegalArgumentException if the order line or its ID is null or empty
     * @throws IllegalStateException if an order line with the same ID already exists
     */
    public void add(OrderLine orderLine) {
        if (orderLine == null) {
            throw new IllegalArgumentException("OrderLine cannot be null.");
        }
        String id = orderLine.getOrderLineId();
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("OrderLine ID cannot be null or empty.");
        }
        if (orderLines.containsKey(id)) {
            throw new IllegalStateException("An OrderLine with the same ID already exists: " + id);
        }
        orderLines.put(id, orderLine);
    }

    /**
     * Finds an order line by its unique ID.
     *
     * @param orderLineId the ID of the order line
     * @return the corresponding {@link OrderLine} object
     * @throws IllegalArgumentException if the ID is null or empty
     * @throws NoSuchElementException if no order line exists with the given ID
     */
    public OrderLine findById(String orderLineId) {
        if (orderLineId == null || orderLineId.isEmpty()) {
            throw new IllegalArgumentException("OrderLine ID cannot be null or empty.");
        }
        OrderLine orderLine = orderLines.get(orderLineId);
        if (orderLine == null) {
            throw new NoSuchElementException("OrderLine not found with ID: " + orderLineId);
        }
        return orderLine;
    }

    /**
     * Checks if an order line with the specified ID exists.
     *
     * @param orderLineId the ID of the order line
     * @return true if the order line exists, false otherwise
     * @throws IllegalArgumentException if the ID is null or empty
     */
    public boolean existsById(String orderLineId) {
        if (orderLineId == null || orderLineId.isEmpty()) {
            throw new IllegalArgumentException("OrderLine ID cannot be null or empty.");
        }
        return orderLines.containsKey(orderLineId);
    }

    /**
     * Removes an order line from the repository by its ID.
     *
     * @param orderLineId the ID of the order line to remove
     * @throws IllegalArgumentException if the ID is null or empty
     * @throws NoSuchElementException if no order line exists with the given ID
     */
    public void remove(String orderLineId) {
        if (orderLineId == null || orderLineId.isEmpty()) {
            throw new IllegalArgumentException("OrderLine ID cannot be null or empty.");
        }
        if (!orderLines.containsKey(orderLineId)) {
            throw new NoSuchElementException("OrderLine not found with ID: " + orderLineId);
        }
        orderLines.remove(orderLineId);
    }

    /**
     * Retrieves all order lines stored in the repository.
     *
     * @return unmodifiable collection of all order lines
     */
    public Collection<OrderLine> findAll() {
        return Collections.unmodifiableCollection(orderLines.values());
    }

    /**
     * Finds an order line by its order line ID.
     *
     * @param orderLineId the order line ID
     * @return the corresponding {@link OrderLine} object
     * @throws IllegalArgumentException if the ID is null or empty
     * @throws NoSuchElementException if no order line is found for the given ID
     */
    public OrderLine findByOrderId(String orderLineId) {
        if (orderLineId == null || orderLineId.isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }
        for (OrderLine orderLine : orderLines.values()) {
            if (orderLine.getOrderLineId().equals(orderLineId)) {
                return orderLine;
            }
        }
        throw new NoSuchElementException("No OrderLine found with Order Line ID: " + orderLineId);
    }

    /**
     * Removes all order lines from the repository.
     */
    public void clear() {
        orderLines.clear();
    }

    /**
     * Counts the total number of order lines currently stored.
     *
     * @return total count of order lines
     */
    public int count() {
        return orderLines.size();
    }
}
