package pt.feup.tvvs.soulknight.view.elements.rocks;
            
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.game.elements.rocks.Rock;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.view.sprites.Sprite;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;
import java.io.IOException;

public class RockViewerMutationTests {
    private SpriteLoader spriteLoader;
    private Sprite smallRockSprite;
    private Sprite bigRockSprite;
    private GUI gui;

    @BeforeEach
    void setUp() throws IOException {
        spriteLoader = mock(SpriteLoader.class);
        smallRockSprite = mock(Sprite.class);
        bigRockSprite = mock(Sprite.class);
        gui = mock(GUI.class);
        
        when(spriteLoader.get("sprites/Ambient/Smallrock.png")).thenReturn(smallRockSprite);
        when(spriteLoader.get("sprites/Ambient/Bigrock.png")).thenReturn(bigRockSprite);
    }

    @Test
    void draw_shouldUseSmallRockSprite() throws IOException {
        RockViewer viewer = new RockViewer(spriteLoader);
        Rock rock = mock(Rock.class);
        when(rock.getChar()).thenReturn('r');
        when(rock.getPosition()).thenReturn(new Position(10, 20));
        
        viewer.draw(rock, gui, 0, 0, 0);
        
        verify(smallRockSprite).draw(gui, 10, 20);
    }

    @Test
    void draw_shouldUseBigRockSprite() throws IOException {
        RockViewer viewer = new RockViewer(spriteLoader);
        Rock rock = mock(Rock.class);
        when(rock.getChar()).thenReturn('R');
        when(rock.getPosition()).thenReturn(new Position(30, 40));
        
        viewer.draw(rock, gui, 0, 0, 0);
        
        verify(bigRockSprite).draw(gui, 30, 40);
    }

    @Test
    void draw_shouldThrowForInvalidChar() throws IOException {
        RockViewer viewer = new RockViewer(spriteLoader);
        Rock rock = mock(Rock.class);
        when(rock.getChar()).thenReturn('x');
        when(rock.getPosition()).thenReturn(new Position(10, 20));
        
        assertThrows(IllegalArgumentException.class, () -> viewer.draw(rock, gui, 0, 0, 0));
    }

    @Test
    void draw_shouldUseCorrectXPosition() throws IOException {
        RockViewer viewer = new RockViewer(spriteLoader);
        Rock rock = mock(Rock.class);
        when(rock.getChar()).thenReturn('r');
        when(rock.getPosition()).thenReturn(new Position(50, 60));
        
        viewer.draw(rock, gui, 0, 0, 0);
        
        verify(smallRockSprite).draw(eq(gui), eq(50), anyInt());
    }

    @Test
    void draw_shouldUseCorrectYPosition() throws IOException {
        RockViewer viewer = new RockViewer(spriteLoader);
        Rock rock = mock(Rock.class);
        when(rock.getChar()).thenReturn('r');
        when(rock.getPosition()).thenReturn(new Position(50, 60));
        
        viewer.draw(rock, gui, 0, 0, 0);
        
        verify(smallRockSprite).draw(eq(gui), anyInt(), eq(60));
    }
}