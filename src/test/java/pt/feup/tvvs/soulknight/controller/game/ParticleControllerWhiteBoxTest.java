package pt.feup.tvvs.soulknight.controller.game;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.Game;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.enemies.Enemies;
import pt.feup.tvvs.soulknight.model.game.elements.particle.Particle;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.List;

public class ParticleControllerWhiteBoxTest {
    private Scene scene;
    private ParticleController controller;
    private Game game;
    private Particle particle;

    @BeforeEach
    void setup() {
        scene = mock(Scene.class);
        controller = new ParticleController(scene);
        game = mock(Game.class);
        particle = mock(Particle.class);
        when(scene.getParticles()).thenReturn(List.of(particle));
        when(scene.getDoubleJumpParticles()).thenReturn(List.of(particle));
        when(scene.getJumpParticles()).thenReturn(List.of(particle));
        when(scene.getRespawnParticles()).thenReturn(List.of(particle));
        when(scene.getDashParticles()).thenReturn(List.of(particle));
        when(particle.moveParticle(any(), anyLong())).thenReturn(new Position(1, 1));
    }

    @Test
    void TestMove() throws IOException {
        controller.move(game, GUI.ACTION.DOWN, 5);
        verify(particle, times(5)).setScene(scene);
        verify(particle, times(5)).setPosition(new Position(1,1));
    }
}