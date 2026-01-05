package pt.feup.tvvs.soulknight.view.sprites;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

public class GameSpriteLoaderWhiteBoxTests {
    private GameSpriteLoader loader;

    @BeforeEach
    void setUp() {
        loader = new GameSpriteLoader();
    }

    @Test
    public void testGet() throws IOException{
        Sprite sprite = loader.get("sprites/Knight.png");
        assertNotNull(sprite);
        sprite = loader.get("sprites/Knight.png");
        assertNotNull(sprite);
    }
}