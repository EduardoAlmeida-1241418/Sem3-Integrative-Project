package domain.OrderRelated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for verifying the correct ordering of boxes inside bays according to business rules.
 * <p>
 * Ordering criteria:
 * <ol>
 *   <li>expiryDate (earliest first, null last)</li>
 *   <li>receivedAt (oldest first, including time)</li>
 *   <li>boxId (alphanumeric ascending)</li>
 * </ol>
 */
public class BayOrderingTest {
    /**
     * Clears the box and bay repositories before each test to ensure isolation.
     */
    @BeforeEach
    public void setUp() {
        Repositories.getInstance().getBoxRepository().clear();
        Repositories.getInstance().getBayRepository().clear();
    }

    /**
     * Verifies that boxes inserted into a bay are ordered by expiryDate, then receivedAt, then boxId.
     * <ul>
     *   <li>Group 1: Same expiry (2025-05-01) - ordered by receivedAt and boxId</li>
     *   <li>Group 2: Later expiry (2025-06-01) - comes after group 1</li>
     *   <li>Group 3: Null expiry - must be last</li>
     * </ul>
     */
    @Test
    public void boxesInsertedIntoBay_areOrderedByExpiryThenReceivedThenId_basicScenario() {
        Box x02 = new Box("BX0002", "SKU0001", 5, Date.fromString("2025-05-01","yyyy-MM-dd"), Date.fromString("2025-04-01","yyyy-MM-dd"), new Time(8,0,0));
        Box x03 = new Box("BX0003", "SKU0001", 5, Date.fromString("2025-05-01","yyyy-MM-dd"), Date.fromString("2025-04-01","yyyy-MM-dd"), new Time(8,0,0)); // same receivedAt -> tie by id
        Box x01 = new Box("BX0001", "SKU0001", 5, Date.fromString("2025-05-01","yyyy-MM-dd"), Date.fromString("2025-04-02","yyyy-MM-dd"), new Time(9,0,0)); // received later

        Box y01 = new Box("BXY001", "SKU0001", 5, Date.fromString("2025-06-01","yyyy-MM-dd"), Date.fromString("2025-03-01","yyyy-MM-dd"), new Time(7,0,0));

        Box zNull = new Box("BXZ001", "SKU0001", 5, null, Date.fromString("2025-01-01","yyyy-MM-dd"), new Time(6,0,0));

        Repositories.getInstance().getBoxRepository().add(x01);
        Repositories.getInstance().getBoxRepository().add(x02);
        Repositories.getInstance().getBoxRepository().add(x03);
        Repositories.getInstance().getBoxRepository().add(y01);
        Repositories.getInstance().getBoxRepository().add(zNull);

        Bay bay = new Bay("W1A1", 1, 10);
        assertTrue(bay.addBox(y01.getBoxId(), RoleType.PICKER));
        assertTrue(bay.addBox(zNull.getBoxId(), RoleType.PICKER));
        assertTrue(bay.addBox(x01.getBoxId(), RoleType.PICKER));
        assertTrue(bay.addBox(x03.getBoxId(), RoleType.PICKER));
        assertTrue(bay.addBox(x02.getBoxId(), RoleType.PICKER));

        List<String> ordered = new ArrayList<>(bay.getBoxIds());

        assertEquals("BX0002", ordered.get(0), "First should be BX0002 (same expiry, earliest received / lowest id)");
        assertEquals("BX0003", ordered.get(1), "Second should be BX0003 (same expiry and receivedAt as BX0002 but id after BX0002)");
        assertEquals("BX0001", ordered.get(2), "Third should be BX0001 (same expiry but received later)");
        assertEquals("BXY001", ordered.get(3), "Fourth should be BXY001 (later expiry)");
        assertEquals("BXZ001", ordered.get(4), "Fifth should be BXZ001 (null expiry must be last)");
    }

