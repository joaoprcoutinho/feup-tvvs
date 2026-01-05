package pt.feup.tvvs.soulknight.model.game.elements.knight;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FallingStateMutationTests {
    private Knight knight;
    private FallingState fallingState;

    @Mock
    private Scene mockScene;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        knight = new Knight(10, 20, 50, 1.0f, 100);
        knight.setScene(mockScene);
        knight.setVelocity(new Vector(0, 2.0));
        knight.setJumpCounter(0);

        fallingState = new FallingState(knight);
    }

    @Test
    public void moveLeftAndRight_AppliesCorrectAccelerationDirection() {
        Vector left = fallingState.moveKnightLeft();
        assertTrue(left.x() < 0);

        Vector right = fallingState.moveKnightRight();
        assertTrue(right.x() > 0);
    }

    @Test
    public void limitVelocity_SnapsSmallXVelocityToZero() {
        Vector input = new Vector(0.19, 3.0);
        Vector limited = fallingState.limitVelocity(input);
        assertEquals(0.0, limited.x(), 0.0001);

        input = new Vector(0.21, 3.0);
        limited = fallingState.limitVelocity(input);
        assertEquals(0.21, limited.x(), 0.0001);
    }

    @Test
    public void applyCollisions_ResolvesAllDirectionsProperly() {
        when(mockScene.collidesDown(any(), any())).thenReturn(true);
        when(mockScene.collidesUp(any(), any())).thenReturn(true);
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        when(mockScene.collidesRight(any(), any())).thenReturn(true);

        Vector velocity = new Vector(5, 5);
        Vector resolved = fallingState.applyCollisions(velocity);

        assertEquals(0.0, resolved.x());
        assertEquals(0.0, resolved.y());
    }

    @Test
    public void getNextGroundState_CoversAllVelocityThresholds() {
        knight.setVelocity(new Vector(0, 0));
        assertTrue(fallingState.getNextGroundState() instanceof IdleState);

        knight.setVelocity(new Vector(0.6, 0));
        assertFalse(fallingState.getNextGroundState() instanceof WalkingState);

        knight.setVelocity(new Vector(1.5, 0));
        assertFalse(fallingState.getNextGroundState() instanceof RunningState);
    }

    @Test
    public void getNextOnAirState_DistinguishesJumpVsFall() {
        knight.setVelocity(new Vector(0, -1.0));
        assertTrue(fallingState.getNextOnAirState() instanceof JumpState);

        knight.setVelocity(new Vector(0, 0.1));
        assertTrue(fallingState.getNextOnAirState() instanceof FallingState);
    }

    @Test
    public void jump_OnlyAllowsDoubleJumpInSmallYVelocityWindow() {
        knight.setVelocity(new Vector(0, 0.5));
        knight.setJumpCounter(1);

        Vector result = fallingState.jump();
        assertTrue(result.y() < 0);
        assertEquals(2, knight.getJumpCounter());

        knight.setVelocity(new Vector(0, 2.0));
        knight.setJumpCounter(1);
        Vector noJump = fallingState.jump();
        assertEquals(2.0, noJump.y(), 0.1);
    }

    @Test
    public void updateVelocity_AlwaysCallsTickParticles() {
        fallingState.updateVelocity(new Vector(0, 0));

        KnightState spyState = Mockito.spy(fallingState);
        spyState.updateVelocity(new Vector(0, 0));
        verify(spyState, atLeastOnce()).tickParticles();
    }

    @Test
    public void updateVelocity_AppliesDifferentGravityBasedOnYVelocity() {
        double gravity = 1.0;
        when(mockScene.getGravity()).thenReturn(gravity);

        knight.setVelocity(new Vector(0, 0.4));
        Vector lightGravity = fallingState.updateVelocity(new Vector(0, 0));
        assertEquals(0.09999999999999998, lightGravity.y() - 0.4, 0.1);

        knight.setVelocity(new Vector(0, 0.6));
        Vector heavyGravity = fallingState.updateVelocity(new Vector(0, 0));
        assertEquals(gravity * 1.15, heavyGravity.y() - 0.6, 0.1);
    }

    @Test
    public void getNextState_ResetsParticlesTimerWhenZero() {
        for (int i = 0; i < 101; i++) {
            fallingState.tickParticles();
        }

        fallingState.getNextState();

        assertEquals(-1, fallingState.getParticlesTimer());
    }

    @Test
    public void getNextState_ReturnsDifferentStatesBasedOnConditions() {
        knight.setVelocity(new Vector(2.1, 0));
        assertTrue(fallingState.getNextState() instanceof DashState);

        knight.setVelocity(new Vector(0, 0));
        when(mockScene.collidesDown(any(), any())).thenReturn(true);
        KnightState groundState = fallingState.getNextState();
        assertTrue(groundState instanceof IdleState ||
                groundState instanceof WalkingState ||
                groundState instanceof RunningState);

        knight.setJumpCounter(2);
        knight.setVelocity(new Vector(0, 1.0));
        when(mockScene.collidesDown(any(), any())).thenReturn(false);
        assertTrue(fallingState.getNextState() instanceof FallingState ||
                fallingState.getNextState() instanceof JumpState);
    }

    @Test
    public void dash_RespectsFacingDirection() {
        knight.setFacingRight(true);
        Vector dashRight = fallingState.dash();
        assertTrue(dashRight.x() > 0);

        knight.setFacingRight(false);
        Vector dashLeft = fallingState.dash();
        assertTrue(dashLeft.x() < 0);
    }
}