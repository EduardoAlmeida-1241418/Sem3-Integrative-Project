package pt.ipp.isep.dei.ui.gui.routePlanner;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.graphstream.graph.Graph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.Viewer;
import pt.ipp.isep.dei.controller.routePlanner.ShortestPathController;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.domain.Graph.Edge;
import pt.ipp.isep.dei.domain.Graph.MetricsStationEdge;
import pt.ipp.isep.dei.domain.LogType;
import pt.ipp.isep.dei.domain.RoleType;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * JavaFX controller responsible for finding and displaying shortest paths
 * and related graph information in the Route Planner module.
 *
 * <p>This controller initialises table views, reads user input for the
 * origin and destination stations, invokes the {@link ShortestPathController}
 * to compute subgraphs and renders the resulting graph using GraphStream.
 * It also exposes simple UI handlers for toggling path/negative‑cycle
 * presentations and for basic navigation. The Javadoc added here is
 * descriptive only and does not modify any program behaviour.</p>
 */
public class ShortestPathGUI implements Initializable {

    private ShortestPathController controller;
    private UIUtils uiUtils;

    @FXML
    private AnchorPane graphContainer;

    @FXML
    private Button backButton, pathButton, negativeCycleButton;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField departureStationTextField, arrivalStationTextField;

    @FXML
    private TableView<Edge<StationEsinf, MetricsStationEdge>> edgesPathTableView, edgesNegativeCycleTableView;

    @FXML
    private TableView<StationEsinf> stationsTableView;

    @FXML
    private TableColumn<Edge<StationEsinf, MetricsStationEdge>, String> orderTableColumn, departureStationTableColumn, arrivalStationTableColumn, distanceTableColumn, capacityTableColumn, costTableColumn;

    @FXML
    private TableColumn<StationEsinf, String> stationIdTableColumn, stationNameTableColumn;

    @FXML
    private Label nStationsLabel, nEdgesLabel, totalDistanceLabel, totalCostLabel;

    private boolean graphFound = false;

