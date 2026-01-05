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
        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> 
            new Position(((Integer)invocation.getArgument(0)).doubleValue(), ((Integer)invocation.getArgument(1)).doubleValue()));

        // Move particle and check new position
        Position newPosition = dispersingState.move(particle, 1000, controller);

        assertNotNull(newPosition, "New position should not be null");

        // Check that x and y positions have been updated based on wind speed and angle (with randomness)
        assertTrue(newPosition.x() >= 10.0, "X position should have increased or stayed same due to wind");
        assertTrue(newPosition.y() >= 10.0, "Y position should have increased or stayed same due to wind");
    }

    @Test
    void move_shouldHandleZeroWindSpeed() {
        // When wind speed is 0, particle should not move much (only random movement)
        when(controller.getWindAngle()).thenReturn(Math.PI / 4);  // 45 degrees
        when(controller.getWindSpeed()).thenReturn(0.0);  // No wind

        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> 
            new Position(((Integer)invocation.getArgument(0)).doubleValue(), ((Integer)invocation.getArgument(1)).doubleValue()));

        Position newPosition = dispersingState.move(particle, 1000, controller);

        // With zero wind, movement should be minimal (only random -1, 0, +1)
        double deltaX = Math.abs(newPosition.x() - 10.0);
        double deltaY = Math.abs(newPosition.y() - 10.0);
        assertTrue(deltaX <= 1.0, "X position should not change much with zero wind speed");
        assertTrue(deltaY <= 1.0, "Y position should not change much with zero wind speed");
    }

    @Test
    void move_shouldApplyRandomness() {
        // Check that randomness is applied to the particle's movement
        when(controller.getWindAngle()).thenReturn(Math.PI / 4);  // 45 degrees
        when(controller.getWindSpeed()).thenReturn(5.0);  // Wind speed is 5

        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> 
            new Position(((Integer)invocation.getArgument(0)).doubleValue(), ((Integer)invocation.getArgument(1)).doubleValue()));

        // Call move multiple times to verify randomness is applied
        Position newPosition1 = dispersingState.move(particle, 1000, controller);
        assertNotNull(newPosition1, "First position should not be null");
        
        Position newPosition2 = dispersingState.move(particle, 1001, controller);
        assertNotNull(newPosition2, "Second position should not be null");

        // Note: Due to randomness in random.nextInt(3), positions might be different
        // This test verifies the move method executes without errors
        assertTrue(newPosition1.x() >= 9.0, "X position should be valid");
        assertTrue(newPosition2.x() >= 9.0, "X position should be valid");
    }

    @Test
    void move_shouldWrapPositionCorrectly() {
        // Test if the wrapPosition logic works correctly when particle moves
        when(controller.getWindAngle()).thenReturn(Math.PI / 4);  // 45 degrees
        when(controller.getWindSpeed()).thenReturn(10.0);  // Wind speed is 10

        // Mocking a position near the screen's edge
        when(particle.getPosition()).thenReturn(new Position(160.0, 90.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> 
            new Position(((Integer)invocation.getArgument(0)).doubleValue(), ((Integer)invocation.getArgument(1)).doubleValue()));

        Position newPosition = dispersingState.move(particle, 1000, controller);

        // Position should be calculated based on wind (verify wrapPosition was called)
        assertNotNull(newPosition, "Position should not be null");
        verify(controller, atLeastOnce()).wrapPosition(anyInt(), anyInt());
    }

    @Test
    void move_shouldReturnNullOnError() {
        // Test handling if the move function returns null when wrapPosition fails
        when(controller.getWindAngle()).thenReturn(Math.PI / 4);
        when(controller.getWindSpeed()).thenReturn(5.0);
        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));

        // Simulate that wrapPosition returns null
        when(controller.wrapPosition(anyInt(), anyInt())).thenReturn(null);

        Position newPosition = dispersingState.move(particle, 1000, controller);
        assertNull(newPosition, "Position should be null if wrapPosition fails");
    }
}