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
    public void testDrawCallsAllMethods() throws IOException {
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

    @Test
    public void testDrawMessagesSpacingMultiplication() throws IOException {
        when(model.getMessages()).thenReturn(new String[]{"Test1", "Test2", "Test3"});
        creditsViewer.draw(gui, 0);
        verify(textViewer, times(3)).draw(anyString(), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
        verify(textViewer).draw(eq("Test1"), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
        verify(textViewer).draw(eq("Test2"), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
        verify(textViewer).draw(eq("Test3"), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
    }

    @Test
    public void testDrawMessagesPositionAddition() throws IOException {
        when(model.getMessages()).thenReturn(new String[]{"Msg1", "Msg2"});
        creditsViewer.draw(gui, 0);
        verify(textViewer, atLeastOnce()).draw(anyString(), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
    }

    @Test
    public void testDrawNamesSpacingMultiplication() throws IOException {
        when(model.getNames()).thenReturn(new String[]{"Name1", "Name2", "Name3"});
        creditsViewer.draw(gui, 0);
        verify(textViewer, times(3)).draw(anyString(), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
        verify(textViewer).draw(eq("Name1"), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
        verify(textViewer).draw(eq("Name2"), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
        verify(textViewer).draw(eq("Name3"), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
    }

    @Test
    public void testDrawNamesPositionAddition() throws IOException {
        when(model.getNames()).thenReturn(new String[]{"Name1"});
        creditsViewer.draw(gui, 0);
        verify(textViewer, atLeastOnce()).draw(anyString(), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
    }

    @Test
    public void testBackgroundLoopBoundaries() throws IOException {
        creditsViewer.draw(gui, 0);
        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(0), eq(0), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(183), eq(111), any(TextColor.RGB.class));
    }

    @Test
    public void testBackgroundGrayCalculationAddition() throws IOException {
        creditsViewer.draw(gui, 100);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testBackgroundDistanceCalculation() throws IOException {
        creditsViewer.draw(gui, 50);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testBackgroundDistanceSubtraction() throws IOException {
        creditsViewer.draw(gui, 50);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testBackgroundSinMultiplication() throws IOException {
        creditsViewer.draw(gui, 75);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testBackgroundRedCalculation() throws IOException {
        creditsViewer.draw(gui, 100);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testBackgroundGreenCalculation() throws IOException {
        creditsViewer.draw(gui, 100);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testBackgroundBlueCalculation() throws IOException {
        creditsViewer.draw(gui, 100);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testBackgroundPowCalculation() throws IOException {
        creditsViewer.draw(gui, 50);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testBorderTopBottom() throws IOException {
        creditsViewer.draw(gui, 0);
        verify(gui, atLeast(184 * 2)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testBorderLeftRight() throws IOException {
        creditsViewer.draw(gui, 0);
        verify(gui, atLeast(110 * 2)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testBorderLoopBoundaries() throws IOException {
        creditsViewer.draw(gui, 0);
        verify(gui, atLeastOnce()).drawPixel(eq(0), anyInt(), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(183), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testDrawPixelCalledForBackground() throws IOException {
        creditsViewer.draw(gui, 0);
        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testDrawPixelCalledForBorder() throws IOException {
        creditsViewer.draw(gui, 0);
        verify(gui, atLeast(1)).drawPixel(anyInt(), eq(0), any(TextColor.RGB.class));
        verify(gui, atLeast(1)).drawPixel(anyInt(), eq(111), any(TextColor.RGB.class));
    }

    @Test
    public void testBackgroundSqrtInDistance() throws IOException {
        creditsViewer.draw(gui, 100);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testTimeBasedAnimation() throws IOException {
        creditsViewer.draw(gui, 0);
        reset(gui);
        creditsViewer.draw(gui, 100);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testDifferentTimesProduceDifferentColors() throws IOException {
        creditsViewer.draw(gui, 0);
        reset(gui);
        creditsViewer.draw(gui, 500);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }
}
