package LAPR.SPRINT3;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.Tree.Interval.IntervalTree;
import pt.ipp.isep.dei.domain.schedule.ScheduleEvent;
import pt.ipp.isep.dei.domain.schedule.ScheduleEventType;
import pt.ipp.isep.dei.domain.trackRelated.TrackLocation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntervalTree_Tests {

    /* =====================================================
       Helpers
       ===================================================== */

    private TimeInterval ti(
            int h1, int m1,
            int h2, int m2
    ) {
        return new TimeInterval(
                new DateTime(new Date(1, 1, 2025), new Time(h1, m1, 0)),
                new DateTime(new Date(1, 1, 2025), new Time(h2, m2, 0))
        );
    }

    private TrackLocation tl(int id) {
        return new Facility(id, "F" + id);
    }

    private ScheduleEvent ev(TimeInterval ti) {
        return new ScheduleEvent(
                ti,
                tl(1),
                tl(2),
                null,
                ScheduleEventType.values()[0]
        );
    }

    /* =====================================================
       Tests
       ===================================================== */

    @Test
    void emptyTree_hasNoOverlaps() {
        System.out.println("\n[Test] emptyTree_hasNoOverlaps");

        IntervalTree tree = new IntervalTree();

        assertTrue(tree.isEmpty());
        assertFalse(tree.hasOverlap(ti(10, 0, 11, 0)));
        assertNull(tree.findAnyOverlap(ti(10, 0, 11, 0)));
    }

    @Test
    void singleInsert_detectsOverlap() {
        System.out.println("\n[Test] singleInsert_detectsOverlap");

        IntervalTree tree = new IntervalTree();

        ScheduleEvent e1 = ev(ti(10, 0, 11, 0));
        tree.insert(e1);

        assertFalse(tree.isEmpty());
        assertTrue(tree.hasOverlap(ti(10, 30, 10, 45)));
        assertEquals(e1, tree.findAnyOverlap(ti(10, 30, 10, 45)));
    }

    @Test
    void nonOverlappingIntervals_returnNoOverlap() {
        System.out.println("\n[Test] nonOverlappingIntervals_returnNoOverlap");

        IntervalTree tree = new IntervalTree();

        tree.insert(ev(ti(8, 0, 9, 0)));
        tree.insert(ev(ti(10, 0, 11, 0)));

        assertFalse(tree.hasOverlap(ti(9, 1, 9, 59)));
    }

    @Test
    void overlappingEvents_areBothReturned() {
        System.out.println("\n[Test] overlappingEvents_areBothReturned");

        IntervalTree tree = new IntervalTree();

        ScheduleEvent e1 = ev(ti(10, 0, 11, 0));
        ScheduleEvent e2 = ev(ti(10, 30, 11, 30));

        tree.insert(e1);
        tree.insert(e2);

        List<ScheduleEvent> overlaps =
                tree.findAllOverlaps(ti(10, 45, 10, 50));

        assertEquals(2, overlaps.size());
        assertTrue(overlaps.contains(e1));
        assertTrue(overlaps.contains(e2));
    }

    @Test
    void removeEvent_eliminatesOverlap() {
        System.out.println("\n[Test] removeEvent_eliminatesOverlap");

        IntervalTree tree = new IntervalTree();

        ScheduleEvent e1 = ev(ti(14, 0, 15, 0));
        tree.insert(e1);

        assertTrue(tree.hasOverlap(ti(14, 30, 14, 45)));

        tree.remove(e1);

        assertTrue(tree.isEmpty());
        assertFalse(tree.hasOverlap(ti(14, 30, 14, 45)));
    }

    @Test
    void intervalCrossingMidnight_isHandledCorrectly() {
        System.out.println("\n[Test] intervalCrossingMidnight_isHandledCorrectly");

        IntervalTree tree = new IntervalTree();

        TimeInterval overnight = new TimeInterval(
                new DateTime(new Date(1, 1, 2025), new Time(23, 0, 0)),
                new DateTime(new Date(2, 1, 2025), new Time(1, 0, 0))
        );

        ScheduleEvent e = ev(overnight);
        tree.insert(e);

        TimeInterval query = new TimeInterval(
                new DateTime(new Date(2, 1, 2025), new Time(0, 30, 0)),
                new DateTime(new Date(2, 1, 2025), new Time(0, 45, 0))
        );

        assertTrue(tree.hasOverlap(query));
        assertEquals(e, tree.findAnyOverlap(query));
    }
}
