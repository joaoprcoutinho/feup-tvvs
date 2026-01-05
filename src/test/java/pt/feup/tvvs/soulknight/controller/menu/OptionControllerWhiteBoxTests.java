package pt.feup.tvvs.soulknight.controller.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.Game;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.gui.RescalableGUI;
import pt.feup.tvvs.soulknight.model.menu.Menu;
import pt.feup.tvvs.soulknight.model.menu.Option;
import pt.feup.tvvs.soulknight.state.GameState;
import pt.feup.tvvs.soulknight.state.MainMenuState;
import pt.feup.tvvs.soulknight.state.SettingsMenuState;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import static org.mockito.Mockito.*;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class OptionControllerWhiteBoxTests {
    private OptionController controller;
    private Menu menu;
    private Game game;

    @BeforeEach
    void setup() {
        menu = mock(Menu.class);
        controller = new OptionController(menu);
        game = mock(Game.class);

        when(game.getSpriteLoader()).thenReturn(mock(SpriteLoader.class));
    }

    @Test
    public void testMoveStartGameSelect() throws IOException, URISyntaxException, FontFormatException {
        when(menu.getCurrentOption()).thenReturn(mock(Option.class));
        when(menu.getCurrentOption().getType()).thenReturn(Option.Type.START_GAME);

        controller.move(game, GUI.ACTION.SELECT, 0);
        verify(game).setState(any(GameState.class));
    }

    @Test
    public void testMoveStartGameNone() throws IOException, URISyntaxException, FontFormatException {
        when(menu.getCurrentOption()).thenReturn(mock(Option.class));
        when(menu.getCurrentOption().getType()).thenReturn(Option.Type.START_GAME);

        controller.move(game, GUI.ACTION.LEFT, 0);
        verify(game, never()).setState(any());
    }

    @Test
    public void testMoveSettingsSelect() throws IOException, URISyntaxException, FontFormatException {
        when(menu.getCurrentOption()).thenReturn(mock(Option.class));
        when(menu.getCurrentOption().getType()).thenReturn(Option.Type.SETTINGS);

        controller.move(game, GUI.ACTION.SELECT, 0);
        verify(game).setState(any(SettingsMenuState.class));
    }

    @Test
    public void testMoveSettingsNone() throws IOException, URISyntaxException, FontFormatException {
        when(menu.getCurrentOption()).thenReturn(mock(Option.class));
        when(menu.getCurrentOption().getType()).thenReturn(Option.Type.SETTINGS);

        controller.move(game, GUI.ACTION.LEFT, 0);
        verify(game, never()).setState(any());
    }

    @Test
    public void testMoveExitSelect() throws IOException, URISyntaxException, FontFormatException {
        when(menu.getCurrentOption()).thenReturn(mock(Option.class));
        when(menu.getCurrentOption().getType()).thenReturn(Option.Type.EXIT);

        controller.move(game, GUI.ACTION.SELECT, 0);
        verify(game).setState(null);
    }

    @Test
    public void testMoveExitNone() throws IOException, URISyntaxException, FontFormatException {
        when(menu.getCurrentOption()).thenReturn(mock(Option.class));
        when(menu.getCurrentOption().getType()).thenReturn(Option.Type.EXIT);

        controller.move(game, GUI.ACTION.LEFT, 0);
        verify(game, never()).setState(any());
    }

    @Test
    public void testMoveMainMenuSelect() throws IOException, URISyntaxException, FontFormatException {
        when(menu.getCurrentOption()).thenReturn(mock(Option.class));
        when(menu.getCurrentOption().getType()).thenReturn(Option.Type.TO_MAIN_MENU);

        controller.move(game, GUI.ACTION.SELECT, 0);
        verify(game).setState(any(MainMenuState.class));
    }

    @Test
    public void testMoveMainMenuNone() throws IOException, URISyntaxException, FontFormatException {
        when(menu.getCurrentOption()).thenReturn(mock(Option.class));
        when(menu.getCurrentOption().getType()).thenReturn(Option.Type.TO_MAIN_MENU);

        controller.move(game, GUI.ACTION.LEFT, 0);
        verify(game, never()).setState(any());
    }


    @Test
    public void testMoveSetsHigherResolution() throws IOException, URISyntaxException, FontFormatException {
        when(menu.getCurrentOption()).thenReturn(mock(Option.class));
        when(menu.getCurrentOption().getType()).thenReturn(Option.Type.RESOLUTION);

        when(game.getResolution()).thenReturn(null);

        controller.move(game, GUI.ACTION.RIGHT, 0);
        verify(game).setResolution(any(RescalableGUI.ResolutionScale.class));
    }

    @Test
    public void testMoveSetsHigherResolutionMax() throws IOException, URISyntaxException, FontFormatException {
        when(menu.getCurrentOption()).thenReturn(mock(Option.class));
        when(menu.getCurrentOption().getType()).thenReturn(Option.Type.RESOLUTION);

        when(game.getResolution()).thenReturn(RescalableGUI.ResolutionScale.WXGA);

        controller.move(game, GUI.ACTION.RIGHT, 0);
        verify(game, never()).setResolution(any());
    }

    @Test
    public void testMoveSetsLowerResolution() throws IOException, URISyntaxException, FontFormatException {
        when(menu.getCurrentOption()).thenReturn(mock(Option.class));
        when(menu.getCurrentOption().getType()).thenReturn(Option.Type.RESOLUTION);

        when(game.getResolution()).thenReturn(RescalableGUI.ResolutionScale.WXGA);

        controller.move(game, GUI.ACTION.LEFT, 0);
        verify(game).setResolution(null);
    }

    @Test
    public void testMoveSetsLowerResolutionLowest() throws IOException, URISyntaxException, FontFormatException {
        when(menu.getCurrentOption()).thenReturn(mock(Option.class));
        when(menu.getCurrentOption().getType()).thenReturn(Option.Type.RESOLUTION);

        when(game.getResolution()).thenReturn(null);

        controller.move(game, GUI.ACTION.LEFT, 0);
        verify(game, never()).setResolution(any());
    }

    @Test
    public void testMoveSetsResolutionNone() throws IOException, URISyntaxException, FontFormatException {
        when(menu.getCurrentOption()).thenReturn(mock(Option.class));
        when(menu.getCurrentOption().getType()).thenReturn(Option.Type.RESOLUTION);

        controller.move(game, GUI.ACTION.DOWN, 0);
        verify(game, never()).setResolution(any());
    }
}