package pt.feup.tvvs.soulknight.state.particle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import java.io.IOException;

import pt.feup.tvvs.soulknight.controller.menu.ParticleMenuController;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.menu.Particle;

public class WindyStateMutationTests {

    @Test
    void move_shouldMoveAccordingToWindAngleAndSpeed() {
        WindyState windyState = new WindyState();
        Particle particle = mock(Particle.class);
        ParticleMenuController controller = mock(ParticleMenuController.class);

        when(particle.getPosition()).thenReturn(new Position(10, 10));
        when(controller.getWindAngle()).thenReturn(Math.PI / 4); // 45 degrees
        when(controller.getWindSpeed()).thenReturn(5.0);
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> new Position(invocation.getArgument(0), invocation.getArgument(1)));

        Position newPosition = windyState.move(particle, 1000, controller);

        // Check that the x and y positions have been updated according to wind speed and angle
        double expectedX = 10 + (int) (5.0 * Math.cos(Math.PI / 4));
        double expectedY = 10 + (int) (5.0 * Math.sin(Math.PI / 4));

        assertEquals(expectedX, newPosition.x(), "Particle X position should be updated according to wind angle and speed");
        assertEquals(expectedY, newPosition.y(), "Particle Y position should be updated according to wind angle and speed");
    }

    @Test
    void move_shouldHandleNullReturnFromWrapPosition() {
        WindyState windyState = new WindyState();
        Particle particle = mock(Particle.class);
        ParticleMenuController controller = mock(ParticleMenuController.class);

        when(particle.getPosition()).thenReturn(new Position(10, 10));
        when(controller.getWindAngle()).thenReturn(Math.PI / 4);
        when(controller.getWindSpeed()).thenReturn(5.0);
        when(controller.wrapPosition(anyInt(), anyInt())).thenReturn(null);

        Position newPosition = windyState.move(particle, 1000, controller);

        assertNull(newPosition, "The position should be null if wrapPosition returns null");
    }
}
