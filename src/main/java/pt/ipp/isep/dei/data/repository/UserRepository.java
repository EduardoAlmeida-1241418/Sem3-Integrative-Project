package pt.ipp.isep.dei.data.repository;

import pt.ipp.isep.dei.domain.User;

import java.util.*;

/**
 * Repository responsible for managing {@link User} entities.
 * Provides CRUD operations and ensures user uniqueness based on email.
 */
public class UserRepository {

    /** Internal map storing users, indexed by their unique email address. */
    private final Map<String, User> users = new HashMap<>();

    /**
     * Adds a new user to the repository.
     *
     * @param user the user to add
     * @throws IllegalArgumentException if the user or their email is null or empty
     * @throws IllegalStateException if a user with the same email already exists
     */
    public void add(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        String email = user.getEmail();
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty.");
        }
        if (users.containsKey(email)) {
            throw new IllegalStateException("A user with the same email already exists: " + email);
        }
        users.put(email, user);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the user's email
     * @return the corresponding {@link User} object
     * @throws IllegalArgumentException if the email is null or empty
     * @throws NoSuchElementException if no user exists with the given email
     */
    public User findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        User user = users.get(email);
        if (user == null) {
            throw new NoSuchElementException("User not found with email: " + email);
        }
        return user;
    }

    /**
     * Checks whether a user with the given email exists.
     *
     * @param email the user's email
     * @return true if a user exists with the given email, false otherwise
     * @throws IllegalArgumentException if the email is null or empty
     */
    public boolean exists(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        return users.containsKey(email);
    }

    /**
     * Removes a user from the repository by their email address.
     *
     * @param email the email of the user to remove
     * @throws IllegalArgumentException if the email is null or empty
     * @throws NoSuchElementException if no user exists with the given email
     */
    public void remove(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        if (!users.containsKey(email)) {
            throw new NoSuchElementException("User not found with email: " + email);
        }
        users.remove(email);
    }

    /**
     * Retrieves all users stored in the repository.
     *
     * @return an unmodifiable collection of all users
     */
    public Collection<User> findAll() {
        return Collections.unmodifiableCollection(users.values());
    }

    /**
     * Removes all users from the repository.
     */
    public void clear() {
        users.clear();
    }

    /**
     * Counts the total number of users currently stored.
     *
     * @return total count of users
     */
    public int count() {
        return users.size();
    }
}
