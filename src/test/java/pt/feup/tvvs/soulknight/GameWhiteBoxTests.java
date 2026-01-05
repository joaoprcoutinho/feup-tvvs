package pt.feup.tvvs.soulknight;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.state.State;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class GameWhiteBoxTests {

    private Game game;

    @BeforeEach
    void setUp() {
        game = mock(Game.class);
        State<?> mockState = mock(State.class);
        game.setState(mockState);
    }

    @Test
    public void testMain() {
        assertDoesNotThrow(() -> game.main(new String[]{}));
    }
}