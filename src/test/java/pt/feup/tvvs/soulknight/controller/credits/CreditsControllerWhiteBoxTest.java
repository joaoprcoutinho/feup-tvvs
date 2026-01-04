package pt.feup.tvvs.soulknight.controller.credits;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.Game;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.credits.Credits;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.model.game.elements.knight.KnightState;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;
import pt.feup.tvvs.soulknight.state.MainMenuState;
import pt.feup.tvvs.soulknight.view.sprites.Sprite;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import static org.mockito.Mockito.*;
import java.io.IOException;

public class CreditsControllerWhiteBoxTest {

    private CreditsController controller;
    private Game game;

    @BeforeEach
    void setup() {
        controller = new CreditsController(mock(Credits.class));
        game = mock(Game.class);
        when(game.getSpriteLoader()).thenReturn(mock(SpriteLoader.class));
    }

    @Test
    void move_doesNothing_whenActionIsNotQuit() throws IOException {
        controller.move(game, GUI.ACTION.LEFT, 0L);
        verify(game, never()).setState(any());
    }

    @Test
    void move_setsMainMenuState_whenActionIsQuit() throws IOException {
        controller.move(game, GUI.ACTION.QUIT, 0L);
        verify(game).setState(any(MainMenuState.class));
    }
}