package pt.ipp.isep.dei.comparator;

import java.util.Comparator;

/**
 * Comparator implementation for sorting aisle identifiers.
 * <p>
 * The comparison is based on the warehouse and aisle numbers extracted from the string.
 * Expected format: a string containing 'W' and 'A' (e.g., "W1A5").
 */
public class AisleOrderComparator implements Comparator<String> {

    /**
     * Compares two aisle identifiers by their warehouse and aisle numbers.
     *
     * @param aisleId1 the first aisle identifier to compare
     * @param aisleId2 the second aisle identifier to compare
     * @return a negative integer, zero, or a positive integer if the first aisle
     *         should come before, is equal to, or after the second aisle
     * @throws NumberFormatException if the numeric parts of the identifiers cannot be parsed
     */
    @Override
    public int compare(String aisleId1, String aisleId2) {
        String[] parts1 = aisleId1.split("W|A");
        String[] parts2 = aisleId2.split("W|A");

        int cmp = parts1[1].compareTo(parts2[1]);
        if (cmp != 0) return cmp;

        int aisle1 = Integer.parseInt(parts1[2]);
        int aisle2 = Integer.parseInt(parts2[2]);
        return Integer.compare(aisle1, aisle2);
    }
}
