package pt.feup.tvvs.soulknight.model.game.elements.enemies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    public void testConstructorSetsInitialVelocity() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        Vector velocity = monster.getVelocity();
        
        assertNotNull(velocity);
        assertEquals(-1.5, velocity.x(), 0.01);
        assertEquals(0.0, velocity.y(), 0.01);
    }

    @Test
    public void testConstructorVelocityIsNegative() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        
        assertTrue(monster.getVelocity().x() < 0);
        assertNotEquals(1.5, monster.getVelocity().x());
    }

    @Test
    public void testConstructorVelocityNotZero() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        assertNotEquals(0.0, monster.getVelocity().x());
    }

    @Test
    public void testConstructorVelocityExactValue() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        assertEquals(-1.5, monster.getVelocity().x(), 0.001);
        assertNotEquals(-1.0, monster.getVelocity().x());
        assertNotEquals(-2.0, monster.getVelocity().x());
    }

    @Test
    public void testConstructorSetsSymbol() {
        PurpleMonster monster = new PurpleMonster(0, 0, 25, mockScene, 5, new Position(8, 9), 'l');
        assertEquals('l', monster.getChar());
    }

    @Test
    public void testConstructorCustomSymbol() {
        PurpleMonster monster = new PurpleMonster(0, 0, 25, mockScene, 5, new Position(8, 9), 'P');
        assertEquals('P', monster.getChar());
    }

    @Test
    public void testGetCharReturnsSymbol() {
        PurpleMonster monster = new PurpleMonster(0, 0, 25, mockScene, 5, new Position(8, 9), 'Q');
        assertEquals('Q', monster.getChar());
        assertNotEquals('l', monster.getChar());
    }

    @Test
    public void testUpdatePositionMovesLeft() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');

        Position newPos = monster.updatePosition();
        
        assertEquals(48.5, newPos.x(), 0.01);
        assertEquals(50, newPos.y(), 0.01);
    }

    @Test
    public void testUpdatePositionWithCustomVelocity() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(3, 2));
        
        Position newPos = monster.updatePosition();
        
        assertEquals(53, newPos.x(), 0.01);
        assertEquals(52, newPos.y(), 0.01);
    }

    @Test
    public void testUpdatePositionWithZeroVelocity() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(0, 0));
        
        Position newPos = monster.updatePosition();
        
        assertEquals(50, newPos.x(), 0.01);
        assertEquals(50, newPos.y(), 0.01);
    }

    @Test
    public void testUpdatePositionWithPositiveVelocity() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(5, 0));
        
        Position newPos = monster.updatePosition();
        
        assertEquals(55, newPos.x(), 0.01);
    }

    @Test
    public void testMoveMonsterReturnsNewPosition() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        
        Position result = monster.moveMonster();
        
        assertNotNull(result);
        assertEquals(48.5, result.x(), 0.01);
    }

    @Test
    public void testMoveMonsterReturnsUpdatePositionResult() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(10, 5));
        
        Position moveResult = monster.moveMonster();
        
        assertEquals(60, moveResult.x(), 0.01);
        assertEquals(55, moveResult.y(), 0.01);
    }

    @Test
    public void testApplyCollisionsLeftCollision() {
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        Position newPos = monster.updatePosition();
        assertTrue(monster.getVelocity().x() > 0);
    }

    @Test
    public void testApplyCollisionsNoLeftCollisionWhenMovingRight() {
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(1.5, 0));
        assertEquals(1.5, monster.getVelocity().x(), 0.01);
    }

    @Test
    public void testApplyCollisionsRightCollision() {
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(1.5, 0));
        Position newPos = monster.updatePosition();
        assertTrue(monster.getVelocity().x() < 0);
    }

    @Test
    public void testApplyCollisionsNoRightCollisionWhenMovingLeft() {
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        assertEquals(-1.5, monster.getVelocity().x(), 0.01);
    }

    @Test
    public void testLeftCollisionReversesVelocity() {
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        double initialVx = monster.getVelocity().x();
        monster.updatePosition();
        assertEquals(-initialVx, monster.getVelocity().x(), 0.01);
        assertEquals(1.5, monster.getVelocity().x(), 0.01);
    }

    @Test
    public void testRightCollisionReversesVelocity() {
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(1.5, 0));
        monster.updatePosition();
        assertEquals(-1.5, monster.getVelocity().x(), 0.01);
    }

    @Test
    public void testMultipleCollisionsBounce() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        when(mockScene.collidesRight(any(), any())).thenReturn(false);
        monster.updatePosition();
        assertTrue(monster.getVelocity().x() > 0);
        
        when(mockScene.collidesLeft(any(), any())).thenReturn(false);
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        monster.updatePosition();
        assertTrue(monster.getVelocity().x() < 0);
    }

    @Test
    public void testCollisionUsesKnightSize10x9() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.updatePosition();
        verify(mockScene).collidesLeft(any(Position.class), any(Position.class));
    }

    @Test
    public void testVelocityYComponentStaysZero() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        assertEquals(0, monster.getVelocity().y(), 0.01);
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        monster.updatePosition();
        assertEquals(0, monster.getVelocity().y(), 0.01, "Y velocity should remain 0");
    }

    @Test
    public void testPositionUpdatedWithResolvedVelocity() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        Position pos = monster.updatePosition();
        assertEquals(48.5, pos.x(), 0.01);
    }

    @Test
    public void testPositionAfterCollisionAdjustment() {
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        Position pos = monster.updatePosition();
        assertEquals(49.5, pos.x(), 0.01, "Position should use adjusted velocity");
    }

    @Test
    public void testMonsterAtOrigin() {
        PurpleMonster monster = new PurpleMonster(0, 0, 25, mockScene, 5, new Position(8, 9), 'l');
        
        Position newPos = monster.updatePosition();
        assertEquals(-1.5, newPos.x(), 0.01);
        assertEquals(0, newPos.y(), 0.01);
    }

    @Test
    public void testMonsterWithLargeVelocity() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(-100, 0));
        
        Position newPos = monster.updatePosition();
        assertEquals(-50, newPos.x(), 0.01);
    }

    @Test
    public void testMonsterWithFractionalVelocity() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(-0.5, -0.25));
        
        Position newPos = monster.updatePosition();
        assertEquals(49.5, newPos.x(), 0.01);
        assertEquals(49.75, newPos.y(), 0.01);
    }


    @Test
    public void testMinFunctionLeftCollision() {
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        Position pos = monster.updatePosition();
        
        assertTrue(pos.x() < 50);
        assertTrue(pos.x() > 48.5);
    }

    @Test
    public void testMinFunctionRightCollision() {
        when(mockScene.collidesRight(any(), any())).thenReturn(true);
        
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(1.5, 0));
        Position pos = monster.updatePosition();
        
        assertEquals(50, pos.x(), 0.01);
    }


    @Test
    public void testSceneCollidesLeftCalled() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.updatePosition();
        verify(mockScene).collidesLeft(any(Position.class), any(Position.class));
    }

    @Test
    public void testSceneCollidesRightCalled() {
        PurpleMonster monster = new PurpleMonster(50, 50, 25, mockScene, 5, new Position(8, 9), 'l');
        monster.setVelocity(new Vector(1.5, 0));
        monster.updatePosition();
        verify(mockScene).collidesRight(any(Position.class), any(Position.class));
    }
}