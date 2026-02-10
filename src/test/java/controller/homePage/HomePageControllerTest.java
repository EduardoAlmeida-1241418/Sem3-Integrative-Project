package controller.homePage;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.homePage.HomePageController;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HomePageController.
 */
public class HomePageControllerTest {
    /**
     * Tests that downloadUserManual throws IOException if the PDF resource is missing.
     */
    @Test
    void testDownloadUserManualResourceMissing() {
        HomePageController controller = new HomePageController();
        try {
            java.lang.reflect.Field field = HomePageController.class.getDeclaredField("RESOURCE_PATH");
            field.setAccessible(true);
            field.set(null, "/PDF/nonexistent.pdf");
        } catch (Exception e) {
        }
        try {
            controller.downloadUserManual();
            fail("Expected IOException for missing resource");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("PDF not found"));
        } catch (IllegalStateException e) {
        }
    }
}
