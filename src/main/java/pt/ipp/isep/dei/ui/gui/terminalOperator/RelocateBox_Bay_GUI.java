package pt.ipp.isep.dei.ui.gui.terminalOperator;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import pt.ipp.isep.dei.controller.terminalOperator.RelocateBox_Bay_Controller;
import pt.ipp.isep.dei.domain.Aisle;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Relocate Box by Bay GUI.
 * Allows terminal operators to select bays and relocate boxes between them within a warehouse.
 * Displays aisles, bays, and their current storage details.
 */
public class RelocateBox_Bay_GUI implements Initializable {

    private RelocateBox_Bay_Controller controller;
    private UIUtils uiUtils;

    @FXML private Button actualBayButtonSelect;
    @FXML private Button newBayButtonSelect;
    @FXML private VBox aislesContainer;
    @FXML private ComboBox<String> comboBoxWarehouse;
    @FXML private Label labelNoWarehouse;
    @FXML private Label labelBayId;
    @FXML private Label labelSku;
    @FXML private Label labelMaxBoxes;
    @FXML private Label labelNumBoxes;
    @FXML private Label labelQuantity;
    @FXML private Label labelErrorMessage;
    @FXML private Label labelActualBay;
    @FXML private Label labelNewBay;

    /** Base CSS style for all bay buttons. */
    private static final String BASE_BUTTON_STYLE =
            "-fx-background-color: #E0E0E0; " +
                    "-fx-border-width: 4; " +
                    "-fx-background-radius: 8; " +
                    "-fx-border-radius: 4; " +
                    "-fx-font-size: 12px;";

    /** CSS style for the selected actual bay button. */
    private static final String SELECTED_ACTUAL_BAY_STYLE =
            "-fx-background-color: #1E90FF;" +
                    "-fx-border-width: 4;" +
                    "-fx-background-radius: 8;" +
                    "-fx-border-radius: 4;" +
                    "-fx-font-size: 12px;";

    /** CSS style for the selected new bay button. */
    private static final String SELECTED_NEW_BAY_STYLE =
            "-fx-background-color: #32CD32;" +
                    "-fx-border-width: 4;" +
                    "-fx-background-radius: 8;" +
                    "-fx-border-radius: 4;" +
                    "-fx-font-size: 12px;";

    /** CSS style for aisle labels. */
    private static final String AISLE_LABEL_STYLE =
            "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;";

    /**
     * Initializes the GUI by setting up controller, utilities, colors, and warehouse combo box.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new RelocateBox_Bay_Controller();
        uiUtils = new UIUtils();
        controller.setBayColors();
        configureComboBox();
    }

    /**
     * Configures the warehouse combo box and sets the event handler to load aisles and bays.
     */
    private void configureComboBox() {
        comboBoxWarehouse.getItems().clear();
        comboBoxWarehouse.getItems().addAll(controller.getWarehousesIds());
        comboBoxWarehouse.setOnAction(event -> {
            String selectedWarehouse = comboBoxWarehouse.getValue();
            controller.setWarehouseId(selectedWarehouse);
            loadAislesAndBays();
        });
    }

    /**
     * Loads the aisles and their corresponding bays into the GUI.
     * Displays an error message if no warehouse is selected.
     */
    private void loadAislesAndBays() {
        aislesContainer.getChildren().clear();

        if (comboBoxWarehouse.getValue() == null || comboBoxWarehouse.getValue().isEmpty()) {
            labelNoWarehouse.setVisible(true);
            return;
        } else labelNoWarehouse.setVisible(false);

        HBox hboxAisles = new HBox(20);
        int maxBays = controller.getAisles().stream()
                .mapToInt(a -> a.getBaysIdAsList().size())
                .max()
                .orElse(0);

        for (Aisle aisle : controller.getAisles()) {
            hboxAisles.getChildren().add(createAisleBox(aisle, maxBays));
        }

        aislesContainer.getChildren().add(hboxAisles);
    }

