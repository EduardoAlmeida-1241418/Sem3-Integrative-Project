package pt.ipp.isep.dei.comparator;

import java.util.Comparator;
import pt.ipp.isep.dei.domain.OrderRelated.Order;

/**
 * Comparator for {@link Order} objects.
 * <p>
 * Orders are compared by priority, then due date, then due time, and finally by order ID.
 */
public class OrderComparator implements Comparator<Order> {

    /**
     * Compares two orders based on their priority, due date, due time, and ID.
     * <p>
     * The comparison order is:
     * <ol>
     *   <li>Priority (ascending)</li>
     *   <li>Due date (ascending)</li>
     *   <li>Due time (ascending)</li>
     *   <li>Order ID (lexicographical)</li>
     * </ol>
     *
     * @param o1 the first order to compare
     * @param o2 the second order to compare
     * @return a negative integer, zero, or a positive integer if the first order
     *         has higher priority, an earlier due date or time, or a lower ID
     */
    @Override
    public int compare(Order o1, Order o2) {
        int priorityCompare = Integer.compare(o1.getPriority(), o2.getPriority());
        if (priorityCompare != 0) return priorityCompare;

        int dateCompare = o1.getDueDate().compareTo(o2.getDueDate());
        if (dateCompare != 0) return dateCompare;

        int timeCompare = o1.getDueTime().compareTo(o2.getDueTime());
        if (timeCompare != 0) return timeCompare;

        return o1.getOrderId().compareTo(o2.getOrderId());
    }
}
