package pt.ipp.isep.dei.ui.gui.wareHousePlanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import pt.ipp.isep.dei.controller.wareHousePlanner.ChooseOrderController;
import pt.ipp.isep.dei.domain.OrderRelated.LineState;
import pt.ipp.isep.dei.domain.OrderRelated.Order;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the "Choose Order" GUI.
 * Allows the warehouse planner to view and select orders for allocation,
 * displaying their progress and line state distribution.
 */
public class ChooseOrderGUI implements Initializable {

    private ChooseOrderController controller;
    private UIUtils uiUtils;

    @FXML private Label totalLinesLabel;
    @FXML private Label eligibleLineLabel;
    @FXML private Label partialLinesLabel;
    @FXML private Label undispatchableLineLabel;
    @FXML private Label allocatedLinesLabel;

    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, String> OrderIdTableColumn;
    @FXML private TableColumn<Order, String> DueDateTableColumn;
    @FXML private TableColumn<Order, String> PriorityTableColumn;

    @FXML private HBox multiColorBar;

    /**
     * Initializes the controller and loads order information into the table.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new ChooseOrderController();
        uiUtils = new UIUtils();
        controller.loadInfo();
        loadGuiInfo();
    }

    /**
     * Loads and binds the order information to the GUI elements.
     */
    private void loadGuiInfo() {
        controller.organizeData();

        OrderIdTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOrderId()));

        DueDateTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDueDate().toString() + "  " +
                        cellData.getValue().getDueTime().toString())
        );

        PriorityTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPriority()))
        );

        ObservableList<Order> orders = FXCollections.observableArrayList(controller.getOrderList());
        orderTable.setItems(orders);

        orderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateLabels(newSelection);
                updateProgressBar(newSelection);
            }
        });
    }

    /**
     * Updates the multi-color progress bar based on line state ratios.
     *
     * @param order The selected order.
     */
    private void updateProgressBar(Order order) {
        int total = controller.getLineNumber(order);
        int eligible = Integer.parseInt(controller.getQuantityOfStateLines(order, LineState.ELIGIBLE));
        int partial = Integer.parseInt(controller.getQuantityOfStateLines(order, LineState.PARTIAL));
        int undispatchable = Integer.parseInt(controller.getQuantityOfStateLines(order, LineState.UNDISPATCHABLE));
        int allocated = Integer.parseInt(controller.getQuantityOfStateLines(order, LineState.ALLOCATED));

        multiColorBar.getChildren().clear();

        if (total > 0) {
            double eligibleRatio = (double) eligible / total;
            double partialRatio = (double) partial / total;
            double undispatchableRatio = (double) undispatchable / total;
            double allocatedRatio = (double) allocated / total;

            double totalWidth = multiColorBar.getPrefWidth();

            Region eligibleRegion = createColoredRegion("#4CAF50", eligibleRatio * totalWidth);       // Green
            Region partialRegion = createColoredRegion("#FF9800", partialRatio * totalWidth);         // Orange
            Region undispatchableRegion = createColoredRegion("#F44336", undispatchableRatio * totalWidth); // Red
            Region allocatedRegion = createColoredRegion("#2196F3", allocatedRatio * totalWidth);     // Blue

            multiColorBar.getChildren().addAll(
                    eligibleRegion,
                    partialRegion,
                    undispatchableRegion,
                    allocatedRegion
            );
        } else {
            Region empty = new Region();
            empty.setStyle("-fx-background-color: lightgray;");
            empty.setPrefSize(multiColorBar.getPrefWidth(), multiColorBar.getPrefHeight());
            multiColorBar.getChildren().add(empty);
        }
    }

    /**
     * Creates a colored region representing a portion of the progress bar.
     *
     * @param color The color of the region.
     * @param width The width of the region proportional to its line count.
     * @return The colored region.
     */
    private Region createColoredRegion(String color, double width) {
        Region region = new Region();
        region.setPrefWidth(width);
        region.setPrefHeight(multiColorBar.getPrefHeight());
        region.setStyle("-fx-background-color: " + color + ";");
        return region;
    }

    /**
     * Updates label values with order line statistics.
     *
     * @param order The selected order.
     */
    private void updateLabels(Order order) {
        totalLinesLabel.setText(String.valueOf(controller.getLineNumber(order)));
        eligibleLineLabel.setText(controller.getQuantityOfStateLines(order, LineState.ELIGIBLE));
        partialLinesLabel.setText(controller.getQuantityOfStateLines(order, LineState.PARTIAL));
        undispatchableLineLabel.setText(controller.getQuantityOfStateLines(order, LineState.UNDISPATCHABLE));
        allocatedLinesLabel.setText(controller.getQuantityOfStateLines(order, LineState.ALLOCATED));
    }

    /**
     * Navigates to the Warehouse Planner Home Page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/warehousePlanner/warehousePlannerHomePage.fxml",
                "MABEC - Warehouse Planner Home Page");
    }

    /**
     * Logs out and navigates to the Visitor Home Page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml",
                "MABEC - Visitor - Home Page");
    }

    /**
     * Opens the Order Lines page for the selected order.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void selectButtonOnAction(ActionEvent event) {
        FXMLLoader loader = uiUtils.loadFXMLScene(event, "/fxml/warehousePlanner/orderLines.fxml",
                "MABEC - Warehouse Planner - Order Lines");
        OrderLinesGUI controller = loader.getController();
        controller.setControllerData(orderTable.getSelectionModel().getSelectedItem());
    }

    /**
     * Opens the Allocation Report page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void allocationReportButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/warehousePlanner/orderAllocations.fxml",
                "MABEC - Warehouse Planner - Allocation Report");
    }

    /**
     * Opens the Warehouse Planner WMS Log page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void handleWmsLogButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/warehousePlanner/warehousePlannerWMS.fxml",
                "MABEC - Warehouse Planner - WMS Log");
    }
}
