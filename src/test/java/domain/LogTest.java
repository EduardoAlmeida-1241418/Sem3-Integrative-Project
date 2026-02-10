package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.*;

import javafx.scene.paint.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Log} class.
 * <p>
 * Ensures that the constructor, getters, setters, and {@code toString()} method
 * work correctly, including proper association with {@link RoleType}.
 * </p>
 */
class LogTest {

    private Log log;

    /** Base setup executed before each test. */
    @BeforeEach
    void setUp() {
        log = new Log("System started successfully", LogType.INFO, RoleType.GLOBAL);
    }

    /** Verifies that the constructor correctly initializes all fields. */
    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals("System started successfully", log.getMessage());
        assertEquals(LogType.INFO, log.getLogType());
        assertEquals(RoleType.GLOBAL, log.getRoleType());
        assertEquals(Color.valueOf("#2962FF"), log.getRoleType().getColor());
    }

    /** Tests that setters correctly update field values. */
    @Test
    void testSettersUpdateValues() {
        log.setMessage("Database connection failed");
        log.setLogType(LogType.ERROR);
        log.setRoleType(RoleType.PLANNER1);

        assertEquals("Database connection failed", log.getMessage());
        assertEquals(LogType.ERROR, log.getLogType());
        assertEquals(RoleType.PLANNER1, log.getRoleType());
        assertEquals(Color.valueOf("#8E24AA"), log.getRoleType().getColor());
    }

    /** Ensures that {@code toString()} returns the expected formatted string. */
    @Test
    void testToStringReturnsExpectedFormat() {
        String result = log.toString();
        assertEquals("INFO: System started successfully", result);
    }

    /** Verifies that {@code toString()} reflects updated log values. */
    @Test
    void testToStringUpdatesAfterChanges() {
        log.setMessage("Operation aborted");
        log.setLogType(LogType.WARNING);
        String result = log.toString();
        assertEquals("WARNING: Operation aborted", result);
    }

    /** Ensures that two separate instances with identical data do not share references. */
    @Test
    void testDifferentInstanceSameContent() {
        Log another = new Log("System started successfully", LogType.INFO, RoleType.GLOBAL);

        assertNotSame(log, another);
        assertEquals(log.getMessage(), another.getMessage());
        assertEquals(log.getLogType(), another.getLogType());
        assertEquals(log.getRoleType(), another.getRoleType());
        assertEquals(log.toString(), another.toString());
    }
}
