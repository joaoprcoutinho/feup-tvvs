package pt.feup.tvvs.soulknight.gui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RescalableGUIMutationTests {

    // Test that getWidth returns correct value (kills "replaced int return with 0" mutation)
    @Test
    void testResolutionScaleGetWidthReturnsCorrectValue() {
        RescalableGUI.ResolutionScale scale = RescalableGUI.ResolutionScale.WXGA;
        
        int width = scale.getWidth();
        
        assertEquals(1280, width, "Width should be 1280 for WXGA resolution");
        assertNotEquals(0, width, "Width should not be 0");
    }

    // Test that getHeight returns correct value (kills "replaced int return with 0" mutation)
    @Test
    void testResolutionScaleGetHeightReturnsCorrectValue() {
        RescalableGUI.ResolutionScale scale = RescalableGUI.ResolutionScale.WXGA;
        
        int height = scale.getHeight();
        
        assertEquals(720, height, "Height should be 720 for WXGA resolution");
        assertNotEquals(0, height, "Height should not be 0");
    }

    // Test width and height together
    @Test
    void testResolutionScaleDimensionsAreCorrect() {
        RescalableGUI.ResolutionScale scale = RescalableGUI.ResolutionScale.WXGA;
        
        assertTrue(scale.getWidth() > 0, "Width must be positive");
        assertTrue(scale.getHeight() > 0, "Height must be positive");
        assertEquals(1280, scale.getWidth());
        assertEquals(720, scale.getHeight());
    }

    // Test that width and height are different from zero
    @Test
    void testResolutionScaleWidthIsNotZero() {
        assertTrue(RescalableGUI.ResolutionScale.WXGA.getWidth() != 0);
    }

    @Test
    void testResolutionScaleHeightIsNotZero() {
        assertTrue(RescalableGUI.ResolutionScale.WXGA.getHeight() != 0);
    }
}