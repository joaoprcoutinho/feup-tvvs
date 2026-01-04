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
        when(particle.getPosition()).thenReturn(new Position(10, 10));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> new Position(invocation.getArgument(0), invocation.getArgument(1)));

        Position newPosition = calmState.move(particle, 1000, controller);

        // Ensure the particle moved horizontally by -1, 0, or +1
        double deltaX = newPosition.x() - 10;
        assertTrue(deltaX >= -1 && deltaX <= 1, "Particle should move by -1, 0, or +1 on the x-axis");

        // Ensure vertical movement is 1 (downward)
        assertEquals(newPosition.y(), 11, "Particle should move down by 1 unit on the y-axis");
    }

    @Test
    void move_shouldHandleVerticalReversal() {
        // Test behavior when vertical movement is reversed (y - 1)
        calmState = new CalmState();

        when(particle.getPosition()).thenReturn(new Position(10, 10));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> new Position(invocation.getArgument(0), invocation.getArgument(1)));

        Position newPosition = calmState.move(particle, 1000, controller);

        // The vertical movement should be reversed and go upwards (y - 1)
        assertEquals(newPosition.y(), 9, "Particle should move up by 1 unit on the y-axis");
    }

    @Test
    void move_shouldHandleNullReturnFromWrapPosition() {
        // Simulate that the wrapPosition function fails and returns null
        when(controller.wrapPosition(anyInt(), anyInt())).thenReturn(null);

        Position newPosition = calmState.move(particle, 1000, controller);

        // Ensure that the system can handle a null return
        assertNull(newPosition, "The position should be null if wrapPosition returns null");
    }

    @Test
    void move_shouldMoveHorizontallyOnlyInPositiveDirection() {
        // Test case where we ensure horizontal movement is only positive (if the mutation makes it so)
        calmState = new CalmState();

        when(particle.getPosition()).thenReturn(new Position(10, 10));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> new Position(invocation.getArgument(0), invocation.getArgument(1)));

        Position newPosition = calmState.move(particle, 1000, controller);

        // Ensure horizontal movement is only positive (0 or 1)
        double deltaX = newPosition.x() - 10;
        assertTrue(deltaX >= 0 && deltaX <= 1, "Particle should only move to the right (positive direction)");

        // Ensure vertical movement is 1 (downward)
        assertEquals(newPosition.y(), 11, "Particle should move down by 1 unit on the y-axis");
    }
}