package pt.ipp.isep.dei.ui.console;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import pt.ipp.isep.dei.domain.Log;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.data.repository.Repositories;
import javafx.geometry.Pos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Utility class for console and GUI operations.
 *
 * <p>This class centralises a number of helper routines used across the console
 * and JavaFX user interfaces. It provides simple methods for reading user input
 * from the console, printing formatted messages, reading CSV files, adding log
 * entries and performing common scene/navigation operations in a JavaFX
 * environment.</p>
 *
 * <p>All methods are implemented as either {@code static} helpers or instance
 * helpers that do not alter underlying domain logic; the class is intended
 * purely as a collection of convenience utilities.</p>
 */
public class UIUtils {

    /** Shared scanner for console input. */
    private static final java.util.Scanner scanner = new java.util.Scanner(System.in);

    // ---------------------- INPUT ----------------------

    /**
     * Reads a string from user input.
     *
     * @param prompt the message to display before reading
     * @return the entered string
     */
    public static String readString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    /**
     * Reads an integer from user input, validating format.
     *
     * @param prompt the message to display
     * @return a valid integer entered by the user
     */
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                printError("Invalid number. Please try again.");
            }
        }
    }

    /**
     * Reads a double value from user input, validating format.
     *
     * @param prompt the message to display
     * @return a valid double entered by the user
     */
    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                printError("Invalid number. Please try again.");
            }
        }
    }

    /**
     * Displays a list of options and allows the user to choose one.
     *
     * @param prompt message displayed before options
     * @param options list of available options
     * @param <T> type of the options
     * @return the chosen option
     */
    public static <T> T chooseFromList(String prompt, List<T> options) {
        printHeader(prompt);
        for (int i = 0; i < options.size(); i++) {
            System.out.println(" [" + (i + 1) + "] " + options.get(i));
        }
        int choice = readInt("Choose an option (1-" + options.size() + ")");
        while (choice < 1 || choice > options.size()) {
            printError("Invalid choice. Try again.");
            choice = readInt("Choose an option (1-" + options.size() + ")");
        }
        return options.get(choice - 1);
    }

    /**
     * Pauses the console until the user presses Enter.
     *
     * @param message message to display before pausing
     */
    public static void pause(String message) {
        System.out.println(message + " (Press Enter to continue)");
        scanner.nextLine();
    }

    /**
     * Clears the console screen by printing multiple blank lines.
     */
    public static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    // ---------------------- PRINT ----------------------

    /**
     * Prints a message surrounded by a border.
     *
     * @param message the message to display
     */
    public static void printMessage(String message) {
        String border = repeatChar('*', message.length() + 4);
        System.out.println(border);
        System.out.println("* " + message + " *");
        System.out.println(border);
    }

    /**
     * Prints multiple messages with borders.
     *
     * @param messages the messages to print
     */
    public static void printMessages(String... messages) {
        for (String msg : messages) {
            printMessage(msg);
        }
    }

    /**
     * Prints a list with a formatted header.
     *
     * @param list list of items
     * @param header section header
     * @param <T> item type
     */
    public static <T> void printList(List<T> list, String header) {
        printHeader(header);
        for (T item : list) {
            System.out.println(" - " + item);
        }
        printSeparator();
    }

    /**
     * Prints an array with a formatted header.
     *
     * @param array array of items
     * @param header section header
     * @param <T> item type
     */
    public static <T> void printArray(T[] array, String header) {
        printHeader(header);
        for (T item : array) {
            System.out.println(" - " + item);
        }
        printSeparator();
    }

    /**
     * Prints a section header with decorative borders.
     *
     * @param header the header text
     */
    public static void printHeader(String header) {
        String border = repeatChar('=', header.length() + 4);
        System.out.println(border);
        System.out.println("| " + header + " |");
        System.out.println(border);
    }

    /**
     * Prints a horizontal separator line.
     */
    public static void printSeparator() {
        System.out.println(repeatChar('-', 40));
    }

    /**
     * Prints an error message with an [ERROR] prefix.
     *
     * @param message the error message
     */
    public static void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    // ---------------------- UTIL ----------------------

    /**
     * Generates a string composed of a repeated character.
     *
     * @param c character to repeat
     * @param count number of repetitions
     * @return a string of repeated characters
     */
    private static String repeatChar(char c, int count) {
        return new String(new char[count]).replace('\0', c);
    }

    /**
     * Trims leading and trailing spaces from an array of strings.
     *
     * @param texts array of strings to trim
     * @return a new array with trimmed strings
     */
    public static String[] trimEdges(String[] texts) {
        if (texts == null) {
            return null;
        }
        String[] result = new String[texts.length];
        for (int i = 0; i < texts.length; i++) {
            result[i] = texts[i] == null ? null : texts[i].trim();
        }
        return result;
    }

    // --------------------- READ CSV --------------------

    /**
     * Reads a CSV file and returns its contents as a list of string arrays.
     * Supports both comma and semicolon delimiters.
     *
     * @param path the file path to read
     * @return a list of rows, each represented as a string array
     */
    public static List<String[]> readCSV(String path) {
        List<String[]> rows = new ArrayList<>();
        String delimiter;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                if (line.contains(";")) {
                    delimiter = ";";
                } else delimiter = ",";

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(delimiter, -1);
                rows.add(trimEdges(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rows;
    }

    // --------------------- LOGGING --------------------

    /**
     * Adds a new log entry to the system log repository.
     *
     * @param message log message
     * @param logType log category (e.g., INFO, ERROR)
     * @param roleType role responsible for the action
     */
    public static void addLog(String message, LogType logType, RoleType roleType) {
        Repositories.getInstance().getLogRepository().add(new Log(message, logType, roleType));
    }

    // --------------------- GUI NAVIGATION --------------------

    /**
     * Loads and displays a new FXML scene.
     *
     * @param event triggering event
     * @param fxmlPath path to the FXML file
     * @param title window title
     * @return the FXMLLoader for the loaded FXML
     */
    public FXMLLoader loadFXMLScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
            return loader;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Show a transient toast-like alert attached to the provided owner stage.
     *
     * <p>The alert uses a small, styled window displayed above the owner
     * stage. The {@code type} argument controls the visual style and icon
     * (for example "success", "error", "warning" or other). The alert
     * automatically closes after a short duration; the user may also close
     * it manually.</p>
     *
     * @param owner the owner {@link Stage} to which the alert will be attached
     * @param message the textual content to display inside the alert
     * @param type a short identifier for the alert style (case-insensitive)
     */
    public static void showAlert(Stage owner, String message, String type) {

        String normalized = (type == null ? "info" : type.toLowerCase());

        String iconText = switch (normalized) {
            case "success" -> "✔";
            case "error"   -> "✖";
            case "warning" -> "⚠";
            default        -> "i";
        };

        Label icon = new Label(iconText);
        icon.getStyleClass().add("toast-icon-circle");


        Label text = new Label(
                switch (normalized) {
                    case "success" -> "Success: " + message;
                    case "error"   -> "Error: " + message;
                    case "warning" -> "Warning: " + message;
                    default        -> "Info: " + message;
                }
        );
        text.getStyleClass().add("toast-message");

        Label closeBtn = new Label("✖");
        closeBtn.getStyleClass().add("toast-close-btn");

        HBox header = new HBox(icon, text, closeBtn);
        header.setSpacing(12);
        header.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(text, Priority.ALWAYS);

        Region progress = new Region();
        progress.getStyleClass().add("toast-progress");
        progress.setPrefWidth(0);

        HBox progressContainer = new HBox(progress);
        progressContainer.getStyleClass().add("toast-progress-container");
        progressContainer.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(header, progressContainer);
        root.getStyleClass().addAll("toast-box", "toast-" + normalized);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        var css = UIUtils.class.getResource("/css/alert.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        Stage toastStage = new Stage();
        toastStage.initStyle(StageStyle.TRANSPARENT);
        toastStage.initOwner(owner);
        toastStage.setAlwaysOnTop(true);
        toastStage.setScene(scene);

        toastStage.setOnShown(e -> Platform.runLater(() -> {

            double ownerX = owner.getX();
            double ownerY = owner.getY();
            double ownerW = owner.getWidth();
            double ownerH = owner.getHeight();

            double x = ownerX + ownerW * 0.008;
            double y = ownerY + ownerH * 0.048;

            toastStage.setX(x);
            toastStage.setY(y);

            double barWidth = root.getWidth() - 32;
            if (barWidth < 0) barWidth = root.getWidth();

            progress.setPrefWidth(0);
            progress.setMaxWidth(barWidth);

            Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(progress.prefWidthProperty(), 0)), new KeyFrame(Duration.seconds(5), new KeyValue(progress.prefWidthProperty(), barWidth))
            );
            timeline.play();

            PauseTransition delay = new PauseTransition(Duration.seconds(5));
            delay.setOnFinished(ev -> toastStage.close());
            delay.play();

            closeBtn.setOnMouseClicked(ev -> {
                timeline.stop();
                delay.stop();
                toastStage.close();
            });

        }));

        toastStage.show();
    }

    /**
     * Show a small informational popup near the control that triggered the
     * supplied {@link ActionEvent}.
     *
     * <p>The popup is positioned immediately below the source node and will
     * automatically hide after the supplied number of seconds.</p>
     *
     * @param event the event that triggered the popup (used to determine position)
     * @param message the message text to show inside the popup
     * @param seconds duration in seconds before the popup is automatically hidden
     */
    public void showInfoPopup(ActionEvent event, String message, int seconds) {

        Popup popup = new Popup();

        Label msg = new Label(message);
        msg.setStyle("""
            -fx-background-color: white;
            -fx-padding: 10;
            -fx-border-color: black;
            -fx-border-width: 1;
            -fx-font-size: 14;
            -fx-background-radius: 6;
            -fx-border-radius: 6;
            """);

        popup.getContent().add(msg);

        // posição baseada no botão que acionou o evento
        Node source = (Node) event.getSource();
        Bounds bounds = source.localToScreen(source.getBoundsInLocal());

        popup.show(
                source.getScene().getWindow(),
                bounds.getMinX(),
                bounds.getMaxY() + 5
        );

        PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
        delay.setOnFinished(e -> popup.hide());
        delay.play();
    }

}
