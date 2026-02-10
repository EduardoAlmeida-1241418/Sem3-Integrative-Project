package pt.ipp.isep.dei.ui.gui.operationsAnalyst;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.operationsAnalyst.OperationsAnalystController;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * JavaFX controller for the Operations Analyst "Max Flow" GUI.
 * <p>
 * This class wires FXML controls (tables, labels, text fields and buttons)
 * to the application logic provided by {@link pt.ipp.isep.dei.controller.operationsAnalyst.OperationsAnalystController}.
 * It supports selecting a source and target station (by clicking the table rows
 * or by entering IDs) and triggering a max-flow calculation between them.
 * The results and any errors are displayed in the GUI using labels and alerts.
 */
public class OperationsAnalystMaxFlowGUI implements Initializable {

    /** Utility helper used for loading scenes and showing alerts. */
    private UIUtils uiUtils;
    /** Controller that encapsulates the operations-analyst domain logic. */
    private OperationsAnalystController controller;

    /** Table that lists candidate source stations for the max-flow calculation. */
    @FXML
    private TableView<StationEsinf> sourceStationsTable;

    /** Column that displays the source station human-readable name. */
    @FXML
    private TableColumn<StationEsinf, String> sourceStationTableColumn;

    /** Column that displays the source station identifier. */
    @FXML
    private TableColumn<StationEsinf, String> sourceStationIDTableColumn;

    /** Table that lists candidate target stations for the max-flow calculation. */
    @FXML
    private TableView<StationEsinf> targetStationsTable;

    /** Column that displays the target station human-readable name. */
    @FXML
    private TableColumn<StationEsinf, String> targetStationTableColumn;

    /** Column that displays the target station identifier. */
    @FXML
    private TableColumn<StationEsinf, String> targetStationIDTableColumn;

    /** Label used to show the currently selected source station ID in the GUI. */
    @FXML
    private Label sourceStationLabel;

    /** Label used to show the currently selected target station ID in the GUI. */
    @FXML
    private Label targetStationLabel;

    /** Label used to display the computed max flow result. */
    @FXML
    private Label maxFlowValueLabel;

    /** Label used to display validation messages and errors to the user. */
    @FXML
    private Label errorLabel;

    /** Button that triggers the calculation using either typed IDs or table selections. */
    @FXML
    private Button selectButton;

    /** Text field where the user can enter a source station ID manually. */
    @FXML
    private TextField sourceStationIDTextField;

    /** Text field where the user can enter a target station ID manually. */
    @FXML
    private TextField targetStationIDTextField;

