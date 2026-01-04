package pt.feup.tvvs.soulknight.model.game.elements.enemies;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class GhostMonsterWhiteBoxTest {
    private Scene scene;
    private GhostMonster ghost;

    @BeforeEach
    void setUp() {
        scene = mock(Scene.class);
        when(scene.getWidth()).thenReturn(8);
        when(scene.getHeight()).thenReturn(8);

        ghost = new GhostMonster(
                5, 5,
                10,
                scene,
                2,
                new Position(1, 1),
                'G'
        );

        ghost.setAmplitude(0);
        ghost.setFrequency(0);
        ghost.setHorizontalSpeed(0);
    }

    @Test
    void testGetChar() {
        assertEquals('G', ghost.getChar());
    }

    @Test
    void testUpdatePositionNormalMovement() {
        Position p = ghost.updatePosition();

        assertEquals(5, p.x(), 0.0001);
        assertEquals(5, p.y(),0.0001);
    }

    @Test
    void testUpdatePositionWrapRightEdge() {
        ghost.setPosition(new Position(10, 5));

        Position p = ghost.updatePosition();

        assertEquals(0, p.x(),0.0001);
        assertEquals(5, p.y(),0.0001);
    }

    @Test
    void testUpdatePositionWrapLeftEdge() {
        ghost.setPosition(new Position(-1, 5));

        Position p = ghost.updatePosition();

        assertEquals(8, p.x(),0.0001);
        assertEquals(5, p.y(),0.0001);
    }

    @Test
    void testUpdatePositionWrapBottomEdge() {
        ghost.setPosition(new Position(5, 10));

        Position p = ghost.updatePosition();

        assertEquals(5, p.x(),0.0001);
        assertEquals(0, p.y(),0.0001);
    }

    @Test
    void testUpdatePositionWrapTopEdge() {
        ghost.setPosition(new Position(5, -1));

        Position p = ghost.updatePosition();

        assertEquals(5, p.x(),0.0001);
        assertEquals(8, p.y(),0.0001);
    }

    @Test
    void testMoveMonster() {
        Position p = ghost.moveMonster();

        assertEquals(p, ghost.getPosition());
    }

    @Test
    void testApplyCollisions() {
        Vector v = new Vector(1, 1);
        Vector result = ghost.applyCollisions(v);

        assertSame(v, result);
    }
}