package pt.feup.tvvs.soulknight.model.game.elements.particle;
            
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class RespawnParticleWhiteBoxTests {
    private Scene scene;

    @BeforeEach
    void setUp() {
        scene = mock(Scene.class);
        when(scene.getHeight()).thenReturn(10);
        when(scene.getWidth()).thenReturn(10);
    }

    @Test
    public void testApplyCollisionsCeiling() {
        when(scene.collidesUp(any(), any())).thenReturn(true);

        RespawnParticle particle = new RespawnParticle(
                5, 5,
                new Position(0, -1),
                new TextColor.RGB(255, 255, 255)
        );
        particle.setScene(scene);
        Vector result = particle.applyCollisions(new Vector(0, -1));
        assertEquals(1, result.y(), 0.001);
    }

    @Test
    public void testApplyCollisionsDownward() {
        when(scene.collidesDown(any(), any())).thenReturn(true);

        RespawnParticle particle = new RespawnParticle(
                5, 5,
                new Position(0, 1),
                new TextColor.RGB(255, 255, 255)
        );
        particle.setScene(scene);
        Vector result = particle.applyCollisions(new Vector(0, 1));
        assertEquals(0, result.y(), 0.1);
    }

    @Test
    public void testApplyCollisionsHorizontalLeft() {
        when(scene.collidesLeft(any(), any())).thenReturn(true);

        RespawnParticle particle = new RespawnParticle(
                5, 5,
                new Position(-1, 0),
                new TextColor.RGB(255, 255, 255)
        );
        particle.setScene(scene);
        Vector result = particle.applyCollisions(new Vector(-1, 0));
        assertEquals(0, result.x(), 0.001);
    }

    @Test
    public void testApplyCollisionsHorizontalRight() {
        when(scene.collidesRight(any(), any())).thenReturn(true);

        RespawnParticle particle = new RespawnParticle(
                5, 5,
                new Position(1, 0),
                new TextColor.RGB(255, 255, 255)
        );
        particle.setScene(scene);
        Vector result = particle.applyCollisions(new Vector(1, 0));
        assertEquals(0, result.x(), 0.001);
    }

    @Test
    public void testMoveParticleStick() {
        RespawnParticle particle = new RespawnParticle(
                5, 0,
                new Position(0, -1),
                new TextColor.RGB(255, 255, 255)
        );
        particle.setScene(scene);

        Position pos = particle.moveParticle(scene, 0);
        assertEquals(5, pos.x(), 0.001);
        assertEquals(0, pos.y(), 0.001);
    }

    @Test
    public void testMoveParticleGravity() {
        RespawnParticle particle = new RespawnParticle(
                5, 5,
                new Position(0, 0),
                new TextColor.RGB(255, 255, 255)
        );
        particle.setScene(scene);

        particle.moveParticle(scene, 0);
        particle.moveParticle(scene, 0);

        Position pos = particle.moveParticle(scene, 0);
        assertTrue(pos.y() > 5);
    }
}