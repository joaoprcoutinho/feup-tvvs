package pt.feup.tvvs.soulknight.model.game.elements.collectables;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class OrbFactoryMutationTests {
    
    @Test
    void createOrb_shouldCreateEnergyOrb() {
        Collectables orb = OrbFactory.createOrb('e', 10, 20);
        assertNotNull(orb, "Should create an orb");
        assertTrue(orb instanceof EnergyOrb, "Should be an EnergyOrb");
        assertEquals('e', orb.getChar(), "Should have correct symbol");
    }

    @Test
    void createOrb_shouldCreateSpeedOrb() {
        Collectables orb = OrbFactory.createOrb('s', 10, 20);
        assertNotNull(orb, "Should create an orb");
        assertTrue(orb instanceof SpeedOrb, "Should be a SpeedOrb");
        assertEquals('s', orb.getChar(), "Should have correct symbol");
    }

    @Test
    void createOrb_shouldCreateHealthOrb() {
        Collectables orb = OrbFactory.createOrb('h', 10, 20);
        assertNotNull(orb, "Should create an orb");
        assertTrue(orb instanceof HealthOrb, "Should be a HealthOrb");
        assertEquals('h', orb.getChar(), "Should have correct symbol");
    }

    @Test
    void createOrb_shouldReturnNullForInvalidType() {
        Collectables orb = OrbFactory.createOrb('x', 10, 20);
        assertNull(orb, "Should return null for invalid type");
    }

    @Test
    void createOrb_shouldUseCorrectXYPositions() {
        Collectables orb = OrbFactory.createOrb('e', 15, 25);
        assertEquals(15, (int) orb.getPosition().x(), "Should have correct X position");
        assertEquals(25, (int) orb.getPosition().y(), "Should have correct Y position");
    }

    @Test
    void createOrb_shouldNotSwapXAndY() {
        Collectables orb = OrbFactory.createOrb('h', 30, 40);
        assertEquals(30, (int) orb.getPosition().x(), "X should be 30, not 40");
        assertEquals(40, (int) orb.getPosition().y(), "Y should be 40, not 30");
    }

    @Test
    void constants_shouldHaveCorrectValues() {
        assertEquals('e', OrbFactory.ENERGY_ORB, "ENERGY_ORB constant should be 'e'");
        assertEquals('s', OrbFactory.SPEED_ORB, "SPEED_ORB constant should be 's'");
        assertEquals('h', OrbFactory.HEALTH_ORB, "HEALTH_ORB constant should be 'h'");
    }
}