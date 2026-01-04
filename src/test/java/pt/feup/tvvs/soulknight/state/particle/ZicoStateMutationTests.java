package pt.feup.tvvs.soulknight.state.particle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

        when(particle.getPosition()).thenReturn(new Position(10, 10));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> new Position(invocation.getArgument(0), invocation.getArgument(1)));

        Position newPosition = zicoState.move(particle, 1000, controller);

        // Check that the y position is incremented by 10 as specified
        assertEquals(20, newPosition.y(), "Particle Y position should be increased by 10");
    }

    @Test
    void move_shouldHandleNullReturnFromWrapPosition() {
        ZicoState zicoState = new ZicoState();
        Particle particle = mock(Particle.class);
        ParticleMenuController controller = mock(ParticleMenuController.class);

        when(particle.getPosition()).thenReturn(new Position(10, 10));
        when(controller.wrapPosition(anyInt(), anyInt())).thenReturn(null);

        Position newPosition = zicoState.move(particle, 1000, controller);

        assertNull(newPosition, "The position should be null if wrapPosition returns null");
    }
}