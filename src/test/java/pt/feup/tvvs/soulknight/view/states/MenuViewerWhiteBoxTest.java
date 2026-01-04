package pt.feup.tvvs.soulknight.view.states;
            
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.gui.RescalableGUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.menu.*;
import pt.feup.tvvs.soulknight.view.menu.LogoViewer;
import pt.feup.tvvs.soulknight.view.menu.OptionViewer;
import pt.feup.tvvs.soulknight.view.sprites.ViewerProvider;

import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.List;

public class MenuViewerWhiteBoxTest {
    private MenuViewer menuViewer;
    private MainMenu menu;
    private RescalableGUI gui;
    private ViewerProvider viewerProvider;

    @BeforeEach
    void setUp() throws IOException {
        menu = mock(MainMenu.class);
        gui = mock(RescalableGUI.class);
        viewerProvider = mock(ViewerProvider.class);

        when(viewerProvider.getEntryViewer()).thenReturn(mock(OptionViewer.class));
        when(viewerProvider.getLogoViewer()).thenReturn(mock(LogoViewer.class));

        Particle particle = mock(Particle.class);
        when(particle.getPosition()).thenReturn(new Position(5.0, 5.0));

        Option option = mock(Option.class);
        when(option.getPosition()).thenReturn(new Position(5.0, 5.0));

        when(menu.getParticles()).thenReturn(List.of(particle));
        when(menu.getOptions()).thenReturn(List.of(option));

        menuViewer = new MenuViewer(menu, viewerProvider);
    }

    @Test
    void testDraw() throws IOException {
        menuViewer.draw(gui, 100L);
        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui).flush();
    }

    @Test
    void testDrawSettings() throws IOException {
        menuViewer = new MenuViewer(mock(SettingsMenu.class), viewerProvider);

        menuViewer.draw(gui, 100L);
        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui).flush();
    }

    @Test
    void testDrawFirstOptionStartTime() throws IOException {
        when(menu.isSelected(0)).thenReturn(false);
        menuViewer.draw(gui, -10L);
        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui).flush();
    }

    @Test
    void testDrawDuringAnimation() throws IOException {
        when(menu.isSelected(0)).thenReturn(false);
        menuViewer.draw(gui, 10L);
        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui).flush();
    }

    @Test
    void testDrawAfterAnimation() throws IOException {
        when(menu.isSelected(0)).thenReturn(false);
        menuViewer.draw(gui, 50L);
        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui).flush();
    }

    @Test
    void testDrawAfterAnimationisSelected() throws IOException {
        when(menu.isSelected(0)).thenReturn(true);
        menuViewer.draw(gui, 50L);
        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui).flush();
    }

    @Test
    void testDrawAfterAnimationisSelected80isVisible() throws IOException {
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(true);
        menuViewer.draw(gui, 120L);
        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui).flush();
    }

    @Test
    void testDrawAfterAnimationisSelected80() throws IOException {
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(true);
        menuViewer.draw(gui, 95L);
        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui).flush();
    }
}