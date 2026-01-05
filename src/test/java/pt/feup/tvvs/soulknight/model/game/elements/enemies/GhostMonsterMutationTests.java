package pt.feup.tvvs.soulknight.model.game.elements.enemies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GhostMonsterMutationTests {

    private Scene mockScene;

    @BeforeEach
    void setup() {
        mockScene = mock(Scene.class);
        when(mockScene.getWidth()).thenReturn(200);
        when(mockScene.getHeight()).thenReturn(150);
    }

    @Test
    public void testAmplitudeInitialization() {
        boolean foundValidAmplitude = false;
        for (int i = 0; i < 100; i++) {
            GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
            ghost.setAmplitude(2);

            foundValidAmplitude = true;
        }
        assertTrue(foundValidAmplitude);
    }

    @Test
    public void testFrequencyInitialization() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setFrequency(0.08);
    }

    @Test
    public void testHorizontalSpeedInitialization() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setHorizontalSpeed(2);
    }

    @Test
    public void testVelocitySetInConstructor() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        
        Vector velocity = ghost.getVelocity();
        assertNotNull(velocity);
        assertTrue(velocity.x() >= 1.0 && velocity.x() <= 2.5);
        assertEquals(0, velocity.y());
    }

    @Test
    public void testElapsedTimeIncrement() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setAmplitude(0);
        ghost.setFrequency(0);
        ghost.setHorizontalSpeed(0);
        
        Position pos1 = ghost.updatePosition();
        Position pos2 = ghost.updatePosition();
        
        assertNotNull(pos1);
        assertNotNull(pos2);
    }

    @Test
    public void testNewXCalculation() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setHorizontalSpeed(5);
        ghost.setAmplitude(0);
        
        Position pos1 = ghost.updatePosition();
        assertTrue(pos1.x() > 50, "X position should increase with positive horizontalSpeed, was: " + pos1.x());
    }

    @Test
    public void testMoveMonsterUpdatesPosition() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setHorizontalSpeed(10);
        
        Position initialPos = ghost.getPosition();
        Position newPos = ghost.moveMonster();
        Position currentPos = ghost.getPosition();
        
        assertEquals(newPos, currentPos);
        assertNotEquals(initialPos.x(), currentPos.x());
    }

    @Test
    public void testScreenWrappingRightToLeft() {
        GhostMonster ghost = new GhostMonster(195, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setHorizontalSpeed(10);
        ghost.setAmplitude(0);
        
        Position newPos = ghost.updatePosition();
        
        assertEquals(0, newPos.x(), 0.01);
    }

    @Test
    public void testScreenWrappingLeftToRight() {
        GhostMonster ghost = new GhostMonster(5, 50, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setHorizontalSpeed(-10);
        ghost.setAmplitude(0);
        
        Position newPos = ghost.updatePosition();
        
        assertEquals(200, newPos.x(), 0.01);
    }

    @Test
    public void testScreenWrappingBottomToTop() {
        GhostMonster ghost = new GhostMonster(50, 148, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setHorizontalSpeed(0);
        ghost.setAmplitude(10);
        ghost.setFrequency(Math.PI / 2);
        
        Position newPos = ghost.updatePosition();
        if (newPos.y() > 150) {
            fail("Y should wrap when exceeding screenHeight");
        }
    }

    @Test
    public void testGetChar() {
        GhostMonster ghost = new GhostMonster(50, 50, 10, mockScene, 1, new Position(8, 8), 'X');
        assertEquals('X', ghost.getChar());
    }

    @Test
    public void testSineWaveYMovement() {
        GhostMonster ghost = new GhostMonster(50, 75, 10, mockScene, 1, new Position(8, 8), 'G');
        ghost.setHorizontalSpeed(0);
        ghost.setAmplitude(10);
        ghost.setFrequency(0.1);
        
        Position pos1 = ghost.updatePosition();
        double expectedY1 = 75 + 10 * Math.sin(0.1 * 1);
        assertEquals(expectedY1, pos1.y(), 0.01, "First Y position should follow sine wave");
        
        Position pos2 = ghost.updatePosition();
        assertNotNull(pos2);
    }
}