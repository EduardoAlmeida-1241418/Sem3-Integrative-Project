package pt.ipp.isep.dei.domain;

import java.util.ArrayList;

/**
 * Represents a system user with authentication credentials, roles, and active status.
 * Provides basic logic for password validation and role-based checks.
 */
public class User {

    /** User's email address, used as a unique identifier for login. */
    private String email;

    /** User's password for authentication. */
    private String password;

    /** Unique numeric identifier for the user. */
    private int id;

    /** List of roles assigned to the user. */
    private ArrayList<String> roleList;

    /** Indicates whether the user account is active. */
    private boolean active;

    /**
     * Constructs a User with an ID, email, and password.
     * The user is initialized as inactive by default.
     *
     * @param id unique identifier of the user
     * @param email user's email address
     * @param password user's password
     */
    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.active = false;
    }

    /**
     * Constructs a User with an email, password, and assigned roles.
     *
     * @param email user's email address
     * @param password user's password
     * @param roleList list of roles assigned to the user
     */
    public User(String email, String password, ArrayList<String> roleList) {
        this.email = email;
        this.password = password;
        this.roleList = roleList;
    }

    /**
     * Verifies if a given password matches the user's stored password.
     *
     * @param pass password to verify
     * @return true if the password matches, false otherwise
     */
    public boolean correctPassword(String pass) {
        return pass.equals(password);
    }

    /** @return the user's email address */
    public String getEmail() {
        return email;
    }

    /** @param email sets the user's email address */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @return the user's password */
    public String getPassword() {
        return password;
    }

    /** @param password sets the user's password */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @return list of roles assigned to the user */
    public ArrayList<String> getRoleList() {
        return roleList;
    }

    /**
     * Sets the list of roles assigned to the user.
     * If the list is not empty, the user becomes active.
     *
     * @param roleList list of roles to assign
     */
    public void setRoleList(ArrayList<String> roleList) {
        if (!roleList.isEmpty()) {
            active = true;
        }
        this.roleList = roleList;
    }

    /** @return the user's ID */
    public int getId() {
        return id;
    }

    /** @param id sets the user's ID */
    public void setId(int id) {
        this.id = id;
    }

    /** @return true if the user account is active, false otherwise */
    public boolean isActive() {
        return active;
    }

    /** @param active sets the user's active status */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Checks if the user has the "Admin" role.
     *
     * @return true if the user is an administrator, false otherwise
     */
    public boolean isAdmin() {
        for (String role : getRoleList()) {
            if (role.equals("Admin")) {
                return true;
            }
        }
        return false;
    }
}
