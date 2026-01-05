package pt.feup.tvvs.soulknight.model.game.elements.particle;
            
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RainParticleWhiteBoxTests {
    private RainParticle particle;
    private Scene scene;

    @BeforeEach
    void setUp() {
        scene = mock(Scene.class);
        when(scene.getHeight()).thenReturn(10);
        when(scene.getWidth()).thenReturn(10);
    }

    @Test
    public void testApplyCollisions() {
        RainParticle particle = new RainParticle(
                3, 3,
                new Position(0, 0),
                new TextColor.RGB(0, 0, 255)
        );
        Vector velocity = new Vector(3, 4);
        Vector result = particle.applyCollisions(velocity);
        assertEquals(velocity, result);
    }

    @Test
    public void testMoveParticle() {
        RainParticle particle = new RainParticle(
                3, 3,
                new Position(0, 0),
                new TextColor.RGB(0, 0, 255)
        );

        Position newPos = particle.moveParticle(scene, 0);

        assertTrue(newPos.x() >= 4 && newPos.x() <= 6); // includes wind [-1,0,1]
        assertEquals(5, newPos.y(), 0.001);
    }

    @Test
    public void testLeftBoundary() {
        RainParticle particle = new RainParticle(
                -5, 3,
                new Position(0, 0),
                new TextColor.RGB(0, 0, 255)
        );

        Position newPos = particle.moveParticle(scene, 0);
        assertEquals(scene.getWidth() - 1, newPos.x(), 0.001);
    }

    @Test
    public void testRightBoundary() {
        RainParticle particle = new RainParticle(
                20, 3,
                new Position(0, 0),
                new TextColor.RGB(0, 0, 255)
        );

        Position newPos = particle.moveParticle(scene, 0);
        assertEquals(1, newPos.x(), 0.001);
    }

    @Test
    public void testBottomBoundaryReset() {
        RainParticle particle = new RainParticle(
                5, 9,
                new Position(0, 0),
                new TextColor.RGB(0, 0, 255)
        );

        Position newPos = particle.moveParticle(scene, 0);
        assertEquals(0, newPos.y(), 0.001);
    }

    @Test
    public void testNullPositionThrowsException() {
        RainParticle particle = new RainParticle(
                0, 0,
                new Position(0, 0),
                new TextColor.RGB(0, 0, 255)
        );

        particle.setPosition(null);

        assertThrows(IllegalStateException.class, () -> particle.moveParticle(scene, 0));
    }
}