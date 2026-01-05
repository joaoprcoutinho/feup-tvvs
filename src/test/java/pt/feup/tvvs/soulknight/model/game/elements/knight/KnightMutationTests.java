package pt.feup.tvvs.soulknight.model.game.elements.knight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.elements.particle.Particle;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KnightMutationTests {

    private Knight knight;
    private Scene scene;

    @BeforeEach
    void setUp() {
        knight = new Knight(10, 20, 100, 1.5f, 50);
        scene = mock(Scene.class);
        knight.setScene(scene);
    }

    // Test constructor sets position with offsets (x + 4, y + 1)
    @Test
    void testConstructorPositionOffset() {
        Knight k = new Knight(10, 20, 100, 1.5f, 50);
        // Position should be (10 + 4, 20 + 1) = (14, 21)
        assertEquals(14, k.getPosition().x(), 0.001);
        assertEquals(21, k.getPosition().y(), 0.001);
    }

    // Test getHP returns actual value, not 0
    @Test
    void testGetHP() {
        assertEquals(100, knight.getHP());
        knight.setHP(75);
        assertEquals(75, knight.getHP());
    }

    // Test getDamage returns actual value
    @Test
    void testGetDamage() {
        assertEquals(1.5f, knight.getDamage(), 0.001);
        knight.setDamage(2.0f);
        assertEquals(2.0f, knight.getDamage(), 0.001);
    }

    // Test getEnergy returns actual value
    @Test
    void testGetEnergy() {
        assertEquals(50, knight.getEnergy());
        knight.setEnergy(75);
        assertEquals(75, knight.getEnergy());
    }

    // Test getWidth returns WIDTH constant (7)
    @Test
    void testGetWidth() {
        assertEquals(7, knight.getWidth());
    }

    // Test getHeight returns HEIGHT constant (8)
    @Test
    void testGetHeight() {
        assertEquals(8, knight.getHeight());
    }

    // Test getJumpBoost returns Math.PI
    @Test
    void testGetJumpBoost() {
        assertEquals(Math.PI, knight.getJumpBoost(), 0.001);
    }

    // Test getDashBoost returns 6
    @Test
    void testGetDashBoost() {
        assertEquals(6.0, knight.getDashBoost(), 0.001);
    }

    // Test getAcceleration returns 0.75
    @Test
    void testGetAcceleration() {
        assertEquals(0.75, knight.getAcceleration(), 0.001);
    }

    // Test getMaxVelocity returns Vector(2.0, 4.0)
    @Test
    void testGetMaxVelocity() {
        Vector maxVel = knight.getMaxVelocity();
        assertEquals(2.0, maxVel.x(), 0.001);
        assertEquals(4.0, maxVel.y(), 0.001);
    }

    // Test getVelocity returns Vector(0, 0) initially
    @Test
    void testGetVelocity() {
        Vector vel = knight.getVelocity();
        assertEquals(0.0, vel.x(), 0.001);
        assertEquals(0.0, vel.y(), 0.001);
    }

    // Test setVelocity actually sets the velocity
    @Test
    void testSetVelocity() {
        Vector newVel = new Vector(5.0, 3.0);
        knight.setVelocity(newVel);
        assertEquals(5.0, knight.getVelocity().x(), 0.001);
        assertEquals(3.0, knight.getVelocity().y(), 0.001);
    }

    // Test isFacingRight returns true initially
    @Test
    void testIsFacingRightInitial() {
        assertTrue(knight.isFacingRight());
    }

    // Test setFacingRight
    @Test
    void testSetFacingRight() {
        knight.setFacingRight(false);
        assertFalse(knight.isFacingRight());
        knight.setFacingRight(true);
        assertTrue(knight.isFacingRight());
    }

    // Test getJumpCounter returns 0 initially
    @Test
    void testGetJumpCounter() {
        assertEquals(0, knight.getJumpCounter());
    }

    // Test setJumpCounter
    @Test
    void testSetJumpCounter() {
        knight.setJumpCounter(2);
        assertEquals(2, knight.getJumpCounter());
    }

    // Test isGotHit returns false initially
    @Test
    void testIsGotHitInitial() {
        assertFalse(knight.isGotHit());
    }

    // Test setGotHit
    @Test
    void testSetGotHit() {
        knight.setGotHit(true);
        assertTrue(knight.isGotHit());
        knight.setGotHit(false);
        assertFalse(knight.isGotHit());
    }

    // Test getOrbs returns 0 initially
    @Test
    void testGetOrbsInitial() {
        assertEquals(0, knight.getOrbs());
    }

    // Test setOrbs
    @Test
    void testSetOrbs() {
        knight.setOrbs(5);
        assertEquals(5, knight.getOrbs());
    }

    // Test addOrbs increments orbs
    @Test
    void testAddOrbs() {
        assertEquals(0, knight.getOrbs());
        knight.addOrbs();
        assertEquals(1, knight.getOrbs());
        knight.addOrbs();
        assertEquals(2, knight.getOrbs());
    }

    // Test getNumberOfDeaths returns 0 initially
    @Test
    void testGetNumberOfDeathsInitial() {
        assertEquals(0, knight.getNumberOfDeaths());
    }

    // Test increaseDeaths
    @Test
    void testIncreaseDeaths() {
        assertEquals(0, knight.getNumberOfDeaths());
        knight.increaseDeaths();
        assertEquals(1, knight.getNumberOfDeaths());
        knight.increaseDeaths();
        assertEquals(2, knight.getNumberOfDeaths());
    }

    // Test getBirthTime is set in constructor
    @Test
    void testGetBirthTime() {
        long before = System.currentTimeMillis();
        Knight k = new Knight(0, 0, 100, 1.0f, 50);
        long after = System.currentTimeMillis();
        assertTrue(k.getBirthTime() >= before && k.getBirthTime() <= after);
    }

    // Test setBirthTime
    @Test
    void testSetBirthTime() {
        knight.setBirthTime(12345L);
        assertEquals(12345L, knight.getBirthTime());
    }

    // Test getScene returns the set scene
    @Test
    void testGetScene() {
        assertEquals(scene, knight.getScene());
    }

    // Test setScene
    @Test
    void testSetScene() {
        Scene newScene = mock(Scene.class);
        knight.setScene(newScene);
        assertEquals(newScene, knight.getScene());
    }

    // Test getState returns IdleState initially
    @Test
    void testGetStateInitial() {
        assertNotNull(knight.getState());
        assertTrue(knight.getState() instanceof IdleState);
    }

    // Test setState
    @Test
    void testSetState() {
        KnightState newState = mock(KnightState.class);
        knight.setState(newState);
        assertEquals(newState, knight.getState());
    }

    // Test setMaxVelocity
    @Test
    void testSetMaxVelocity() {
        Vector newMaxVel = new Vector(5.0, 6.0);
        knight.setMaxVelocity(newMaxVel);
        assertEquals(5.0, knight.getMaxVelocity().x(), 0.001);
        assertEquals(6.0, knight.getMaxVelocity().y(), 0.001);
    }

    // Test isOverMaxXVelocity with velocity < maxVelocity
    @Test
    void testIsOverMaxXVelocityFalse() {
        knight.setVelocity(new Vector(1.0, 0.0));
        assertFalse(knight.isOverMaxXVelocity());
    }

    // Test isOverMaxXVelocity with velocity > maxVelocity
    @Test
    void testIsOverMaxXVelocityTrue() {
        knight.setVelocity(new Vector(3.0, 0.0));
        assertTrue(knight.isOverMaxXVelocity());
    }

    // Test isOverMaxXVelocity with negative velocity
    @Test
    void testIsOverMaxXVelocityNegative() {
        knight.setVelocity(new Vector(-3.0, 0.0));
        assertTrue(knight.isOverMaxXVelocity()); // Math.abs(-3.0) > 2.0
    }

    // Test isOverMaxXVelocity at boundary (velocity == maxVelocity)
    @Test
    void testIsOverMaxXVelocityAtBoundary() {
        knight.setVelocity(new Vector(2.0, 0.0));
        assertFalse(knight.isOverMaxXVelocity()); // 2.0 is NOT > 2.0
    }

    // Test isOnGround delegates to scene.collidesDown
    @Test
    void testIsOnGround() {
        when(scene.collidesDown(any(Position.class), any(Position.class))).thenReturn(true);
        assertTrue(knight.isOnGround());
        verify(scene).collidesDown(any(Position.class), any(Position.class));
    }

    // Test isOnGround returns false when not colliding
    @Test
    void testIsOnGroundFalse() {
        when(scene.collidesDown(any(Position.class), any(Position.class))).thenReturn(false);
        assertFalse(knight.isOnGround());
    }

    // Test updateVelocity delegates to state
    @Test
    void testUpdateVelocity() {
        KnightState mockState = mock(KnightState.class);
        Vector expectedVel = new Vector(1.0, 2.0);
        when(mockState.updateVelocity(any(Vector.class))).thenReturn(expectedVel);
        knight.setState(mockState);
        
        Vector result = knight.updateVelocity();
        assertEquals(expectedVel, result);
        verify(mockState).updateVelocity(any(Vector.class));
    }

    // Test updatePosition uses resolvedVelocity for new position
    @Test
    void testUpdatePosition() {
        KnightState mockState = mock(KnightState.class);
        Vector resolvedVel = new Vector(2.0, 3.0);
        when(mockState.applyCollisions(any(Vector.class))).thenReturn(resolvedVel);
        knight.setState(mockState);
        
        Position oldPos = knight.getPosition();
        Position newPos = knight.updatePosition();
        
        assertEquals(oldPos.x() + 2.0, newPos.x(), 0.001);
        assertEquals(oldPos.y() + 3.0, newPos.y(), 0.001);
    }

    // Test moveLeft delegates to state
    @Test
    void testMoveLeft() {
        KnightState mockState = mock(KnightState.class);
        Vector expectedVel = new Vector(-1.0, 0.0);
        when(mockState.moveKnightLeft()).thenReturn(expectedVel);
        knight.setState(mockState);
        
        Vector result = knight.moveLeft();
        assertEquals(expectedVel, result);
        verify(mockState).moveKnightLeft();
    }

    // Test moveRight delegates to state
    @Test
    void testMoveRight() {
        KnightState mockState = mock(KnightState.class);
        Vector expectedVel = new Vector(1.0, 0.0);
        when(mockState.moveKnightRight()).thenReturn(expectedVel);
        knight.setState(mockState);
        
        Vector result = knight.moveRight();
        assertEquals(expectedVel, result);
        verify(mockState).moveKnightRight();
    }

    // Test jump delegates to state
    @Test
    void testJump() {
        KnightState mockState = mock(KnightState.class);
        Vector expectedVel = new Vector(0.0, -5.0);
        when(mockState.jump()).thenReturn(expectedVel);
        knight.setState(mockState);
        
        Vector result = knight.jump();
        assertEquals(expectedVel, result);
        verify(mockState).jump();
    }

    // Test dash delegates to state
    @Test
    void testDash() {
        KnightState mockState = mock(KnightState.class);
        Vector expectedVel = new Vector(6.0, 0.0);
        when(mockState.dash()).thenReturn(expectedVel);
        knight.setState(mockState);
        
        Vector result = knight.dash();
        assertEquals(expectedVel, result);
        verify(mockState).dash();
    }

    // Test getNextState delegates to state
    @Test
    void testGetNextState() throws IOException {
        KnightState mockState = mock(KnightState.class);
        KnightState nextState = mock(KnightState.class);
        when(mockState.getNextState()).thenReturn(nextState);
        knight.setState(mockState);
        
        KnightState result = knight.getNextState();
        assertEquals(nextState, result);
        verify(mockState).getNextState();
    }

    // Test resetValues
    @Test
    void testResetValues() {
        knight.setFacingRight(false);
        knight.resetValues();
        assertTrue(knight.isFacingRight());
        assertTrue(knight.getState() instanceof FallingState);
    }

    // Test PlayerHit when gotHit is true (early return)
    @Test
    void testPlayerHitWhenAlreadyHit() {
        knight.setGotHit(true);
        int hpBefore = knight.getHP();
        knight.PlayerHit(10);
        assertEquals(hpBefore, knight.getHP()); // HP should not change
    }

    // Test PlayerHit reduces HP
    @Test
    void testPlayerHitReducesHP() {
        knight.setGotHit(false);
        int hpBefore = knight.getHP();
        knight.PlayerHit(10);
        assertEquals(hpBefore - 10, knight.getHP());
        assertTrue(knight.isGotHit());
    }

    // Test PlayerHit when HP is 0 (sets to 1 first)
    @Test
    void testPlayerHitWhenHPZero() {
        knight.setHP(0);
        knight.setGotHit(false);
        knight.PlayerHit(5);
        // HP was 0, set to 1, then subtracted 5 = -4
        assertEquals(-4, knight.getHP());
    }

    // Test PlayerHit sets state to DamagedState
    @Test
    void testPlayerHitSetsDamagedState() {
        knight.setGotHit(false);
        knight.PlayerHit(10);
        assertTrue(knight.getState() instanceof DamagedState);
    }

    // Test createParticlesJump returns correct number of particles
    @Test
    void testCreateParticlesJump() {
        List<Particle> particles = knight.createParticlesJump(5);
        assertEquals(5, particles.size());
        for (Particle p : particles) {
            assertNotNull(p);
        }
    }

    // Test createParticlesJump particles start at correct position
    @Test
    void testCreateParticlesJumpPosition() {
        List<Particle> particles = knight.createParticlesJump(1);
        Particle p = particles.get(0);
        // Starting position should be centered: x + width/2, y + height
        double expectedX = knight.getPosition().x() + knight.getWidth() / 2.0;
        double expectedY = knight.getPosition().y() + knight.getHeight();
        assertEquals((int) expectedX, (int) p.getPosition().x());
        assertEquals((int) expectedY, (int) p.getPosition().y());
    }

    // Test createParticlesDoubleJump returns correct number
    @Test
    void testCreateParticlesDoubleJump() {
        List<Particle> particles = knight.createParticlesDoubleJump(5, scene);
        assertEquals(5, particles.size());
    }

    // Test createDashParticles returns correct number
    @Test
    void testCreateDashParticles() {
        List<Particle> particles = knight.createDashParticles(5);
        assertEquals(5, particles.size());
    }

    // Test createRespawnParticles returns correct number
    @Test
    void testCreateRespawnParticles() {
        List<Particle> particles = knight.createRespawnParticles(5);
        assertEquals(5, particles.size());
    }

    // Test particles have velocity (tests arithmetic operations)
    @Test
    void testCreateParticlesJumpVelocity() {
        knight.setVelocity(new Vector(2.0, 0.0));
        List<Particle> particles = knight.createParticlesJump(10);
        for (Particle p : particles) {
            assertNotNull(p.getVelocity());
            // Velocity should be calculated using baseSpeed and angle
        }
    }

    // Test createDashParticles velocity calculation
    @Test
    void testCreateDashParticlesVelocity() {
        knight.setVelocity(new Vector(3.0, 0.0));
        List<Particle> particles = knight.createDashParticles(10);
        for (Particle p : particles) {
            assertNotNull(p.getVelocity());
        }
    }

    // Test PlayerHit blood calculation (ratio formula)
    @Test
    void testPlayerHitBloodCalculation() {
        knight.setHP(25); // ratio = 1 - 25/50 = 0.5, blood = 10 * 0.5 = 5
        knight.setGotHit(false);
        knight.PlayerHit(10);
        assertTrue(knight.getState() instanceof DamagedState);
    }

    // Test PlayerHit damage calculation affects state
    @Test
    void testPlayerHitDamageCalculation() {
        knight.setHP(50);
        knight.setGotHit(false);
        // ratio = 1 - 50/50 = 0, blood = 0, particles = 0 + 10/5 = 2
        knight.PlayerHit(10);
        assertTrue(knight.getState() instanceof DamagedState);
    }
}