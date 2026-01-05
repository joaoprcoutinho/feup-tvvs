package pt.feup.tvvs.soulknight.model.game.elements.enemies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Mutation tests for PurpleMonster class.
 * Targets constructor, velocity, movement, and collision logic.
 */
public class PurpleMonsterMutationTests {

    private Scene mockScene;

    @BeforeEach
    void setUp() {
        mockScene = mock(Scene.class);
        when(mockScene.getWidth()).thenReturn(200);
        when(mockScene.getHeight()).thenReturn(150);
        when(mockScene.collidesLeft(any(), any())).thenReturn(false);
        when(mockScene.collidesRight(any(), any())).thenReturn(false);
    }

    // ========== Constructor Tests ==========

    @Test
    void testConstructorSetsInitialVelocity() {
        // PurpleMonster sets velocity to (-1.5, 0)
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        Vector velocity = monster.getVelocity();
        
        assertNotNull(velocity);
        assertEquals(-1.5, velocity.x(), 0.01, "Initial X velocity should be -1.5");
        assertEquals(0.0, velocity.y(), 0.01, "Initial Y velocity should be 0");
    }

    @Test
    void testConstructorVelocityIsNegative() {
        // Kills mutation: 1.5 instead of -1.5
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        
        assertTrue(monster.getVelocity().x() < 0, "Initial velocity X should be negative");
        assertNotEquals(1.5, monster.getVelocity().x());
    }

    @Test
    void testConstructorVelocityNotZero() {
        // Kills mutation: 0 instead of -1.5
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        
        assertNotEquals(0.0, monster.getVelocity().x(), "Initial velocity X should not be 0");
    }

