package pt.ipp.isep.dei.controller.admin;

import javafx.scene.control.CheckBox;
import pt.ipp.isep.dei.domain.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for editing user roles and managing user data persistence.
 * <p>
 * Handles loading user information, updating role assignments via checkboxes,
 * and writing changes to the user CSV file.
 */
public class AdminEditUserController {

    /** Path to the user data CSV file. */
    private final String USER_DATA = "data/userData.csv";

    /** The user currently being edited. */
    private User user;

    /**
     * Default constructor.
     */
    public AdminEditUserController() {}

    /**
     * Loads user information for editing.
     * <p>
     * Ensures that the user's role list is initialized to prevent null references.
     *
     * @param user the user to load
     */
    public void loadInfo(User user) {
        this.user = user;
        if (this.user.getRoleList() == null) {
            this.user.setRoleList(new ArrayList<>());
        }
    }

    /**
     * Initializes checkbox states based on the user's current roles.
     * <p>
     * All checkboxes are reset first to ensure a clean state before assignment.
     *
     * @param pickerCheckBox checkbox for Picker
     * @param plannerCheckBox checkbox for Planner
     * @param qualityOperatorCheckBox checkbox for Quality Operator
     * @param terminalOperatorCheckBox checkbox for Terminal Operator
     * @param trafficDispatcherCheckBox checkbox for Traffic Dispatcher
     * @param warehousePlannerCheckBox checkbox for Warehouse Planner
     */
    public void initializeBox(CheckBox pickerCheckBox, CheckBox plannerCheckBox, CheckBox qualityOperatorCheckBox,
                              CheckBox terminalOperatorCheckBox, CheckBox trafficDispatcherCheckBox, CheckBox warehousePlannerCheckBox) {

        pickerCheckBox.setSelected(false);
        plannerCheckBox.setSelected(false);
        qualityOperatorCheckBox.setSelected(false);
        terminalOperatorCheckBox.setSelected(false);
        trafficDispatcherCheckBox.setSelected(false);
        warehousePlannerCheckBox.setSelected(false);

        List<String> roles = user.getRoleList();
        if (roles == null || roles.isEmpty()) {
            return;
        }

        for (String role : roles) {
            switch (role) {
                case "Picker" -> pickerCheckBox.setSelected(true);
                case "Planner" -> plannerCheckBox.setSelected(true);
                case "Quality Operator" -> qualityOperatorCheckBox.setSelected(true);
                case "Terminal Operator" -> terminalOperatorCheckBox.setSelected(true);
                case "Traffic Dispatcher" -> trafficDispatcherCheckBox.setSelected(true);
                case "Warehouse Planner" -> warehousePlannerCheckBox.setSelected(true);
                default -> {}
            }
        }
    }

    /**
     * Updates the user's roles based on the selected checkboxes and persists changes to the CSV file.
     *
     * @param pickerCheckBox checkbox for Picker
     * @param plannerCheckBox checkbox for Planner
     * @param qualityOperatorCheckBox checkbox for Quality Operator
     * @param terminalOperatorCheckBox checkbox for Terminal Operator
     * @param trafficDispatcherCheckBox checkbox for Traffic Dispatcher
     * @param warehousePlannerCheckBox checkbox for Warehouse Planner
     */
    public void setRoles(CheckBox pickerCheckBox, CheckBox plannerCheckBox, CheckBox qualityOperatorCheckBox,
                         CheckBox terminalOperatorCheckBox, CheckBox trafficDispatcherCheckBox, CheckBox warehousePlannerCheckBox) {

        ArrayList<String> userRoles = new ArrayList<>();

        if (user.isAdmin()) {
            userRoles.add("Admin");
        }

        if (pickerCheckBox.isSelected()) userRoles.add("Picker");
        if (plannerCheckBox.isSelected()) userRoles.add("Planner");
        if (qualityOperatorCheckBox.isSelected()) userRoles.add("Quality Operator");
        if (terminalOperatorCheckBox.isSelected()) userRoles.add("Terminal Operator");
        if (trafficDispatcherCheckBox.isSelected()) userRoles.add("Traffic Dispatcher");
        if (warehousePlannerCheckBox.isSelected()) userRoles.add("Warehouse Planner");

        user.setRoleList(userRoles);
        updateCsv();
    }

    /**
     * Updates the user data CSV file with the current user's role configuration.
     * <p>
     * Creates the file and header if it does not exist.
     */
    private void updateCsv() {
        File file = new File(USER_DATA);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("ID,Mail,Password,Admin,Picker,Planner,Quality Operator,Terminal Operator,Traffic Dispatcher,Warehouse Planner");
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Error creating CSV: " + e.getMessage());
                return;
            }
        }

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            lines.add(header);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data[0].trim().equals(String.valueOf(user.getId()))) {
                    lines.add(buildCsvLine());
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    /**
     * Builds a single CSV line representing the current user's data.
     *
     * @return a CSV-formatted string of user data and roles
     */
    private String buildCsvLine() {
        List<String> roles = user.getRoleList();
        int admin = user.isAdmin() ? 1 : 0;
        int picker = roles.contains("Picker") ? 1 : 0;
        int planner = roles.contains("Planner") ? 1 : 0;
        int quality = roles.contains("Quality Operator") ? 1 : 0;
        int terminal = roles.contains("Terminal Operator") ? 1 : 0;
        int dispatcher = roles.contains("Traffic Dispatcher") ? 1 : 0;
        int warehouse = roles.contains("Warehouse Planner") ? 1 : 0;

        return String.join(",",
                String.valueOf(user.getId()),
                user.getEmail(),
                user.getPassword(),
                String.valueOf(admin),
                String.valueOf(picker),
                String.valueOf(planner),
                String.valueOf(quality),
                String.valueOf(terminal),
                String.valueOf(dispatcher),
                String.valueOf(warehouse)
        );
    }

    /**
     * Returns the user's ID as a string.
     *
     * @return the user ID
     */
    public String getId() { return String.valueOf(user.getId()); }

    /**
     * Returns the user's email.
     *
     * @return the user email
     */
    public String getEmail() { return user.getEmail(); }

    /**
     * Returns the user's activation status as a string.
     *
     * @return "Active" if the user is active, otherwise "Inactive"
     */
    public String getActive() { return user.isActive() ? "Active" : "Inactive"; }
}
