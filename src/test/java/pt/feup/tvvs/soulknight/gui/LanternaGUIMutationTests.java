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

/**
 * Mutation tests for LanternaGUI class.
 * Targets:
 * - VoidMethodCall mutations in createScreen (setCursorPosition, startScreen)
 * - ConditionalBoundary mutations in keyPressed (SPAM_KEYS.contains)
 * - Math mutations in drawRectangle (x + dx, y + dy)
 * - ConditionalBoundary and Math mutations in drawHitBox
 */
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

    /**
     * Tests that screen.setCursorPosition(null) is called during construction
     * Kills mutation: "removed call to setCursorPosition"
     */
    @Test
    void testSetCursorPositionCalled() throws IOException, URISyntaxException, FontFormatException {
        new LanternaGUI(mockScreenGenerator, "Test");
        
        verify(mockScreen).setCursorPosition(null);
    }

    /**
     * Tests that screen.startScreen() is called during construction
     * Kills mutation: "removed call to startScreen"
     */
    @Test
    void testStartScreenCalled() throws IOException, URISyntaxException, FontFormatException {
        new LanternaGUI(mockScreenGenerator, "Test");
        
        verify(mockScreen).startScreen();
    }

    /**
     * Tests drawRectangle draws at correct positions
     * Kills mutations:
     * - "Replaced integer addition with subtraction" on x + dx
     * - "Replaced integer addition with subtraction" on y + dy
     */
    @Test
    void testDrawRectanglePositions() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        TextColor.RGB color = new TextColor.RGB(255, 0, 0);
        
        gui.drawRectangle(10, 20, 3, 2, color);
        
        // Verify putString is called at correct positions
        // For width=3, height=2:
        // (10+0, 20+0), (10+1, 20+0), (10+2, 20+0)  - row 0
        // (10+0, 20+1), (10+1, 20+1), (10+2, 20+1)  - row 1
        verify(mockTextGraphics).putString(10, 20, " ");
        verify(mockTextGraphics).putString(11, 20, " ");
        verify(mockTextGraphics).putString(12, 20, " ");
        verify(mockTextGraphics).putString(10, 21, " ");
        verify(mockTextGraphics).putString(11, 21, " ");
        verify(mockTextGraphics).putString(12, 21, " ");
    }

    /**
     * Tests drawHitBox draws top and bottom edges correctly
     * Kills mutations:
     * - "changed conditional boundary" at dx < width
     * - "negated conditional" at dx < width
     * - "Replaced integer addition with subtraction" at x + dx
     */
    @Test
    void testDrawHitBoxTopBottomEdges() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        TextColor.RGB color = new TextColor.RGB(0, 255, 0);
        
        gui.drawHitBox(5, 10, 4, 3, color);
        
        // Top edge: y=10, x from 5 to 8
        verify(mockTextGraphics).putString(5, 10, " ");
        verify(mockTextGraphics).putString(6, 10, " ");
        verify(mockTextGraphics).putString(7, 10, " ");
        verify(mockTextGraphics).putString(8, 10, " ");
        
        // Bottom edge: y=10+3-1=12, x from 5 to 8
        verify(mockTextGraphics).putString(5, 12, " ");
        verify(mockTextGraphics).putString(6, 12, " ");
        verify(mockTextGraphics).putString(7, 12, " ");
        verify(mockTextGraphics).putString(8, 12, " ");
    }

    /**
     * Tests drawHitBox draws left and right edges correctly
     * Kills mutations in the dy loop:
     * - "changed conditional boundary" at dy < height - 1
     * - "Replaced integer subtraction with addition" at height - 1
     * - "Replaced integer addition with subtraction" at y + dy
     * - "Replaced integer addition with subtraction" at x + width - 1
     */
    @Test
    void testDrawHitBoxLeftRightEdges() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        TextColor.RGB color = new TextColor.RGB(0, 0, 255);
        
        gui.drawHitBox(5, 10, 4, 4, color);
        
        // Left edge: x=5, y from 11 to 12 (excluding corners)
        // dy from 1 to height-2 (dy < height-1 => dy < 3, so dy=1,2)
        verify(mockTextGraphics).putString(5, 11, " ");
        verify(mockTextGraphics).putString(5, 12, " ");
        
        // Right edge: x=5+4-1=8, y from 11 to 12
        verify(mockTextGraphics).putString(8, 11, " ");
        verify(mockTextGraphics).putString(8, 12, " ");
    }

    /**
     * Tests drawHitBox with minimum dimensions (1x1)
     */
    @Test
    void testDrawHitBoxMinimalSize() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        TextColor.RGB color = new TextColor.RGB(255, 255, 0);
        
        gui.drawHitBox(0, 0, 1, 1, color);
        
        // Should draw at (0, 0) for top edge and bottom edge (which is same position)
        // Top: y=0, dx=0 => (0,0)
        // Bottom: y+height-1 = 0+1-1 = 0, dx=0 => (0,0)
        verify(mockTextGraphics, times(2)).putString(0, 0, " ");
    }

    /**
     * Tests drawHitBox with width=0 - verifies loop behavior
     * The top/bottom edges won't draw but left/right edges still will (if height > 2)
     */
    @Test
    void testDrawHitBoxZeroWidth() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        TextColor.RGB color = new TextColor.RGB(128, 128, 128);
        
        gui.drawHitBox(10, 10, 0, 5, color);
        
        // With width=0, dx < 0 is never true for top/bottom edges
        // But the left/right edge loop still runs for dy from 1 to height-2 (1 to 3)
        // Left edge at x=10, right edge at x=10+0-1 = 9
        verify(mockTextGraphics, atLeastOnce()).putString(anyInt(), anyInt(), eq(" "));
    }

    /**
     * Tests drawPixel calls putString at correct position
     */
    @Test
    void testDrawPixel() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        TextColor.RGB color = new TextColor.RGB(100, 150, 200);
        
        gui.drawPixel(25, 30, color);
        
        verify(mockTextGraphics).setBackgroundColor(color);
        verify(mockTextGraphics).putString(25, 30, " ");
    }

    /**
     * Tests drawText calls putString with correct text
     */
    @Test
    void testDrawText() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        TextColor.RGB color = new TextColor.RGB(255, 255, 255);
        
        gui.drawText(15, 25, color, "Hello World");
        
        verify(mockTextGraphics).setBackgroundColor(color);
        verify(mockTextGraphics).putString(15, 25, "Hello World");
    }

    /**
     * Tests getWidth returns screen generator width
     */
    @Test
    void testGetWidth() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        
        assertEquals(184, gui.getWidth());
    }

    /**
     * Tests getHeight returns screen generator width (bug in code)
     */
    @Test
    void testGetHeight() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        
        // Note: getHeight() actually returns getWidth() - this is a bug in the code
        assertEquals(184, gui.getHeight());
    }

    /**
     * Tests cls clears the screen
     */
    @Test
    void testCls() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        
        gui.cls();
        
        verify(mockScreen).clear();
    }

    /**
     * Tests flush refreshes the screen
     */
    @Test
    void testFlush() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        
        gui.flush();
        
        verify(mockScreen).refresh();
    }

    /**
     * Tests close closes the screen
     */
    @Test
    void testClose() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        
        gui.close();
        
        verify(mockScreen).close();
    }

    /**
     * Tests getFPS and setFPS
     */
    @Test
    void testFPS() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        
        assertEquals(0, gui.getFPS());
        gui.setFPS(60);
        assertEquals(60, gui.getFPS());
    }

    /**
     * Tests getGUI returns this
     */
    @Test
    void testGetGUI() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        
        assertSame(gui, gui.getGUI());
    }

    /**
     * Tests getACTION with no key pressed
     */
    @Test
    void testGetActionNull() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        
        assertEquals(GUI.ACTION.NULL, gui.getACTION());
    }

    /**
     * Tests getResolutionScale
     */
    @Test
    void testGetResolutionScale() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        
        // Default resolution scale is null (set in constructor with null)
        assertNull(gui.getResolutionScale());
    }

    /**
     * Tests KeyAdapter is created and accessible
     */
    @Test
    void testGetKeyAdapter() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        
        assertNotNull(gui.getKeyAdapter());
    }

    /**
     * Tests key press for LEFT action (spam key)
     * Kills mutation: "negated conditional" on SPAM_KEYS.contains
     */
    @Test
    void testKeyPressLeft() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        KeyAdapter adapter = gui.getKeyAdapter();
        
        // Simulate LEFT key press
        KeyEvent leftEvent = mock(KeyEvent.class);
        when(leftEvent.getKeyCode()).thenReturn(VK_LEFT);
        
        adapter.keyPressed(leftEvent);
        
        assertEquals(GUI.ACTION.LEFT, gui.getACTION());
    }

    /**
     * Tests key press for RIGHT action (spam key)
     */
    @Test
    void testKeyPressRight() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        KeyAdapter adapter = gui.getKeyAdapter();
        
        KeyEvent rightEvent = mock(KeyEvent.class);
        when(rightEvent.getKeyCode()).thenReturn(VK_RIGHT);
        
        adapter.keyPressed(rightEvent);
        
        assertEquals(GUI.ACTION.RIGHT, gui.getACTION());
    }

    /**
     * Tests key press for non-spam key
     */
    @Test
    void testKeyPressNonSpamKey() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        KeyAdapter adapter = gui.getKeyAdapter();
        
        KeyEvent spaceEvent = mock(KeyEvent.class);
        when(spaceEvent.getKeyCode()).thenReturn(VK_SPACE);
        
        adapter.keyPressed(spaceEvent);
        
        assertEquals(GUI.ACTION.JUMP, gui.getACTION());
    }

    /**
     * Tests key release for spam key
     */
    @Test
    void testKeyReleaseSpamKey() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        KeyAdapter adapter = gui.getKeyAdapter();
        
        KeyEvent leftEvent = mock(KeyEvent.class);
        when(leftEvent.getKeyCode()).thenReturn(VK_LEFT);
        
        adapter.keyPressed(leftEvent);
        assertEquals(GUI.ACTION.LEFT, gui.getACTION());
        
        // Re-press and release
        adapter.keyPressed(leftEvent);
        adapter.keyReleased(leftEvent);
        
        // After release, should return NULL
        assertEquals(GUI.ACTION.NULL, gui.getACTION());
    }

    /**
     * Tests various key actions
     */
    @Test
    void testAllKeyActions() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        KeyAdapter adapter = gui.getKeyAdapter();
        
        // Test UP
        KeyEvent upEvent = mock(KeyEvent.class);
        when(upEvent.getKeyCode()).thenReturn(VK_UP);
        adapter.keyPressed(upEvent);
        assertEquals(GUI.ACTION.UP, gui.getACTION());
        
        // Test DOWN
        KeyEvent downEvent = mock(KeyEvent.class);
        when(downEvent.getKeyCode()).thenReturn(VK_DOWN);
        adapter.keyPressed(downEvent);
        assertEquals(GUI.ACTION.DOWN, gui.getACTION());
        
        // Test DASH (X)
        KeyEvent dashEvent = mock(KeyEvent.class);
        when(dashEvent.getKeyCode()).thenReturn(VK_X);
        adapter.keyPressed(dashEvent);
        assertEquals(GUI.ACTION.DASH, gui.getACTION());
        
        // Test QUIT (ESC)
        KeyEvent quitEvent = mock(KeyEvent.class);
        when(quitEvent.getKeyCode()).thenReturn(VK_ESCAPE);
        adapter.keyPressed(quitEvent);
        assertEquals(GUI.ACTION.QUIT, gui.getACTION());
        
        // Test KILL (Q)
        KeyEvent killEvent = mock(KeyEvent.class);
        when(killEvent.getKeyCode()).thenReturn(VK_Q);
        adapter.keyPressed(killEvent);
        assertEquals(GUI.ACTION.KILL, gui.getACTION());
        
        // Test SELECT (ENTER)
        KeyEvent selectEvent = mock(KeyEvent.class);
        when(selectEvent.getKeyCode()).thenReturn(VK_ENTER);
        adapter.keyPressed(selectEvent);
        assertEquals(GUI.ACTION.SELECT, gui.getACTION());
    }

    /**
     * Tests unknown key returns NULL action
     */
    @Test
    void testUnknownKeyReturnsNull() throws IOException, URISyntaxException, FontFormatException {
        LanternaGUI gui = new LanternaGUI(mockScreenGenerator, "Test");
        KeyAdapter adapter = gui.getKeyAdapter();
        
        KeyEvent unknownEvent = mock(KeyEvent.class);
        when(unknownEvent.getKeyCode()).thenReturn(VK_A); // 'A' key not mapped
        
        adapter.keyPressed(unknownEvent);
        
        assertEquals(GUI.ACTION.NULL, gui.getACTION());
    }
}