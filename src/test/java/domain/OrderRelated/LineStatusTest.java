package domain.OrderRelated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.OrderRelated.LineState;
import pt.ipp.isep.dei.domain.OrderRelated.LineStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link LineStatus}.
 * Ensures correct initialization, getters, setters, and state handling.
 */
class LineStatusTest {

    private LineStatus lineStatus;

    /**
     * Initializes a {@link LineStatus} instance before each test.
     */
    @BeforeEach
    void setUp() {
        lineStatus = new LineStatus("SKU123", 50, LineState.UNDISPATCHABLE);
    }

    /**
     * Verifies that the constructor correctly initializes all fields.
     */
    @Test
    void testConstructorInitialization() {
        assertEquals("SKU123", lineStatus.getSku());
        assertEquals(50, lineStatus.getRequestedQty());
        assertEquals(0, lineStatus.getAllocatedQty());
        assertEquals(LineState.UNDISPATCHABLE, lineStatus.getState());
    }

    /**
     * Verifies that {@link LineStatus#setSku(String)} updates the SKU correctly.
     */
    @Test
    void testSetSku() {
        lineStatus.setSku("SKU999");
        assertEquals("SKU999", lineStatus.getSku());
    }

    /**
     * Verifies that {@link LineStatus#setRequestedQty(int)} updates the requested quantity.
     */
    @Test
    void testSetRequestedQty() {
        lineStatus.setRequestedQty(100);
        assertEquals(100, lineStatus.getRequestedQty());
    }

    /**
     * Verifies that {@link LineStatus#setAllocatedQty(int)} updates the allocated quantity.
     */
    @Test
    void testSetAllocatedQty() {
        lineStatus.setAllocatedQty(30);
        assertEquals(30, lineStatus.getAllocatedQty());
    }

    /**
     * Verifies that {@link LineStatus#setState(LineState)} updates the state correctly.
     */
    @Test
    void testSetState() {
        lineStatus.setState(LineState.ALLOCATED);
        assertEquals(LineState.ALLOCATED, lineStatus.getState());
    }
}
