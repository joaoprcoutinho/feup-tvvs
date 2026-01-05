package pt.feup.tvvs.soulknight.view.elements.tile;
            
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.game.elements.tile.Tile;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.view.sprites.Sprite;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;
import java.io.IOException;

public class TileViewerMutationTests {
    private SpriteLoader spriteLoader;
    private Sprite metalSprite;
    private Sprite grassSprite;
    private Sprite lavaSprite;
    private GUI gui;

    @BeforeEach
    void setUp() throws IOException {
        spriteLoader = mock(SpriteLoader.class);
        metalSprite = mock(Sprite.class);
        grassSprite = mock(Sprite.class);
        lavaSprite = mock(Sprite.class);
        gui = mock(GUI.class);
        
        when(spriteLoader.get("sprites/Tiles/ground_metal.png")).thenReturn(metalSprite);
        when(spriteLoader.get("sprites/Tiles/ground_grass.png")).thenReturn(grassSprite);
        when(spriteLoader.get("sprites/Tiles/ground_rocky_lava.png")).thenReturn(lavaSprite);
    }

    @Test
    public void draw_shouldUseMetalSprite() throws IOException {
        TileViewer viewer = new TileViewer(spriteLoader);
        Tile tile = mock(Tile.class);
        when(tile.getCharacter()).thenReturn('M');
        when(tile.getPosition()).thenReturn(new Position(10, 20));
        
        viewer.draw(tile, gui, 0, 0, 0);
        
        verify(metalSprite).draw(gui, 10, 20);
    }

    @Test
    public void draw_shouldUseGrassSprite() throws IOException {
        TileViewer viewer = new TileViewer(spriteLoader);
        Tile tile = mock(Tile.class);
        when(tile.getCharacter()).thenReturn('G');
        when(tile.getPosition()).thenReturn(new Position(30, 40));
        
        viewer.draw(tile, gui, 0, 0, 0);
        
        verify(grassSprite).draw(gui, 30, 40);
    }

    @Test
    public void draw_shouldUseLavaSprite() throws IOException {
        TileViewer viewer = new TileViewer(spriteLoader);
        Tile tile = mock(Tile.class);
        when(tile.getCharacter()).thenReturn('L');
        when(tile.getPosition()).thenReturn(new Position(50, 60));
        
        viewer.draw(tile, gui, 0, 0, 0);
        
        verify(lavaSprite).draw(gui, 50, 60);
    }

    @Test
    public void draw_shouldUseCorrectXPosition() throws IOException {
        TileViewer viewer = new TileViewer(spriteLoader);
        Tile tile = mock(Tile.class);
        when(tile.getCharacter()).thenReturn('M');
        when(tile.getPosition()).thenReturn(new Position(100, 200));
        
        viewer.draw(tile, gui, 0, 0, 0);
        
        verify(metalSprite).draw(eq(gui), eq(100), anyInt());
    }

    @Test
    public void draw_shouldUseCorrectYPosition() throws IOException {
        TileViewer viewer = new TileViewer(spriteLoader);
        Tile tile = mock(Tile.class);
        when(tile.getCharacter()).thenReturn('M');
        when(tile.getPosition()).thenReturn(new Position(100, 200));
        
        viewer.draw(tile, gui, 0, 0, 0);
        
        verify(metalSprite).draw(eq(gui), anyInt(), eq(200));
    }
}