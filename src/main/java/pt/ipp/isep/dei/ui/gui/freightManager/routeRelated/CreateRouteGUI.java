package pt.ipp.isep.dei.ui.gui.freightManager.routeRelated;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.freightManager.routeRelated.CreateRouteController;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for creating freight routes.
 *
 * <p>This JavaFX controller coordinates the user interface used by freight
 * managers to assemble a sequence of freights into a route. It initialises
 * helper objects, populates the primary and secondary freight tables, and
 * provides handlers for navigation and route creation actions. All business
 * logic is delegated to the associated controller class
 * ({@link CreateRouteController}); this class is concerned purely with
 * presentation and user interaction.</p>
 *
 */
public class CreateRouteGUI implements Initializable {

    private UIUtils uiUtils;
    private CreateRouteController controller;

    @FXML
    private TableView<Freight> freightTableView;

    @FXML
    private TableColumn<Freight, String> idTableColumn, originStationTableColumn, destinationStationTableColumn, dateTableColumn, wagonQuantityTableColumn;

    @FXML
    private TableView<Freight> freightOrderTableView;

    @FXML
    private TableColumn<Freight, String> id2TableColumn, departureTableColumn, destinationTableColumn;

    /**
     * Initialise the GUI controller.
     *
     * @param url not used by this implementation
     * @param resourceBundle not used by this implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        this.controller = new CreateRouteController();

        initGuiInfo();

    }

    /**
     * Initialise the graphical elements and populate the tables.
     *
     * <p>This method delegates to specific initialisation helpers for the
     * primary freight table and the secondary (order) table.</p>
     */
    private void initGuiInfo() {
        initFreightTableView();
        initSecundaryTable();
    }


    /**
     * Configure and populate the primary freight table.
     *
     * <p>Column cell factories extract displayable values from
     * {@link Freight} instances. A double-click listener is registered to
     * allow quick addition of freights to the route order, subject to the
     * controller's validation rules.</p>
     */
    private void initFreightTableView() {
        idTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        originStationTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getOriginFacility().getName()))
        );

        destinationStationTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDestinationFacility().getName()))
        );

        dateTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDate()))
        );

        wagonQuantityTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getWagons().size()))
        );

        ObservableList<Freight> freightList = FXCollections.observableArrayList(controller.getFreights());
        freightTableView.setItems(freightList);

        // -------------------------------
        // LISTENER FOR DOUBLE-CLICK
        // -------------------------------
        freightTableView.setRowFactory(tv -> {
            TableRow<Freight> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Freight selected = row.getItem();

                    if (!controller.combinationIsPossible(selected, freightOrderTableView)){
                        Stage stage = (Stage) freightTableView.getScene().getWindow();
                        UIUtils.showAlert(stage, "Same Wagon on Different Freight", "error");
                        return;
                    }

                    // validations equivalent to the button behaviour
                    if (!controller.getFreightOrderList().contains(selected)) {
                        controller.addFreight(selected);
                        updateFreightList();
                        updateFreightOrderList();
                    } else {
                        Stage stage = (Stage) freightTableView.getScene().getWindow();
                        UIUtils.showAlert(stage, "This train is already added", "error");
                    }
                }
            });
            return row;
        });
    }

    /**
     * Refresh the primary freight table by removing those selected for the
     * current route order.
     */
    private void updateFreightList() {
        ObservableList<Freight> freightList = FXCollections.observableArrayList(controller.getFreights());
        freightList.removeAll(controller.getFreightOrderList());
        freightTableView.setItems(freightList);
    }


    /**
     * Refresh the secondary table showing the freight order for the route.
     */
    private void updateFreightOrderList() {
        ObservableList<Freight> freightList = FXCollections.observableArrayList(controller.getFreightOrderList());
        freightOrderTableView.setItems(freightList);
    }

    /**
     * Configure and populate the secondary freight table. A double-click
     * listener allows removal of freights from the current order.
     */
    private void initSecundaryTable() {
        id2TableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        departureTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOriginFacility().getName())
        );

        destinationTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDestinationFacility().getName())
        );

        // -----------------------------------
        // DOUBLE-CLICK TO REMOVE FROM ORDER
        // -----------------------------------
        freightOrderTableView.setRowFactory(tv -> {
            TableRow<Freight> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Freight selected = row.getItem();
                    controller.getFreightOrderList().remove(selected);
                    updateFreightList();
                    updateFreightOrderList();
                }
            });
            return row;
        });
    }

    /**
     * Navigate to the Freight Manager home page.
     *
     * @param event the action event that triggered navigation
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerHomePage.fxml", "MABEC - Freight Manager - Home Page");
    }

    /**
     * Navigate to the trains view.
     *
     * @param event the action event that triggered navigation
     */
    @FXML
    public void trainsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/TrainRelated/freightManagerLocomotive.fxml", "MABEC - Freight Manager - Trains");
    }

    /**
     * Navigate to the route management view.
     *
     * @param event the action event that triggered navigation
     */
    @FXML
    public void routeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/RouteRelated/freightManagerRoute.fxml", "MABEC - Freight Manager - Route");
    }

    /**
     * Navigate back to the route management view.
     *
     * @param event the action event that triggered navigation
     */
    @FXML
    public void backButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/RouteRelated/freightManagerRoute.fxml", "MABEC - Freight Manager - Route");
    }

    /**
     * Navigate to the GQS / NOS logs view.
     *
     * @param event the action event that triggered navigation
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerGQS_NOS.fxml", "MABEC - Freight Manager - GQS / NOS Logs");
    }

    /**
     * Navigate to the scheduler view.
     *
     * @param event the action event that triggered navigation
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
     * Clear the current freight order list and refresh both tables.
     *
     * @param event the action event that triggered the clear action
     */
    public void clearListButtonOnAction(ActionEvent event) {
        controller.getFreightOrderList().clear();

        updateFreightOrderList();
        updateFreightList();
    }



    /**
     * Create the route from the selected freights after performing
     * verification checks.
     *
     * <p>If verification fails the method returns without creating a route.</p>
     *
     * @param event the action event that triggered the creation
     */
    public void createRouteButtonOnAction(ActionEvent event) {
        if(!verificationsCreation(event)){
            return;
        }

        controller.createRoute();
        controller.getFreightOrderList().clear();

        updateFreightOrderList();
        updateFreightList();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        UIUtils.showAlert(stage, "Route Created with Success", "success");

    }

    /**
     * Perform basic verification checks before creating a route.
     *
     * @param event the action event which initiated the verification
     * @return {@code true} if verifications passed; {@code false} otherwise
     */
    private boolean verificationsCreation(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if(controller.getFreightOrderList().isEmpty()){
            UIUtils.showAlert(stage, "Please add a freight to create a route", "error");
            return false;
        }

        return true;
    }

    /**
     * Show an informational popup explaining interaction hints for the view.
     *
     * @param event the action event that triggered the display
     */
    public void infoButonOnAction(ActionEvent event) {
        uiUtils.showInfoPopup(event, "Double-Click do add Freights from Freight List .\n"
                + "Double-Click to remove freights from Freight Order.\n"
                + "\nCannot add freight with the same wagons", 5);
    }
}