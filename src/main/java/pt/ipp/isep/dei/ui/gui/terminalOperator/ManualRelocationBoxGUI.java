package pt.ipp.isep.dei.ui.gui.terminalOperator;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;
import pt.ipp.isep.dei.controller.terminalOperator.ManualRelocationBoxController;
import pt.ipp.isep.dei.domain.Bay;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Manual Relocation Box GUI.
 * Allows terminal operators to manually relocate boxes between bays.
 * Handles user interactions and updates the corresponding tables and messages.
 */
public class ManualRelocationBoxGUI implements Initializable {

    private ManualRelocationBoxController controller;
    private UIUtils uiUtils;

    @FXML private TableView<Bay> tableViewBays;
    @FXML private TableColumn<Bay, String> columnBayId;
    @FXML private TableColumn<Bay, String> columnSKU;
    @FXML private TableColumn<Bay, Integer> columnMaxBoxes;
    @FXML private TableColumn<Bay, Integer> columnNumBoxes;

    @FXML private TableView<Box> tableViewBoxes;
    @FXML private TableColumn<Box, String> columnBoxID;
    @FXML private TableColumn<Box, String> columnSkuItem;
    @FXML private TableColumn<Box, Integer> columnQuantity;
    @FXML private TableColumn<Box, String> columnExpiryDate;
    @FXML private TableColumn<Box, String> columnReceivedDate;

    @FXML private TextField textFieldBoxId;
    @FXML private TextField textFieldNewBayId;
    @FXML private Button buttonSelect;
    @FXML private Label labelTextMessage;

    /**
     * Initializes the GUI by setting up the controller, utilities, and tables.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new ManualRelocationBoxController();
        uiUtils = new UIUtils();

        setupTableBays();
    }

    /**
     * Configures the table displaying all bays and sets up listeners for selection changes.
     */
    private void setupTableBays() {
        columnBayId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBayId()));
        columnSKU.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSkuItemSafe()));
        columnMaxBoxes.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getMaxCapacityBoxes()).asObject());
        columnNumBoxes.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getNBoxesStorage()).asObject());

        tableViewBays.setItems(controller.getAllBays());
        tableViewBays.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                controller.setSelectedBay(newSel);
                buttonSelect.setDisable(false);
                buttonSelect.setText("Select Bay");
                setupTableBoxes();
            }
        });
    }

    /**
     * Configures the table displaying boxes for the selected bay and sets up listeners for selection changes.
     */
    private void setupTableBoxes() {
        columnBoxID.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBoxId()));
        columnSkuItem.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSkuItem()));
        columnQuantity.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getQuantity()).asObject());
        columnExpiryDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getExpiryDateString()));
        columnReceivedDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getReceivedDateTimeString()));

        tableViewBoxes.setItems(controller.getBoxesFromSelectedBay());
        tableViewBoxes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                buttonSelect.setText("Select Box");
            }
        });
    }

    /**
     * Displays a message on the screen with fade-out animation.
     * Updates logs depending on success or failure of relocation.
     *
     * @param message The message to be displayed.
     */
    private void showMessage(String message) {
        if (message.equals("Box relocated successfully")) {
            UIUtils.addLog(controller.getLogMessageRelocateBoxSuccess(), LogType.INFO, pt.ipp.isep.dei.domain.RoleType.TERMINAL_OPERATOR);
            labelTextMessage.setTextFill(javafx.scene.paint.Color.GREEN);
            textFieldBoxId.clear();
            textFieldNewBayId.clear();
            setupTableBays();
            tableViewBays.getSelectionModel().clearSelection();
            tableViewBoxes.getSelectionModel().clearSelection();
        } else {
            UIUtils.addLog(controller.getLogMessageRelocateBoxFailure(message), LogType.ERROR, pt.ipp.isep.dei.domain.RoleType.TERMINAL_OPERATOR);
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

    /**
     * Handles logout button action. Returns the user to the main home page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Handles home button action. Returns the user to the terminal operator home page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/terminalOperatorHomePage.fxml",
                "MABEC - Terminal Operator - Home Page");
    }

    /**
     * Handles wagons button action. Loads the unload wagons page.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void wagonsButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/unloadWagons.fxml",
                "MABEC - Terminal Operator - Unload Wagons");
    }

    /**
     * Handles WMS logs button action. Opens the WMS logs interface.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void handleWmsLogsButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event,  "/fxml/terminalOperator/terminalOperatorWMS.fxml", "MABEC - Terminal Operator - WMS Logs");
    }

    /**
     * Handles back button action. Returns to the relocation box menu.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void handleBackButton(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/terminalOperator/relocateBox_Bay.fxml",
                "MABEC - Terminal Operator - Relocation Box");
    }

    /**
     * Handles select button action. Selects either a box or a bay based on the current button state.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void handleSelectButton(ActionEvent event) {
        if (buttonSelect.getText().equals("Select Box")) {
            Box selectedBox = tableViewBoxes.getSelectionModel().getSelectedItem();
            if (selectedBox != null) {
                textFieldBoxId.setText(selectedBox.getBoxId());
            }
        } else {
            Bay selectedBay = tableViewBays.getSelectionModel().getSelectedItem();
            if (selectedBay != null) {
                textFieldNewBayId.setText(selectedBay.getBayId());
            }
        }
    }

    /**
     * Handles confirm button action. Triggers box relocation process and shows the corresponding message.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    public void handleConfirmButton(ActionEvent event) {
        controller.setBoxId(textFieldBoxId.getText().trim());
        controller.setNewBayId(textFieldNewBayId.getText().trim());
        showMessage(controller.relocateBox());
    }
}
