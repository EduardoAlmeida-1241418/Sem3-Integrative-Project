package pt.ipp.isep.dei.ui.gui.infrastructurePlanner;

/**
 * Context holder for the Backbone view.
 *
 * <p>This utility class exposes two static properties used by the
 * infrastructure planner's backbone view: a boolean flag indicating whether
 * the view has been adapted, and a base filename used when loading or
 * saving view-related resources. The class only contains static accessors
 * and mutators; instantiation is prevented by a private constructor.</p>
 *
 */
public class BackboneViewContext {

    private static boolean adapted;
    private static String baseFilename;

    /**
     * Returns whether the backbone view has been adapted.
     *
     * @return {@code true} when the view is adapted; {@code false} otherwise
     */
    public static boolean isAdapted() {
        return adapted;
    }

    /**
     * Set the adapted flag for the backbone view.
     *
     * @param adapted {@code true} to mark the view as adapted, {@code false}
     *                to mark it as not adapted
     */
    public static void setAdapted(boolean adapted) {
        BackboneViewContext.adapted = adapted;
    }

    /**
     * Obtain the base filename associated with the backbone view.
     *
     * @return the base filename, or {@code null} if none has been set
     */
    public static String getBaseFilename() {
        return baseFilename;
    }

    /**
     * Set the base filename used by the backbone view when loading or saving
     * resources.
     *
     * @param baseFilename the base filename to store; may be {@code null}
     */
    public static void setBaseFilename(String baseFilename) {
        BackboneViewContext.baseFilename = baseFilename;
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BackboneViewContext() {
        // evita instanciação
    }
}
