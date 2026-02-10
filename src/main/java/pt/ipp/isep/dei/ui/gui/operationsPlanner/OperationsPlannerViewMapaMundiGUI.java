package pt.ipp.isep.dei.ui.gui.operationsPlanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import pt.ipp.isep.dei.controller.operationsPlanner.OperationsPlannerViewMapaMundiController;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * JavaFX controller for the "mapa mundi" view used by Operations Planners.
 *
 * <p>This controller renders a world map image together with station
 * markers and a highlighted search point. It supports interactive panning
 * and zooming centred on the mouse pointer. The station list and the
 * search coordinates are supplied by the associated
 * {@link OperationsPlannerViewMapaMundiController} via
 * {@link #loadControllerInfo(List, double, double, String, String, String)}.</p>
 *
 */
public class OperationsPlannerViewMapaMundiGUI implements Initializable {

    private static final double INITIAL_SCALE = 1.0;
    private static final double MIN_SCALE = 1.0;
    private static final double MAX_SCALE = 15.0;
    private static final double ZOOM_IN_FACTOR = 1.1;
    private static final double ZOOM_OUT_FACTOR = 0.9;

    private static final double INITIAL_TRANSLATE_X = 0.0;
    private static final double INITIAL_TRANSLATE_Y = 0.0;
    private static final double SCALE_PIVOT_X = 0.0;
    private static final double SCALE_PIVOT_Y = 0.0;

    private static final double BASE_RADIUS = 1.5;
    private static final double DIAMETER_MULTIPLIER = 2.0;
    private static final Color STATION_FILL_COLOR = Color.RED;
    private static final Color POINT_FILL_COLOR = Color.GREEN;

    private static final double WORLD_LON_MIN = -180.0;
    private static final double WORLD_LON_MAX = 180.0;
    private static final double WORLD_LAT_MIN = -90.0;
    private static final double WORLD_LAT_MAX = 90.0;
    private static final double WORLD_LON_RANGE = WORLD_LON_MAX - WORLD_LON_MIN; // 360
    private static final double WORLD_LAT_RANGE = WORLD_LAT_MAX - WORLD_LAT_MIN; // 180

    private static final double MAX_TRANSLATE_X = 0.0;
    private static final double MAX_TRANSLATE_Y = 0.0;

    @FXML
    private AnchorPane viewport;
    @FXML
    private ImageView mapaMundiImageView;
    @FXML
    private Canvas canvas;

    private final UIUtils uiUtils = new UIUtils();
    private final OperationsPlannerViewMapaMundiController controller = new OperationsPlannerViewMapaMundiController();

    private final Translate translate = new Translate(INITIAL_TRANSLATE_X, INITIAL_TRANSLATE_Y);
    private final Scale scale = new Scale(INITIAL_SCALE, INITIAL_SCALE, SCALE_PIVOT_X, SCALE_PIVOT_Y);
    private double lastMouseSceneX, lastMouseSceneY;

    /**
     * Initialise the view controller.
     *
     * <p>Sets up transforms, clipping and event handlers required for
     * panning and zooming. This method is invoked by the JavaFX runtime
     * when the FXML view is loaded.</p>
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTransforms();
        setupClipping();
        setupHandlers();
    }

    /**
     * Load station data and the search point into the underlying controller
     * and request an immediate redraw of the view.
     *
     * @param stations list of stations to render (may be empty)
     * @param latitude latitude of the search point in degrees
     * @param longitude longitude of the search point in degrees
     * @param latitudeInfo textual latitude used in the search UI
     * @param longitudeInfo textual longitude used in the search UI
     * @param radius textual radius used in the search UI
     */
    public void loadControllerInfo(List<StationEsinf> stations, double latitude, double longitude, String latitudeInfo, String longitudeInfo, String radius) {
        controller.setStations(stations);
        controller.setLatitudePoint(latitude);
        controller.setLongitudePoint(longitude);

        controller.setLatitudeInfo(latitudeInfo);
        controller.setLongitudeInfo(longitudeInfo);
        controller.setRadiusInfo(radius);

        draw();
    }

    /**
     * Render the stations and the search point on the canvas.
     *
     * <p>If no stations have been supplied the method returns without
     * modifying the canvas.</p>
     */
    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(STATION_FILL_COLOR);

        List<StationEsinf> stations = controller.getStations();
        if (stations == null || stations.isEmpty()) return;

        double radius = BASE_RADIUS * (1.0 / scale.getX());
        double diameter = radius * DIAMETER_MULTIPLIER;

        for (StationEsinf s : stations) {
            double x = (s.getLongitude() - WORLD_LON_MIN) * (canvas.getWidth() / WORLD_LON_RANGE);
            double y = (WORLD_LAT_MAX - s.getLatitude()) * (canvas.getHeight() / WORLD_LAT_RANGE);
            gc.fillOval(x - radius, y - radius, diameter, diameter);
        }

        double pointX = (controller.getLongitudePoint() - WORLD_LON_MIN) * (canvas.getWidth() / WORLD_LON_RANGE);
        double pointY = (WORLD_LAT_MAX - controller.getLatitudePoint()) * (canvas.getHeight() / WORLD_LAT_RANGE);

        Color original = (Color) gc.getFill();
        gc.setFill(POINT_FILL_COLOR);
        gc.fillOval(pointX - radius, pointY - radius, diameter, diameter);
        gc.setFill(original);
    }


    /**
     * Handle the "back" button action by returning to the radius search
     * view. The previous search parameters are restored into the search
     * controller prior to navigation so the user sees their original inputs.
     *
     * @param event the action event that triggered navigation
     */
    public void backButtonOnAction(ActionEvent event) {
        FXMLLoader fxmlLoader = uiUtils.loadFXMLScene(event, "/fxml/operationsPlanner/operationsPlannerRadiusSearch.fxml", "MABE - Operations Planner - Radius Search");
        OperationsPlannerRadiusSearchGUI controller = fxmlLoader.getController();
        controller.loadOldControllerInfo(this.controller.getLatitudeInfo(), this.controller.getLongitudeInfo(), this.controller.getRadiusInfo());
    }

    /**
     * Apply the initial translate and scale transforms to the image and canvas.
     */
    private void setupTransforms() {
        mapaMundiImageView.getTransforms().addAll(translate, scale);
        canvas.getTransforms().addAll(translate, scale);
    }

    /**
     * Configure clipping so that the map and canvas are restricted to the
     * visible viewport area.
     */
    private void setupClipping() {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(viewport.widthProperty());
        clip.heightProperty().bind(viewport.heightProperty());
        viewport.setClip(clip);
    }

    /**
     * Register event handlers for zooming and panning interactions.
     *
     * <p>The viewport receives a scroll filter for zoom events and mouse
     * press/drag filters for panning.</p>
     */
    private void setupHandlers() {
        viewport.addEventFilter(ScrollEvent.SCROLL, this::handleZoom);

        viewport.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            lastMouseSceneX = e.getSceneX();
            lastMouseSceneY = e.getSceneY();
        });

        viewport.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            double dx = e.getSceneX() - lastMouseSceneX;
            double dy = e.getSceneY() - lastMouseSceneY;
            translate.setX(translate.getX() + dx);
            translate.setY(translate.getY() + dy);
            lastMouseSceneX = e.getSceneX();
            lastMouseSceneY = e.getSceneY();
        });
    }

    /**
     * Handle zoom gestures originating from the mouse wheel or trackpad.
     *
     * <p>The view scales around the mouse pointer: the code converts the
     * viewport coordinates to the image's local coordinates, computes the
     * new scale, adjusts the translation to keep the mouse position stable
     * and then clamps panning before redrawing.</p>
     *
     * @param e the scroll event describing the gesture
     */
    private void handleZoom(ScrollEvent e) {
        double factor = (e.getDeltaY() > 0) ? ZOOM_IN_FACTOR : ZOOM_OUT_FACTOR;

        double oldScale = scale.getX();
        double newScale = clamp(oldScale * factor, MIN_SCALE, MAX_SCALE);
        if (newScale == oldScale) return;

        Point2D mouseInViewport = new Point2D(e.getX(), e.getY());
        Point2D mouseInContent = mapaMundiImageView.parentToLocal(mouseInViewport);

        double f = newScale / oldScale;

        translate.setX((translate.getX() - mouseInContent.getX()) * f + mouseInContent.getX());
        translate.setY((translate.getY() - mouseInContent.getY()) * f + mouseInContent.getY());

        scale.setX(newScale);
        scale.setY(newScale);

        limitarPan();
        e.consume();
        draw();
    }

    /**
     * Ensure the map cannot be panned outside the bounds of the viewport.
     *
     * <p>The method computes the visible bounds and clamps the translation
     * so that the image remains at least partially visible.</p>
     */
    private void limitarPan() {
        double imgWidth = mapaMundiImageView.getBoundsInParent().getWidth();
        double imgHeight = mapaMundiImageView.getBoundsInParent().getHeight();

        double viewportWidth = viewport.getWidth();
        double viewportHeight = viewport.getHeight();

        double minX = viewportWidth - imgWidth;
        double minY = viewportHeight - imgHeight;

        translate.setX(clamp(translate.getX(), minX, MAX_TRANSLATE_X));
        translate.setY(clamp(translate.getY(), minY, MAX_TRANSLATE_Y));
    }

    /**
     * Utility to clamp a value between the supplied minimum and maximum.
     *
     * @param value value to clamp
     * @param min lower bound
     * @param max upper bound
     * @return the clamped value
     */
    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}