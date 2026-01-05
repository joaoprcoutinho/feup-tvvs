package pt.feup.tvvs.soulknight.controller.menu;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.Game;
import pt.feup.tvvs.soulknight.model.menu.MainMenu;

import static org.mockito.Mockito.*;

public class MainMenuControllerWhiteBoxTests {

    private MainMenuController controller;
    private Game game;

    @BeforeEach
    void setup() {
        controller = new MainMenuController(mock(MainMenu.class), mock(ParticleMenuController.class), mock(OptionController.class));
        game = mock(Game.class);
    }

    @Test
    public void testOnQuitSetsGameStateToNull() {
        controller.onQuit(game);
        verify(game).setState(null);
    }
}