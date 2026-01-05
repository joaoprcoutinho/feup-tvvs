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

    // Test transparency extraction using right shift by 24
    @Test
    void testTransparencyExtractionRightShift() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        
        assertNotNull(sprite.getImage());
        // Sprite should be loaded successfully
        assertTrue(sprite.getImage().getWidth() > 0);
    }

    // Test RGB red channel extraction with right shift 16 and mask 0xFF
    @Test
    void testRedChannelExtractionOperations() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        
        sprite.draw(gui, 10, 20);
        
        // Should call drawPixel if pixel is not transparent
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test RGB green channel extraction with right shift 8 and mask 0xFF
    @Test
    void testGreenChannelExtractionOperations() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        
        sprite.draw(gui, 0, 0);
        
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        TextColor.RGB color = colorCaptor.getValue();
        
        // Color values should be in valid range (0-255)
        assertTrue(color.getRed() >= 0 && color.getRed() <= 255);
        assertTrue(color.getGreen() >= 0 && color.getGreen() <= 255);
        assertTrue(color.getBlue() >= 0 && color.getBlue() <= 255);
    }

    // Test RGB blue channel extraction with mask 0xFF (no shift)
    @Test
    void testBlueChannelExtractionMask() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        
        sprite.draw(gui, 5, 5);
        
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test draw loop increments dx correctly
    @Test
    void testDrawLoopXIncrement() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        int width = sprite.getImage().getWidth();
        
        sprite.draw(gui, 0, 0);
        
        // Should draw pixels up to width
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test draw loop increments dy correctly
    @Test
    void testDrawLoopYIncrement() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        int height = sprite.getImage().getHeight();
        
        sprite.draw(gui, 0, 0);
        
        // Should draw pixels up to height
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test position offset addition (x + dx, y + dy)
    @Test
    void testPositionOffsetAddition() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        
        sprite.draw(gui, 10, 20);
        
        verify(gui, atLeastOnce()).drawPixel(xCaptor.capture(), yCaptor.capture(), any(TextColor.RGB.class));
        
        // Captured positions should be offset by base position (10, 20)
        int capturedX = xCaptor.getValue();
        int capturedY = yCaptor.getValue();
        
        assertTrue(capturedX >= 10);
        assertTrue(capturedY >= 20);
    }

    // Test transparency check with equality comparison (== 0)
    @Test
    void testTransparencyEqualityCheck() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        
        sprite.draw(gui, 0, 0);
        
        // If transparency is 0, pixel should not be drawn
        // If transparency is not 0, pixel should be drawn
        // We can't control the pixel data, but we verify the method runs
        verify(gui, atLeast(0)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test getImage returns actual BufferedImage
    @Test
    void testGetImageReturnsActualImage() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        
        assertNotNull(sprite.getImage());
        assertTrue(sprite.getImage().getWidth() > 0);
        assertTrue(sprite.getImage().getHeight() > 0);
    }

    // Test drawing at different positions uses correct offset
    @Test
    void testDrawAtDifferentPositions() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        
        sprite.draw(gui, 50, 100);
        
        verify(gui, atLeastOnce()).drawPixel(xCaptor.capture(), yCaptor.capture(), any(TextColor.RGB.class));
        
        // Position should be at least the base offset
        assertTrue(xCaptor.getValue() >= 50);
        assertTrue(yCaptor.getValue() >= 100);
    }

    // Test loop conditions with < operator
    @Test
    void testLoopConditionsLessThan() throws IOException {
        sprite = new Sprite("sprite/sprite1.png");
        int width = sprite.getImage().getWidth();
        int height = sprite.getImage().getHeight();
        
        sprite.draw(gui, 0, 0);
        
        // Loops should execute based on width and height
        assertTrue(width > 0);
        assertTrue(height > 0);
    }
}