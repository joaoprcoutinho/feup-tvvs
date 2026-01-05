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

    // Test 3 * (getModel().getSceneID() + 1) multiplication
    // Kills mutation: "Replaced integer multiplication with division"
    @Test
    void testOrbRequirementMultiplication() throws IOException {
        when(scene.isAtEndPosition()).thenReturn(true);
        when(scene.getSceneID()).thenReturn(0);
        // 3 * (0 + 1) = 3 orbs needed for level 0
        when(knight.getOrbs()).thenReturn(3);

        controller.move(game, GUI.ACTION.UP, 0);

        // Should transition to next level
        verify(game).setState(any());
    }

    @Test
    void testOrbRequirementLevel1() throws IOException {
        when(scene.isAtEndPosition()).thenReturn(true);
        when(scene.getSceneID()).thenReturn(1);
        // 3 * (1 + 1) = 6 orbs needed for level 1
        when(knight.getOrbs()).thenReturn(6);

        controller.move(game, GUI.ACTION.UP, 0);

        verify(game).setState(any());
    }

    // Test collectOrbs is called
    // Kills mutation: "removed call to Scene::collectOrbs"
    @Test
    void testCollectOrbsCalled() throws IOException {
        Collectables[][] orbs = new Collectables[1][1];
        when(scene.getOrbs()).thenReturn(orbs);

        controller.move(game, GUI.ACTION.UP, 0);

        verify(scene).collectOrbs(orbs);
    }

    // Test collideMonsters is called
    // Kills mutation: "removed call to Scene::collideMonsters"
    @Test
    void testCollideMonstersCalled() throws IOException {
        Enemies enemy = mock(Enemies.class);
        List<Enemies> monsters = List.of(enemy);
        when(scene.getMonsters()).thenReturn(monsters);

        controller.move(game, GUI.ACTION.UP, 0);

        verify(scene).collideMonsters(monsters);
    }

    // Test playerController.move is called
    @Test
    void testPlayerControllerMoveCalled() throws IOException {
        controller.move(game, GUI.ACTION.UP, 100);

        verify(playerController).move(game, GUI.ACTION.UP, 100);
    }

    // Test particleController.move is called
    @Test
    void testParticleControllerMoveCalled() throws IOException {
        controller.move(game, GUI.ACTION.UP, 100);

        verify(particleController).move(game, GUI.ACTION.UP, 100);
    }

    // Test enemieController.move is called
    @Test
    void testEnemieControllerMoveCalled() throws IOException {
        controller.move(game, GUI.ACTION.UP, 100);

        verify(enemieController).move(game, GUI.ACTION.UP, 100);
    }

    // Test QUIT action changes state to MainMenuState
    @Test
    void testQuitActionChangesState() throws IOException {
        controller.move(game, GUI.ACTION.QUIT, 0);

        verify(game).setState(any());
        verify(playerController, never()).move(any(), any(), anyLong());
    }

    // Test transition to credits when last level completed
    @Test
    void testTransitionToCreditsOnLastLevel() throws IOException {
        when(scene.isAtEndPosition()).thenReturn(true);
        when(scene.getSceneID()).thenReturn(2); // Last level (0-indexed)
        when(game.getNumberOfLevels()).thenReturn(3);
        // 3 * (2 + 1) = 9 orbs needed
        when(knight.getOrbs()).thenReturn(9);

        controller.move(game, GUI.ACTION.UP, 0);

        verify(game).setState(any());
    }

    // Test not at end position - no level transition
    @Test
    void testNotAtEndPosition() throws IOException {
        when(scene.isAtEndPosition()).thenReturn(false);
        when(knight.getOrbs()).thenReturn(3);

        controller.move(game, GUI.ACTION.UP, 0);

        // Should process normal game logic
        verify(scene).collectOrbs(any());
        verify(scene).collideMonsters(any());
    }

    // Test insufficient orbs - no level transition
    @Test
    void testInsufficientOrbs() throws IOException {
        when(scene.isAtEndPosition()).thenReturn(true);
        when(scene.getSceneID()).thenReturn(0);
        when(knight.getOrbs()).thenReturn(2); // Need 3

        controller.move(game, GUI.ACTION.UP, 0);

        // Should process normal game logic
        verify(scene).collectOrbs(any());
        verify(scene).collideMonsters(any());
    }

    // Test getModel returns scene
    @Test
    void testGetModelReturnsScene() {
        assertEquals(scene, controller.getModel());
    }
}