package ESINF.sprint2.USEI09;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Tree.KDTree.KD2TreeStation;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the Haversine distance comparator used by the KD-tree.
 *
 * <p>These tests verify that the Haversine distance implementation produces
 * expected results for a known inter-city distance, a zero-distance case and
 * a maximal antipodal distance.
 */
public class HaversineDistanceTest {

    /**
     * Test that the Haversine implementation returns the known distance between
     * Paris (48.8566, 2.3522) and London (51.5073509, -0.1277583).
     */
    @Test
    public void haversineKnownDistance() {
        KD2TreeStation.HaversineDistanceComparator comp = new KD2TreeStation.HaversineDistanceComparator(48.8566, 2.3522);
        double actual = comp.haversine(48.8566, 2.3522, 51.5073509, -0.1277583);
        double expected = 343.5503745611873;
        System.out.println("\nDistância de Paris a Londres:");
        System.out.println("Expected distance (km): " + expected);
        System.out.println("Actual distance   (km): " + actual);
        assertEquals(expected, actual, 1e-3, "Expected exactly=" + expected + " km, but actual=" + actual + " km");
    }

    /**
     * Test that identical coordinates return a zero distance (same-point case).
     */
    @Test
    public void haversineMinDistance() {
        KD2TreeStation.HaversineDistanceComparator comp = new KD2TreeStation.HaversineDistanceComparator(41.1579438, -8.6291053);
        double actual = comp.haversine(41.1579438, -8.6291053, 41.1579438, -8.6291053);
        double expected = 0.0;
        System.out.println("\nDistância do Porto ao Porto:");
        System.out.println("Expected distance (km): " + expected);
        System.out.println("Actual distance   (km): " + actual);
        assertEquals(expected, actual, 1e-6, "Expected exactly=" + expected + " km, but actual=" + actual + " km");
    }

    /**
     * Test that antipodal points (North Pole vs South Pole) produce the maximal
     * great-circle distance expected by the implementation.
     */
    @Test
    public void haversineMaxDistance() {
        KD2TreeStation.HaversineDistanceComparator comp = new KD2TreeStation.HaversineDistanceComparator(90.0, 0.0);
        double actual = comp.haversine(90.0, 0.0, -90.0, 0.0);
        double expected = 20015.114442035923;
        System.out.println("\nDistância do Pólo Norte ao Pólo Sul:");
        System.out.println("Expected distance (km): " + expected);
        System.out.println("Actual distance   (km): " + actual);
        assertEquals(expected, actual, 1e-3, "Expected exactly=" + expected + " km, but actual=" + actual + " km");
    }
}
