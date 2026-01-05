package pt.feup.tvvs.soulknight.view.elements.spike;
            
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.game.elements.Spike;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.view.sprites.Sprite;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;
import java.io.IOException;

public class SpikeViewerMutationTests {
    private SpriteLoader spriteLoader;
    private Sprite groundSprite;
    private Sprite brown1Sprite;
    private Sprite brown2Sprite;
    private GUI gui;

    @BeforeEach
    void setUp() throws IOException {
        spriteLoader = mock(SpriteLoader.class);
        groundSprite = mock(Sprite.class);
        brown1Sprite = mock(Sprite.class);
        brown2Sprite = mock(Sprite.class);
        gui = mock(GUI.class);
        
        when(spriteLoader.get("sprites/spikes/spike_ground.png")).thenReturn(groundSprite);
        when(spriteLoader.get("sprites/spikes/spike_brown.png")).thenReturn(brown1Sprite);
        when(spriteLoader.get("sprites/spikes/spike_brown_2.png")).thenReturn(brown2Sprite);
    }

    @Test
    public void draw_shouldUseGroundSprite() throws IOException {
        SpikeViewer viewer = new SpikeViewer(spriteLoader);
        Spike spike = mock(Spike.class);
        when(spike.getCharacter()).thenReturn('^');
        when(spike.getPosition()).thenReturn(new Position(10, 20));
        
        viewer.draw(spike, gui, 0, 0, 0);
        
        verify(groundSprite).draw(gui, 10, 20);
    }

    @Test
    public void draw_shouldUseBrown1Sprite() throws IOException {
        SpikeViewer viewer = new SpikeViewer(spriteLoader);
        Spike spike = mock(Spike.class);
        when(spike.getCharacter()).thenReturn('+');
        when(spike.getPosition()).thenReturn(new Position(30, 40));
        
        viewer.draw(spike, gui, 0, 0, 0);
        
        verify(brown1Sprite).draw(gui, 30, 40);
    }

    @Test
    public void draw_shouldUseBrown2Sprite() throws IOException {
        SpikeViewer viewer = new SpikeViewer(spriteLoader);
        Spike spike = mock(Spike.class);
        when(spike.getCharacter()).thenReturn('-');
        when(spike.getPosition()).thenReturn(new Position(50, 60));
        
        viewer.draw(spike, gui, 0, 0, 0);
        
        verify(brown2Sprite).draw(gui, 50, 60);
    }

    @Test
    public void draw_shouldUseCorrectXPosition() throws IOException {
        SpikeViewer viewer = new SpikeViewer(spriteLoader);
        Spike spike = mock(Spike.class);
        when(spike.getCharacter()).thenReturn('^');
        when(spike.getPosition()).thenReturn(new Position(100, 200));
        
        viewer.draw(spike, gui, 0, 0, 0);
        
        verify(groundSprite).draw(eq(gui), eq(100), anyInt());
    }

    @Test
    public void draw_shouldUseCorrectYPosition() throws IOException {
        SpikeViewer viewer = new SpikeViewer(spriteLoader);
        Spike spike = mock(Spike.class);
        when(spike.getCharacter()).thenReturn('^');
        when(spike.getPosition()).thenReturn(new Position(100, 200));
        
        viewer.draw(spike, gui, 0, 0, 0);
        
        verify(groundSprite).draw(eq(gui), anyInt(), eq(200));
    }
}