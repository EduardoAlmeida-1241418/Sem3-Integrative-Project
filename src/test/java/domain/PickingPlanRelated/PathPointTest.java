package domain.PickingPlanRelated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.pickingPath.PathPoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PathPoint}.
 * Validates creation, box management, and string representation.
 */
class PathPointTest {

    private Box box1;
    private Box box2;
    private PathPoint pathPoint;

    @BeforeEach
    void setUp() {
        box1 = new Box("B1", "SKU1", 10,
                new Date(1, 1, 2026),
                new Date(25, 10, 2025),
                new Time(8, 30, 0));
        box2 = new Box("B2", "SKU2", 5,
                new Date(2, 2, 2027),
                new Date(26, 10, 2025),
                new Time(9, 45, 0));

        pathPoint = new PathPoint(box1, 12.5, 3.0, 4.0);
    }

    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals(1, pathPoint.getBoxList().size());
        assertEquals(box1, pathPoint.getBoxList().get(0));
        assertEquals(12.5, pathPoint.getDistanceFromPrevious());
    }

    @Test
    void testAddBoxAddsToList() {
        pathPoint.addBox(box2);
        assertEquals(2, pathPoint.getBoxList().size());
        assertTrue(pathPoint.getBoxList().contains(box2));
    }

    @Test
    void testSetAndGetBoxListReplacesContent() {
        List<Box> newList = new ArrayList<>();
        newList.add(box2);
        pathPoint.setBoxList(newList);

        assertEquals(1, pathPoint.getBoxList().size());
        assertEquals("B2", pathPoint.getBoxList().get(0).getBoxId());
    }

    @Test
    void testSetAndGetDistanceFromPrevious() {
        pathPoint.setDistanceFromPrevious(20.0);
        assertEquals(20.0, pathPoint.getDistanceFromPrevious());
    }

    @Test
    void testToStringContainsBoxIdsAndCoordinates() {
        pathPoint.addBox(box2);
        String result = pathPoint.toString();

        assertTrue(result.contains("B1"));
        assertTrue(result.contains("B2"));
        assertTrue(result.contains("distanceFromPrevious=12.5"));
        assertTrue(result.contains("aisleCoord=3.0"));
        assertTrue(result.contains("bayCoord=4.0"));
    }

    @Test
    void testToStringWithSingleBoxFormattedCorrectly() {
        String result = pathPoint.toString();
        assertTrue(result.startsWith("PathPoint{boxes=[B1"));
        assertTrue(result.endsWith("bayCoord=4.0}"));
    }
}
