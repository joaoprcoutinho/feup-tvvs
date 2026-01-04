package pt.feup.tvvs.soulknight;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.LanternaGUI;
import pt.feup.tvvs.soulknight.gui.RescalableGUI;
import pt.feup.tvvs.soulknight.sound.MenuSoundPlayer;
import pt.feup.tvvs.soulknight.state.State;
import pt.feup.tvvs.soulknight.view.sprites.GameSpriteLoader;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class GameWhiteBoxTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = mock(Game.class);
        State<?> mockState = mock(State.class);
        game.setState(mockState);
    }

    //@Test
    void testMain() {
        assertDoesNotThrow(() -> game.main(new String[]{}));
    }
}