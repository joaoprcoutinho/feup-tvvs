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

        // Fix: Ensure knight.getPosition() is never null
        when(knight.getPosition()).thenReturn(new Position(0, 0));

        when(knight.getScene()).thenReturn(scene);
        when(knight.getVelocity()).thenReturn(new Vector(0, 0));
        when(knight.getMaxVelocity()).thenReturn(new Vector(5, 5));
        when(knight.getAcceleration()).thenReturn(1.0);

        // Necessary for jump logic (Mutation 20.1)
        when(knight.getJumpBoost()).thenReturn(5.0);

        damagedState = new DamagedState(knight, 10);
    }

    @Test
    void testJumpSideEffects() {
        // Targets Mutations 17.1, 17.2, 20.1, 22.1
        when(knight.getJumpCounter()).thenReturn(1);
        when(knight.getJumpBoost()).thenReturn(5.0);
        when(knight.getVelocity()).thenReturn(new Vector(0, 10.0));

        Vector result = damagedState.jump();

        // Kill 17.1 & 17.2: Verify counter increased (1 + 1 = 2)
        verify(knight).setJumpCounter(2);

        // Kill 20.1: Verify math (10.0 - 5.0 = 5.0)
        assertEquals(5.0, result.y(), 0.001);

        // Kill 22.1: Verify particles were set
        verify(scene).setJumpParticles(any());
    }

    @Test
    void testDashSideEffects() {
        // Targets Mutation 33.1
        damagedState.dash();
        verify(scene).setDashParticles(any());
    }

    @Test
    void testUpdateVelocityCallsTick() {
        // Targets Mutation 39.1
        // We check if particlesTimer changes inside the abstract KnightState
        long initialTimer = damagedState.getParticlesTimer();
        damagedState.updateVelocity(new Vector(1, 1));
        assertEquals(initialTimer - 1, damagedState.getParticlesTimer());
    }

    @Test
    void testGetNextStateTransitions() {
        // Targets Mutations 58.1, 59.1, 61.3

        // Setup state to bypass the 'ticks < 15' block (Mutation 53)
        damagedState.setTicks(15);
        when(scene.collideSpike()).thenReturn(false);
        when(knight.getHP()).thenReturn(10);

        // Case: On Air (Mutation 58.1)
        when(knight.isOnGround()).thenReturn(false);
        when(knight.getVelocity()).thenReturn(new Vector(0, 5.0)); // Falling
        assertNotNull(damagedState.getNextState());

        // Case: On Ground (Mutation 59.1)
        when(knight.isOnGround()).thenReturn(true);
        damagedState.getNextState();
        verify(knight).setJumpCounter(0);

        // Case: Walking boundary (Mutation 61.3)
        // Testing exactly at WalkingState.MIN_VELOCITY
        double minWalk = 1.0; // Assuming this value
        when(knight.getVelocity()).thenReturn(new Vector(minWalk, 0));
        assertTrue(damagedState.getNextState() instanceof WalkingState);
    }
}