    @Test
    void testConstructorVelocityExactValue() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        
        assertEquals(-1.5, monster.getVelocity().x(), 0.001);
        assertNotEquals(-1.0, monster.getVelocity().x());
        assertNotEquals(-2.0, monster.getVelocity().x());
    }

    @Test
    void testConstructorSetsSymbol() {
        PurpleMonster monster = new PurpleMonster(0, 0, 25, mockScene, 5, new Position(8, 9), 'l');
        assertEquals('l', monster.getChar());
    }

    @Test
    void testConstructorCustomSymbol() {
        PurpleMonster monster = new PurpleMonster(0, 0, 25, mockScene, 5, new Position(8, 9), 'P');
        assertEquals('P', monster.getChar());
    }

    // ========== getChar Tests ==========

    @Test
    void testGetCharReturnsSymbol() {
        PurpleMonster monster = new PurpleMonster(0, 0, 25, mockScene, 5, new Position(8, 9), 'Q');
        assertEquals('Q', monster.getChar());
        assertNotEquals('l', monster.getChar());
    }

    // ========== updatePosition Tests ==========

    @Test
    void testUpdatePositionMovesLeft() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        // Velocity is (-1.5, 0), so should move left
        
        Position newPos = monster.updatePosition();
        
        assertEquals(48.5, newPos.x(), 0.01, "X should decrease by velocity");
        assertEquals(50, newPos.y(), 0.01, "Y should stay the same");
    }

    @Test
    void testUpdatePositionWithCustomVelocity() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(3, 2));
        
        Position newPos = monster.updatePosition();
        
        assertEquals(53, newPos.x(), 0.01, "X should be 50 + 3");
        assertEquals(52, newPos.y(), 0.01, "Y should be 50 + 2");
    }

    @Test
    void testUpdatePositionWithZeroVelocity() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(0, 0));
        
        Position newPos = monster.updatePosition();
        
        assertEquals(50, newPos.x(), 0.01, "X should stay the same");
        assertEquals(50, newPos.y(), 0.01, "Y should stay the same");
    }

    @Test
    void testUpdatePositionWithPositiveVelocity() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(5, 0));
        
        Position newPos = monster.updatePosition();
        
        assertEquals(55, newPos.x(), 0.01);
    }

    // ========== moveMonster Tests ==========

    @Test
    void testMoveMonsterReturnsNewPosition() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        
        Position result = monster.moveMonster();
        
        assertNotNull(result);
        assertEquals(48.5, result.x(), 0.01);
    }

    @Test
    void testMoveMonsterReturnsUpdatePositionResult() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(10, 5));
        
        Position moveResult = monster.moveMonster();
        
        // moveMonster returns updatePosition() result
        assertEquals(60, moveResult.x(), 0.01);
        assertEquals(55, moveResult.y(), 0.01);
    }

    // ========== applyCollisions Tests - Left Collision ==========

    @Test
    void testApplyCollisionsLeftCollision() {
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        // Default velocity is (-1.5, 0) - moving left
        
        Position newPos = monster.updatePosition();
        
        // After left collision, velocity should be reversed
        assertTrue(monster.getVelocity().x() > 0, "Velocity should reverse after left collision");
    }

    @Test
    void testApplyCollisionsNoLeftCollisionWhenMovingRight() {
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(1.5, 0)); // Moving right
        
        Position newPos = monster.updatePosition();
        
        // Should not trigger left collision when moving right
        assertEquals(1.5, monster.getVelocity().x(), 0.01, "Velocity should not change when moving right");
    }

    // ========== applyCollisions Tests - Right Collision ==========

    @Test
    void testApplyCollisionsRightCollision() {
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(1.5, 0)); // Moving right
        
        Position newPos = monster.updatePosition();
        
        // After right collision, velocity should be reversed
        assertTrue(monster.getVelocity().x() < 0, "Velocity should reverse after right collision");
    }

    @Test
    void testApplyCollisionsNoRightCollisionWhenMovingLeft() {
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        // Default velocity is (-1.5, 0) - moving left
        
        Position newPos = monster.updatePosition();
        
        // Should not trigger right collision when moving left
        assertEquals(-1.5, monster.getVelocity().x(), 0.01, "Velocity should not change when moving left");
    }

    // ========== Collision Velocity Reversal Tests ==========

    @Test
    void testLeftCollisionReversesVelocity() {
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        double initialVx = monster.getVelocity().x(); // -1.5
        
        monster.updatePosition();
        
        // Velocity should be negated (becomes 1.5)
        assertEquals(-initialVx, monster.getVelocity().x(), 0.01, "Velocity should be negated");
        assertEquals(1.5, monster.getVelocity().x(), 0.01);
    }

    @Test
    void testRightCollisionReversesVelocity() {
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(1.5, 0));
        
        monster.updatePosition();
        
        assertEquals(-1.5, monster.getVelocity().x(), 0.01, "Velocity should be negated");
    }

    @Test
    void testMultipleCollisionsBounce() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        
        // First collision: moving left, hits left wall
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        when(mockScene.collidesRight(any(), any())).thenReturn(false);
        monster.updatePosition();
        assertTrue(monster.getVelocity().x() > 0, "Should move right after left collision");
        
        // Second collision: moving right, hits right wall
        when(mockScene.collidesLeft(any(), any())).thenReturn(false);
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        monster.updatePosition();
        assertTrue(monster.getVelocity().x() < 0, "Should move left after right collision");
    }

    // ========== KnightSize in applyCollisions Tests ==========

    @Test
    void testCollisionUsesKnightSize10x9() {
        // PurpleMonster uses knightSize = (10, 9) in applyCollisions
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        
        monster.updatePosition();
        
        // Verify scene collision methods are called
        verify(mockScene).collidesLeft(any(Position.class), any(Position.class));
    }

    // ========== Velocity Components Tests ==========

    @Test
    void testVelocityYComponentStaysZero() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        
        // Initial Y velocity is 0
        assertEquals(0, monster.getVelocity().y(), 0.01);
        
        // After collision, Y should still be 0
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        monster.updatePosition();
        assertEquals(0, monster.getVelocity().y(), 0.01, "Y velocity should remain 0");
    }

    // ========== Position Updates Based on Resolved Velocity ==========

    @Test
    void testPositionUpdatedWithResolvedVelocity() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        
        // No collision, full velocity applied
        Position pos = monster.updatePosition();
        assertEquals(48.5, pos.x(), 0.01);
    }

    @Test
    void testPositionAfterCollisionAdjustment() {
        // When collision happens, vx is adjusted by +1 (for left collision)
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        // Velocity is -1.5, after collision adjustment: min(-1.5 + 1, 0) = -0.5
        
        Position pos = monster.updatePosition();
        // New position uses adjusted velocity
        assertEquals(49.5, pos.x(), 0.01, "Position should use adjusted velocity");
    }

    // ========== Edge Cases ==========

    @Test
    void testMonsterAtOrigin() {
        PurpleMonster monster = new PurpleMonster(0, 0, 25, mockScene, 5, new Position(8, 9), 'l');
        
        Position newPos = monster.updatePosition();
        assertEquals(-1.5, newPos.x(), 0.01);
        assertEquals(0, newPos.y(), 0.01);
    }

    @Test
    void testMonsterWithLargeVelocity() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(-100, 0));
        
        Position newPos = monster.updatePosition();
        assertEquals(-50, newPos.x(), 0.01);
    }

    @Test
    void testMonsterWithFractionalVelocity() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(-0.5, -0.25));
        
        Position newPos = monster.updatePosition();
        assertEquals(49.5, newPos.x(), 0.01);
        assertEquals(49.75, newPos.y(), 0.01);
    }

    // ========== Math.min in applyCollisions Tests ==========

    @Test
    void testMinFunctionLeftCollision() {
        // vx = Math.min(vx + 1, 0) for left collision
        // If vx = -1.5, then vx + 1 = -0.5, min(-0.5, 0) = -0.5
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        Position pos = monster.updatePosition();
        
        // Position should reflect adjusted velocity
        assertTrue(pos.x() < 50, "Position should decrease");
        assertTrue(pos.x() > 48.5, "Position should decrease less due to collision");
    }

    @Test
    void testMinFunctionRightCollision() {
        // vx = Math.min(vx - 1, 0) for right collision
        // If vx = 1.5, then vx - 1 = 0.5, min(0.5, 0) = 0
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(1.5, 0));
        Position pos = monster.updatePosition();
        
        // With vx = 1.5, after collision: min(1.5 - 1, 0) = min(0.5, 0) = 0
        assertEquals(50, pos.x(), 0.01, "Position should not change with zeroed velocity");
    }

    // ========== Scene Interaction Tests ==========

    @Test
    void testSceneCollidesLeftCalled() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        // Default velocity is (-1.5, 0)
        
        monster.updatePosition();
        
        verify(mockScene).collidesLeft(any(Position.class), any(Position.class));
    }

    @Test
    void testSceneCollidesRightCalled() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(1.5, 0));
        
        monster.updatePosition();
        
        verify(mockScene).collidesRight(any(Position.class), any(Position.class));
    }
}