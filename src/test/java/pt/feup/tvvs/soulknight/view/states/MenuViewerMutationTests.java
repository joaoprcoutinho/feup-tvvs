package pt.feup.tvvs.soulknight.view.states;

import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.RescalableGUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.menu.*;
import pt.feup.tvvs.soulknight.state.particle.RandomState;
import pt.feup.tvvs.soulknight.model.menu.SettingsMenu;
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

        Menu menu = mock(MainMenu.class);

        Particle particle = new Particle(new Position(5, 5), new RandomState(), new TextColor.RGB(255, 0, 0));
        Option option = new Option(10, 10, Option.Type.START_GAME);

        when(menu.getParticles()).thenReturn(List.of(particle));
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);

        menuViewer = new MenuViewer<>(menu, viewerProvider);
    }

    @Test
    public void draw_shouldDrawLogoParticlesOptionsAndBackground() throws IOException {
        menuViewer.draw(gui, 50);

        verify(logoViewer).draw(gui, 90, 30);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
        verify(gui).cls();
        verify(gui).flush();
    }

    @Test
    public void testDrawParticlesCallsParticleViewerDraw() throws IOException {
        Menu menu = mock(MainMenu.class);
        Particle particle1 = new Particle(new Position(10, 20), new RandomState(), new TextColor.RGB(255, 0, 0));
        Particle particle2 = new Particle(new Position(30, 40), new RandomState(), new TextColor.RGB(0, 255, 0));
        
        when(menu.getParticles()).thenReturn(List.of(particle1, particle2));
        when(menu.getOptions()).thenReturn(List.of());
        when(menu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = spy(new MenuViewer<>(menu, viewerProvider));
        
        ParticleViewer mockParticleViewer = mock(ParticleViewer.class);
        
        viewer.drawParticles(gui, List.of(particle1, particle2), mockParticleViewer, 100);
        
        verify(mockParticleViewer).draw(eq(particle1), eq(gui), eq(100L), eq(0), eq(0));
        verify(mockParticleViewer).draw(eq(particle2), eq(gui), eq(100L), eq(0), eq(0));
    }

    @Test
    public void testSettingsMenuDrawsColorfulBackground() throws IOException {
        Menu settingsMenu = mock(SettingsMenu.class);
        when(settingsMenu.getParticles()).thenReturn(List.of());
        when(settingsMenu.getOptions()).thenReturn(List.of());
        when(settingsMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(settingsMenu, viewerProvider);
        viewer.draw(gui, 0);
        
        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testDrawParticlesCalled() throws IOException {
        menuViewer.draw(gui, 0);
        verify(gui, atLeastOnce()).cls();
    }

    @Test
    public void testParticlePositionMultiplication() throws IOException {
        menuViewer.draw(gui, 0);
        verify(gui, atLeastOnce()).cls();
    }

    @Test
    public void testOptionPositionMultiplicationWithMultipleOptions() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option1 = new Option(10, 10, Option.Type.START_GAME);
        Option option2 = new Option(10, 30, Option.Type.EXIT);
        Option option3 = new Option(10, 50, Option.Type.SETTINGS);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option1, option2, option3));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.isSelected(1)).thenReturn(false);
        when(menu.isSelected(2)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        menuViewer.draw(gui, 60);
        
        verify(optionViewer, times(3)).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    @Test
    public void testOptionStartTimeCalculationAtBoundary() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option1 = new Option(10, 10, Option.Type.START_GAME);
        Option option2 = new Option(10, 30, Option.Type.EXIT);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option1, option2));
        when(menu.isSelected(0)).thenReturn(false);
        when(menu.isSelected(1)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        menuViewer.draw(gui, 19);
        
        verify(optionViewer, atLeast(1)).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    @Test
    public void testOptionAnimationTimingConditionals() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(100, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        menuViewer.draw(gui, 10);
        
        verify(optionViewer).draw(argThat(opt -> opt.getPosition().x() > 100), eq(gui), any(TextColor.RGB.class));
    }

    @Test
    public void testOptionAnimationComplete() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(100, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        menuViewer.draw(gui, 25);
        
        verify(optionViewer).draw(argThat(opt -> opt.getPosition().x() == 100), eq(gui), any(TextColor.RGB.class));
    }

    @Test
    public void testAnimationOffsetCalculation() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(50, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        menuViewer.draw(gui, 0);
        verify(optionViewer).draw(argThat(opt -> opt.getPosition().x() == 90), eq(gui), any(TextColor.RGB.class));
        
        reset(optionViewer);
        
        menuViewer.draw(gui, 10);
        verify(optionViewer).draw(argThat(opt -> opt.getPosition().x() == 70), eq(gui), any(TextColor.RGB.class));
    }

    @Test
    public void testSelectedOptionBlinkingAfter80() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        menuViewer.draw(gui, 79);
        verify(optionViewer).draw(any(Option.class), eq(gui), eq(MenuViewer.unselectedColor));
        
        reset(optionViewer);
        
        menuViewer.draw(gui, 80);
        verify(optionViewer).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
    }

    @Test
    public void testBlinkingVisibilityToggle() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        
        menuViewer.draw(gui, 80);
        verify(optionViewer).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
        
        reset(optionViewer);
        
        menuViewer.draw(gui, 88);
        verify(optionViewer, never()).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
        
        reset(optionViewer);
        
        menuViewer.draw(gui, 96);
        verify(optionViewer).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
    }

    @Test
    public void testInGameBlinkingTiming() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(true);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        
        menuViewer.draw(gui, 80);
        verify(optionViewer).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
        
        reset(optionViewer);
        
        menuViewer.draw(gui, 84);
        verify(optionViewer, never()).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
        
        reset(optionViewer);
        
        menuViewer.draw(gui, 88);
        verify(optionViewer).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
    }

    @Test
    public void testOptionNotVisibleDuringBlinkOff() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        menuViewer.draw(gui, 88);
        
        verify(optionViewer, never()).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
    }

    @Test
    public void testUnselectedOptionAlwaysDrawn() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option1 = new Option(10, 10, Option.Type.START_GAME);
        Option option2 = new Option(10, 30, Option.Type.EXIT);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option1, option2));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.isSelected(1)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        menuViewer.draw(gui, 88);
        
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    @Test
    public void testDrawRetroDynamicBackgroundGrayGradient() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        
        viewer.draw(gui, 0);

        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testDrawRetroDynamicBackgroundColorfulGradient() throws IOException {
        Menu settingsMenu = mock(SettingsMenu.class);
        when(settingsMenu.getParticles()).thenReturn(List.of());
        when(settingsMenu.getOptions()).thenReturn(List.of());
        when(settingsMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(settingsMenu, viewerProvider);
        viewer.draw(gui, 0);
        
        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testBackgroundLoopPixelCount() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        viewer.draw(gui, 0);

        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testBorderDrawnAtCorrectPositions() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        viewer.draw(gui, 0);
        
        verify(gui, atLeastOnce()).drawPixel(eq(0), eq(0), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(183), eq(0), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(0), eq(111), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(183), eq(111), any(TextColor.RGB.class));
    }
}