package pt.ipp.isep.dei.ui.gui.qualityOperator;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import pt.ipp.isep.dei.controller.qualityOperator.QuarantineReturnController;
import pt.ipp.isep.dei.domain.Return;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the Quality Operator's Quarantine Return page.
 * <p>
 * This class manages the quarantine return interface, allowing the operator
 * to inspect returned products, decide whether to restock or discard them,
 * and view the corresponding audit log entries.
 */
public class QuarantineReturnGUI implements Initializable {

    private QuarantineReturnController controller;
    private UIUtils uiUtils;

    @FXML
    private TableView<Return> tblQuarantine;
    @FXML
    private TableColumn<Return, String> colId;
    @FXML
    private TableColumn<Return, String> colSku;
    @FXML
    private TableColumn<Return, String> colQty;
    @FXML
    private TableColumn<Return, String> colReason;
    @FXML
    private TableColumn<Return, String> colReceivedAt;
    @FXML
    private TableColumn<Return, String> colExpiryDate;
    @FXML
    private TextArea txtAudit;
    @FXML
    private Button inspectButton;
    @FXML
    private Button totalRestockButton;
    @FXML
    private Button partialRestockButton;
    @FXML
    private Button discardButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label labelMessage;
    @FXML
    private TextField quantityTextField;

