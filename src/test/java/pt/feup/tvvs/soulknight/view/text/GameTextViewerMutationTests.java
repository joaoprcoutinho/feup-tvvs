package pt.feup.tvvs.soulknight.view.text;

import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import com.googlecode.lanterna.TextColor;

import java.io.IOException;

import static org.mockito.Mockito.*;

class GameTextViewerMutationTests {

    @Test
    void draw_shouldDrawKnownChar() throws IOException {
        // Mocking dependencies
        GUI gui = mock(GUI.class);
        GameTextViewer gameTextViewer = new GameTextViewer();
        char character = 'A';
        TextColor foregroundColor = TextColor.ANSI.RED;
        double x = 5.0, y = 10.0;

        // Simulating the existence of a character in the map
        gameTextViewer.draw(character, x, y, foregroundColor, gui);

        // Verifying that drawKnownChar was called when character is known
        verify(gui, times(1)).drawPixel(anyInt(), anyInt(), eq((TextColor.RGB) foregroundColor));
    }

    @Test
    void draw_shouldDrawUnknownChar() throws IOException {
        // Mocking dependencies
        GUI gui = mock(GUI.class);
        GameTextViewer gameTextViewer = new GameTextViewer();
        char character = 'Z';  // Assuming 'Z' is not in the font map
        TextColor foregroundColor = TextColor.ANSI.RED;
        double x = 5.0, y = 10.0;

        // Simulating the unknown character scenario
        gameTextViewer.draw(character, x, y, foregroundColor, gui);

        // Verifying that drawUnknownChar was called when character is unknown
        verify(gui, times(1)).drawRectangle(anyInt(), anyInt(), eq(3), eq(5), eq((TextColor.RGB) foregroundColor));
    }

    @Test
    void draw_shouldHandleStringOfCharacters() throws IOException {
        // Mocking dependencies
        GUI gui = mock(GUI.class);
        GameTextViewer gameTextViewer = new GameTextViewer();
        String str = "Hello";
        TextColor foregroundColor = TextColor.ANSI.GREEN;
        double x = 5.0, y = 10.0;

        // Simulating drawing a string of characters
        gameTextViewer.draw(str, x, y, foregroundColor, gui);

        // Verifying that draw was called for each character in the string
        for (int i = 0; i < str.length(); i++) {
            verify(gui, times(1)).drawPixel(anyInt(), anyInt(), eq((TextColor.RGB) foregroundColor));
        }
    }

    @Test
    void draw_shouldHandleNullCharMap() throws IOException {
        // Mocking dependencies
        GUI gui = mock(GUI.class);
        GameTextViewer gameTextViewer = new GameTextViewer();

        char character = 'A';
        TextColor foregroundColor = TextColor.ANSI.BLUE;
        double x = 5.0, y = 10.0;

        // Simulating the null charMap case
        gameTextViewer.draw(character, x, y, foregroundColor, gui);

        // Verifying that an unknown character is drawn if the charMap is empty
        verify(gui, times(1)).drawRectangle(anyInt(), anyInt(), eq(3), eq(5), eq((TextColor.RGB) foregroundColor));
    }
}
