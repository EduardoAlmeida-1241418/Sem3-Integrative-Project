package pt.ipp.isep.dei.domain.OrderRelated;

import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a customer order with an identifier, due date and time,
 * priority level, and associated order line identifiers.
 */
public class Order {
    private String orderId;
    private Date dueDate;
    private Time dueTime = new Time();
    private int priority;
    private Set<String> orderLineIds = new TreeSet<>();

    /**
     * Constructs an {@code Order} with a due date and priority.
     * The due time is initialized by default.
     *
     * @param orderId  the unique identifier of the order
     * @param dueDate  the due date for order completion
     * @param priority the order priority level
     */
    public Order(String orderId, Date dueDate, int priority) {
        this.orderId = orderId;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    /**
     * Constructs an {@code Order} with a due date, due time, and priority.
     *
     * @param orderId  the unique identifier of the order
     * @param dueDate  the due date for order completion
     * @param dueTime  the due time for order completion
     * @param priority the order priority level
     */
    public Order(String orderId, Date dueDate, Time dueTime, int priority) {
        this.orderId = orderId;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.priority = priority;
    }

    /**
     * @return the order identifier
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the order identifier to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the due date of the order
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the due date to set
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the due time of the order
     */
    public Time getDueTime() {
        return dueTime;
    }

    /**
     * @param dueTime the due time to set
     */
    public void setDueTime(Time dueTime) {
        this.dueTime = dueTime;
    }

    /**
     * @return the priority level of the order
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority level to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Adds an order line identifier to this order.
     *
     * @param orderLineId the identifier of the order line to add
     */
    public void addOrderLine(String orderLineId) {
        this.orderLineIds.add(orderLineId);
    }

    /**
     * @return a set of order line identifiers associated with this order
     */
    public Set<String> getOrderLineIds() {
        return orderLineIds;
    }

    /**
     * @param orderLineIds the set of order line identifiers to set
     */
    public void setOrderLineIds(Set<String> orderLineIds) {
        this.orderLineIds = orderLineIds;
    }

    /**
     * @return a string representation of the order including all line IDs
     */
    @Override
    public String toString() {
        String string = orderId + " - " + dueDate + " - " + dueTime + " - " + priority + "\n";
        for (String orderLineId : orderLineIds) {
            string += orderLineId + ", ";
        }
        return string;
    }
}
