package pt.ipp.isep.dei.domain;

import java.util.Objects;

/**
 * Represents a closed interval between two DateTime instances.
 * The initial DateTime must not be after the final DateTime.
 */
public class TimeInterval {

    private final DateTime initialDateTime;
    private final DateTime finalDateTime;

    /**
     * Creates a TimeInterval between two DateTime instances.
     *
     * @param initialDateTime start of the interval
     * @param finalDateTime   end of the interval
     * @throws IllegalArgumentException if initialDateTime is after finalDateTime
     */
    public TimeInterval(DateTime initialDateTime, DateTime finalDateTime) {
        if (initialDateTime == null || finalDateTime == null) {
            throw new IllegalArgumentException("DateTime values cannot be null");
        }
        if (initialDateTime.compareTo(finalDateTime) > 0) {
            throw new IllegalArgumentException("Initial DateTime must be before or equal to final DateTime");
        }
        this.initialDateTime = initialDateTime;
        this.finalDateTime = finalDateTime;
    }

    public DateTime getInitialDateTime() {
        return initialDateTime;
    }

    public DateTime getFinalDateTime() {
        return finalDateTime;
    }

    /**
     * @return duration of the interval in seconds
     */
    public long getDurationInSeconds() {
        return initialDateTime.secondsUntil(finalDateTime);
    }

    /**
     * Checks whether a given DateTime is inside this interval (inclusive).
     *
     * @param dateTime DateTime to test
     * @return true if inside interval
     */
    public boolean contains(DateTime dateTime) {
        return dateTime.compareTo(initialDateTime) >= 0
                && dateTime.compareTo(finalDateTime) <= 0;
    }

    /**
     * Checks whether this interval overlaps another interval.
     *
     * @param other another TimeInterval
     * @return true if intervals overlap
     */
    public boolean overlaps(TimeInterval other) {
        return this.initialDateTime.compareTo(other.finalDateTime) <= 0
                && this.finalDateTime.compareTo(other.initialDateTime) >= 0;
    }

    @Override
    public String toString() {
        return "From " + initialDateTime.getTime() + " - "+ initialDateTime.getDate() + " to " + finalDateTime.getTime() + " - " + finalDateTime.getDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeInterval)) return false;
        TimeInterval that = (TimeInterval) o;
        return initialDateTime.equals(that.initialDateTime)
                && finalDateTime.equals(that.finalDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialDateTime, finalDateTime);
    }
}
