package pt.ipp.isep.dei.domain.Tree.Interval;

import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.DateTime;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.TimeInterval;
import pt.ipp.isep.dei.domain.schedule.ScheduleEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Interval Tree implementation.
 *
 * <p>This data structure allows the insertion of overlapping events,
 * stores all events without applying business rules,
 * and supports overlap detection queries.</p>
 *
 * <p>Based on the implementation described at:
 * https://www.geeksforgeeks.org/dsa/interval-tree/</p>
 */
public class IntervalTree {

    /** Root node of the interval tree */
    private Node root;

    /* =========================
       Public API
       ========================= */

    /**
     * Checks whether the interval tree is empty.
     *
     * @return true if the tree is empty, false otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Inserts a schedule event into the interval tree.
     *
     * @param event schedule event to insert
     * @throws NullPointerException if the event is null
     */
    public void insert(ScheduleEvent event) {
        Objects.requireNonNull(event, "ScheduleEvent cannot be null");
        root = insert(root, event);
    }

    /**
     * Removes a schedule event from the interval tree.
     *
     * @param event schedule event to remove
     * @throws NullPointerException if the event is null
     */
    public void remove(ScheduleEvent event) {
        Objects.requireNonNull(event, "ScheduleEvent cannot be null");
        root = remove(root, event);
    }

    /**
     * Checks if there is any overlap with the given time interval.
     *
     * @param interval time interval to check
     * @return true if an overlap exists, false otherwise
     */
    public boolean hasOverlap(TimeInterval interval) {
        Objects.requireNonNull(interval);
        return findAnyOverlap(interval) != null;
    }

    /**
     * Finds any schedule event that overlaps the given time interval.
     *
     * @param interval time interval to check
     * @return overlapping schedule event or null if none exists
     */
    public ScheduleEvent findAnyOverlap(TimeInterval interval) {
        Objects.requireNonNull(interval);
        Node n = searchOverlap(root, interval);
        return n == null ? null : n.event;
    }

    /**
     * Finds all schedule events that overlap the given time interval.
     *
     * @param interval time interval to check
     * @return list of overlapping schedule events
     */
    public List<ScheduleEvent> findAllOverlaps(TimeInterval interval) {
        Objects.requireNonNull(interval);
        List<ScheduleEvent> result = new ArrayList<>();
        findAllOverlaps(root, interval, result);
        return result;
    }

    /* =========================
       Node
       ========================= */

    /**
     * Internal node representation of the interval tree.
     */
    private static class Node {
        ScheduleEvent event;
        long low;
        long high;
        long max;
        Node left;
        Node right;

        /**
         * Constructs a node for the given event and interval bounds.
         *
         * @param event schedule event
         * @param low start instant
         * @param high end instant
         */
        Node(ScheduleEvent event, long low, long high) {
            this.event = event;
            this.low = low;
            this.high = high;
            this.max = high;
        }
    }

    /* =========================
       Insert / Remove
       ========================= */

    /**
     * Inserts a schedule event into the subtree rooted at the given node.
     *
     * @param node current node
     * @param event event to insert
     * @return updated node
     */
    private Node insert(Node node, ScheduleEvent event) {
        long low = start(event);
        long high = end(event);

        if (node == null)
            return new Node(event, low, high);

        if (low < node.low)
            node.left = insert(node.left, event);
        else
            node.right = insert(node.right, event);

        node.max = Math.max(node.max, high);
        return node;
    }

    /**
     * Removes a schedule event from the subtree rooted at the given node.
     *
     * @param node current node
     * @param event event to remove
     * @return updated node
     */
    private Node remove(Node node, ScheduleEvent event) {
        if (node == null)
            return null;

        long low = start(event);

        if (low < node.low) {
            node.left = remove(node.left, event);
        } else if (low > node.low) {
            node.right = remove(node.right, event);
        } else {
            if (node.event != event)
                return node;

            if (node.left == null)
                return node.right;
            if (node.right == null)
                return node.left;

            Node successor = min(node.right);
            node.event = successor.event;
            node.low = successor.low;
            node.high = successor.high;

            node.right = remove(node.right, successor.event);
        }

        node.max = computeMax(node);
        return node;
    }

