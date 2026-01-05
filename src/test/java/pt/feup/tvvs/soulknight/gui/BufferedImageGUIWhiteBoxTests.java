package pt.feup.tvvs.soulknight.gui;
            
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BufferedImageGUIWhiteBoxTests {
    private BufferedImage buffer;
    private BufferedImageGUI gui;

    @BeforeEach
    void setUp() {
        buffer = mock(BufferedImage.class);

        when(buffer.getHeight()).thenReturn(10);
        when(buffer.getWidth()).thenReturn(10);
        when(buffer.createGraphics()).thenReturn(mock(java.awt.Graphics2D.class));
        gui = new BufferedImageGUI(buffer);
    }

    @Test
    public void testGetWidthAndHeight() {
        assertEquals(10, gui.getWidth());
        assertEquals(10, gui.getHeight());
    }

    @Test
    public void testDrawPixelInsideBounds() {
        TextColor.RGB color = new TextColor.RGB(255, 0, 0);
        gui.drawPixel(5, 5, color);

        verify(buffer).setRGB(5,5, new Color(color.getRed(), color.getGreen(), color.getBlue()).getRGB());
    }

    @Test
    public void testDrawPixelOutsideBounds() {
        int before = buffer.getRGB(0, 0);

        gui.drawPixel(-1, -1, new TextColor.RGB(0, 255, 0));
        gui.drawPixel(-1, 5, new TextColor.RGB(0, 255, 0));
        gui.drawPixel(5, -1, new TextColor.RGB(0, 255, 0));
        gui.drawPixel(20, 20, new TextColor.RGB(0, 255, 0));
        gui.drawPixel(buffer.getWidth(), 5, new TextColor.RGB(255, 0, 0));
        gui.drawPixel(5, buffer.getHeight(), new TextColor.RGB(255, 0, 0));

        assertEquals(before, buffer.getRGB(0, 0));
    }

    @Test
    public void testDrawRectangle() {
        TextColor.RGB color = new TextColor.RGB(0, 0, 255);
        gui.drawRectangle(2, 2, 3, 3, color);
        verify(buffer.createGraphics()).setColor(any());
    }

    @Test
    public void testCls() {
        gui.drawPixel(1, 1, new TextColor.RGB(255, 255, 255));
        gui.cls();
        verify(buffer.createGraphics()).setColor(any());
    }

    @Test
    public void testFlush() {
        gui.flush();
    }

    @Test
    public void testClose() {
        gui.close();
    }

    @Test
    public void testDrawText() {
        TextColor.RGB color = new TextColor.RGB(255, 255, 255);
        gui.drawText(1, 5, color, "Test");
        verify(buffer.createGraphics()).setColor(any());
    }

    @Test
    public void testGetACTIONThrowsException() {
        UnsupportedOperationException ex =
                assertThrows(UnsupportedOperationException.class, gui::getACTION);
        assertEquals("Not supported for BufferedImageGUI", ex.getMessage());
    }

    @Test
    public void testGetGUIThrowsException() {
        UnsupportedOperationException ex =
                assertThrows(UnsupportedOperationException.class, gui::getGUI);
        assertEquals("Not supported for BufferedImageGUI", ex.getMessage());
    }

    @Test
    public void testGetFPSThrowsException() {
        UnsupportedOperationException ex =
                assertThrows(UnsupportedOperationException.class, gui::getFPS);
        assertEquals("Not supported for BufferedImageGUI", ex.getMessage());
    }

    @Test
    public void testSetFPSThrowsException() {
        UnsupportedOperationException ex =
                assertThrows(UnsupportedOperationException.class, () -> gui.setFPS(60));
        assertEquals("Not supported for BufferedImageGUI", ex.getMessage());
    }

    @Test
    public void testDrawHitBoxThrowsException() {
        UnsupportedOperationException ex =
                assertThrows(
                        UnsupportedOperationException.class,
                        () -> gui.drawHitBox(0, 0, 2, 2, new TextColor.RGB(255, 0, 0))
                );
        assertEquals("Not implemented for BufferedImageGUI", ex.getMessage());
    }
}