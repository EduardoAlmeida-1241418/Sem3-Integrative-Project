package controller.admin;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.admin.chooseUserController;
import pt.ipp.isep.dei.domain.User;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for chooseUserController.
 */
public class ChooseUserControllerTest {

    private chooseUserController controller;
    private UserRepository userRepository;

    /**
     * Sets up the test environment before each test execution.
     * Initializes the UserRepository and clears its contents.
     * Instantiates the chooseUserController.
     */
    @BeforeEach
    void setUp() {
        userRepository = Repositories.getInstance().getUserRepository();
        userRepository.clear();
        controller = new chooseUserController();
    }

    /**
     * Tests that getUserInfo() returns all users added to the repository.
     * Verifies that the returned list contains the expected users by their email addresses.
     */
    @Test
    void testGetUserInfo_returnsAllUsers() {
        User u1 = new User(1, "a@test.com", "p1");
        User u2 = new User(2, "b@test.com", "p2");
        userRepository.add(u1);
        userRepository.add(u2);

        ObservableList<User> users = controller.getUserInfo();
        assertNotNull(users);
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("a@test.com")));
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("b@test.com")));
    }
}