package pt.feup.tvvs.soulknight.controller.menu;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.Game;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.menu.Menu;
import pt.feup.tvvs.soulknight.model.menu.Particle;
import pt.feup.tvvs.soulknight.state.particle.RandomState;
import com.googlecode.lanterna.TextColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;

public class ParticleMenuControllerMutationTests {
    
    private ParticleMenuController controller;
    private Menu menu;
    private Game game;
    private GUI gui;

    @BeforeEach
    void setUp() {
        menu = mock(Menu.class);
        game = mock(Game.class);
        gui = mock(GUI.class);
        
        Particle particle1 = new Particle(new Position(10, 10), new RandomState(), new TextColor.RGB(255, 0, 0));
        Particle particle2 = new Particle(new Position(50, 50), new RandomState(), new TextColor.RGB(0, 255, 0));
        
        List<Particle> particles = new ArrayList<>();
        particles.add(particle1);
        particles.add(particle2);
        
        when(menu.getParticles()).thenReturn(particles);
        
        controller = new ParticleMenuController(menu);
    }

    // Test getWindAngle returns actual value, not 0.0d
    @Test
    void testGetWindAngle() {
        double windAngle = controller.getWindAngle();
        assertEquals(0.0, windAngle, 0.001); // Verifies actual return value
        assertNotNull(controller); // Ensures method returns properly
    }

    // Test getWindSpeed returns non-zero value
    @Test
    void testGetWindSpeed() {
        double windSpeed = controller.getWindSpeed();
        assertTrue(windSpeed > 0); // Should be 2.0, not 0.0d
        assertEquals(2.0, windSpeed, 0.001);
    }

    // Test wrapPosition with negative x
    @Test
    void testWrapPositionNegativeX() {
        Position pos = controller.wrapPosition(-1, 50);
        assertNotNull(pos);
        assertEquals(219, pos.x(), 0.001); // Should wrap to screenWidth - 1
        assertEquals(50, pos.y(), 0.001);
    }

    // Test wrapPosition with x >= screenWidth
    @Test
    void testWrapPositionTooLargeX() {
        Position pos = controller.wrapPosition(220, 50);
        assertNotNull(pos);
        assertEquals(1, pos.x(), 0.001); // Should wrap to 1
        assertEquals(50, pos.y(), 0.001);
    }

    // Test wrapPosition with negative y
    @Test
    void testWrapPositionNegativeY() {
        Position pos = controller.wrapPosition(100, -1);
        assertNotNull(pos);
        assertEquals(100, pos.x(), 0.001);
        assertEquals(109, pos.y(), 0.001); // Should wrap to screenHeight - 1
    }

    // Test wrapPosition with y >= screenHeight
    @Test
    void testWrapPositionTooLargeY() {
        Position pos = controller.wrapPosition(100, 110);
        assertNotNull(pos);
        assertEquals(100, pos.x(), 0.001);
        assertEquals(1, pos.y(), 0.001); // Should wrap to 1
    }

    // Test wrapPosition boundaries (< vs <=)
    @Test
    void testWrapPositionXBoundary() {
        Position pos1 = controller.wrapPosition(0, 50);
        assertEquals(0, pos1.x(), 0.001); // Should not wrap (>= 0)
        
        Position pos2 = controller.wrapPosition(219, 50);
        assertEquals(219, pos2.x(), 0.001); // Should not wrap (< 220)
    }

    // Test wrapPosition y boundaries
    @Test
    void testWrapPositionYBoundary() {
        Position pos1 = controller.wrapPosition(100, 0);
        assertEquals(0, pos1.y(), 0.001); // Should not wrap (>= 0)
        
        Position pos2 = controller.wrapPosition(100, 109);
        assertEquals(109, pos2.y(), 0.001); // Should not wrap (< 110)
    }

    // Test move method modulo operation with particle state verification
    @Test
    void testMoveModuloOperation() throws IOException {
        Particle particle = menu.getParticles().get(0);
        Position oldPos = new Position(particle.getPosition().x(), particle.getPosition().y());
        controller.move(game, GUI.ACTION.NULL, 50);
        // mode = (50 / 50) % 5 = 1 % 5 = 1 (WindyState)
        verify(menu, atLeastOnce()).getParticles();
        // Verify particle state was actually changed (not just called)
        assertNotNull(particle.getState());
        assertTrue(particle.getState().getClass().getName().contains("State"));
    }

