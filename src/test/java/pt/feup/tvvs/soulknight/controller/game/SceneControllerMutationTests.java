package pt.feup.tvvs.soulknight.controller.game;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.Game;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.collectables.Collectables;
import pt.feup.tvvs.soulknight.model.game.elements.enemies.Enemies;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SceneControllerMutationTests {

    private Scene scene;
    private Game game;
    private Knight knight;
    private PlayerController playerController;
    private ParticleController particleController;
    private EnemieController enemieController;
    private SceneController controller;
    private SpriteLoader spriteLoader;

    @BeforeEach
    void setUp() throws IOException {
        scene = mock(Scene.class);
        game = mock(Game.class);
        knight = mock(Knight.class);
        playerController = mock(PlayerController.class);
        particleController = mock(ParticleController.class);
        enemieController = mock(EnemieController.class);
        spriteLoader = mock(SpriteLoader.class);

        when(scene.getPlayer()).thenReturn(knight);
        when(scene.getSceneID()).thenReturn(0);
        when(scene.isAtEndPosition()).thenReturn(false);
        when(scene.getOrbs()).thenReturn(new Collectables[0][0]);
        when(scene.getMonsters()).thenReturn(List.of());
        when(knight.getOrbs()).thenReturn(0);
        when(game.getSpriteLoader()).thenReturn(spriteLoader);
        when(game.getNumberOfLevels()).thenReturn(3);

        controller = new SceneController(scene, playerController, particleController, enemieController);
    }

    @Test
    public void testOrbRequirementMultiplication() throws IOException {
        when(scene.isAtEndPosition()).thenReturn(true);
        when(scene.getSceneID()).thenReturn(0);
        when(knight.getOrbs()).thenReturn(3);

        controller.move(game, GUI.ACTION.UP, 0);

        verify(game).setState(any());
    }

    @Test
    public void testOrbRequirementLevel1() throws IOException {
        when(scene.isAtEndPosition()).thenReturn(true);
        when(scene.getSceneID()).thenReturn(1);
        when(knight.getOrbs()).thenReturn(6);

        controller.move(game, GUI.ACTION.UP, 0);

        verify(game).setState(any());
    }

    @Test
    public void testCollectOrbsCalled() throws IOException {
        Collectables[][] orbs = new Collectables[1][1];
        when(scene.getOrbs()).thenReturn(orbs);

        controller.move(game, GUI.ACTION.UP, 0);

        verify(scene).collectOrbs(orbs);
    }

    @Test
    public void testCollideMonstersCalled() throws IOException {
        Enemies enemy = mock(Enemies.class);
        List<Enemies> monsters = List.of(enemy);
        when(scene.getMonsters()).thenReturn(monsters);

        controller.move(game, GUI.ACTION.UP, 0);
        verify(scene).collideMonsters(monsters);
    }

    @Test
    public void testPlayerControllerMoveCalled() throws IOException {
        controller.move(game, GUI.ACTION.UP, 100);
        verify(playerController).move(game, GUI.ACTION.UP, 100);
    }

    @Test
    public void testParticleControllerMoveCalled() throws IOException {
        controller.move(game, GUI.ACTION.UP, 100);
        verify(particleController).move(game, GUI.ACTION.UP, 100);
    }

    @Test
    public void testEnemieControllerMoveCalled() throws IOException {
        controller.move(game, GUI.ACTION.UP, 100);
        verify(enemieController).move(game, GUI.ACTION.UP, 100);
    }

    @Test
    public void testQuitActionChangesState() throws IOException {
        controller.move(game, GUI.ACTION.QUIT, 0);
        verify(game).setState(any());
        verify(playerController, never()).move(any(), any(), anyLong());
    }

    @Test
    public void testTransitionToCreditsOnLastLevel() throws IOException {
        when(scene.isAtEndPosition()).thenReturn(true);
        when(scene.getSceneID()).thenReturn(2);
        when(game.getNumberOfLevels()).thenReturn(3);
        when(knight.getOrbs()).thenReturn(9);
        controller.move(game, GUI.ACTION.UP, 0);
        verify(game).setState(any());
    }

    @Test
    public void testNotAtEndPosition() throws IOException {
        when(scene.isAtEndPosition()).thenReturn(false);
        when(knight.getOrbs()).thenReturn(3);
        controller.move(game, GUI.ACTION.UP, 0);
        verify(scene).collectOrbs(any());
        verify(scene).collideMonsters(any());
    }

    @Test
    public void testInsufficientOrbs() throws IOException {
        when(scene.isAtEndPosition()).thenReturn(true);
        when(scene.getSceneID()).thenReturn(0);
        when(knight.getOrbs()).thenReturn(2); // Need 3
        controller.move(game, GUI.ACTION.UP, 0);
        verify(scene).collectOrbs(any());
        verify(scene).collideMonsters(any());
    }

    @Test
    public void testGetModelReturnsScene() {
        assertEquals(scene, controller.getModel());
    }
}