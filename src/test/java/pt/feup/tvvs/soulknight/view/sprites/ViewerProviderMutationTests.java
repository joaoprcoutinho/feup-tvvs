package pt.feup.tvvs.soulknight.view.sprites;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ViewerProviderMutationTests {
    
    private ViewerProvider provider;
    private SpriteLoader spriteLoader;

    @BeforeEach
    void setUp() throws IOException {
        spriteLoader = mock(SpriteLoader.class);
        when(spriteLoader.get(anyString())).thenReturn(mock(Sprite.class));
        provider = new ViewerProvider(spriteLoader);
    }

    // Test getParticleViewer returns actual viewer
    @Test
    void testGetParticleViewerReturnsActualViewer() {
        assertNotNull(provider.getParticleViewer());
    }

    // Test getPlayerViewer returns actual viewer
    @Test
    void testGetPlayerViewerReturnsActualViewer() {
        assertNotNull(provider.getPlayerViewer());
    }

    // Test getSpikeViewer returns actual viewer
    @Test
    void testGetSpikeViewerReturnsActualViewer() {
        assertNotNull(provider.getSpikeViewer());
    }

    // Test getTreeViewer returns actual viewer
    @Test
    void testGetTreeViewerReturnsActualViewer() {
        assertNotNull(provider.getTreeViewer());
    }

    // Test getOrbViewer returns actual viewer
    @Test
    void testGetOrbViewerReturnsActualViewer() {
        assertNotNull(provider.getOrbViewer());
    }

    // Test getRockViewer returns actual viewer
    @Test
    void testGetRockViewerReturnsActualViewer() {
        assertNotNull(provider.getRockViewer());
    }

    // Test getTileViewer returns actual viewer
    @Test
    void testGetTileViewerReturnsActualViewer() {
        assertNotNull(provider.getTileViewer());
    }

    // Test getTextViewer returns actual viewer
    @Test
    void testGetTextViewerReturnsActualViewer() {
        assertNotNull(provider.getTextViewer());
    }

    // Test getEntryViewer returns actual viewer
    @Test
    void testGetEntryViewerReturnsActualViewer() {
        assertNotNull(provider.getEntryViewer());
    }

    // Test getLogoViewer returns actual viewer
    @Test
    void testGetLogoViewerReturnsActualViewer() {
        assertNotNull(provider.getLogoViewer());
    }

    // Test getMonsterViewer returns actual viewer
    @Test
    void testGetMonsterViewerReturnsActualViewer() {
        assertNotNull(provider.getMonsterViewer());
    }

    // Test that all viewers are initialized in constructor
    @Test
    void testAllViewersInitializedInConstructor() {
        assertNotNull(provider.getParticleViewer());
        assertNotNull(provider.getPlayerViewer());
        assertNotNull(provider.getSpikeViewer());
        assertNotNull(provider.getTreeViewer());
        assertNotNull(provider.getOrbViewer());
        assertNotNull(provider.getRockViewer());
        assertNotNull(provider.getTileViewer());
        assertNotNull(provider.getMonsterViewer());
        assertNotNull(provider.getTextViewer());
        assertNotNull(provider.getEntryViewer());
        assertNotNull(provider.getLogoViewer());
    }

    // Test that getters return same instance on multiple calls
    @Test
    void testGettersReturnSameInstance() {
        assertSame(provider.getParticleViewer(), provider.getParticleViewer());
        assertSame(provider.getPlayerViewer(), provider.getPlayerViewer());
        assertSame(provider.getSpikeViewer(), provider.getSpikeViewer());
        assertSame(provider.getTextViewer(), provider.getTextViewer());
    }
}