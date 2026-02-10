package pt.ipp.isep.dei.ui.gui.freightManager.routeRelated;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import pt.ipp.isep.dei.ui.console.UIUtils;


/**
 * GUI controller for freight-manager route-related views.
 *
 * <p>This controller centralises navigation from the route-related menu to
 * other Freight Manager screens such as the home page, trains, scheduler
 * and the create/view route screens. Scene transitions are delegated to
 * {@link UIUtils} and no business logic is performed here.</p>
 *
 */
public class RouteGUI{

    private UIUtils uiUtils;

    /**
     * Initialise helper objects required by the GUI controller.
     *
     * <p>This method is invoked by the JavaFX runtime (or by the test
     * harness) to set up utility helpers used when handling UI events.</p>
     */
    public void initialize() {
        this.uiUtils = new UIUtils();
    }

    /**
     * Navigate to the Freight Manager home page.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerHomePage.fxml", "MABEC - Freight Manager - Home Page");
    }

    /**
     * Navigate to the freight trains view.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void trainsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/TrainRelated/freightManagerLocomotive.fxml", "MABEC - Freight Manager - Trains");
    }


    /**
     * Navigate to the GQS / NOS logs view for the freight manager.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerGQS_NOS.fxml", "MABEC - Freight Manager - GQS / NOS Logs");
    }

    /**
     * Navigate to the scheduler view for freight management.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void schedulerOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerScheduler.fxml", "MABEC - Freight Manager - Scheduler");
    }

    /**
     * Log out the current user and return to the public home page.
     *
     * @param event the action event that triggered the logout
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Open the view route screen.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void viewRouteOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/RouteRelated/freightManagerViewRoute.fxml", "MABEC - Freight Manager - View Route");
    }

    /**
     * Open the create route screen where freights are selected.
     *
     * @param event the action event that triggered the navigation
     */
    @FXML
    public void createRouteOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/RouteRelated/chooseFreights.fxml", "MABEC - Freight Manager - Create Route");
    }
}