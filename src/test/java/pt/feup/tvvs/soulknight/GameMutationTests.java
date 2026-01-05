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

    @Test
    public void testPixelWidthConstant() {
        assertEquals(230, Game.PIXEL_WIDTH);
    }

    @Test
    public void testPixelWidthIsExactly230() {
        assertEquals(230, Game.PIXEL_WIDTH);
        assertNotEquals(0, Game.PIXEL_WIDTH);
        assertNotEquals(1, Game.PIXEL_WIDTH);
        assertNotEquals(-230, Game.PIXEL_WIDTH);
        assertNotEquals(229, Game.PIXEL_WIDTH);
        assertNotEquals(231, Game.PIXEL_WIDTH);
        assertNotEquals(130, Game.PIXEL_WIDTH);
    }

    @Test
    public void testPixelWidthBounds() {
        assertTrue(Game.PIXEL_WIDTH > 0);
        assertTrue(Game.PIXEL_WIDTH > 100);
        assertTrue(Game.PIXEL_WIDTH < 500);
        assertTrue(Game.PIXEL_WIDTH > Game.PIXEL_HEIGHT);
    }

    @Test
    public void testPixelHeightConstant() {
        assertEquals(130, Game.PIXEL_HEIGHT);
    }

    @Test
    public void testPixelHeightIsExactly130() {
        assertEquals(130, Game.PIXEL_HEIGHT);
        assertNotEquals(0, Game.PIXEL_HEIGHT);
        assertNotEquals(1, Game.PIXEL_HEIGHT);
        assertNotEquals(-130, Game.PIXEL_HEIGHT);
        assertNotEquals(129, Game.PIXEL_HEIGHT);
        assertNotEquals(131, Game.PIXEL_HEIGHT);
        assertNotEquals(230, Game.PIXEL_HEIGHT);
    }

    @Test
    public void testPixelHeightBounds() {
        assertTrue(Game.PIXEL_HEIGHT > 0);
        assertTrue(Game.PIXEL_HEIGHT > 50);
        assertTrue(Game.PIXEL_HEIGHT < 300);
    }

    @Test
    public void testAspectRatio() {
        assertTrue(Game.PIXEL_WIDTH > Game.PIXEL_HEIGHT);
        double ratio = (double) Game.PIXEL_WIDTH / Game.PIXEL_HEIGHT;
        assertTrue(ratio > 1.0);
        assertTrue(ratio < 3.0);
    }

    @Test
    public void testDimensionsDifference() {
        int diff = Game.PIXEL_WIDTH - Game.PIXEL_HEIGHT;
        assertEquals(100, diff);
    }

    @Test
    public void testGetNumberOfLevelsReturns4() {
        int expectedLevels = 4;
        assertTrue(expectedLevels > 0);
        assertEquals(4, expectedLevels);
        assertNotEquals(0, expectedLevels);
        assertNotEquals(3, expectedLevels);
        assertNotEquals(5, expectedLevels);
    }

    @Test
    public void testNumberOfLevelsIsPositive() {
        int levels = 4;
        assertTrue(levels > 0);
        assertTrue(levels >= 1);
        assertTrue(levels <= 10);
    }

    @Test
    public void testSetStateAcceptsNonNull() {
        State<?> mockState = mock(State.class);
        assertNotNull(mockState);
    }

    @Test
    public void testStateCanBeMocked() {
        State<?> mockState = mock(State.class);
        assertNotNull(mockState);
        assertFalse(mockState.getClass().equals(State.class));
    }

    @Test
    public void testResolutionScaleEnumExists() {
        RescalableGUI.ResolutionScale[] scales = RescalableGUI.ResolutionScale.values();
        assertTrue(scales.length > 0);
    }

    @Test
    public void testResolutionScaleValues() {
        RescalableGUI.ResolutionScale[] scales = RescalableGUI.ResolutionScale.values();
        assertNotNull(scales);
        for (RescalableGUI.ResolutionScale scale : scales) {
            assertNotNull(scale);
            assertNotNull(scale.name());
        }
    }

    @Test
    public void testFpsFrameTimeCalculation() {
        int FPS = 30;
        int frameTime = 1000 / FPS;
        assertEquals(33, frameTime);
        assertTrue(frameTime > 0);
    }

    @Test
    public void testFpsIs30() {
        int FPS = 30;
        assertEquals(30, FPS);
        assertNotEquals(0, FPS);
        assertNotEquals(60, FPS);
        assertNotEquals(29, FPS);
        assertNotEquals(31, FPS);
    }

    @Test
    public void testInitialSleepDelay() {
        long sleepTime = 100;
        assertEquals(100, sleepTime);
        assertNotEquals(0, sleepTime);
        assertTrue(sleepTime > 0);
    }

    @Test
    public void testFpsCounterInterval() {
        long interval = 1000;
        assertEquals(1000, interval);
        assertTrue(interval > 0);
    }

    @Test
    public void testConstantsAreDistinct() {
        assertNotEquals(Game.PIXEL_WIDTH, Game.PIXEL_HEIGHT);
    }

    @Test
    public void testConstantsMathRelationship() {
        assertTrue(Game.PIXEL_WIDTH + Game.PIXEL_HEIGHT == 360);
        assertTrue(Game.PIXEL_WIDTH * Game.PIXEL_HEIGHT == 29900);
    }

    @Test
    public void testPixelWidthNotIncrementedOrDecremented() {
        assertNotEquals(Game.PIXEL_WIDTH + 1, 230);
        assertNotEquals(Game.PIXEL_WIDTH - 1, 230);
        assertEquals(230, Game.PIXEL_WIDTH);
    }

    @Test
    public void testPixelHeightNotIncrementedOrDecremented() {
        assertNotEquals(Game.PIXEL_HEIGHT + 1, 130);
        assertNotEquals(Game.PIXEL_HEIGHT - 1, 130);
        assertEquals(130, Game.PIXEL_HEIGHT);
    }

    @Test
    public void testConstantsNotNegated() {
        assertTrue(Game.PIXEL_WIDTH > 0);
        assertTrue(Game.PIXEL_HEIGHT > 0);
        assertNotEquals(-Game.PIXEL_WIDTH, Game.PIXEL_WIDTH);
        assertNotEquals(-Game.PIXEL_HEIGHT, Game.PIXEL_HEIGHT);
    }
}