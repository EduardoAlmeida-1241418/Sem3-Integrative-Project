package pt.ipp.isep.dei.ui.gui;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * GUI controller responsible for displaying a temporary success popup message.
 * The popup is rounded, styled, and automatically disappears after a few seconds.
 */
public class SuccessPopupGUI {

    @FXML private AnchorPane rootPane;
    @FXML private Label successLabel;

    /**
     * Initializes the popup by applying a rounded clipping mask.
     */
    @FXML
    private void initialize() {
        applyRoundedClip();
    }

    /**
     * Applies rounded corners to the popup window by clipping the root pane.
     */
    private void applyRoundedClip() {
        Rectangle clip = new Rectangle();
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        rootPane.setClip(clip);
        rootPane.layoutBoundsProperty().addListener((obs, o, n) -> {
            clip.setWidth(n.getWidth());
            clip.setHeight(n.getHeight());
        });
    }

    /**
     * Displays a success popup with the given message for a specified duration.
     *
     * @param message The message to display.
     * @param seconds Duration in seconds before the popup automatically closes.
     */
    public static void showPopup(String message, int seconds) {
        try {
            FXMLLoader loader = new FXMLLoader(SuccessPopupGUI.class.getResource("/fxml/message/SuccessMessage.fxml"));
            Parent root = loader.load();

            SuccessPopupGUI controller = loader.getController();
            controller.successLabel.setText(message);

            Stage popupStage = new Stage(StageStyle.TRANSPARENT);
            popupStage.initModality(Modality.NONE);
            popupStage.setResizable(false);
            popupStage.setTitle("Success");

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            popupStage.setScene(scene);

            popupStage.centerOnScreen();
            popupStage.show();

            PauseTransition delay = new PauseTransition(Duration.seconds(Math.max(1, seconds)));
            delay.setOnFinished(e -> popupStage.close());
            delay.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
