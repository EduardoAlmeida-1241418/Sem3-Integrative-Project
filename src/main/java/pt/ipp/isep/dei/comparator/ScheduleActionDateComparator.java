package pt.ipp.isep.dei.comparator;

import pt.ipp.isep.dei.domain.schedule.ScheduleAction;

import java.util.Comparator;

/**
 * Comparator that orders {@link ScheduleAction} instances by their date/time.
 *
 * <p>The comparison is performed by delegating to the {@code compareTo} method
 * of the objects returned by {@link ScheduleAction#getDateTime()}. The comparator
 * follows the natural ordering of those date/time objects.</p>
 *
 * <p>Note: this implementation does not perform {@code null} checks. If either
 * the provided {@code ScheduleAction} references, or the objects returned by
 * {@code getDateTime()}, are {@code null}, a {@link NullPointerException}
 * may be thrown.</p>
 *
 * @see java.util.Comparator
 * @see pt.ipp.isep.dei.domain.schedule.ScheduleAction#getDateTime()
 */
public class ScheduleActionDateComparator implements Comparator<ScheduleAction> {

    /**
     * Compare two {@link ScheduleAction} objects by their date/time.
     *
     * @param a1 the first schedule action to compare
     * @param a2 the second schedule action to compare
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second
     * @throws NullPointerException if either {@code a1} or {@code a2} is
     *         {@code null}, or if either action's {@code getDateTime()} returns
     *         {@code null}
     */
    @Override
    public int compare(ScheduleAction a1, ScheduleAction a2) {
        return a1.getDateTime().compareTo(a2.getDateTime());
    }
}
