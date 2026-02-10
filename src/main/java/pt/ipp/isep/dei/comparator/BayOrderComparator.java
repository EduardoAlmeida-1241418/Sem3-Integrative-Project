package pt.ipp.isep.dei.comparator;

import java.util.Comparator;

/**
 * Comparator for bay identifiers.
 * <p>
 * Compares bay strings based on warehouse, aisle, and bay numbers extracted from the ID.
 * Expected format: a string containing 'A' and 'B' (e.g., "W1A2B3").
 */
public class BayOrderComparator implements Comparator<String> {

    /**
     * Compares two bay identifiers.
     * <p>
     * The comparison follows this order:
     * <ol>
     *   <li>Warehouse ID (lexicographical)</li>
     *   <li>Aisle number (numerical)</li>
     *   <li>Bay number (numerical)</li>
     * </ol>
     *
     * @param bayId1 the first bay identifier
     * @param bayId2 the second bay identifier
     * @return a negative integer, zero, or a positive integer if the first identifier
     *         is less than, equal to, or greater than the second identifier
     * @throws NumberFormatException if numeric parts cannot be parsed
     */
    @Override
    public int compare(String bayId1, String bayId2) {
        String[] parts1 = bayId1.split("A|B");
        String[] parts2 = bayId2.split("A|B");

        // Compare warehouse ID (strings)
        int cmp = parts1[0].compareTo(parts2[0]);
        if (cmp != 0) return cmp;

        // Compare aisle numbers
        int aisle1 = Integer.parseInt(parts1[1]);
        int aisle2 = Integer.parseInt(parts2[1]);
        cmp = Integer.compare(aisle1, aisle2);
        if (cmp != 0) return cmp;

        // Compare bay numbers
        int bay1 = Integer.parseInt(parts1[2]);
        int bay2 = Integer.parseInt(parts2[2]);
        return Integer.compare(bay1, bay2);
    }
}
