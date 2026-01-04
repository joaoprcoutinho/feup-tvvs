package pt.feup.tvvs.soulknight.view.sprites;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class GameSpriteLoaderWhiteBoxTest {
    private GameSpriteLoader loader;

    @BeforeEach
    void setUp() {
        loader = new GameSpriteLoader();
    }

    @Test
    void testGet() throws IOException{
        Sprite sprite = loader.get("sprites/Knight.png");
        assertNotNull(sprite);
        sprite = loader.get("sprites/Knight.png");
        assertNotNull(sprite);
    }
}