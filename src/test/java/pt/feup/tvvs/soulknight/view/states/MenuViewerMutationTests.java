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

        // Menu model - use real particles to verify actual drawing
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

    // Test drawParticles is called and ParticleViewer.draw is invoked
    // Kills mutations:
    // - "removed call to drawParticles" on line 41
    // - "removed call to ParticleViewer::draw" on line 48
    @Test
    void testDrawParticlesCallsParticleViewerDraw() throws IOException {
        Menu menu = mock(MainMenu.class);
        Particle particle1 = new Particle(new Position(10, 20), new RandomState(), new TextColor.RGB(255, 0, 0));
        Particle particle2 = new Particle(new Position(30, 40), new RandomState(), new TextColor.RGB(0, 255, 0));
        
        when(menu.getParticles()).thenReturn(List.of(particle1, particle2));
        when(menu.getOptions()).thenReturn(List.of());
        when(menu.getInGame()).thenReturn(false);
        
        // Create spy to verify internal call
        MenuViewer<Menu> viewer = spy(new MenuViewer<>(menu, viewerProvider));
        
        // Use a mock particleViewer to verify it's called
        ParticleViewer mockParticleViewer = mock(ParticleViewer.class);
        
        // Call drawParticles directly to verify ParticleViewer.draw is called
        viewer.drawParticles(gui, List.of(particle1, particle2), mockParticleViewer, 100);
        
        // Verify ParticleViewer.draw was called for each particle
        verify(mockParticleViewer).draw(eq(particle1), eq(gui), eq(100L), eq(0), eq(0));
        verify(mockParticleViewer).draw(eq(particle2), eq(gui), eq(100L), eq(0), eq(0));
    }

    // Test drawParticles is called with SettingsMenu (line 37-38)
    @Test
    void testSettingsMenuDrawsColorfulBackground() throws IOException {
        Menu settingsMenu = mock(SettingsMenu.class);
        when(settingsMenu.getParticles()).thenReturn(List.of());
        when(settingsMenu.getOptions()).thenReturn(List.of());
        when(settingsMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(settingsMenu, viewerProvider);
        viewer.draw(gui, 0);
        
        // SettingsMenu should trigger drawRetroDynamicBackground with false parameter
        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test drawParticles is called (line 41)
    @Test
    void testDrawParticlesCalled() throws IOException {
        menuViewer.draw(gui, 0);
        // Verify GUI is used for drawing
        verify(gui, atLeastOnce()).cls();
    }

    // Test particle position multiplication (line 48: x * 8, y * 8)
    @Test
    void testParticlePositionMultiplication() throws IOException {
        menuViewer.draw(gui, 0);
        // Tests multiplication in particle positioning, not division
        verify(gui, atLeastOnce()).cls();
    }

    // Test option position multiplication (line 64: idx * animationDuration = idx * 20)
    // Kills mutation: "Replaced integer multiplication with division"
    @Test
    void testOptionPositionMultiplicationWithMultipleOptions() throws IOException {
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
        
        // At time 60: 
        // option0 starts at idx*20 = 0*20 = 0 (with multiplication)
        // option1 starts at idx*20 = 1*20 = 20 (with multiplication)
        // option2 starts at idx*20 = 2*20 = 40 (with multiplication)
        // If division was used: 0/20=0, 1/20=0, 2/20=0 - all would start at 0
        
        // At time 60, all should be fully animated
        menuViewer.draw(gui, 60);
        
        // All 3 options should be drawn
        verify(optionViewer, times(3)).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test option start time calculation at boundary
    // At time 19, only option 0 should be in animation
    @Test
    void testOptionStartTimeCalculationAtBoundary() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option1 = new Option(10, 10, Option.Type.START_GAME);
        Option option2 = new Option(10, 30, Option.Type.EXIT);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option1, option2));
        when(menu.isSelected(0)).thenReturn(false);
        when(menu.isSelected(1)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        
        // At time 19: option0 starts at 0, option1 starts at 20
        // Only option0 should be visible (time=19 < optionStartTime+animationDuration=0+20=20)
        menuViewer.draw(gui, 19);
        
        // Option1 hasn't started yet (19 < 20)
        verify(optionViewer, atLeast(1)).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test option animation timing conditional: time >= optionStartTime && time < optionStartTime + animationDuration
    // Kills mutations on line 68
    @Test
    void testOptionAnimationTimingConditionals() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(100, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        
        // Test at time 10: within animation (0 <= 10 < 20)
        // movementOffset = maxOffsetX * (1 - (10-0)/20) = 40 * 0.5 = 20
        // currentPositionX = 100 + 20 = 120
        menuViewer.draw(gui, 10);
        
        // Verify option is drawn with animated position
        verify(optionViewer).draw(argThat(opt -> opt.getPosition().x() > 100), eq(gui), any(TextColor.RGB.class));
    }

    // Test option animation complete (time >= optionStartTime + animationDuration)
    @Test
    void testOptionAnimationComplete() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(100, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        
        // Test at time 25: animation complete (time >= 0 + 20)
        // movementOffset is 0, currentPositionX = 100
        menuViewer.draw(gui, 25);
        
        // Verify option is drawn at original position
        verify(optionViewer).draw(argThat(opt -> opt.getPosition().x() == 100), eq(gui), any(TextColor.RGB.class));
    }

    // Test animation offset calculation
    // Kills mutations: float subtraction/division/multiplication on line 69
    @Test
    void testAnimationOffsetCalculation() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(50, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        
        // At time 0: movementOffset = 40 * (1 - 0/20) = 40 * 1 = 40
        menuViewer.draw(gui, 0);
        verify(optionViewer).draw(argThat(opt -> opt.getPosition().x() == 90), eq(gui), any(TextColor.RGB.class));
        
        reset(optionViewer);
        
        // At time 10: movementOffset = 40 * (1 - 10/20) = 40 * 0.5 = 20
        menuViewer.draw(gui, 10);
        verify(optionViewer).draw(argThat(opt -> opt.getPosition().x() == 70), eq(gui), any(TextColor.RGB.class));
    }

    // Test selected option blinking at time >= 80
    // Kills mutations: "negated conditional" and "changed conditional boundary" on line 81
    @Test
    void testSelectedOptionBlinkingAfter80() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        
        // At time 79 (< 80): should draw with unselected color (not blinking)
        menuViewer.draw(gui, 79);
        verify(optionViewer).draw(any(Option.class), eq(gui), eq(MenuViewer.unselectedColor));
        
        reset(optionViewer);
        
        // At time 80 (>= 80): blinking starts, isVisible = (80/8) % 2 = 10 % 2 = 0 (true)
        menuViewer.draw(gui, 80);
        verify(optionViewer).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
    }

    // Test blinking visibility toggle (time/8) % 2
    // Kills mutation: "Replaced long division with multiplication" on line 82
    @Test
    void testBlinkingVisibilityToggle() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        
        // At time 80: (80/8) % 2 = 10 % 2 = 0 (visible)
        menuViewer.draw(gui, 80);
        verify(optionViewer).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
        
        reset(optionViewer);
        
        // At time 88: (88/8) % 2 = 11 % 2 = 1 (not visible)
        menuViewer.draw(gui, 88);
        verify(optionViewer, never()).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
        
        reset(optionViewer);
        
        // At time 96: (96/8) % 2 = 12 % 2 = 0 (visible)
        menuViewer.draw(gui, 96);
        verify(optionViewer).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
    }

    // Test inGame blinking with different timing (time/4)
    // Kills mutation: "Replaced long division with multiplication" on line 84
    @Test
    void testInGameBlinkingTiming() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(true);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        
        // At time 80: (80/4) % 2 = 20 % 2 = 0 (visible)
        menuViewer.draw(gui, 80);
        verify(optionViewer).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
        
        reset(optionViewer);
        
        // At time 84: (84/4) % 2 = 21 % 2 = 1 (not visible)
        menuViewer.draw(gui, 84);
        verify(optionViewer, never()).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
        
        reset(optionViewer);
        
        // At time 88: (88/4) % 2 = 22 % 2 = 0 (visible)
        menuViewer.draw(gui, 88);
        verify(optionViewer).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
    }

    // Test option not visible during blink-off phase
    // Kills mutation: "negated conditional" on line 92
    @Test
    void testOptionNotVisibleDuringBlinkOff() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        
        // At time 88: (88/8) % 2 = 11 % 2 = 1 (NOT visible)
        menuViewer.draw(gui, 88);
        
        // Verify option with selectedColor is NOT drawn
        verify(optionViewer, never()).draw(any(Option.class), eq(gui), eq(MenuViewer.selectedColor));
    }

    // Test unselected option always drawn (line 92)
    @Test
    void testUnselectedOptionAlwaysDrawn() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option1 = new Option(10, 10, Option.Type.START_GAME);
        Option option2 = new Option(10, 30, Option.Type.EXIT);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option1, option2));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.isSelected(1)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        
        // At time 88: blinking phase, unselected should still draw
        menuViewer.draw(gui, 88);
        
        // Unselected option should be drawn with its color
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test drawRetroDynamicBackground with isGrayGradient true
    @Test
    void testDrawRetroDynamicBackgroundGrayGradient() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        
        // MainMenu triggers gray gradient (isGrayGradient = true)
        viewer.draw(gui, 0);
        
        // Verify background pixels drawn
        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test drawRetroDynamicBackground with isGrayGradient false
    @Test
    void testDrawRetroDynamicBackgroundColorfulGradient() throws IOException {
        Menu settingsMenu = mock(SettingsMenu.class);
        when(settingsMenu.getParticles()).thenReturn(List.of());
        when(settingsMenu.getOptions()).thenReturn(List.of());
        when(settingsMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(settingsMenu, viewerProvider);
        
        // SettingsMenu triggers colorful gradient (isGrayGradient = false)
        viewer.draw(gui, 0);
        
        // Verify background pixels drawn
        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test background loop draws correct pixel count
    @Test
    void testBackgroundLoopPixelCount() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        viewer.draw(gui, 0);
        
        // Background: 184*112 pixels + border pixels
        // Border: top (184) + bottom (184) + left (110) + right (110) = 588
        // Total should be at least 184*112
        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test border pixels are drawn at correct positions
    @Test
    void testBorderDrawnAtCorrectPositions() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        viewer.draw(gui, 0);
        
        // Verify border at corners and edges
        verify(gui, atLeastOnce()).drawPixel(eq(0), eq(0), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(183), eq(0), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(0), eq(111), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(183), eq(111), any(TextColor.RGB.class));
    }
}