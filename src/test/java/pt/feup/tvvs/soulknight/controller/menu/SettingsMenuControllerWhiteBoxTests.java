package pt.feup.tvvs.soulknight.controller.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.Game;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.menu.SettingsMenu;
import pt.feup.tvvs.soulknight.state.MainMenuState;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import static org.mockito.Mockito.*;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class SettingsMenuControllerWhiteBoxTests {

    private SettingsMenuController controller;
    private SettingsMenu menu;
    private ParticleMenuController particleMenuController;
    private OptionController optionController;
    private Game game;

    @BeforeEach
    void setup() {
        menu = mock(SettingsMenu.class);
        particleMenuController = mock(ParticleMenuController.class);
        optionController = mock(OptionController.class);
        controller = new SettingsMenuController(menu, particleMenuController, optionController);
        game = mock(Game.class);
        SpriteLoader spriteLoader = mock(SpriteLoader.class);
        when(game.getSpriteLoader()).thenReturn(spriteLoader);
    }

    @Test
    public void testOnQuit() throws IOException {
        controller.onQuit(game);
        verify(game).setState(any(MainMenuState.class));
    }

    @Test
    public void testMoveUp() throws IOException, URISyntaxException, FontFormatException {
        controller.move(game, GUI.ACTION.UP, 0);
        verify(menu).previousOption();
        verify(particleMenuController).move(game, GUI.ACTION.UP, 0);
    }

    @Test
    public void testMoveDown() throws IOException, URISyntaxException, FontFormatException {
        controller.move(game, GUI.ACTION.DOWN, 0);
        verify(menu).nextOption();
        verify(particleMenuController).move(game, GUI.ACTION.DOWN, 0);
    }

    @Test
    public void testMoveQuit() throws IOException, URISyntaxException, FontFormatException {
        controller.move(game, GUI.ACTION.QUIT, 0);
        verify(particleMenuController).move(game, GUI.ACTION.QUIT, 0);
    }

    @Test
    public void testMoveDefault() throws IOException, URISyntaxException, FontFormatException {
        controller.move(game, GUI.ACTION.LEFT, 0);
        verify(optionController, times(1)).move(game, GUI.ACTION.LEFT, 0);
        verify(particleMenuController).move(game, GUI.ACTION.LEFT, 0);
    }
}