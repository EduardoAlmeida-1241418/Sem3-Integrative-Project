package domain;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Date} class.
 * Validates creation, arithmetic operations, comparisons, and formatting.
 */
class DateTest {

    /** Verifies that the constructor creates a valid date. */
    @Test
    void testConstructorValidDate() {
        Date d = new Date(15, 8, 2020);
        assertEquals(15, d.getDay());
        assertEquals(8, d.getMonth());
        assertEquals(2020, d.getYear());
    }

    /** Ensures that the constructor throws an exception for invalid dates. */
    @Test
    void testConstructorInvalidDateThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Date(31, 4, 2021)); // April has 30 days
        assertThrows(IllegalArgumentException.class, () -> new Date(0, 1, 2021));
        assertThrows(IllegalArgumentException.class, () -> new Date(1, 13, 2021));
    }

    /** Tests the leap year verification method. */
    @Test
    void testIsLeapYear() {
        assertTrue(Date.isLeapYear(2020));
        assertFalse(Date.isLeapYear(2021));
        assertFalse(Date.isLeapYear(1900));
        assertTrue(Date.isLeapYear(2000));
    }

    /** Tests the number of days in some months (except February with 28 days). */
    @Test
    void testGetDaysInMonth() {
        assertEquals(31, Date.getDaysInMonth(1, 2021));
        assertEquals(29, Date.getDaysInMonth(2, 2020)); // leap year only
        assertEquals(30, Date.getDaysInMonth(11, 2021));
    }

    /** Tests adding days without changing month. */
    @Test
    void testAddDaysWithinMonth() {
        Date d = new Date(5, 5, 2023);
        d.addDays(10);
        assertEquals(15, d.getDay());
        assertEquals(5, d.getMonth());
        assertEquals(2023, d.getYear());
    }

    /** Tests adding days with month and year transition. */
    @Test
    void testAddDaysAcrossMonthsAndYears() {
        Date d = new Date(30, 12, 2023);
        d.addDays(5);
        assertEquals(4, d.getDay());
        assertEquals(1, d.getMonth());
        assertEquals(2024, d.getYear());
    }

    /** Tests subtracting days within the same month. */
    @Test
    void testSubtractDaysWithinMonth() {
        Date d = new Date(15, 3, 2023);
        d.subtractDays(5);
        assertEquals(10, d.getDay());
        assertEquals(3, d.getMonth());
        assertEquals(2023, d.getYear());
    }

    /** Tests subtracting days across months and years. */
    @Test
    void testSubtractDaysAcrossMonthsAndYears() {
        Date d = new Date(2, 1, 2023);
        d.subtractDays(5);
        assertEquals(28, d.getDay());
        assertEquals(12, d.getMonth());
        assertEquals(2022, d.getYear());
    }

    /** Tests adding and subtracting years (ignores leap-year February edge cases). */
    @Test
    void testAddAndSubtractYears() {
        Date d = new Date(15, 7, 2020);
        d.addYears(1);
        assertEquals(15, d.getDay());
        assertEquals(7, d.getMonth());
        assertEquals(2021, d.getYear());

        d.subtractYears(1);
        assertEquals(15, d.getDay());
        assertEquals(7, d.getMonth());
        assertEquals(2020, d.getYear());
    }

    /** Tests date comparison methods. */
    @Test
    void testComparisonMethods() {
        Date d1 = new Date(1, 1, 2020);
        Date d2 = new Date(2, 1, 2020);
        Date d3 = new Date(1, 1, 2020);

        assertTrue(d1.isBefore(d2));
        assertTrue(d2.isAfter(d1));
        assertTrue(d1.isEqual(d3));
        assertEquals(-1, d1.compareTo(d2));
        assertEquals(0, d1.compareTo(d3));
        assertEquals(1, d2.compareTo(d1));
    }

    /** Tests the calculation of day differences between two dates. */
    @Test
    void testDaysBetween() {
        Date d1 = new Date(1, 1, 2023);
        Date d2 = new Date(10, 1, 2023);
        assertEquals(9, d1.daysBetween(d2));

        Date d3 = new Date(31, 12, 2023);
        Date d4 = new Date(2, 1, 2024);
        assertEquals(2, d3.daysBetween(d4));
    }

    /** Tests deep copy creation. */
    @Test
    void testCopyCreatesNewObject() {
        Date d1 = new Date(5, 6, 2023);
        Date d2 = d1.copy();
        assertNotSame(d1, d2);
        assertEquals(d1.getDay(), d2.getDay());
        assertEquals(d1.getMonth(), d2.getMonth());
        assertEquals(d1.getYear(), d2.getYear());
    }

    /** Tests default and custom string formatting. */
    @Test
    void testToStringFormatting() {
        Date d = new Date(3, 7, 2023);
        assertEquals("03/07/2023", d.toString());
        assertEquals("2023-07-03", d.toString("yyyy-MM-dd"));
        assertEquals("07-03-2023", d.toString("MM-dd-yyyy"));
    }

    /** Tests creation from a formatted string. */
    @Test
    void testFromStringValid() {
        Date d1 = Date.fromString("03/07/2023", "dd/MM/yyyy");
        assertEquals(3, d1.getDay());
        assertEquals(7, d1.getMonth());
        assertEquals(2023, d1.getYear());

        Date d2 = Date.fromString("2023-12-25", "yyyy-MM-dd");
        assertEquals(25, d2.getDay());
        assertEquals(12, d2.getMonth());
        assertEquals(2023, d2.getYear());
    }

    /** Ensures that fromString throws an exception for invalid inputs. */
    @Test
    void testFromStringInvalidThrows() {
        assertThrows(IllegalArgumentException.class, () -> Date.fromString("2023/99/10", "yyyy/MM/dd"));
        assertThrows(IllegalArgumentException.class, () -> Date.fromString("invalid", "dd/MM/yyyy"));
    }
}
