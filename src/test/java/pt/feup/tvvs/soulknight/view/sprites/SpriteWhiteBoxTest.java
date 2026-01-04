package pt.feup.tvvs.soulknight.view.sprites;
            
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class SpriteWhiteBoxTest {
    private GUI gui;
    private Sprite sprite;

    @BeforeEach
    void setUp() throws IOException {
        gui = mock(GUI.class);
        sprite = new Sprite("sprites/Knight.png");
    }

    @Test
    void testGetImage() {
        assertNotNull(sprite.getImage());
    }

    @Test
    void testDraw() throws IOException {
        sprite.draw(gui, 0, 0);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }
}