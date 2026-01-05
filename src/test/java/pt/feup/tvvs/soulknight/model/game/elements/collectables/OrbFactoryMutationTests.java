package pt.feup.tvvs.soulknight.model.game.elements.collectables;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class OrbFactoryMutationTests {
    
    @Test
    public void createOrb_shouldCreateEnergyOrb() {
        Collectables orb = OrbFactory.createOrb('e', 10, 20);
        assertNotNull(orb);
        assertTrue(orb instanceof EnergyOrb);
        assertEquals('e', orb.getChar());
    }

    @Test
    public void createOrb_shouldCreateSpeedOrb() {
        Collectables orb = OrbFactory.createOrb('s', 10, 20);
        assertNotNull(orb);
        assertTrue(orb instanceof SpeedOrb);
        assertEquals('s', orb.getChar());
    }

    @Test
    public void createOrb_shouldCreateHealthOrb() {
        Collectables orb = OrbFactory.createOrb('h', 10, 20);
        assertNotNull(orb);
        assertTrue(orb instanceof HealthOrb);
        assertEquals('h', orb.getChar());
    }

    @Test
    public void createOrb_shouldReturnNullForInvalidType() {
        Collectables orb = OrbFactory.createOrb('x', 10, 20);
        assertNull(orb);
    }

    @Test
    public void createOrb_shouldUseCorrectXYPositions() {
        Collectables orb = OrbFactory.createOrb('e', 15, 25);
        assertEquals(15, (int) orb.getPosition().x());
        assertEquals(25, (int) orb.getPosition().y());
    }

    @Test
    public void createOrb_shouldNotSwapXAndY() {
        Collectables orb = OrbFactory.createOrb('h', 30, 40);
        assertEquals(30, (int) orb.getPosition().x());
        assertEquals(40, (int) orb.getPosition().y());
    }

    @Test
    public void constants_shouldHaveCorrectValues() {
        assertEquals('e', OrbFactory.ENERGY_ORB);
        assertEquals('s', OrbFactory.SPEED_ORB);
        assertEquals('h', OrbFactory.HEALTH_ORB);
    }
}