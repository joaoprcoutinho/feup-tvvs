package pt.feup.tvvs.soulknight.gui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;

import static java.awt.event.KeyEvent.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LanternaGUIMutationTests {

    private Screen mockScreen;
    private ScreenGenerator mockScreenGenerator;
    private TextGraphics mockTextGraphics;

    @BeforeEach
    void setup() throws IOException, URISyntaxException, FontFormatException {
        mockScreen = mock(Screen.class);
        mockScreenGenerator = mock(ScreenGenerator.class);
        mockTextGraphics = mock(TextGraphics.class);
        
        when(mockScreen.newTextGraphics()).thenReturn(mockTextGraphics);
        when(mockScreenGenerator.createScreen(any(), anyString(), any())).thenReturn(mockScreen);
        when(mockScreenGenerator.getWidth()).thenReturn(184);
    }

    @Test
    public void testSetCursorPositionCalled() throws IOException, URISyntaxException, FontFormatException {
        new LanternaGUI(mockScreenGenerator, "Test");
        verify(mockScreen).setCursorPosition(null);
    }

    @Test
    public void testStartScreenCalled() throws IOException, URISyntaxException, FontFormatException {
        new LanternaGUI(mockScreenGenerator, "Test");
        verify(mockScreen).startScreen();
    }

    @Test
    public void testDrawRectanglePositions() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        TextColor.RGB color = new TextColor.RGB(255, 0, 0);
        
        gui.drawRectangle(10, 20, 3, 2, color);
        
        verify(mockTextGraphics).putString(10, 20, " ");
        verify(mockTextGraphics).putString(11, 20, " ");
        verify(mockTextGraphics).putString(12, 20, " ");
        verify(mockTextGraphics).putString(10, 21, " ");
        verify(mockTextGraphics).putString(11, 21, " ");
        verify(mockTextGraphics).putString(12, 21, " ");
    }

    @Test
    public void testDrawHitBoxTopBottomEdges() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        TextColor.RGB color = new TextColor.RGB(0, 255, 0);
        
        gui.drawHitBox(5, 10, 4, 3, color);
        
        verify(mockTextGraphics).putString(5, 10, " ");
        verify(mockTextGraphics).putString(6, 10, " ");
        verify(mockTextGraphics).putString(7, 10, " ");
        verify(mockTextGraphics).putString(8, 10, " ");
        verify(mockTextGraphics).putString(5, 12, " ");
        verify(mockTextGraphics).putString(6, 12, " ");
        verify(mockTextGraphics).putString(7, 12, " ");
        verify(mockTextGraphics).putString(8, 12, " ");
    }

    @Test
    public void testDrawHitBoxLeftRightEdges() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        TextColor.RGB color = new TextColor.RGB(0, 0, 255);
        
        gui.drawHitBox(5, 10, 4, 4, color);

        verify(mockTextGraphics).putString(5, 11, " ");
        verify(mockTextGraphics).putString(5, 12, " ");
        verify(mockTextGraphics).putString(8, 11, " ");
        verify(mockTextGraphics).putString(8, 12, " ");
    }

    @Test
    public void testDrawHitBoxMinimalSize() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        TextColor.RGB color = new TextColor.RGB(255, 255, 0);
        
        gui.drawHitBox(0, 0, 1, 1, color);
        verify(mockTextGraphics, times(2)).putString(0, 0, " ");
    }

    @Test
    public void testDrawHitBoxZeroWidth() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        TextColor.RGB color = new TextColor.RGB(128, 128, 128);
        
        gui.drawHitBox(10, 10, 0, 5, color);
        verify(mockTextGraphics, atLeastOnce()).putString(anyInt(), anyInt(), eq(" "));
    }

    @Test
    public void testDrawPixel() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        TextColor.RGB color = new TextColor.RGB(100, 150, 200);
        
        gui.drawPixel(25, 30, color);
        
        verify(mockTextGraphics).setBackgroundColor(color);
        verify(mockTextGraphics).putString(25, 30, " ");
    }

    @Test
    public void testDrawText() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        TextColor.RGB color = new TextColor.RGB(255, 255, 255);
        
        gui.drawText(15, 25, color, "Hello World");
        
        verify(mockTextGraphics).setBackgroundColor(color);
        verify(mockTextGraphics).putString(15, 25, "Hello World");
    }

    @Test
    public void testGetWidth() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        assertEquals(184, gui.getWidth());
    }

    @Test
    public void testGetHeight() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        assertEquals(184, gui.getHeight());
    }

    /**
     * Tests cls clears the screen
     */
    @Test
    public void testCls() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        
        gui.cls();
        
        verify(mockScreen).clear();
    }

    @Test
    public void testFlush() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        
        gui.flush();
        
        verify(mockScreen).refresh();
    }

    @Test
    public void testClose() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        
        gui.close();
        
        verify(mockScreen).close();
    }

    @Test
    public void testFPS() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        
        assertEquals(0, gui.getFPS());
        gui.setFPS(60);
        assertEquals(60, gui.getFPS());
    }

    @Test
    public void testGetGUI() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        assertSame(gui, gui.getGUI());
    }

    @Test
    public void testGetActionNull() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        assertEquals(GUI.ACTION.NULL, gui.getACTION());
    }

    @Test
    public void testGetResolutionScale() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        assertNull(gui.getResolutionScale());
    }

    @Test
    public void testGetKeyAdapter() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        assertNotNull(gui.getKeyAdapter());
    }

    @Test
    public void testKeyPressLeft() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        KeyAdapter adapter = gui.getKeyAdapter();
        
        KeyEvent leftEvent = mock(KeyEvent.class);
        when(leftEvent.getKeyCode()).thenReturn(VK_LEFT);
        
        adapter.keyPressed(leftEvent);
        
        assertEquals(GUI.ACTION.LEFT, gui.getACTION());
    }

    @Test
    public void testKeyPressRight() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        KeyAdapter adapter = gui.getKeyAdapter();
        
        KeyEvent rightEvent = mock(KeyEvent.class);
        when(rightEvent.getKeyCode()).thenReturn(VK_RIGHT);
        
        adapter.keyPressed(rightEvent);
        
        assertEquals(GUI.ACTION.RIGHT, gui.getACTION());
    }

    @Test
    public void testKeyPressNonSpamKey() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        KeyAdapter adapter = gui.getKeyAdapter();
        
        KeyEvent spaceEvent = mock(KeyEvent.class);
        when(spaceEvent.getKeyCode()).thenReturn(VK_SPACE);
        
        adapter.keyPressed(spaceEvent);
        
        assertEquals(GUI.ACTION.JUMP, gui.getACTION());
    }

    @Test
    public void testKeyReleaseSpamKey() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        KeyAdapter adapter = gui.getKeyAdapter();
        
        KeyEvent leftEvent = mock(KeyEvent.class);
        when(leftEvent.getKeyCode()).thenReturn(VK_LEFT);
        
        adapter.keyPressed(leftEvent);
        assertEquals(GUI.ACTION.LEFT, gui.getACTION());
        adapter.keyPressed(leftEvent);
        adapter.keyReleased(leftEvent);
        
        assertEquals(GUI.ACTION.NULL, gui.getACTION());
    }

    @Test
    public void testAllKeyActions() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        KeyAdapter adapter = gui.getKeyAdapter();
        
        KeyEvent upEvent = mock(KeyEvent.class);
        when(upEvent.getKeyCode()).thenReturn(VK_UP);
        adapter.keyPressed(upEvent);
        assertEquals(GUI.ACTION.UP, gui.getACTION());
        
        KeyEvent downEvent = mock(KeyEvent.class);
        when(downEvent.getKeyCode()).thenReturn(VK_DOWN);
        adapter.keyPressed(downEvent);
        assertEquals(GUI.ACTION.DOWN, gui.getACTION());
        
        KeyEvent dashEvent = mock(KeyEvent.class);
        when(dashEvent.getKeyCode()).thenReturn(VK_X);
        adapter.keyPressed(dashEvent);
        assertEquals(GUI.ACTION.DASH, gui.getACTION());
        
        KeyEvent quitEvent = mock(KeyEvent.class);
        when(quitEvent.getKeyCode()).thenReturn(VK_ESCAPE);
        adapter.keyPressed(quitEvent);
        assertEquals(GUI.ACTION.QUIT, gui.getACTION());
        
        KeyEvent killEvent = mock(KeyEvent.class);
        when(killEvent.getKeyCode()).thenReturn(VK_Q);
        adapter.keyPressed(killEvent);
        assertEquals(GUI.ACTION.KILL, gui.getACTION());
        
        KeyEvent selectEvent = mock(KeyEvent.class);
        when(selectEvent.getKeyCode()).thenReturn(VK_ENTER);
        adapter.keyPressed(selectEvent);
        assertEquals(GUI.ACTION.SELECT, gui.getACTION());
    }

    @Test
    public void testUnknownKeyReturnsNull() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        KeyAdapter adapter = gui.getKeyAdapter();
        
        KeyEvent unknownEvent = mock(KeyEvent.class);
        when(unknownEvent.getKeyCode()).thenReturn(VK_A);
        
        adapter.keyPressed(unknownEvent);
        
        assertEquals(GUI.ACTION.NULL, gui.getACTION());
    }
}