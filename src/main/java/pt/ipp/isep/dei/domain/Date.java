package pt.ipp.isep.dei.domain;

/**
 * Represents a date with day, month, and year components.
 * Provides utility methods for validation, comparison, arithmetic operations,
 * and conversion to and from formatted string representations.
 */
public class Date {

    private int day;
    private int month;
    private int year;

    /**
     * Constructs a {@code Date} object after validating the provided parameters.
     *
     * @param day   the day of the month (1–31)
     * @param month the month of the year (1–12)
     * @param year  the year (must be positive)
     * @throws IllegalArgumentException if the date is invalid
     */
    public Date(int day, int month, int year) {
        if (!isValid(day, month, year)) {
            throw new IllegalArgumentException("Invalid date");
        }
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }

    /**
     * Sets the day of the month, ensuring the new date remains valid.
     *
     * @param day the new day to set
     * @throws IllegalArgumentException if the resulting date is invalid
     */
    public void setDay(int day) {
        if (!isValid(day, this.month, this.year)) throw new IllegalArgumentException("Invalid day");
        this.day = day;
    }

    /**
     * Sets the month, ensuring the new date remains valid.
     *
     * @param month the new month to set
     * @throws IllegalArgumentException if the resulting date is invalid
     */
    public void setMonth(int month) {
        if (!isValid(this.day, month, this.year)) throw new IllegalArgumentException("Invalid month");
        this.month = month;
    }

    /**
     * Sets the year, ensuring the new date remains valid.
     *
     * @param year the new year to set
     * @throws IllegalArgumentException if the resulting date is invalid
     */
    public void setYear(int year) {
        if (!isValid(this.day, this.month, year)) throw new IllegalArgumentException("Invalid year");
        this.year = year;
    }

    /**
     * Determines if the given year is a leap year.
     *
     * @param year the year to test
     * @return true if leap year, false otherwise
     */
    public static boolean isLeapYear(int year) {
        if (year % 4 != 0) return false;
        if (year % 100 != 0) return true;
        return year % 400 == 0;
    }

    /**
     * Validates if a combination of day, month, and year forms a valid date.
     *
     * @param day   the day to test
     * @param month the month to test
     * @param year  the year to test
     * @return true if the date is valid, false otherwise
     */
    public static boolean isValid(int day, int month, int year) {
        if (year < 1 || month < 1 || month > 12) return false;
        int maxDay = getDaysInMonth(month, year);
        return day >= 1 && day <= maxDay;
    }

