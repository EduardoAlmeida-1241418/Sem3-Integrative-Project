package controller.picker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.picker.PickingPathController;
import pt.ipp.isep.dei.domain.Aisle;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.pickingPath.PickingPathReport;
import pt.ipp.isep.dei.domain.pickingPlanRelated.PickingPlan;
import pt.ipp.isep.dei.domain.trolleyRelated.Trolley;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyAllocation;
import pt.ipp.isep.dei.domain.trolleyRelated.TrolleyModel;
import pt.ipp.isep.dei.data.repository.sprint1.AisleRepository;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.sprint1.PickingPlansRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PickingPathController.
 */
public class PickingPathControllerTest {
    private PickingPathController controller;
    private PickingPlansRepository pickingPlansRepository;
    private BayRepository bayRepository;
    private AisleRepository aisleRepository;
    private Bay bay;
    private Aisle aisle;

    /**
     * Sets up the test environment before each test. Adds minimal domain objects to repositories.
     */
    @BeforeEach
    void setUp() {
        controller = new PickingPathController();
        pickingPlansRepository = Repositories.getInstance().getPickingPlansRepository();
        bayRepository = Repositories.getInstance().getBayRepository();
        aisleRepository = Repositories.getInstance().getAisleRepository();
        pickingPlansRepository.clear();
        bayRepository.clear();
        aisleRepository.clear();
        aisle = new Aisle("W1A1", "W1");
        aisleRepository.add(aisle);
        bay = new Bay("W1A1", 1, 10);
        bayRepository.add(bay);
    }

    /**
     * Tests getPickingPlanList returns all picking plans in the repository.
     */
    @Test
    void testGetPickingPlanList() {
        TrolleyModel model = new TrolleyModel("TROLLEY1", 10);
        PickingPlan plan = new PickingPlan(model);
        pickingPlansRepository.add(plan);
        List<PickingPlan> plans = controller.getPickingPlanList();
        assertEquals(1, plans.size());
        assertSame(plan, plans.get(0));
    }

    /**
     * Tests pickingPathReport_StratAGeneral generates a PickingPathReport for a plan with one trolley and allocation.
     */
    @Test
    void testPickingPathReportStratAGeneral() {
        TrolleyModel model = new TrolleyModel("TROLLEY1", 10);
        PickingPlan plan = new PickingPlan(model);
        Trolley trolley = new Trolley(model);
        Date expiryDate = new Date(25, 10, 2025);
        Date receivedDate = new Date(24, 10, 2025);
        Time receivedTime = new Time(12, 0, 0);
        Box box = new Box("BOX1", "SKU1", 5, expiryDate, receivedDate, receivedTime);

        box.setBayId(bay.getBayId());

        TrolleyAllocation allocation = new TrolleyAllocation(box, 5, null, 1.0);
        trolley.getTrolleyAllocations().add(allocation);
        plan.getTrolleys().add(trolley);

        PickingPathReport report = controller.pickingPathReport_StratAGeneral(plan);
        assertNotNull(report);
        assertEquals(1, report.getPickingPaths().size());
    }

    /**
     * Tests pickingPathReport_StratBGeneral generates a PickingPathReport for a plan with one trolley and allocation.
     */
    @Test
    void testPickingPathReportStratBGeneral() {
        TrolleyModel model = new TrolleyModel("TROLLEY1", 10);
        PickingPlan plan = new PickingPlan(model);
        Trolley trolley = new Trolley(model);
        Date expiryDate = new Date(25, 10, 2025);
        Date receivedDate = new Date(24, 10, 2025);
        Time receivedTime = new Time(12, 0, 0);
        Box box = new Box("BOX1", "SKU1", 5, expiryDate, receivedDate, receivedTime);

        box.setBayId(bay.getBayId());

        TrolleyAllocation allocation = new TrolleyAllocation(box, 5, null, 1.0);
        trolley.getTrolleyAllocations().add(allocation);
        plan.getTrolleys().add(trolley);

        PickingPathReport report = controller.pickingPathReport_StratBGeneral(plan);
        assertNotNull(report);
        assertEquals(1, report.getPickingPaths().size());
    }
}