package pt.ipp.isep.dei.ui.gui.planner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import pt.ipp.isep.dei.controller.planner.PlannerViewMapaMundiController;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller responsible for rendering the world map and station markers
 * inside the embedded JavaFX canvas.
 *
 * <p>This class displays a world map image and paints station markers at
 * their corresponding projected positions. It supports interactive
 * panning and zooming centred on the mouse pointer, and provides a
 * navigation handler to return to the previous scene. All business data
 * (the list of stations) is supplied by
 * {@link PlannerViewMapaMundiController}; this class is concerned solely
 * with presentation and user interaction.</p>
 *
 */
public class PlannerViewMapaMundiGUI implements Initializable {

    // Zoom
    private static final double INITIAL_SCALE = 1.0;
    private static final double MIN_SCALE     = 1.0;
    private static final double MAX_SCALE     = 15.0;
    private static final double ZOOM_IN_FACTOR  = 1.1;
    private static final double ZOOM_OUT_FACTOR = 0.9;

    // Transform pivots e offsets iniciais
    private static final double INITIAL_TRANSLATE_X = 0.0;
    private static final double INITIAL_TRANSLATE_Y = 0.0;
    private static final double SCALE_PIVOT_X = 0.0;
    private static final double SCALE_PIVOT_Y = 0.0;

    // Desenho das estações
    private static final double BASE_RADIUS = 1.5;
    private static final double DIAMETER_MULTIPLIER = 2.0;
    private static final Color  STATION_FILL_COLOR = Color.RED;

    // Constantes de projeção e conversão lat/lon -> canvas
    private static final double WORLD_LON_MIN   = -180.0;
    private static final double WORLD_LON_MAX   =  180.0;
    private static final double WORLD_LAT_MIN   =  -90.0;
    private static final double WORLD_LAT_MAX   =   90.0;
    private static final double WORLD_LON_RANGE = WORLD_LON_MAX - WORLD_LON_MIN; // 360
    private static final double WORLD_LAT_RANGE = WORLD_LAT_MAX - WORLD_LAT_MIN; // 180

    // Limites de pan
    private static final double MAX_TRANSLATE_X = 0.0;
    private static final double MAX_TRANSLATE_Y = 0.0;

    @FXML private AnchorPane viewport;
    @FXML private ImageView mapaMundiImageView;
    @FXML private Canvas canvas;

    private final UIUtils uiUtils = new UIUtils();
    private final PlannerViewMapaMundiController controller = new PlannerViewMapaMundiController();

    private final Translate translate = new Translate(INITIAL_TRANSLATE_X, INITIAL_TRANSLATE_Y);
    private final Scale scale = new Scale(INITIAL_SCALE, INITIAL_SCALE, SCALE_PIVOT_X, SCALE_PIVOT_Y);
    private double lastMouseSceneX, lastMouseSceneY;

    private String returnScenePath;
    private String returnSceneTitle;

    /**
     * Initialise the view controller.
     *
     * <p>Sets up the transforms, clipping rectangle and event handlers
     * necessary for interactive panning and zooming. This method is
     * invoked by the JavaFX runtime when the FXML view is loaded.</p>
     *
     * @param url not used by this implementation
     * @param resourceBundle not used by this implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTransforms();
        setupClipping();
        setupHandlers();
    }

    /**
     * Receive station data and the return scene information from the
     * caller and request an immediate draw of the stations.
     *
     * @param stations list of stations to render (may be empty)
     * @param returnScene FXML path to the scene used when navigating back
     * @param returnTitle window title to use for the return scene
     */
    public void loadControllerInfo(List<StationEsinf> stations, String returnScene, String returnTitle) {
        this.returnScenePath = returnScene;
        this.returnSceneTitle = returnTitle;

        controller.setStations(stations);
        drawStations();
    }