    /**
     * Initialise the UI controller. This method is invoked by the JavaFX
     * runtime when the view is loaded.
     *
     * <p>It constructs the underlying {@link ShortestPathController},
     * prepares helper utilities and configures the table views. Example
     * station identifiers are placed in the input fields to ease
     * manual testing.</p>
     *
     * @param url unused in this implementation
     * @param resourceBundle unused in this implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            controller = new ShortestPathController();
            uiUtils = new UIUtils();
            configEdgesTableView();
            configStationsTableView();

            departureStationTextField.setText("447"); // Example
            arrivalStationTextField.setText("2089");   // Example

            putData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Configure the columns used in the edges table. Column factories map
     * edge attributes to a displayable string representation.
     */
    private void configEdgesTableView() {
        orderTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(edgesPathTableView.getItems().indexOf(cellData.getValue()) + 1)));

        departureStationTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVOrig().getStationName()));

        arrivalStationTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVDest().getStationName()));

        distanceTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f km", cellData.getValue().getWeight().getDistance())));

        capacityTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getWeight().getCapacity())));

        costTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.3f", cellData.getValue().getWeight().getCost())));

        edgesPathTableView.setSelectionModel(null);
    }

    /**
     * Configure the stations table used to display the vertices that appear
     * in the computed subgraph.
     */
    private void configStationsTableView() {
        stationIdTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));

        stationNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStationName()));

        stationsTableView.setSelectionModel(null);
    }

    /**
     * Request the controller to compute the subgraph for the provided
     * departure and arrival station identifiers and handle the case where
     * no path exists.
     *
     * @throws IOException when underlying IO operations fail
     */
    private void putData() throws IOException {
        controller.setvOrigKey(departureStationTextField.getText());
        controller.setvDestKey(arrivalStationTextField.getText());
        controller.setSubgraphResult();
        if (controller.getSubgraphResult().numEdges() == 0) {
            showTemporaryMessage("No path found between the selected stations.", Color.RED);
            UIUtils.addLog("No path found between stations " + departureStationTextField.getText() + " and " + arrivalStationTextField.getText() + ".", LogType.ERROR, RoleType.ROUTE_PLANNER);
            nStationsLabel.setText("?");
            nEdgesLabel.setText("?");
            totalDistanceLabel.setText("?");
            totalCostLabel.setText("?");
            graphFound = false;
        } else {
            showTemporaryMessage("Path found successfully!", Color.GREEN);
            UIUtils.addLog("Path found between stations " + departureStationTextField.getText() + " and " + arrivalStationTextField.getText() + ".", LogType.INFO, RoleType.ROUTE_PLANNER);
            graphFound = true;

            loadPath();
            showGraph();

            new Thread(() -> {
                try {
                    controller.createResultSvgGraph();
                    UIUtils.addLog("SVG graph created for path between stations " + departureStationTextField.getText() + " and " + arrivalStationTextField.getText() + ".", LogType.INFO, RoleType.ROUTE_PLANNER);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    /**
     * Populate the UI tables with the stations and edges that belong to the
     * computed subgraph and update summary labels.
     */
    private void loadPath() {
        if (graphFound) {
            ObservableList<StationEsinf> stations = controller.getStationsOfSubgraph();
            ObservableList<Edge<StationEsinf, MetricsStationEdge>> edges = controller.getEdgesOfSubgraph();
            stationsTableView.setItems(stations);
            edgesPathTableView.setItems(edges);
            nStationsLabel.setText(stations.size() + "");
            nEdgesLabel.setText(edges.size() + "");
            totalDistanceLabel.setText(String.format("%.2f km", controller.getTotalDistanceOfSubgraph()));
            totalCostLabel.setText(String.format("%.3f", controller.getTotalCostOfSubgraph()));
            String text = "Path found involving stations: ";
            for (StationEsinf station : stations) {
                text += station.getStationName() + ", ";
            }
            text = text.substring(0, text.length() - 2);
            UIUtils.addLog(text, LogType.INFO, RoleType.ROUTE_PLANNER);
        }
    }

    /**
     * Populate the UI tables with the stations and edges that belong to the
     * detected negative cycle and update summary labels accordingly.
     */
    private void loadNegativeCycle() {
        if (graphFound) {
            ObservableList<StationEsinf> stations = controller.getStationsOfNegativeCycle();
            ObservableList<Edge<StationEsinf, MetricsStationEdge>> edges = controller.getEdgesOfNegativeCycle();
            stationsTableView.setItems(stations);
            edgesPathTableView.setItems(edges);
            nStationsLabel.setText(String.valueOf(stations.size()));
            nEdgesLabel.setText(String.valueOf(edges.size()));
            totalDistanceLabel.setText("?");
            totalCostLabel.setText("-∞");
            String text = "Negative cycle found involving stations: ";
            for (StationEsinf station : stations) {
                text += station.getStationName() + ", ";
            }
            text = text.substring(0, text.length() - 2);
            UIUtils.addLog(text, LogType.INFO, RoleType.ROUTE_PLANNER);
        }
    }

    /**
     * Render the computed subgraph using GraphStream and embed the view
     * inside the JavaFX container assigned to the controller.
     */
    private void showGraph() {
        Graph graph = controller.getGraphStreamRepresentation(controller.getSubgraphResult());

        graph.setAttribute("ui.stylesheet",
                "node {" +
                        "   fill-color: #4A90E2;" +
                        "   size: 25px;" +
                        "   text-size: 18px;" +
                        "} " +
                        "edge {" +
                        "   fill-color: gray;" +
                        "   arrow-shape: arrow;" +
                        "   arrow-size: 12px, 7px;" +
                        "   text-size: 16px;" +
                        "   padding: 15px;" +
                        "   text-offset: 10px;" +
                        "}"
        );

        FxViewer viewer = new FxViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

        viewer.enableAutoLayout(new org.graphstream.ui.layout.springbox.implementations.LinLog());

        FxViewPanel panel = (FxViewPanel) viewer.addDefaultView(false);

        panel.setMouseTransparent(false);

        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            viewer.disableAutoLayout();
        }).start();

        AnchorPane.setTopAnchor(panel, 0.0);
        AnchorPane.setBottomAnchor(panel, 0.0);
        AnchorPane.setLeftAnchor(panel, 0.0);
        AnchorPane.setRightAnchor(panel, 0.0);

        graphContainer.getChildren().add(panel);
    }

    /**
     * Validate the user input fields and, if valid, request a new path
     * computation from the controller.
     *
     * <p>This handler is bound to the UI search action and performs basic
     * presence and logical checks (for example that origin and destination
     * are distinct and exist in the dataset) before invoking the path
     * computation routine.</p>
     *
     * @param actionEvent the action event triggered by the UI
     * @throws IOException when underlying IO operations fail
     */
    @FXML
    public void handleSearchPath(ActionEvent actionEvent) throws IOException {
        String departureStation = departureStationTextField.getText();
        String arrivalStation = arrivalStationTextField.getText();

        if (departureStation.isEmpty() || arrivalStation.isEmpty()) {
            showTemporaryMessage("Please enter both initial and final stations.", Color.RED);
            return;
        }

        if (!controller.existsStation(departureStation)) {
            showTemporaryMessage("Initial station doesn't exist.", Color.RED);
            return;
        }

        if (!controller.existsStation(arrivalStation)) {
            showTemporaryMessage("Final station doesn't exist.", Color.RED);
            return;
        }

        if (departureStation.equals(arrivalStation)) {
            showTemporaryMessage("Initial and final stations cannot be the same.", Color.RED);
            return;
        }

        putData();
    }

    /**
     * Switch the UI to display the computed path; disables the path button
     * to avoid redundant actions and enables the negative‑cycle button.
     */
    @FXML
    public void handleShowPath(ActionEvent actionEvent) {
        pathButton.setDisable(true);
        negativeCycleButton.setDisable(false);
        loadPath();
    }

    /**
     * Switch the UI to display a detected negative cycle; toggles button
     * states appropriately.
     */
    @FXML
    public void handleShowNegativeCycle(ActionEvent actionEvent) {
        pathButton.setDisable(false);
        negativeCycleButton.setDisable(true);
        loadNegativeCycle();
    }

    /**
     * Make the embedded graph view visible to the user and enable the
     * back control so the user may return to the textual summaries.
     */
    @FXML
    public void handleShowGraph(ActionEvent actionEvent) {
        graphContainer.setDisable(false);
        graphContainer.setVisible(true);
        backButton.setDisable(false);
        backButton.setVisible(true);
    }

    /**
     * Hide the embedded graph view and restore the previous UI state.
     */
    @FXML
    public void backButtonOnAction(ActionEvent actionEvent) {
        graphContainer.setDisable(true);
        graphContainer.setVisible(false);
        backButton.setDisable(true);
        backButton.setVisible(false);
    }

    /**
     * Navigate to the Route Planner home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void homeOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/routePlanner/routePlannerHomePage.fxml", "MABEC - Route Planner - Home Page");
    }

    /**
     * Log out the current user and navigate to the public home page.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, "/fxml/visitorMenu/homePage.fxml", "MABEC - Home Page");
    }

    /**
     * Open the Route Planner NOS logs view.
     *
     * @param actionEvent the action event supplied by the UI
     */
    @FXML
    public void handleNOSLogsButton(ActionEvent actionEvent) {
        uiUtils.loadFXMLScene(actionEvent, "/fxml/routePlanner/routePlannerNOS.fxml", "MABEC - Route Planner - NOS Logs");
    }

    /**
     * Display a transient message in the UI. The message fades out after
     * a short delay.
     *
     * @param message the message text to show
     * @param color the colour to use for the message text
     */
    private void showTemporaryMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setTextFill(color);
        messageLabel.setOpacity(1.0);
        messageLabel.setVisible(true);
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(1), messageLabel);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(ev -> messageLabel.setVisible(false));
            fade.play();
        }));
        delay.play();
    }
}
