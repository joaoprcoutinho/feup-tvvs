package pt.feup.tvvs.soulknight.model.game.elements.knight;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.elements.particle.Particle;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.List;

public class FallingStateWhiteBoxTest {
    Knight knight;
    Scene scene;
    FallingState state;

    @BeforeEach
    void setup() {
        knight = mock(Knight.class);
        scene = mock(Scene.class);

        state = spy(new FallingState(knight));

        when(knight.getScene()).thenReturn(scene);
        when(knight.getAcceleration()).thenReturn(2.0);
        when(scene.getGravity()).thenReturn(9.8);
        when(knight.getDashBoost()).thenReturn(5.0);
        when(knight.getJumpBoost()).thenReturn(6.0);
        when(knight.isFacingRight()).thenReturn(true);
        when(knight.getJumpCounter()).thenReturn(1);
        when(knight.getVelocity()).thenReturn(new Vector(2, 2));
        when(knight.getPosition()).thenReturn(new Position(0, 0));
        when(knight.getMaxVelocity()).thenReturn(new Vector(20, 20));

        Particle particle = mock(Particle.class);
        when(knight.createDashParticles(anyInt())).thenReturn(List.of(particle));
        when(knight.createParticlesDoubleJump(anyInt(), any())).thenReturn(List.of(particle));
        when(knight.createRespawnParticles(anyInt())).thenReturn(List.of(particle));
    }

    @Test
    void testJumpDoubleJump() {
        when(knight.getVelocity()).thenReturn(new Vector(2, 0.1));
        when(knight.getJumpCounter()).thenReturn(1);

        Vector result = state.jump();

        verify(knight).setJumpCounter(2);
        verify(scene).setDoubleJumpParticles(any());
        assertEquals(4, result.x(), 0.001);
        assertEquals(-1, result.y(), 0.001);
    }

    @Test
    void testJumpNoDoubleJump() {
        when(knight.getVelocity()).thenReturn(new Vector(2, 3));
        when(knight.getJumpCounter()).thenReturn(2);

        Vector result = state.jump();

        assertEquals(4, result.x(), 0.001);
        assertEquals(20, result.y(), 0.001);
    }

    @Test
    void testDashFacingRight() {
        Vector result = state.dash();

        assertEquals(7, result.x(), 0.001);
        assertEquals(2, result.y(), 0.001);
        verify(scene).setDashParticles(any());
    }

    @Test
    void testDashFacingLeft() {
        when(knight.isFacingRight()).thenReturn(false);
        Vector result = state.dash();

        assertEquals(-3, result.x(), 0.001);
        assertEquals(2, result.y(), 0.001);
    }

    @Test
    void testUpdateVelocityLowFall() {
        when(knight.getVelocity()).thenReturn(new Vector(1, 0.3));

        Vector result = state.updateVelocity(new Vector(2, 1));

        assertEquals(4, result.x(), 0.001);
        assertEquals(1 + 9.8 * 0.5, result.y(), 0.01);
    }

    @Test
    void testUpdateVelocityFastFall() {
        when(knight.getVelocity()).thenReturn(new Vector(1, 2));

        Vector result = state.updateVelocity(new Vector(2, 1));

        assertEquals(4, result.x(), 0.001);
        assertEquals(1 + 9.8 * 1.75, result.y(), 0.01);
    }

    @Test
    void testGetNextStateWhenSpikeCollision() {
        when(scene.collideSpike()).thenReturn(true);

        assertTrue(state.getNextState() instanceof RespawnState);
    }

    @Test
    void testGetNextStateDash() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.isOverMaxXVelocity()).thenReturn(true);

        assertTrue(state.getNextState() instanceof DashState);
    }

    @Test
    void testGetNextStateOnGround() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.isOnGround()).thenReturn(true);

        KnightState next = state.getNextState();

        assertNotSame(state, next);
    }

    @Test
    void testGetNextStateJumpCounterTwo() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.isOnGround()).thenReturn(false);
        when(knight.getJumpCounter()).thenReturn(2);

        KnightState next = state.getNextState();

        assertNotSame(state, next);
    }

    @Test
    void testGetNextStateStayFalling() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.isOverMaxXVelocity()).thenReturn(false);
        when(knight.isOnGround()).thenReturn(false);
        when(knight.getJumpCounter()).thenReturn(1);

        assertSame(state, state.getNextState());
    }

    @Test
    void testGetNextStateRespawnParticlesTriggered() {
        when(scene.collideSpike()).thenReturn(false);
        when(state.getParticlesTimer()).thenReturn(0L);

        state.getNextState();

        verify(scene).setRespawnParticles(any());
    }
}