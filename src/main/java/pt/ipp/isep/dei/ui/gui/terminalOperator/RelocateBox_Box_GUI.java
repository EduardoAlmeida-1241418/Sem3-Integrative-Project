package pt.ipp.isep.dei.ui.gui.terminalOperator;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.util.Duration;
import pt.ipp.isep.dei.controller.terminalOperator.RelocateBox_Box_Controller;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class for the Relocate Box by Box GUI.
 * Allows the terminal operator to select and relocate boxes from one bay to another.
 * Displays box data in a table and corresponding quantities in a bar chart.
 */
public class RelocateBox_Box_GUI implements Initializable {

    private RelocateBox_Box_Controller controller;
    private UIUtils uiUtils;

    @FXML private BarChart<String, Number> boxChart;
    @FXML private CategoryAxis categoryAxis;
    @FXML private NumberAxis numberAxis;

    @FXML private TableView<Box> boxTable;
    @FXML private TableColumn<Box, String> columnBoxID;
    @FXML private TableColumn<Box, String> columnSkuItem;
    @FXML private TableColumn<Box, Integer> columnQuantity;
    @FXML private TableColumn<Box, String> columnExpiryDate;
    @FXML private TableColumn<Box, String> columnReceivedDate;

    @FXML private Label labelTextMessage;

    /**
     * Initializes the controller and UI components.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new RelocateBox_Box_Controller();
        uiUtils = new UIUtils();

        setupBarChart();
    }

    /**
     * Configures the bar chart used to visualize box quantities.
     */
    private void setupBarChart() {
        boxChart.setLegendVisible(true);
        boxChart.setAnimated(false);
        categoryAxis.setTickLabelRotation(45);
        categoryAxis.setTickLabelFont(Font.font(12));
        numberAxis.setTickLabelFont(Font.font(12));
    }

    /**
     * Sets the actual and new bay IDs and loads their boxes.
     *
     * @param actualBayId The current bay ID.
     * @param newBayId The target bay ID.
     */
    public void setSelectedBay(String actualBayId, String newBayId) {
        if (actualBayId == null || newBayId == null) {
            showMessage("Bay IDs cannot be null.", false);
            return;
        }

        if (actualBayId.equals(newBayId)) {
            showMessage("Actual Bay and New Bay cannot be the same.", false);
            return;
        }

        controller.setSelectedBaysById(actualBayId, newBayId);
        setupTableBoxes();
        updateChart();
    }

    /**
     * Configures the table columns and populates it with boxes.
     * Adds a listener for box selection.
     */
    private void setupTableBoxes() {
        columnBoxID.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBoxId()));
        columnSkuItem.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSkuItem()));
        columnQuantity.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getQuantity()).asObject());
        columnExpiryDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getExpiryDateString()));
        columnReceivedDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getReceivedDateTimeString()));

        boxTable.setItems(controller.getBoxesObservableList());

        boxTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                controller.setSelectedBox(newSelection);
                updateChart();
            }
        });
    }

    /**
     * Updates the bar chart with current box quantities and highlights the selected box.
     */
    private void updateChart() {
        List<Box> boxes = controller.getBoxes();
        Box selectedBox = controller.getSelectedBox();

        boxChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Quantity");

        for (Box box : boxes) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(box.getBoxId(), box.getQuantity());
            series.getData().add(data);
        }

        boxChart.getData().add(series);
        boxChart.applyCss();
        boxChart.layout();

        for (XYChart.Data<String, Number> data : series.getData()) {
            Node node = data.getNode();
            if (node != null) {
                node.setStyle("-fx-bar-fill: #3498db; -fx-bar-gap: 2px; -fx-bar-width: 20px;");
                Tooltip.install(node, new Tooltip(data.getXValue() + ": " + data.getYValue()));
                if (selectedBox != null && data.getXValue().equals(selectedBox.getBoxId())) {
                    node.setStyle("-fx-bar-fill: #e74c3c; -fx-bar-gap: 2px; -fx-bar-width: 20px;");
                }
                node.setOnMouseClicked(e -> {
                    controller.setSelectedBoxById(data.getXValue());
                    boxTable.getSelectionModel().select(controller.getSelectedBox());
                    boxTable.scrollTo(controller.getSelectedBox());
                    updateChart();
                });
            }
        }
    }

    /**
     * Displays a message in the UI with a fade-out animation.
     * Logs the operation result as either INFO or ERROR.
     *
     * @param message The message to display.
     * @param ignoreCondition Whether to skip log writing.
     */
    private void showMessage(String message, boolean ignoreCondition) {
        if (!ignoreCondition) {
            if (message.equals("Box relocated successfully")) {
                UIUtils.addLog(controller.getLogMessageRelocateBoxSuccess(), LogType.INFO, RoleType.TERMINAL_OPERATOR);
                labelTextMessage.setTextFill(javafx.scene.paint.Color.GREEN);
                boxTable.getSelectionModel().clearSelection();
            } else {
                UIUtils.addLog(controller.getLogMessageRelocateBoxFailure(message), LogType.ERROR, RoleType.TERMINAL_OPERATOR);
                labelTextMessage.setTextFill(javafx.scene.paint.Color.RED);
            }
        } else {
            labelTextMessage.setTextFill(javafx.scene.paint.Color.RED);
        }
        labelTextMessage.setText(message);
        labelTextMessage.setOpacity(1.0);
        labelTextMessage.setVisible(true);
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(1), labelTextMessage);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(ev -> labelTextMessage.setVisible(false));
            fade.play();
        }));
        delay.play();
    }

    /** Handles navigation to the main home page. */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /** Handles navigation to the terminal operator home page. */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/terminalOperatorHomePage.fxml",
                "MABEC - Terminal Operator - Home Page");
    }

    /** Handles navigation to the unload wagons page. */
    @FXML
    public void handleUnloadWagons(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/unloadWagons.fxml",
                "MABEC - Terminal Operator - Unload Wagons");
    }

    /** Handles navigation to the WMS logs page. */
    @FXML
    public void handleWmsLogsButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/terminalOperator/terminalOperatorWMS.fxml",
                "MABEC - Terminal Operator - WMS Logs");
    }

    /** Handles navigation back to the bay selection interface. */
    @FXML
    public void handleBackButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/relocateBox_Bay.fxml",
                "MABEC - Terminal Operator - Relocation Box");
    }

    /**
     * Confirms relocation of the selected box.
     * Updates UI and logs based on operation success.
     *
     * @param event The triggered action event.
     */
    @FXML
    public void confirmButtonOnAction(ActionEvent event) {
        if (boxTable.getSelectionModel().getSelectedItem() == null) {
            showMessage("Please select a box to relocate.", true);
            return;
        }

        controller.setSelectedBox(boxTable.getSelectionModel().getSelectedItem());
        showMessage(controller.relocateBox(), false);
        if (controller.bayIsEmpty()) {
            uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/relocateBox_Bay.fxml",
                    "MABEC - Terminal Operator - Relocation Box");
            return;
        }
        setupTableBoxes();
        updateChart();
    }

    /**
     * Relocates all boxes from the current bay to the target bay.
     * Logs and returns to the previous interface after completion.
     *
     * @param event The triggered action event.
     */
    @FXML
    public void handleRelocateAllButton(ActionEvent event) {
        for (Box box : controller.getBoxes()) {
            controller.setSelectedBox(box);
            showMessage(controller.relocateBox(), false);
        }
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/relocateBox_Bay.fxml",
                "MABEC - Terminal Operator - Relocation Box");
    }
}
