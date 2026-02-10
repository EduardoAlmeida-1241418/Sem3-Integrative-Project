package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Owner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Owner} class.
 * <p>
 * Ensures that constructors, getters, and setters function correctly.
 * </p>
 */
class OwnerTest {

    private Owner owner;

    /** Base setup executed before each test. */
    @BeforeEach
    void setUp() {
        owner = new Owner("Infraestruturas de Portugal", "IP", "PT500111222");
    }

    /** Verifies that the constructor correctly initializes all fields. */
    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals("Infraestruturas de Portugal", owner.getName());
        assertEquals("IP", owner.getShortName());
        assertEquals("PT500111222", owner.getVatNumber());
    }

    /** Tests that setters correctly update field values. */
    @Test
    void testSettersUpdateValues() {
        owner.setName("Comboios de Portugal");
        owner.setShortName("CP");
        owner.setVatNumber("PT500333444");

        assertEquals("Comboios de Portugal", owner.getName());
        assertEquals("CP", owner.getShortName());
        assertEquals("PT500333444", owner.getVatNumber());
    }

    /** Ensures getters return consistent values after multiple updates. */
    @Test
    void testGettersReturnExpectedValuesAfterMultipleChanges() {
        owner.setName("Medway Transportes");
        owner.setShortName("MEDWAY");
        owner.setVatNumber("PT509876321");

        assertEquals("Medway Transportes", owner.getName());
        assertEquals("MEDWAY", owner.getShortName());
        assertEquals("PT509876321", owner.getVatNumber());
    }

    /** Ensures different instances with identical data do not share references. */
    @Test
    void testDifferentInstancesWithSameDataAreIndependent() {
        Owner another = new Owner("Infraestruturas de Portugal", "IP", "PT500111222");

        assertNotSame(owner, another);
        assertEquals(owner.getName(), another.getName());
        assertEquals(owner.getShortName(), another.getShortName());
        assertEquals(owner.getVatNumber(), another.getVatNumber());
    }
}
