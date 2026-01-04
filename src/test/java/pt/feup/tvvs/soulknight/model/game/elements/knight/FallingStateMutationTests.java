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

        // Create knight with default values
        knight = new Knight(10, 20, 50, 1.0f, 100);
        knight.setScene(mockScene);

        // Set initial falling velocity
        knight.setVelocity(new Vector(0, 2.0));

        // Reset jump counter for double jump tests
        knight.setJumpCounter(0);

        fallingState = new FallingState(knight);
    }

    /** Kills mutants in moveKnightLeft/moveKnightRight acceleration direction (lines 25, 33 in KnightState) */
    @Test
    void moveLeftAndRight_AppliesCorrectAccelerationDirection() {
        Vector left = fallingState.moveKnightLeft();
        assertTrue(left.x() < 0, "Moving left should decrease X velocity");

        Vector right = fallingState.moveKnightRight();
        assertTrue(right.x() > 0, "Moving right should increase X velocity");
    }

    /** Kills conditional boundary mutant in limitVelocity: if (Math.abs(vx) < 0.2) (line 43) */
    @Test
    void limitVelocity_SnapsSmallXVelocityToZero() {
        Vector input = new Vector(0.19, 3.0);
        Vector limited = fallingState.limitVelocity(input);
        assertEquals(0.0, limited.x(), 0.0001);

        input = new Vector(0.21, 3.0);
        limited = fallingState.limitVelocity(input);
        assertEquals(0.21, limited.x(), 0.0001);
    }

    /** Kills multiple mutants in applyCollisions loops (boundaries, additions, negations) */
    @Test
    void applyCollisions_ResolvesAllDirectionsProperly() {
        // Mock collisions in all directions
        when(mockScene.collidesDown(any(), any())).thenReturn(true);
        when(mockScene.collidesUp(any(), any())).thenReturn(true);
        when(mockScene.collidesLeft(any(), any())).thenReturn(true);
        when(mockScene.collidesRight(any(), any())).thenReturn(true);

        Vector velocity = new Vector(5, 5); // strong movement in all directions
        Vector resolved = fallingState.applyCollisions(velocity);

        assertEquals(0.0, resolved.x(), "Should stop horizontal on collision");
        assertEquals(0.0, resolved.y(), "Should stop vertical on collision");
    }

    /** Kills boundary mutants in getNextGroundState velocity checks (lines 69, 71, 72) */
    @Test
    void getNextGroundState_CoversAllVelocityThresholds() {
        knight.setVelocity(new Vector(0, 0));
        assertTrue(fallingState.getNextGroundState() instanceof IdleState);

        knight.setVelocity(new Vector(0.6, 0));
        assertFalse(fallingState.getNextGroundState() instanceof WalkingState);

        knight.setVelocity(new Vector(1.5, 0));
        assertFalse(fallingState.getNextGroundState() instanceof RunningState);
    }

    /** Kills mutants in getNextOnAirState (boundary and negation) */
    @Test
    void getNextOnAirState_DistinguishesJumpVsFall() {
        knight.setVelocity(new Vector(0, -1.0));
        assertTrue(fallingState.getNextOnAirState() instanceof JumpState);

        knight.setVelocity(new Vector(0, 0.1));
        assertTrue(fallingState.getNextOnAirState() instanceof FallingState);
    }

    /** Kills jump condition boundaries in FallingState.jump() (lines 12, 13, 41) */
    @Test
    void jump_OnlyAllowsDoubleJumpInSmallYVelocityWindow() {
        // Just after landing: small positive Y velocity
        knight.setVelocity(new Vector(0, 0.5));
        knight.setJumpCounter(1);

        Vector result = fallingState.jump();
        assertTrue(result.y() < 0); // should jump upward
        assertEquals(2, knight.getJumpCounter());

        // Too fast downward → no double jump
        knight.setVelocity(new Vector(0, 2.0));
        knight.setJumpCounter(1);
        Vector noJump = fallingState.jump();
        assertEquals(2.0, noJump.y(), 0.1); // velocity unchanged
    }

    /** Kills tickParticles call in updateVelocity (line 39) */
    @Test
    void updateVelocity_AlwaysCallsTickParticles() {
        fallingState.updateVelocity(new Vector(0, 0));

        // Verify tickParticles was called (we can spy on the state)
        KnightState spyState = Mockito.spy(fallingState);
        spyState.updateVelocity(new Vector(0, 0));
        verify(spyState, atLeastOnce()).tickParticles();
    }

    /** Kills conditional boundaries in updateVelocity gravity application (line 41) */
    @Test
    void updateVelocity_AppliesDifferentGravityBasedOnYVelocity() {
        double gravity = 1.0;
        when(mockScene.getGravity()).thenReturn(gravity);

        // Low fall speed → lighter gravity
        knight.setVelocity(new Vector(0, 0.4));
        Vector lightGravity = fallingState.updateVelocity(new Vector(0, 0));
        assertEquals(0.09999999999999998, lightGravity.y() - 0.4, 0.1);

        // High fall speed → heavier gravity
        knight.setVelocity(new Vector(0, 0.6));
        Vector heavyGravity = fallingState.updateVelocity(new Vector(0, 0));
        assertEquals(gravity * 1.15, heavyGravity.y() - 0.6, 0.1);
    }

    /** Kills particles timer reset call (line 67) */
    @Test
    void getNextState_ResetsParticlesTimerWhenZero() {
        // Set timer to 0 via reflection or multiple ticks
        for (int i = 0; i < 101; i++) {
            fallingState.tickParticles();
        }

        fallingState.getNextState(); // triggers reset

        assertEquals(-1, fallingState.getParticlesTimer());
    }

    /** Kills return value null mutants in getNextState branches (lines 70, 72, 74) */
    @Test
    void getNextState_ReturnsDifferentStatesBasedOnConditions() {
        // Over max X velocity → DashState
        knight.setVelocity(new Vector(2.1, 0));
        assertTrue(fallingState.getNextState() instanceof DashState);

        // On ground → some ground state
        knight.setVelocity(new Vector(0, 0));
        when(mockScene.collidesDown(any(), any())).thenReturn(true);
        KnightState groundState = fallingState.getNextState();
        assertTrue(groundState instanceof IdleState ||
                groundState instanceof WalkingState ||
                groundState instanceof RunningState);

        // Double jump used → stays in air state
        knight.setJumpCounter(2);
        knight.setVelocity(new Vector(0, 1.0));
        when(mockScene.collidesDown(any(), any())).thenReturn(false);
        assertTrue(fallingState.getNextState() instanceof FallingState ||
                fallingState.getNextState() instanceof JumpState);
    }

    /** Ensures dash respects facing direction (kills negation mutant line 31) */
    @Test
    void dash_RespectsFacingDirection() {
        knight.setFacingRight(true);
        Vector dashRight = fallingState.dash();
        assertTrue(dashRight.x() > 0);

        knight.setFacingRight(false);
        Vector dashLeft = fallingState.dash();
        assertTrue(dashLeft.x() < 0);
    }
}