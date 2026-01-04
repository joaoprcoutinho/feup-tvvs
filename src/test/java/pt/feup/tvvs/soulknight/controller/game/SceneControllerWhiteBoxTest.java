package pt.feup.tvvs.soulknight.controller.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.Game;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.elements.knight.IdleState;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.model.game.elements.knight.KnightState;
import pt.feup.tvvs.soulknight.model.game.elements.knight.RespawnState;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;
import pt.feup.tvvs.soulknight.model.game.scene.SceneLoader;
import pt.feup.tvvs.soulknight.state.CreditsState;
import pt.feup.tvvs.soulknight.state.GameState;
import pt.feup.tvvs.soulknight.state.MainMenuState;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import static org.mockito.Mockito.*;
import java.io.IOException;

public class SceneControllerWhiteBoxTest {
    private Scene scene;
    private SceneController controller;
    private PlayerController playerController;
    private EnemieController enemieController;
    private ParticleController particleController;
    private Game game;
    private Knight knight;

    @BeforeEach
    void setup() throws IOException {
        scene = mock(Scene.class);
        playerController = mock(PlayerController.class);
        enemieController = mock(EnemieController.class);
        particleController = mock(ParticleController.class);
        controller = new SceneController(scene, playerController, particleController, enemieController);
        game = mock(Game.class);
        SpriteLoader spriteLoader = mock(SpriteLoader.class);
        when(game.getSpriteLoader()).thenReturn(spriteLoader);
        knight = mock(Knight.class);
        when(scene.getPlayer()).thenReturn(knight);
    }

    @Test
    void testMove() throws IOException{
        when(scene.isAtEndPosition()).thenReturn(false);

        controller.move(game, GUI.ACTION.RIGHT, 0);

        verify(playerController).move(game, GUI.ACTION.RIGHT, 0);
        verify(particleController).move(game, GUI.ACTION.RIGHT, 0);
        verify(enemieController).move(game, GUI.ACTION.RIGHT, 0);
    }


    @Test
    void testMoveEnd() throws IOException{
        when(scene.isAtEndPosition()).thenReturn(true);
        when(knight.getOrbs()).thenReturn(3);
        when(scene.getSceneID()).thenReturn(1);
        when(game.getNumberOfLevels()).thenReturn(2);

        controller.move(game, GUI.ACTION.RIGHT, 0);

        verify(playerController).move(game, GUI.ACTION.RIGHT, 0);
        verify(particleController).move(game, GUI.ACTION.RIGHT, 0);
        verify(enemieController).move(game, GUI.ACTION.RIGHT, 0);
    }

    @Test
    void testMoveNext() throws IOException{
        when(scene.isAtEndPosition()).thenReturn(true);
        when(knight.getOrbs()).thenReturn(3);
        when(scene.getSceneID()).thenReturn(0);
        when(game.getNumberOfLevels()).thenReturn(2);

        SceneLoader loader = mock(SceneLoader.class);
        Scene newScene = mock(Scene.class);
        when(loader.createScene(knight)).thenReturn(newScene);

        controller.move(game, GUI.ACTION.RIGHT, 0);

        verify(game).setState(any(GameState.class));
    }

    @Test
    void testMoveFinal() throws IOException {
        when(scene.isAtEndPosition()).thenReturn(true);
        when(knight.getOrbs()).thenReturn(3);
        when(scene.getSceneID()).thenReturn(0);
        when(game.getNumberOfLevels()).thenReturn(1);

        controller.move(game, GUI.ACTION.RIGHT, 0);

        verify(game).setState(any(CreditsState.class));
    }

    @Test
    void testMoveQuit() throws IOException {
        controller.move(game, GUI.ACTION.QUIT, 0);
        verify(game).setState(any(MainMenuState.class));
    }
}