package domain.PickingPlanRelated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.pickingPath.PathPoint;
import pt.ipp.isep.dei.domain.pickingPath.PickingPath;
import pt.ipp.isep.dei.domain.trolleyRelated.Trolley;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PickingPath}.
 */
class PickingPathTest {

    private PickingPath pickingPath;
    private PathPoint point1;
    private PathPoint point2;
    private PathPoint point3;

    @BeforeEach
    void setUp() {
        // Criar trolley com modelo
        TrolleyModel model = new TrolleyModel("TM1", 100);
        Trolley trolley = new Trolley(model);
        pickingPath = new PickingPath(trolley);

        // Criar boxes
        Box box1 = new Box("B1", "SKU1", 10,
                new Date(1, 1, 2026),
                new Date(25, 10, 2025),
                new Time(8, 30, 0));
        Box box2 = new Box("B2", "SKU2", 5,
                new Date(2, 2, 2027),
                new Date(26, 10, 2025),
                new Time(9, 45, 0));

        // Criar pontos
        point1 = new PathPoint(box1, 5.0, 1.0, 2.0);
        point2 = new PathPoint(box2, 7.5, 1.5, 3.5);
        point3 = new PathPoint(box1, 2.0, 2.0, 4.0);
    }

    @Test
    void testConstructorInitializesCorrectly() {
        assertNotNull(pickingPath.getTrolley());
        assertTrue(pickingPath.getPathPointList().isEmpty());
    }

    @Test
    void testAddPathPointAddsToList() {
        pickingPath.addPathPoint(point1);
        pickingPath.addPathPoint(point2);
        assertEquals(2, pickingPath.getPathPointList().size());
        assertTrue(pickingPath.getPathPointList().contains(point2));
    }

    @Test
    void testSetAndGetPathPointList() {
        List<PathPoint> list = new ArrayList<>();
        list.add(point1);
        list.add(point3);
        pickingPath.setPathPointList(list);
        assertEquals(2, pickingPath.getPathPointList().size());
        assertEquals(point3, pickingPath.getPathPointList().get(1));
    }

    @Test
    void testGetTotalDistanceReturnsSumOfDistances() {
        pickingPath.addPathPoint(point1);
        pickingPath.addPathPoint(point2);
        pickingPath.addPathPoint(point3);
        assertEquals(14.5, pickingPath.getTotalDistance(), 0.0001);
    }

    @Test
    void testGetTotalDistanceReturnsZeroWhenEmpty() {
        assertEquals(0.0, pickingPath.getTotalDistance(), 0.0001);
    }

}
