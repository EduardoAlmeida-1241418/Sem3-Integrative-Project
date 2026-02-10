package controller.picker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.picker.StrategiesController;
import pt.ipp.isep.dei.domain.Aisle;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.pickingPath.PathPoint;
import pt.ipp.isep.dei.domain.pickingPath.PickingPath;
import pt.ipp.isep.dei.domain.pickingPath.PickingPathReport;
import pt.ipp.isep.dei.domain.pickingPath.PickingStrategyType;
import pt.ipp.isep.dei.domain.trolleyRelated.Trolley;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;
import pt.ipp.isep.dei.data.repository.sprint1.AisleRepository;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the {@link StrategiesController} class.
 */
class StrategiesControllerTest {

    private StrategiesController strategiesController;
    private PickingPathReport reportA;
    private PickingPathReport reportB;
    private Box box;
    private AisleRepository aisleRepository;
    private BayRepository bayRepository;

    /**
     * Sets up the test environment before each test.
     * This method initialises the controller, repositories, and test data.
     */
    @BeforeEach
    void setUp() throws Exception {
        Field instance = Repositories.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        Repositories repositories = Repositories.getInstance();
        aisleRepository = repositories.getAisleRepository();
        bayRepository = repositories.getBayRepository();

        strategiesController = new StrategiesController();
        reportA = new PickingPathReport(PickingStrategyType.STRATEGY_A);
        reportB = new PickingPathReport(PickingStrategyType.STRATEGY_B);

        Aisle aisle = new Aisle("W01A01", "W01");
        aisleRepository.add(aisle);

        Bay bay = new Bay("W01A01", 1, 10);
        bayRepository.add(bay);

        box = new Box("B000001", "1000001", 10, new Date(31, 12, 2025), new Date(26, 10, 2025), new Time(10, 0, 0));
        box.setBayId("W01A01B1");
    }

    /**
     * Tests the {@link StrategiesController#setData(PickingPathReport, PickingPathReport)} method.
     * It verifies that the picking path reports are correctly set in the controller.
     */
    @Test
    void testSetData() {
        strategiesController.setData(reportA, reportB);
        assertEquals(reportA, strategiesController.getPickingPathReportA());
        assertEquals(reportB, strategiesController.getPickingPathReportB());
    }

    /**
     * Tests the {@link StrategiesController#getPickingPathReportA()} method.
     * It verifies that the correct picking path report for Strategy A is returned.
     */
    @Test
    void testGetPickingPathReportA() {
        strategiesController.setPickingPathReportA(reportA);
        assertEquals(reportA, strategiesController.getPickingPathReportA());
    }

    /**
     * Tests the {@link StrategiesController#setPickingPathReportA(PickingPathReport)} method.
     * It verifies that the picking path report for Strategy A is correctly set.
     */
    @Test
    void testSetPickingPathReportA() {
        strategiesController.setPickingPathReportA(reportA);
        assertNotNull(strategiesController.getPickingPathReportA());
        assertEquals(reportA, strategiesController.getPickingPathReportA());
    }

    /**
     * Tests the {@link StrategiesController#getPickingPathReportB()} method.
     * It verifies that the correct picking path report for Strategy B is returned.
     */
    @Test
    void testGetPickingPathReportB() {
        strategiesController.setPickingPathReportB(reportB);
        assertEquals(reportB, strategiesController.getPickingPathReportB());
    }

    /**
     * Tests the {@link StrategiesController#setPickingPathReportB(PickingPathReport)} method.
     * It verifies that the picking path report for Strategy B is correctly set.
     */
    @Test
    void testSetPickingPathReportB() {
        strategiesController.setPickingPathReportB(reportB);
        assertNotNull(strategiesController.getPickingPathReportB());
        assertEquals(reportB, strategiesController.getPickingPathReportB());
    }

    /**
     * Tests the {@link StrategiesController#getAisleNumber(Box)} method.
     * It verifies that the correct aisle number for a given box is returned.
     */
    @Test
    void testGetAisleNumber() {
        String aisleNumber = strategiesController.getAisleNumber(box);
        assertEquals("1", aisleNumber);
    }

    /**
     * Tests the {@link StrategiesController#getBayNumber(Box)} method.
     * It verifies that the correct bay number for a given box is returned.
     */
    @Test
    void testGetBayNumber() {
        String bayNumber = strategiesController.getBayNumber(box);
        assertEquals("1", bayNumber);
    }

    /**
     * Tests the {@link StrategiesController#getTotalDistance(PickingPathReport)} method.
     * It verifies that the total distance for a picking path report is calculated correctly.
     */
    @Test
    void testGetTotalDistance() {
        TrolleyModel trolleyModel = new TrolleyModel("ModelX", 100);
        Trolley trolley = new Trolley(trolleyModel);
        PickingPath path = new PickingPath(trolley);
        List<PathPoint> pathPoints = new ArrayList<>();
        pathPoints.add(new PathPoint(box, 10.0, 1, 1));
        pathPoints.add(new PathPoint(box, 5.5, 1, 2));
        path.setPathPointList(pathPoints);
        reportA.addPickingPath(path);

        double totalDistance = strategiesController.getTotalDistance(reportA);
        assertEquals(15.5, totalDistance);
    }
}
