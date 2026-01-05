package pt.feup.tvvs.soulknight.model.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.state.particle.RandomState;
import com.googlecode.lanterna.TextColor;

import java.util.ArrayList;
import java.util.List;

public class SettingsMenuMutationTests {

    private SettingsMenu settingsMenu;

    @BeforeEach
    void setUp() {
        settingsMenu = new SettingsMenu();
    }

    @Test
    public void createEntries_shouldReturnCorrectOptions() {
        List<Option> entries = settingsMenu.createEntries();
        assertNotNull(entries);
        assertEquals(2, entries.size());
    }

    @Test
    public void createParticles_shouldCreateParticles() {
        List<Particle> particles = settingsMenu.createParticles();
        assertNotNull(particles);
        assertTrue(particles.size() > 0);
    }

    @Test
    public void createParticles_shouldHaveCorrectColor() {
        List<Particle> particles = settingsMenu.createParticles();
        for (Particle particle : particles) {
            TextColor.RGB color = particle.getColor();
            assertTrue(color.getGreen() >= 0 && color.getGreen() <= 150);
            assertTrue(color.getBlue() >= 100 && color.getBlue() <= 150);
        }
    }

    @Test
    public void createParticles_shouldHaveRandomPositions() {
        List<Particle> particles = settingsMenu.createParticles();
        for (Particle particle : particles) {
            Position position = particle.getPosition();
            assertTrue(position.x() >= 0 && position.x() <= 160);
            assertTrue(position.y() >= 0 && position.y() <= 90);
        }
    }

    @Test
    public void createEntries_shouldReturnEmptyList_whenNoOptions() {
        SettingsMenu emptyMenu = new SettingsMenu() {
            @Override
            protected List<Option> createEntries() {
                return List.of();
            }

            @Override
            public List<Particle> createParticles() {
                return new ArrayList<>();
            }
        };

        List<Option> options = emptyMenu.createEntries();
        assertTrue(options.isEmpty());
    }
}