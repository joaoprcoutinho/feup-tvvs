package pt.feup.tvvs.soulknight.view.states;

import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.credits.Credits;
import pt.feup.tvvs.soulknight.view.menu.LogoViewer;
import pt.feup.tvvs.soulknight.view.sprites.ViewerProvider;
import pt.feup.tvvs.soulknight.view.text.TextViewer;

import static org.mockito.Mockito.*;
import java.io.IOException;

public class CreditsViewerWhiteBoxTests {
    private CreditsViewer creditsViewer;
    private Credits credits;
    private GUI gui;
    private TextViewer textViewer;
    private LogoViewer logoViewer;
    private ViewerProvider viewerProvider;

    @BeforeEach
    void setUp() {
        credits = mock(Credits.class);
        gui = mock(GUI.class);
        textViewer = mock(TextViewer.class);
        logoViewer = mock(LogoViewer.class);
        viewerProvider = mock(ViewerProvider.class);

        when(viewerProvider.getTextViewer()).thenReturn(textViewer);
        when(viewerProvider.getLogoViewer()).thenReturn(logoViewer);

        creditsViewer = new CreditsViewer(credits, viewerProvider);
    }

    @Test
    public void testDraw() throws IOException {
        when(credits.getMessages()).thenReturn(new String[]{"Message1"});
        when(credits.getNames()).thenReturn(new String[]{"Name1"});
        when(credits.getScore()).thenReturn(100);
        when(credits.getDeaths()).thenReturn(5);
        when(credits.getMinutes()).thenReturn(10);
        when(credits.getSeconds()).thenReturn(30);

        creditsViewer.draw(gui, 100L);

        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(textViewer, atLeastOnce()).draw(anyString(), anyDouble(), anyDouble(), any(TextColor.class), eq(gui));
        verify(logoViewer).draw(gui, 60, 16);
        verify(gui).flush();
    }
}
