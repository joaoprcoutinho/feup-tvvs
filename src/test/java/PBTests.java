import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.rocks.Rock;
import pt.feup.tvvs.soulknight.view.elements.rocks.RockViewer;
import pt.feup.tvvs.soulknight.view.sprites.Sprite;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class PBTests {
    @Provide
    private Arbitrary<Character> validRockChars() {
        return Arbitraries.of('r', 'R');
    }

    @Provide
    Arbitrary<Character> invalidRockChars() {
        return Arbitraries.chars().ascii().filter(c -> c != 'r' && c != 'R');
    }

    @Provide
    Arbitrary<Position> positions() {
        return Arbitraries.integers().between(-1000, 1000)
                .tuple2()
                .map(t -> new Position(t.get1(), t.get2()));
    }

    @Property
    public void testValidCharactersDrawExactlyOneCorrectSprite(@ForAll("validRockChars") char rockChar, @ForAll("positions") Position pos) throws IOException {
        SpriteLoader loader = mock(SpriteLoader.class);
        Sprite small = mock(Sprite.class);
        Sprite big = mock(Sprite.class);

        when(loader.get("sprites/Ambient/Smallrock.png")).thenReturn(small);
        when(loader.get("sprites/Ambient/Bigrock.png")).thenReturn(big);

        RockViewer viewer = new RockViewer(loader);

        Rock rock = mock(Rock.class);
        GUI gui = mock(GUI.class);
        when(rock.getChar()).thenReturn(rockChar);
        when(rock.getPosition()).thenReturn(pos);

        viewer.draw(rock, gui, 0L, 0, 0);

        if (rockChar == 'r') {
            verify(small, times(1)).draw(gui, (int) pos.x(), (int) pos.y());
            verify(big, never()).draw(any(), anyInt(), anyInt());
        } else {
            verify(big, times(1)).draw(gui, (int) pos.x(), (int) pos.y());
            verify(small, never()).draw(any(), anyInt(), anyInt());
        }
    }

    @Property
    public void testInvalidCharactersAlwaysThrow(@ForAll("invalidRockChars") char rockChar, @ForAll("positions") Position pos) throws IOException {
        SpriteLoader loader = mock(SpriteLoader.class);
        when(loader.get(anyString())).thenReturn(mock(Sprite.class));

        RockViewer viewer = new RockViewer(loader);
        Rock rock = mock(Rock.class);
        GUI gui = mock(GUI.class);
        when(rock.getChar()).thenReturn(rockChar);
        when(rock.getPosition()).thenReturn(pos);

        assertThrows(IllegalArgumentException.class, () -> viewer.draw(rock, gui, 0L, 0, 0));
    }
}
