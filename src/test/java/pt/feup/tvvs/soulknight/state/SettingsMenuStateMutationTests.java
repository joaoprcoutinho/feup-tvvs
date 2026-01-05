package pt.feup.tvvs.soulknight.state;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import pt.feup.tvvs.soulknight.model.menu.SettingsMenu;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;
import pt.feup.tvvs.soulknight.controller.Controller;
import pt.feup.tvvs.soulknight.controller.menu.SettingsMenuController;
import pt.feup.tvvs.soulknight.view.states.ScreenViewer;
import pt.feup.tvvs.soulknight.view.states.MenuViewer;
import java.io.IOException;
import java.lang.reflect.Field;

public class SettingsMenuStateMutationTests {
    private SettingsMenu settingsMenu;
    private SpriteLoader spriteLoader;

    @BeforeEach
    void setUp() throws IOException {
        settingsMenu = new SettingsMenu();
        spriteLoader = mock(SpriteLoader.class);
    }

    @Test
    void constructor_shouldCreateSettingsMenuState() throws IOException {
        SettingsMenuState state = new SettingsMenuState(settingsMenu, spriteLoader);
        assertNotNull(state, "SettingsMenuState should be created");
    }

    @Test
    void getModel_shouldReturnSettingsMenu() throws IOException {
        SettingsMenuState state = new SettingsMenuState(settingsMenu, spriteLoader);
        assertEquals(settingsMenu, state.getModel(), "Should return the same SettingsMenu instance");
    }

    @Test
    void getModel_shouldNotReturnNull() throws IOException {
        SettingsMenuState state = new SettingsMenuState(settingsMenu, spriteLoader);
        assertNotNull(state.getModel(), "Model should not be null");
    }

    @Test
    void constructor_shouldNotThrowException() {
        assertDoesNotThrow(() -> new SettingsMenuState(settingsMenu, spriteLoader),
            "Constructor should not throw exception");
    }

    // Test createController returns SettingsMenuController (not null)
    @Test
    void testCreateControllerReturnsNonNull() throws Exception {
        SettingsMenuState state = new SettingsMenuState(settingsMenu, spriteLoader);
        
        Field controllerField = State.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        Controller<?> controller = (Controller<?>) controllerField.get(state);
        
        assertNotNull(controller, "Controller should not be null");
        assertTrue(controller instanceof SettingsMenuController, "Controller should be SettingsMenuController");
    }

    // Test createScreenViewer returns MenuViewer (not null)
    @Test
    void testCreateScreenViewerReturnsNonNull() throws Exception {
        SettingsMenuState state = new SettingsMenuState(settingsMenu, spriteLoader);
        
        Field viewerField = State.class.getDeclaredField("screenViewer");
        viewerField.setAccessible(true);
        ScreenViewer<?> viewer = (ScreenViewer<?>) viewerField.get(state);
        
        assertNotNull(viewer, "ScreenViewer should not be null");
        assertTrue(viewer instanceof MenuViewer, "ScreenViewer should be MenuViewer");
    }

    // Test model is stored correctly
    @Test
    void testModelIsStoredCorrectly() throws IOException {
        SettingsMenu menu1 = new SettingsMenu();
        SettingsMenu menu2 = new SettingsMenu();
        
        SettingsMenuState state1 = new SettingsMenuState(menu1, spriteLoader);
        SettingsMenuState state2 = new SettingsMenuState(menu2, spriteLoader);
        
        assertSame(menu1, state1.getModel());
        assertSame(menu2, state2.getModel());
        assertNotSame(state1.getModel(), state2.getModel());
    }

    // Test multiple state instances are independent
    @Test
    void testMultipleStatesAreIndependent() throws IOException {
        SettingsMenuState state1 = new SettingsMenuState(settingsMenu, spriteLoader);
        SettingsMenuState state2 = new SettingsMenuState(new SettingsMenu(), spriteLoader);
        
        assertNotSame(state1, state2);
    }

    // Test constructor uses the provided model
    @Test
    void testConstructorUsesProvidedModel() throws IOException {
        SettingsMenu customMenu = new SettingsMenu();
        SettingsMenuState state = new SettingsMenuState(customMenu, spriteLoader);
        
        assertSame(customMenu, state.getModel());
    }
}