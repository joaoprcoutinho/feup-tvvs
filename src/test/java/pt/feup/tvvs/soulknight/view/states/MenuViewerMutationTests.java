package pt.feup.tvvs.soulknight.view.states;

import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.gui.RescalableGUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.menu.*;
import pt.feup.tvvs.soulknight.state.particle.RandomState;
import pt.feup.tvvs.soulknight.view.menu.LogoViewer;
import pt.feup.tvvs.soulknight.view.menu.OptionViewer;
import pt.feup.tvvs.soulknight.view.menu.ParticleViewer;
import pt.feup.tvvs.soulknight.view.sprites.ViewerProvider;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

public class MenuViewerMutationTests {
    private RescalableGUI gui;
    private ViewerProvider viewerProvider;
    private MenuViewer<Menu> menuViewer;

    private LogoViewer logoViewer;
    private OptionViewer optionViewer;

    @BeforeEach
    void setup() throws IOException {
        gui = mock(RescalableGUI.class);
        viewerProvider = mock(ViewerProvider.class);

        logoViewer = mock(LogoViewer.class);
        optionViewer = mock(OptionViewer.class);

        when(viewerProvider.getLogoViewer()).thenReturn(logoViewer);
        when(viewerProvider.getEntryViewer()).thenReturn(optionViewer);

        // Menu model
        Menu menu = mock(MainMenu.class);

        Particle particle = new Particle(new Position(5, 5), new RandomState(), new TextColor.RGB(255, 0, 0)); // x, y, size/velocity/etc.
        Option option = new Option(10, 10, Option.Type.START_GAME);

        when(menu.getParticles()).thenReturn(List.of(particle));
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);

        menuViewer = new MenuViewer<>(menu, viewerProvider);
    }

    @Test
    void draw_shouldDrawLogoParticlesOptionsAndBackground() throws IOException {
        menuViewer.draw(gui, 50);

        // Logo must be drawn
        verify(logoViewer).draw(gui, 90, 30);

        // Particles must be drawn
        verify(gui, atLeastOnce())
                .drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));

        // Options must be drawn
        verify(optionViewer, atLeastOnce())
                .draw(any(Option.class), eq(gui), any(TextColor.RGB.class));

        // Screen lifecycle
        verify(gui).cls();
        verify(gui).flush();
    }
}