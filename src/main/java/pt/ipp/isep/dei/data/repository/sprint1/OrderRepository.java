package pt.ipp.isep.dei.data.repository.sprint1;

import pt.ipp.isep.dei.comparator.OrderComparator;
import pt.ipp.isep.dei.domain.OrderRelated.Order;

import java.util.*;

/**
 * Repository responsible for managing {@link Order} entities.
 * Provides CRUD operations and supports retrieval of sorted order collections.
 */
public class OrderRepository {

    /** Internal map storing orders indexed by their unique order IDs. */
    private final Map<String, Order> orders = new TreeMap<>();

    /**
     * Adds a new order to the repository.
     *
     * @param order the order to add
     * @throws IllegalArgumentException if the order or its ID is null or empty
     * @throws IllegalStateException if an order with the same ID already exists
     */
    public void add(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }
        String id = order.getOrderId();
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }
        if (orders.containsKey(id)) {
            throw new IllegalStateException("An order with the same ID already exists: " + id);
        }
        orders.put(id, order);
    }

    /**
     * Finds an order by its unique identifier.
     *
     * @param orderId the order ID
     * @return the corresponding {@link Order} object
     * @throws IllegalArgumentException if the order ID is null or empty
     * @throws NoSuchElementException if no order exists with the given ID
     */
    public Order findById(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }
        Order order = orders.get(orderId);
        if (order == null) {
            throw new NoSuchElementException("Order not found with ID: " + orderId);
        }
        return order;
    }

    /**
     * Checks if an order with the specified ID exists.
     *
     * @param orderId the order ID
     * @return true if the order exists, false otherwise
     * @throws IllegalArgumentException if the order ID is null or empty
     */
    public boolean existsById(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }
        return orders.containsKey(orderId);
    }

    /**
     * Removes an order from the repository by its ID.
     *
     * @param orderId the order ID
     * @throws IllegalArgumentException if the order ID is null or empty
     * @throws NoSuchElementException if no order exists with the given ID
     */
    public void remove(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }
        if (!orders.containsKey(orderId)) {
            throw new NoSuchElementException("Order not found with ID: " + orderId);
        }
        orders.remove(orderId);
    }

    /**
     * Retrieves all orders stored in the repository.
     *
     * @return unmodifiable collection of all orders
     */
    public Collection<Order> findAll() {
        return Collections.unmodifiableCollection(orders.values());
    }

    /**
     * Retrieves all orders sorted according to {@link OrderComparator}.
     *
     * @return unmodifiable list of sorted orders
     */
    public Collection<Order> findAllSorted() {
        List<Order> sorted = new ArrayList<>(orders.values());
        sorted.sort(new OrderComparator());
        return Collections.unmodifiableList(sorted);
    }

    /**
     * Removes all orders from the repository.
     */
    public void clear() {
        orders.clear();
    }

    /**
     * Counts the total number of orders currently stored.
     *
     * @return total count of orders
     */
    public int count() {
        return orders.size();
    }
}
