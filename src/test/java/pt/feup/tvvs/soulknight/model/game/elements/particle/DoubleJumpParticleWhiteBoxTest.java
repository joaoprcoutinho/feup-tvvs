package pt.feup.tvvs.soulknight.model.game.elements.particle;
            
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class DoubleJumpParticleWhiteBoxTest {
    private DoubleJumpParticle particle;

    @BeforeEach
    void setUp() {
        particle = new DoubleJumpParticle(
                5,
                5,
                new Position(1, 2),
                new TextColor.RGB(255, 0, 0)
        );
    }

    @Test
    void testApplyCollisions() {
        Vector velocity = new Vector(3, 4);
        Vector result = particle.applyCollisions(velocity);
        assertEquals(velocity, result);
    }

    @Test
    void testMoveParticle() {
        Scene scene = mock(Scene.class);
        Position initialPos = particle.getPosition();
        Position initialVel = particle.getVelocity();

        Position newPos = particle.moveParticle(scene, 1);

        assertEquals(initialPos.x() + initialVel.x() + particle.getVelocity().x(), newPos.x(), 0.0001);
        assertEquals(initialPos.y() + initialVel.y() + particle.getVelocity().y(), newPos.y(), 0.0001);
    }
}