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
        // Initialize SettingsMenu before each test
        settingsMenu = new SettingsMenu();
    }

    /* -------------------------------------------------
     * Test Menu Option Handling
     * ------------------------------------------------- */
    @Test
    void createEntries_shouldReturnCorrectOptions() {
        List<Option> entries = settingsMenu.createEntries();
        assertNotNull(entries, "Entries list should not be null");
        assertEquals(2, entries.size(), "There should be 2 options (Resolution, Exit to Main Menu)");
    }

    /* -------------------------------------------------
     * Test Particle Creation
     * ------------------------------------------------- */
    @Test
    void createParticles_shouldCreateParticles() {
        List<Particle> particles = settingsMenu.createParticles();
        assertNotNull(particles, "Particles list should not be null");
        assertTrue(particles.size() > 0, "Particles list should not be empty");
    }

    @Test
    void createParticles_shouldHaveCorrectColor() {
        List<Particle> particles = settingsMenu.createParticles();
        for (Particle particle : particles) {
            TextColor.RGB color = (TextColor.RGB) particle.getColor();
            // Ensure green intensity is in the expected range
            assertTrue(color.getGreen() >= 0 && color.getGreen() <= 150, "Green color intensity should be between 0 and 150");
            // Ensure blue intensity is in the expected range
            assertTrue(color.getBlue() >= 100 && color.getBlue() <= 150, "Blue color intensity should be between 100 and 150");
        }
    }

    @Test
    void createParticles_shouldHaveRandomPositions() {
        List<Particle> particles = settingsMenu.createParticles();
        for (Particle particle : particles) {
            Position position = particle.getPosition();
            assertTrue(position.x() >= 0 && position.x() <= 160, "X position should be within screen width (0-160)");
            assertTrue(position.y() >= 0 && position.y() <= 90, "Y position should be within screen height (0-90)");
        }
    }

    /* -------------------------------------------------
     * Edge Case Test for Empty Entries List
     * ------------------------------------------------- */
    @Test
    void createEntries_shouldReturnEmptyList_whenNoOptions() {
        SettingsMenu emptyMenu = new SettingsMenu() {
            @Override
            protected List<Option> createEntries() {
                return List.of(); // Simulating an empty list of options
            }

            @Override
            public List<Particle> createParticles() {
                return new ArrayList<>();
            }
        };

        List<Option> options = emptyMenu.createEntries();
        assertTrue(options.isEmpty(), "Options list should be empty when createEntries returns empty list");
    }
}