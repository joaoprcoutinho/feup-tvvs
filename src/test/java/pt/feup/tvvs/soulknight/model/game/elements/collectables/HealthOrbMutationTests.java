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
    public void constructor_shouldSetHealthValue() {
        assertEquals(25, healthOrb.getHealth());
    }

    @Test
    public void constructor_shouldSetSymbol() {
        assertEquals('h', healthOrb.getChar());
    }

    @Test
    public void setHealth_shouldUpdateHealthValue() {
        healthOrb.setHealth(50);
        assertEquals(50, healthOrb.getHealth());
    }

    @Test
    public void setHealth_shouldHandleZero() {
        healthOrb.setHealth(0);
        assertEquals(0, healthOrb.getHealth());
    }

    @Test
    public void setHealth_shouldHandleNegative() {
        healthOrb.setHealth(-10);
        assertEquals(-10, healthOrb.getHealth());
    }

    @Test
    public void benefit_shouldAddHealthToKnight() {
        when(knight.getHP()).thenReturn(50);
        healthOrb.benefit(knight);
        verify(knight).setHP(75);
    }

    @Test
    public void benefit_shouldAddCorrectAmountWhenHealthChanged() {
        healthOrb.setHealth(30);
        when(knight.getHP()).thenReturn(40);
        healthOrb.benefit(knight);
        verify(knight).setHP(70);
    }

    @Test
    public void benefit_shouldHandleZeroHealth() {
        healthOrb.setHealth(0);
        when(knight.getHP()).thenReturn(50);
        healthOrb.benefit(knight);
        verify(knight).setHP(50);
    }

    @Test
    public void benefit_shouldNotSubtractHealth() {
        when(knight.getHP()).thenReturn(100);
        healthOrb.benefit(knight);
        verify(knight).setHP(125);
    }
}