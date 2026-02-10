package pt.ipp.isep.dei.comparator;

import java.util.Comparator;
import java.util.Map;

/**
 * Comparator for facilities based on their distances.
 * <p>
 * Compares facility identifiers by their corresponding distance values from a provided map.
 */
public class FacilityDistanceComparator implements Comparator<String> {

    /** Map storing facility identifiers and their associated distance values. */
    private Map<String, Double> distances;

    /**
     * Constructs a comparator with a given map of facility distances.
     *
     * @param distances a map containing facility identifiers as keys and their distances as values
     */
    public FacilityDistanceComparator(Map<String, Double> distances) {
        this.distances = distances;
    }

    /**
     * Compares two facility identifiers based on their distances.
     *
     * @param f1 the first facility identifier
     * @param f2 the second facility identifier
     * @return a negative integer, zero, or a positive integer if the first facility
     *         is closer, at the same distance, or farther than the second facility
     * @throws NullPointerException if any of the identifiers are not present in the map
     */
    @Override
    public int compare(String f1, String f2) {
        return Double.compare(distances.get(f1), distances.get(f2));
    }
}
