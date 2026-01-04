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

public class DamagedStateWhiteBoxTest {
    Knight knight;
    Scene scene;
    DamagedState state;

    @BeforeEach
    void setup() {
        scene = mock(Scene.class);
        knight = mock(Knight.class);

        when(knight.getScene()).thenReturn(scene);
        when(knight.getVelocity()).thenReturn(new Vector(1, 1));
        when(knight.getAcceleration()).thenReturn(2.0);
        when(scene.getGravity()).thenReturn(9.8);
        when(knight.getJumpBoost()).thenReturn(5.0);
        when(knight.getDashBoost()).thenReturn(8.0);
        when(knight.isFacingRight()).thenReturn(true);
        when(knight.isOnGround()).thenReturn(true);
        when(knight.isOverMaxXVelocity()).thenReturn(false);
        when(knight.getHP()).thenReturn(10);
        when(knight.getPosition()).thenReturn(new Position(0, 0));
        when(knight.getMaxVelocity()).thenReturn(new Vector(20, 20));

        Particle particle = mock(Particle.class);
        when(knight.createParticlesJump(anyInt())).thenReturn(List.of(particle));
        when(knight.createDashParticles(anyInt())).thenReturn(List.of(particle));
        when(knight.createRespawnParticles(anyInt())).thenReturn(List.of(particle));

        state = new DamagedState(knight, 5);
    }

    @Test
    void testJump() {
        Vector result = state.jump();

        assertNotNull(result);
    }

    @Test
    void testDashFacingRight() {
        Vector result = state.dash();

        assertEquals(8.0, result.x(), 0.001);
        assertEquals(1.0, result.y(), 0.001);
    }

    @Test
    void testDashFacingLeft() {
        when(knight.isFacingRight()).thenReturn(false);
        Vector result = state.dash();

        assertEquals(-8.0, result.x(), 0.001);
        assertEquals(1.0, result.y(), 0.001);
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
    void testGetNextStateWhenNoHP() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.getHP()).thenReturn(0);

        KnightState next = state.getNextState();

        assertTrue(next instanceof RespawnState);
    }

    @Test
    void testNextStateTicksLessThan15() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.getHP()).thenReturn(10);

        state.setTicks(0);
        KnightState result = state.getNextState();

        assertSame(state, result);
        assertEquals(1, state.getTicks());
    }

    @Test
    void testGetNextStateWhenNotOnGround() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.getHP()).thenReturn(10);
        when(knight.isOnGround()).thenReturn(false);
        state.setTicks(15);

        KnightState next = state.getNextState();

        verify(knight).setGotHit(false);
        assertNotSame(state, next);
    }

    @Test
    void testGetNextStateOverMaxXVelocity() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.getHP()).thenReturn(10);
        state.setTicks(15);
        when(knight.isOnGround()).thenReturn(true);
        when(knight.isOverMaxXVelocity()).thenReturn(true);

        KnightState result = state.getNextState();

        assertTrue(result instanceof DashState);
    }

    @Test
    void testNextStateWalking() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.getHP()).thenReturn(10);
        state.setTicks(15);
        when(knight.isOnGround()).thenReturn(true);
        when(knight.isOverMaxXVelocity()).thenReturn(false);
        when(knight.getVelocity()).thenReturn(new Vector(WalkingState.MIN_VELOCITY + 1, 0));

        KnightState result = state.getNextState();

        assertTrue(result instanceof WalkingState);
    }

    @Test
    void testNextStateIdle() {
        when(scene.collideSpike()).thenReturn(false);
        when(knight.getHP()).thenReturn(10);
        state.setTicks(15);
        when(knight.isOnGround()).thenReturn(true);
        when(knight.isOverMaxXVelocity()).thenReturn(false);
        when(knight.getVelocity()).thenReturn(new Vector(WalkingState.MIN_VELOCITY - 1, 0));

        KnightState result = state.getNextState();

        assertTrue(result instanceof IdleState);
    }
}