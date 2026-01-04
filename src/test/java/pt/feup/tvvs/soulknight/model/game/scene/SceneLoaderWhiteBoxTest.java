package pt.feup.tvvs.soulknight.model.game.scene;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SceneLoaderWhiteBoxTest {
    private SceneLoader loader;

    @BeforeEach
    void setup() throws IOException{
        loader = new SceneLoader(1);
    }

    @Test
    void testLevelNotExists(){
        assertThrows(FileNotFoundException.class, () -> new SceneLoader(999));
    }

    @Test
    void testGetHeight() {
        int height = loader.getHeight();
        assertTrue(height > 0);
    }

    @Test
    void testGetWidth(){
        int width = loader.getWidth();
        assertTrue(width > 0);
    }
}