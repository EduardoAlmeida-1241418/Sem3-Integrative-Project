package LAPR.SPRINT3;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.algorithms.BellmanFordAlgorithm;
import pt.ipp.isep.dei.controller.algorithms.LAPR.LAPRShortestPathAlgorithm;
import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLineSegment;
import pt.ipp.isep.dei.domain.trackRelated.Siding;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LAPRShortestPathAlgorithm_Tests {

    @Test
    void verifySimplePath() {
        System.out.println("\n[Test] verifySimplePath");

        Facility a = new Facility(1, "A", true, null);
        Facility b = new Facility(2, "B", true, null);

        RailwayLineSegment seg = new RailwayLineSegment(1, false, 100, 10, 1, 100, 1);
        RailwayLine line = new RailwayLine(1, "A-B", 1, 2, "VAT");
        line.addSegment(seg, 1);

        LAPRShortestPathAlgorithm alg = new LAPRShortestPathAlgorithm();
        List<Facility> path = alg.shortestPath(a, b, List.of(a, b), List.of(line));

        System.out.println("Path found: " + path);

        assertEquals(List.of(a, b), path);
    }

    @Test
    void verifyShortestPathChosen() {
        System.out.println("\n[Test] verifyShortestPathChosen");

        Facility a = new Facility(1, "A", true, null);
        Facility b = new Facility(2, "B", true, null);
        Facility c = new Facility(3, "C", true, null);

        RailwayLineSegment ab = new RailwayLineSegment(1, false, 100, 50, 1, 100, 1);
        RailwayLineSegment ac = new RailwayLineSegment(2, false, 100, 10, 1, 100, 1);
        RailwayLineSegment cb = new RailwayLineSegment(3, false, 100, 10, 1, 100, 1);

        RailwayLine lineAB = new RailwayLine(1, "A-B", 1, 2, "VAT");
        lineAB.addSegment(ab, 1);
        RailwayLine lineAC = new RailwayLine(2, "A-C", 1, 3, "VAT");
        lineAC.addSegment(ac, 1);
        RailwayLine lineCB = new RailwayLine(3, "C-B", 3, 2, "VAT");
        lineCB.addSegment(cb, 1);

        LAPRShortestPathAlgorithm alg = new LAPRShortestPathAlgorithm();
        List<Facility> path = alg.shortestPath(a, b, List.of(a, b, c), List.of(lineAB, lineAC, lineCB));

        System.out.println("Path found: " + path);

        assertEquals(List.of(a, c, b), path);
    }

    @Test
    void verifyDoubleTrackIsPreferred() {
        System.out.println("\n[Test] verifyDoubleTrackIsPreferred");

        Facility a = new Facility(1, "A", true, null);
        Facility b = new Facility(2, "B", true, null);

        RailwayLineSegment singleTrack = new RailwayLineSegment(1, false, 100, 10, 1, 100, 1);
        RailwayLineSegment doubleTrack = new RailwayLineSegment(2, false, 100, 12, 2, 100, 1);

        RailwayLine line1 = new RailwayLine(1, "Single", 1, 2, "VAT");
        line1.addSegment(singleTrack, 1);
        RailwayLine line2 = new RailwayLine(2, "Double", 1, 2, "VAT");
        line2.addSegment(doubleTrack, 1);

        LAPRShortestPathAlgorithm alg = new LAPRShortestPathAlgorithm();
        List<Facility> path = alg.shortestPath(a, b, List.of(a, b), List.of(line1, line2));

        System.out.println("Path found: " + path);
        System.out.println("Expected preference: Double track");

        assertEquals(List.of(a, b), path);
    }

    @Test
    void verifySidingBoostApplied() {
        System.out.println("\n[Test] verifySidingBoostApplied");

        Facility a = new Facility(1, "A", true, null);
        Facility b = new Facility(2, "B", true, null);

        Siding siding = new Siding(1, 5, 2);
        RailwayLineSegment withSiding = new RailwayLineSegment(1, false, 100, 10, 1, 100, 1, siding);
        RailwayLineSegment noSiding = new RailwayLineSegment(2, false, 100, 9, 1, 100, 1);

        RailwayLine line1 = new RailwayLine(1, "WithSiding", 1, 2, "VAT");
        line1.addSegment(withSiding, 1);
        RailwayLine line2 = new RailwayLine(2, "NoSiding", 1, 2, "VAT");
        line2.addSegment(noSiding, 1);

        LAPRShortestPathAlgorithm alg = new LAPRShortestPathAlgorithm();
        List<Facility> path = alg.shortestPath(a, b, List.of(a, b), List.of(line1, line2));

        System.out.println("Path found: " + path);
        System.out.println("Expected preference: Segment with siding");

        assertEquals(List.of(a, b), path);
    }

    @Test
    void verifyNoPathReturnsEmptyList() {
        System.out.println("\n[Test] verifyNoPathReturnsEmptyList");

        Facility a = new Facility(1, "A", true, null);
        Facility b = new Facility(2, "B", true, null);
        Facility c = new Facility(3, "C", true, null);

        RailwayLine line = new RailwayLine(1, "A-C", 1, 3, "VAT");
        line.addSegment(new RailwayLineSegment(1, false, 100, 10, 1, 100, 1), 1);

        LAPRShortestPathAlgorithm alg = new LAPRShortestPathAlgorithm();
        List<Facility> path = alg.shortestPath(a, b, List.of(a, b, c), List.of(line));

        System.out.println("Path found: " + path);
        System.out.println("Expected: empty path");

        assertTrue(path.isEmpty());
    }

    @Test
    void verifyDenseNetworkChoosesOptimalPath() {
        System.out.println("\n[Test] verifyVeryDenseNetworkChoosesGloballyOptimalPath");

        Facility a = new Facility(1, "A", true, null);
        Facility b = new Facility(2, "B", true, null);
        Facility c = new Facility(3, "C", true, null);
        Facility d = new Facility(4, "D", true, null);
        Facility e = new Facility(5, "E", true, null);
        Facility f = new Facility(6, "F", true, null);
        Facility g = new Facility(7, "G", true, null);

        RailwayLineSegment ab = new RailwayLineSegment(1, false, 100, 25, 1, 100, 1);
        RailwayLineSegment bc = new RailwayLineSegment(2, false, 100, 25, 1, 100, 1);
        RailwayLineSegment cd = new RailwayLineSegment(3, false, 100, 25, 1, 100, 1);
        RailwayLineSegment de = new RailwayLineSegment(4, false, 100, 25, 1, 100, 1);
        RailwayLineSegment ef = new RailwayLineSegment(5, false, 100, 25, 1, 100, 1);
        RailwayLineSegment fg = new RailwayLineSegment(6, false, 100, 25, 1, 100, 1);

        RailwayLineSegment ac = new RailwayLineSegment(7, false, 100, 18, 2, 100, 1);
        RailwayLineSegment ce = new RailwayLineSegment(8, false, 100, 18, 2, 100, 1);
        RailwayLineSegment eg = new RailwayLineSegment(9, false, 100, 18, 2, 100, 1);

        RailwayLineSegment bd = new RailwayLineSegment(10, false, 100, 15, 1, 100, 1, new Siding(1, 6, 3));
        RailwayLineSegment df = new RailwayLineSegment(11, false, 100, 15, 1, 100, 1, new Siding(2, 7, 3));

        RailwayLineSegment ag = new RailwayLineSegment(12, false, 100, 90, 1, 100, 1);
        RailwayLineSegment cf = new RailwayLineSegment(13, false, 100, 40, 1, 100, 1);

        RailwayLine lineAB = new RailwayLine(1, "A-B", 1, 2, "VAT");
        lineAB.addSegment(ab, 1);

        RailwayLine lineBC = new RailwayLine(2, "B-C", 2, 3, "VAT");
        lineBC.addSegment(bc, 1);

        RailwayLine lineCD = new RailwayLine(3, "C-D", 3, 4, "VAT");
        lineCD.addSegment(cd, 1);

        RailwayLine lineDE = new RailwayLine(4, "D-E", 4, 5, "VAT");
        lineDE.addSegment(de, 1);

        RailwayLine lineEF = new RailwayLine(5, "E-F", 5, 6, "VAT");
        lineEF.addSegment(ef, 1);

        RailwayLine lineFG = new RailwayLine(6, "F-G", 6, 7, "VAT");
        lineFG.addSegment(fg, 1);

        RailwayLine lineAC = new RailwayLine(7, "A-C", 1, 3, "VAT");
        lineAC.addSegment(ac, 1);

        RailwayLine lineCE = new RailwayLine(8, "C-E", 3, 5, "VAT");
        lineCE.addSegment(ce, 1);

        RailwayLine lineEG = new RailwayLine(9, "E-G", 5, 7, "VAT");
        lineEG.addSegment(eg, 1);

        RailwayLine lineBD = new RailwayLine(10, "B-D", 2, 4, "VAT");
        lineBD.addSegment(bd, 1);

        RailwayLine lineDF = new RailwayLine(11, "D-F", 4, 6, "VAT");
        lineDF.addSegment(df, 1);

        RailwayLine lineAG = new RailwayLine(12, "A-G", 1, 7, "VAT");
        lineAG.addSegment(ag, 1);

        RailwayLine lineCF = new RailwayLine(13, "C-F", 3, 6, "VAT");
        lineCF.addSegment(cf, 1);

        LAPRShortestPathAlgorithm alg = new LAPRShortestPathAlgorithm();
        List<Facility> path = alg.shortestPath(a, g, List.of(a, b, c, d, e, f, g), List.of(lineAB, lineBC, lineCD, lineDE, lineEF, lineFG, lineAC, lineCE, lineEG, lineBD, lineDF, lineAG, lineCF));

        System.out.println("Path found: " + path);
        System.out.println("Expected optimal path: A -> C -> E -> G");

        assertEquals(List.of(a, c, e, g), path);
    }

    @Test
    void verifyLineWithMultipleSegmentsIsCorrectlyEvaluated() {
        System.out.println("\n[Test] verifyLineWithMultipleSegmentsIsCorrectlyEvaluated");

        Facility a = new Facility(1, "A", true, null);
        Facility b = new Facility(2, "B", true, null);
        Facility c = new Facility(3, "C", true, null);
        Facility d = new Facility(4, "D", true, null);
        Facility e = new Facility(5, "E", true, null);
        Facility f = new Facility(6, "F", true, null);

        RailwayLineSegment ab1 = new RailwayLineSegment(1, false, 100, 12, 1, 100, 1);
        RailwayLineSegment ab2 = new RailwayLineSegment(2, false, 100, 10, 2, 100, 1);
        RailwayLineSegment ab3 = new RailwayLineSegment(3, false, 100, 8, 1, 100, 1, new Siding(1, 4, 2));

        RailwayLineSegment bc1 = new RailwayLineSegment(4, false, 100, 20, 1, 100, 1);
        RailwayLineSegment bc2 = new RailwayLineSegment(5, false, 100, 20, 1, 100, 1);

        RailwayLineSegment cd = new RailwayLineSegment(6, false, 100, 18, 2, 100, 1);

        RailwayLineSegment de1 = new RailwayLineSegment(7, false, 100, 15, 1, 100, 1);
        RailwayLineSegment de2 = new RailwayLineSegment(8, false, 100, 15, 1, 100, 1);

        RailwayLineSegment af = new RailwayLineSegment(9, false, 100, 80, 1, 100, 1);
        RailwayLineSegment fe = new RailwayLineSegment(10, false, 100, 35, 1, 100, 1);

        RailwayLine lineAB = new RailwayLine(1, "A-B", 1, 2, "VAT");
        lineAB.addSegment(ab1, 1);
        lineAB.addSegment(ab2, 2);
        lineAB.addSegment(ab3, 3);

        RailwayLine lineBC = new RailwayLine(2, "B-C", 2, 3, "VAT");
        lineBC.addSegment(bc1, 1);
        lineBC.addSegment(bc2, 2);

        RailwayLine lineCD = new RailwayLine(3, "C-D", 3, 4, "VAT");
        lineCD.addSegment(cd, 1);

        RailwayLine lineDE = new RailwayLine(4, "D-E", 4, 5, "VAT");
        lineDE.addSegment(de1, 1);
        lineDE.addSegment(de2, 2);

        RailwayLine lineAF = new RailwayLine(5, "A-F", 1, 6, "VAT");
        lineAF.addSegment(af, 1);

        RailwayLine lineFE = new RailwayLine(6, "F-E", 6, 5, "VAT");
        lineFE.addSegment(fe, 1);

        LAPRShortestPathAlgorithm alg = new LAPRShortestPathAlgorithm();
        List<Facility> path = alg.shortestPath(a, e, List.of(a, b, c, d, e, f), List.of(lineAB, lineBC, lineCD, lineDE, lineAF, lineFE));

        System.out.println("Path found: " + path);
        System.out.println("Expected optimal path: A -> B -> C -> D -> E");

        assertEquals(List.of(a, b, c, d, e), path);
    }

    @Test
    void verifyTwistedGraphChoosesOptimalPath() {
        System.out.println("\n[Test] verifyTwistedGraphChoosesOptimalPath");

        Facility a = new Facility(1, "A", true, null);
        Facility b = new Facility(2, "B", true, null);
        Facility c = new Facility(3, "C", true, null);
        Facility d = new Facility(4, "D", true, null);
        Facility e = new Facility(5, "E", true, null);
        Facility f = new Facility(6, "F", true, null);
        Facility g = new Facility(7, "G", true, null);
        Facility h = new Facility(8, "H", true, null);

        RailwayLineSegment ab = new RailwayLineSegment(1, false, 100, 20, 1, 100, 1);
        RailwayLineSegment ac = new RailwayLineSegment(2, false, 100, 18, 2, 100, 1);
        RailwayLineSegment ah = new RailwayLineSegment(3, false, 100, 25, 1, 100, 1);

        RailwayLineSegment bc = new RailwayLineSegment(4, false, 100, 20, 1, 100, 1);
        RailwayLineSegment bd = new RailwayLineSegment(5, false, 100, 15, 1, 100, 1, new Siding(1, 6, 3));

        RailwayLineSegment cd = new RailwayLineSegment(6, false, 100, 12, 2, 100, 1);
        RailwayLineSegment ce = new RailwayLineSegment(7, false, 100, 15, 2, 100, 1);

        RailwayLineSegment de = new RailwayLineSegment(8, false, 100, 30, 1, 100, 1);
        RailwayLineSegment df = new RailwayLineSegment(9, false, 100, 14, 1, 100, 1, new Siding(2, 7, 3));
        RailwayLineSegment dh = new RailwayLineSegment(10, false, 100, 10, 1, 100, 1);

        RailwayLineSegment ef = new RailwayLineSegment(11, false, 100, 12, 1, 100, 1);
        RailwayLineSegment eg = new RailwayLineSegment(12, false, 100, 20, 1, 100, 1);

        RailwayLineSegment fg = new RailwayLineSegment(13, false, 100, 10, 2, 100, 1);

        RailwayLine lineAB = new RailwayLine(1, "A-B", 1, 2, "VAT");
        lineAB.addSegment(ab, 1);

        RailwayLine lineAC = new RailwayLine(2, "A-C", 1, 3, "VAT");
        lineAC.addSegment(ac, 1);

        RailwayLine lineAH = new RailwayLine(3, "A-H", 1, 8, "VAT");
        lineAH.addSegment(ah, 1);

        RailwayLine lineBC = new RailwayLine(4, "B-C", 2, 3, "VAT");
        lineBC.addSegment(bc, 1);

        RailwayLine lineBD = new RailwayLine(5, "B-D", 2, 4, "VAT");
        lineBD.addSegment(bd, 1);

        RailwayLine lineCD = new RailwayLine(6, "C-D", 3, 4, "VAT");
        lineCD.addSegment(cd, 1);

        RailwayLine lineCE = new RailwayLine(7, "C-E", 3, 5, "VAT");
        lineCE.addSegment(ce, 1);

        RailwayLine lineDE = new RailwayLine(8, "D-E", 4, 5, "VAT");
        lineDE.addSegment(de, 1);

        RailwayLine lineDF = new RailwayLine(9, "D-F", 4, 6, "VAT");
        lineDF.addSegment(df, 1);

        RailwayLine lineDH = new RailwayLine(10, "D-H", 4, 8, "VAT");
        lineDH.addSegment(dh, 1);

        RailwayLine lineEF = new RailwayLine(11, "E-F", 5, 6, "VAT");
        lineEF.addSegment(ef, 1);

        RailwayLine lineEG = new RailwayLine(12, "E-G", 5, 7, "VAT");
        lineEG.addSegment(eg, 1);

        RailwayLine lineFG = new RailwayLine(13, "F-G", 6, 7, "VAT");
        lineFG.addSegment(fg, 1);

        LAPRShortestPathAlgorithm alg = new LAPRShortestPathAlgorithm();
        List<Facility> path = alg.shortestPath(a, g, List.of(a, b, c, d, e, f, g, h), List.of(lineAB, lineAC, lineAH, lineBC, lineBD, lineCD, lineCE, lineDE, lineDF, lineDH, lineEF, lineEG, lineFG));

        System.out.println("Path found: " + path);
        System.out.println("Expected optimal path: A -> C -> D -> F -> G");

        assertEquals(List.of(a, c, d, f, g), path);
    }
}
