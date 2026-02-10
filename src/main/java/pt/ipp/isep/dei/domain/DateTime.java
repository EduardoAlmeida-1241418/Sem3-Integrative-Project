package pt.ipp.isep.dei.domain;

/**
 * Represents a date and time combination.
 * This class encapsulates a Date and a Time and provides
 * comparison and time manipulation utilities.
 */
public class DateTime implements Comparable<DateTime> {

    /** Unique identifier of the DateTime instance */
    private final int id;
    /** Date component */
    private final Date date;
    /** Time component */
    private final Time time;

    /** Static counter used to generate identifiers */
    private static int idCounter = 1;

    /**
     * Constructs a DateTime with a specific identifier.
     *
     * @param id DateTime identifier
     * @param date date component
     * @param time time component
     */
    public DateTime(int id, Date date, Time time) {
        this.id = id;
        this.date = date;
        this.time = time;
    }

    /**
     * Constructs a DateTime with an automatically generated identifier.
     *
     * @param date date component
     * @param time time component
     */
    public DateTime(Date date, Time time) {
        this.id = idCounter++;
        this.date = date;
        this.time = time;
    }

    /**
     * Compares this DateTime with another DateTime.
     * Comparison is first done by date and then by time.
     *
     * @param other DateTime to compare
     * @return comparison result
     */
    @Override
    public int compareTo(DateTime other) {
        if (date.isAfter(other.date)) return 1;
        if (date.isBefore(other.date)) return -1;
        return time.compareTo(other.time);
    }

    /**
     * Returns the DateTime identifier.
     *
     * @return id
     */
    public int getId() { return id; }

    /**
     * Returns the date component.
     *
     * @return date
     */
    public Date getDate() { return date; }

    /**
     * Returns the time component.
     *
     * @return time
     */
    public Time getTime() { return time; }

    /**
     * Calculates the number of seconds until another DateTime.
     *
     * @param other target DateTime
     * @return number of seconds between this and the other DateTime
     */
    public int secondsUntil(DateTime other) {
        return (date.daysBetween(other.date) * 86400)
                + (other.time.toSeconds() - time.toSeconds());
    }

    /**
     * Checks if this DateTime is before another.
     *
     * @param other DateTime to compare
     * @return true if before
     */
    public boolean isBefore(DateTime other) {
        return this.compareTo(other) < 0;
    }

    /**
     * Checks if this DateTime is after another.
     *
     * @param other DateTime to compare
     * @return true if after
     */
    public boolean isAfter(DateTime other) {
        return this.compareTo(other) > 0;
    }

    /**
     * Checks if this DateTime is equal to another.
     *
     * @param other DateTime to compare
     * @return true if equal
     */
    public boolean isEqual(DateTime other) {
        return this.compareTo(other) == 0;
    }

    /**
     * Adds a number of seconds to this DateTime without altering
     * the original date structure incorrectly.
     *
     * @param sec number of seconds to add
     * @return new DateTime instance with updated time
     */
    public DateTime plusSeconds(double sec) {
        if (sec == 0.0) return this;

        double totalSec = time.toSeconds() + sec;

        int days;
        if (totalSec >= 0) {
            days = (int) (totalSec / 86400);
            totalSec = totalSec % 86400;
        } else {
            days = (int) Math.floor(totalSec / 86400);
            totalSec = totalSec - (days * 86400);
        }

        Date newDate = date.copy();
        if (days > 0) newDate.addDays(days);
        if (days < 0) newDate.subtractDays(-days);

        int hour = (int) (totalSec / 3600);
        totalSec %= 3600;

        int min = (int) (totalSec / 60);
        double secFinal = totalSec % 60;

        return new DateTime(
                newDate,
                new Time(hour, min, (int) secFinal)
        );
    }

    /**
     * Returns the string representation of the DateTime.
     *
     * @return formatted DateTime string
     */
    @Override
    public String toString() {
        return date.toString() + "\n" + time.toString();
    }
}
