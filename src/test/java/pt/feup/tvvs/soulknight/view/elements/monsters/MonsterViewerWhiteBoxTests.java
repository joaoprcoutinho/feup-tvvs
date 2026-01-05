package pt.feup.tvvs.soulknight.view.elements.monsters;
            
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.enemies.Enemies;
import pt.feup.tvvs.soulknight.view.sprites.Sprite;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.List;

public class MonsterViewerWhiteBoxTests {
    private SpriteLoader spriteLoader;
    private MonsterViewer monsterViewer;
    private GUI gui;
    private Enemies enemy;

    @BeforeEach
    void setUp() throws IOException {
        spriteLoader = mock(SpriteLoader.class);
        gui = mock(GUI.class);

        Sprite minhoteSprite = mock(Sprite.class);
        Sprite purpleSprite = mock(Sprite.class);
        Sprite swordSprite0 = mock(Sprite.class);
        Sprite swordSprite1 = mock(Sprite.class);

        when(spriteLoader.get("sprites/Enemies/MinhoteMonster.png")).thenReturn(minhoteSprite);
        when(spriteLoader.get("sprites/Enemies/PurpleMonster.png")).thenReturn(purpleSprite);
        when(spriteLoader.get("sprites/Enemies/swordMonster-Attack0.png")).thenReturn(swordSprite0);
        when(spriteLoader.get("sprites/Enemies/swordMonster-Attack1.png")).thenReturn(swordSprite1);

        monsterViewer = new MonsterViewer(spriteLoader);
    }

    @Test
    public void testDrawSwordMonsterAnimation() throws IOException {
        enemy = mock(Enemies.class);
        when(enemy.getChar()).thenReturn('E');
        when(enemy.getPosition()).thenReturn(new Position(10, 20));

        for (long tick = 0; tick < 10; tick++) {
            monsterViewer.draw(enemy, gui, tick, 0, 0);
        }

        verify(spriteLoader, times(1)).get("sprites/Enemies/swordMonster-Attack0.png");
        verify(spriteLoader, times(1)).get("sprites/Enemies/swordMonster-Attack1.png");
    }

    @Test
    public void testDrawMinhoteMonster() throws IOException {
        enemy = mock(Enemies.class);
        when(enemy.getChar()).thenReturn('m');
        when(enemy.getPosition()).thenReturn(new Position(15, 25));

        monsterViewer.draw(enemy, gui, 0, 0, 0);

        verify(spriteLoader).get("sprites/Enemies/MinhoteMonster.png");
        verify(gui).drawHitBox(anyInt(), anyInt(), anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testDrawPurpleMonster() throws IOException {
        enemy = mock(Enemies.class);
        when(enemy.getChar()).thenReturn('l');
        when(enemy.getPosition()).thenReturn(new Position(5, 10));

        monsterViewer.draw(enemy, gui, 0, 0, 0);

        verify(spriteLoader).get("sprites/Enemies/PurpleMonster.png");
    }

    @Test
    public void testDrawUnknownMonsterThrowsException() {
        enemy = mock(Enemies.class);
        when(enemy.getChar()).thenReturn('X');

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            monsterViewer.draw(enemy, gui, 0, 0, 0);
        });

        assertTrue(exception.getMessage().contains("No sprite for character"));
    }

    @Test
    public void testGetSpriteForAnimationReturnsCorrectFrame() {
        Sprite s1 = mock(Sprite.class);
        Sprite s2 = mock(Sprite.class);
        Sprite s3 = mock(Sprite.class);

        List<Sprite> sprites = List.of(s1, s2, s3);

        assertEquals(s1, monsterViewer.getSpriteForAnimation(sprites, 0));
        assertEquals(s2, monsterViewer.getSpriteForAnimation(sprites, 5));
        assertEquals(s3, monsterViewer.getSpriteForAnimation(sprites, 10));
        assertEquals(s1, monsterViewer.getSpriteForAnimation(sprites, 15));
    }
}