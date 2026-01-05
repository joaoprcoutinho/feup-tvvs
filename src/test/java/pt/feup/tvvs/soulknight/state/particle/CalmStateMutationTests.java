package pt.feup.tvvs.soulknight.state.particle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import pt.feup.tvvs.soulknight.controller.menu.ParticleMenuController;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.menu.Particle;

import java.util.Random;

public class CalmStateMutationTests {
    private CalmState calmState;
    private Particle particle;
    private ParticleMenuController controller;

    @BeforeEach
    void setUp() {
        calmState = new CalmState();
        particle = mock(Particle.class);
        controller = mock(ParticleMenuController.class);
    }

    @Test
    void move_shouldMoveHorizontallyRandomly() {
        // Given that the x value changes randomly by -1, 0, or +1
        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> 
            new Position(((Integer)invocation.getArgument(0)).doubleValue(), ((Integer)invocation.getArgument(1)).doubleValue()));

        Position newPosition = calmState.move(particle, 1000, controller);

        // Ensure the particle moved horizontally by -1, 0, or +1
        double deltaX = newPosition.x() - 10.0;
        assertTrue(deltaX >= -1 && deltaX <= 1, "Particle should move by -1, 0, or +1 on the x-axis");

        // Ensure vertical movement is 1 (downward)
        assertEquals(11.0, newPosition.y(), "Particle should move down by 1 unit on the y-axis");
    }

    @Test
    void move_shouldHandleVerticalReversal() {
        // Test that mutation changing y+1 to y-1 is caught
        calmState = new CalmState();

        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> 
            new Position(((Integer)invocation.getArgument(0)).doubleValue(), ((Integer)invocation.getArgument(1)).doubleValue()));

        Position newPosition = calmState.move(particle, 1000, controller);

        // The vertical movement should be downward (y + 1), not upward
        assertEquals(11.0, newPosition.y(), 0.1, "Particle should move down by 1 unit on the y-axis");
    }

    @Test
    void move_shouldHandleNullReturnFromWrapPosition() {
        // Simulate that the wrapPosition function fails and returns null
        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenReturn(null);

        Position newPosition = calmState.move(particle, 1000, controller);

        // Ensure that the system can handle a null return
        assertNull(newPosition, "The position should be null if wrapPosition returns null");
    }

    @Test
    void move_shouldMoveHorizontallyOnlyInPositiveDirection() {
        // Test that horizontal movement can be negative, zero, or positive
        calmState = new CalmState();

        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> 
            new Position(((Integer)invocation.getArgument(0)).doubleValue(), ((Integer)invocation.getArgument(1)).doubleValue()));

        Position newPosition = calmState.move(particle, 1000, controller);

        // Ensure horizontal movement is in the range [-1, 1]
        double deltaX = newPosition.x() - 10.0;
        assertTrue(deltaX >= -1 && deltaX <= 1, "Particle should move by -1, 0, or +1 on the x-axis");

        // Ensure vertical movement is 1 (downward)
        assertEquals(11.0, newPosition.y(), "Particle should move down by 1 unit on the y-axis");
    }
}