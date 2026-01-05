package pt.feup.tvvs.soulknight.state;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import pt.feup.tvvs.soulknight.model.menu.MainMenu;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;
import pt.feup.tvvs.soulknight.controller.Controller;
import pt.feup.tvvs.soulknight.controller.menu.MainMenuController;
import pt.feup.tvvs.soulknight.view.states.ScreenViewer;
import pt.feup.tvvs.soulknight.view.states.MenuViewer;
import java.io.IOException;
import java.lang.reflect.Field;

public class MainMenuStateMutationTests {
    private MainMenu mainMenu;
    private SpriteLoader spriteLoader;

    @BeforeEach
    void setUp() throws IOException {
        mainMenu = new MainMenu();
        spriteLoader = mock(SpriteLoader.class);
    }

    @Test
    void constructor_shouldCreateMainMenuState() throws IOException {
        MainMenuState state = new MainMenuState(mainMenu, spriteLoader);
        assertNotNull(state, "MainMenuState should be created");
    }

    @Test
    void getModel_shouldReturnMainMenu() throws IOException {
        MainMenuState state = new MainMenuState(mainMenu, spriteLoader);
        assertEquals(mainMenu, state.getModel(), "Should return the same MainMenu instance");
    }

    @Test
    void getModel_shouldNotReturnNull() throws IOException {
        MainMenuState state = new MainMenuState(mainMenu, spriteLoader);
        assertNotNull(state.getModel(), "Model should not be null");
    }

    @Test
    void constructor_shouldNotThrowException() {
        assertDoesNotThrow(() -> new MainMenuState(mainMenu, spriteLoader),
            "Constructor should not throw exception");
    }

    // Test createController returns MainMenuController (not null)
    @Test
    void testCreateControllerReturnsNonNull() throws Exception {
        MainMenuState state = new MainMenuState(mainMenu, spriteLoader);
        
        // Use reflection to access the private controller field
        Field controllerField = State.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        Controller<?> controller = (Controller<?>) controllerField.get(state);
        
        assertNotNull(controller, "Controller should not be null");
        assertTrue(controller instanceof MainMenuController, "Controller should be MainMenuController");
    }

    // Test createScreenViewer returns MenuViewer (not null)
    @Test
    void testCreateScreenViewerReturnsNonNull() throws Exception {
        MainMenuState state = new MainMenuState(mainMenu, spriteLoader);
        
        // Use reflection to access the private screenViewer field
        Field viewerField = State.class.getDeclaredField("screenViewer");
        viewerField.setAccessible(true);
        ScreenViewer<?> viewer = (ScreenViewer<?>) viewerField.get(state);
        
        assertNotNull(viewer, "ScreenViewer should not be null");
        assertTrue(viewer instanceof MenuViewer, "ScreenViewer should be MenuViewer");
    }

    // Test model is stored correctly
    @Test
    void testModelIsStoredCorrectly() throws IOException {
        MainMenu menu1 = new MainMenu();
        MainMenu menu2 = new MainMenu();
        
        MainMenuState state1 = new MainMenuState(menu1, spriteLoader);
        MainMenuState state2 = new MainMenuState(menu2, spriteLoader);
        
        assertSame(menu1, state1.getModel());
        assertSame(menu2, state2.getModel());
        assertNotSame(state1.getModel(), state2.getModel());
    }

    // Test multiple state instances are independent
    @Test
    void testMultipleStatesAreIndependent() throws IOException {
        MainMenuState state1 = new MainMenuState(mainMenu, spriteLoader);
        MainMenuState state2 = new MainMenuState(new MainMenu(), spriteLoader);
        
        assertNotSame(state1, state2);
    }

    // Test constructor uses the provided model
    @Test
    void testConstructorUsesProvidedModel() throws IOException {
        MainMenu customMenu = new MainMenu();
        MainMenuState state = new MainMenuState(customMenu, spriteLoader);
        
        assertSame(customMenu, state.getModel());
    }
}