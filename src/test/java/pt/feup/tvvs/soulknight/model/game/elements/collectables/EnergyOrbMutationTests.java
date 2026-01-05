package pt.feup.tvvs.soulknight.model.game.elements.collectables;
            
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import java.io.IOException;

public class EnergyOrbMutationTests {
    private EnergyOrb energyOrb;
    private Knight knight;

    @BeforeEach
    void setUp() {
        energyOrb = new EnergyOrb(10, 20, 10, 'e');
        knight = mock(Knight.class);
    }

    @Test
    void constructor_shouldSetEnergyValue() {
        assertEquals(10, energyOrb.getEnergy());
    }

    @Test
    void constructor_shouldSetSymbol() {
        assertEquals('e', energyOrb.getChar());
    }

    @Test
    void setEnergy_shouldUpdateEnergyValue() {
        energyOrb.setEnergy(20);
        assertEquals(20, energyOrb.getEnergy());
    }

    @Test
    void setEnergy_shouldHandleZero() {
        energyOrb.setEnergy(0);
        assertEquals(0, energyOrb.getEnergy());
    }

    @Test
    void setEnergy_shouldHandleNegative() {
        energyOrb.setEnergy(-5);
        assertEquals(-5, energyOrb.getEnergy());
    }

    @Test
    void benefit_shouldAddEnergyToKnight() {
        when(knight.getEnergy()).thenReturn(50);
        energyOrb.benefit(knight);
        verify(knight).setEnergy(60); // 50 + 10
    }

    @Test
    void benefit_shouldAddCorrectAmountWhenEnergyChanged() {
        energyOrb.setEnergy(15);
        when(knight.getEnergy()).thenReturn(30);
        energyOrb.benefit(knight);
        verify(knight).setEnergy(45); // 30 + 15
    }

    @Test
    void benefit_shouldHandleZeroEnergy() {
        energyOrb.setEnergy(0);
        when(knight.getEnergy()).thenReturn(50);
        energyOrb.benefit(knight);
        verify(knight).setEnergy(50); // 50 + 0
    }

    @Test
    void benefit_shouldNotSubtractEnergy() {
        when(knight.getEnergy()).thenReturn(100);
        energyOrb.benefit(knight);
        verify(knight).setEnergy(110); // Should add, not subtract
    }
}