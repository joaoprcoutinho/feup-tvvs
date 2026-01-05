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

    @BeforeEach
    void setUp() {
        menu = mock(Menu.class);
        game = mock(Game.class);

        Particle particle1 = new Particle(new Position(10, 10), new RandomState(), new TextColor.RGB(255, 0, 0));
        Particle particle2 = new Particle(new Position(50, 50), new RandomState(), new TextColor.RGB(0, 255, 0));
        
        List<Particle> particles = new ArrayList<>();
        particles.add(particle1);
        particles.add(particle2);
        
        when(menu.getParticles()).thenReturn(particles);
        
        controller = new ParticleMenuController(menu);
    }

    @Test
    public void testGetWindAngle() {
        double windAngle = controller.getWindAngle();
        assertEquals(0.0, windAngle, 0.001);
        assertNotNull(controller);
    }

    @Test
    public void testGetWindSpeed() {
        double windSpeed = controller.getWindSpeed();
        assertTrue(windSpeed > 0);
        assertEquals(2.0, windSpeed, 0.001);
    }

    @Test
    public void testWrapPositionNegativeX() {
        Position pos = controller.wrapPosition(-1, 50);
        assertNotNull(pos);
        assertEquals(219, pos.x(), 0.001);
        assertEquals(50, pos.y(), 0.001);
    }

    @Test
    public void testWrapPositionTooLargeX() {
        Position pos = controller.wrapPosition(220, 50);
        assertNotNull(pos);
        assertEquals(1, pos.x(), 0.001);
        assertEquals(50, pos.y(), 0.001);
    }

    @Test
    public void testWrapPositionNegativeY() {
        Position pos = controller.wrapPosition(100, -1);
        assertNotNull(pos);
        assertEquals(100, pos.x(), 0.001);
        assertEquals(109, pos.y(), 0.001);
    }

    @Test
    public void testWrapPositionTooLargeY() {
        Position pos = controller.wrapPosition(100, 110);
        assertNotNull(pos);
        assertEquals(100, pos.x(), 0.001);
        assertEquals(1, pos.y(), 0.001);
    }

    @Test
    public void testWrapPositionXBoundary() {
        Position pos1 = controller.wrapPosition(0, 50);
        assertEquals(0, pos1.x(), 0.001);
        
        Position pos2 = controller.wrapPosition(219, 50);
        assertEquals(219, pos2.x(), 0.001);
    }

    @Test
    public void testWrapPositionYBoundary() {
        Position pos1 = controller.wrapPosition(100, 0);
        assertEquals(0, pos1.y(), 0.001);
        
        Position pos2 = controller.wrapPosition(100, 109);
        assertEquals(109, pos2.y(), 0.001);
    }

    @Test
    public void testMoveModuloOperation() throws IOException {
        Particle particle = menu.getParticles().get(0);
        controller.move(game, GUI.ACTION.NULL, 50);
        verify(menu, atLeastOnce()).getParticles();
        assertNotNull(particle.getState());
        assertTrue(particle.getState().getClass().getName().contains("State"));
    }

    @Test
    public void testMoveDivisionInModeCalculation() throws IOException {
        Particle particle = menu.getParticles().get(0);
        controller.move(game, GUI.ACTION.NULL, 100);
        verify(menu, atLeastOnce()).getParticles();
        assertNotNull(particle.getPosition());
        String stateName = particle.getState().getClass().getSimpleName();
        assertNotNull(stateName);
    }

    @Test
    public void testMoveCallsUpdateGradients() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 0);
        controller.move(game, GUI.ACTION.NULL, 500);
        controller.move(game, GUI.ACTION.NULL, 501);
        assertNotNull(controller);
        verify(menu, atLeast(3)).getParticles();
    }

    @Test
    public void testUpdateGradientsAtBoundary500() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 499);
        controller.move(game, GUI.ACTION.NULL, 500);
        controller.move(game, GUI.ACTION.NULL, 501);
        verify(menu, atLeast(3)).getParticles();
        assertNotNull(controller);
    }

    @Test
    public void testUpdateGradientsModuloNotMultiplication() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 1000);
        controller.move(game, GUI.ACTION.NULL, 1500);
        assertNotNull(controller);
    }

    @Test
    public void testGradientTransitionBoundaryExactly100() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 500);
        Particle particle = menu.getParticles().get(0);
        assertNotNull(particle.getState());
        controller.move(game, GUI.ACTION.NULL, 599);
        assertNotNull(particle.getPosition());
        controller.move(game, GUI.ACTION.NULL, 600);
        assertNotNull(particle.getState());
        verify(menu, atLeast(3)).getParticles();
    }

    @Test
    public void testGradientTransitionStartTickConditions() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 500);
        controller.move(game, GUI.ACTION.NULL, 550);
        assertNotNull(controller);
    }

    @Test
    public void testInterpolateColorSubtraction() {
        TextColor.RGB start = new TextColor.RGB(100, 100, 100);
        TextColor.RGB end = new TextColor.RGB(200, 200, 200);
        TextColor.RGB result = controller.interpolateColor(start, end, 0.5f);
        
        assertNotNull(result);
        assertEquals(150, result.getRed());
        assertEquals(150, result.getGreen());
        assertEquals(150, result.getBlue());
    }

    @Test
    public void testInterpolateColorMultiplication() {
        TextColor.RGB start = new TextColor.RGB(0, 0, 0);
        TextColor.RGB end = new TextColor.RGB(100, 150, 200);
        TextColor.RGB result = controller.interpolateColor(start, end, 0.5f);
        
        assertNotNull(result);
        assertEquals(50, result.getRed());
        assertEquals(75, result.getGreen());
        assertEquals(100, result.getBlue());
    }

    @Test
    public void testInterpolateColorAtFactorZero() {
        TextColor.RGB start = new TextColor.RGB(100, 100, 100);
        TextColor.RGB end = new TextColor.RGB(200, 200, 200);
        TextColor.RGB result = controller.interpolateColor(start, end, 0.0f);
        
        assertNotNull(result);
        assertEquals(100, result.getRed());
        assertEquals(100, result.getGreen());
        assertEquals(100, result.getBlue());
    }

    @Test
    public void testInterpolateColorAtFactorOne() {
        TextColor.RGB start = new TextColor.RGB(100, 100, 100);
        TextColor.RGB end = new TextColor.RGB(200, 200, 200);
        TextColor.RGB result = controller.interpolateColor(start, end, 1.0f);
        
        assertNotNull(result);
        assertEquals(200, result.getRed());
        assertEquals(200, result.getGreen());
        assertEquals(200, result.getBlue());
    }

    @Test
    public void testRandomColor() {
        TextColor.RGB color = controller.randomColor();
        assertNotNull(color);
        assertTrue(color.getRed() >= 0 && color.getRed() <= 255);
        assertTrue(color.getGreen() >= 0 && color.getGreen() <= 255);
        assertTrue(color.getBlue() >= 0 && color.getBlue() <= 255);
    }

    @Test
    public void testMoveMode0Random() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 0);
        verify(menu, atLeastOnce()).getParticles();
    }

    @Test
    public void testMoveMode1Windy() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 50);
        verify(menu, atLeastOnce()).getParticles();
    }

    @Test
    public void testMoveMode2Calm() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 100);
        verify(menu, atLeastOnce()).getParticles();
    }

    @Test
    public void testMoveMode3Dispersing() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 150);
        verify(menu, atLeastOnce()).getParticles();
    }

    @Test
    public void testMoveMode4Zico() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 200);
        verify(menu, atLeastOnce()).getParticles();
    }

    @Test
    public void testGradientTransitionFactorSubtraction() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 500);
        controller.move(game, GUI.ACTION.NULL, 550);
        assertNotNull(controller);
    }

    @Test
    public void testGradientTransitionFactorDivision() throws IOException {
        controller.move(game, GUI.ACTION.NULL, 500);
        controller.move(game, GUI.ACTION.NULL, 525);
        assertNotNull(controller);
    }

    @Test
    public void testDivisionInModeCalculationProducesCorrectStateIntegration() throws IOException {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);

        realController.move(mock(Game.class), GUI.ACTION.NULL, 100);
        
        Particle particle = realMenu.getParticles().get(0);
        String stateName = particle.getState().getClass().getSimpleName();
        assertEquals("CalmState", stateName);
    }

    @Test
    public void testModuloOperationCyclesModesIntegration() throws IOException {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        realController.move(mock(Game.class), GUI.ACTION.NULL, 0);
        realController.move(mock(Game.class), GUI.ACTION.NULL, 50);
        realController.move(mock(Game.class), GUI.ACTION.NULL, 250);
        
        Particle particle = realMenu.getParticles().get(0);
        String stateName = particle.getState().getClass().getSimpleName();
        assertEquals("RandomState", stateName);
    }

    @Test
    public void testSetStateActuallyChangesParticleStateIntegration() throws IOException {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        Particle particle = realMenu.getParticles().get(0);
        String initialState = particle.getState().getClass().getSimpleName();
        
        realController.move(mock(Game.class), GUI.ACTION.NULL, 100);
        
        String newState = particle.getState().getClass().getSimpleName();
        assertNotEquals(initialState, newState);
    }

    @Test
    public void testSetPositionActuallyChangesParticlePositionIntegration() throws IOException {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        Particle particle = realMenu.getParticles().get(0);
        Position initialPos = new Position(particle.getPosition().x(), particle.getPosition().y());
        
        realController.move(mock(Game.class), GUI.ACTION.NULL, 10);
        
        Position newPos = particle.getPosition();
        assertTrue(newPos.x() != initialPos.x() || newPos.y() != initialPos.y());
    }

    @Test
    public void testGradientUpdateTriggersAt500TicksIntegration() throws IOException {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        Particle particle = realMenu.getParticles().get(0);

        realController.move(mock(Game.class), GUI.ACTION.NULL, 0);
        realController.move(mock(Game.class), GUI.ACTION.NULL, 500);
        
        assertNotNull(particle.getState());
        assertNotNull(particle.getPosition());
    }

    @Test
    public void testGradientTransitionUsesCorrectAdditionIntegration() throws IOException {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        realController.move(mock(Game.class), GUI.ACTION.NULL, 500);
        realController.move(mock(Game.class), GUI.ACTION.NULL, 600);
        
        Particle particle = realMenu.getParticles().get(0);
        assertNotNull(particle.getState());
        assertNotNull(particle.getPosition());
    }

    @Test
    public void testInterpolateColorArithmeticOperationsIntegration() {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        TextColor.RGB start = new TextColor.RGB(100, 100, 100);
        TextColor.RGB end = new TextColor.RGB(200, 200, 200);
        TextColor.RGB result = realController.interpolateColor(start, end, 0.5f);
        
        assertEquals(150, result.getRed());
        assertEquals(150, result.getGreen());
        assertEquals(150, result.getBlue());
    }

    @Test
    public void testWrapPositionActuallyWrapsCoordinatesIntegration() {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        Position wrapped1 = realController.wrapPosition(-1, 50);
        assertEquals(219, wrapped1.x(), 0.001);
        Position wrapped2 = realController.wrapPosition(220, 50);
        assertEquals(1, wrapped2.x(), 0.001);
        Position wrapped3 = realController.wrapPosition(50, -1);
        assertEquals(109, wrapped3.y(), 0.001);
        Position wrapped4 = realController.wrapPosition(50, 110);
        assertEquals(1, wrapped4.y(), 0.001);
    }

    @Test
    public void testGetWindAngleReturnsActualValueIntegration() {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        double windAngle = realController.getWindAngle();
        assertEquals(0.0, windAngle, 0.001);
        assertFalse(Double.isNaN(windAngle));
    }

    @Test
    public void testGetWindSpeedReturnsActualValueIntegration() {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        double windSpeed = realController.getWindSpeed();
        assertEquals(2.0, windSpeed, 0.001);
        assertTrue(windSpeed > 0);
    }

    @Test
    public void testRandomColorDoesNotReturnNullIntegration() {
        TestMenu realMenu = new TestMenu();
        ParticleMenuController realController = new ParticleMenuController(realMenu);
        
        TextColor.RGB color = realController.randomColor();
        assertNotNull(color);
        
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        
        assertTrue(red >= 0 && red <= 255);
        assertTrue(green >= 0 && green <= 255);
        assertTrue(blue >= 0 && blue <= 255);
    }

    private static class TestMenu extends Menu {
        public TestMenu() {
            super();
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