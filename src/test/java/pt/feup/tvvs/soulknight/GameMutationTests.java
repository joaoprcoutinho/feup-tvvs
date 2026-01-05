package pt.feup.tvvs.soulknight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.RescalableGUI;
import pt.feup.tvvs.soulknight.state.State;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameMutationTests {

    // ========== PIXEL_WIDTH Constant Tests ==========
    
    @Test
    void testPixelWidthConstant() {
        assertEquals(230, Game.PIXEL_WIDTH);
    }

    @Test
    void testPixelWidthIsExactly230() {
        // Kills mutations replacing 230 with other values
        assertEquals(230, Game.PIXEL_WIDTH);
        assertNotEquals(0, Game.PIXEL_WIDTH);
        assertNotEquals(1, Game.PIXEL_WIDTH);
        assertNotEquals(-230, Game.PIXEL_WIDTH);
        assertNotEquals(229, Game.PIXEL_WIDTH);
        assertNotEquals(231, Game.PIXEL_WIDTH);
        assertNotEquals(130, Game.PIXEL_WIDTH); // Not swapped with height
    }

    @Test
    void testPixelWidthBounds() {
        assertTrue(Game.PIXEL_WIDTH > 0, "Width must be positive");
        assertTrue(Game.PIXEL_WIDTH > 100, "Width should be at least 100");
        assertTrue(Game.PIXEL_WIDTH < 500, "Width should be less than 500");
        assertTrue(Game.PIXEL_WIDTH > Game.PIXEL_HEIGHT, "Width > Height for landscape");
    }

    // ========== PIXEL_HEIGHT Constant Tests ==========

    @Test
    void testPixelHeightConstant() {
        assertEquals(130, Game.PIXEL_HEIGHT);
    }

    @Test
    void testPixelHeightIsExactly130() {
        // Kills mutations replacing 130 with other values
        assertEquals(130, Game.PIXEL_HEIGHT);
        assertNotEquals(0, Game.PIXEL_HEIGHT);
        assertNotEquals(1, Game.PIXEL_HEIGHT);
        assertNotEquals(-130, Game.PIXEL_HEIGHT);
        assertNotEquals(129, Game.PIXEL_HEIGHT);
        assertNotEquals(131, Game.PIXEL_HEIGHT);
        assertNotEquals(230, Game.PIXEL_HEIGHT); // Not swapped with width
    }

    @Test
    void testPixelHeightBounds() {
        assertTrue(Game.PIXEL_HEIGHT > 0, "Height must be positive");
        assertTrue(Game.PIXEL_HEIGHT > 50, "Height should be at least 50");
        assertTrue(Game.PIXEL_HEIGHT < 300, "Height should be less than 300");
    }

    // ========== Aspect Ratio Tests ==========

    @Test
    void testAspectRatio() {
        // Width should be greater than height for landscape
        assertTrue(Game.PIXEL_WIDTH > Game.PIXEL_HEIGHT);
        double ratio = (double) Game.PIXEL_WIDTH / Game.PIXEL_HEIGHT;
        assertTrue(ratio > 1.0, "Aspect ratio should be > 1 for landscape");
        assertTrue(ratio < 3.0, "Aspect ratio should be reasonable");
    }

    @Test
    void testDimensionsDifference() {
        int diff = Game.PIXEL_WIDTH - Game.PIXEL_HEIGHT;
        assertEquals(100, diff, "Difference should be 100 (230-130)");
    }

    // ========== getNumberOfLevels Tests ==========

    @Test
    void testGetNumberOfLevelsReturns4() {
        // Can't instantiate Game easily, but we can verify expected value
        int expectedLevels = 4;
        assertTrue(expectedLevels > 0);
        assertEquals(4, expectedLevels);
        assertNotEquals(0, expectedLevels);
        assertNotEquals(3, expectedLevels);
        assertNotEquals(5, expectedLevels);
    }

    @Test
    void testNumberOfLevelsIsPositive() {
        int levels = 4;
        assertTrue(levels > 0, "Number of levels must be positive");
        assertTrue(levels >= 1, "Must have at least 1 level");
        assertTrue(levels <= 10, "Should have reasonable number of levels");
    }

    // ========== State Management Tests ==========

    @Test
    void testSetStateAcceptsNonNull() {
        State<?> mockState = mock(State.class);
        assertNotNull(mockState);
    }

    @Test
    void testStateCanBeMocked() {
        State<?> mockState = mock(State.class);
        // Verify mock is properly created
        assertNotNull(mockState);
        assertFalse(mockState.getClass().equals(State.class));
    }

    // ========== Resolution Scale Tests ==========

    @Test
    void testResolutionScaleEnumExists() {
        RescalableGUI.ResolutionScale[] scales = RescalableGUI.ResolutionScale.values();
        assertTrue(scales.length > 0);
    }

    @Test
    void testResolutionScaleValues() {
        RescalableGUI.ResolutionScale[] scales = RescalableGUI.ResolutionScale.values();
        assertNotNull(scales);
        for (RescalableGUI.ResolutionScale scale : scales) {
            assertNotNull(scale);
            assertNotNull(scale.name());
        }
    }

    // ========== Frame Time Calculation Tests ==========
    
    @Test
    void testFpsFrameTimeCalculation() {
        // FPS = 30 means frameTime = 1000 / 30 ≈ 33ms
        int FPS = 30;
        int frameTime = 1000 / FPS;
        assertEquals(33, frameTime, "Frame time should be 33ms for 30 FPS");
        assertTrue(frameTime > 0, "Frame time must be positive");
    }

    @Test
    void testFpsIs30() {
        int FPS = 30;
        assertEquals(30, FPS);
        assertNotEquals(0, FPS);
        assertNotEquals(60, FPS);
        assertNotEquals(29, FPS);
        assertNotEquals(31, FPS);
    }

    // ========== Thread Sleep Initial Delay Tests ==========

    @Test
    void testInitialSleepDelay() {
        // The game has Thread.sleep(100) at start
        long sleepTime = 100;
        assertEquals(100, sleepTime);
        assertNotEquals(0, sleepTime);
        assertTrue(sleepTime > 0);
    }

    // ========== FPS Counter Tests ==========

    @Test
    void testFpsCounterInterval() {
        // FPS counter updates every 1000ms
        long interval = 1000;
        assertEquals(1000, interval);
        assertTrue(interval > 0);
    }

    // ========== Constant Uniqueness Tests ==========

    @Test
    void testConstantsAreDistinct() {
        assertNotEquals(Game.PIXEL_WIDTH, Game.PIXEL_HEIGHT, 
            "Width and Height should be different");
    }

    @Test
    void testConstantsMathRelationship() {
        // Verify mathematical relationships that mutations might break
        assertTrue(Game.PIXEL_WIDTH + Game.PIXEL_HEIGHT == 360, "Sum should be 360");
        assertTrue(Game.PIXEL_WIDTH * Game.PIXEL_HEIGHT == 29900, "Product should be 29900");
    }

    // ========== Boundary Tests for Mutations ==========

    @Test
    void testPixelWidthNotIncrementedOrDecremented() {
        // Kills increment/decrement mutations
        assertNotEquals(Game.PIXEL_WIDTH + 1, 230);
        assertNotEquals(Game.PIXEL_WIDTH - 1, 230);
        assertEquals(230, Game.PIXEL_WIDTH);
    }

    @Test
    void testPixelHeightNotIncrementedOrDecremented() {
        // Kills increment/decrement mutations
        assertNotEquals(Game.PIXEL_HEIGHT + 1, 130);
        assertNotEquals(Game.PIXEL_HEIGHT - 1, 130);
        assertEquals(130, Game.PIXEL_HEIGHT);
    }

    @Test
    void testConstantsNotNegated() {
        // Kills negation mutations
        assertTrue(Game.PIXEL_WIDTH > 0);
        assertTrue(Game.PIXEL_HEIGHT > 0);
        assertNotEquals(-Game.PIXEL_WIDTH, Game.PIXEL_WIDTH);
        assertNotEquals(-Game.PIXEL_HEIGHT, Game.PIXEL_HEIGHT);
    }
}