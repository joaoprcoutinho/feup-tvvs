package pt.feup.tvvs.soulknight.view.elements.particle;
            
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.game.elements.particle.Particle;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import com.googlecode.lanterna.TextColor;
import java.io.IOException;

public class ParticleViewerMutationTests {
    private ParticleViewer viewer;
    private GUI gui;
    private Particle particle;

    @BeforeEach
    void setUp() {
        viewer = new ParticleViewer();
        gui = mock(GUI.class);
        particle = mock(Particle.class);
    }

    @Test
    void draw_shouldUseParticlePosition() throws IOException {
        when(particle.getColor()).thenReturn(new TextColor.RGB(100, 150, 200));
        when(particle.getOpacity()).thenReturn(1.0);
        when(particle.getPosition()).thenReturn(new Position(50, 60));
        
        viewer.draw(particle, gui, 0, 0, 0);
        
        verify(gui).drawPixel(eq(50), eq(60), any(TextColor.RGB.class));
    }

    @Test
    void draw_shouldMultiplyRedByOpacity() throws IOException {
        when(particle.getColor()).thenReturn(new TextColor.RGB(100, 0, 0));
        when(particle.getOpacity()).thenReturn(0.5);
        when(particle.getPosition()).thenReturn(new Position(10, 20));
        
        viewer.draw(particle, gui, 0, 0, 0);
        
        verify(gui).drawPixel(anyInt(), anyInt(), argThat(color -> 
            color instanceof TextColor.RGB && ((TextColor.RGB) color).getRed() == 50
        ));
    }

    @Test
    void draw_shouldMultiplyGreenByOpacity() throws IOException {
        when(particle.getColor()).thenReturn(new TextColor.RGB(0, 100, 0));
        when(particle.getOpacity()).thenReturn(0.5);
        when(particle.getPosition()).thenReturn(new Position(10, 20));
        
        viewer.draw(particle, gui, 0, 0, 0);
        
        verify(gui).drawPixel(anyInt(), anyInt(), argThat(color -> 
            color instanceof TextColor.RGB && ((TextColor.RGB) color).getGreen() == 50
        ));
    }

    @Test
    void draw_shouldMultiplyBlueByOpacity() throws IOException {
        when(particle.getColor()).thenReturn(new TextColor.RGB(0, 0, 100));
        when(particle.getOpacity()).thenReturn(0.5);
        when(particle.getPosition()).thenReturn(new Position(10, 20));
        
        viewer.draw(particle, gui, 0, 0, 0);
        
        verify(gui).drawPixel(anyInt(), anyInt(), argThat(color -> 
            color instanceof TextColor.RGB && ((TextColor.RGB) color).getBlue() == 50
        ));
    }

    @Test
    void draw_shouldClampOpacityToMax1() throws IOException {
        when(particle.getColor()).thenReturn(new TextColor.RGB(100, 100, 100));
        when(particle.getOpacity()).thenReturn(1.5); // Above 1
        when(particle.getPosition()).thenReturn(new Position(10, 20));
        
        viewer.draw(particle, gui, 0, 0, 0);
        
        // Should clamp to 1.0, so color stays 100
        verify(gui).drawPixel(anyInt(), anyInt(), argThat(color -> 
            color instanceof TextColor.RGB && ((TextColor.RGB) color).getRed() == 100
        ));
    }

    @Test
    void draw_shouldClampOpacityToMin0() throws IOException {
        when(particle.getColor()).thenReturn(new TextColor.RGB(100, 100, 100));
        when(particle.getOpacity()).thenReturn(-0.5); // Below 0
        when(particle.getPosition()).thenReturn(new Position(10, 20));
        
        viewer.draw(particle, gui, 0, 0, 0);
        
        // Should clamp to 0.0, so color becomes 0
        verify(gui).drawPixel(anyInt(), anyInt(), argThat(color -> 
            color instanceof TextColor.RGB && ((TextColor.RGB) color).getRed() == 0
        ));
    }

    @Test
    void draw_shouldUseCorrectXPosition() throws IOException {
        when(particle.getColor()).thenReturn(new TextColor.RGB(100, 100, 100));
        when(particle.getOpacity()).thenReturn(1.0);
        when(particle.getPosition()).thenReturn(new Position(75, 85));
        
        viewer.draw(particle, gui, 0, 0, 0);
        
        verify(gui).drawPixel(eq(75), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    void draw_shouldUseCorrectYPosition() throws IOException {
        when(particle.getColor()).thenReturn(new TextColor.RGB(100, 100, 100));
        when(particle.getOpacity()).thenReturn(1.0);
        when(particle.getPosition()).thenReturn(new Position(75, 85));
        
        viewer.draw(particle, gui, 0, 0, 0);
        
        verify(gui).drawPixel(anyInt(), eq(85), any(TextColor.RGB.class));
    }
}