package pt.feup.tvvs.soulknight.gui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.awt.event.KeyEvent.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;

public class LanternaGUIWhiteBoxTest {
    private LanternaGUI gui;
    private ScreenGenerator screenGenerator;
    private Screen screen;
    private TextGraphics textGraphics;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException, FontFormatException {
        screenGenerator = mock(ScreenGenerator.class);
        screen = mock(Screen.class);
        textGraphics = mock(TextGraphics.class);

        when(screenGenerator.createScreen(any(), anyString(), any(KeyAdapter.class))).thenReturn(screen);
        when(screenGenerator.getWidth()).thenReturn(80);
        when(screen.newTextGraphics()).thenReturn(textGraphics);

        gui = new LanternaGUI(screenGenerator, "Test");
    }

    @Test
    void testGetActionNull() throws IOException {
        assertEquals(GUI.ACTION.NULL, gui.getACTION());
    }

    @Test
    void testKeyActions() throws IOException {
        KeyAdapter adapter = gui.getKeyAdapter();

        adapter.keyPressed(new KeyEvent(new java.awt.Canvas(), 0, 0, 0, VK_LEFT));
        assertEquals(GUI.ACTION.LEFT, gui.getACTION());

        adapter.keyPressed(new KeyEvent(new java.awt.Canvas(), 0, 0, 0, VK_RIGHT));
        assertEquals(GUI.ACTION.RIGHT, gui.getACTION());

        adapter.keyPressed(new KeyEvent(new java.awt.Canvas(), 0, 0, 0, VK_UP));
        assertEquals(GUI.ACTION.UP, gui.getACTION());

        adapter.keyPressed(new KeyEvent(new java.awt.Canvas(), 0, 0, 0, VK_DOWN));
        assertEquals(GUI.ACTION.DOWN, gui.getACTION());

        adapter.keyPressed(new KeyEvent(new java.awt.Canvas(), 0, 0, 0, VK_X));
        assertEquals(GUI.ACTION.DASH, gui.getACTION());

        adapter.keyPressed(new KeyEvent(new java.awt.Canvas(), 0, 0, 0, VK_ESCAPE));
        assertEquals(GUI.ACTION.QUIT, gui.getACTION());

        adapter.keyPressed(new KeyEvent(new java.awt.Canvas(), 0, 0, 0, VK_Q));
        assertEquals(GUI.ACTION.KILL, gui.getACTION());

        adapter.keyPressed(new KeyEvent(new java.awt.Canvas(), 0, 0, 0, VK_ENTER));
        assertEquals(GUI.ACTION.SELECT, gui.getACTION());

        adapter.keyPressed(new KeyEvent(new java.awt.Canvas(), 0, 0, 0, VK_SPACE));
        assertEquals(GUI.ACTION.JUMP, gui.getACTION());

        adapter.keyPressed(new KeyEvent(new java.awt.Canvas(), 0, 0, 0, VK_E));
        assertEquals(GUI.ACTION.NULL, gui.getACTION());
    }

    @Test
    void testKeyReleaseSpamKey() throws IOException {
        KeyAdapter adapter = gui.getKeyAdapter();

        KeyEvent left = new KeyEvent(new java.awt.Canvas(), 0, 0, 0, VK_LEFT);
        adapter.keyPressed(left);
        adapter.keyReleased(left);

        assertEquals(GUI.ACTION.NULL, gui.getACTION());
    }

    @Test
    void testKeyReleaseNoSpamKey() throws IOException {
        KeyAdapter adapter = gui.getKeyAdapter();

        KeyEvent e = new KeyEvent(new java.awt.Canvas(), 0, 0, 0, VK_E);
        adapter.keyPressed(e);
        adapter.keyReleased(e);

        assertEquals(GUI.ACTION.NULL, gui.getACTION());
    }

    @Test
    void testFPS() {
        gui.setFPS(60);
        assertEquals(60, gui.getFPS());
    }

    @Test
    void testDrawPixel() {
        gui.drawPixel(1, 2, new TextColor.RGB(255, 0, 0));
        verify(textGraphics).putString(1, 2, " ");
    }

    @Test
    void testDrawRectangle() {
        gui.drawRectangle(0, 0, 2, 2, new TextColor.RGB(0, 255, 0));
        verify(textGraphics, times(4)).putString(anyInt(), anyInt(), eq(" "));
    }

    @Test
    void testDrawHitBox() {
        gui.drawHitBox(0, 0, 3, 3, new TextColor.RGB(0, 0, 255));
        verify(textGraphics, atLeastOnce()).putString(anyInt(), anyInt(), eq(" "));
    }

    @Test
    void testDrawText() {
        gui.drawText(5, 5, new TextColor.RGB(255, 255, 255), "Hello");
        verify(textGraphics).putString(5, 5, "Hello");
    }

    @Test
    void testCLS() {
        gui.cls();
        verify(screen).clear();
    }

    @Test
    void testFlush() throws IOException {
        gui.flush();
        verify(screen).refresh();
    }

    @Test
    void testClose() throws IOException {
        gui.close();
        verify(screen).close();
    }

    @Test
    void testGetWidthAndHeight() {
        when(screenGenerator.getWidth()).thenReturn(100);
        assertEquals(100, gui.getWidth());
        assertEquals(100, gui.getHeight());
    }

    @Test
    void testGetGUI() {
        assertSame(gui, gui.getGUI());
    }

    @Test
    void testResolutionScale() throws IOException, URISyntaxException, FontFormatException {
        RescalableGUI.ResolutionScale scale = mock(RescalableGUI.ResolutionScale.class);
        gui.setResolutionScale(scale);
        assertEquals(gui.getResolutionScale(), scale);
        verify(screen).close();
    }
}