    /**
     * Paint the station markers onto the canvas using a simple equirectangular
     * projection (longitude → x, latitude → y). Marker size scales with the
     * current zoom so markers remain visible at different zoom levels.
     *
     * <p>If no stations have been supplied the method returns without
     * modifying the canvas.</p>
     */
    private void drawStations() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(STATION_FILL_COLOR);

        List<StationEsinf> estacoes = controller.getStations();
        if (estacoes == null || estacoes.isEmpty()) return;

        double radius = BASE_RADIUS * (1.0 / scale.getX()); // ajusta com zoom
        double diameter = radius * DIAMETER_MULTIPLIER;

        for (StationEsinf s : estacoes) {
            double x = (s.getLongitude() - WORLD_LON_MIN) * (canvas.getWidth()  / WORLD_LON_RANGE);
            double y = (WORLD_LAT_MAX - s.getLatitude()) * (canvas.getHeight() / WORLD_LAT_RANGE);
            gc.fillOval(x - radius, y - radius, diameter, diameter);
        }
    }

    /**
     * Navigate back to the previously supplied scene. The stored return
     * scene path and title are used when loading the previous view.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void backButtonOnAction(ActionEvent event) {
        uiUtils.loadFXMLScene(event, returnScenePath, returnSceneTitle);
    }

    /**
     * Attach the translation and scaling transforms to the map image and
     * the canvas so both respond in unison to panning and zooming.
     */
    private void setupTransforms() {
        mapaMundiImageView.getTransforms().addAll(translate, scale);
        canvas.getTransforms().addAll(translate, scale);
    }

    /**
     * Configure a rectangular clip so that rendering is confined to the
     * visible viewport area.
     */
    private void setupClipping() {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(viewport.widthProperty());
        clip.heightProperty().bind(viewport.heightProperty());
        viewport.setClip(clip);
    }

    /**
     * Register event filters for scroll (zoom) and mouse press/drag (pan).
     * The handlers update transforms and trigger a redraw when appropriate.
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
     * <p>The view scales about the mouse pointer: the code converts the
     * viewport coordinates to image-local coordinates, computes the new
     * scale, adjusts the translation such that the mouse position remains
     * stable and then clamps panning before redrawing the stations.</p>
     *
     * @param e the scroll event describing the gesture
     */
    private void handleZoom(ScrollEvent e) {
        double factor = (e.getDeltaY() > 0) ? ZOOM_IN_FACTOR : ZOOM_OUT_FACTOR;

        double oldScale = scale.getX();
        double newScale = clamp(oldScale * factor, MIN_SCALE, MAX_SCALE);
        if (newScale == oldScale) return;

        Point2D mouseInViewport = new Point2D(e.getX(), e.getY());
        Point2D mouseInContent  = mapaMundiImageView.parentToLocal(mouseInViewport);

        double f = newScale / oldScale;

        translate.setX((translate.getX() - mouseInContent.getX()) * f + mouseInContent.getX());
        translate.setY((translate.getY() - mouseInContent.getY()) * f + mouseInContent.getY());

        scale.setX(newScale);
        scale.setY(newScale);

        limitarPan();
        e.consume();
        drawStations();
    }

    /**
     * Prevent the map being panned entirely out of view by clamping the
     * translation to sensible bounds calculated from the viewport and
     * image sizes.
     */
    private void limitarPan() {
        double imgWidth  = mapaMundiImageView.getBoundsInParent().getWidth();
        double imgHeight = mapaMundiImageView.getBoundsInParent().getHeight();

        double viewportWidth  = viewport.getWidth();
        double viewportHeight = viewport.getHeight();

        double minX = viewportWidth  - imgWidth;
        double minY = viewportHeight - imgHeight;

        translate.setX(clamp(translate.getX(), minX, MAX_TRANSLATE_X));
        translate.setY(clamp(translate.getY(), minY, MAX_TRANSLATE_Y));
    }

    /**
     * Clamp a value between the supplied minimum and maximum.
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
