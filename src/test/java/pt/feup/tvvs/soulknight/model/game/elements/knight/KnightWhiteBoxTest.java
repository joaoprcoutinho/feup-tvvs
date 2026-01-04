package pt.feup.tvvs.soulknight.model.game.elements.knight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.elements.knight.FallingState;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.model.game.elements.knight.KnightState;
import pt.feup.tvvs.soulknight.model.game.elements.particle.*;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.List;

public class KnightWhiteBoxTest {

    private Knight knight;

    @BeforeEach
    void setup() {
        knight = new Knight(10, 20, 50, 2.0f, 100);
        Scene sceneMock = mock(Scene.class);
        knight.setScene(sceneMock);
        KnightState mockState = mock(KnightState.class);
        knight.setState(mockState);
    }

    @Test
    void testConstructorInitialValues() {
        assertEquals(50, knight.getHP());
        assertEquals(2.0f, knight.getDamage(), 0.0001);
        assertEquals(6, knight.getDashBoost(), 0.0001);
        assertEquals(100, knight.getEnergy());
        assertEquals(0.75, knight.getAcceleration(), 0.0001);
        assertEquals(new Vector(2.0,4.0), knight.getMaxVelocity());
        assertEquals(Math.PI, knight.getJumpBoost(), 0.0001);
        assertTrue(knight.isFacingRight());
        assertNotNull(knight.getVelocity());
        assertNotNull(knight.getMaxVelocity());
        assertNotNull(knight.getState());
    }

    @Test
    void testSetHP() {
        knight.setHP(12);
        assertEquals(12, knight.getHP());
    }

    @Test
    void testSetFacingRight() {
        knight.setFacingRight(false);
        assertFalse(knight.isFacingRight());
    }

    @Test
    void testSetDamage() {
        knight.setDamage(3.5f);
        assertEquals(3.5f, knight.getDamage(), 0.0001);
    }

    @Test
    void testSetEnergy() {
        knight.setEnergy(60);
        assertEquals(60, knight.getEnergy());
    }

    @Test
    void testSetState() {
        KnightState mockState = mock(KnightState.class);
        knight.setState(mockState);
        assertEquals(mockState, knight.getState());
    }

    @Test
    void testSetVelocity() {
        Vector v = new Vector(3, 4);
        knight.setVelocity(v);
        assertEquals(v, knight.getVelocity());
    }

    @Test
    void testSetBirthTime() {
        knight.setBirthTime(5);
        assertEquals(5, knight.getBirthTime());
    }

    @Test
    void testSetMaxVelocity() {
        knight.setMaxVelocity(new Vector(3.0,3.0));
        assertEquals(new Vector(3.0,3.0), knight.getMaxVelocity());
    }

    @Test
    void testSetJumpCounter() {
        knight.setJumpCounter(12);
        assertEquals(12, knight.getJumpCounter());
    }

    @Test
    void testUpdateVelocity() {
        Vector inputVel = knight.getVelocity();
        Vector expectedVel = new Vector(9, 9);
        KnightState state = knight.getState();

        when(state.updateVelocity(inputVel)).thenReturn(expectedVel);

        assertEquals(expectedVel, knight.updateVelocity());
        verify(state).updateVelocity(inputVel);
    }

    @Test
    void testUpdatePosition() {
        KnightState state = knight.getState();;

        knight.setVelocity(new Vector(5, -3));

        when(state.applyCollisions(knight.getVelocity())).thenReturn(new Vector(90, 180));

        Position result = knight.updatePosition();

        assertEquals(knight.getPosition().x() + 90, result.x(), 0.0001);
        assertEquals(knight.getPosition().y() + 180, result.y(), 0.0001);
    }

    @Test
    void testMoveLeft() {
        KnightState state = knight.getState();;

        Vector expected = new Vector(-1, 0);
        when(state.moveKnightLeft()).thenReturn(expected);

        assertEquals(expected, knight.moveLeft());
        verify(state).moveKnightLeft();
    }

