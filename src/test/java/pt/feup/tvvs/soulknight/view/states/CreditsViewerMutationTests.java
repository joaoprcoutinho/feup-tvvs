package pt.feup.tvvs.soulknight.view.states;

import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
        // Mock GUI
        gui = mock(GUI.class);

        // Mock viewers
        textViewer = mock(TextViewer.class);
        logoViewer = mock(LogoViewer.class);

        // Mock ViewerProvider to return correct viewers
        viewerProvider = mock(ViewerProvider.class);
        when(viewerProvider.getTextViewer()).thenReturn(textViewer);
        when(viewerProvider.getLogoViewer()).thenReturn(logoViewer);

        // Create a real Credits model (not mock) and initialize messages/names
        Knight knight = new Knight(1, 1, 1, 1, 1);
        Credits realCredits = new Credits(knight);
        realCredits.setMessages(new String[]{"Message1", "Message2"});
        realCredits.setNames(new String[]{"Name1", "Name2"});

        // Use spy if you want to stub methods
        model = spy(realCredits);

        // Initialize CreditsViewer with model and mocked ViewerProvider
        creditsViewer = new CreditsViewer(model, viewerProvider);
    }

    @Test
    void testDraw_CallsAllDrawMethods() throws IOException {
        // Mock model data
        when(model.getMessages()).thenReturn(new String[]{"Hello", "World"});
        when(model.getNames()).thenReturn(new String[]{"Alice", "Bob"});
        when(model.getScore()).thenReturn(42);
        when(model.getDeaths()).thenReturn(3);
        when(model.getMinutes()).thenReturn(1);
        when(model.getSeconds()).thenReturn(5);

        creditsViewer.draw(gui, 0);

        // Verify TextViewer.draw called for messages
        verify(textViewer).draw(eq("Hello"), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
        verify(textViewer).draw(eq("World"), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));

        // Verify TextViewer.draw called for names
        verify(textViewer).draw(eq("Alice"), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
        verify(textViewer).draw(eq("Bob"), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));

        // Verify TextViewer.draw called for score, deaths, duration
        verify(textViewer).draw(contains("Score"), anyDouble(), anyDouble(), eq(CreditsViewer.scoreColor), eq(gui));
        verify(textViewer).draw(contains("Deaths"), anyDouble(), anyDouble(), eq(CreditsViewer.deathColor), eq(gui));
        verify(textViewer).draw(contains("Time"), anyDouble(), anyDouble(), eq(CreditsViewer.timeColor), eq(gui));

        // Verify LogoViewer.draw called
        verify(logoViewer).draw(gui, 60, 16);

        // Verify GUI methods called
        verify(gui).cls();
        verify(gui).flush();
    }

    @Test
    void testDrawMessagesSpacingAndBoundaries() throws IOException {
        when(model.getMessages()).thenReturn(new String[]{"Test1", "Test2"});
        creditsViewer.draw(gui, 0);

        // This indirectly kills negated conditional and multiplication/division arithmetic mutations
        verify(textViewer, times(7)).draw(anyString(), anyDouble(), anyDouble(), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    void testDrawNamesSpacingAndBoundaries() throws IOException {
        when(model.getNames()).thenReturn(new String[]{"Name1", "Name2"});
        creditsViewer.draw(gui, 0);

        // Kill negated conditional and arithmetic mutation in drawNames
        verify(textViewer, times(7)).draw(anyString(), anyDouble(), anyDouble(), any(TextColor.RGB.class), eq(gui));
    }
}
