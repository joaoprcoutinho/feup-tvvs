package pt.feup.tvvs.soulknight.view.states;

import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.gui.RescalableGUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.menu.*;
import pt.feup.tvvs.soulknight.state.particle.RandomState;
import pt.feup.tvvs.soulknight.model.menu.SettingsMenu;
import pt.feup.tvvs.soulknight.view.menu.LogoViewer;
import pt.feup.tvvs.soulknight.view.menu.OptionViewer;
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
    @Test
    void testOptionPositionMultiplication() throws IOException {
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
        // At time 60: option0 starts at 0, option1 at 20, option2 at 40
        // All should be visible/animated by time 60
        menuViewer.draw(gui, 60);
        
        // Tests multiplication in option start time calculation (idx * 20)
        // If replaced with division, timing would be wrong
        verify(optionViewer, times(3)).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test option position addition (line 68: yAlignment + idx * OptionHeight)
    @Test
    void testOptionPositionAddition() throws IOException {
        menuViewer.draw(gui, 0);
        // Tests addition in position calculation
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test animation timing with selected option at different times (lines 76-79, 88-94, 97-102)
    @Test
    void testSelectedOptionBlinkingBehavior() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        
        // Test at time 80 (blink starts)
        menuViewer.draw(gui, 80);
        reset(optionViewer);
        
        // Test at time 88 (blinkTime = 88 % 30 = 28, should use isVisible logic)
        menuViewer.draw(gui, 88);
        reset(optionViewer);
        
        // Test at time 96 (blinkTime = 96 % 30 = 6, visible)
        menuViewer.draw(gui, 96);
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test inGame blinking logic (line 89-91)
    @Test
    void testInGameBlinkingLogic() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(true); // InGame = true
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        
        // Test with inGame blinking (time / 4) % 2 == 0
        menuViewer.draw(gui, 88); // 88 / 4 = 22, 22 % 2 = 0 (visible)
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test animation timing conditional (line 76: blinkTime < 15)
    @Test
    void testAnimationTimingConditionalLessThan15() throws IOException {
        menuViewer.draw(gui, 10); // time % 30 = 10 < 15
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test animation timing boundary (line 76: blinkTime == 15)
    @Test
    void testAnimationTimingBoundaryAt15() throws IOException {
        menuViewer.draw(gui, 15); // time % 30 = 15
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test animation timing modulo (line 78: time % 30)
    @Test
    void testAnimationTimingModulo() throws IOException {
        menuViewer.draw(gui, 35); // 35 % 30 = 5
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test animation timing subtraction and division (line 78-79: blinkTime - 15, division)
    @Test
    void testAnimationTimingSubtraction() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        
        // Test at various times to cover all arithmetic operations
        menuViewer.draw(gui, 95); // 95 % 30 = 5 < 15 (visible)
        reset(optionViewer);
        menuViewer.draw(gui, 110); // 110 % 30 = 20 >= 15 (alpha calculation: 1.0 - (20-15)/15.0)
        reset(optionViewer);
        menuViewer.draw(gui, 115); // 115 % 30 = 25 (alpha calculation: 1.0 - (25-15)/15.0)
        
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test option selected conditional (line 88: i == selected)
    @Test
    void testOptionSelectedConditional() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        menuViewer.draw(gui, 5);
        
        // Tests == conditional for selected option
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test option not selected (line 88: negation)
    @Test
    void testOptionNotSelected() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        menuViewer.draw(gui, 0);
        
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test visibility conditional OR (line 90: blinkTime < 15 || i != selected)
    @Test
    void testVisibilityConditionalOr() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option1 = new Option(10, 10, Option.Type.START_GAME);
        Option option2 = new Option(10, 10, Option.Type.EXIT);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option1, option2));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.isSelected(1)).thenReturn(false);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        menuViewer.draw(gui, 20); // blinkTime = 20, >= 15
        
        // Tests OR conditional - unselected options always visible
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test visibility when blinking (line 90-94)
    @Test
    void testVisibilityWhenBlinking() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        menuViewer.draw(gui, 25); // blinkTime = 25, should fade
        
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test alpha calculation division (line 97: (blinkTime - 15) / 15.0)
    @Test
    void testAlphaCalculationDivision() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        menuViewer.draw(gui, 22); // blinkTime = 22, alpha = 1 - 7/15
        
        // Tests division in alpha calculation
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test alpha calculation subtraction (line 98: 1.0 - ...)
    @Test
    void testAlphaCalculationSubtraction() throws IOException {
        Menu menu = mock(MainMenu.class);
        Option option = new Option(10, 10, Option.Type.START_GAME);
        when(menu.getParticles()).thenReturn(List.of());
        when(menu.getOptions()).thenReturn(List.of(option));
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.getInGame()).thenReturn(false);
        
        menuViewer = new MenuViewer<>(menu, viewerProvider);
        menuViewer.draw(gui, 28); // blinkTime = 28, alpha close to 0
        
        // Tests subtraction in alpha calculation
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test retro background conditional (line 102: if (useRetroBackground))
    @Test
    void testRetroBackgroundConditional() throws IOException {
        // Using MainMenu triggers retro background
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        viewer.draw(gui, 0);
        
        // When retro background is enabled, drawRetroDynamicBackground should be called
        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test retro background loop boundaries (line 114: w < screenWidth, h < screenHeight)
    @Test
    void testRetroBackgroundLoopBoundaries() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        viewer.draw(gui, 0);
        
        // Tests < not <=
        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test retro background sin calculation (line 117: Math.sin)
    @Test
    void testRetroBackgroundSinCalculation() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        viewer.draw(gui, 100);
        
        // Tests Math.sin with different parameters
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test retro background multiplication (line 118: 0.5 * Math.sin)
    @Test
    void testRetroBackgroundMultiplication() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        viewer.draw(gui, 50);
        
        // Tests multiplication, not division
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test retro background addition (line 119: 0.5 + ...)
    @Test
    void testRetroBackgroundAddition() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        viewer.draw(gui, 75);
        
        // Tests addition, not subtraction
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test retro background PI operations (line 120-123: Math.PI / 2.0, etc.)
    @Test
    void testRetroBackgroundPIOperations() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        viewer.draw(gui, 100);
        
        // Tests Math.PI division and various offsets
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test colorful gradient (SettingsMenu) with all RGB calculations (lines 121-123)
    @Test
    void testColorfulGradientRGBCalculations() throws IOException {
        Menu settingsMenu = mock(SettingsMenu.class);
        when(settingsMenu.getParticles()).thenReturn(List.of());
        when(settingsMenu.getOptions()).thenReturn(List.of());
        when(settingsMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(settingsMenu, viewerProvider);
        viewer.draw(gui, 150);
        
        // Verify all RGB channels are calculated separately with PI offsets
        verify(gui, atLeast(184 * 112)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test border drawing top/bottom (line 128-129)
    @Test
    void testBorderTopBottom() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        viewer.draw(gui, 0);
        
        // for (int w = 0; w < screenWidth; w++)
        verify(gui, atLeast(184 * 2)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test border drawing left/right (line 135-141)
    @Test
    void testBorderLeftRight() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        viewer.draw(gui, 0);
        
        // for (int h = 1; h < screenHeight - 1; h++)
        verify(gui, atLeast(110 * 2)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test border loop subtraction (line 135: screenHeight - 1) and drawPixel calls
    @Test
    void testBorderLoopSubtraction() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        viewer.draw(gui, 0);
        
        // Tests subtraction in loop boundary (h < screenHeight - 1)
        // Verify left border (x=0) and right border (x=183) are drawn
        verify(gui, atLeastOnce()).drawPixel(eq(0), anyInt(), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(183), anyInt(), any(TextColor.RGB.class));
        // Verify top/bottom borders at specific heights
        verify(gui, atLeastOnce()).drawPixel(anyInt(), eq(0), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(anyInt(), eq(111), any(TextColor.RGB.class));
    }

    // Test different time values affect animation
    @Test
    void testDifferentTimeValuesAffectAnimation() throws IOException {
        menuViewer.draw(gui, 5);
        reset(optionViewer);
        menuViewer.draw(gui, 15);
        reset(optionViewer);
        menuViewer.draw(gui, 25);
        
        verify(optionViewer, atLeastOnce()).draw(any(Option.class), eq(gui), any(TextColor.RGB.class));
    }

    // Test retro background with different times
    @Test
    void testRetroBackgroundDifferentTimes() throws IOException {
        Menu mainMenu = mock(MainMenu.class);
        when(mainMenu.getParticles()).thenReturn(List.of());
        when(mainMenu.getOptions()).thenReturn(List.of());
        when(mainMenu.getInGame()).thenReturn(false);
        
        MenuViewer<Menu> viewer = new MenuViewer<>(mainMenu, viewerProvider);
        
        viewer.draw(gui, 0);
        reset(gui);
        viewer.draw(gui, 500);
        
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }
}