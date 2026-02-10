package pt.ipp.isep.dei.ui.gui.wareHousePlanner;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import pt.ipp.isep.dei.controller.wareHousePlanner.OrderLinesController;
import pt.ipp.isep.dei.domain.OrderRelated.LineState;
import pt.ipp.isep.dei.domain.OrderRelated.Order;
import pt.ipp.isep.dei.domain.OrderRelated.OrderLine;
import pt.ipp.isep.dei.domain.OrderRelated.ViewMode;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for displaying and managing order lines in the Warehouse Planner module.
 * Provides functionality for allocating order lines based on their state and selected view mode.
 */
public class OrderLinesGUI implements Initializable {

    private OrderLinesController controller;
    private UIUtils uiUtils;

    @FXML
    private Label requestedQtyLabel, eligibleQtyLabel, alocatedQtyLabel, orderNumberLabel, errorLabel;

    @FXML
    private TableView<OrderLine> orderLineTable;

    @FXML
    private TableColumn<OrderLine, String> LineNoTableColumn;

    @FXML
    private TableColumn<OrderLine, String> SKUTableColumn;

    @FXML
    private TableColumn<OrderLine, String> StatusTableColumn;

    @FXML
    private ComboBox<ViewMode> viewModeComboBox;

    /**
     * Initializes the controller and utility components.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new OrderLinesController();
        uiUtils = new UIUtils();
    }

    /**
     * Loads and configures the ViewMode combo box.
     */
    private void loadComboBoxGUI(){
        // Combo Box
        viewModeComboBox.setItems(controller.getViewModeValues());
        viewModeComboBox.setValue(ViewMode.STRICT);
    }

    /**
     * Loads and populates all GUI elements with data from the controller.
     */
    private void loadGuiInfo() {
        clearLabelGuiInfo();

        orderNumberLabel.setText("Order #" + controller.getOrder().getOrderId() + " - Order Lines");

        LineNoTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getLineNumber()))
        );

        SKUTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSkuItem())
        );

        viewModeComboBox.valueProperty().addListener((obs, oldMode, newMode) -> {
            updateTableData(newMode);
        });

        // Column setup for table
        StatusTableColumn.setCellValueFactory(cellData -> {
            var line = cellData.getValue();
            var state = line.getPossibleState();

            if (viewModeComboBox.getValue() == ViewMode.STRICT && state == LineState.PARTIAL) {
                return new SimpleStringProperty("UNDISPATCHABLE");
            }

            if (viewModeComboBox.getValue() == ViewMode.PARTIAL && state == LineState.PARTIAL) {
                return new SimpleStringProperty("PARTIAL");
            }

            return new SimpleStringProperty(state.toString());
        });

        // Initialize table
        updateTableData(ViewMode.STRICT);

        // Update labels on selection
        orderLineTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateLabels(newSelection);
            }
        });
    }

    /**
     * Updates table data based on the selected ViewMode.
     *
     * @param mode The selected ViewMode (STRICT or PARTIAL)
     */
    private void updateTableData(ViewMode mode) {
        var allLines = controller.getOrderLines();

        var filtered = allLines.stream()
                .filter(line -> {
                    var state = line.getPossibleState();
                    return state == LineState.ELIGIBLE
                            || state == LineState.PARTIAL
                            || state == LineState.UNDISPATCHABLE;
                })
                .toList();

        orderLineTable.setItems(FXCollections.observableArrayList(filtered));
        orderLineTable.refresh();
    }

    /**
     * Clears the labels that display line information.
     */
    private void clearLabelGuiInfo() {
        requestedQtyLabel.setText("?");
        eligibleQtyLabel.setText("?");
        alocatedQtyLabel.setText("?");
    }

    /**
     * Updates the quantity labels with the values of the selected order line.
     *
     * @param line The selected order line
     */
    private void updateLabels(OrderLine line) {
        requestedQtyLabel.setText(controller.getRequestedQuantity(line));
        eligibleQtyLabel.setText(String.valueOf(line.getEligibleQuantity()));
        alocatedQtyLabel.setText(controller.getAllocatedQuantity(line));
    }

    /**
     * Sets the order for this GUI and loads the corresponding order lines.
     *
     * @param order The order to display
     */
    public void setControllerData(Order order){
        controller.setOrder(order);

        controller.loadLineInfo();
        loadGuiInfo();
        loadComboBoxGUI();
    }

    /**
     * Handles the Back button action.
     */
    @FXML
    public void backButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/chooseOrder.fxml", "MABEC - Warehouse Planner - Choose Order");
    }

    /**
     * Handles the Home button action.
     */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/warehousePlannerHomePage.fxml", "MABEC - Warehouse Planner - Home Page");
    }

    /**
     * Handles the Logout button action.
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/visitorMenu/homePage.fxml", "MABEC - Visitor - Home Page");
    }

    /**
     * Handles navigation to the Warehouse Planner WMS Log.
     */
    @FXML
    public void handleWmsLogButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/warehousePlannerWMS.fxml", "MABEC - Warehouse Planner - WMS Log");
    }

    /**
     * Displays a temporary message in the error label.
     *
     * @param message The message text
     * @param color The text color (CSS format)
     * @param seconds Duration before the message disappears
     */
    private void showTemporaryMessage(String message, String color, int seconds) {
        errorLabel.setStyle("-fx-text-fill: " + color + ";");
        errorLabel.setText(message);
        errorLabel.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(event -> errorLabel.setVisible(false));
        pause.play();
    }

    /**
     * Handles the allocation button action.
     * Allocates an order line according to the selected ViewMode and validates constraints.
     */
    public void allocateButtonOnAction(ActionEvent event) {
        if (orderLineTable.getSelectionModel().getSelectedItem() == null) {
            showTemporaryMessage("First select an order line", "red", 3);
            return;
        }

        ViewMode mode = viewModeComboBox.getValue();
        var selectedLine = orderLineTable.getSelectionModel().getSelectedItem();
        var state = selectedLine.getPossibleState();

        if (mode == ViewMode.STRICT && state != LineState.ELIGIBLE) {
            showTemporaryMessage("Strict Mode only allows allocation of Eligible Lines", "red", 3);
            return;
        }

        if (mode == ViewMode.PARTIAL && state == LineState.UNDISPATCHABLE) {
            showTemporaryMessage("Partial Mode doesn't allow allocation of Undispatchable Lines", "red", 3);
            return;
        }

        if (controller.verifyQuantityLeft(orderLineTable.getSelectionModel().getSelectedItem())) {
            showTemporaryMessage("There are no available Items to allocate", "red", 3);
            return;
        }

        String message = controller.allocateOrderLine(selectedLine);
        showTemporaryMessage(message, "green", 3);

        clearLabelGuiInfo();
        loadGuiInfo();
    }

    /**
     * Handles navigation to the Allocation Report view.
     */
    public void allocationReportButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/warehousePlanner/orderAllocations.fxml", "MABEC - Warehouse Planner - Allocation Report");
    }
}
