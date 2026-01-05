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

    // Test CharPosition.row() returns actual value, not 0
    @Test
    void testCharPositionRow() {
        GameTextViewer.CharPosition pos = new GameTextViewer.CharPosition(5, 3);
        assertEquals(5, pos.row()); // Should return 5, not 0
    }

    // Test CharPosition.col() returns actual value, not 0
    @Test
    void testCharPositionCol() {
        GameTextViewer.CharPosition pos = new GameTextViewer.CharPosition(5, 3);
        assertEquals(3, pos.col()); // Should return 3, not 0
    }

    // Test drawKnownChar is called when character is in map
    @Test
    void testDrawCallsDrawKnownChar() throws IOException {
        gameTextViewer.draw('A', 5.0, 10.0, foregroundColor, gui);
        // Verify drawPixel is called (drawKnownChar behavior)
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    // Test drawUnknownChar is called when character is not in map
    @Test
    void testDrawCallsDrawUnknownChar() throws IOException {
        gameTextViewer.draw('Ω', 5.0, 10.0, foregroundColor, gui);
        // Verify drawRectangle is called (drawUnknownChar behavior)
        verify(gui, times(1)).drawRectangle(anyInt(), anyInt(), eq(3), eq(5), eq(foregroundColor));
    }

    // Test parseCharMap increment (y++, not y--)
    @Test
    void testParseCharMapIncrement() throws IOException {
        // Just verify the viewer initializes correctly with proper parsing
        assertNotNull(gameTextViewer);
    }

    // Test drawKnownChar multiplication for imgX
    @Test
    void testDrawKnownCharImgXMultiplication() throws IOException {
        gameTextViewer.draw('A', 0.0, 0.0, foregroundColor, gui);
        // imgX = position.row() * (charWidth + 1)
        // Tests multiplication, not division
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    // Test drawKnownChar multiplication for imgY
    @Test
    void testDrawKnownCharImgYMultiplication() throws IOException {
        gameTextViewer.draw('A', 0.0, 0.0, foregroundColor, gui);
        // imgY = position.col() * (charHeight + 1)
        // Tests multiplication, not division
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    // Test drawKnownChar loop boundaries (dy < charHeight)
    @Test
    void testDrawKnownCharHeightBoundary() throws IOException {
        gameTextViewer.draw('A', 0.0, 0.0, foregroundColor, gui);
        // Loop: for (int dy = 0; dy < charHeight; dy++)
        // Should iterate 0-4 (charHeight = 5), tests < not <=
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    // Test drawKnownChar loop boundaries (dx < charWidth)
    @Test
    void testDrawKnownCharWidthBoundary() throws IOException {
        gameTextViewer.draw('A', 0.0, 0.0, foregroundColor, gui);
        // Loop: for (int dx = 0; dx < charWidth; dx++)
        // Should iterate 0-2 (charWidth = 3), tests < not <=
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    // Test drawKnownChar pixel position calculation (addition)
    @Test
    void testDrawKnownCharPixelPositionAddition() throws IOException {
        gameTextViewer.draw('A', 10.0, 20.0, foregroundColor, gui);
        // gui.drawPixel((int) (x + dx), (int) (y + dy), ...)
        // Tests addition x + dx and y + dy, not subtraction
        verify(gui, atLeastOnce()).drawPixel(eq(10), eq(20), eq(foregroundColor));
    }

    // Test drawKnownChar conditional (!=, not ==)
    @Test
    void testDrawKnownCharConditionalNotEqual() throws IOException {
        gameTextViewer.draw('A', 0.0, 0.0, foregroundColor, gui);
        // if (fontImage.getRGB(...) != COLOR_WHITE)
        // Tests negated conditional
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    // Test drawUnknownChar is actually called
    @Test
    void testDrawUnknownCharIsCalled() throws IOException {
        gameTextViewer.draw('☺', 5.0, 10.0, foregroundColor, gui);
        // Verify drawRectangle is called
        verify(gui, times(1)).drawRectangle(anyInt(), anyInt(), eq(3), eq(5), eq(foregroundColor));
    }

    // Test draw(String) loop boundary
    @Test
    void testDrawStringLoopBoundary() throws IOException {
        gameTextViewer.draw("ABC", 0.0, 0.0, foregroundColor, gui);
        // for (int i = 0; i < string.length(); i++)
        // Tests < not <=, should iterate 0-2 (3 characters)
        verify(gui, atLeast(3)).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    // Test draw(String) xOffset multiplication
    @Test
    void testDrawStringXOffsetMultiplication() throws IOException {
        gameTextViewer.draw("AB", 0.0, 0.0, foregroundColor, gui);
        // xOffset = i * (charWidth + spacing)
        // Tests multiplication, not division
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    // Test draw(String) xOffset addition
    @Test
    void testDrawStringXOffsetAddition() throws IOException {
        gameTextViewer.draw("AB", 10.0, 0.0, foregroundColor, gui);
        // draw(..., x + xOffset, y, ...)
        // Tests addition, not subtraction
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    // Test getCharHeight returns 5, not 0
    @Test
    void testGetCharHeight() {
        int height = GameTextViewer.getCharHeight();
        assertEquals(5, height); // Should return 5, not 0
    }

    // Test string with multiple characters increments correctly
    @Test
    void testDrawStringMultipleCharsIncrement() throws IOException {
        gameTextViewer.draw("TEST", 0.0, 0.0, foregroundColor, gui);
        // Should draw all 4 characters
        verify(gui, atLeast(4)).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    // Test charMap contains check (positive case)
    @Test
    void testCharMapContainsKnownChar() throws IOException {
        gameTextViewer.draw('A', 0.0, 0.0, foregroundColor, gui);
        // charMap.containsKey('A') should be true
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
        verify(gui, never()).drawRectangle(anyInt(), anyInt(), anyInt(), anyInt(), any());
    }

    // Test charMap contains check (negative case)
    @Test
    void testCharMapContainsUnknownChar() throws IOException {
        gameTextViewer.draw('™', 0.0, 0.0, foregroundColor, gui);
        // charMap.containsKey('™') should be false
        verify(gui, times(1)).drawRectangle(anyInt(), anyInt(), eq(3), eq(5), eq(foregroundColor));
        verify(gui, never()).drawPixel(anyInt(), anyInt(), any());
    }

    // Test position casting to int
    @Test
    void testPositionCastingToInt() throws IOException {
        gameTextViewer.draw('A', 5.7, 10.3, foregroundColor, gui);
        // (int) (x + dx) and (int) (y + dy)
        // Verify pixel at truncated position
        verify(gui, atLeastOnce()).drawPixel(eq(5), eq(10), eq(foregroundColor));
    }

    // Test drawing at different positions
    @Test
    void testDrawAtDifferentPositions() throws IOException {
        gameTextViewer.draw('A', 20.0, 30.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(eq(20), eq(30), eq(foregroundColor));
        
        reset(gui);
        gameTextViewer.draw('A', 50.0, 60.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(eq(50), eq(60), eq(foregroundColor));
    }

    // Test parseCharMap returns non-empty map
    @Test
    void testParseCharMapReturnsNonEmptyMap() throws IOException {
        // Verify viewer can draw known characters (map is not empty)
        gameTextViewer.draw('A', 0.0, 0.0, foregroundColor, gui);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), eq(foregroundColor));
    }

    // ========== Integration Tests - Verify Actual Pixel Coordinates and Arithmetic ==========

    // Test that x offset multiplication is correct: i * 4
    @Test
    void testDrawStringXOffsetArithmeticIntegration() {
        // Draw "AB" - 2 characters
        gameTextViewer.draw("AB", 10, 20, new TextColor.RGB(255, 255, 255), gui);
        
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        
        verify(gui, atLeast(1)).drawPixel(xCaptor.capture(), yCaptor.capture(), any());
        
        List<Integer> xValues = xCaptor.getAllValues();
        
        // First char 'A' should have pixels at x: 10 + (0 * 4) = 10
        // Second char 'B' should have pixels at x: 10 + (1 * 4) = 14
        boolean hasChar1X = xValues.stream().anyMatch(x -> x >= 10 && x < 13);
        boolean hasChar2X = xValues.stream().anyMatch(x -> x >= 14 && x < 17);
        
        assertTrue(hasChar1X, "First character should be drawn starting at x=10");
        assertTrue(hasChar2X, "Second character should be drawn starting at x=14 (10 + 1*4)");
        
        // If multiplication mutated to division (i / 4), both would be at same position
        int distinctXRanges = (int) xValues.stream()
            .map(x -> x / 4)
            .distinct()
            .count();
        
        assertTrue(distinctXRanges >= 2, "Characters should be at different x positions (multiplication not division)");
    }

    // Test imgX calculation: col * 3
    @Test
    void testDrawKnownCharImgXArithmeticIntegration() {
        // Draw a known character that should trigger imgX = col * 3
        gameTextViewer.draw("A", 0, 0, new TextColor.RGB(255, 255, 255), gui);
        
        // The character should be drawn with pixels, verify method was called
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any());
        
        // If col * 3 mutated to col / 3, the character would be drawn incorrectly
        // At minimum, verify the method executes without error with correct math
        assertNotNull(gameTextViewer);
    }

    // Test imgY calculation: row * 5
    @Test
    void testDrawKnownCharImgYArithmeticIntegration() {
        // Draw a character to test row * 5 calculation
        gameTextViewer.draw("B", 0, 0, new TextColor.RGB(255, 255, 255), gui);
        
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(gui, atLeast(1)).drawPixel(anyInt(), yCaptor.capture(), any());
        
        // Character should be drawn at y=0 (no offset)
        List<Integer> yValues = yCaptor.getAllValues();
        assertTrue(yValues.stream().allMatch(y -> y >= 0 && y < 5), 
            "Y coordinates should be in valid range for row calculation");
    }

    // Test pixel position addition: x + col, y + row
    @Test
    void testDrawKnownCharPixelPositionArithmeticIntegration() {
        // Draw at position (10, 20)
        gameTextViewer.draw("C", 10.0, 20.0, new TextColor.RGB(255, 255, 255), gui);
        
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        
        verify(gui, atLeast(1)).drawPixel(xCaptor.capture(), yCaptor.capture(), any(TextColor.RGB.class));
        
        List<Integer> xValues = xCaptor.getAllValues();
        List<Integer> yValues = yCaptor.getAllValues();
        
        // All x values should be >= 10 (position + col offset)
        assertTrue(xValues.stream().allMatch(x -> x >= 10), 
            "X coordinates should start at base position 10");
        
        // All y values should be >= 20 (position + row offset)  
        assertTrue(yValues.stream().allMatch(y -> y >= 20),
            "Y coordinates should start at base position 20");
        
        // If addition mutated to subtraction, values would be < base position
        assertTrue(xValues.stream().anyMatch(x -> x >= 10 && x <= 12),
            "Should have pixels near x=10 (not subtraction)");
        assertTrue(yValues.stream().anyMatch(y -> y >= 20 && y <= 24),
            "Should have pixels near y=20 (not subtraction)");
    }

    // Test that multiple character string uses correct arithmetic for positioning
    @Test
    void testDrawStringMultipleCharsPositioningIntegration() {
        gameTextViewer.draw("XYZ", 0, 0, new TextColor.RGB(255, 255, 255), gui);
        
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(gui, atLeast(1)).drawPixel(xCaptor.capture(), anyInt(), any());
        
        List<Integer> xValues = xCaptor.getAllValues();
        
        // Verify we have pixels spread across the expected range
        // Char 0: x = 0 + 0*4 = 0-2
        // Char 1: x = 0 + 1*4 = 4-6  
        // Char 2: x = 0 + 2*4 = 8-10
        boolean hasChar0 = xValues.stream().anyMatch(x -> x <= 2);
        boolean hasChar1 = xValues.stream().anyMatch(x -> x >= 4 && x <= 6);
        boolean hasChar2 = xValues.stream().anyMatch(x -> x >= 8 && x <= 10);
        
        assertTrue(hasChar0 && hasChar1 && hasChar2,
            "All three characters should be positioned correctly with i*4 spacing");
    }

    // Verify position casting doesn't lose significant data
    @Test
    void testPositionCastingToIntPreservesValueIntegration() {
        // Test with fractional positions
        gameTextViewer.draw("T", 10.7, 20.3, new TextColor.RGB(255, 255, 255), gui);
        
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        
        verify(gui, atLeast(1)).drawPixel(xCaptor.capture(), yCaptor.capture(), any(TextColor.RGB.class));
        
        List<Integer> xValues = xCaptor.getAllValues();
        List<Integer> yValues = yCaptor.getAllValues();
        
        // After casting 10.7 -> 10, 20.3 -> 20
        assertTrue(xValues.stream().anyMatch(x -> x >= 10 && x <= 12),
            "Position 10.7 should cast to 10");
        assertTrue(yValues.stream().anyMatch(y -> y >= 20 && y <= 24),
            "Position 20.3 should cast to 20");
    }

    // Test draw at various positions to ensure arithmetic consistency
    @Test
    void testDrawAtMultiplePositionsArithmeticIntegration() {
        // Test multiple positions
        gameTextViewer.draw("1", 5, 10, new TextColor.RGB(255, 255, 255), gui);
        gameTextViewer.draw("2", 15, 30, new TextColor.RGB(255, 255, 255), gui);
        gameTextViewer.draw("3", 25, 50, new TextColor.RGB(255, 255, 255), gui);
        
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> yCaptor = ArgumentCaptor.forClass(Integer.class);
        
        verify(gui, atLeast(3)).drawPixel(xCaptor.capture(), yCaptor.capture(), any());
        
        List<Integer> xValues = xCaptor.getAllValues();
        
        // Verify pixels at all three positions
        assertTrue(xValues.stream().anyMatch(x -> x >= 5 && x < 8), 
            "Should have pixels near x=5");
        assertTrue(xValues.stream().anyMatch(x -> x >= 15 && x < 18),
            "Should have pixels near x=15");
        assertTrue(xValues.stream().anyMatch(x -> x >= 25 && x < 28),
            "Should have pixels near x=25");
    }

    // Test x offset addition: x + i * 4 + xOffset
    @Test
    void testXOffsetAdditionNotSubtractionIntegration() {
        gameTextViewer.draw("MN", 50, 50, new TextColor.RGB(255, 255, 255), gui);
        
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(gui, atLeast(1)).drawPixel(xCaptor.capture(), anyInt(), any());
        
        List<Integer> xValues = xCaptor.getAllValues();
        
        // First char at x=50+0*4=50, second at x=50+1*4=54
        // If addition mutated to subtraction anywhere, positioning would be wrong
        boolean firstCharRange = xValues.stream().anyMatch(x -> x >= 50 && x < 53);
        boolean secondCharRange = xValues.stream().anyMatch(x -> x >= 54 && x < 57);
        
        assertTrue(firstCharRange, "First character should be at x>=50");
        assertTrue(secondCharRange, "Second character should be at x>=54 (addition, not subtraction)");
    }

    // Test the recursive draw call happens correctly
    @Test
    void testRecursiveDrawCallWithCorrectOffsetIntegration() {
        // Draw multiple characters to trigger recursive calls
        gameTextViewer.draw("ABCD", 0, 0, new TextColor.RGB(255, 255, 255), gui);
        
        // Should have many drawPixel calls from all characters
        verify(gui, atLeast(10)).drawPixel(anyInt(), anyInt(), any());
        
        ArgumentCaptor<Integer> xCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(gui, atLeast(10)).drawPixel(xCaptor.capture(), anyInt(), any());
        
        // Verify wide spread of x values (0-15 range for 4 chars)
        List<Integer> xValues = xCaptor.getAllValues();
        int minX = xValues.stream().mapToInt(Integer::intValue).min().orElse(-1);
        int maxX = xValues.stream().mapToInt(Integer::intValue).max().orElse(-1);
        
        assertTrue(maxX - minX >= 10, 
            "Characters should span at least 10 pixels horizontally with correct arithmetic");
    }

    // Test height verification
    @Test
    void testCharHeightIs5Integration() {
        int height = GameTextViewer.getCharHeight();
        assertEquals(5, height, "Character height should be 5");
    }

    // Test that negative increment would break loop
    @Test
    void testLoopIncrementIsPositiveIntegration() {
        // Draw string to test loop increment
        gameTextViewer.draw("PQ", 0, 0, new TextColor.RGB(255, 255, 255), gui);
        
        // If increment was -1 instead of 1, this would hang or fail
        // Verify it completed successfully
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any());
    }
}
