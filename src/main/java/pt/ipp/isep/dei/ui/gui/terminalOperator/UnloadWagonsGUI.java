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
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import pt.ipp.isep.dei.controller.terminalOperator.UnloadWagonsController;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller class for the Unload Wagons GUI.
 * Allows terminal operators to unload wagons, view SKU totals, and monitor warehouse occupation.
 * Provides table views for wagons and SKUs, along with a pie chart visualization.
 */
public class UnloadWagonsGUI implements Initializable {

    private UnloadWagonsController controller;
    private UIUtils uiUtils;

    @FXML private TableView<UnloadWagonsController.WagonSelection> tableViewWagons;
    @FXML private TableColumn<UnloadWagonsController.WagonSelection, String> columnWagonID;
    @FXML private TableColumn<UnloadWagonsController.WagonSelection, Integer> columnNumBoxes;
    @FXML private TableColumn<UnloadWagonsController.WagonSelection, Boolean> columnSelected;
    @FXML private TableView<UnloadWagonsController.SkuTotalDetails> tableViewSkuDetails;
    @FXML private TableColumn<UnloadWagonsController.SkuTotalDetails, String> columnSku;
    @FXML private TableColumn<UnloadWagonsController.SkuTotalDetails, Integer> columnTotalBoxes;
    @FXML private TableColumn<UnloadWagonsController.SkuTotalDetails, Integer> columnTotalItems;
    @FXML private ComboBox<String> comboBoxWarehouse;
    @FXML private PieChart pieChartOccupation;
    @FXML private Label labelErrorMessage;
    @FXML private Label labelWagonId;
    @FXML private Label labelNumBoxes;

