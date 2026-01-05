package pt.feup.tvvs.soulknight.model.game.elements;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpikeMutationTests {
    
    @Test
    void getCharacter_shouldReturnGroundSpike() {
        Spike spike = new Spike(10, 20, '^');
        assertEquals('^', spike.getCharacter());
    }

    @Test
    void getCharacter_shouldReturnBrown1Spike() {
        Spike spike = new Spike(15, 25, '+');
        assertEquals('+', spike.getCharacter());
    }

    @Test
    void getCharacter_shouldReturnBrown2Spike() {
        Spike spike = new Spike(30, 40, '-');
        assertEquals('-', spike.getCharacter());
    }

    @Test
    void constructor_shouldSetPosition() {
        Spike spike = new Spike(100, 200, '^');
        assertEquals(100, spike.getPosition().x());
        assertEquals(200, spike.getPosition().y());
    }
}