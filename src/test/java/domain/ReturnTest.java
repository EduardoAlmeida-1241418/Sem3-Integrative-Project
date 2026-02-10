package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Return} class.
 * <p>
 * Ensures correct functionality of constructors, getters, setters, and {@code toString()}.
 * </p>
 */
class ReturnTest {

    private Return productReturn;
    private Date returnDate;
    private Time returnTime;
    private Date expiryDate;

    /** Base setup executed before each test. */
    @BeforeEach
    void setUp() {
        returnDate = new Date(10, 5, 2025);
        returnTime = new Time(14, 30, 0);
        expiryDate = new Date(1, 1, 2026);

        productReturn = new Return(
                "RET001",
                "SKU123",
                50,
                ReturnReason.EXPIRED,
                returnDate,
                returnTime,
                expiryDate
        );
    }

    /** Verifies that the constructor correctly initializes all fields. */
    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals("RET001", productReturn.getReturnId());
        assertEquals("SKU123", productReturn.getSkuItem());
        assertEquals(50, productReturn.getQuantity());
        assertEquals(ReturnReason.EXPIRED, productReturn.getReturnReason());
        assertEquals(returnDate, productReturn.getDateStamp());
        assertEquals(returnTime, productReturn.getTimeStamp());
        assertEquals(expiryDate, productReturn.getExpiryDate());
    }

    /** Tests that setters correctly update all field values. */
    @Test
    void testSettersUpdateValues() {
        Date newDate = new Date(15, 6, 2025);
        Time newTime = new Time(9, 45, 30);
        Date newExpiry = new Date(31, 12, 2026);

        productReturn.setReturnId("RET999");
        productReturn.setSkuItem("SKU999");
        productReturn.setQuantity(10);
        productReturn.setReturnReason(ReturnReason.DAMAGED);
        productReturn.setDateStamp(newDate);
        productReturn.setTimeStamp(newTime);
        productReturn.setExpiryDate(newExpiry);

        assertEquals("RET999", productReturn.getReturnId());
        assertEquals("SKU999", productReturn.getSkuItem());
        assertEquals(10, productReturn.getQuantity());
        assertEquals(ReturnReason.DAMAGED, productReturn.getReturnReason());
        assertEquals(newDate, productReturn.getDateStamp());
        assertEquals(newTime, productReturn.getTimeStamp());
        assertEquals(newExpiry, productReturn.getExpiryDate());
    }
}
