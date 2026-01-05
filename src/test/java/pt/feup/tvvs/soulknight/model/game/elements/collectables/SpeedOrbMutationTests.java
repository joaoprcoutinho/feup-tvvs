package pt.feup.tvvs.soulknight.model.game.elements.collectables;
            
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import java.io.IOException;

public class SpeedOrbMutationTests {
    private SpeedOrb speedOrb;
    private Knight knight;

    @BeforeEach
    void setUp() {
        speedOrb = new SpeedOrb(10, 20, 1.1, 's');
        knight = mock(Knight.class);
    }

    @Test
    void constructor_shouldSetSpeedBoost() {
        assertEquals(1.1, speedOrb.getSpeed_boost(), 0.001);
    }

    @Test
    void constructor_shouldSetSymbol() {
        assertEquals('s', speedOrb.getChar());
    }

    @Test
    void setSpeed_boost_shouldUpdateValue() {
        speedOrb.setSpeed_boost(1.5);
        assertEquals(1.5, speedOrb.getSpeed_boost(), 0.001);
    }

    @Test
    void benefit_shouldMultiplyVelocityXByBoost() {
        Vector currentVelocity = new Vector(10.0, 5.0);
        when(knight.getMaxVelocity()).thenReturn(currentVelocity);
        
        speedOrb.benefit(knight);
        
        // Verify multiplication: 10.0 * 1.1 = 11.0
        verify(knight).setMaxVelocity(argThat(v -> 
            Math.abs(v.x() - 11.0) < 0.001 && Math.abs(v.y() - 5.0) < 0.001
        ));
    }

    @Test
    void benefit_shouldNotChangeYVelocity() {
        Vector currentVelocity = new Vector(10.0, 5.0);
        when(knight.getMaxVelocity()).thenReturn(currentVelocity);
        
        speedOrb.benefit(knight);
        
        verify(knight).setMaxVelocity(argThat(v -> v.y() == 5.0));
    }

    @Test
    void benefit_shouldHandleDifferentBoostValues() {
        speedOrb.setSpeed_boost(2.0);
        Vector currentVelocity = new Vector(5.0, 3.0);
        when(knight.getMaxVelocity()).thenReturn(currentVelocity);
        
        speedOrb.benefit(knight);
        
        // Verify multiplication: 5.0 * 2.0 = 10.0
        verify(knight).setMaxVelocity(argThat(v -> 
            Math.abs(v.x() - 10.0) < 0.001 && Math.abs(v.y() - 3.0) < 0.001
        ));
    }

    @Test
    void benefit_shouldHandleZeroBoost() {
        speedOrb.setSpeed_boost(0.0);
        Vector currentVelocity = new Vector(10.0, 5.0);
        when(knight.getMaxVelocity()).thenReturn(currentVelocity);
        
        speedOrb.benefit(knight);
        
        // Verify multiplication: 10.0 * 0.0 = 0.0
        verify(knight).setMaxVelocity(argThat(v -> 
            Math.abs(v.x() - 0.0) < 0.001
        ));
    }

    @Test
    void benefit_shouldMultiplyNotDivide() {
        Vector currentVelocity = new Vector(10.0, 5.0);
        when(knight.getMaxVelocity()).thenReturn(currentVelocity);
        
        speedOrb.benefit(knight);
        
        // Ensure it's multiplication (11.0), not division (10.0 / 1.1 ≈ 9.09)
        verify(knight).setMaxVelocity(argThat(v -> v.x() > 10.5));
    }
}