    /* =========================
       Search
       ========================= */

    /**
     * Searches for any overlapping node with the given interval.
     *
     * @param node current node
     * @param interval interval to check
     * @return overlapping node or null
     */
    private Node searchOverlap(Node node, TimeInterval interval) {
        if (node == null)
            return null;

        if (overlaps(node.low, node.high, interval))
            return node;

        if (node.left != null && node.left.max >= start(interval))
            return searchOverlap(node.left, interval);

        return searchOverlap(node.right, interval);
    }

    /**
     * Collects all overlapping events with the given interval.
     *
     * @param node current node
     * @param interval interval to check
     * @param result result list
     */
    private void findAllOverlaps(Node node, TimeInterval interval, List<ScheduleEvent> result) {
        if (node == null)
            return;

        if (overlaps(node.low, node.high, interval))
            result.add(node.event);

        if (node.left != null && node.left.max >= start(interval))
            findAllOverlaps(node.left, interval, result);

        if (node.right != null && node.low <= end(interval))
            findAllOverlaps(node.right, interval, result);
    }

    /* =========================
       Helpers
       ========================= */

    /**
     * Returns the node with the minimum low value in the subtree.
     *
     * @param node subtree root
     * @return minimum node
     */
    private Node min(Node node) {
        while (node.left != null)
            node = node.left;
        return node;
    }

    /**
     * Computes the maximum interval endpoint in the subtree.
     *
     * @param node current node
     * @return maximum endpoint
     */
    private long computeMax(Node node) {
        long max = node.high;
        if (node.left != null)
            max = Math.max(max, node.left.max);
        if (node.right != null)
            max = Math.max(max, node.right.max);
        return max;
    }

    /**
     * Checks whether a node interval overlaps the given interval.
     *
     * @param low1 start of node interval
     * @param high1 end of node interval
     * @param interval interval to compare
     * @return true if overlapping
     */
    private boolean overlaps(long low1, long high1, TimeInterval interval) {
        return low1 <= end(interval) && start(interval) <= high1;
    }

    private long start(ScheduleEvent event) {
        return start(event.getTimeInterval());
    }

    private long end(ScheduleEvent event) {
        return end(event.getTimeInterval());
    }

    private long start(TimeInterval interval) {
        return toEpochSeconds(interval.getInitialDateTime());
    }

    private long end(TimeInterval interval) {
        return toEpochSeconds(interval.getFinalDateTime());
    }

    /* =========================
       DateTime → epoch seconds
       ========================= */

    /**
     * Converts a DateTime to epoch seconds.
     *
     * @param dt date-time to convert
     * @return epoch seconds
     */
    private long toEpochSeconds(DateTime dt) {
        Date date = dt.getDate();
        Time time = dt.getTime();
        return daysSinceEpoch(date) * 86400L + time.toSeconds();
    }

    /**
     * Logical epoch: 01/01/0001.
     *
     * @param date date to convert
     * @return number of days since the epoch
     */
    private long daysSinceEpoch(Date date) {
        int y = date.getYear();
        int m = date.getMonth();
        int d = date.getDay();

        long days = 0;

        for (int year = 1; year < y; year++)
            days += Date.isLeapYear(year) ? 366 : 365;

        for (int month = 1; month < m; month++)
            days += Date.getDaysInMonth(month, y);

        days += (d - 1);
        return days;
    }

    /**
     * Returns all schedule events stored in the tree.
     *
     * @return list of all schedule events
     */
    public List<ScheduleEvent> getAll() {
        List<ScheduleEvent> result = new ArrayList<>();
        collectAll(root, result);
        return result;
    }

    /**
     * Recursively collects all events from the tree.
     *
     * @param node current node
     * @param result result list
     */
    private void collectAll(Node node, List<ScheduleEvent> result) {
        if (node == null)
            return;

        result.add(node.event);
        collectAll(node.left, result);
        collectAll(node.right, result);
    }
}
