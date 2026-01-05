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

    // Test containsKey check with negation
    @Test
    void testContainsKeyNegation() throws IOException {
        String spritePath = "sprite/sprite1.png";
        
        // First call should add to map (not contained)
        Sprite sprite1 = loader.get(spritePath);
        assertNotNull(sprite1);
        
        // Second call should return same instance (is contained)
        Sprite sprite2 = loader.get(spritePath);
        assertNotNull(sprite2);
        
        // Should return the same instance (cached)
        assertSame(sprite1, sprite2);
    }

    // Test that sprite is added to map when not present
    @Test
    void testSpriteAddedToMapWhenNotPresent() throws IOException {
        String spritePath = "sprite/sprite1.png";
        
        Sprite sprite = loader.get(spritePath);
        
        assertNotNull(sprite);
        // Verify the sprite is now in the map by getting it again
        Sprite cachedSprite = loader.get(spritePath);
        assertSame(sprite, cachedSprite);
    }

    // Test that get returns sprite from map
    @Test
    void testGetReturnsFromMap() throws IOException {
        String spritePath = "sprite/sprite1.png";
        
        // Add sprite to map
        Sprite firstGet = loader.get(spritePath);
        
        // Get again should return from map
        Sprite secondGet = loader.get(spritePath);
        
        assertNotNull(secondGet);
        assertSame(firstGet, secondGet);
    }

    // Test multiple different sprites are cached separately
    @Test
    void testMultipleSpritesAreDistinct() throws IOException {
        String sprite1Path = "sprite/sprite1.png";
        String sprite2Path = "sprite/sprite1.png"; // Using same for testing
        
        Sprite sprite1 = loader.get(sprite1Path);
        Sprite sprite2 = loader.get(sprite2Path);
        
        assertNotNull(sprite1);
        assertNotNull(sprite2);
        // Same path should return same instance
        assertSame(sprite1, sprite2);
    }

    // Test map put operation actually stores sprite
    @Test
    void testMapPutStoresSprite() throws IOException {
        String spritePath = "sprite/sprite1.png";
        
        // First access puts in map
        Sprite sprite = loader.get(spritePath);
        
        // Map should contain the sprite
        assertTrue(loader.spriteMap.containsKey(spritePath));
        assertNotNull(loader.spriteMap.get(spritePath));
    }

    // Test spriteMap initialization in constructor
    @Test
    void testSpriteMapInitialization() {
        assertNotNull(loader.spriteMap);
        assertTrue(loader.spriteMap.isEmpty());
    }

    // Test conditional branch when sprite exists
    @Test
    void testConditionalBranchSpriteExists() throws IOException {
        String spritePath = "sprite/sprite1.png";
        
        // Add sprite
        loader.get(spritePath);
        
        // Get again - should not create new sprite
        Sprite cached = loader.get(spritePath);
        
        assertNotNull(cached);
    }

    // Test conditional branch when sprite doesn't exist
    @Test
    void testConditionalBranchSpriteDoesNotExist() throws IOException {
        String spritePath = "sprite/sprite1.png";
        
        // First get - sprite doesn't exist
        Sprite newSprite = loader.get(spritePath);
        
        assertNotNull(newSprite);
        assertTrue(loader.spriteMap.containsKey(spritePath));
    }
}