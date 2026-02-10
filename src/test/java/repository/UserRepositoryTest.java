package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.User;
import pt.ipp.isep.dei.data.repository.UserRepository;

import java.util.Collection;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UserRepository} class.
 */
public class UserRepositoryTest {
    private UserRepository repository;
    private User userAdm;
    private User userM;
    private User userA;
    private User userB;

    /**
     * Initialise repository and sample users from userData.csv before each test.
     */
    @BeforeEach
    public void setUp() {
        repository = new UserRepository();
        userAdm = new User(1, "adm", "a");
        userM = new User(2, "m", "123");
        userA = new User(3, "a", "a");
        userB = new User(4, "b", "123");
    }

    /**
     * Tests adding a new user and verifies it is stored correctly.
     */
    @Test
    public void testAddUser() {
        repository.add(userAdm);
        assertTrue(repository.exists("adm"));
        assertEquals(userAdm, repository.findByEmail("adm"));
    }

    /**
     * Tests that adding a null user throws IllegalArgumentException.
     */
    @Test
    public void testAddNullThrows() {
        try {
            repository.add(null);
            fail("Should throw IllegalArgumentException when adding null");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests that adding a user with null or empty email throws IllegalArgumentException.
     */
    @Test
    public void testAddUserWithNullOrEmptyEmailThrows() {
        User nullEmail = new User(5, null, "pass");
        try {
            repository.add(nullEmail);
            fail("Should throw IllegalArgumentException for null email");
        } catch (IllegalArgumentException e) {
            // expected
        }
        User emptyEmail = new User(6, "", "pass");
        try {
            repository.add(emptyEmail);
            fail("Should throw IllegalArgumentException for empty email");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests that adding a duplicate user (same email) throws IllegalStateException.
     */
    @Test
    public void testAddDuplicateThrows() {
        repository.add(userAdm);
        User duplicate = new User(99, "adm", "otherpass");
        try {
            repository.add(duplicate);
            fail("Should throw IllegalStateException when adding duplicate");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    /**
     * Tests finding a user by email returns the correct object.
     */
    @Test
    public void testFindByEmailReturnsUser() {
        repository.add(userM);
        User found = repository.findByEmail("m");
        assertEquals(userM, found);
    }

    /**
     * Tests that findByEmail returns the same instance that was added.
     */
    @Test
    public void testFindByEmailReturnsSameInstance() {
        repository.add(userAdm);
        User found = repository.findByEmail("adm");
        assertSame(userAdm, found);
    }

    /**
     * Tests that finding by null or empty email throws IllegalArgumentException.
     */
    @Test
    public void testFindByEmailThrowsIfInvalid() {
        try {
            repository.findByEmail(null);
            fail("Should throw IllegalArgumentException for null email");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            repository.findByEmail("");
            fail("Should throw IllegalArgumentException for empty email");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests that finding by email throws NoSuchElementException if not found.
     */
    @Test
    public void testFindByEmailThrowsIfNotFound() {
        try {
            repository.findByEmail("notfound");
            fail("Should throw NoSuchElementException for unknown email");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    /**
     * Tests existence check for a user by email and that null/empty input throws.
     */
    @Test
    public void testExistsUserAndNullBehaviour() {
        repository.add(userA);
        assertTrue(repository.exists("a"));
        assertFalse(repository.exists("b"));
        try {
            repository.exists(null);
            fail("Should throw IllegalArgumentException for null email");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            repository.exists("");
            fail("Should throw IllegalArgumentException for empty email");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests removing a user by email works and user is no longer present.
     */
    @Test
    public void testRemoveUser() {
        repository.add(userB);
        repository.remove("b");
        assertFalse(repository.exists("b"));
    }

    /**
     * Tests that removing by null or empty email throws IllegalArgumentException.
     */
    @Test
    public void testRemoveThrowsIfInvalid() {
        try {
            repository.remove(null);
            fail("Should throw IllegalArgumentException for null email");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            repository.remove("");
            fail("Should throw IllegalArgumentException for empty email");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests that removing by email throws NoSuchElementException if not found.
     */
    @Test
    public void testRemoveThrowsIfNotFound() {
        try {
            repository.remove("notfound");
            fail("Should throw NoSuchElementException for unknown email");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    /**
     * Tests retrieving all users returns the correct collection and is unmodifiable.
     */
    @Test
    public void testFindAllReturnsUsersAndUnmodifiable() {
        repository.add(userAdm);
        repository.add(userM);
        Collection<User> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(userAdm));
        assertTrue(all.contains(userM));
        try {
            all.clear();
            fail("findAll() must return an unmodifiable collection");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    /**
     * Tests explicit count() behaviour (increment/decrement) for the repository.
     */
    @Test
    public void testCountUsers() {
        assertEquals(0, repository.count());
        repository.add(userAdm);
        assertEquals(1, repository.count());
        repository.add(userM);
        assertEquals(2, repository.count());
        repository.remove("adm");
        assertEquals(1, repository.count());
    }

    /**
     * Tests clearing the repository removes all users.
     */
    @Test
    public void testClearRemovesAllUsers() {
        repository.add(userAdm);
        repository.add(userA);
        repository.clear();
        assertTrue(repository.findAll().isEmpty());
    }
}