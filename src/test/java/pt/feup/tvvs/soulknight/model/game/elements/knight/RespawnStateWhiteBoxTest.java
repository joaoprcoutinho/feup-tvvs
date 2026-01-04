package pt.feup.tvvs.soulknight.model.game.elements.knight;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.elements.collectables.Collectables;
import pt.feup.tvvs.soulknight.model.game.elements.particle.Particle;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;
import pt.feup.tvvs.soulknight.model.game.scene.SceneLoader;

import static org.junit.Assert.*;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.List;

public class RespawnStateWhiteBoxTest {
    Knight knight;
    Scene scene;
    RespawnState state;

    @BeforeEach
    void setup() {
        knight = mock(Knight.class);
        scene = mock(Scene.class);

        state = new RespawnState(knight, 0);

        when(knight.getScene()).thenReturn(scene);
        when(scene.getSceneID()).thenReturn(1);
        when(scene.getStartPosition()).thenReturn(mock(Position.class));
    }

    @Test
    void testJump() {
        Vector result = state.jump();

        assertEquals(0, result.x(), 0.001);
        assertEquals(0, result.y(), 0.001);
    }

    @Test
    void testDash() {
        Vector result = state.jump();

        assertEquals(0, result.x(), 0.001);
        assertEquals(0, result.y(), 0.001);
    }

    @Test
    void testUpdateVelocity() {
        Vector v = new Vector(2, 2);

        Vector result = state.updateVelocity(v);

        assertEquals(0, result.x(), 0.001);
        assertEquals(0, result.y(), 0.001);
    }

    @Test
    void testNextStateDeathTimerZero() throws IOException {
        when(scene.getHeight()).thenReturn(100);
        when(scene.getWidth()).thenReturn(100);

        assertTrue(state.getNextState() instanceof FallingState);
        verify(knight).increaseDeaths();
        verify(knight).setOrbs(3);
        verify(knight).setHP(50);
        verify(knight).setPosition(any());
        verify(knight).setGotHit(false);
    }

    @Test
    void testNextStateRespawnParticlesTriggered() throws IOException {
        state = new RespawnState(knight, 5);

        assertSame(state, state.getNextState());

        verify(scene).setRespawnParticles(any());
    }
}