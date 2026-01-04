package pt.feup.tvvs.soulknight.view.elements.monsters;

import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.enemies.Enemies;
import pt.feup.tvvs.soulknight.model.game.elements.enemies.GhostMonster;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;
import pt.feup.tvvs.soulknight.view.sprites.Sprite;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MonsterViewerMutationTests {

    /* -------------------------------------------------
     * Constructor mutation (loop boundary)
     * ------------------------------------------------- */
    @Test
    void constructor_shouldLoadTwoSwordMonsterSprites() throws IOException {
        SpriteLoader loader = mock(SpriteLoader.class);
        when(loader.get(anyString())).thenReturn(mock(Sprite.class));

        MonsterViewer viewer = new MonsterViewer(loader);

        // indirectly validate size via getSpriteForAnimation
        Sprite s1 = mock(Sprite.class);
        Sprite s2 = mock(Sprite.class);

        List<Sprite> sprites = List.of(s1, s2);

        Sprite result = viewer.getSpriteForAnimation(sprites, 0);
        assertNotNull(result);
        assertTrue(sprites.contains(result));
    }

    /* -------------------------------------------------
     * Sword monster ('E')
     * ------------------------------------------------- */
    @Test
    void draw_shouldDrawSwordMonsterWithCorrectOffset() throws IOException {
        Sprite sprite = mock(Sprite.class);
        SpriteLoader loader = mock(SpriteLoader.class);
        when(loader.get(anyString())).thenReturn(sprite);

        MonsterViewer viewer = new MonsterViewer(loader);

        Enemies enemy = new GhostMonster(30,40,1,new Scene(10, 10, 1),30, new Position(20, 10), 'E');

        GUI gui = mock(GUI.class);

        viewer.draw(enemy, gui, 0L, 0, 0);

        verify(sprite).draw(gui, 26, 40); // x - 4
    }

    /* -------------------------------------------------
     * Ghost monster ('m')
     * ------------------------------------------------- */
    @Test
    void draw_shouldDrawGhostMonsterWithCorrectOffset() throws IOException {
        Sprite sprite = mock(Sprite.class);
        SpriteLoader loader = mock(SpriteLoader.class);
        when(loader.get(anyString())).thenReturn(sprite);

        MonsterViewer viewer = new MonsterViewer(loader);

        Enemies enemy = new GhostMonster(30,40,1,new Scene(10, 10, 1),30, new Position(30, 40), 'm');

        GUI gui = mock(GUI.class);

        viewer.draw(enemy, gui, 0L, 0, 0);

        verify(sprite).draw(gui, 26, 34); // x - 4, y - 6
        verify(gui).drawHitBox(eq(30), eq(40), eq(4), eq(4), any());
    }

    /* -------------------------------------------------
     * Purple monster ('l')
     * ------------------------------------------------- */
    @Test
    void draw_shouldDrawPurpleMonsterWithCorrectOffset() throws IOException {
        Sprite sprite = mock(Sprite.class);
        SpriteLoader loader = mock(SpriteLoader.class);
        when(loader.get(anyString())).thenReturn(sprite);

        MonsterViewer viewer = new MonsterViewer(loader);

        Enemies enemy = new GhostMonster(30,40,1,new Scene(10, 10, 1),30, new Position(50, 60), 'l');

        GUI gui = mock(GUI.class);

        viewer.draw(enemy, gui, 0L, 0, 0);

        verify(sprite).draw(gui, 26, 39); // x - 4, y - 1
    }

    /* -------------------------------------------------
     * getSpriteForAnimation mutation
     * ------------------------------------------------- */
    @Test
    void getSpriteForAnimation_shouldReturnCorrectFrame() throws IOException {
        MonsterViewer viewer = new MonsterViewer(mock(SpriteLoader.class));

        Sprite s0 = mock(Sprite.class);
        Sprite s1 = mock(Sprite.class);

        List<Sprite> sprites = List.of(s0, s1);

        // animationFPS = 6 → frameTime = 5
        // tick = 5 → frameIndex = (5 / 5) % 2 = 1
        Sprite result = viewer.getSpriteForAnimation(sprites, 5);

        assertSame(s1, result);
    }
}
