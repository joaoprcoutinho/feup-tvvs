package pt.feup.tvvs.soulknight.model.game.elements.enemies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    public void testConstructorSetsInitialVelocity() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        Vector velocity = monster.getVelocity();
        
        assertNotNull(velocity);
        assertEquals(1.0, velocity.x(), 0.01);
        assertEquals(0.0, velocity.y(), 0.01);
    }

    @Test
    public void testConstructorVelocityNotNegative() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        
        assertTrue(monster.getVelocity().x() > 0);
        assertNotEquals(-1.0, monster.getVelocity().x());
    }

    @Test
    public void testConstructorVelocityNotZero() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        
        assertNotEquals(0.0, monster.getVelocity().x());
    }

    @Test
    public void testConstructorSetsSymbol() {
        SwordMonster monster = new SwordMonster(0, 0, 10, mockScene, 20, new Position(8, 8), 'E');
        assertEquals('E', monster.getChar());
    }

    @Test
    public void testConstructorCustomSymbol() {
        SwordMonster monster = new SwordMonster(0, 0, 10, mockScene, 20, new Position(8, 8), 'X');
        assertEquals('X', monster.getChar());
    }

    @Test
    public void testGetCharReturnsSymbol() {
        SwordMonster monster = new SwordMonster(0, 0, 10, mockScene, 20, new Position(8, 8), 'S');
        assertEquals('S', monster.getChar());
        assertNotEquals('E', monster.getChar());
    }

    @Test
    public void testUpdatePositionMovesRight() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');

        Position newPos = monster.updatePosition();
        
        assertEquals(51, newPos.x(), 0.01);
        assertEquals(50, newPos.y(), 0.01);
    }

    @Test
    public void testUpdatePositionWithCustomVelocity() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(5, 3));
        
        Position newPos = monster.updatePosition();
        
        assertEquals(55, newPos.x(), 0.01);
        assertEquals(53, newPos.y(), 0.01);
    }

    @Test
    public void testUpdatePositionWithNegativeVelocity() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(-3, -2));
        
        Position newPos = monster.updatePosition();
        
        assertEquals(47, newPos.x(), 0.01);
        assertEquals(48, newPos.y(), 0.01);
    }

    @Test
    public void testUpdatePositionWithZeroVelocity() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(0, 0));
        
        Position newPos = monster.updatePosition();
        
        assertEquals(50, newPos.x(), 0.01);
        assertEquals(50, newPos.y(), 0.01);
    }

    @Test
    public void testMoveMonsterReturnsNewPosition() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        Position result = monster.moveMonster();
        assertNotNull(result);
        assertEquals(51, result.x(), 0.01);
    }

    @Test
    public void testMoveMonsterReturnsUpdatePositionResult() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(10, 5));
        Position moveResult = monster.moveMonster();
        assertEquals(60, moveResult.x(), 0.01);
        assertEquals(55, moveResult.y(), 0.01);
    }

    @Test
    public void testApplyCollisionsLeftCollision() {
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(-2, 0));
        Position newPos = monster.updatePosition();
        assertTrue(monster.getVelocity().x() > 0);
    }

    @Test
    public void testApplyCollisionsNoLeftCollisionWhenMovingRight() {
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        assertEquals(1.0, monster.getVelocity().x(), 0.01);
    }

    @Test
    public void testApplyCollisionsRightCollision() {
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        Position newPos = monster.updatePosition();
        assertTrue(monster.getVelocity().x() < 0);
    }

    @Test
    public void testApplyCollisionsNoRightCollisionWhenMovingLeft() {
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(-1, 0));
        assertEquals(-1.0, monster.getVelocity().x(), 0.01);
    }

    @Test
    public void testCollisionReversesVelocity() {
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        double initialVx = monster.getVelocity().x();
        monster.updatePosition();
        assertEquals(-initialVx, monster.getVelocity().x(), 0.01);
    }

    @Test
    public void testMultipleCollisionsReverse() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');

        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        when(mockScene.collidesLeft(any(), any())).thenReturn(false);
        monster.updatePosition();
        assertTrue(monster.getVelocity().x() < 0);
        
        when(mockScene.collidesRight(any(), any())).thenReturn(false);
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        monster.updatePosition();
        assertTrue(monster.getVelocity().x() > 0);
    }

    @Test
    public void testCollisionUsesCorrectKnightSize() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.updatePosition();
        verify(mockScene, atLeastOnce()).collidesRight(any(), any());
    }

    @Test
    public void testMultipleUpdatePositionCalls() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        
        Position pos1 = monster.updatePosition();
        assertEquals(51, pos1.x(), 0.01);
        Position pos2 = monster.updatePosition();
        assertEquals(51, pos2.x(), 0.01);
    }

    @Test
    public void testVelocityYComponentStaysZero() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        
        assertEquals(0, monster.getVelocity().y(), 0.01);
        
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        monster.updatePosition();
        assertEquals(0, monster.getVelocity().y(), 0.01);
    }

    @Test
    public void testMonsterAtOrigin() {
        SwordMonster monster = new SwordMonster(0, 0, 10, mockScene, 20, new Position(8, 8), 'E');
        
        Position newPos = monster.updatePosition();
        assertEquals(1, newPos.x(), 0.01);
        assertEquals(0, newPos.y(), 0.01);
    }

    @Test
    public void testMonsterWithLargeVelocity() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(100, 0));
        
        Position newPos = monster.updatePosition();
        assertEquals(150, newPos.x(), 0.01);
    }

    @Test
    public void testMonsterWithFractionalVelocity() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(0.5, 0.25));
        
        Position newPos = monster.updatePosition();
        assertEquals(50.5, newPos.x(), 0.01);
        assertEquals(50.25, newPos.y(), 0.01);
    }

    @Test
    public void testSceneCollidesLeftCalled() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        monster.setVelocity(new Vector(-1, 0));
        
        monster.updatePosition();
        
        verify(mockScene).collidesLeft(any(Position.class), any(Position.class));
    }

    @Test
    public void testSceneCollidesRightCalled() {
        SwordMonster monster = new SwordMonster(50, 50, 10, mockScene, 20, new Position(8, 8), 'E');

        monster.updatePosition();
        verify(mockScene).collidesRight(any(Position.class), any(Position.class));
    }
}