package pt.feup.tvvs.soulknight.state.particle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.IOException;

import pt.feup.tvvs.soulknight.controller.menu.ParticleMenuController;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.menu.Particle;

public class ZicoStateMutationTests {

    @Test
    void move_shouldMoveParticleDown() {
        ZicoState zicoState = new ZicoState();
        Particle particle = mock(Particle.class);
        ParticleMenuController controller = mock(ParticleMenuController.class);

        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> 
            new Position(((Integer)invocation.getArgument(0)).doubleValue(), ((Integer)invocation.getArgument(1)).doubleValue()));

        Position newPosition = zicoState.move(particle, 1000, controller);

        // Check that the y position is incremented by 10 as specified
        assertEquals(20.0, newPosition.y(), "Particle Y position should be increased by 10");
    }

    @Test
    void move_shouldNotChangeXPosition() {
        ZicoState zicoState = new ZicoState();
        Particle particle = mock(Particle.class);
        ParticleMenuController controller = mock(ParticleMenuController.class);

        when(particle.getPosition()).thenReturn(new Position(15.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> 
            new Position(((Integer)invocation.getArgument(0)).doubleValue(), ((Integer)invocation.getArgument(1)).doubleValue()));

        Position newPosition = zicoState.move(particle, 1000, controller);

        // X should remain unchanged
        assertEquals(15.0, newPosition.x(), "Particle X position should not change");
    }

    @Test
    void move_shouldAddNotSubtract() {
        // Verify that Y increases (addition), not decreases (subtraction)
        ZicoState zicoState = new ZicoState();
        Particle particle = mock(Particle.class);
        ParticleMenuController controller = mock(ParticleMenuController.class);

        when(particle.getPosition()).thenReturn(new Position(10.0, 50.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> 
            new Position(((Integer)invocation.getArgument(0)).doubleValue(), ((Integer)invocation.getArgument(1)).doubleValue()));

        Position newPosition = zicoState.move(particle, 1000, controller);

        // Should be 60, not 40
        assertTrue(newPosition.y() > 50.0, "Y should increase, not decrease");
        assertEquals(60.0, newPosition.y(), "Y should be 60 (50 + 10)");
    }

    @Test
    void move_shouldHandleNullReturnFromWrapPosition() {
        ZicoState zicoState = new ZicoState();
        Particle particle = mock(Particle.class);
        ParticleMenuController controller = mock(ParticleMenuController.class);

        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenReturn(null);

        Position newPosition = zicoState.move(particle, 1000, controller);

        assertNull(newPosition, "The position should be null if wrapPosition returns null");
    }
}