    /**
     * Creates a visual representation of an aisle with its bays.
     *
     * @param aisle The aisle object containing bay information.
     * @param maxBays The maximum number of bays across all aisles.
     * @return A VBox containing the aisle label and its bays grid.
     */
    private VBox createAisleBox(Aisle aisle, int maxBays) {
        VBox aisleBox = new VBox(10);

        Label aisleLabel = new Label("Aisle " + aisle.getAisleID().substring(3));
        aisleLabel.setStyle(AISLE_LABEL_STYLE);

        GridPane bayGrid = new GridPane();
        bayGrid.setHgap(5);
        bayGrid.setVgap(5);

        for (int i = 0; i < maxBays; i++) {
            Button bayButton = (i < aisle.getBaysIdAsList().size())
                    ? createBayButton(aisle.getBaysIdAsList().get(i))
                    : createInvisiblePlaceholder("N/A");

            bayGrid.add(bayButton, 0, i);
        }

        aisleBox.getChildren().addAll(aisleLabel, bayGrid);
        return aisleBox;
    }

    /**
     * Creates a button representing a bay.
     *
     * @param bayId The unique ID of the bay.
     * @return A configured bay button.
     */
    private Button createBayButton(String bayId) {
        Button bayButton = new Button("");
        configureBayButton(bayButton, bayId);
        bayButton.setOnAction(e -> selectBay(bayButton));
        return bayButton;
    }

    /**
     * Creates an invisible placeholder button for alignment.
     *
     * @param bayId The bay ID placeholder.
     * @return An invisible, disabled button.
     */
    private Button createInvisiblePlaceholder(String bayId) {
        Button placeholder = new Button();
        placeholder.setDisable(true);
        placeholder.setOpacity(0);
        configureBayButton(placeholder, bayId);
        return placeholder;
    }

    /**
     * Configures a bay button with its text, size, and style.
     *
     * @param button The button to configure.
     * @param bayId The ID of the bay.
     */
    private void configureBayButton(Button button, String bayId) {
        String sku = controller.getSkuByBayId(bayId);
        if (sku == null || sku.isEmpty()) {
            sku = "Empty";
        }
        button.setText("B" + bayId.substring(bayId.lastIndexOf('B') + 1) + " - " + sku);
        button.setPrefWidth(110);
        button.setPrefHeight(50);
        button.setId(bayId);
        button.setStyle(BASE_BUTTON_STYLE + "-fx-border-color: " + controller.getBayColor(bayId) + ";");
    }

    /**
     * Handles bay selection logic for actual and new bays.
     * Updates UI elements and validation accordingly.
     *
     * @param bayButton The clicked bay button.
     */
    private void selectBay(Button bayButton) {

        if (bayButton == actualBayButtonSelect) {
            actualBayButtonSelect.setStyle(BASE_BUTTON_STYLE + "-fx-border-color: " + controller.getBayColor(bayButton.getId()) + ";");
            actualBayButtonSelect = null;
            labelActualBay.setText("         ?");
            updateBayDetails(bayButton.getId());
            return;
        }

        if (bayButton == newBayButtonSelect) {
            newBayButtonSelect.setStyle(BASE_BUTTON_STYLE + "-fx-border-color: " + controller.getBayColor(bayButton.getId()) + ";");
            newBayButtonSelect = null;
            labelNewBay.setText("         ?");
            updateBayDetails(bayButton.getId());
            return;
        }

        if (actualBayButtonSelect == null) {
            actualBayButtonSelect = bayButton;
            if (controller.isBayEmpty(actualBayButtonSelect.getId())) {
                bayButton.setStyle(BASE_BUTTON_STYLE + "-fx-border-color: " + controller.getBayColor(bayButton.getId()) + ";");
                showErrorMessage("The selected actual bay is empty.");
                actualBayButtonSelect = null;
                updateBayDetails(bayButton.getId());
                return;
            }
            if (newBayButtonSelect != null && !baysConfirmation(bayButton)) {
                actualBayButtonSelect = null;
                return;
            }
            labelActualBay.setText(bayButton.getId());
            bayButton.setStyle(SELECTED_ACTUAL_BAY_STYLE);
            updateBayDetails(bayButton.getId());
            return;
        }

        if (newBayButtonSelect == null) {
            newBayButtonSelect = bayButton;
            if (baysConfirmation(bayButton)) {
                newBayButtonSelect = null;
                return;
            }
            labelNewBay.setText(bayButton.getId());
            bayButton.setStyle(SELECTED_NEW_BAY_STYLE);
            updateBayDetails(bayButton.getId());
            return;
        }

        updateBayDetails(bayButton.getId());
    }

