package pt.feup.tvvs.soulknight.model.game.elements.knight;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class AfterDashStateWhiteBoxTest {
    Knight knight;
    Scene scene;
    AfterDashState state;

    @BeforeEach
    void setup() {
        scene = mock(Scene.class);
        knight = mock(Knight.class);

        state = new AfterDashState(knight);

        when(knight.getScene()).thenReturn(scene);
        when(knight.getAcceleration()).thenReturn(2.0);
        when(scene.getGravity()).thenReturn(9.8);
        when(knight.getVelocity()).thenReturn(new Vector(1, 1));
        when(knight.getMaxVelocity()).thenReturn(new Vector(20, 20));
        when(knight.getPosition()).thenReturn(new Position(0, 0));
    }

    @Test
    void testJump() {
        Vector result = state.jump();

        assertNotNull(result);
    }

    @Test
    void testDash() {
        Vector result = state.dash();

        assertNotNull(result);
    }

    @Test
    void testUpdateVelocity() {
        Vector v = new Vector(2, 3);

        Vector result = state.updateVelocity(v);

        assertEquals(4, result.x(), 0.001);
        assertEquals(12.8, result.y(), 0.001);
    }

    @Test
    void testGetNextStateWhenSpikeCollision() {
        when(scene.collideSpike()).thenReturn(true);

        KnightState next = state.getNextState();

        assertTrue(next instanceof RespawnState);
    }

    @Test
    void testGetNextStateWhenOnGround() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.isOnGround()).thenReturn(true);

        KnightState next = state.getNextState();

        assertNotSame(state, next);
    }

    @Test
    void testGetNextStateOtherwiseStaysSame() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.isOnGround()).thenReturn(false);

        KnightState next = state.getNextState();

        assertSame(state, next);
    }
}