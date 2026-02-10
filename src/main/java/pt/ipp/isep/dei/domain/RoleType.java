package pt.ipp.isep.dei.domain;

import javafx.scene.paint.Color;

/**
 * Enumeration representing different user role types within the system.
 * Each role is associated with a specific color for UI representation.
 */
public enum RoleType {

    /** Global role with access to all system operations. */
    GLOBAL(Color.valueOf("#2962FF")),
    /** Picker role, responsible for selecting and handling items. */
    PICKER(Color.valueOf("#43A047")),

    /** Planner role, responsible for planning and scheduling operations. */
    PLANNER1(Color.valueOf("#8E24AA")),

    /** Quality operator role, responsible for inspections and quality checks. */
    QUALITY_OPERATOR(Color.valueOf("#FBC02D")),

    /** Terminal operator role, responsible for terminal operations and logistics. */
    TERMINAL_OPERATOR(Color.valueOf("#FB8C00")),

    /** Traffic dispatcher role, responsible for dispatching and routing trains. */
    TRAFFIC_DISPATCHER(Color.valueOf("#E53935")),

    /** Warehouse planner role, responsible for warehouse layout and task planning. */
    WAREHOUSE_PLANNER(Color.valueOf("#6D4C41")),

    ANALYST(Color.valueOf("#43A047")),
    DATA_ENGINEER(Color.valueOf("#8E24AA")),
    FREIGHT_MANAGER2(Color.valueOf("#FBC02D")),
    OPERATIONS_PLANNER(Color.valueOf("#FB8C00")),
    PLANNER2(Color.valueOf("#E53935")),

    FREIGHT_MANAGER3(Color.valueOf("#43A047")),
    INFRASTRUCTURE_PLANNER(Color.valueOf("#8E24AA")),
    MAINTENANCE_PLANNER(Color.valueOf("#FBC02D")),
    OPERATIONS_ANALYST(Color.valueOf("#FB8C00")),
    ROUTE_PLANNER(Color.valueOf("#E53935")),
    TRAFFIC_MANAGER(Color.valueOf("#6D4C41"));

    /** Color associated with the role for UI visualization. */
    private final Color color;

    /**
     * Constructs a RoleType with the specified color.
     *
     * @param color the color associated with the role
     */
    RoleType(Color color) {
        this.color = color;
    }

    /**
     * Gets the color assigned to the role.
     *
     * @return the color associated with the role
     */
    public Color getColor() {
        return color;
    }
}