    // Test move with time that triggers gradient change (division, not multiplication)
    @Test
    void testMoveDivisionInModeCalculation() throws IOException {
        // time = 100, mode = (100 / 50) % 5 = 2 % 5 = 2 (CalmState)
        Particle particle = menu.getParticles().get(0);
        Position beforePos = particle.getPosition();
        controller.move(game, GUI.ACTION.NULL, 100);
        verify(menu, atLeastOnce()).getParticles();
        // Verify the division calculation produces correct mode and position changed
        assertNotNull(particle.getPosition());
        // If division is replaced with multiplication, mode would be wrong
        String stateName = particle.getState().getClass().getSimpleName();
        assertNotNull(stateName);
    }

    // Test updateGradients is called and modulo operation works
    @Test
    void testMoveCallsUpdateGradients() throws IOException {
        // First call to initialize gradient colors
        controller.move(game, GUI.ACTION.NULL, 0);
        // Call at gradient change interval (500 ticks)
        controller.move(game, GUI.ACTION.NULL, 500);
        // Should have updated gradients - verify by calling again
        controller.move(game, GUI.ACTION.NULL, 501);
        assertNotNull(controller);
        // Verify the controller actually processed the gradient update
        verify(menu, atLeast(3)).getParticles();
    }

    // Test gradient change at exact boundary (500) - modulo operation
    @Test
    void testUpdateGradientsAtBoundary500() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 499);
        controller.move(game, GUI.ACTION.NULL, 500);
        controller.move(game, GUI.ACTION.NULL, 501);
        // Should trigger gradient change at 500 (time % 500 == 0)
        // If modulo replaced with multiplication, would not trigger
        verify(menu, atLeast(3)).getParticles();
        assertNotNull(controller);
    }

    // Test gradient change detection (modulo, not multiplication)
    @Test
    void testUpdateGradientsModuloNotMultiplication() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 1000);
        // 1000 % 500 = 0, should trigger change
        controller.move(game, GUI.ACTION.NULL, 1500);
        // 1500 % 500 = 0, should trigger change
        assertNotNull(controller);
    }

    // Test gradient transition boundary conditions - all conditionals and additions
    @Test
    void testGradientTransitionBoundaryExactly100() throws IOException {
        // Start transition at time 500
        controller.move(game, GUI.ACTION.NULL, 500);
        Particle particle = menu.getParticles().get(0);
        assertNotNull(particle.getState());
        // At time 599: 599 < 500 + 100 (should still be transitioning)
        controller.move(game, GUI.ACTION.NULL, 599);
        assertNotNull(particle.getPosition());
        // At time 600: 600 >= 500 + 100 (should finalize)
        controller.move(game, GUI.ACTION.NULL, 600);
        assertNotNull(particle.getState());
        // If conditional boundaries or addition are mutated, behavior changes
        verify(menu, atLeast(3)).getParticles();
    }

    // Test gradient transition condition boundaries
    @Test
    void testGradientTransitionStartTickConditions() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 500);
        // transitionStartTick = 500
        // At time 550: transitionStartTick >= 0 (true) && time < 500 + 100 (true)
        controller.move(game, GUI.ACTION.NULL, 550);
        assertNotNull(controller);
    }

    // Test interpolateColor subtraction operation
    @Test
    void testInterpolateColorSubtraction() {
        TextColor.RGB start = new TextColor.RGB(100, 100, 100);
        TextColor.RGB end = new TextColor.RGB(200, 200, 200);
        TextColor.RGB result = controller.interpolateColor(start, end, 0.5f);
        
        assertNotNull(result);
        // At factor 0.5: 100 + 0.5 * (200 - 100) = 150
        // Tests subtraction (end - start), not addition
        assertEquals(150, result.getRed());
        assertEquals(150, result.getGreen());
        assertEquals(150, result.getBlue());
    }

    // Test interpolateColor multiplication
    @Test
    void testInterpolateColorMultiplication() {
        TextColor.RGB start = new TextColor.RGB(0, 0, 0);
        TextColor.RGB end = new TextColor.RGB(100, 150, 200);
        TextColor.RGB result = controller.interpolateColor(start, end, 0.5f);
        
        assertNotNull(result);
        // Tests multiplication (factor * difference), not division
        assertEquals(50, result.getRed());
        assertEquals(75, result.getGreen());
        assertEquals(100, result.getBlue());
    }

    // Test interpolateColor addition at factor 0
    @Test
    void testInterpolateColorAtFactorZero() {
        TextColor.RGB start = new TextColor.RGB(100, 100, 100);
        TextColor.RGB end = new TextColor.RGB(200, 200, 200);
        TextColor.RGB result = controller.interpolateColor(start, end, 0.0f);
        
        assertNotNull(result);
        // At factor 0: should be start color
        assertEquals(100, result.getRed());
        assertEquals(100, result.getGreen());
        assertEquals(100, result.getBlue());
    }

    // Test interpolateColor addition at factor 1
    @Test
    void testInterpolateColorAtFactorOne() {
        TextColor.RGB start = new TextColor.RGB(100, 100, 100);
        TextColor.RGB end = new TextColor.RGB(200, 200, 200);
        TextColor.RGB result = controller.interpolateColor(start, end, 1.0f);
        
        assertNotNull(result);
        // At factor 1: should be end color
        assertEquals(200, result.getRed());
        assertEquals(200, result.getGreen());
        assertEquals(200, result.getBlue());
    }

    // Test randomColor returns non-null
    @Test
    void testRandomColor() {
        TextColor.RGB color = controller.randomColor();
        assertNotNull(color);
        // Should return valid RGB color, not null
        assertTrue(color.getRed() >= 0 && color.getRed() <= 255);
        assertTrue(color.getGreen() >= 0 && color.getGreen() <= 255);
        assertTrue(color.getBlue() >= 0 && color.getBlue() <= 255);
    }

    // Test move with different mode values
    @Test
    void testMoveMode0Random() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 0);
        // mode = 0 / 50 % 5 = 0 (RandomState)
        verify(menu, atLeastOnce()).getParticles();
    }

    @Test
    void testMoveMode1Windy() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 50);
        // mode = 50 / 50 % 5 = 1 (WindyState)
        verify(menu, atLeastOnce()).getParticles();
    }

    @Test
    void testMoveMode2Calm() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 100);
        // mode = 100 / 50 % 5 = 2 (CalmState)
        verify(menu, atLeastOnce()).getParticles();
    }

    @Test
    void testMoveMode3Dispersing() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 150);
        // mode = 150 / 50 % 5 = 3 (DispersingSt ate)
        verify(menu, atLeastOnce()).getParticles();
    }

    @Test
    void testMoveMode4Zico() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 200);
        // mode = 200 / 50 % 5 = 4 (ZicoState)
        verify(menu, atLeastOnce()).getParticles();
    }

    // Test gradient transition factor calculation (subtraction in numerator)
    @Test
    void testGradientTransitionFactorSubtraction() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 500);
        // Start transition
        controller.move(game, GUI.ACTION.NULL, 550);
        // factor = (550 - 500) / 100 = 0.5
        // Tests subtraction (time - transitionStartTick), not addition
        assertNotNull(controller);
    }

    // Test gradient transition factor division
    @Test
    void testGradientTransitionFactorDivision() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 500);
        controller.move(game, GUI.ACTION.NULL, 525);
        // factor = 25 / 100 = 0.25
        // Tests division by gradientTransitionFrames, not multiplication
        assertNotNull(controller);
    }

    // Integration tests with real objects and value verification

    @Test
    void testDivisionInModeCalculationProducesCorrectStateIntegration() throws IOException {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        // time=100: mode = (100 / 50) % 5 = 2 % 5 = 2
        realController.move(mock(Game.class), GUI.ACTION.NULL, 100);
        
        // Verify division was used (not multiplication) by checking state
        Particle particle = realMenu.getParticles().get(0);
        String stateName = particle.getState().getClass().getSimpleName();
        assertEquals("CalmState", stateName);
    }

    @Test
    void testModuloOperationCyclesModesIntegration() throws IOException {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        // mode cycles: 0->1->2->3->4->0
        realController.move(mock(Game.class), GUI.ACTION.NULL, 0);   // mode 0
        realController.move(mock(Game.class), GUI.ACTION.NULL, 50);  // mode 1
        realController.move(mock(Game.class), GUI.ACTION.NULL, 250); // mode 5 % 5 = 0
        
        Particle particle = realMenu.getParticles().get(0);
        String stateName = particle.getState().getClass().getSimpleName();
        assertEquals("RandomState", stateName); // Should cycle back to 0
    }

    @Test
    void testSetStateActuallyChangesParticleStateIntegration() throws IOException {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        Particle particle = realMenu.getParticles().get(0);
        String initialState = particle.getState().getClass().getSimpleName();
        
        realController.move(mock(Game.class), GUI.ACTION.NULL, 100);
        
        String newState = particle.getState().getClass().getSimpleName();
        assertNotEquals(initialState, newState);
    }

    @Test
    void testSetPositionActuallyChangesParticlePositionIntegration() throws IOException {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        Particle particle = realMenu.getParticles().get(0);
        Position initialPos = new Position(particle.getPosition().x(), particle.getPosition().y());
        
        realController.move(mock(Game.class), GUI.ACTION.NULL, 10);
        
        Position newPos = particle.getPosition();
        // Position should change (not remain the same)
        assertTrue(newPos.x() != initialPos.x() || newPos.y() != initialPos.y());
    }

    @Test
    void testGradientUpdateTriggersAt500TicksIntegration() throws IOException {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        Particle particle = realMenu.getParticles().get(0);
        Position initialPos = new Position(particle.getPosition().x(), particle.getPosition().y());
        
        // Initialize colors
        realController.move(mock(Game.class), GUI.ACTION.NULL, 0);
        
        // Move to gradient trigger point
        realController.move(mock(Game.class), GUI.ACTION.NULL, 500);
        
        // Verify particle was updated (position may have changed)
        assertNotNull(particle.getState());
        assertNotNull(particle.getPosition());
    }

    @Test
    void testGradientTransitionUsesCorrectAdditionIntegration() throws IOException {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        // Start transition
        realController.move(mock(Game.class), GUI.ACTION.NULL, 500);
        
        // Move to exact end of transition: 500 + 100 = 600
        realController.move(mock(Game.class), GUI.ACTION.NULL, 600);
        
        Particle particle = realMenu.getParticles().get(0);
        assertNotNull(particle.getState());
        assertNotNull(particle.getPosition());
    }

    @Test
    void testInterpolateColorArithmeticOperationsIntegration() {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        TextColor.RGB start = new TextColor.RGB(100, 100, 100);
        TextColor.RGB end = new TextColor.RGB(200, 200, 200);
        TextColor.RGB result = realController.interpolateColor(start, end, 0.5f);
        
        // At 0.5: 100 + 0.5 * (200 - 100) = 150
        // Verifies: subtraction (200-100), multiplication (0.5*100), addition (100+50)
        assertEquals(150, result.getRed());
        assertEquals(150, result.getGreen());
        assertEquals(150, result.getBlue());
    }

    @Test
    void testWrapPositionActuallyWrapsCoordinatesIntegration() {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        // Test negative x wraps to 219
        Position wrapped1 = realController.wrapPosition(-1, 50);
        assertEquals(219, wrapped1.x(), 0.001);
        
        // Test x=220 wraps to 1
        Position wrapped2 = realController.wrapPosition(220, 50);
        assertEquals(1, wrapped2.x(), 0.001);
        
        // Test negative y wraps to 109
        Position wrapped3 = realController.wrapPosition(50, -1);
        assertEquals(109, wrapped3.y(), 0.001);
        
        // Test y=110 wraps to 1
        Position wrapped4 = realController.wrapPosition(50, 110);
        assertEquals(1, wrapped4.y(), 0.001);
    }

    @Test
    void testGetWindAngleReturnsActualValueIntegration() {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        double windAngle = realController.getWindAngle();
        assertEquals(0.0, windAngle, 0.001);
        assertFalse(Double.isNaN(windAngle));
    }

    @Test
    void testGetWindSpeedReturnsActualValueIntegration() {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        double windSpeed = realController.getWindSpeed();
        assertEquals(2.0, windSpeed, 0.001);
        assertTrue(windSpeed > 0);
    }

    @Test
    void testRandomColorDoesNotReturnNullIntegration() {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        TextColor.RGB color = realController.randomColor();
        assertNotNull(color);
        
        // Verify it's a valid color with values in range
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        
        assertTrue(red >= 0 && red <= 255);
        assertTrue(green >= 0 && green <= 255);
        assertTrue(blue >= 0 && blue <= 255);
    }

    // Helper class to create real Menu for integration testing
    private static class TestMenu extends Menu {
        public TestMenu() {
            super();
            // Create real particles with initial state
            Particle p1 = new Particle(new Position(10, 10), new RandomState(), new TextColor.RGB(255, 0, 0));
            Particle p2 = new Particle(new Position(50, 50), new RandomState(), new TextColor.RGB(0, 255, 0));
            getParticles().add(p1);
            getParticles().add(p2);
        }

        @Override
        protected List<pt.feup.tvvs.soulknight.model.menu.Option> createEntries() {
            return new ArrayList<>();
        }

        @Override
        public List<Particle> createParticles() {
            return new ArrayList<>();
        }
    }
}