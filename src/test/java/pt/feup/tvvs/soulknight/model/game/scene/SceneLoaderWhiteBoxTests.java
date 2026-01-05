package pt.feup.tvvs.soulknight.model.game.scene;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SceneLoaderWhiteBoxTests {
    private SceneLoader loader;

    @BeforeEach
    void setup() throws IOException{
        loader = new SceneLoader(1);
    }

    @Test
    public void testLevelNotExists(){
        assertThrows(FileNotFoundException.class, () -> new SceneLoader(999));
    }

    @Test
    public void testGetHeight() {
        int height = loader.getHeight();
        assertTrue(height > 0);
    }

    @Test
    public void testGetWidth(){
        int width = loader.getWidth();
        assertTrue(width > 0);
    }
}