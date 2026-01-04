package pt.feup.tvvs.soulknight.state.particle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import pt.feup.tvvs.soulknight.controller.menu.ParticleMenuController;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.menu.Particle;

import java.util.Random;

public class DispersingStateMutationTests {
    private DispersingState dispersingState;
    private Particle particle;
    private ParticleMenuController controller;

    @BeforeEach
    void setUp() {
        dispersingState = new DispersingState();
        particle = mock(Particle.class);
        controller = mock(ParticleMenuController.class);
    }

    @Test
    void move_shouldCalculateNewPositionBasedOnWind() {
        // Mocking wind angle and speed
        when(controller.getWindAngle()).thenReturn(Math.PI / 4);  // 45 degrees
        when(controller.getWindSpeed()).thenReturn(5.0);  // Speed = 5 units per tick

        // Mocking the particle's current position
        when(particle.getPosition()).thenReturn(new Position(10, 10));

        // Move particle and check new position
        Position newPosition = dispersingState.move(particle, 1000, controller);

        assertNotNull(newPosition, "New position should not be null");

        // Check that x and y positions have been updated based on wind speed and angle
        assertTrue(newPosition.x() > 10, "X position should have increased due to wind");
        assertTrue(newPosition.y() > 10, "Y position should have increased due to wind");
    }

    @Test
    void move_shouldHandleZeroWindSpeed() {
        // When wind speed is 0, particle should not move
        when(controller.getWindAngle()).thenReturn(Math.PI / 4);  // 45 degrees
        when(controller.getWindSpeed()).thenReturn(0.0);  // No movement

        when(particle.getPosition()).thenReturn(new Position(10, 10));

        Position newPosition = dispersingState.move(particle, 1000, controller);

        // The new position should be the same as the original one
        assertEquals(10, newPosition.x(), "X position should not change");
        assertEquals(10, newPosition.y(), "Y position should not change");
    }

    @Test
    void move_shouldApplyRandomness() {
        // Check that randomness is applied to the particle's movement
        when(controller.getWindAngle()).thenReturn(Math.PI / 4);  // 45 degrees
        when(controller.getWindSpeed()).thenReturn(5.0);  // Wind speed is 5

        when(particle.getPosition()).thenReturn(new Position(10, 10));

        Position newPosition1 = dispersingState.move(particle, 1000, controller);
        Position newPosition2 = dispersingState.move(particle, 1001, controller);

        // The two positions should be different due to randomness
        assertNotEquals(newPosition1, newPosition2, "The two positions should not be the same due to randomness");
    }

    @Test
    void move_shouldWrapPositionCorrectly() {
        // Test if the wrapPosition logic works correctly when particle moves out of bounds
        when(controller.getWindAngle()).thenReturn(Math.PI / 4);  // 45 degrees
        when(controller.getWindSpeed()).thenReturn(10.0);  // Wind speed is 10

        // Mocking a position near the screen's edge
        when(particle.getPosition()).thenReturn(new Position(160, 90));

        Position newPosition = dispersingState.move(particle, 1000, controller);

        // Position should be wrapped around if it exceeds the screen size (assuming 160x90 screen)
        assertTrue(newPosition.x() < 160, "X position should wrap around screen");
        assertTrue(newPosition.y() < 90, "Y position should wrap around screen");
    }

    @Test
    void move_shouldReturnNullOnError() {
        // Test handling if the move function might return null (unlikely, but for mutation test)
        when(controller.getWindAngle()).thenReturn(Math.PI / 4);
        when(controller.getWindSpeed()).thenReturn(5.0);
        when(particle.getPosition()).thenReturn(new Position(10, 10));

        // Simulate that move() might return null for some reason (perhaps an invalid state)
        when(controller.wrapPosition(anyInt(), anyInt())).thenReturn(null);

        Position newPosition = dispersingState.move(particle, 1000, controller);
        assertNull(newPosition, "Position should be null if wrapPosition fails");
    }
}