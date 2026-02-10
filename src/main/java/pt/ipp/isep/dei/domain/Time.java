package pt.ipp.isep.dei.domain;

/**
 * Represents a specific time of day, including hours, minutes, and seconds.
 * Provides basic validation, formatting, comparison, and arithmetic operations.
 */
public class Time {

    /** Hour component of the time (0–23). */
    private int hour;

    /** Minute component of the time (0–59). */
    private int minute;

    /** Second component of the time (0–59). */
    private int second;

    /**
     * Constructs a Time instance with specified hour, minute, and second.
     *
     * @param hour the hour value (0–23)
     * @param minute the minute value (0–59)
     * @param second the second value (0–59)
     * @throws IllegalArgumentException if any component is outside valid range
     */
    public Time(int hour, int minute, int second) {
        setHour(hour);
        setMinute(minute);
        setSecond(second);
    }

    public Time(int hour, int minute) {
        setHour(hour);
        setMinute(minute);
        setSecond(0);
    }

    /**
     * Constructs a Time instance initialized to 00:00:00.
     */
    public Time() {
        this(0, 0, 0);
    }

    /** @return the hour value */
    public int getHour() {
        return hour;
    }

    /**
     * Sets the hour value.
     *
     * @param hour the hour to set (0–23)
     * @throws IllegalArgumentException if the value is outside valid range
     */
    public void setHour(int hour) {
        if (hour < 0 || hour >= 24) {
            throw new IllegalArgumentException("Hour must be between 0 and 23.");
        }
        this.hour = hour;
    }

    /** @return the minute value */
    public int getMinute() {
        return minute;
    }

    /**
     * Sets the minute value.
     *
     * @param minute the minute to set (0–59)
     * @throws IllegalArgumentException if the value is outside valid range
     */
    public void setMinute(int minute) {
        if (minute < 0 || minute >= 60) {
            throw new IllegalArgumentException("Minute must be between 0 and 59.");
        }
        this.minute = minute;
    }

    /** @return the second value */
    public int getSecond() {
        return second;
    }

    /**
     * Sets the second value.
     *
     * @param second the second to set (0–59)
     * @throws IllegalArgumentException if the value is outside valid range
     */
    public void setSecond(int second) {
        if (second < 0 || second >= 60) {
            throw new IllegalArgumentException("Second must be between 0 and 59.");
        }
        this.second = second;
    }

    /**
     * Returns a string representation of the time in HH:mm:ss format.
     *
     * @return formatted string of the time
     */
    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    /**
     * Adds the specified number of seconds to the time, wrapping around after 24 hours.
     *
     * @param seconds number of seconds to add (may be negative)
     */
    public void addSeconds(int seconds) {
        int total = hour * 3600 + minute * 60 + second + seconds;
        total = ((total % (24 * 3600)) + (24 * 3600)) % (24 * 3600);

        hour = total / 3600;
        minute = (total % 3600) / 60;
        second = total % 60;
    }

    /**
     * Converts this time to total seconds since 00:00:00.
     *
     * @return total number of seconds
     */
    public int toSeconds() {
        return hour * 3600 + minute * 60 + second;
    }

    /**
     * Compares this time with another time.
     *
     * @param other another {@link Time} instance
     * @return a negative integer, zero, or a positive integer
     *         if this time is earlier than, equal to, or later than the specified time
     */
    public int compareTo(Time other) {
        if (this.hour != other.hour) {
            return Integer.compare(this.hour, other.hour);
        }
        if (this.minute != other.minute) {
            return Integer.compare(this.minute, other.minute);
        }
        return Integer.compare(this.second, other.second);
    }

    public Time copy() {
        return new Time(this.hour, this.minute, this.second);
    }
}
