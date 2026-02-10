package pt.ipp.isep.dei.controller.homePage;

import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Controller responsible for handling actions on the home page.
 * <p>
 * Provides functionality to allow users to download the application's user manual (PDF file)
 * from the resources directory.
 */
public class HomePageController {

    /** Path to the user manual resource inside the classpath. */
    private static final String RESOURCE_PATH = "/PDF/User_Manual.pdf";

    /** Default filename suggested when saving the user manual. */
    private static final String DEFAULT_FILENAME = "User_Manual.pdf";

    /**
     * Downloads the user manual PDF from the classpath and allows the user to save it locally.
     *
     * @return the saved {@link File} if successful, or {@code null} if the user cancels the save dialog
     * @throws IOException if the PDF resource is not found or a file operation fails
     */
    public File downloadUserManual() throws IOException {
        try (InputStream in = HomePageController.class.getResourceAsStream(RESOURCE_PATH)) {
            if (in == null) {
                throw new IOException("PDF not found in classpath: " + RESOURCE_PATH);
            }

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save PDF");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf"));
            chooser.setInitialFileName(DEFAULT_FILENAME);

            File destino = chooser.showSaveDialog(null);
            if (destino == null) return null;

            Files.copy(in, destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return destino;
        }
    }
}
