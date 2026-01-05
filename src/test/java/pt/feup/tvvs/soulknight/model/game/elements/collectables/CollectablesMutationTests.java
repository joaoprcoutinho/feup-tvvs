package pt.feup.tvvs.soulknight.model.game.elements.collectables;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import java.io.IOException;

public class CollectablesMutationTests {
    
    @Test
    void healthOrb_getChar_shouldReturnCorrectSymbol() {
        HealthOrb healthOrb = new HealthOrb(10, 20, 25, 'h');
        assertEquals('h', healthOrb.getChar(), "Should return 'h'");
    }

    @Test
    void energyOrb_getChar_shouldReturnCorrectSymbol() {
        EnergyOrb energyOrb = new EnergyOrb(10, 20, 10, 'e');
        assertEquals('e', energyOrb.getChar(), "Should return 'e'");
    }

    @Test
    void speedOrb_getChar_shouldReturnCorrectSymbol() {
        SpeedOrb speedOrb = new SpeedOrb(10, 20, 1.1, 's');
        assertEquals('s', speedOrb.getChar(), "Should return 's'");
    }

    @Test
    void healthOrb_benefit_shouldBeCalledWithoutException() {
        HealthOrb healthOrb = new HealthOrb(10, 20, 25, 'h');
        Knight knight = mock(Knight.class);
        when(knight.getHP()).thenReturn(50);
        
        assertDoesNotThrow(() -> healthOrb.benefit(knight), "Should not throw exception");
    }

    @Test
    void energyOrb_benefit_shouldBeCalledWithoutException() {
        EnergyOrb energyOrb = new EnergyOrb(10, 20, 10, 'e');
        Knight knight = mock(Knight.class);
        when(knight.getEnergy()).thenReturn(50);
        
        assertDoesNotThrow(() -> energyOrb.benefit(knight), "Should not throw exception");
    }

    @Test
    void speedOrb_benefit_shouldBeCalledWithoutException() {
        SpeedOrb speedOrb = new SpeedOrb(10, 20, 1.1, 's');
        Knight knight = mock(Knight.class);
        when(knight.getMaxVelocity()).thenReturn(new pt.feup.tvvs.soulknight.model.dataStructs.Vector(10, 5));
        
        assertDoesNotThrow(() -> speedOrb.benefit(knight), "Should not throw exception");
    }
}