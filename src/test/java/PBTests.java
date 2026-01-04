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
        return Arbitraries.chars()
                .filter(c -> c != 'r' && c != 'R');
    }

    @Property
    void drawsCorrectSpriteForValidCharacters(@ForAll("validRockChars") char rockChar) throws IOException {
        SpriteLoader spriteLoader = mock(SpriteLoader.class);
        Sprite smallRock = mock(Sprite.class);
        Sprite bigRock = mock(Sprite.class);

        when(spriteLoader.get("sprites/Ambient/Smallrock.png")).thenReturn(smallRock);
        when(spriteLoader.get("sprites/Ambient/Bigrock.png")).thenReturn(bigRock);

        RockViewer viewer = new RockViewer(spriteLoader);

        Rock rock = mock(Rock.class);
        GUI gui = mock(GUI.class);

        when(rock.getChar()).thenReturn(rockChar);
        when(rock.getPosition()).thenReturn(new Position(10, 20));

        // Act
        viewer.draw(rock, gui, 0L, 0, 0);

        // Assert
        if (rockChar == 'r') {
            verify(smallRock, times(1)).draw(gui, 10, 20);
            verify(bigRock, never()).draw(any(), anyInt(), anyInt());
        } else {
            verify(bigRock, times(1)).draw(gui, 10, 20);
            verify(smallRock, never()).draw(any(), anyInt(), anyInt());
        }
    }

    @Property
    void throwsExceptionForInvalidCharacters(@ForAll("invalidRockChars") char rockChar)
            throws IOException {

        // Arrange
        SpriteLoader spriteLoader = mock(SpriteLoader.class);
        when(spriteLoader.get(anyString())).thenReturn(mock(Sprite.class));

        RockViewer viewer = new RockViewer(spriteLoader);

        Rock rock = mock(Rock.class);
        GUI gui = mock(GUI.class);

        when(rock.getChar()).thenReturn(rockChar);
        when(rock.getPosition()).thenReturn(new Position(0, 0));

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> viewer.draw(rock, gui, 0L, 0, 0)
        );

        assertTrue(exception.getMessage().contains("No sprite for character"));
    }

    @Property
    void spriteLoaderIsInitializedWithCorrectPaths(@ForAll("validRockChars") char rockChar)
            throws IOException {

        // Arrange
        SpriteLoader spriteLoader = mock(SpriteLoader.class);
        when(spriteLoader.get(anyString())).thenReturn(mock(Sprite.class));

        // Act
        new RockViewer(spriteLoader);

        // Assert
        verify(spriteLoader).get("sprites/Ambient/Smallrock.png");
        verify(spriteLoader).get("sprites/Ambient/Bigrock.png");
    }
}
