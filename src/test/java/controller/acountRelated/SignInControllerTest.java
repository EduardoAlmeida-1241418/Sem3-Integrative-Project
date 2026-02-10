package controller.acountRelated;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.acountRelated.SignInController;
import pt.ipp.isep.dei.domain.User;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SignInController.
 */
public class SignInControllerTest {

    private SignInController controller;
    private UserRepository userRepository;

    /**
     * Sets up the test environment before each test execution.
     * Initializes the controller and user repository, and clears the repository.
     */
    @BeforeEach
    void setUp() {
        userRepository = Repositories.getInstance().getUserRepository();
        userRepository.clear();
        controller = new SignInController();
    }

    /**
     * Tests account existence, password verification, role retrieval, and account activation status.
     * Also checks behavior for non-existent accounts and roles.
     */
    @Test
    void testAccountExists_and_verifyPassword_and_roles_and_active() {
        User u = new User(1, "test@x.com", "S3cret!");
        ArrayList<String> roles = new ArrayList<>();
        roles.add("Picker");
        u.setRoleList(roles);
        u.setActive(true);
        userRepository.add(u);
        assertTrue(userRepository.exists("test@x.com"));
        assertTrue(controller.accountExists("test@x.com"));
        assertFalse(controller.accountExists("no@such.com"));
        assertTrue(controller.verifyPassword("test@x.com", "S3cret!"));
        assertFalse(controller.verifyPassword("test@x.com", "wrong"));
        ObservableList<String> userRoles = controller.getRoles("test@x.com");
        assertNotNull(userRoles);
        assertEquals(1, userRoles.size());
        assertEquals("Picker", userRoles.get(0));
        assertEquals("/fxml/picker/pickerHomePage.fxml", controller.getSignPath("Picker"));
        assertEquals("/fxml/visitorMenu/homePage.fxml", controller.getSignPath("UnknownRole"));
        assertTrue(controller.verifyIfAccountIsActive("test@x.com"));
        boolean inactiveResult = false;
        try {
            inactiveResult = controller.verifyIfAccountIsActive("no@such.com");
        } catch (NoSuchElementException e) {
            inactiveResult = false;
        }
        assertFalse(inactiveResult);
    }

    /**
     * Tests role retrieval for a non-existent user and a user with no roles assigned.
     * Ensures the returned list is empty in both cases.
     */
    @Test
    void testGetRoles_userNotExistsOrNoRoles() {
        ObservableList<String> roles = null;
        try {
            roles = controller.getRoles("no@user.com");
        } catch (NoSuchElementException e) {
            roles = javafx.collections.FXCollections.observableArrayList();
        }
        assertNotNull(roles);
        assertTrue(roles.isEmpty());

        User u = new User(2, "noroles@x.com", "pass");
        userRepository.add(u);
        ObservableList<String> roles2 = controller.getRoles("noroles@x.com");
        assertNotNull(roles2);
        assertTrue(roles2.isEmpty());
    }

    /**
     * Tests the FXML path returned for all possible roles and for an unknown role.
     */
    @Test
    void testGetSignPath_allRoles() {
        assertEquals("/fxml/admin/adminHomePage.fxml", controller.getSignPath("Admin"));
        assertEquals("/fxml/picker/pickerHomePage.fxml", controller.getSignPath("Picker"));
        assertEquals("/fxml/planner/plannerHomePage.fxml", controller.getSignPath("Planner"));
        assertEquals("/fxml/stationStorageManager/stationStorageManagerHomePage.fxml", controller.getSignPath("Station Storage Manager"));
        assertEquals("/fxml/terminalOperator/terminalOperatorHomePage.fxml", controller.getSignPath("Terminal Operator"));
        assertEquals("/fxml/trafficDispatcher/trafficDispatcherHomePage.fxml", controller.getSignPath("Traffic Dispatcher"));
        assertEquals("/fxml/warehousePlanner/warehousePlannerHomePage.fxml", controller.getSignPath("Warehouse Planner"));
        assertEquals("/fxml/qualityOperator/qualityOperatorHomePage.fxml", controller.getSignPath("Quality Operator"));
        assertEquals("/fxml/visitorMenu/homePage.fxml", controller.getSignPath("UnknownRole"));
    }

    /**
     * Tests account activation status for an inactive user.
     */
    @Test
    void testVerifyIfAccountIsActive_inactiveUser() {
        User u = new User(3, "inactive@x.com", "pass");
        u.setActive(false);
        userRepository.add(u);
        assertFalse(controller.verifyIfAccountIsActive("inactive@x.com"));
    }
}