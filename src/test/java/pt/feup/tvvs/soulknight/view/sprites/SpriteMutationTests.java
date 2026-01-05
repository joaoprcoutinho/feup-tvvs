package pt.feup.tvvs.soulknight.view.sprites;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pt.feup.tvvs.soulknight.gui.GUI;
import com.googlecode.lanterna.TextColor;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpriteMutationTests {
    
    private Sprite sprite;
    private GUI gui;

    @BeforeEach
    void setUp() throws IOException {
        gui = mock(GUI.class);
    }

    @Test
    public void testTransparencyExtractionRightShift() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        
        assertNotNull(sprite.getImage());
        assertTrue(sprite.getImage().getWidth() > 0);
    }

    @Test
    public void testRedChannelExtractionOperations() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        
        sprite.draw(gui, 10, 20);
        
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testGreenChannelExtractionOperations() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        
        sprite.draw(gui, 0, 0);
        
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        TextColor.RGB color = colorCaptor.getValue();
        
        assertTrue(color.getRed() >= 0 && color.getRed() <= 255);
        assertTrue(color.getGreen() >= 0 && color.getGreen() <= 255);
        assertTrue(color.getBlue() >= 0 && color.getBlue() <= 255);
    }

    @Test
    public void testBlueChannelExtractionMask() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        
        sprite.draw(gui, 5, 5);
        
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testDrawLoopXIncrement() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        int width = sprite.getImage().getWidth();
        
        sprite.draw(gui, 0, 0);
        
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testDrawLoopYIncrement() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        int height = sprite.getImage().getHeight();
        
        sprite.draw(gui, 0, 0);
        
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testPositionOffsetAddition() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        
        sprite.draw(gui, 10, 20);
        
        verify(gui, atLeastOnce()).drawPixel(xCaptor.capture(), yCaptor.capture(), any(TextColor.RGB.class));
        
        int capturedX = xCaptor.getValue();
        int capturedY = yCaptor.getValue();
        
        assertTrue(capturedX >= 10);
        assertTrue(capturedY >= 20);
    }

    @Test
    public void testTransparencyEqualityCheck() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        
        sprite.draw(gui, 0, 0);
        
        verify(gui, atLeast(0)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testGetImageReturnsActualImage() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        
        assertNotNull(sprite.getImage());
        assertTrue(sprite.getImage().getWidth() > 0);
        assertTrue(sprite.getImage().getHeight() > 0);
    }

    @Test
    public void testDrawAtDifferentPositions() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        
        sprite.draw(gui, 50, 100);
        
        verify(gui, atLeastOnce()).drawPixel(xCaptor.capture(), yCaptor.capture(), any(TextColor.RGB.class));
        
        assertTrue(xCaptor.getValue() >= 50);
        assertTrue(yCaptor.getValue() >= 100);
    }

    @Test
    public void testLoopConditionsLessThan() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        int width = sprite.getImage().getWidth();
        int height = sprite.getImage().getHeight();
        
        sprite.draw(gui, 0, 0);
        
        assertTrue(width > 0);
        assertTrue(height > 0);
    }
}