    /**
     * Initializes the interface by configuring the table columns,
     * and loading quarantine return data along with audit logs.
     *
     * @param url            the URL location of the FXML file (unused)
     * @param resourceBundle the ResourceBundle for localization (unused)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.controller = new QuarantineReturnController();
        this.uiUtils = new UIUtils();
        setQuarantineTableView();
        refreshQuarantine();
        refreshAudit();
    }

    /**
     * Sets up the quarantine table columns with data bindings
     * from the {@link Return} entity fields.
     */
    private void setQuarantineTableView() {
        colId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getReturnId()));
        colSku.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSkuItem()));
        colQty.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getQuantity())));
        colReason.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getReturnReason() == null ? "" : c.getValue().getReturnReason().name()));

        colReceivedAt.setCellValueFactory(c -> {
            var d = c.getValue().getDateStamp();
            if (d == null) return new SimpleStringProperty("");
            String formatted = String.format("%02d/%02d/%04d", d.getDay(), d.getMonth(), d.getYear());
            return new SimpleStringProperty(formatted);
        });

        colExpiryDate.setCellValueFactory(c -> {
            var d = c.getValue().getExpiryDate();
            if (d == null) return new SimpleStringProperty("");
            String formatted = String.format("%02d/%02d/%04d", d.getDay(), d.getMonth(), d.getYear());
            return new SimpleStringProperty(formatted);
        });
    }

    /**
     * Refreshes the quarantine table with updated data
     * retrieved from the controller.
     */
    private void refreshQuarantine() {
        tblQuarantine.setItems(FXCollections.observableArrayList(controller.listQuarantine()));
    }

    /**
     * Updates the audit log text area with the latest entries.
     */
    private void refreshAudit() {
        if (txtAudit == null) return;
        txtAudit.clear();
        for (String line : controller.listAuditLines()) {
            txtAudit.appendText(line + "\n");
        }
    }

    /**
     * Navigates to the Quality Operator Home Page.
     *
     * @param event the event triggered by pressing the Home button
     */
    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/qualityOperator/qualityOperatorHomePage.fxml",
                "MABEC - Quality Operator - Home Page");
    }

    /**
     * Logs out the current user and redirects to the main Home Page.
     *
     * @param event the event triggered by pressing the Logout button
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Opens the WMS Logs page for the Quality Operator.
     *
     * @param actionEvent the event triggered by pressing the WMS Logs button
     */
    @FXML
    public void handleWmsLogsButton(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/qualityOperator/qualityOperatorWMS.fxml",
                "MABEC - Quality Operator - WMS Logs");
    }

    /**
     * Performs automatic inspection of the selected return item.
     * Based on inspection results, it may restock, discard, or
     * require manual decision-making.
     */
    @FXML
    private void handleInspectButton() {
        if (tblQuarantine.getSelectionModel().isEmpty() && !tblQuarantine.getItems().isEmpty()) {
            tblQuarantine.getSelectionModel().select(0);
        }
        Return sel = tblQuarantine.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        String id = sel.getReturnId();
        String result = controller.inspectAuto(id);

        switch (result) {
            case "RESTOCKED" ->
                    showTemporaryMessage(id + " successfully restocked.", Color.GREEN);
            case "DISCARDED_FAILED_RESTOCK" ->
                    showTemporaryMessage("Restock failed for " + id + ". Item discarded.", Color.RED);
            case "DISCARDED" ->
                    showTemporaryMessage(id + " successfully discarded.", Color.RED);
            case "NEEDS_DECISION" ->
                    handleNeedDecision();
        }

        refreshQuarantine();
        refreshAudit();
    }

    /**
     * Handles full restocking of the selected item after inspection.
     */
    @FXML
    public void handleTotalRestockButton() {
        String id = tblQuarantine.getSelectionModel().getSelectedItem().getReturnId();
        boolean success = controller.restock(id, controller.getReturnQuantity(id));
        if (success) {
            showTemporaryMessage(id + " successfully restocked.", Color.GREEN);
            resetButtons();
            refreshQuarantine();
            refreshAudit();
        } else {
            showTemporaryMessage("Total restock failed. Try again.", Color.RED);
        }
    }

    /**
     * Handles partial restocking of a selected item based on the user-specified quantity.
     * Validates input before performing the operation.
     */
    @FXML
    public void handlePartialRestockButton() {
        String id = tblQuarantine.getSelectionModel().getSelectedItem().getReturnId();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityTextField.getText());
            if (quantity <= 0) {
                showTemporaryMessage("Quantity must be a positive integer.", Color.RED);
                return;
            }
            if (quantity > controller.getReturnQuantity(id)) {
                showTemporaryMessage("Quantity exceeds returned amount.", Color.RED);
                return;
            }
        } catch (NumberFormatException e) {
            showTemporaryMessage("Invalid quantity. Please enter a valid integer.", Color.RED);
            return;
        }

        boolean success = controller.restock(id, quantity);
        if (success) {
            showTemporaryMessage(id + " partially restocked with quantity " + quantity + ".", Color.GREEN);
            resetButtons();
            refreshQuarantine();
            refreshAudit();
        } else {
            showTemporaryMessage("Partial restock failed. Check quantity and try again.", Color.RED);
        }
    }

    /**
     * Handles the manual discard of a selected item after inspection.
     */
    @FXML
    public void handleDiscardButton() {
        String id = tblQuarantine.getSelectionModel().getSelectedItem().getReturnId();
        controller.discard(id);
        showTemporaryMessage(id + " successfully discarded.", Color.RED);
        resetButtons();
        refreshQuarantine();
        refreshAudit();
    }

    /**
     * Cancels the manual decision process and resets the action buttons to their default state.
     */
    @FXML
    public void handleCancelButton() {
        resetButtons();
    }

    /**
     * Enables manual decision buttons (restock, partial restock, discard)
     * when an item requires operator intervention.
     */
    private void handleNeedDecision() {
        inspectButton.setDisable(true);
        totalRestockButton.setDisable(false);
        partialRestockButton.setDisable(false);
        discardButton.setDisable(false);
        cancelButton.setDisable(false);
        quantityTextField.setDisable(false);
    }

    /**
     * Resets all buttons and input fields to their default inactive state
     * after a manual decision has been completed or canceled.
     */
    private void resetButtons() {
        inspectButton.setDisable(false);
        totalRestockButton.setDisable(true);
        partialRestockButton.setDisable(true);
        discardButton.setDisable(true);
        cancelButton.setDisable(true);
        quantityTextField.setDisable(true);
        quantityTextField.clear();
    }

    /**
     * Displays a temporary, color-coded message on the screen with a fade-out animation.
     *
     * @param message the text to display
     * @param color   the color used for the message text
     */
    private void showTemporaryMessage(String message, Color color) {
        labelMessage.setText(message);
        labelMessage.setTextFill(color);
        labelMessage.setOpacity(1.0);
        labelMessage.setVisible(true);
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(1), labelMessage);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(ev -> labelMessage.setVisible(false));
            fade.play();
        }));
        delay.play();
    }
}