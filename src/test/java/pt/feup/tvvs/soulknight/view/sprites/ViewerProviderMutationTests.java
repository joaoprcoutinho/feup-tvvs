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

    @Test
    public void testGetParticleViewerReturnsActualViewer() {
        assertNotNull(provider.getParticleViewer());
    }

    @Test
    public void testGetPlayerViewerReturnsActualViewer() {
        assertNotNull(provider.getPlayerViewer());
    }

    @Test
    public void testGetSpikeViewerReturnsActualViewer() {
        assertNotNull(provider.getSpikeViewer());
    }

    @Test
    public void testGetTreeViewerReturnsActualViewer() {
        assertNotNull(provider.getTreeViewer());
    }

    @Test
    public void testGetOrbViewerReturnsActualViewer() {
        assertNotNull(provider.getOrbViewer());
    }

    @Test
    public void testGetRockViewerReturnsActualViewer() {
        assertNotNull(provider.getRockViewer());
    }

    @Test
    public void testGetTileViewerReturnsActualViewer() {
        assertNotNull(provider.getTileViewer());
    }

    @Test
    public void testGetTextViewerReturnsActualViewer() {
        assertNotNull(provider.getTextViewer());
    }

    @Test
    public void testGetEntryViewerReturnsActualViewer() {
        assertNotNull(provider.getEntryViewer());
    }

    @Test
    public void testGetLogoViewerReturnsActualViewer() {
        assertNotNull(provider.getLogoViewer());
    }

    @Test
    public void testGetMonsterViewerReturnsActualViewer() {
        assertNotNull(provider.getMonsterViewer());
    }

    @Test
    public void testAllViewersInitializedInConstructor() {
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

    @Test
    public void testGettersReturnSameInstance() {
        assertSame(provider.getParticleViewer(), provider.getParticleViewer());
        assertSame(provider.getPlayerViewer(), provider.getPlayerViewer());
        assertSame(provider.getSpikeViewer(), provider.getSpikeViewer());
        assertSame(provider.getTextViewer(), provider.getTextViewer());
    }
}