package pt.feup.tvvs.soulknight.model.game.elements.knight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
public class DamagedStateMutationTests {
    private Knight knight;
    private Scene scene;
    private DamagedState damagedState;

    @BeforeEach
    void setUp() {
        knight = mock(Knight.class);
        scene = mock(Scene.class);

        when(knight.getPosition()).thenReturn(new Position(0, 0));

        when(knight.getScene()).thenReturn(scene);
        when(knight.getVelocity()).thenReturn(new Vector(0, 0));
        when(knight.getMaxVelocity()).thenReturn(new Vector(5, 5));
        when(knight.getAcceleration()).thenReturn(1.0);

        when(knight.getJumpBoost()).thenReturn(5.0);

        damagedState = new DamagedState(knight, 10);
    }

    @Test
    public void testJumpSideEffects() {
        when(knight.getJumpCounter()).thenReturn(1);
        when(knight.getJumpBoost()).thenReturn(5.0);
        when(knight.getVelocity()).thenReturn(new Vector(0, 10.0));

        Vector result = damagedState.jump();

        verify(knight).setJumpCounter(2);
        assertEquals(5.0, result.y(), 0.001);
        verify(scene).setJumpParticles(any());
    }

    @Test
    public void testDashSideEffects() {
        damagedState.dash();
        verify(scene).setDashParticles(any());
    }

    @Test
    public void testUpdateVelocityCallsTick() {
        long initialTimer = damagedState.getParticlesTimer();
        damagedState.updateVelocity(new Vector(1, 1));
        assertEquals(initialTimer - 1, damagedState.getParticlesTimer());
    }

    @Test
    public void testGetNextStateTransitions() {
        damagedState.setTicks(15);
        when(scene.collideSpike()).thenReturn(false);
        when(knight.getHP()).thenReturn(10);
        when(knight.isOnGround()).thenReturn(false);
        when(knight.getVelocity()).thenReturn(new Vector(0, 5.0));
        assertNotNull(damagedState.getNextState());
        when(knight.isOnGround()).thenReturn(true);
        damagedState.getNextState();

        verify(knight).setJumpCounter(0);

        double minWalk = 1.0;
        when(knight.getVelocity()).thenReturn(new Vector(minWalk, 0));
        assertTrue(damagedState.getNextState() instanceof WalkingState);
    }
}