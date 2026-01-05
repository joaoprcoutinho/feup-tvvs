package pt.feup.tvvs.soulknight.controller.menu;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.Game;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.menu.Particle;
import pt.feup.tvvs.soulknight.model.menu.Menu;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.state.particle.ParticleState;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import java.io.IOException;

public class ParticleMenuControllerWhiteBoxTests {
    private ParticleMenuController controller;
    private Menu menu;
    private Game game;
    private Particle particle;

    @BeforeEach
    void setup() {
        menu = mock(Menu.class);
        controller = new ParticleMenuController(menu);
        game = mock(Game.class);
        particle = mock(Particle.class);
        ParticleState state = mock(ParticleState.class);
        when(menu.getParticles()).thenReturn(new ArrayList<>(List.of(particle)));
        when(particle.getState()).thenReturn(state);
        when(state.move(any(), anyLong(), any())).thenReturn(new Position(10, 10));
    }

    @Test
    public void testMove() throws IOException {
        for (int time = 0; time < 250; time += 50) {
            controller.move(game, GUI.ACTION.SELECT, time);
            verify(particle, atLeastOnce()).setState(any(ParticleState.class));
            verify(particle, atLeastOnce()).setPosition(any(Position.class));
        }
    }
}