    @Test
    void testMoveRight() {
        KnightState state = knight.getState();

        Vector expected = new Vector(-1, 0);
        when(state.moveKnightRight()).thenReturn(expected);

        assertEquals(expected, knight.moveRight());
        verify(state).moveKnightRight();
    }

    @Test
    void testJump() {
        KnightState state = knight.getState();

        Vector expected = new Vector(0, -10);
        when(state.jump()).thenReturn(expected);

        assertEquals(expected, knight.jump());
        verify(state).jump();
    }

    @Test
    void testDash() {
        KnightState state = knight.getState();

        Vector expected = new Vector(0, -10);
        when(state.dash()).thenReturn(expected);

        assertEquals(expected, knight.dash());
        verify(state).dash();
    }

    @Test
    void testDeaths() {
        assertEquals(0, knight.getNumberOfDeaths());
        knight.increaseDeaths();
        knight.increaseDeaths();
        assertEquals(2, knight.getNumberOfDeaths());
    }

    @Test
    void testOrbs() {
        assertEquals(0, knight.getOrbs());
        knight.addOrbs();
        knight.addOrbs();
        assertEquals(2, knight.getOrbs());
        knight.setOrbs(10);
        assertEquals(10, knight.getOrbs());
    }

    @Test
    void testIsOverMaxXVelocity() {
        knight.setVelocity(new Vector(5, 0));
        assertTrue(knight.isOverMaxXVelocity());

        knight.setVelocity(new Vector(1, 0));
        assertFalse(knight.isOverMaxXVelocity());
    }

    @Test
    void testIsOnGround() {
        Scene scene = knight.getScene();

        when(scene.collidesDown(any(Position.class), any(Position.class))).thenReturn(true);
        assertTrue(knight.isOnGround());

        when(scene.collidesDown(any(Position.class), any(Position.class))).thenReturn(false);
        assertFalse(knight.isOnGround());
    }

    @Test
    void testPlayerHit() {
        knight.setHP(30);
        knight.PlayerHit(10);
        assertEquals(20, knight.getHP());
        assertTrue(knight.isGotHit());
        assertTrue(knight.getState() instanceof DamagedState);
    }

    @Test
    void testPlayerHitWhenAlreadyHit() {
        knight.setHP(30);
        knight.setGotHit(true);
        knight.PlayerHit(10);
        assertEquals(30, knight.getHP());
    }

    @Test
    void testPlayerHitWhenNoHP() {
        knight.setHP(0);
        knight.PlayerHit(10);
        assertEquals(-9, knight.getHP());
    }

    @Test
    void testResetValues() {
        knight.setFacingRight(false);
        knight.setState(mock(KnightState.class));

        knight.resetValues();

        assertTrue(knight.isFacingRight());
        assertTrue(knight.getState() instanceof FallingState);
    }

    @Test
    void testCreateParticlesDoubleJump() {
        Scene scene = knight.getScene();
        List<Particle> particles = knight.createParticlesDoubleJump(10, scene);
        assertEquals(10, particles.size());
        assertTrue(particles.get(0) instanceof DoubleJumpParticle);
    }

    @Test
    void testCreateParticlesJump() {
        List<Particle> particles = knight.createParticlesJump(8);
        assertEquals(8, particles.size());
        assertTrue(particles.get(0) instanceof JumpParticle);
    }

    @Test
    void testCreateRespawnParticles() {
        List<Particle> particles = knight.createRespawnParticles(6);
        assertEquals(6, particles.size());
        assertTrue(particles.get(0) instanceof RespawnParticle);
    }

    @Test
    void testCreateDashParticles() {
        List<Particle> particles = knight.createDashParticles(5);
        assertEquals(5, particles.size());
        assertTrue(particles.get(0) instanceof DashParticle);
    }
}
