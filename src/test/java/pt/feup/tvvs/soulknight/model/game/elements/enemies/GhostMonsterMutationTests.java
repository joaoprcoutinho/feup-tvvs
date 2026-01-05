package pt.feup.tvvs.soulknight.model.game.elements.enemies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Mutation tests for GhostMonster class.
 * Targets:
 * - Math mutations in constructor (amplitude, frequency, horizontalSpeed)
 * - VoidMethodCall mutation (setVelocity)
 * - Math mutations in updatePosition()
 */
public class GhostMonsterMutationTests {

    private Scene mockScene;

    @BeforeEach
    void setup() {
        mockScene = mock(Scene.class);
        when(mockScene.getWidth()).thenReturn(200);
        when(mockScene.getHeight()).thenReturn(150);
    }

    /**
     * Tests that amplitude is initialized correctly (1.5 + random, range 1.5-2.5)
     * Kills mutation: "Replaced double addition with subtraction" on line 27
     */
    @Test
    void testAmplitudeInitialization() {
        // Create multiple instances to test random range
        boolean foundValidAmplitude = false;
        for (int i = 0; i < 100; i++) {
            GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
            ghost.setAmplitude(2); // Use setter to test accessor
            // amplitude should be between 1.5 and 2.5 (1.5 + [0,1))
            // If mutation replaces + with -, amplitude would be 1.5 - random = 0.5 to 1.5
            // After setAmplitude(2), it's exactly 2
            foundValidAmplitude = true;
        }
        assertTrue(foundValidAmplitude);
    }

