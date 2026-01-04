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

public class JumpStateWhiteBoxTest {
    Knight knight;
    Scene scene;
    JumpState state;

    @BeforeEach
    void setup() {
        knight = mock(Knight.class);
        scene = mock(Scene.class);

        state = spy(new JumpState(knight));

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
    void testJumpCounterBelowTwo() {
        when(knight.getJumpCounter()).thenReturn(1);

        Vector result = state.jump();

        verify(knight).setJumpCounter(2);
        verify(scene).setDoubleJumpParticles(any());
        assertEquals(4, result.x(), 0.001);
        assertEquals(9.8, result.y(), 0.001);
    }

    @Test
    void testJumpCounterAtTwo() {
        when(knight.getJumpCounter()).thenReturn(2);

        Vector result = state.jump();

        verify(knight, never()).setJumpCounter(anyInt());
        assertEquals(4, result.x(), 0.001);
        assertEquals(11.8, result.y(), 0.001);
    }

    @Test
    void testDashFacingRight() {
        Vector result = state.dash();

        assertEquals(5, result.x(), 0.001);
        assertEquals(2, result.y(), 0.001);
        verify(scene).setDashParticles(any());
    }

    @Test
    void testDashFacingLeft() {
        when(knight.isFacingRight()).thenReturn(false);

        Vector result = state.dash();

        assertEquals(-5, result.x(), 0.001);
        assertEquals(2, result.y(), 0.001);
    }

    @Test
    void testUpdateVelocityNegativeLowFall() {
        when(knight.getVelocity()).thenReturn(new Vector(1, -0.3));
        Vector result = state.updateVelocity(new Vector(2, 1));

        assertEquals(4, result.x(), 0.001);
        assertEquals(1 + 9.8 * 0.5, result.y(), 0.01);
    }

    @Test
    void testUpdateVelocityNegativeFastFall() {
        when(knight.getVelocity()).thenReturn(new Vector(1, -1));
        Vector result = state.updateVelocity(new Vector(2, 1));

        assertEquals(4, result.x(), 0.001);
        assertEquals(1 + 9.8, result.y(), 0.01);
    }

    @Test
    void testGetNextStateWhenSpikeCollision() {
        when(scene.collideSpike()).thenReturn(true);
        assertTrue(state.getNextState() instanceof RespawnState);
    }

    @Test
    void testGetNextStateRespawnParticlesTriggered() {
        when(scene.collideSpike()).thenReturn(false);
        when(state.getParticlesTimer()).thenReturn(0L);

        state.getNextState();

        verify(scene).setRespawnParticles(any());
    }

    @Test
    void testGetNextStateDash() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.isOverMaxXVelocity()).thenReturn(true);

        assertTrue(state.getNextState() instanceof DashState);
    }

    @Test
    void testGetNextStateStartFalling() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.isOverMaxXVelocity()).thenReturn(false);

        assertTrue(state.getNextState() instanceof FallingState);
    }

    @Test
    void testGetNextStateStayJumping() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.isOverMaxXVelocity()).thenReturn(false);
        when(knight.getVelocity()).thenReturn(new Vector(0, -1));

        assertSame(state, state.getNextState());
    }
}