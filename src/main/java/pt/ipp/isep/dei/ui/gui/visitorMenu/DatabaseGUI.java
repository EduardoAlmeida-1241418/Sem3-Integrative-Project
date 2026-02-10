package pt.ipp.isep.dei.ui.gui.visitorMenu;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.ipp.isep.dei.Bootstrap;
import pt.ipp.isep.dei.controller.global.LoadDataBaseController;
import pt.ipp.isep.dei.data.config.*;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DatabaseGUI implements Initializable {

    private UIUtils uiUtils;
    private boolean waiting = false;
    private ScheduledExecutorService scheduler;

    @FXML
    private Button resetDataBaseButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uiUtils = new UIUtils();
        resetDataBaseButton.setDisable(true);

        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });

        scheduler.scheduleAtFixedRate(() -> {
            if (Bootstrap.isBootstrapLoaded()) {
                Platform.runLater(() -> {
                    resetDataBaseButton.setDisable(false);
                    if (waiting) {
                        initializeDatabase();
                        waiting = false;
                    }
                });
            }
        }, 0, 200, TimeUnit.MILLISECONDS);

        resetDataBaseButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs2, oldWin, newWin) -> {
                    if (newWin != null) {
                        try {
                            if (!DatabaseConnection.validateDatabase(DatabaseSchema.tablesTest)) {
                                initializeDatabase();
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }

    @FXML
    public void loginButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/signIn.fxml", "MABEC - Sign In");
    }

    @FXML
    public void homeButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    @FXML
    public void devTeamButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/devTeam.fxml", "MABEC - Dev Team");
    }

    @FXML
    public void aboutProjectButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/aboutProject.fxml", "MABEC - About Project");
    }

    @FXML
    public void closeApplicationButtonOnAction(ActionEvent event) {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
        Platform.exit();
    }

    @FXML
    public void resetDataBaseOnAction(ActionEvent event) {
        if (Bootstrap.isBootstrapLoaded()) {
            initializeDatabase();
        } else {
            waiting = true;
        }
    }

    private void initializeDatabase() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                DatabaseDropper.dropAll();
                DatabaseInitializer.initialize();
                new LoadDataBaseController().run();
                new Bootstrap().generateDefaultSchedules();
                return null;
            }
        };

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(resetDataBaseButton.getScene().getWindow());
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

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/loadingPopup.css")).toExternalForm()
        );

        popup.setScene(scene);

        task.setOnSucceeded(e -> popup.close());
        task.setOnFailed(e -> popup.close());

        new Thread(task).start();
        popup.show();
    }
}
