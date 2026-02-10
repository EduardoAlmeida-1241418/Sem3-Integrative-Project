package pt.ipp.isep.dei.ui.gui.freightManager.trainRelated;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.freightManager.FreightManagerFreightController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * GUI controller used by the Freight Manager to inspect and select freights.
 *
 * <p>This JavaFX controller initialises table views showing available
 * freights and the current freight order. It delegates business logic to the
 * {@link FreightManagerFreightController} and scene navigation to
 * {@link UIUtils}.</p>
 *
 */
public class FreightManagerFreightGUI implements Initializable {

    private UIUtils uiUtils;
    private FreightManagerFreightController controller;

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
     * <p>This method is invoked by the JavaFX runtime. It creates helper
     * instances used throughout the controller.</p>
     *
     * @param url resource location (not used)
     * @param resourceBundle resources (not used)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.uiUtils = new UIUtils();
        this.controller = new FreightManagerFreightController();

    }

    /**
     * Supply contextual information to the controller.
     *
     * <p>The method forwards operator, date, time and available locomotive
     * list to the underlying controller and initialises the GUI content.</p>
     *
     * @param operator current operator responsible for the action
     * @param date the date to use for freight selection
     * @param time the time to use for freight selection
     * @param locomotiveList available locomotives
     */
    public void setControllerInfo(Operator operator, Date date, Time time, List<Locomotive> locomotiveList) {
        controller.setOperator(operator);
        controller.setDate(date);
        controller.setTime(time);
        controller.setLocomotiveList(locomotiveList);

        initGuiInfo();
    }

    /**
     * Initialise graphical elements and table content.
     *
     * <p>Delegates to specific initialisation helpers for the primary and
     * secondary tables.</p>
     */
    private void initGuiInfo() {
        initFreightTableView();
        initSecundaryTable();
    }

    /**
     * Configure the secondary table columns which list freights in the
     * current order.
     */
    private void initSecundaryTable() {
        id2TableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId()))
        );

        departureTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getOriginFacility().getName()))
        );

        destinationTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDestinationFacility().getName()))
        );
    }

    /**
     * Configure and populate the primary freight table.
     *
     * <p>Column factories extract readable values from {@link Freight}
     * instances. The table is initialised with the controller's freight
     * list.</p>
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
    }


    /**
     * Navigate to the Freight Manager home page.
     *
     * @param event triggering action
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerHomePage.fxml", "MABEC - Freight Manager - Home Page");
    }

    /**
     * Navigate to the freight trains view.
     *
     * @param event triggering action
     */
    @FXML
    public void trainsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/TrainRelated/freightManagerLocomotive.fxml", "MABEC - Freight Manager - Trains");
    }

    /**
     * Navigate to the route management view.
     *
     * @param event triggering action
     */
    @FXML
    public void routeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/RouteRelated/freightManagerRoute.fxml", "MABEC - Freight Manager - Route");
    }

    /**
     * Open the GQS / NOS logs view.
     *
     * @param event triggering action
     */
    @FXML
    public void LogsOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerGQS_NOS.fxml", "MABEC - Freight Manager - GQS / NOS Logs");
    }

    /**
     * Navigate to the scheduler view.
     *
     * @param event triggering action
     */
    @FXML
    public void schedulerOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerScheduler.fxml", "MABEC - Freight Manager - Scheduler");
    }

    /**
     * Log out the current user and return to the public home page.
     *
     * @param event triggering action
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Add the currently selected freight to the freight order, after
     * performing validations.
     *
     * @param event triggering action
     */
    public void addFreightOnAction(ActionEvent event) {
        if(!verificationsAdd(event)){
            return;
        }

        controller.addFreight(freightTableView.getSelectionModel().getSelectedItem());

        updateFreightOrderList();
    }

    /**
     * Refresh the freight order table to reflect the controller's current
     * freight order list.
     */
    private void updateFreightOrderList() {
        ObservableList<Freight> freightList = FXCollections.observableArrayList(controller.getFreightOrderList());
        freightOrderTableView.setItems(freightList);
    }

    /**
     * Validate user selections before adding a freight to the order.
     *
     * @param event triggering action
     * @return {@code true} if all validations pass, otherwise {@code false}
     */
    private boolean verificationsAdd(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if(freightTableView.getSelectionModel().getSelectedItem() == null){
            UIUtils.showAlert(stage, "Please select a train to add", "error");
            return false;
        }

        if(controller.getFreightOrderList().contains(freightTableView.getSelectionModel().getSelectedItem())){
            UIUtils.showAlert(stage, "This train is already added", "error");
            return false;
        }

        return true;
    }

    /**
     * Handler invoked when the user requests to create a train from the
     * currently selected freights. Verification is performed before any
     * further action.
     *
     * @param event triggering action
     */
    public void createTrainButtonOnAction(ActionEvent event) {
        if(!verificationsCreation(event)){
            return;
        }
        /*
        FXMLLoader loader =  uiUtils.loadFXMLScene(event, "/fxml/freightManager/freightManagerPath.fxml", "MABEC - Freight Manager - Choose Path");
        FreightManagerPathGUI newController = loader.getController();

        newController.initControllerItems(controller.getOperator(), controller.getDate(), controller.getTime(), controller.getLocomotiveList(), controller.getFreightOrderList());


         */
    }

    /**
     * Validate user input before creating a train from the freight order.
     *
     * @param event triggering action
     * @return {@code true} when creation preconditions are satisfied
     */
    private boolean verificationsCreation(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if(controller.getFreightOrderList().isEmpty()){
            UIUtils.showAlert(stage, "Please add a train to create", "error");
            return false;
        }

        return true;
    }
}