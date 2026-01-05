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

    @Test
    public void testConstructorPositionOffset() {
        Knight k = new Knight(10, 20, 100, 1.5f, 50);
        assertEquals(14, k.getPosition().x(), 0.001);
        assertEquals(21, k.getPosition().y(), 0.001);
    }

    @Test
    public void testGetHP() {
        assertEquals(100, knight.getHP());
        knight.setHP(75);
        assertEquals(75, knight.getHP());
    }

    @Test
    public void testGetDamage() {
        assertEquals(1.5f, knight.getDamage(), 0.001);
        knight.setDamage(2.0f);
        assertEquals(2.0f, knight.getDamage(), 0.001);
    }

    @Test
    public void testGetEnergy() {
        assertEquals(50, knight.getEnergy());
        knight.setEnergy(75);
        assertEquals(75, knight.getEnergy());
    }

    @Test
    public void testGetWidth() {
        assertEquals(7, knight.getWidth());
    }

    @Test
    public void testGetHeight() {
        assertEquals(8, knight.getHeight());
    }

    @Test
    public void testGetJumpBoost() {
        assertEquals(Math.PI, knight.getJumpBoost(), 0.001);
    }

    @Test
    public void testGetDashBoost() {
        assertEquals(6.0, knight.getDashBoost(), 0.001);
    }

    @Test
    public void testGetAcceleration() {
        assertEquals(0.75, knight.getAcceleration(), 0.001);
    }

    @Test
    public void testGetMaxVelocity() {
        Vector maxVel = knight.getMaxVelocity();
        assertEquals(2.0, maxVel.x(), 0.001);
        assertEquals(4.0, maxVel.y(), 0.001);
    }

    @Test
    public void testGetVelocity() {
        Vector vel = knight.getVelocity();
        assertEquals(0.0, vel.x(), 0.001);
        assertEquals(0.0, vel.y(), 0.001);
    }

    @Test
    public void testSetVelocity() {
        Vector newVel = new Vector(5.0, 3.0);
        knight.setVelocity(newVel);
        assertEquals(5.0, knight.getVelocity().x(), 0.001);
        assertEquals(3.0, knight.getVelocity().y(), 0.001);
    }

    @Test
    public void testIsFacingRightInitial() {
        assertTrue(knight.isFacingRight());
    }

    @Test
    public void testSetFacingRight() {
        knight.setFacingRight(false);
        assertFalse(knight.isFacingRight());
        knight.setFacingRight(true);
        assertTrue(knight.isFacingRight());
    }

    @Test
    public void testGetJumpCounter() {
        assertEquals(0, knight.getJumpCounter());
    }

    @Test
    public void testSetJumpCounter() {
        knight.setJumpCounter(2);
        assertEquals(2, knight.getJumpCounter());
    }

    @Test
    public void testIsGotHitInitial() {
        assertFalse(knight.isGotHit());
    }

    @Test
    public void testSetGotHit() {
        knight.setGotHit(true);
        assertTrue(knight.isGotHit());
        knight.setGotHit(false);
        assertFalse(knight.isGotHit());
    }

    @Test
    public void testGetOrbsInitial() {
        assertEquals(0, knight.getOrbs());
    }

    @Test
    public void testSetOrbs() {
        knight.setOrbs(5);
        assertEquals(5, knight.getOrbs());
    }

    @Test
    public void testAddOrbs() {
        assertEquals(0, knight.getOrbs());
        knight.addOrbs();
        assertEquals(1, knight.getOrbs());
        knight.addOrbs();
        assertEquals(2, knight.getOrbs());
    }

    @Test
    public void testGetNumberOfDeathsInitial() {
        assertEquals(0, knight.getNumberOfDeaths());
    }

    @Test
    public void testIncreaseDeaths() {
        assertEquals(0, knight.getNumberOfDeaths());
        knight.increaseDeaths();
        assertEquals(1, knight.getNumberOfDeaths());
        knight.increaseDeaths();
        assertEquals(2, knight.getNumberOfDeaths());
    }

    @Test
    public void testGetBirthTime() {
        long before = System.currentTimeMillis();
        Knight k = new Knight(0, 0, 100, 1.0f, 50);
        long after = System.currentTimeMillis();
        assertTrue(k.getBirthTime() >= before && k.getBirthTime() <= after);
    }

    @Test
    public void testSetBirthTime() {
        knight.setBirthTime(12345L);
        assertEquals(12345L, knight.getBirthTime());
    }

    @Test
    public void testGetScene() {
        assertEquals(scene, knight.getScene());
    }

    @Test
    public void testSetScene() {
        Scene newScene = mock(Scene.class);
        knight.setScene(newScene);
        assertEquals(newScene, knight.getScene());
    }

    @Test
    public void testGetStateInitial() {
        assertNotNull(knight.getState());
        assertTrue(knight.getState() instanceof IdleState);
    }

    @Test
    public void testSetState() {
        KnightState newState = mock(KnightState.class);
        knight.setState(newState);
        assertEquals(newState, knight.getState());
    }

    @Test
    public void testSetMaxVelocity() {
        Vector newMaxVel = new Vector(5.0, 6.0);
        knight.setMaxVelocity(newMaxVel);
        assertEquals(5.0, knight.getMaxVelocity().x(), 0.001);
        assertEquals(6.0, knight.getMaxVelocity().y(), 0.001);
    }

    @Test
    public void testIsOverMaxXVelocityFalse() {
        knight.setVelocity(new Vector(1.0, 0.0));
        assertFalse(knight.isOverMaxXVelocity());
    }

    @Test
    public void testIsOverMaxXVelocityTrue() {
        knight.setVelocity(new Vector(3.0, 0.0));
        assertTrue(knight.isOverMaxXVelocity());
    }

    @Test
    public void testIsOverMaxXVelocityNegative() {
        knight.setVelocity(new Vector(-3.0, 0.0));
        assertTrue(knight.isOverMaxXVelocity()); // Math.abs(-3.0) > 2.0
    }

    @Test
    public void testIsOverMaxXVelocityAtBoundary() {
        knight.setVelocity(new Vector(2.0, 0.0));
        assertFalse(knight.isOverMaxXVelocity()); // 2.0 is NOT > 2.0
    }

    @Test
    public void testIsOnGround() {
        when(scene.collidesDown(any(Position.class), any(Position.class))).thenReturn(true);
        assertTrue(knight.isOnGround());
        verify(scene).collidesDown(any(Position.class), any(Position.class));
    }

    @Test
    public void testIsOnGroundFalse() {
        when(scene.collidesDown(any(Position.class), any(Position.class))).thenReturn(false);
        assertFalse(knight.isOnGround());
    }

    @Test
    public void testUpdateVelocity() {
        KnightState mockState = mock(KnightState.class);
        Vector expectedVel = new Vector(1.0, 2.0);
        when(mockState.updateVelocity(any(Vector.class))).thenReturn(expectedVel);
        knight.setState(mockState);
        
        Vector result = knight.updateVelocity();
        assertEquals(expectedVel, result);
        verify(mockState).updateVelocity(any(Vector.class));
    }

    @Test
    public void testUpdatePosition() {
        KnightState mockState = mock(KnightState.class);
        Vector resolvedVel = new Vector(2.0, 3.0);
        when(mockState.applyCollisions(any(Vector.class))).thenReturn(resolvedVel);
        knight.setState(mockState);
        
        Position oldPos = knight.getPosition();
        Position newPos = knight.updatePosition();
        
        assertEquals(oldPos.x() + 2.0, newPos.x(), 0.001);
        assertEquals(oldPos.y() + 3.0, newPos.y(), 0.001);
    }

    @Test
    public void testMoveLeft() {
        KnightState mockState = mock(KnightState.class);
        Vector expectedVel = new Vector(-1.0, 0.0);
        when(mockState.moveKnightLeft()).thenReturn(expectedVel);
        knight.setState(mockState);
        
        Vector result = knight.moveLeft();
        assertEquals(expectedVel, result);
        verify(mockState).moveKnightLeft();
    }

    @Test
    public void testMoveRight() {
        KnightState mockState = mock(KnightState.class);
        Vector expectedVel = new Vector(1.0, 0.0);
        when(mockState.moveKnightRight()).thenReturn(expectedVel);
        knight.setState(mockState);
        
        Vector result = knight.moveRight();
        assertEquals(expectedVel, result);
        verify(mockState).moveKnightRight();
    }

    @Test
    public void testJump() {
        KnightState mockState = mock(KnightState.class);
        Vector expectedVel = new Vector(0.0, -5.0);
        when(mockState.jump()).thenReturn(expectedVel);
        knight.setState(mockState);
        
        Vector result = knight.jump();
        assertEquals(expectedVel, result);
        verify(mockState).jump();
    }

    @Test
    public void testDash() {
        KnightState mockState = mock(KnightState.class);
        Vector expectedVel = new Vector(6.0, 0.0);
        when(mockState.dash()).thenReturn(expectedVel);
        knight.setState(mockState);
        
        Vector result = knight.dash();
        assertEquals(expectedVel, result);
        verify(mockState).dash();
    }

    @Test
    public void testGetNextState() throws IOException {
        KnightState mockState = mock(KnightState.class);
        KnightState nextState = mock(KnightState.class);
        when(mockState.getNextState()).thenReturn(nextState);
        knight.setState(mockState);
        
        KnightState result = knight.getNextState();
        assertEquals(nextState, result);
        verify(mockState).getNextState();
    }

    @Test
    public void testResetValues() {
        knight.setFacingRight(false);
        knight.resetValues();
        assertTrue(knight.isFacingRight());
        assertTrue(knight.getState() instanceof FallingState);
    }

    @Test
    public void testPlayerHitWhenAlreadyHit() {
        knight.setGotHit(true);
        int hpBefore = knight.getHP();
        knight.PlayerHit(10);
        assertEquals(hpBefore, knight.getHP());
    }

    @Test
    public void testPlayerHitReducesHP() {
        knight.setGotHit(false);
        int hpBefore = knight.getHP();
        knight.PlayerHit(10);
        assertEquals(hpBefore - 10, knight.getHP());
        assertTrue(knight.isGotHit());
    }

    @Test
    public void testPlayerHitWhenHPZero() {
        knight.setHP(0);
        knight.setGotHit(false);
        knight.PlayerHit(5);
        assertEquals(-4, knight.getHP());
    }

    @Test
    public void testPlayerHitSetsDamagedState() {
        knight.setGotHit(false);
        knight.PlayerHit(10);
        assertTrue(knight.getState() instanceof DamagedState);
    }

    @Test
    public void testCreateParticlesJump() {
        List<Particle> particles = knight.createParticlesJump(5);
        assertEquals(5, particles.size());
        for (Particle p : particles) {
            assertNotNull(p);
        }
    }

    @Test
    public void testCreateParticlesJumpPosition() {
        List<Particle> particles = knight.createParticlesJump(1);
        Particle p = particles.get(0);
        double expectedX = knight.getPosition().x() + knight.getWidth() / 2.0;
        double expectedY = knight.getPosition().y() + knight.getHeight();
        assertEquals((int) expectedX, (int) p.getPosition().x());
        assertEquals((int) expectedY, (int) p.getPosition().y());
    }

    @Test
    public void testCreateParticlesDoubleJump() {
        List<Particle> particles = knight.createParticlesDoubleJump(5, scene);
        assertEquals(5, particles.size());
    }

    @Test
    public void testCreateDashParticles() {
        List<Particle> particles = knight.createDashParticles(5);
        assertEquals(5, particles.size());
    }

    @Test
    public void testCreateRespawnParticles() {
        List<Particle> particles = knight.createRespawnParticles(5);
        assertEquals(5, particles.size());
    }

    @Test
    public void testCreateParticlesJumpVelocity() {
        knight.setVelocity(new Vector(2.0, 0.0));
        List<Particle> particles = knight.createParticlesJump(10);
        for (Particle p : particles) {
            assertNotNull(p.getVelocity());
        }
    }

    @Test
    public void testCreateDashParticlesVelocity() {
        knight.setVelocity(new Vector(3.0, 0.0));
        List<Particle> particles = knight.createDashParticles(10);
        for (Particle p : particles) {
            assertNotNull(p.getVelocity());
        }
    }

    @Test
    public void testPlayerHitBloodCalculation() {
        knight.setHP(25);
        knight.setGotHit(false);
        knight.PlayerHit(10);
        assertTrue(knight.getState() instanceof DamagedState);
    }

    @Test
    public void testPlayerHitDamageCalculation() {
        knight.setHP(50);
        knight.setGotHit(false);
        knight.PlayerHit(10);
        assertTrue(knight.getState() instanceof DamagedState);
    }
}