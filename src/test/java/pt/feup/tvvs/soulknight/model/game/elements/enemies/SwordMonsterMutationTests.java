package pt.feup.tvvs.soulknight.model.game.elements.enemies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Mutation tests for SwordMonster class.
 * Targets constructor, velocity, movement, and collision logic.
 */
public class SwordMonsterMutationTests {

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
        // SwordMonster sets velocity to (1, 0)
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        Vector velocity = monster.getVelocity();
        
        assertNotNull(velocity);
        assertEquals(1.0, velocity.x(), 0.01, "Initial X velocity should be 1");
        assertEquals(0.0, velocity.y(), 0.01, "Initial Y velocity should be 0");
    }

    @Test
    void testConstructorVelocityNotNegative() {
        // Kills mutation: -1 instead of 1
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        
        assertTrue(monster.getVelocity().x() > 0, "Initial velocity X should be positive");
        assertNotEquals(-1.0, monster.getVelocity().x());
    }

    @Test
    void testConstructorVelocityNotZero() {
        // Kills mutation: 0 instead of 1
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        
        assertNotEquals(0.0, monster.getVelocity().x(), "Initial velocity X should not be 0");
    }

    @Test
    void testConstructorSetsSymbol() {
        SwordMonster monster = new SwordMonster(0, 0, 10, mockScene, 20, new Position(8, 8), 'E');
        assertEquals('E', monster.getChar());
    }

    @Test
    void testConstructorCustomSymbol() {
        SwordMonster monster = new SwordMonster(0, 0, 10, mockScene, 20, new Position(8, 8), 'X');
        assertEquals('X', monster.getChar());
    }

    // ========== getChar Tests ==========

    @Test
    void testGetCharReturnsSymbol() {
        SwordMonster monster = new SwordMonster(0, 0, 10, mockScene, 20, new Position(8, 8), 'S');
        assertEquals('S', monster.getChar());
        assertNotEquals('E', monster.getChar());
    }

    // ========== updatePosition Tests ==========

    @Test
    void testUpdatePositionMovesRight() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        // Velocity is (1, 0), so should move right
        
        Position newPos = monster.updatePosition();
        
        assertEquals(51, newPos.x(), 0.01, "X should increase by velocity");
        assertEquals(50, newPos.y(), 0.01, "Y should stay the same");
    }

    @Test
    void testUpdatePositionWithCustomVelocity() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(5, 3));
        
        Position newPos = monster.updatePosition();
        
        assertEquals(55, newPos.x(), 0.01, "X should be 50 + 5");
        assertEquals(53, newPos.y(), 0.01, "Y should be 50 + 3");
    }

    @Test
    void testUpdatePositionWithNegativeVelocity() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(-3, -2));
        
        Position newPos = monster.updatePosition();
        
        assertEquals(47, newPos.x(), 0.01, "X should be 50 - 3");
        assertEquals(48, newPos.y(), 0.01, "Y should be 50 - 2");
    }

    @Test
    void testUpdatePositionWithZeroVelocity() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(0, 0));
        
        Position newPos = monster.updatePosition();
        
        assertEquals(50, newPos.x(), 0.01, "X should stay the same");
        assertEquals(50, newPos.y(), 0.01, "Y should stay the same");
    }

    // ========== moveMonster Tests ==========

    @Test
    void testMoveMonsterReturnsNewPosition() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        
        Position result = monster.moveMonster();
        
        assertNotNull(result);
        assertEquals(51, result.x(), 0.01);
    }

    @Test
    void testMoveMonsterReturnsUpdatePositionResult() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
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
        
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(-2, 0)); // Moving left
        
        Position newPos = monster.updatePosition();
        
        // After left collision, velocity should be reversed
        assertTrue(monster.getVelocity().x() > 0, "Velocity should reverse after left collision");
    }

    @Test
    void testApplyCollisionsNoLeftCollisionWhenMovingRight() {
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        // Default velocity is (1, 0) - moving right
        
        Position newPos = monster.updatePosition();
        
        // Should not trigger left collision when moving right
        assertEquals(1.0, monster.getVelocity().x(), 0.01, "Velocity should not change when moving right");
    }

    // ========== applyCollisions Tests - Right Collision ==========

    @Test
    void testApplyCollisionsRightCollision() {
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        // Default velocity is (1, 0) - moving right
        
        Position newPos = monster.updatePosition();
        
        // After right collision, velocity should be reversed
        assertTrue(monster.getVelocity().x() < 0, "Velocity should reverse after right collision");
    }

    @Test
    void testApplyCollisionsNoRightCollisionWhenMovingLeft() {
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(-1, 0)); // Moving left
        
        Position newPos = monster.updatePosition();
        
        // Should not trigger right collision when moving left
        assertEquals(-1.0, monster.getVelocity().x(), 0.01, "Velocity should not change when moving left");
    }

    // ========== Collision Velocity Adjustment Tests ==========

    @Test
    void testCollisionReversesVelocity() {
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        double initialVx = monster.getVelocity().x(); // 1.0
        
        monster.updatePosition();
        
        // Velocity should be negated
        assertEquals(-initialVx, monster.getVelocity().x(), 0.01, "Velocity should be negated");
    }

    @Test
    void testMultipleCollisionsReverse() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        
        // First collision: moving right, hits right wall
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        when(mockScene.collidesLeft(any(), any())).thenReturn(false);
        monster.updatePosition();
        assertTrue(monster.getVelocity().x() < 0, "Should move left after right collision");
        
        // Second collision: moving left, hits left wall
        when(mockScene.collidesRight(any(), any())).thenReturn(false);
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        monster.updatePosition();
        assertTrue(monster.getVelocity().x() > 0, "Should move right after left collision");
    }

    // ========== KnightSize in applyCollisions Tests ==========

    @Test
    void testCollisionUsesCorrectKnightSize() {
        // The collision detection uses knightSize = (8, 8)
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        
        // This verifies the scene methods are called (indirectly testing knightSize usage)
        monster.updatePosition();
        
        verify(mockScene, atLeastOnce()).collidesRight(any(), any());
    }

    // ========== Multiple Update Calls Tests ==========

    @Test
    void testMultipleUpdatePositionCalls() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        
        Position pos1 = monster.updatePosition();
        assertEquals(51, pos1.x(), 0.01);
        
        // Note: updatePosition returns new position but doesn't update internal state
        // unless moveMonster is called (which for SwordMonster just returns updatePosition())
        Position pos2 = monster.updatePosition();
        assertEquals(51, pos2.x(), 0.01); // Same because position wasn't updated
    }

    // ========== Velocity Components Tests ==========

    @Test
    void testVelocityYComponentStaysZero() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        
        // Initial Y velocity is 0
        assertEquals(0, monster.getVelocity().y(), 0.01);
        
        // After collision, Y should still be 0
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        monster.updatePosition();
        assertEquals(0, monster.getVelocity().y(), 0.01, "Y velocity should remain 0");
    }

    // ========== Edge Cases ==========

    @Test
    void testMonsterAtOrigin() {
        SwordMonster monster = new SwordMonster(0, 0, 10, mockScene, 20, new Position(8, 8), 'E');
        
        Position newPos = monster.updatePosition();
        assertEquals(1, newPos.x(), 0.01);
        assertEquals(0, newPos.y(), 0.01);
    }

    @Test
    void testMonsterWithLargeVelocity() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(100, 0));
        
        Position newPos = monster.updatePosition();
        assertEquals(150, newPos.x(), 0.01);
    }

    @Test
    void testMonsterWithFractionalVelocity() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(0.5, 0.25));
        
        Position newPos = monster.updatePosition();
        assertEquals(50.5, newPos.x(), 0.01);
        assertEquals(50.25, newPos.y(), 0.01);
    }

    // ========== Scene Interaction Tests ==========

    @Test
    void testSceneCollidesLeftCalled() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(-1, 0));
        
        monster.updatePosition();
        
        verify(mockScene).collidesLeft(any(Position.class), any(Position.class));
    }

    @Test
    void testSceneCollidesRightCalled() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        // Default velocity is (1, 0)
        
        monster.updatePosition();
        
        verify(mockScene).collidesRight(any(Position.class), any(Position.class));
    }
}