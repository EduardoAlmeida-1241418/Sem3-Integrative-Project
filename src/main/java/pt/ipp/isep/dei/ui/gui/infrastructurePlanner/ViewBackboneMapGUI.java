package pt.ipp.isep.dei.ui.gui.infrastructurePlanner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * JavaFX controller that displays a backbone map SVG inside a {@link WebView}.
 *
 * <p>This controller reads the user's choice (adapted or original backbone)
 * and a base filename from {@link BackboneViewContext} and loads the
 * corresponding SVG file into the embedded web view. Simple zoom controls
 * and a navigation button are provided. The class is responsible only for
 * presentation and view interaction; it does not perform backbone
 * generation or other business logic.</p>
 */
public class ViewBackboneMapGUI implements Initializable {

    @FXML
    private WebView webView;

    @FXML
    private Button backButton;

    @FXML
    private Button zoomInButton;

    @FXML
    private Button zoomOutButton;

    private final UIUtils uiUtils = new UIUtils();

    private static final double ZOOM_STEP = 0.1;
    private static final double MIN_ZOOM = 0.1;
    private static final double MAX_ZOOM = 3.0;

    /**
     * Initialise the view controller.
     *
     * <p>Sets an initial zoom level, determines whether the adapted or
     * original backbone should be displayed, constructs the expected SVG
     * path and loads the file into the {@link WebView}. If the SVG file is
     * not present the method logs an error to standard error and returns
     * without further action.</p>
     *
     * @param url not used by this implementation
     * @param resourceBundle not used by this implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Zoom inicial
        webView.setZoom(0.35);

        boolean adapted = BackboneViewContext.isAdapted();
        String baseFilename = BackboneViewContext.getBaseFilename();

        String variantDir = adapted ? "Adapted" : "Original";

        String svgPath =
                "output/ESINF/Specific Graphs/"
                        + variantDir + "/"
                        + baseFilename + "_mst.svg";

        File svgFile = new File(svgPath);

        if (!svgFile.exists()) {
            System.err.println("SVG not found: " + svgFile.getAbsolutePath());
            return;
        }

        WebEngine engine = webView.getEngine();
        engine.load(svgFile.toURI().toString());


    }

    // =========================
    // Botões de zoom
    // =========================


    /**
     * Increase the current web view zoom by 25% (clamped to a sensible
     * maximum). This method is invoked by the associated FXML button.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void handleZoomIn(ActionEvent event) {
        double zoom = webView.getZoom();
        zoom = zoom * 1.25;   // aumento de 25%
        if (zoom > 5.0) zoom = 5.0;
        webView.setZoom(zoom);
    }

    /**
     * Decrease the current web view zoom by approximately 20% (clamped to a
     * sensible minimum). This method is invoked by the associated FXML
     * button.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void handleZoomOut(ActionEvent event) {
        double zoom = webView.getZoom();
        zoom = zoom / 1.25;   // redução proporcional
        if (zoom < 0.1) zoom = 0.1;
        webView.setZoom(zoom);
    }


    // =========================
    // Botão Back
    // =========================

    /**
     * Navigate back to the railway baseline cost view. Navigation is
     * delegated to {@link UIUtils#loadFXMLScene}.
     *
     * @param event the action event supplied by the UI
     */
    @FXML
    public void handleBack(ActionEvent event) {
        uiUtils.loadFXMLScene(
                event,
                "/fxml/infrastructurePlanner/railwayBaselineCost.fxml",
                "MABEC - Infrastructure Planner - Baseline Cost"
        );
    }
}
