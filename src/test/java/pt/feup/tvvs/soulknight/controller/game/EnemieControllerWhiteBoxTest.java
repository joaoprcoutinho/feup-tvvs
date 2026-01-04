package pt.feup.tvvs.soulknight.controller.game;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.Game;
import pt.feup.tvvs.soulknight.controller.menu.OptionController;
import pt.feup.tvvs.soulknight.controller.menu.ParticleMenuController;
import pt.feup.tvvs.soulknight.controller.menu.SettingsMenuController;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.enemies.Enemies;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;
import pt.feup.tvvs.soulknight.model.menu.Particle;
import pt.feup.tvvs.soulknight.model.menu.SettingsMenu;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnemieControllerWhiteBoxTest {
    private Scene scene;
    private EnemieController controller;
    private Game game;
    private Enemies enemies;

    @BeforeEach
    void setup() {
        scene = mock(Scene.class);
        controller = new EnemieController(scene);
        game = mock(Game.class);
        enemies = mock(Enemies.class);
        when(enemies.moveMonster()).thenReturn(new Position(10, 20));
        when(scene.getMonsters()).thenReturn(List.of(enemies));
    }

    @Test
    void TestMove() throws IOException {
        controller.move(game, GUI.ACTION.DOWN, 5);
        verify(enemies, atLeastOnce()).setPosition(new Position(10, 20));
    }

    @Test
    void TestMoveDoNothing() throws IOException {
        controller.move(game, GUI.ACTION.DOWN, 0);
        verify(enemies, never()).setPosition(any());
    }
}