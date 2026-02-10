package domain.PickingPlanRelated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.pickingPath.PathPoint;
import pt.ipp.isep.dei.domain.pickingPath.PickingPath;
import pt.ipp.isep.dei.domain.pickingPath.PickingPathReport;
import pt.ipp.isep.dei.domain.pickingPath.PickingStrategyType;
import pt.ipp.isep.dei.domain.trolleyRelated.Trolley;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PickingPathReport}.
 */
class PickingPathReportTest {

    private PickingPathReport report;
    private PickingPath path1;
    private PickingPath path2;

    @BeforeEach
    void setUp() {
        PickingPathReport.counter = 0; // Reset ID counter for consistency
        report = new PickingPathReport(PickingStrategyType.STRATEGY_A);

        TrolleyModel model = new TrolleyModel("TM1", 100);
        path1 = new PickingPath(new Trolley(model));
        path2 = new PickingPath(new Trolley(model));

        Box boxA = new Box("B1", "SKU1", 10,
                new Date(1, 1, 2026),
                new Date(25, 10, 2025),
                new Time(8, 30, 0));
        Box boxB = new Box("B2", "SKU2", 5,
                new Date(2, 2, 2027),
                new Date(26, 10, 2025),
                new Time(9, 45, 0));

        path1.addPathPoint(new PathPoint(boxA, 5.0, 1.0, 1.0));
        path1.addPathPoint(new PathPoint(boxB, 3.0, 1.5, 2.0));

        path2.addPathPoint(new PathPoint(boxA, 4.0, 2.0, 2.0));
    }

    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals(PickingStrategyType.STRATEGY_A, report.getPickingStrategyType());
        assertTrue(report.getPickingPaths().isEmpty());
        assertEquals("PP000", report.getId());
    }

    @Test
    void testIdAutoIncrements() {
        PickingPathReport anotherReport = new PickingPathReport(PickingStrategyType.STRATEGY_B);
        assertEquals("PP001", anotherReport.getId());
    }

    @Test
    void testSetAndGetPickingStrategyType() {
        report.setPickingStrategyType(PickingStrategyType.STRATEGY_B);
        assertEquals(PickingStrategyType.STRATEGY_B, report.getPickingStrategyType());
    }

    @Test
    void testAddPickingPathAddsToList() {
        report.addPickingPath(path1);
        report.addPickingPath(path2);
        assertEquals(2, report.getPickingPaths().size());
        assertTrue(report.getPickingPaths().contains(path1));
    }

    @Test
    void testSetAndGetPickingPaths() {
        List<PickingPath> list = new ArrayList<>();
        list.add(path1);
        report.setPickingPaths(list);
        assertEquals(1, report.getPickingPaths().size());
        assertEquals(path1, report.getPickingPaths().get(0));
    }

    @Test
    void testGetTotalDistanceCalculatesCorrectly() {
        report.addPickingPath(path1);
        report.addPickingPath(path2);
        assertEquals(12.0, report.getTotalDistance(), 0.0001);
    }

    @Test
    void testGetTotalDistanceReturnsZeroWhenEmpty() {
        assertEquals(0.0, report.getTotalDistance(), 0.0001);
    }

    @Test
    void testSetIdChangesReportId() {
        report.setId("PP999");
        assertEquals("PP999", report.getId());
    }
}
