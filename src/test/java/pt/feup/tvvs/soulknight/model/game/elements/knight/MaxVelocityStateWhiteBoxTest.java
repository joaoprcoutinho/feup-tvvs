package pt.feup.tvvs.soulknight.model.game.elements.knight;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.elements.particle.Particle;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.Assert.*;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.List;

public class MaxVelocityStateWhiteBoxTest {
    Knight knight;
    Scene scene;
    MaxVelocityState state;

    @BeforeEach
    void setup() {
        knight = mock(Knight.class);
        scene = mock(Scene.class);

        state = spy(new MaxVelocityState(knight));

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
    void testJump() {
        Vector result = state.jump();

        verify(knight).setJumpCounter(2);
        verify(scene).setJumpParticles(any());
        assertEquals(4, result.x(), 0.001);
        assertEquals(-4, result.y(), 0.001);
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
    void testUpdateVelocity() {
        Vector v = new Vector(1, 1);

        Vector result = state.updateVelocity(v);

        assertEquals(2, result.x(), 0.001);
        assertEquals(1, result.y(), 0.001);
    }

    @Test
    void testGetNextStateWhenSpikeCollision() {
        when(scene.collideSpike()).thenReturn(true);
        assertTrue(state.getNextState() instanceof RespawnState);
    }

    @Test
    void testNextStateRespawnParticlesTriggered() {
        when(scene.collideSpike()).thenReturn(false);
        when(state.getParticlesTimer()).thenReturn(0L);

        state.getNextState();

        verify(scene).setRespawnParticles(any());
    }

    @Test
    void testGetNextStateWhenNotOnGround() {
        when(scene.collideSpike()).thenReturn(false);
        when(state.getParticlesTimer()).thenReturn(5L);
        when(knight.isOnGround()).thenReturn(false);

        KnightState next = state.getNextState();

        assertNotSame(state, next);
    }

    @Test
    void testGetNextStateOverMaxXVelocity() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.getHP()).thenReturn(10);
        when(state.getParticlesTimer()).thenReturn(5L);

        when(knight.isOnGround()).thenReturn(true);
        when(knight.isOverMaxXVelocity()).thenReturn(true);

        KnightState result = state.getNextState();

        assertTrue(result instanceof DashState);
    }

    @Test
    void testGetNextStateRunningState() {
        when(knight.getVelocity()).thenReturn(new Vector(0.5, 0.5));
        when(scene.collideSpike()).thenReturn(false);
        when(state.getParticlesTimer()).thenReturn(5L);
        when(knight.isOverMaxXVelocity()).thenReturn(false);
        when(knight.isOnGround()).thenReturn(true);
        when(knight.getVelocity()).thenReturn(new Vector(RunningState.MAX_VELOCITY - 0.1, 0));

        assertTrue(state.getNextState() instanceof RunningState);
    }

    @Test
    void testGetNextStateStayMaxVelocity() {
        when(knight.getVelocity()).thenReturn(new Vector(1, 1));
        when(scene.collideSpike()).thenReturn(false);
        when(state.getParticlesTimer()).thenReturn(5L);
        when(knight.isOverMaxXVelocity()).thenReturn(false);
        when(knight.isOnGround()).thenReturn(true);
        when(knight.getVelocity()).thenReturn(new Vector(RunningState.MAX_VELOCITY, 0));

        assertSame(state, state.getNextState());
    }
}