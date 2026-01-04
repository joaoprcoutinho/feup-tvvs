package pt.feup.tvvs.soulknight.model.game.elements.enemies;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class PurpleMonsterWhiteBoxTest {
    private Scene scene;
    private PurpleMonster monster;

    @BeforeEach
    void setUp() {
        scene = mock(Scene.class);
        when(scene.getWidth()).thenReturn(100);
        when(scene.getHeight()).thenReturn(100);

        monster = new PurpleMonster(10, 10, 20, scene, 5, new Position(8, 9), 'P');
    }

    @Test
    void testGetChar() {
        assertEquals('P', monster.getChar());
    }

    @Test
    void testUpdatePosition() {
        when(scene.collidesLeft(any(), any())).thenReturn(false);
        when(scene.collidesRight(any(), any())).thenReturn(false);

        Vector velocity = monster.getVelocity();
        Position newPos = monster.updatePosition();

        assertEquals(monster.getPosition().x() + velocity.x(), newPos.x(), 0.0001);
        assertEquals(monster.getPosition().y() + velocity.y(), newPos.y(), 0.0001);
    }

    @Test
    void testApplyCollisionsNoCollision() {
        when(scene.collidesLeft(any(), any())).thenReturn(false);
        when(scene.collidesRight(any(), any())).thenReturn(false);

        Vector v = new Vector(-1.5, 0);
        Vector result = monster.applyCollisions(v);

        assertEquals(-1.5, result.x(), 0.0001);
        assertEquals(0, result.y(), 0.0001);
    }

    @Test
    void testApplyCollisionsLeftCollision() {
        when(scene.collidesLeft(any(), any())).thenReturn(true);

        Vector v = new Vector(-1.5, 0);
        Vector result = monster.applyCollisions(v);

        assertTrue(result.x() >= -1.5 && result.x() <= 0);
        assertEquals(0, result.y(), 0.0001);

        // Velocity should be inverted
        assertEquals(1.5, monster.getVelocity().x(), 0.0001);
    }

    @Test
    void testApplyCollisionsRightCollision() {
        when(scene.collidesRight(any(), any())).thenReturn(true);

        Vector v = new Vector(1.0, 0);
        Vector result = monster.applyCollisions(v);

        assertTrue(result.x() >= 0 && result.x() <= 1.0);
        assertEquals(0, result.y(), 0.0001);

        // Velocity should be inverted
        assertEquals(-1.0, monster.getVelocity().x(), 0.0001);
    }

    @Test
    void testMoveMonsterReturnsUpdatedPosition() {
        when(scene.collidesLeft(any(), any())).thenReturn(false);
        when(scene.collidesRight(any(), any())).thenReturn(false);

        Position pos = monster.moveMonster();
        assertEquals(monster.getPosition().x() + monster.getVelocity().x(), pos.x(), 0.0001);
        assertEquals(monster.getPosition().y() + monster.getVelocity().y(), pos.y(), 0.0001);
    }
}