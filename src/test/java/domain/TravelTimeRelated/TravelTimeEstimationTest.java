package domain.TravelTimeRelated;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for travel time estimation between two directly connected stations using a selected locomotive.
 * Acceptance Criteria:
 *  - Locomotive speed used in estimation.
 *  - Constant speed (instant acceleration).
 *  - Output lists sections in order with calculated travel time.
 *
 * Improvements:
 *  - Added validation for locomotive speed <= 0 (expects IllegalArgumentException).
 *  - Uses Locale-independent checks and numeric assertions for times.
 *  - Adds explanatory assertion messages.
 */
public class TravelTimeEstimationTest {

    static class Locomotive {
        String locoId;
        double speedKmh; // km/h
        Locomotive(String locoId, double speedKmh) {
            this.locoId = locoId;
            this.speedKmh = speedKmh;
        }
    }

    static class Section {
        String sectionId;
        String fromStation;
        String toStation;
        double lengthKm;
        Section(String sectionId, String fromStation, String toStation, double lengthKm) {
            this.sectionId = sectionId;
            this.fromStation = fromStation;
            this.toStation = toStation;
            this.lengthKm = lengthKm;
        }
    }

    /**
     * Estimates travel times for each section using the selected locomotive.
     * <p>
     * The locomotive's speed is used for calculation. Acceleration is assumed instantaneous (constant speed).
     * Throws IllegalArgumentException if locomotive is null or speed is zero/negative.
     *
     * @param sections List of sections to traverse, in order
     * @param locomotive The locomotive to use for the calculation
     * @return List of formatted strings for each section, showing sectionId, stations, and travel time in minutes
     */
    static List<String> estimateTravelTimes(List<Section> sections, Locomotive locomotive) {
        if (locomotive == null) throw new IllegalArgumentException("Locomotive cannot be null");
        if (locomotive.speedKmh <= 0) throw new IllegalArgumentException("Locomotive speed must be > 0");
        List<String> result = new ArrayList<>();
        for (Section s : sections) {
            double timeHours = s.lengthKm / locomotive.speedKmh;
            double timeMinutes = timeHours * 60.0;
            String line = String.format(Locale.US, "%s: %s-%s, %.2f min", s.sectionId, s.fromStation, s.toStation, timeMinutes);
            result.add(line);
        }
        return result;
    }

    /**
     * Test: Estimates travel time for multiple sections and a locomotive.
     * <p>
     * Verifies that the output contains the correct travel times for each section, in order.
     */
    @Test
    public void testTravelTimeEstimation_multipleSections() {
        Locomotive loco = new Locomotive("LOCO01", 80.0); // 80 km/h
        List<Section> route = Arrays.asList(
                new Section("SEC01", "StationA", "StationB", 40.0), // 30 min
                new Section("SEC02", "StationB", "StationC", 60.0)  // 45 min
        );
        List<String> output = estimateTravelTimes(route, loco);
        assertEquals(2, output.size(), "Expected two section outputs");
        assertEquals("SEC01: StationA-StationB, 30.00 min", output.get(0), "First section time mismatch");
        assertEquals("SEC02: StationB-StationC, 45.00 min", output.get(1), "Second section time mismatch");
    }

    /**
     * Test: Estimates travel time for a single section and a fast locomotive.
     * <p>
     * Verifies that the output contains the correct travel time for the section.
     */
    @Test
    public void testSingleSectionFastLocomotive() {
        Locomotive loco = new Locomotive("LOCO02", 120.0); // 120 km/h
        List<Section> route = Collections.singletonList(
                new Section("SEC03", "StationX", "StationY", 90.0)
        );
        List<String> output = estimateTravelTimes(route, loco);
        assertEquals(1, output.size());
        assertEquals("SEC03: StationX-StationY, 45.00 min", output.get(0), "Single section with fast loco mismatch");
    }

    /**
     * Test: Estimates travel time for a section of zero length.
     * <p>
     * Verifies that the output is zero minutes for the section.
     */
    @Test
    public void testZeroLengthSection() {
        Locomotive loco = new Locomotive("LOCO03", 100.0);
        List<Section> route = Collections.singletonList(
                new Section("SEC04", "StationZ", "StationW", 0.0)
        );
        List<String> output = estimateTravelTimes(route, loco);
        assertEquals(1, output.size());
        assertEquals("SEC04: StationZ-StationW, 0.00 min", output.get(0), "Zero length should yield 0.00 min");
    }

    /**
     * Test: Verifies that zero or negative locomotive speed throws an exception.
     * <p>
     * Ensures that invalid locomotive speeds are rejected.
     */
    @Test
    public void testZeroOrNegativeSpeed_throws() {
        Locomotive locoZero = new Locomotive("LOCO_ZERO", 0.0);
        Locomotive locoNeg = new Locomotive("LOCO_NEG", -10.0);
        List<Section> route = Collections.singletonList(new Section("S", "A", "B", 10.0));
        assertThrows(IllegalArgumentException.class, () -> estimateTravelTimes(route, locoZero),
                "Locomotive with zero speed must be rejected");
        assertThrows(IllegalArgumentException.class, () -> estimateTravelTimes(route, locoNeg),
                "Locomotive with negative speed must be rejected");
    }

    /**
     * Test: Verifies numeric precision of travel time calculation.
     * <p>
     * Parses the output and checks that the numeric value matches the expected calculation rounded to two decimals.
     */
    @Test
    public void testPrecision_numericComparison() {
        Locomotive loco = new Locomotive("LOCO04", 90.0); // 90 km/h => 40 km => 26.666... min
        List<Section> route = Collections.singletonList(new Section("SEC05", "A", "B", 40.0));
        List<String> output = estimateTravelTimes(route, loco);
        assertEquals(1, output.size());
        // Parse numeric minutes from the formatted string and assert with delta
        String s = output.get(0);
        String minutesPart = s.substring(s.lastIndexOf(",") + 1).trim().replace(" min", "");
        double minutes = Double.parseDouble(minutesPart);
        double expectedRounded = Math.round((40.0/90.0)*60.0 * 100.0) / 100.0;
        assertEquals(expectedRounded, minutes, 1e-6, "Numeric minutes must match calculation rounded to two decimals");
    }
}