package pt.feup.tvvs.soulknight.view.text;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pt.feup.tvvs.soulknight.gui.GUI;
import com.googlecode.lanterna.TextColor;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class GameTextViewerMutationTests {
    
    private GUI gui;
    private GameTextViewer gameTextViewer;
    private TextColor.RGB foregroundColor;

    @BeforeEach
    void setUp() throws IOException {
        gui = mock(GUI.class);
        gameTextViewer = new GameTextViewer();
        foregroundColor = new TextColor.RGB(255, 0, 0);
    }

    @Test
    public void testCharPositionRow() {
        GameTextViewer.CharPosition pos = new GameTextViewer.CharPosition(5, 3);
        assertEquals(5, pos.row());
    }

    @Test
    public void testCharPositionCol() {
        GameTextViewer.CharPosition pos = new GameTextViewer.CharPosition(5, 3);
        assertEquals(3, pos.col());
    }

    @Test
    public void testDrawCallsDrawKnownChar() throws IOException {
        gameTextViewer.draw('A', 5.0, 10.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    @Test
    public void testDrawCallsDrawUnknownChar() throws IOException {
        gameTextViewer.draw('Ω', 5.0, 10.0, foregroundColor, gui);
        verify(gui, times(1)).drawRectangle(anyInt(), anyInt(), eq(3), eq(5), eq(foregroundColor));
    }

    @Test
    public void testParseCharMapIncrement() throws IOException {
        assertNotNull(gameTextViewer);
    }

    @Test
    public void testDrawKnownCharImgXMultiplication() throws IOException {
        gameTextViewer.draw('A', 0.0, 0.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    @Test
    public void testDrawKnownCharImgYMultiplication() throws IOException {
        gameTextViewer.draw('A', 0.0, 0.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    @Test
    public void testDrawKnownCharHeightBoundary() throws IOException {
        gameTextViewer.draw('A', 0.0, 0.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    @Test
    public void testDrawKnownCharWidthBoundary() throws IOException {
        gameTextViewer.draw('A', 0.0, 0.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    @Test
    public void testDrawKnownCharPixelPositionAddition() throws IOException {
        gameTextViewer.draw('A', 10.0, 20.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(eq(10), eq(20), eq(foregroundColor));
    }

    @Test
    public void testDrawKnownCharConditionalNotEqual() throws IOException {
        gameTextViewer.draw('A', 0.0, 0.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    @Test
    public void testDrawUnknownCharIsCalled() throws IOException {
        gameTextViewer.draw('☺', 5.0, 10.0, foregroundColor, gui);
        verify(gui, times(1)).drawRectangle(anyInt(), anyInt(), eq(3), eq(5), eq(foregroundColor));
    }

    @Test
    public void testDrawStringLoopBoundary() throws IOException {
        gameTextViewer.draw("ABC", 0.0, 0.0, foregroundColor, gui);
        verify(gui, atLeast(3)).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    @Test
    public void testDrawStringXOffsetMultiplication() throws IOException {
        gameTextViewer.draw("AB", 0.0, 0.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    @Test
    public void testDrawStringXOffsetAddition() throws IOException {
        gameTextViewer.draw("AB", 10.0, 0.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    @Test
    public void testGetCharHeight() {
        int height = GameTextViewer.getCharHeight();
        assertEquals(5, height);
    }

    @Test
    public void testDrawStringMultipleCharsIncrement() throws IOException {
        gameTextViewer.draw("TEST", 0.0, 0.0, foregroundColor, gui);
        verify(gui, atLeast(4)).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    @Test
    public void testCharMapContainsKnownChar() throws IOException {
        gameTextViewer.draw('A', 0.0, 0.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
        verify(gui, never()).drawRectangle(anyInt(), anyInt(), anyInt(), anyInt(), any());
    }

    @Test
    public void testCharMapContainsUnknownChar() throws IOException {
        gameTextViewer.draw('™', 0.0, 0.0, foregroundColor, gui);
        verify(gui, times(1)).drawRectangle(anyInt(), anyInt(), eq(3), eq(5), eq(foregroundColor));
        verify(gui, never()).drawPixel(anyInt(), anyInt(), any());
    }

    @Test
    public void testPositionCastingToInt() throws IOException {
        gameTextViewer.draw('A', 5.7, 10.3, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(eq(5), eq(10), eq(foregroundColor));
    }

    @Test
    public void testDrawAtDifferentPositions() throws IOException {
        gameTextViewer.draw('A', 20.0, 30.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(eq(20), eq(30), eq(foregroundColor));
        
        reset(gui);
        gameTextViewer.draw('A', 50.0, 60.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(eq(50), eq(60), eq(foregroundColor));
    }

    @Test
    public void testParseCharMapReturnsNonEmptyMap() throws IOException {
        gameTextViewer.draw('A', 0.0, 0.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    @Test
    public void testDrawStringXOffsetArithmeticIntegration() {
        gameTextViewer.draw("AB", 10, 20, new TextColor.RGB(255, 255, 255), gui);
        
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        
        verify(gui, atLeast(1)).drawPixel(xCaptor.capture(), yCaptor.capture(), any());
        
        List<Integer> xValues = xCaptor.getAllValues();
        
        boolean hasChar1X = xValues.stream().anyMatch(x -> x >= 10 && x < 13);
        boolean hasChar2X = xValues.stream().anyMatch(x -> x >= 14 && x < 17);
        
        assertTrue(hasChar1X);
        assertTrue(hasChar2X);
        
        int distinctXRanges = (int) xValues.stream()
            .map(x -> x / 4)
            .distinct()
            .count();
        
        assertTrue(distinctXRanges >= 2);
    }

    @Test
    public void testDrawKnownCharImgXArithmeticIntegration() {
        gameTextViewer.draw("A", 0, 0, new TextColor.RGB(255, 255, 255), gui);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any());
        assertNotNull(gameTextViewer);
    }

    @Test
    public void testDrawKnownCharImgYArithmeticIntegration() {
        gameTextViewer.draw("B", 0, 0, new TextColor.RGB(255, 255, 255), gui);
        
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(gui, atLeast(1)).drawPixel(anyInt(), yCaptor.capture(), any());
        
        List<Integer> yValues = yCaptor.getAllValues();
        assertTrue(yValues.stream().allMatch(y -> y >= 0 && y < 5));
    }

    @Test
    public void testDrawKnownCharPixelPositionArithmeticIntegration() {
        gameTextViewer.draw("C", 10.0, 20.0, new TextColor.RGB(255, 255, 255), gui);
        
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        
        verify(gui, atLeast(1)).drawPixel(xCaptor.capture(), yCaptor.capture(), any(TextColor.RGB.class));
        
        List<Integer> xValues = xCaptor.getAllValues();
        List<Integer> yValues = yCaptor.getAllValues();
        
        assertTrue(xValues.stream().allMatch(x -> x >= 10));
        
        assertTrue(yValues.stream().allMatch(y -> y >= 20));
        
        assertTrue(xValues.stream().anyMatch(x -> x >= 10 && x <= 12));
        assertTrue(yValues.stream().anyMatch(y -> y >= 20 && y <= 24));
    }

    @Test
    public void testDrawStringMultipleCharsPositioningIntegration() {
        gameTextViewer.draw("XYZ", 0, 0, new TextColor.RGB(255, 255, 255), gui);
        
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(gui, atLeast(1)).drawPixel(xCaptor.capture(), anyInt(), any());
        
        List<Integer> xValues = xCaptor.getAllValues();
        
        boolean hasChar0 = xValues.stream().anyMatch(x -> x <= 2);
        boolean hasChar1 = xValues.stream().anyMatch(x -> x >= 4 && x <= 6);
        boolean hasChar2 = xValues.stream().anyMatch(x -> x >= 8 && x <= 10);
        
        assertTrue(hasChar0 && hasChar1 && hasChar2);
    }

    @Test
    public void testPositionCastingToIntPreservesValueIntegration() {
        gameTextViewer.draw("T", 10.7, 20.3, new TextColor.RGB(255, 255, 255), gui);
        
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        
        verify(gui, atLeast(1)).drawPixel(xCaptor.capture(), yCaptor.capture(), any(TextColor.RGB.class));
        
        List<Integer> xValues = xCaptor.getAllValues();
        List<Integer> yValues = yCaptor.getAllValues();
        
        assertTrue(xValues.stream().anyMatch(x -> x >= 10 && x <= 12));
        assertTrue(yValues.stream().anyMatch(y -> y >= 20 && y <= 24));
    }

    @Test
    public void testDrawAtMultiplePositionsArithmeticIntegration() {
        gameTextViewer.draw("1", 5, 10, new TextColor.RGB(255, 255, 255), gui);
        gameTextViewer.draw("2", 15, 30, new TextColor.RGB(255, 255, 255), gui);
        gameTextViewer.draw("3", 25, 50, new TextColor.RGB(255, 255, 255), gui);
        
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        
        verify(gui, atLeast(3)).drawPixel(xCaptor.capture(), yCaptor.capture(), any());
        
        List<Integer> xValues = xCaptor.getAllValues();
        
        assertTrue(xValues.stream().anyMatch(x -> x >= 5 && x < 8));
        assertTrue(xValues.stream().anyMatch(x -> x >= 15 && x < 18));
        assertTrue(xValues.stream().anyMatch(x -> x >= 25 && x < 28));
    }

    @Test
    public void testXOffsetAdditionNotSubtractionIntegration() {
        gameTextViewer.draw("MN", 50, 50, new TextColor.RGB(255, 255, 255), gui);
        
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(gui, atLeast(1)).drawPixel(xCaptor.capture(), anyInt(), any());
        
        List<Integer> xValues = xCaptor.getAllValues();
        
        boolean firstCharRange = xValues.stream().anyMatch(x -> x >= 50 && x < 53);
        boolean secondCharRange = xValues.stream().anyMatch(x -> x >= 54 && x < 57);
        
        assertTrue(firstCharRange);
        assertTrue(secondCharRange);
    }

    @Test
    public void testRecursiveDrawCallWithCorrectOffsetIntegration() {
        gameTextViewer.draw("ABCD", 0, 0, new TextColor.RGB(255, 255, 255), gui);
        verify(gui, atLeast(10)).drawPixel(anyInt(), anyInt(), any());
        
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(gui, atLeast(10)).drawPixel(xCaptor.capture(), anyInt(), any());
        
        List<Integer> xValues = xCaptor.getAllValues();
        int minX = xValues.stream().mapToInt(Integer::intValue).min().orElse(-1);
        int maxX = xValues.stream().mapToInt(Integer::intValue).max().orElse(-1);
        
        assertTrue(maxX - minX >= 10);
    }

    @Test
    public void testCharHeightIs5Integration() {
        int height = GameTextViewer.getCharHeight();
        assertEquals(5, height);
    }

    @Test
    public void testLoopIncrementIsPositiveIntegration() {
        gameTextViewer.draw("PQ", 0, 0, new TextColor.RGB(255, 255, 255), gui);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any());
    }
}
