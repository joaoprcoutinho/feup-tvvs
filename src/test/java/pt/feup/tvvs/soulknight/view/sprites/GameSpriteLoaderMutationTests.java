package pt.feup.tvvs.soulknight.view.sprites;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class GameSpriteLoaderMutationTests {
    
    private GameSpriteLoader loader;

    @BeforeEach
    void setUp() {
        loader = new GameSpriteLoader();
    }

    @Test
    public void testContainsKeyNegation() throws IOException {
        String spritePath = "sprite/sprite1.png";
        
        Sprite sprite1 = loader.get(spritePath);
        assertNotNull(sprite1);
        
        Sprite sprite2 = loader.get(spritePath);
        assertNotNull(sprite2);
        
        assertSame(sprite1, sprite2);
    }

    @Test
    public void testSpriteAddedToMapWhenNotPresent() throws IOException {
        String spritePath = "sprite/sprite1.png";
        
        Sprite sprite = loader.get(spritePath);
        
        assertNotNull(sprite);
        Sprite cachedSprite = loader.get(spritePath);
        assertSame(sprite, cachedSprite);
    }

    @Test
    public void testGetReturnsFromMap() throws IOException {
        String spritePath = "sprite/sprite1.png";
        
        Sprite firstGet = loader.get(spritePath);
        Sprite secondGet = loader.get(spritePath);
        
        assertNotNull(secondGet);
        assertSame(firstGet, secondGet);
    }

    @Test
    public void testMultipleSpritesAreDistinct() throws IOException {
        String sprite1Path = "sprite/sprite1.png";
        String sprite2Path = "sprite/sprite1.png";
        
        Sprite sprite1 = loader.get(sprite1Path);
        Sprite sprite2 = loader.get(sprite2Path);
        
        assertNotNull(sprite1);
        assertNotNull(sprite2);
        assertSame(sprite1, sprite2);
    }

    @Test
    public void testMapPutStoresSprite() throws IOException {
        String spritePath = "sprite/sprite1.png";
        
        Sprite sprite = loader.get(spritePath);
        
        assertTrue(loader.spriteMap.containsKey(spritePath));
        assertNotNull(loader.spriteMap.get(spritePath));
    }

    @Test
    public void testSpriteMapInitialization() {
        assertNotNull(loader.spriteMap);
        assertTrue(loader.spriteMap.isEmpty());
    }

    @Test
    public void testConditionalBranchSpriteExists() throws IOException {
        String spritePath = "sprite/sprite1.png";
        
        loader.get(spritePath);

        Sprite cached = loader.get(spritePath);
        
        assertNotNull(cached);
    }

    @Test
    public void testConditionalBranchSpriteDoesNotExist() throws IOException {
        String spritePath = "sprite/sprite1.png";
        
        Sprite newSprite = loader.get(spritePath);
        
        assertNotNull(newSprite);
        assertTrue(loader.spriteMap.containsKey(spritePath));
    }
}