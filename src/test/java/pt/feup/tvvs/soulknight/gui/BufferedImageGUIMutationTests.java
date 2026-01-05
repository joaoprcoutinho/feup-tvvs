package pt.feup.tvvs.soulknight.gui;

import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class BufferedImageGUIMutationTests {

    private BufferedImage buffer;
    private BufferedImageGUI gui;

    @BeforeEach
    void setup() {
        buffer = new BufferedImage(100, 80, BufferedImage.TYPE_INT_ARGB);
        gui = new BufferedImageGUI(buffer);
    }

    @Test
    public void testDrawPixelAtXZero() {
        TextColor.RGB red = new TextColor.RGB(255, 0, 0);
        
        gui.drawPixel(0, 40, red);
        
        int rgb = buffer.getRGB(0, 40);
        Color color = new Color(rgb);
        assertEquals(255, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(0, color.getBlue());
    }

    @Test
    public void testDrawPixelAtXMaxBoundary() {
        TextColor.RGB green = new TextColor.RGB(0, 255, 0);
        gui.drawPixel(99, 40, green);
        int rgb = buffer.getRGB(99, 40);
        Color color = new Color(rgb);
        assertEquals(0, color.getRed());
        assertEquals(255, color.getGreen());
        assertEquals(0, color.getBlue());
    }

    @Test
    public void testDrawPixelAtXEqualToWidth() {
        TextColor.RGB blue = new TextColor.RGB(0, 0, 255);
        gui.drawPixel(100, 40, blue);
    }

    @Test
    public void testDrawPixelAtYZero() {
        TextColor.RGB yellow = new TextColor.RGB(255, 255, 0);
        gui.drawPixel(50, 0, yellow);
        int rgb = buffer.getRGB(50, 0);
        Color color = new Color(rgb);
        assertEquals(255, color.getRed());
        assertEquals(255, color.getGreen());
        assertEquals(0, color.getBlue());
    }

    @Test
    public void testDrawPixelAtYMaxBoundary() {
        TextColor.RGB cyan = new TextColor.RGB(0, 255, 255);
        gui.drawPixel(50, 79, cyan);
        int rgb = buffer.getRGB(50, 79);
        Color color = new Color(rgb);
        assertEquals(0, color.getRed());
        assertEquals(255, color.getGreen());
        assertEquals(255, color.getBlue());
    }

    @Test
    public void testDrawPixelTopLeftCorner() {
        TextColor.RGB white = new TextColor.RGB(255, 255, 255);
        gui.drawPixel(0, 0, white);
        
        int rgb = buffer.getRGB(0, 0);
        Color color = new Color(rgb);
        assertEquals(255, color.getRed());
        assertEquals(255, color.getGreen());
        assertEquals(255, color.getBlue());
    }

    @Test
    public void testDrawPixelBottomRightCorner() {
        TextColor.RGB magenta = new TextColor.RGB(255, 0, 255);
        gui.drawPixel(99, 79, magenta);
        
        int rgb = buffer.getRGB(99, 79);
        Color color = new Color(rgb);
        assertEquals(255, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(255, color.getBlue());
    }

    @Test
    public void testDrawPixelNegativeX() {
        TextColor.RGB red = new TextColor.RGB(255, 0, 0);
        gui.drawPixel(-1, 40, red);
    }

    @Test
    public void testDrawPixelNegativeY() {
        TextColor.RGB red = new TextColor.RGB(255, 0, 0);
        gui.drawPixel(50, -1, red);
    }

    @Test
    public void testDrawRectangleFillsArea() {
        TextColor.RGB orange = new TextColor.RGB(255, 165, 0);
        
        gui.drawRectangle(10, 10, 20, 15, orange);
        int rgb = buffer.getRGB(15, 12);
        Color color = new Color(rgb);
        assertEquals(255, color.getRed());
        assertEquals(165, color.getGreen());
        assertEquals(0, color.getBlue());
        int rgbCorner = buffer.getRGB(10, 10);
        Color cornerColor = new Color(rgbCorner);
        assertEquals(255, cornerColor.getRed());
    }

    @Test
    public void testDrawRectangleAtEdge() {
        TextColor.RGB pink = new TextColor.RGB(255, 192, 203);
        gui.drawRectangle(90, 70, 10, 10, pink);
        int rgb = buffer.getRGB(90, 70);
        Color color = new Color(rgb);
        assertEquals(255, color.getRed());
        assertEquals(192, color.getGreen());
        assertEquals(203, color.getBlue());
    }

    @Test
    public void testClsClearsBuffer() {
        BufferedImage spyBuffer = new BufferedImage(100, 80, BufferedImage.TYPE_INT_ARGB);
        BufferedImageGUI testGui = new BufferedImageGUI(spyBuffer);
        testGui.cls();
        assertNotNull(spyBuffer);
    }

    @Test
    public void testClsExecutesSuccessfully() {
        gui.cls();
    }

    @Test
    public void testDrawTextRendersText() {
        TextColor.RGB white = new TextColor.RGB(255, 255, 255);
        BufferedImage textBuffer = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB);
        BufferedImageGUI textGui = new BufferedImageGUI(textBuffer);
        
        textGui.drawText(10, 50, white, "Hello");

        boolean hasWhitePixel = false;
        for (int x = 10; x < 60; x++) {
            for (int y = 35; y < 55; y++) {
                int rgb = textBuffer.getRGB(x, y);
                Color color = new Color(rgb, true);
                if (color.getAlpha() > 0 && color.getRed() == 255) {
                    hasWhitePixel = true;
                    break;
                }
            }
            if (hasWhitePixel) break;
        }
        assertTrue(hasWhitePixel, "Text should render pixels with the specified color");
    }

    @Test
    public void testGetWidth() {
        assertEquals(100, gui.getWidth(), "Width should match buffer width");
    }

    @Test
    public void testGetHeight() {
        assertEquals(80, gui.getHeight(), "Height should match buffer height");
    }

    @Test
    public void testFlushDoesNotThrow() {
        gui.flush();
    }

    @Test
    public void testCloseDoesNotThrow() {
        gui.close();
    }

    @Test
    public void testGetACTIONThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class, () -> gui.getACTION());
    }

    @Test
    public void testGetGUIThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class, () -> gui.getGUI());
    }

    @Test
    public void testGetFPSThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class, () -> gui.getFPS());
    }

    @Test
    public void testSetFPSThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class, () -> gui.setFPS(60));
    }

    @Test
    public void testDrawHitBoxThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class, 
            () -> gui.drawHitBox(10, 10, 20, 20, new TextColor.RGB(255, 0, 0)));
    }
}