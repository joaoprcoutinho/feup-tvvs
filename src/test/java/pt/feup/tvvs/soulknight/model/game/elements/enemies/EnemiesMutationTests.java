package pt.feup.tvvs.soulknight.model.game.elements.enemies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EnemiesMutationTests {

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
    public void testConstructorSetsPosition() {
        SwordMonster enemy = new SwordMonster(100, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        Position pos = enemy.getPosition();
        
        assertEquals(100, pos.x());
        assertEquals(50, pos.y());
    }

    @Test
    public void testConstructorSetsScene() {
        SwordMonster enemy = new SwordMonster(100, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        
        assertSame(mockScene, enemy.getScene());
        assertNotNull(enemy.getScene());
    }

    @Test
    public void testConstructorSetsDamage() {
        SwordMonster enemy = new SwordMonster(100, 50, 10, mockScene, 25, new Position(8, 8), 'E');
        
        assertEquals(25, enemy.getDamage());
        assertNotEquals(0, enemy.getDamage());
    }

    @Test
    public void testConstructorSetsSize() {
        Position size = new Position(8, 8);
        SwordMonster enemy = new SwordMonster(100, 50, 10, mockScene, 20, size, 'E');
        
        assertNotNull(enemy.getSize());
        assertEquals(8, enemy.getSize().x());
        assertEquals(8, enemy.getSize().y());
    }

    @Test
    public void testConstructorSetsDefaultVelocity() {
        SwordMonster sword = new SwordMonster(100, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        Vector velocity = sword.getVelocity();
        
        assertNotNull(velocity);
        assertEquals(1.0, velocity.x(), 0.01);
        assertEquals(0.0, velocity.y(), 0.01);
    }

    @Test
    public void testSetVelocity() {
        SwordMonster enemy = new SwordMonster(100, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        Vector newVelocity = new Vector(3.5, 2.0);
        
        enemy.setVelocity(newVelocity);
        
        assertEquals(3.5, enemy.getVelocity().x(), 0.01);
        assertEquals(2.0, enemy.getVelocity().y(), 0.01);
    }

    @Test
    public void testSetVelocityNegative() {
        SwordMonster enemy = new SwordMonster(100, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        Vector negativeVelocity = new Vector(-2.5, -1.5);
        
        enemy.setVelocity(negativeVelocity);
        
        assertEquals(-2.5, enemy.getVelocity().x(), 0.01);
        assertEquals(-1.5, enemy.getVelocity().y(), 0.01);
    }

    @Test
    public void testSetVelocityZero() {
        SwordMonster enemy = new SwordMonster(100, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        Vector zeroVelocity = new Vector(0, 0);
        
        enemy.setVelocity(zeroVelocity);
        
        assertEquals(0, enemy.getVelocity().x(), 0.01);
        assertEquals(0, enemy.getVelocity().y(), 0.01);
    }

    @Test
    public void testGetVelocityReturnsSetValue() {
        SwordMonster enemy = new SwordMonster(100, 50, 10, mockScene, 20, new Position(8, 8), 'E');
        Vector velocity = new Vector(5.5, 3.3);
        enemy.setVelocity(velocity);
        
        Vector retrieved = enemy.getVelocity();
        assertEquals(velocity.x(), retrieved.x(), 0.01);
        assertEquals(velocity.y(), retrieved.y(), 0.01);
    }

    @Test
    public void testGetSceneReturnsCorrectScene() {
        Scene scene1 = mock(Scene.class);
        Scene scene2 = mock(Scene.class);
        when(scene1.getWidth()).thenReturn(100);
        when(scene2.getWidth()).thenReturn(200);
        
        SwordMonster enemy1 = new SwordMonster(0, 0, 10, scene1, 20, new Position(8, 8), 'E');
        SwordMonster enemy2 = new SwordMonster(0, 0, 10, scene2, 20, new Position(8, 8), 'E');
        
        assertSame(scene1, enemy1.getScene());
        assertSame(scene2, enemy2.getScene());
        assertNotSame(enemy1.getScene(), enemy2.getScene());
    }

    @Test
    public void testGetDamageReturnsCorrectValue() {
        SwordMonster enemy1 = new SwordMonster(0, 0, 10, mockScene, 10, new Position(8, 8), 'E');
        SwordMonster enemy2 = new SwordMonster(0, 0, 10, mockScene, 25, new Position(8, 8), 'E');
        SwordMonster enemy3 = new SwordMonster(0, 0, 10, mockScene, 50, new Position(8, 8), 'E');
        
        assertEquals(10, enemy1.getDamage());
        assertEquals(25, enemy2.getDamage());
        assertEquals(50, enemy3.getDamage());
    }

    @Test
    public void testDamageIsPositive() {
        SwordMonster enemy = new SwordMonster(0, 0, 10, mockScene, 20, new Position(8, 8), 'E');
        assertTrue(enemy.getDamage() > 0);
    }

    @Test
    public void testDamageNotZero() {
        SwordMonster enemy = new SwordMonster(0, 0, 10, mockScene, 15, new Position(8, 8), 'E');
        assertNotEquals(0, enemy.getDamage());
    }

    @Test
    public void testGetSizeReturnsCorrectDimensions() {
        Position size = new Position(12, 14);
        SwordMonster enemy = new SwordMonster(0, 0, 10, mockScene, 20, size, 'E');
        
        assertEquals(12, enemy.getSize().x(), 0.01);
        assertEquals(14, enemy.getSize().y(), 0.01);
    }

    @Test
    public void testGetSizeNotNull() {
        SwordMonster enemy = new SwordMonster(0, 0, 10, mockScene, 20, new Position(8, 8), 'E');
        assertNotNull(enemy.getSize());
    }

    @Test
    public void testDifferentEnemySizes() {
        Position smallSize = new Position(2, 2);
        Position mediumSize = new Position(8, 8);
        Position largeSize = new Position(16, 16);
        
        SwordMonster small = new SwordMonster(0, 0, 10, mockScene, 20, smallSize, 'E');
        SwordMonster medium = new SwordMonster(0, 0, 10, mockScene, 20, mediumSize, 'E');
        SwordMonster large = new SwordMonster(0, 0, 10, mockScene, 20, largeSize, 'E');
        
        assertTrue(small.getSize().x() < medium.getSize().x());
        assertTrue(medium.getSize().x() < large.getSize().x());
    }

    @Test
    public void testDefaultVelocityComponents() {
        PurpleMonster purple = new PurpleMonster(0, 0, 25, mockScene, 5, new Position(8, 9), 'l');
        
        assertEquals(-1.5, purple.getVelocity().x(), 0.01);
        assertEquals(0, purple.getVelocity().y(), 0.01);
    }

    @Test
    public void testGetCharReturnsSymbol() {
        SwordMonster sword = new SwordMonster(0, 0, 10, mockScene, 20, new Position(8, 8), 'E');
        PurpleMonster purple = new PurpleMonster(0, 0, 25, mockScene, 5, new Position(8, 9), 'l');
        GhostMonster ghost = new GhostMonster(0, 0, 1, mockScene, 10, new Position(2, 2), 'm');
        
        assertEquals('E', sword.getChar());
        assertEquals('l', purple.getChar());
        assertEquals('m', ghost.getChar());
    }

    @Test
    public void testGetCharCustomSymbol() {
        SwordMonster enemy = new SwordMonster(0, 0, 10, mockScene, 20, new Position(8, 8), 'X');
        assertEquals('X', enemy.getChar());
    }

    @Test
    public void testEnemyAtOrigin() {
        SwordMonster enemy = new SwordMonster(0, 0, 10, mockScene, 20, new Position(8, 8), 'E');
        assertEquals(0, enemy.getPosition().x());
        assertEquals(0, enemy.getPosition().y());
    }

    @Test
    public void testEnemyAtLargeCoordinates() {
        SwordMonster enemy = new SwordMonster(1000, 500, 10, mockScene, 20, new Position(8, 8), 'E');
        assertEquals(1000, enemy.getPosition().x());
        assertEquals(500, enemy.getPosition().y());
    }

    @Test
    public void testEnemyNegativeCoordinates() {
        SwordMonster enemy = new SwordMonster(-10, -5, 10, mockScene, 20, new Position(8, 8), 'E');
        assertEquals(-10, enemy.getPosition().x());
        assertEquals(-5, enemy.getPosition().y());
    }

    @Test
    public void testMultipleEnemiesIndependent() {
        SwordMonster enemy1 = new SwordMonster(10, 20, 10, mockScene, 15, new Position(8, 8), 'A');
        SwordMonster enemy2 = new SwordMonster(30, 40, 10, mockScene, 25, new Position(8, 8), 'B');
        
        enemy1.setVelocity(new Vector(5, 5));
        assertNotEquals(enemy1.getVelocity().x(), enemy2.getVelocity().x());
        assertNotEquals(enemy1.getPosition().x(), enemy2.getPosition().x());
        assertNotEquals(enemy1.getDamage(), enemy2.getDamage());
        assertNotEquals(enemy1.getChar(), enemy2.getChar());
    }
}