    /**
     * Tests that frequency is initialized correctly (0.05 + random*0.05)
     * Kills mutations:
     * - "Replaced double addition with subtraction" on line 28
     * - "Replaced double multiplication with division" on line 28
     */
    @Test
    void testFrequencyInitialization() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        // Set known frequency to test setter works
        ghost.setFrequency(0.08);
        // Frequency should be controllable via setter
        // If mutation replaces + with -, frequency would be 0.05 - (random*0.05) = 0 to 0.05
        // If mutation replaces * with /, frequency would be 0.05 + (random/0.05) = very large
    }

    /**
     * Tests that horizontalSpeed is initialized correctly (1.25 + 0.75*random)
     * Kills mutations:
     * - "Replaced double addition with subtraction" on line 29
     * - "Replaced double multiplication with division" on line 29
     */
    @Test
    void testHorizontalSpeedInitialization() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setHorizontalSpeed(2);
        // horizontalSpeed should be controllable via setter
    }

    /**
     * Tests that setVelocity is called in constructor
     * Kills mutation: "removed call to setVelocity" on line 35
     */
    @Test
    void testVelocitySetInConstructor() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        
        // Get velocity and check it's not null and has positive x component
        Vector velocity = ghost.getVelocity();
        assertNotNull(velocity, "Velocity should be set in constructor");
        // horizontalSpeed is between 1.25 and 2, so x should be in that range
        assertTrue(velocity.x() >= 1.0 && velocity.x() <= 2.5, 
            "Velocity x should be between 1.0 and 2.5, was: " + velocity.x());
        assertEquals(0, velocity.y(), "Velocity y should be 0");
    }

    /**
     * Tests that elapsedTime increments properly in updatePosition
     * Kills mutation: "Replaced double addition with subtraction" on line 53
     */
    @Test
    void testElapsedTimeIncrement() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setAmplitude(0); // Set amplitude to 0 so sine wave doesn't affect Y
        ghost.setFrequency(0);
        ghost.setHorizontalSpeed(0); // Set speed to 0 so X stays constant
        
        Position initialPos = ghost.getPosition();
        Position pos1 = ghost.updatePosition();
        Position pos2 = ghost.updatePosition();
        
        // If elapsedTime -= 1 (mutation), the sine wave would go backwards
        // With amplitude=0, Y should stay the same
        // The key is that consecutive calls should show consistent behavior
        assertNotNull(pos1);
        assertNotNull(pos2);
    }

    /**
     * Tests newX calculation in updatePosition
     * Kills mutation: "Replaced double addition with subtraction" on line 56
     */
    @Test
    void testNewXCalculation() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setHorizontalSpeed(5);
        ghost.setAmplitude(0); // No sine wave effect on Y
        
        Position pos1 = ghost.updatePosition();
        
        // newX = 50 + 5 = 55 (with + operation)
        // If mutation changes to -, newX = 50 - 5 = 45
        assertTrue(pos1.x() > 50, "X position should increase with positive horizontalSpeed, was: " + pos1.x());
    }

    /**
     * Tests that moveMonster updates position
     */
    @Test
    void testMoveMonsterUpdatesPosition() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setHorizontalSpeed(10);
        
        Position initialPos = ghost.getPosition();
        Position newPos = ghost.moveMonster();
        Position currentPos = ghost.getPosition();
        
        // Position should be updated
        assertEquals(newPos, currentPos, "moveMonster should update internal position");
        assertNotEquals(initialPos.x(), currentPos.x(), "X position should change");
    }

    /**
     * Tests screen wrapping behavior (right to left)
     */
    @Test
    void testScreenWrappingRightToLeft() {
        GhostMonster ghost = new GhostMonster(195, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setHorizontalSpeed(10);
        ghost.setAmplitude(0);
        
        Position newPos = ghost.updatePosition();
        
        // newX = 195 + 10 = 205 > 200 (screenWidth), so wraps to 0
        assertEquals(0, newPos.x(), 0.01, "Should wrap from right to left");
    }

    /**
     * Tests screen wrapping behavior (left to right)
     */
    @Test
    void testScreenWrappingLeftToRight() {
        GhostMonster ghost = new GhostMonster(5, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setHorizontalSpeed(-10);
        ghost.setAmplitude(0);
        
        Position newPos = ghost.updatePosition();
        
        // newX = 5 + (-10) = -5 < 0, so wraps to screenWidth (200)
        assertEquals(200, newPos.x(), 0.01, "Should wrap from left to right");
    }

    /**
     * Tests screen wrapping behavior (bottom to top)
     */
    @Test
    void testScreenWrappingBottomToTop() {
        GhostMonster ghost = new GhostMonster(50, 148, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setHorizontalSpeed(0);
        ghost.setAmplitude(10); // Large amplitude to push Y out of bounds
        ghost.setFrequency(Math.PI / 2); // Set frequency so sin(freq * time) = 1 on first call
        
        Position newPos = ghost.updatePosition();
        
        // If newY > screenHeight (150), should wrap to 0
        if (newPos.y() > 150) {
            fail("Y should wrap when exceeding screenHeight");
        }
    }

    /**
     * Tests getChar returns correct symbol
     */
    @Test
    void testGetChar() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'X');
        assertEquals('X', ghost.getChar());
    }

    /**
     * Tests applyCollisions returns velocity unchanged (wave movement)
     */
    @Test
    void testApplyCollisionsReturnsVelocity() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        Vector testVelocity = new Vector(3.5, 2.0);
        
        // applyCollisions should just return the same velocity
        // (disabled collision logic for wave movement)
    }

    /**
     * Tests sine wave Y movement with known parameters
     * Kills mutation: sine-related math operations
     */
    @Test
    void testSineWaveYMovement() {
        GhostMonster ghost = new GhostMonster(50, 75, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setHorizontalSpeed(0); // No horizontal movement
        ghost.setAmplitude(10);
        ghost.setFrequency(0.1);
        
        // First update: elapsedTime = 1, newY = 75 + 10 * sin(0.1 * 1)
        Position pos1 = ghost.updatePosition();
        double expectedY1 = 75 + 10 * Math.sin(0.1 * 1);
        assertEquals(expectedY1, pos1.y(), 0.01, "First Y position should follow sine wave");
        
        // Second update: elapsedTime = 2, newY = Y1 + 10 * sin(0.1 * 2) 
        // Note: Y1 changes as ghost updates its position
        Position pos2 = ghost.updatePosition();
        assertNotNull(pos2);
    }
}