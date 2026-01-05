package pt.feup.tvvs.soulknight.state;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import pt.feup.tvvs.soulknight.model.credits.Credits;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;
import pt.feup.tvvs.soulknight.controller.Controller;
import pt.feup.tvvs.soulknight.controller.credits.CreditsController;
import pt.feup.tvvs.soulknight.view.states.ScreenViewer;
import pt.feup.tvvs.soulknight.view.states.CreditsViewer;
import java.io.IOException;
import java.lang.reflect.Field;

public class CreditsStateMutationTests {
    private Credits credits;
    private SpriteLoader spriteLoader;
    private Knight knight;

    @BeforeEach
    void setUp() throws IOException {
        knight = mock(Knight.class);
        when(knight.getEnergy()).thenReturn(100);
        when(knight.getNumberOfDeaths()).thenReturn(5);
        when(knight.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        credits = new Credits(knight);
        spriteLoader = mock(SpriteLoader.class);
    }

    @Test
    void constructor_shouldCreateCreditsState() throws IOException {
        CreditsState state = new CreditsState(credits, spriteLoader);
        assertNotNull(state, "CreditsState should be created");
    }

    @Test
    void getModel_shouldReturnCredits() throws IOException {
        CreditsState state = new CreditsState(credits, spriteLoader);
        assertEquals(credits, state.getModel(), "Should return the same Credits instance");
    }

    @Test
    void getModel_shouldNotReturnNull() throws IOException {
        CreditsState state = new CreditsState(credits, spriteLoader);
        assertNotNull(state.getModel(), "Model should not be null");
    }

    @Test
    void constructor_shouldNotThrowException() {
        assertDoesNotThrow(() -> new CreditsState(credits, spriteLoader),
            "Constructor should not throw exception");
    }

    // Test createController returns CreditsController (not null)
    @Test
    void testCreateControllerReturnsNonNull() throws Exception {
        CreditsState state = new CreditsState(credits, spriteLoader);
        
        Field controllerField = State.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        Controller<?> controller = (Controller<?>) controllerField.get(state);
        
        assertNotNull(controller, "Controller should not be null");
        assertTrue(controller instanceof CreditsController, "Controller should be CreditsController");
    }

    // Test createScreenViewer returns CreditsViewer (not null)
    @Test
    void testCreateScreenViewerReturnsNonNull() throws Exception {
        CreditsState state = new CreditsState(credits, spriteLoader);
        
        Field viewerField = State.class.getDeclaredField("screenViewer");
        viewerField.setAccessible(true);
        ScreenViewer<?> viewer = (ScreenViewer<?>) viewerField.get(state);
        
        assertNotNull(viewer, "ScreenViewer should not be null");
        assertTrue(viewer instanceof CreditsViewer, "ScreenViewer should be CreditsViewer");
    }

    // Test model is stored correctly
    @Test
    void testModelIsStoredCorrectly() throws IOException {
        Knight k2 = mock(Knight.class);
        when(k2.getEnergy()).thenReturn(50);
        when(k2.getNumberOfDeaths()).thenReturn(2);
        when(k2.getBirthTime()).thenReturn(System.currentTimeMillis());
        Credits credits2 = new Credits(k2);
        
        CreditsState state1 = new CreditsState(credits, spriteLoader);
        CreditsState state2 = new CreditsState(credits2, spriteLoader);
        
        assertSame(credits, state1.getModel());
        assertSame(credits2, state2.getModel());
        assertNotSame(state1.getModel(), state2.getModel());
    }

    // Test credits contain knight info
    @Test
    void testCreditsContainKnightInfo() throws IOException {
        CreditsState state = new CreditsState(credits, spriteLoader);
        Credits model = state.getModel();
        
        assertNotNull(model);
        assertEquals(5, model.getDeaths());
        // Score is calculated from energy, verify it's not null/zero
        assertTrue(model.getScore() > 0);
    }
}