    /**
     * JavaFX lifecycle method called after FXML injection.
     * Initializes helpers, controller and GUI components.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uiUtils = new UIUtils();
        controller = new OperationsAnalystController();

        initGuiInfo();

        selectButton.setDisable(false);
    }

    /**
     * Initializes table columns, populates the stations list and sets up table row factories.
     * This method wires the data model to the table views and configures initial GUI state.
     */
    private void initGuiInfo() {
        sourceStationTableColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStationName()));
        targetStationTableColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStationName()));
        sourceStationIDTableColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getId()));
        targetStationIDTableColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getId()));

        ObservableList<StationEsinf> stations = FXCollections.observableArrayList(controller.getStations());
        sourceStationsTable.setItems(stations);
        targetStationsTable.setItems(stations);

        initSourceStationsTable();
        initTargetStationsTable();
    }

    /**
     * Configures the row factory for the source stations table to handle mouse clicks on rows.
     */
    private void initSourceStationsTable() {
        sourceStationsTable.setRowFactory(tv -> {
            TableRow<StationEsinf> row = new TableRow<>();
            row.setOnMouseClicked(event -> handleSourceRowClick(event, row));
            return row;
        });
    }

    /**
     * Handles a mouse click on a source table row. If the row is valid, it selects the source station
     * and updates the select button enabled state.
     */
    private void handleSourceRowClick(MouseEvent event, TableRow<StationEsinf> row) {
        if (event == null || event.getClickCount() < 1) return;
        if (row == null || row.isEmpty()) return;
        selectSourceStation(row.getItem());
        selectButton.setDisable(!controller.hasSelectedSourceAndTarget());
    }

    /**
     * Updates the controller with the selected source station and refreshes the displayed source label.
     *
     * @param station the station selected as source
     */
    private void selectSourceStation(StationEsinf station) {
        controller.setSource(station);
        sourceStationLabel.setText(station.getId());
        selectButton.setDisable(!controller.hasSelectedSourceAndTarget());
    }

    /**
     * Configures the row factory for the target stations table to handle mouse clicks on rows.
     */
    private void initTargetStationsTable() {
        targetStationsTable.setRowFactory(tv -> {
            TableRow<StationEsinf> row = new TableRow<>();
            row.setOnMouseClicked(event -> handleTargetRowClick(event, row));
            return row;
        });
    }

    /**
     * Handles a mouse click on a target table row. If the row is valid, it selects the target station
     * and updates the select button enabled state.
     */
    private void handleTargetRowClick(MouseEvent event, TableRow<StationEsinf> row) {
        if (event == null || event.getClickCount() < 1) return;
        if (row == null || row.isEmpty()) return;
        selectTargetStation(row.getItem());
        selectButton.setDisable(!controller.hasSelectedSourceAndTarget());
    }

    /**
     * Updates the controller with the selected target station and refreshes the displayed target label.
     *
     * @param station the station selected as target
     */
    private void selectTargetStation(StationEsinf station) {
        controller.setTarget(station);
        targetStationLabel.setText(station.getId());
        selectButton.setDisable(!controller.hasSelectedSourceAndTarget());
    }

    /**
     * Accepts either typed station IDs from the text fields or table selections, delegates validation
     * and calculation to the controller, updates result labels and shows a success or error alert.
     */
    @FXML
    private void selectButtonOnAction() {
        errorLabel.setText("");

        String srcId = sourceStationIDTextField.getText();
        String tgtId = targetStationIDTextField.getText();

        StationEsinf srcSelected = sourceStationsTable.getSelectionModel().getSelectedItem();
        StationEsinf tgtSelected = targetStationsTable.getSelectionModel().getSelectedItem();

        try {
            int maxFlow = controller.calculateMaxFlowFromInputs(srcId, tgtId, srcSelected, tgtSelected);
            StationEsinf s = controller.getSource();
            StationEsinf t = controller.getTarget();
            if (s != null) {
                sourceStationLabel.setText(s.getId());
            } else {
                sourceStationLabel.setText("");
            }
            if (t != null) {
                targetStationLabel.setText(t.getId());
            } else {
                targetStationLabel.setText("");
            }
            maxFlowValueLabel.setText(String.valueOf(maxFlow));
            Stage stage = (Stage) sourceStationsTable.getScene().getWindow();
            UIUtils.showAlert(stage, "Max flow calculated successfully.", "success");
        } catch (IllegalArgumentException ex) {
            Stage stage = (Stage) sourceStationsTable.getScene().getWindow();
            UIUtils.showAlert(stage, ex.getMessage(), "error");
        }
    }

    /**
     * Navigates to the Operations Analyst home page scene.
     *
     * @param event the action event generated by clicking the Home button
     */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/operationsAnalyst/operationsAnalystHomePage.fxml", "MABEC - Operations Analyst - Home Page");
    }

    /**
     * Logs out the current user and navigates back to the general home page scene.
     *
     * @param event the action event generated by clicking the Logout button
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Navigates to the NOS (Network Operations System) logs scene for the Operations Analyst.
     *
     * @param event the action event generated by clicking the NOS button
     */
    @FXML
    public void NOSButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/operationsAnalyst/operationsAnalystNOS.fxml", "MABEC - Operations Analyst - NOS Logs");
    }
}
