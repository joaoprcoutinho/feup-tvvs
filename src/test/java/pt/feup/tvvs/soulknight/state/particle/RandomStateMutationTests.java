package pt.feup.tvvs.soulknight.state.particle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import java.io.IOException;

import pt.feup.tvvs.soulknight.controller.menu.ParticleMenuController;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.menu.Particle;

import java.util.Random;

public class RandomStateMutationTests {

    @Test
    void move_shouldMoveHorizontallyRandomly() {
        RandomState randomState = new RandomState();
        Particle particle = mock(Particle.class);
        ParticleMenuController controller = mock(ParticleMenuController.class);

        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> 
            new Position(((Integer)invocation.getArgument(0)).doubleValue(), ((Integer)invocation.getArgument(1)).doubleValue()));

        Position newPosition = randomState.move(particle, 1000, controller);

        double deltaX = newPosition.x() - 10.0;
        assertTrue(deltaX >= -1 && deltaX <= 1, "Particle should move by -1, 0, or +1 on the x-axis");

        double deltaY = newPosition.y() - 10.0;
        assertTrue(deltaY >= 1 && deltaY <= 3, "Particle should move by 1, 2, or 3 on the y-axis");
    }

    @Test
    void move_shouldNotMoveYByZero() {
        // Test that Y movement is always at least 1 (random.nextInt(3) + 1)
        RandomState randomState = new RandomState();
        Particle particle = mock(Particle.class);
        ParticleMenuController controller = mock(ParticleMenuController.class);

        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> 
            new Position(((Integer)invocation.getArgument(0)).doubleValue(), ((Integer)invocation.getArgument(1)).doubleValue()));

        Position newPosition = randomState.move(particle, 1000, controller);

        // Y should always increase (y + random.nextInt(3) + 1), never be 10 or less
        assertTrue(newPosition.y() > 10.0, "Y should always increase");
    }

    @Test
    void move_shouldUseAdditionNotSubtraction() {
        // Verify that mutations changing + to - are caught
        RandomState randomState = new RandomState();
        Particle particle = mock(Particle.class);
        ParticleMenuController controller = mock(ParticleMenuController.class);

        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> 
            new Position(((Integer)invocation.getArgument(0)).doubleValue(), ((Integer)invocation.getArgument(1)).doubleValue()));

        Position newPosition = randomState.move(particle, 1000, controller);

        // Y should be at least 11 (10 + 0 + 1), not 9 or less (would be subtraction)
        assertTrue(newPosition.y() >= 11.0, "Y movement should use addition");
    }

    @Test
    void move_shouldHandleSubtractionInYMovement() {
        RandomState randomState = new RandomState();
        Particle particle = mock(Particle.class);
        ParticleMenuController controller = mock(ParticleMenuController.class);

        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenAnswer(invocation -> 
            new Position(((Integer)invocation.getArgument(0)).doubleValue(), ((Integer)invocation.getArgument(1)).doubleValue()));

        Position newPosition = randomState.move(particle, 1000, controller);

        double deltaY = newPosition.y() - 10.0;
        assertTrue(deltaY >= 1 && deltaY <= 3, "Particle should move by 1, 2, or 3 on the y-axis");
    }

    @Test
    void move_shouldHandleNullReturnFromWrapPosition() {
        RandomState randomState = new RandomState();
        Particle particle = mock(Particle.class);
        ParticleMenuController controller = mock(ParticleMenuController.class);

        when(particle.getPosition()).thenReturn(new Position(10.0, 10.0));
        when(controller.wrapPosition(anyInt(), anyInt())).thenReturn(null);

        Position newPosition = randomState.move(particle, 1000, controller);

        assertNull(newPosition, "The position should be null if wrapPosition returns null");
    }
}