    /**
     * Verifies that when boxes have the same receivedAt date, ordering is done by time and then by boxId.
     */
    @Test
    public void whenReceivedSameDate_differentTimes_areOrderedByTime() {
        Box a = new Box("BA0001", "SKU0004", 1, Date.fromString("2025-07-01","yyyy-MM-dd"), Date.fromString("2025-06-01","yyyy-MM-dd"), new Time(6,0,0));
        Box b = new Box("BB0001", "SKU0004", 1, Date.fromString("2025-07-01","yyyy-MM-dd"), Date.fromString("2025-06-01","yyyy-MM-dd"), new Time(8,30,0));
        Box c = new Box("BC0001", "SKU0004", 1, Date.fromString("2025-07-01","yyyy-MM-dd"), Date.fromString("2025-06-01","yyyy-MM-dd"), new Time(8,30,0)); // tie with B -> id decides

        Repositories.getInstance().getBoxRepository().add(a);
        Repositories.getInstance().getBoxRepository().add(b);
        Repositories.getInstance().getBoxRepository().add(c);

        Bay bay = new Bay("W2B1", 1, 10);
        assertTrue(bay.addBox(c.getBoxId(), RoleType.PICKER));
        assertTrue(bay.addBox(b.getBoxId(), RoleType.PICKER));
        assertTrue(bay.addBox(a.getBoxId(), RoleType.PICKER));

        List<String> ordered = new ArrayList<>(bay.getBoxIds());
        assertEquals("BA0001", ordered.get(0));
        assertEquals("BB0001", ordered.get(1));
        assertEquals("BC0001", ordered.get(2));
    }

    /**
     * Verifies that multiple boxes with null expiry are ordered by receivedAt and then by boxId.
     */
    @Test
    public void multipleNullExpiries_areOrderedByReceivedThenId() {
        Box n1 = new Box("BN0001", "SKU0005", 1, null, Date.fromString("2025-02-01","yyyy-MM-dd"), new Time(9,0,0));
        Box n2 = new Box("BN0002", "SKU0005", 1, null, Date.fromString("2025-01-01","yyyy-MM-dd"), new Time(9,0,0)); // received earlier -> comes before
        Box nonNull = new Box("BM0001", "SKU0005", 1, Date.fromString("2025-04-01","yyyy-MM-dd"), Date.fromString("2025-03-01","yyyy-MM-dd"), new Time(9,0,0));

        Repositories.getInstance().getBoxRepository().add(n1);
        Repositories.getInstance().getBoxRepository().add(n2);
        Repositories.getInstance().getBoxRepository().add(nonNull);

        Bay bay = new Bay("W3C1", 1, 10);
        assertTrue(bay.addBox(n1.getBoxId(), RoleType.PICKER));
        assertTrue(bay.addBox(nonNull.getBoxId(), RoleType.PICKER));
        assertTrue(bay.addBox(n2.getBoxId(), RoleType.PICKER));

        List<String> ordered = new ArrayList<>(bay.getBoxIds());
        assertEquals("BM0001", ordered.get(0));
        assertEquals("BN0002", ordered.get(1));
        assertEquals("BN0001", ordered.get(2));
    }

    /**
     * Verifies that boxId ordering handles numeric suffixes consistently (alphanumeric sort).
     */
    @Test
    public void boxIdOrdering_handlesNumericSuffixes_consistently() {
        Box x2 = new Box("BX0002", "SKU0006", 1, Date.fromString("2025-08-01","yyyy-MM-dd"), Date.fromString("2025-07-01","yyyy-MM-dd"), new Time(9,0,0));
        Box x10 = new Box("BX0010", "SKU0006", 1, Date.fromString("2025-08-01","yyyy-MM-dd"), Date.fromString("2025-07-01","yyyy-MM-dd"), new Time(9,0,0));

        Repositories.getInstance().getBoxRepository().add(x2);
        Repositories.getInstance().getBoxRepository().add(x10);

        Bay bay = new Bay("W4D1", 1, 10);
        assertTrue(bay.addBox(x10.getBoxId(), RoleType.PICKER));
        assertTrue(bay.addBox(x2.getBoxId(), RoleType.PICKER));

        List<String> ordered = new ArrayList<>(bay.getBoxIds());
        assertEquals("BX0002", ordered.get(0));
        assertEquals("BX0010", ordered.get(1));
    }
}