package repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.LogRepository;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.ui.console.UIUtils;
import pt.ipp.isep.dei.controller.qualityOperator.QuarantineReturnController;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link LogRepository} class.
 */
public class LogRepositoryTest {

    private LogRepository repo;
    private LogRepository globalRepo;

    /**
     * Initializes repositories before each test.
     */
    @BeforeEach
    public void setUp() {
        repo = new LogRepository();
        globalRepo = Repositories.getInstance().getLogRepository();
        globalRepo.clear();
    }

    /**
     * Clears the global repository after each test.
     */
    @AfterEach
    public void tearDown() {
        globalRepo.clear();
    }

    /**
     * Tests adding null logs, duplicate logs, and existence checks.
     * Verifies correct exceptions and repository state.
     */
    @Test
    public void testAddNullAndDuplicateAndExists() {
        Log l1 = new Log("Test message 1", LogType.INFO, RoleType.PICKER);

        try {
            repo.add(null);
            fail("Adding null log must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        repo.add(l1);
        assertTrue(repo.exists(l1), "Log must exist after add");
        assertEquals(1, repo.count(), "Count must reflect added log");

        try {
            repo.add(l1);
            fail("Adding duplicate log must throw IllegalStateException");
        } catch (IllegalStateException e) {
        }

        try {
            repo.exists(null);
            fail("exists(null) should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests findAll returns unmodifiable collection and remove behavior.
     * Verifies correct exceptions and repository state after removals.
     */
    @Test
    public void testFindAllUnmodifiableAndRemoveBehavior() {
        Log l1 = new Log("m1", LogType.INFO, RoleType.PICKER);
        Log l2 = new Log("m2", LogType.WARNING, RoleType.PLANNER1);
        repo.add(l1);
        repo.add(l2);
        Collection<Log> all = repo.findAll();
        assertEquals(2, all.size(), "findAll should return all logs");

        try {
            all.remove(l1);
            fail("findAll must return an unmodifiable collection (remove should throw)");
        } catch (UnsupportedOperationException e) {
        }

        try {
            repo.remove(null);
            fail("remove(null) must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        Log fake = new Log("fake", LogType.INFO, RoleType.GLOBAL);
        try {
            repo.remove(fake);
            fail("Removing non-existing log must throw NoSuchElementException");
        } catch (NoSuchElementException e) {
        }

        repo.remove(l1);
        assertFalse(repo.exists(l1), "Log should not exist after removal");
        assertEquals(1, repo.count(), "Count must reflect removal");
    }

    /**
     * Tests findByType and related exceptions for null and missing types.
     */
    @Test
    public void testFindByType_and_exceptions() {
        Log lInfo = new Log("info", LogType.INFO, RoleType.PICKER);
        Log lWarn = new Log("warning", LogType.WARNING, RoleType.PLANNER1);
        repo.add(lInfo);
        repo.add(lWarn);
        List<Log> infos = repo.findByType(LogType.INFO);
        assertEquals(1, infos.size(), "findByType(INFO) must return one log");
        assertEquals("info", infos.get(0).getMessage(), "Returned log message must match");

        try {
            repo.findByType(null);
            fail("findByType(null) must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            repo.findByType(LogType.ERROR);
            fail("findByType(ERROR) when none exist must throw NoSuchElementException");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Tests clear and count methods, and findByType behavior after clearing.
     */
    @Test
    public void testClearAndCount() {
        repo.add(new Log("a", LogType.INFO, RoleType.PICKER));
        repo.add(new Log("b", LogType.INFO, RoleType.GLOBAL));
        assertEquals(2, repo.count(), "count before clear");
        repo.clear();
        assertEquals(0, repo.count(), "count after clear should be zero");

        try {
            repo.findByType(LogType.INFO);
            fail("findByType on empty repository should throw NoSuchElementException");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Integration test: UIUtils.addLog writes to Repositories singleton.
     * Verifies logs are present and attributes are correct.
     */
    @Test
    public void testUIUtilsAddLog_integration_with_Repositories_singleton() {
        assertEquals(0, globalRepo.count(), "Global repo should be empty at test start");
        UIUtils.addLog("Allocation event", LogType.INFO_ALLOCATION, RoleType.WAREHOUSE_PLANNER);
        UIUtils.addLog("Inspection done", LogType.INSPECTION, RoleType.QUALITY_OPERATOR);
        UIUtils.addLog("Global event", LogType.INFO, RoleType.GLOBAL);
        assertEquals(3, globalRepo.count(), "Global repository must contain logs added via UIUtils");

        boolean foundAllocation = false;
        boolean foundInspection = false;
        boolean foundGlobal = false;
        for (Log l : globalRepo.findAll()) {
            if (l.getMessage() != null && l.getMessage().equals("Allocation event") &&
                    l.getLogType() == LogType.INFO_ALLOCATION &&
                    l.getRoleType() == RoleType.WAREHOUSE_PLANNER) {
                foundAllocation = true;
            }
            if (l.getMessage() != null && l.getMessage().equals("Inspection done") &&
                    l.getLogType() == LogType.INSPECTION &&
                    l.getRoleType() == RoleType.QUALITY_OPERATOR) {
                foundInspection = true;
            }
            if (l.getMessage() != null && l.getMessage().equals("Global event") &&
                    l.getLogType() == LogType.INFO &&
                    l.getRoleType() == RoleType.GLOBAL) {
                foundGlobal = true;
            }
        }

        assertTrue(foundAllocation, "Allocation log must be present in global repository");
        assertTrue(foundInspection, "Inspection log must be present in global repository");
        assertTrue(foundGlobal, "Global log must be present in global repository");
    }

    /**
     * Integration test: QuarantineReturnController.listAuditLines filters by LogType.INSPECTION.
     * Verifies only inspection logs are returned.
     */
    @Test
    public void testQuarantineController_listAuditLines_filtersInspectionType() {
        globalRepo.clear();
        globalRepo.add(new Log("audit1", LogType.INSPECTION, RoleType.QUALITY_OPERATOR));
        globalRepo.add(new Log("audit2", LogType.INFO, RoleType.PLANNER1));
        globalRepo.add(new Log("audit3", LogType.INSPECTION, RoleType.QUALITY_OPERATOR));
        QuarantineReturnController ctrl = new QuarantineReturnController();
        List<String> auditLines = ctrl.listAuditLines();
        assertEquals(2, auditLines.size(), "There should be two inspection audit lines returned");

        boolean hasAudit1 = false;
        boolean hasAudit3 = false;
        boolean hasAudit2 = false;
        for (String msg : auditLines) {
            if ("audit1".equals(msg)) hasAudit1 = true;
            if ("audit3".equals(msg)) hasAudit3 = true;
            if ("audit2".equals(msg)) hasAudit2 = true;
        }

        assertTrue(hasAudit1, "audit1 must be present in audit lines");
        assertTrue(hasAudit3, "audit3 must be present in audit lines");
        assertFalse(hasAudit2, "audit2 (INFO) must not be present in audit lines");
    }
}