package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.User;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link User} class.
 * <p>
 * Ensures correct behavior of constructors, authentication,
 * role management, and active state handling.
 * </p>
 */
class UserTest {

    private User user;
    private ArrayList<String> roles;

    /** Base setup executed before each test. */
    @BeforeEach
    void setUp() {
        roles = new ArrayList<>();
        roles.add("User");
        roles.add("Admin");

        user = new User(1, "john.doe@example.com", "12345");
    }

    /** Verifies that the constructor with ID initializes correctly and sets the user as inactive. */
    @Test
    void testConstructorWithIdInitializesCorrectly() {
        assertEquals(1, user.getId());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("12345", user.getPassword());
        assertFalse(user.isActive());
    }

    /** Verifies that the constructor with roles initializes correctly. */
    @Test
    void testConstructorWithRolesInitializesCorrectly() {
        User userWithRoles = new User("alice@example.com", "pass", roles);
        assertEquals("alice@example.com", userWithRoles.getEmail());
        assertEquals("pass", userWithRoles.getPassword());
        assertEquals(roles, userWithRoles.getRoleList());
    }

    /** Tests that {@code correctPassword()} correctly validates the password. */
    @Test
    void testCorrectPasswordReturnsTrueWhenMatch() {
        assertTrue(user.correctPassword("12345"));
        assertFalse(user.correctPassword("wrongpass"));
    }

    /** Tests that getters and setters work correctly. */
    @Test
    void testSettersAndGettersWorkCorrectly() {
        user.setId(99);
        user.setEmail("new.email@test.com");
        user.setPassword("newpass");

        assertEquals(99, user.getId());
        assertEquals("new.email@test.com", user.getEmail());
        assertEquals("newpass", user.getPassword());
    }

    /** Tests that setting a non-empty role list activates the user. */
    @Test
    void testSetRoleListActivatesUserWhenNotEmpty() {
        user.setRoleList(roles);
        assertTrue(user.isActive());
        assertEquals(roles, user.getRoleList());
    }

    /** Tests that setting an empty role list does not activate the user. */
    @Test
    void testSetRoleListDoesNotActivateWhenEmpty() {
        ArrayList<String> emptyRoles = new ArrayList<>();
        user.setRoleList(emptyRoles);
        assertFalse(user.isActive());
        assertEquals(emptyRoles, user.getRoleList());
    }

    /** Tests that {@code isAdmin()} returns true only when the "Admin" role exists. */
    @Test
    void testIsAdminReturnsTrueWhenAdminRolePresent() {
        user.setRoleList(roles);
        assertTrue(user.isAdmin());
    }

    /** Tests that {@code isAdmin()} returns false when the "Admin" role is not present. */
    @Test
    void testIsAdminReturnsFalseWhenNoAdminRole() {
        ArrayList<String> nonAdminRoles = new ArrayList<>();
        nonAdminRoles.add("User");
        user.setRoleList(nonAdminRoles);

        assertFalse(user.isAdmin());
    }

    /** Tests {@code setActive()} and {@code isActive()}. */
    @Test
    void testSetActiveManually() {
        user.setActive(true);
        assertTrue(user.isActive());
        user.setActive(false);
        assertFalse(user.isActive());
    }
}