    /**
     * Returns the number of days in a given month and year.
     *
     * @param month the month (1–12)
     * @param year  the year
     * @return the number of days in the month
     */
    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case 2: return isLeapYear(year) ? 29 : 28;
            case 4: case 6: case 9: case 11: return 30;
            default: return 31;
        }
    }

    /**
     * Adds a given number of days to this date.
     * Handles month and year transitions.
     *
     * @param days the number of days to add (can be negative)
     */
    public void addDays(int days) {
        if (days >= 0) {
            while (days > 0) {
                int maxDay = getDaysInMonth(month, year);
                if (day + days <= maxDay) {
                    day += days;
                    break;
                } else {
                    days -= (maxDay - day + 1);
                    day = 1;
                    addMonths(1);
                }
            }
        } else {
            subtractDays(-days);
        }
    }

    /**
     * Subtracts a given number of days from this date.
     *
     * @param days the number of days to subtract
     */
    public void subtractDays(int days) {
        while (days > 0) {
            if (day > days) {
                day -= days;
                break;
            } else {
                days -= day;
                addMonths(-1);
                day = getDaysInMonth(month, year);
            }
        }
    }

    /**
     * Adds a given number of months to this date.
     * Adjusts year and day automatically if necessary.
     *
     * @param months the number of months to add (can be negative)
     */
    public void addMonths(int months) {
        int totalMonths = month + months - 1;
        int newYear = year + totalMonths / 12;
        int newMonth = totalMonths % 12 + 1;
        if (newMonth <= 0) {
            newMonth += 12;
            newYear -= 1;
        }
        month = newMonth;
        year = newYear;
        int maxDay = getDaysInMonth(month, year);
        if (day > maxDay) day = maxDay;
    }

    /**
     * Adds a given number of years to this date.
     *
     * @param years the number of years to add
     */
    public void addYears(int years) {
        year += years;
        if (month == 2 && day == 29 && !isLeapYear(year)) day = 28;
    }

    /**
     * Subtracts a given number of years from this date.
     *
     * @param years the number of years to subtract
     */
    public void subtractYears(int years) {
        addYears(-years);
    }

    /**
     * Checks if this date is before another date.
     *
     * @param other the date to compare
     * @return true if this date is before the other
     */
    public boolean isBefore(Date other) {
        if (year != other.year) return year < other.year;
        if (month != other.month) return month < other.month;
        return day < other.day;
    }

    /**
     * Checks if this date is after another date.
     *
     * @param other the date to compare
     * @return true if this date is after the other
     */
    public boolean isAfter(Date other) {
        if (year != other.year) return year > other.year;
        if (month != other.month) return month > other.month;
        return day > other.day;
    }

    /**
     * Checks if this date is equal to another date.
     *
     * @param other the date to compare
     * @return true if both dates are equal
     */
    public boolean isEqual(Date other) {
        return year == other.year && month == other.month && day == other.day;
    }

    /**
     * Compares this date with another date.
     *
     * @param other the date to compare
     * @return 1 if after, -1 if before, 0 if equal
     */
    public int compareTo(Date other) {
        if (year != other.year) return Integer.compare(year, other.year);
        if (month != other.month) return Integer.compare(month, other.month);
        return Integer.compare(day, other.day);
    }

    /**
     * Calculates the number of days between this date and another date.
     *
     * @param other the date to compare
     * @return the number of days between the two dates
     */
    public int daysBetween(Date other) {
        Date start = this.isBefore(other) ? this.copy() : other.copy();
        Date end = this.isBefore(other) ? other.copy() : this.copy();
        int count = 0;
        while (!start.isEqual(end)) {
            start.addDays(1);
            count++;
        }
        return count;
    }

    /**
     * Creates a deep copy of this date.
     *
     * @return a new {@code Date} object with the same values
     */
    public Date copy() {
        return new Date(day, month, year);
    }

    /**
     * @return a string representation in the format "dd/MM/yyyy"
     */
    public String toString() {
        return String.format("%02d/%02d/%04d", day, month, year);
    }

    /**
     * Returns a formatted string representation of this date.
     * Example format: "yyyy-MM-dd".
     *
     * @param format the desired date format
     * @return the formatted date string
     */
    public String toString(String format) {
        return format.replace("dd", String.format("%02d", day))
                .replace("MM", String.format("%02d", month))
                .replace("yyyy", String.format("%04d", year));
    }

    /**
     * Creates a {@code Date} object from a formatted string.
     *
     * @param str    the string representing a date
     * @param format the format pattern used in the string
     * @return the corresponding {@code Date} object
     * @throws IllegalArgumentException if the string is invalid or does not match the format
     */
    public static Date fromString(String str, String format) {
        try {
            int d = -1, m = -1, y = -1;
            String[] partsFormat = format.split("[^a-zA-Z]+");
            String[] partsDate = str.split("[^0-9]+");
            for (int i = 0; i < partsFormat.length; i++) {
                switch (partsFormat[i]) {
                    case "dd": d = Integer.parseInt(partsDate[i]); break;
                    case "MM": m = Integer.parseInt(partsDate[i]); break;
                    case "yyyy": y = Integer.parseInt(partsDate[i]); break;
                }
            }
            return new Date(d, m, y);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date string");
        }
    }
}
