package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Time;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Time} class.
 * <p>
 * Ensures correct behavior of constructors, validation,
 * formatting, comparison, and arithmetic operations.
 * </p>
 */
class TimeTest {

    private Time time;

    /** Base setup executed before each test. */
    @BeforeEach
    void setUp() {
        time = new Time(10, 30, 15);
    }

    /** Verifies that the constructor correctly initializes all fields. */
    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals(10, time.getHour());
        assertEquals(30, time.getMinute());
        assertEquals(15, time.getSecond());
    }

    /** Verifies that the default constructor initializes to 00:00:00. */
    @Test
    void testDefaultConstructorSetsZeroTime() {
        Time defaultTime = new Time();
        assertEquals(0, defaultTime.getHour());
        assertEquals(0, defaultTime.getMinute());
        assertEquals(0, defaultTime.getSecond());
    }

    /** Tests validation for invalid hour values. */
    @Test
    void testSetHourThrowsExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> time.setHour(-1));
        assertThrows(IllegalArgumentException.class, () -> time.setHour(24));
    }

    /** Tests validation for invalid minute values. */
    @Test
    void testSetMinuteThrowsExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> time.setMinute(-1));
        assertThrows(IllegalArgumentException.class, () -> time.setMinute(60));
    }

    /** Tests validation for invalid second values. */
    @Test
    void testSetSecondThrowsExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> time.setSecond(-5));
        assertThrows(IllegalArgumentException.class, () -> time.setSecond(60));
    }

    /** Verifies that {@code toString()} returns the expected HH:mm:ss format. */
    @Test
    void testToStringReturnsExpectedFormat() {
        assertEquals("10:30:15", time.toString());
    }

    /** Verifies that {@code toSeconds()} converts correctly. */
    @Test
    void testToSecondsCalculatesCorrectly() {
        assertEquals(10 * 3600 + 30 * 60 + 15, time.toSeconds());
    }

    /** Tests addition of seconds within the same day. */
    @Test
    void testAddSecondsWithinSameDay() {
        time.addSeconds(45);
        assertEquals("10:31:00", time.toString());
    }

    /** Tests addition of seconds crossing an hour boundary. */
    @Test
    void testAddSecondsCrossesHourBoundary() {
        time.addSeconds(3600);
        assertEquals("11:30:15", time.toString());
    }

    /** Tests addition that wraps around after midnight. */
    @Test
    void testAddSecondsWrapsAfterMidnight() {
        Time t = new Time(23, 59, 30);
        t.addSeconds(90);
        assertEquals("00:01:00", t.toString());
    }

    /** Tests subtraction of seconds using negative values. */
    @Test
    void testAddSecondsHandlesNegativeValues() {
        Time t = new Time(0, 0, 30);
        t.addSeconds(-90);
        assertEquals("23:59:00", t.toString());
    }

    /** Verifies {@code compareTo()} for equal times. */
    @Test
    void testCompareToEqualTimes() {
        assertEquals(0, time.compareTo(new Time(10, 30, 15)));
    }

    /** Verifies {@code compareTo()} for an earlier time. */
    @Test
    void testCompareToEarlierTime() {
        assertTrue(time.compareTo(new Time(11, 0, 0)) < 0);
    }

    /** Verifies {@code compareTo()} for a later time. */
    @Test
    void testCompareToLaterTime() {
        assertTrue(time.compareTo(new Time(9, 59, 59)) > 0);
    }
}
