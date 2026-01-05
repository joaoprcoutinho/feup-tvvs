package pt.feup.tvvs.soulknight.model.game.elements.collectables;
            
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import java.io.IOException;

public class HealthOrbMutationTests {
    private HealthOrb healthOrb;
    private Knight knight;

    @BeforeEach
    void setUp() {
        healthOrb = new HealthOrb(10, 20, 25, 'h');
        knight = mock(Knight.class);
    }

    @Test
    void constructor_shouldSetHealthValue() {
        assertEquals(25, healthOrb.getHealth());
    }

    @Test
    void constructor_shouldSetSymbol() {
        assertEquals('h', healthOrb.getChar());
    }

    @Test
    void setHealth_shouldUpdateHealthValue() {
        healthOrb.setHealth(50);
        assertEquals(50, healthOrb.getHealth());
    }

    @Test
    void setHealth_shouldHandleZero() {
        healthOrb.setHealth(0);
        assertEquals(0, healthOrb.getHealth());
    }

    @Test
    void setHealth_shouldHandleNegative() {
        healthOrb.setHealth(-10);
        assertEquals(-10, healthOrb.getHealth());
    }

    @Test
    void benefit_shouldAddHealthToKnight() {
        when(knight.getHP()).thenReturn(50);
        healthOrb.benefit(knight);
        verify(knight).setHP(75); // 50 + 25
    }

    @Test
    void benefit_shouldAddCorrectAmountWhenHealthChanged() {
        healthOrb.setHealth(30);
        when(knight.getHP()).thenReturn(40);
        healthOrb.benefit(knight);
        verify(knight).setHP(70); // 40 + 30
    }

    @Test
    void benefit_shouldHandleZeroHealth() {
        healthOrb.setHealth(0);
        when(knight.getHP()).thenReturn(50);
        healthOrb.benefit(knight);
        verify(knight).setHP(50); // 50 + 0
    }

    @Test
    void benefit_shouldNotSubtractHealth() {
        when(knight.getHP()).thenReturn(100);
        healthOrb.benefit(knight);
        verify(knight).setHP(125); // Should add, not subtract
    }
}