    /**
     * Validates the selected bays for compatibility.
     *
     * @param bayButton The button being validated.
     * @return True if validation failed, false otherwise.
     */
    private boolean baysConfirmation(Button bayButton) {
        if (controller.baysWithDifferentSkus(actualBayButtonSelect.getId(), newBayButtonSelect.getId())) {
            bayButton.setStyle(BASE_BUTTON_STYLE + "-fx-border-color: " + controller.getBayColor(bayButton.getId()) + ";");
            showErrorMessage("The selected bays contain different SKUs.");
            updateBayDetails(bayButton.getId());
            return true;
        }

        if (controller.isBayFull(newBayButtonSelect.getId())) {
            bayButton.setStyle(BASE_BUTTON_STYLE + "-fx-border-color: " + controller.getBayColor(newBayButtonSelect.getId()) + ";");
            showErrorMessage("The selected new bay is full.");
            updateBayDetails(bayButton.getId());
            return true;
        }

        return false;
    }

    /**
     * Updates the bay information labels based on a given bay ID.
     *
     * @param bayId The bay ID to retrieve information for.
     */
    private void updateBayDetails(String bayId) {
        if (bayId == null) {
            labelBayId.setText("?");
            labelSku.setText("?");
            labelMaxBoxes.setText("?");
            labelNumBoxes.setText("?");
            labelQuantity.setText("?");
            return;
        }
        Bay bay = controller.getBayById(bayId);
        if (bay == null) {
            return;
        }
        labelBayId.setText(bayId);
        if (bay.getSkuItem() == null) {
            labelSku.setText("Empty");
        } else {
            labelSku.setText(bay.getSkuItem());
        }
        labelMaxBoxes.setText("" + bay.getMaxCapacityBoxes());
        labelNumBoxes.setText("" + bay.getNBoxesStorage());
        labelQuantity.setText("" + bay.getQuantityItems());
    }

    /** Handles navigation to the home page. */
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
    public void wagonsButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/unloadWagons.fxml",
                "MABEC - Terminal Operator - Unload Wagons");
    }

    /** Handles navigation to the WMS logs interface. */
    @FXML
    public void handleWmsLogsButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/terminalOperator/terminalOperatorWMS.fxml", "MABEC - Terminal Operator - WMS Logs");
    }

    /**
     * Handles confirmation of bay selection and navigates to the box relocation interface.
     *
     * @param event The triggered action event.
     */
    @FXML
    public void handleConfirmButton(ActionEvent event) {
        if (comboBoxWarehouse.getValue() == null || comboBoxWarehouse.getValue().isEmpty()) {
            showErrorMessage("Please select a warehouse.");
            return;
        }

        if (actualBayButtonSelect == null) {
            showErrorMessage("Please select the actual bay.");
            return;
        }

        if (newBayButtonSelect == null) {
            showErrorMessage("Please select the new bay.");
            return;
        }

        if (actualBayButtonSelect == newBayButtonSelect) {
            showErrorMessage("Actual bay and new bay cannot be the same.");
            return;
        }

        FXMLLoader loader = uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/relocateBox_Box.fxml",
                "MABEC - Terminal Operator - Relocate Box - Select Box");
        RelocateBox_Box_GUI controller = loader.getController();
        controller.setSelectedBay(labelActualBay.getText(), labelNewBay.getText());
    }

    /** Handles navigation to the manual relocation mode. */
    @FXML
    public void handleButtonManual(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/manualRelocationBox.fxml",
                "MABEC - Terminal Operator - Relocate Box - Manual");
    }

    /**
     * Displays an error message with fade-out animation.
     *
     * @param message The message to display.
     */
    private void showErrorMessage(String message) {
        labelErrorMessage.setText(message);
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
