package pt.ipp.isep.dei;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.ipp.isep.dei.controller.global.LoadDataBaseController;
import pt.ipp.isep.dei.data.config.*;
import pt.ipp.isep.dei.data.memory.GeneralScheduleStoreInMemory;
import pt.ipp.isep.dei.data.memory.TrainStoreInMemory;
import pt.ipp.isep.dei.domain.schedule.ScheduleGenerator;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenuApp extends Application implements Runnable {

    @Override
    public void run() {
        Logger logger = Logger.getLogger("javafx.fxml");
        logger.setLevel(Level.SEVERE);
        for (Handler handler : Logger.getLogger("").getHandlers()) {
            handler.setLevel(Level.SEVERE);
        }
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/visitorMenu/homePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("MABEC - Main Menu");
        stage.show();

        if (!DatabaseConnection.validateDatabase(DatabaseSchema.tablesTest)) {
            Task<Void> databaseTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    DatabaseDropper.dropAll();
                    DatabaseInitializer.initialize();
                    return null;
                }
            };

            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(stage.getScene().getWindow());
            popup.setAlwaysOnTop(true);
            popup.setResizable(false);
            popup.setTitle("Processing...");
            popup.setOnCloseRequest(event -> event.consume());

            Label title = new Label("Creating Default Database...");
            title.getStyleClass().add("popup-title");

            Label subtitle = new Label("Please wait while the operation completes.");
            subtitle.getStyleClass().add("popup-subtitle");

            ProgressBar bar = new ProgressBar();
            bar.setProgress(-1);
            bar.setPrefWidth(380);
            bar.getStyleClass().add("popup-progress");

            VBox root = new VBox(18, title, subtitle, bar);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(30));
            root.getStyleClass().add("popup-root");

            Scene popupScene = new Scene(root);
            popupScene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/css/loadingPopup.css")).toExternalForm()
            );

            popup.setScene(popupScene);

            databaseTask.setOnSucceeded(e -> {
                popup.close();
                startBootstrap(stage);
                new Bootstrap().generateDefaultSchedules();
            });

            databaseTask.setOnFailed(e -> {
                popup.close();
                UIUtils.showAlert(stage, "Database initialization failed.", "error");
            });

            new Thread(databaseTask, "Database-Init-Thread").start();
            popup.show();
        } else {
            startBootstrap(stage);
        }
    }

    private void startBootstrap(Stage stage) {
        new Thread(() -> {
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.run();
                Bootstrap.setBootstrapLoaded(true);

                Platform.runLater(() ->
                        UIUtils.showAlert(stage, "Bootstrap Completed.", "info")
                );
            } catch (Exception e) {
                Platform.runLater(() ->
                        UIUtils.showAlert(stage, "Bootstrap error: " + e.getMessage(), "error")
                );
            }
        }, "Bootstrap-Thread").start();
    }
}
