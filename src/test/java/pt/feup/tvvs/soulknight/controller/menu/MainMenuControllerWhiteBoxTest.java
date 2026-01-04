package pt.feup.tvvs.soulknight.controller.menu;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.Game;
import pt.feup.tvvs.soulknight.controller.credits.CreditsController;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.credits.Credits;
import pt.feup.tvvs.soulknight.model.menu.MainMenu;
import pt.feup.tvvs.soulknight.state.MainMenuState;
import pt.feup.tvvs.soulknight.view.sprites.Sprite;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import static org.mockito.Mockito.*;
import java.io.IOException;

public class MainMenuControllerWhiteBoxTest {

    private MainMenuController controller;
    private Game game;

    @BeforeEach
    void setup() {
        controller = new MainMenuController(mock(MainMenu.class), mock(ParticleMenuController.class), mock(OptionController.class));
        game = mock(Game.class);
    }

    @Test
    void testOnQuitSetsGameStateToNull() {
        controller.onQuit(game);
        verify(game).setState(null);
    }
}