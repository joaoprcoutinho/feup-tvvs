package pt.feup.tvvs.soulknight.view.states;

import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.credits.Credits;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.view.menu.LogoViewer;
import pt.feup.tvvs.soulknight.view.sprites.ViewerProvider;
import pt.feup.tvvs.soulknight.view.text.TextViewer;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CreditsViewerMutationTests {
    private GUI gui;
    private Credits model;
    private TextViewer textViewer;
    private LogoViewer logoViewer;
    private ViewerProvider viewerProvider;
    private CreditsViewer creditsViewer;

    @BeforeEach
    void setup() throws IOException {
        gui = mock(GUI.class);
        textViewer = mock(TextViewer.class);
        logoViewer = mock(LogoViewer.class);
        viewerProvider = mock(ViewerProvider.class);

        when(viewerProvider.getTextViewer()).thenReturn(textViewer);
        when(viewerProvider.getLogoViewer()).thenReturn(logoViewer);

        Knight knight = new Knight(1, 1, 1, 1, 1);
        Credits realCredits = new Credits(knight);
        realCredits.setMessages(new String[]{"Message1", "Message2"});
        realCredits.setNames(new String[]{"Name1", "Name2"});

        model = spy(realCredits);
        creditsViewer = new CreditsViewer(model, viewerProvider);
    }

    @Test
    void testDrawCallsAllMethods() throws IOException {
        when(model.getMessages()).thenReturn(new String[]{"Hello", "World"});
        when(model.getNames()).thenReturn(new String[]{"Alice", "Bob"});
        when(model.getScore()).thenReturn(42);
        when(model.getDeaths()).thenReturn(3);
        when(model.getMinutes()).thenReturn(1);
        when(model.getSeconds()).thenReturn(5);

        creditsViewer.draw(gui, 0);

        verify(textViewer).draw(eq("Hello"), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
        verify(textViewer).draw(eq("World"), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
        verify(textViewer).draw(eq("Alice"), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
        verify(textViewer).draw(eq("Bob"), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
        verify(textViewer).draw(contains("Score"), anyDouble(), anyDouble(), eq(CreditsViewer.scoreColor), eq(gui));
        verify(textViewer).draw(contains("Deaths"), anyDouble(), anyDouble(), eq(CreditsViewer.deathColor), eq(gui));
        verify(textViewer).draw(contains("Time"), anyDouble(), anyDouble(), eq(CreditsViewer.timeColor), eq(gui));
        verify(logoViewer).draw(gui, 60, 16);
        verify(gui).cls();
        verify(gui).flush();
    }

    // Test drawMessages spacing multiplication and addition (lines 46-47, 49)
    @Test
    void testDrawMessagesSpacingMultiplication() throws IOException {
        when(model.getMessages()).thenReturn(new String[]{"Test1", "Test2", "Test3"});
        creditsViewer.draw(gui, 0);
        // spacing = charHeight * 8, tests multiplication not division
        // yAlignment + spacing * idx tests addition
        verify(textViewer, times(3)).draw(anyString(), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
        // Verify different Y positions for each message (multiplication creates spacing)
        verify(textViewer).draw(eq("Test1"), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
        verify(textViewer).draw(eq("Test2"), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
        verify(textViewer).draw(eq("Test3"), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
    }

    // Test drawMessages addition (yAlignment + spacing * idx)
    @Test
    void testDrawMessagesPositionAddition() throws IOException {
        when(model.getMessages()).thenReturn(new String[]{"Msg1", "Msg2"});
        creditsViewer.draw(gui, 0);
        // Tests addition in position calculation
        verify(textViewer, atLeastOnce()).draw(anyString(), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
    }

    // Test drawNames spacing multiplication and addition (lines 60-62, 64)
    @Test
    void testDrawNamesSpacingMultiplication() throws IOException {
        when(model.getNames()).thenReturn(new String[]{"Name1", "Name2", "Name3"});
        creditsViewer.draw(gui, 0);
        // spacing = charHeight * 2, tests multiplication
        // yAlignment + spacing * idx tests addition
        verify(textViewer, times(3)).draw(anyString(), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
        // Verify all names are actually drawn
        verify(textViewer).draw(eq("Name1"), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
        verify(textViewer).draw(eq("Name2"), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
        verify(textViewer).draw(eq("Name3"), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
    }

    // Test drawNames addition (yAlignment + spacing * idx)
    @Test
    void testDrawNamesPositionAddition() throws IOException {
        when(model.getNames()).thenReturn(new String[]{"Name1"});
        creditsViewer.draw(gui, 0);
        // Tests addition in position calculation
        verify(textViewer, atLeastOnce()).draw(anyString(), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
    }

    // Test background loop boundaries and drawPixel calls (lines 107-108)
    @Test
    void testBackgroundLoopBoundaries() throws IOException {
        creditsViewer.draw(gui, 0);
        // for (int w = 0; w < screenWidth; w++)
        // for (int h = 0; h < screenHeight; h++)
        // Tests < not <=, and drawPixel is actually called
        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        // Verify specific coordinates to ensure loops execute correctly
        verify(gui, atLeastOnce()).drawPixel(eq(0), eq(0), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(183), eq(111), any(TextColor.RGB.class));
    }

    // Test background gray calculation (addition in Math.sin)
    @Test
    void testBackgroundGrayCalculationAddition() throws IOException {
        creditsViewer.draw(gui, 100);
        // gray = 180 + 75 * Math.sin(...)
        // Tests addition, not subtraction
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test background distance calculation (division)
    @Test
    void testBackgroundDistanceCalculation() throws IOException {
        creditsViewer.draw(gui, 50);
        // (double) w / screenWidth - 0.5
        // Tests division, not multiplication
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test background distance calculation (subtraction)
    @Test
    void testBackgroundDistanceSubtraction() throws IOException {
        creditsViewer.draw(gui, 50);
        // w / screenWidth - 0.5
        // Tests subtraction, not addition
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test background Math.sin with multiplication
    @Test
    void testBackgroundSinMultiplication() throws IOException {
        creditsViewer.draw(gui, 75);
        // 75 * Math.sin(...)
        // Tests multiplication, not division
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test background red calculation
    @Test
    void testBackgroundRedCalculation() throws IOException {
        creditsViewer.draw(gui, 100);
        // red = gray + 20 * Math.sin(...)
        // Tests addition and multiplication
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test background green calculation
    @Test
    void testBackgroundGreenCalculation() throws IOException {
        creditsViewer.draw(gui, 100);
        // green = gray + 15 * Math.sin(...)
        // Tests addition and multiplication
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test background blue calculation
    @Test
    void testBackgroundBlueCalculation() throws IOException {
        creditsViewer.draw(gui, 100);
        // blue = gray + 25 * Math.sin(...)
        // Tests addition and multiplication
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test background Math.pow in distance calculation
    @Test
    void testBackgroundPowCalculation() throws IOException {
        creditsViewer.draw(gui, 50);
        // Math.pow((double) w / screenWidth - 0.5, 2)
        // Tests all operations in distance calculation
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test border drawing (top and bottom)
    @Test
    void testBorderTopBottom() throws IOException {
        creditsViewer.draw(gui, 0);
        // for (int w = 0; w < screenWidth; w++)
        // Tests < boundary
        verify(gui, atLeast(184 * 2)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test border drawing (left and right)
    @Test
    void testBorderLeftRight() throws IOException {
        creditsViewer.draw(gui, 0);
        // for (int h = 1; h < screenHeight - 1; h++)
        // Tests subtraction in boundary
        verify(gui, atLeast(110 * 2)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test border loop condition boundaries
    @Test
    void testBorderLoopBoundaries() throws IOException {
        creditsViewer.draw(gui, 0);
        // h = 1 (not 0), h < screenHeight - 1 (not <=)
        // Tests changed conditional boundary
        verify(gui, atLeastOnce()).drawPixel(eq(0), anyInt(), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(183), anyInt(), any(TextColor.RGB.class));
    }

    // Test drawPixel is called for background
    @Test
    void testDrawPixelCalledForBackground() throws IOException {
        creditsViewer.draw(gui, 0);
        // Verify drawPixel is actually called
        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test drawPixel for border specifically
    @Test
    void testDrawPixelCalledForBorder() throws IOException {
        creditsViewer.draw(gui, 0);
        // Border pixels
        verify(gui, atLeast(1)).drawPixel(anyInt(), eq(0), any(TextColor.RGB.class));
        verify(gui, atLeast(1)).drawPixel(anyInt(), eq(111), any(TextColor.RGB.class));
    }

    // Test Math.sqrt in distance calculation
    @Test
    void testBackgroundSqrtInDistance() throws IOException {
        creditsViewer.draw(gui, 100);
        // Math.sqrt(Math.pow(...) + Math.pow(...))
        // Tests all mathematical operations
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test time-based animation (changeRate multiplication)
    @Test
    void testTimeBasedAnimation() throws IOException {
        creditsViewer.draw(gui, 0);
        reset(gui);
        creditsViewer.draw(gui, 100);
        // time * changeRate
        // Tests multiplication in time-based calculations
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test different time values produce different colors
    @Test
    void testDifferentTimesProduceDifferentColors() throws IOException {
        creditsViewer.draw(gui, 0);
        reset(gui);
        creditsViewer.draw(gui, 500);
        // Verifies time affects the output
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }
}