    /**
     * Initializes the controller, tables, combo box, and UI components.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new UnloadWagonsController();
        uiUtils = new UIUtils();
        setupTableWagons();
        setupComboBox();
        setupTableSkuTotalDetails();
    }

    /** Logs out and navigates to the main home page. */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /** Returns to the terminal operator home page. */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/terminalOperatorHomePage.fxml",
                "MABEC - Terminal Operator - Home Page");
    }

    /** Opens the relocation box interface. */
    @FXML
    public void relocationBoxButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/relocateBox_Bay.fxml",
                "MABEC - Terminal Operator - Relocation Box");
    }

    /** Opens the WMS logs interface. */
    @FXML
    public void handleWmsLogsButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/terminalOperatorWMS.fxml",
                "MABEC - Terminal Operator - WMS Logs");
    }

    /** Selects all wagons for unloading. */
    @FXML
    public void selectAllButtonOnAction(ActionEvent event) {
        toggleSelection(true);
    }

    /** Deselects all wagons. */
    @FXML
    public void deselectAllButtonOnAction(ActionEvent event) {
        toggleSelection(false);
        labelWagonId.setText("           ?");
        labelNumBoxes.setText("           ?");
    }

    /**
     * Confirms unloading of selected wagons.
     * Updates data tables and visualizations upon completion.
     */
    @FXML
    public void confirmButtonOnAction(ActionEvent event) {
        if (comboBoxWarehouse.getValue() == null) {
            showTemporaryMessage("Please select a warehouse.", Color.RED);
            return;
        }

        if (controller.hasSelectedWagons()) {
            controller.setWarehouseId(comboBoxWarehouse.getValue());
            controller.unloadSelectedWagons();
            updateTableSkuDetails();
            pieChartOccupation.getData().clear();
            tableViewWagons.setItems(controller.getAllWagons());
            tableViewWagons.refresh();
            showTemporaryMessage("Selected wagons unloaded successfully.", Color.GREEN);
        } else {
            showTemporaryMessage("No wagons selected for unloading.", Color.RED);
        }
    }

    /**
     * Toggles wagon selection on or off.
     *
     * @param selectAll True to select all wagons, false to deselect all.
     */
    private void toggleSelection(boolean selectAll) {
        for (UnloadWagonsController.WagonSelection wagon : tableViewWagons.getItems()) {
            wagon.setSelected(selectAll);
        }
        updateTableSkuDetails();
        tableViewWagons.refresh();
        updateWagonInfoLabels(null);
        pieChartOccupation.getData().clear();
    }

    /** Configures the wagon table and its checkbox behavior. */
    private void setupTableWagons() {
        columnWagonID.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getWagonId()));
        columnNumBoxes.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getNumBoxes()).asObject());
        columnSelected.setCellValueFactory(c -> c.getValue().selectedProperty());
        columnSelected.setCellFactory(tc -> {
            CheckBoxTableCell<UnloadWagonsController.WagonSelection, Boolean> cell =
                    new CheckBoxTableCell<>(index -> {
                        UnloadWagonsController.WagonSelection wagon = tableViewWagons.getItems().get(index);
                        wagon.selectedProperty().addListener((obs, oldVal, newVal) -> {
                            updateTableSkuDetails();
                            updatePieChart(wagon);
                            updateWagonInfoLabels(wagon);
                            tableViewWagons.refresh();
                        });
                        return wagon.selectedProperty();
                    });
            cell.setAlignment(javafx.geometry.Pos.CENTER);
            return cell;
        });
        tableViewWagons.setItems(controller.getAllWagons());
        tableViewWagons.setRowFactory(tv -> {
            TableRow<UnloadWagonsController.WagonSelection> row = new TableRow<>() {
                @Override
                protected void updateItem(UnloadWagonsController.WagonSelection item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setStyle("");
                    } else {
                        setStyle(item.isSelected() ? "-fx-background-color: lightblue;" : "");
                        item.selectedProperty().addListener((obs, oldVal, newVal) ->
                                setStyle(newVal ? "-fx-background-color: lightblue;" : ""));
                    }
                }
            };
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && !isClickOnCheckbox(event, row)) {
                    UnloadWagonsController.WagonSelection clickedWagon = row.getItem();
                    updatePieChart(clickedWagon);
                    updateWagonInfoLabels(clickedWagon);
                }
            });
            return row;
        });
    }

    /** Configures the SKU total details table. */
    private void setupTableSkuTotalDetails() {
        columnSku.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSku()));
        columnTotalBoxes.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTotalBoxes()).asObject());
        columnTotalItems.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTotalQuantity()).asObject());
        tableViewSkuDetails.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableViewSkuDetails.setSelectionModel(null);
    }

    /** Configures the warehouse combo box. */
    private void setupComboBox() {
        comboBoxWarehouse.setItems(controller.getWarehouseIds());
    }

    /**
     * Determines if a click event occurred on a checkbox.
     *
     * @param event The mouse event.
     * @param row The table row clicked.
     * @return True if the click was on a checkbox, false otherwise.
     */
    private boolean isClickOnCheckbox(javafx.scene.input.MouseEvent event, TableRow<?> row) {
        Node target = event.getPickResult().getIntersectedNode();
        while (target != null) {
            if (target instanceof CheckBox) return true;
            target = target.getParent();
        }
        return false;
    }

    /**
     * Updates the pie chart with the SKU distribution for a selected wagon.
     *
     * @param selectedWagon The wagon whose SKUs are to be displayed.
     */
    private void updatePieChart(UnloadWagonsController.WagonSelection selectedWagon) {
        if (selectedWagon == null || selectedWagon.getWagonId().equals(labelWagonId.getText())) return;
        pieChartOccupation.getData().clear();
        Map<String, Integer> skuCountMap = controller.getWagonNumSkus(Integer.parseInt(selectedWagon.getWagonId()));
        for (Map.Entry<String, Integer> entry : skuCountMap.entrySet()) {
            pieChartOccupation.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Updates the displayed wagon information labels.
     *
     * @param selectedWagon The selected wagon to show details for.
     */
    private void updateWagonInfoLabels(UnloadWagonsController.WagonSelection selectedWagon) {
        if (selectedWagon != null) {
            labelWagonId.setText(selectedWagon.getWagonId());
            labelNumBoxes.setText(String.valueOf(selectedWagon.getNumBoxes()));
        } else {
            labelWagonId.setText("");
            labelNumBoxes.setText("");
        }
    }

    /** Updates the SKU totals table with current selection data. */
    private void updateTableSkuDetails() {
        tableViewSkuDetails.setItems(controller.getSkuTotalDetails());
    }

    /**
     * Displays a temporary message to the user with fade-out animation.
     *
     * @param message The message text.
     * @param color The color of the message text.
     */
    private void showTemporaryMessage(String message, Color color) {
        labelErrorMessage.setText(message);
        labelErrorMessage.setTextFill(color);
        labelErrorMessage.setOpacity(1.0);
        labelErrorMessage.setVisible(true);
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(1), labelErrorMessage);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(ev -> labelErrorMessage.setVisible(false));
            fade.play();
        }));
        delay.play();
    }
}
