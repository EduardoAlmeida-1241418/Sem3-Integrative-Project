package comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.comparator.AlphaNumericComparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AlphaNumericComparator.
 * This class contains test cases to verify the correct behavior of alphanumeric string comparison.
 * It tests various scenarios including null values, simple strings, alphanumeric combinations,
 * case sensitivity, and special characters.
 */
class AlphaNumericComparatorTest {

    private AlphaNumericComparator comparator;

    /**
     * Sets up the test environment before each test.
     * Initializes a new instance of AlphaNumericComparator.
     */
    @BeforeEach
    void setUp() {
        comparator = new AlphaNumericComparator();
    }

    /**
     * Tests the comparison behavior with null values.
     * Verifies that:
     * - Two null values are considered equal
     * - A non-null value is considered greater than null
     * - A null value is considered less than a non-null value
     */
    @Test
    void compareNullValues() {
        assertEquals(0, comparator.compare(null, null), "Both null values should be equal");
        assertTrue(comparator.compare("A", null) > 0, "Non-null should be greater than null");
        assertTrue(comparator.compare(null, "A") < 0, "Null should be less than non-null");
    }

    /**
     * Tests the comparison of simple string values.
     * Verifies basic string comparison behavior:
     * - Equal strings return 0
     * - Correct ordering of different strings
     */
    @Test
    void compareSimpleStrings() {
        assertEquals(0, comparator.compare("A", "A"), "Equal strings should return 0");
        assertTrue(comparator.compare("A", "B") < 0, "A should be less than B");
        assertTrue(comparator.compare("B", "A") > 0, "B should be greater than A");
    }

    /**
     * Tests the comparison of alphanumeric strings.
     * Verifies that numerical parts are compared correctly:
     * - "A2" comes before "A10" (natural number ordering)
     * - Equal alphanumeric strings are considered equal
     */
    @Test
    void compareAlphanumericStrings() {
        assertTrue(comparator.compare("A2", "A10") < 0, "A2 should come before A10");
        assertTrue(comparator.compare("A10", "A2") > 0, "A10 should come after A2");
        assertEquals(0, comparator.compare("A10", "A10"), "Equal alphanumeric strings should be equal");
    }

    /**
     * Tests the comparison behavior with mixed case strings.
     * Verifies case sensitivity in string comparison:
     * - Lowercase letters come after uppercase letters
     */
    @Test
    void compareMixedCases() {
        assertTrue(comparator.compare("a1", "A1") > 0, "Lowercase should come after uppercase");
        assertTrue(comparator.compare("A1", "a1") < 0, "Uppercase should come before lowercase");
    }

    /**
     * Tests the comparison of strings containing large numbers.
     * Verifies correct ordering of strings with multi-digit numbers:
     * - Natural number ordering is maintained for large numbers
     */
    @Test
    void compareLargeNumbers() {
        assertTrue(comparator.compare("X999", "X1000") < 0, "X999 should come before X1000");
        assertTrue(comparator.compare("X1000", "X999") > 0, "X1000 should come after X999");
    }

    /**
     * Tests the comparison of strings containing multiple numbers.
     * Verifies correct ordering when strings contain multiple numeric parts:
     * - Numbers are compared naturally at each position
     */
    @Test
    void compareStringsWithMultipleNumbers() {
        assertTrue(comparator.compare("A1B2", "A1B10") < 0, "A1B2 should come before A1B10");
        assertTrue(comparator.compare("A2B1", "A10B1") < 0, "A2B1 should come before A10B1");
        assertEquals(0, comparator.compare("A1B2C3", "A1B2C3"), "Identical strings should be equal");
    }

    /**
     * Tests the comparison of strings with different lengths.
     * Verifies that:
     * - Shorter strings come before longer strings when the prefix matches
     */
    @Test
    void compareStringsDifferentLengths() {
        assertTrue(comparator.compare("A1", "A1B") < 0, "Shorter string should come before longer string");
        assertTrue(comparator.compare("A1B", "A1") > 0, "Longer string should come after shorter string");
    }

    /**
     * Tests the comparison of strings containing special characters.
     * Verifies correct ordering when strings contain non-alphanumeric characters:
     * - Special characters are compared based on their ASCII values
     */
    @Test
    void compareWithSpecialCharacters() {
        assertTrue(comparator.compare("A-1", "A-2") < 0, "A-1 should come before A-2");
        assertTrue(comparator.compare("A#1", "A#2") < 0, "A#1 should come before A#2");
    }
}