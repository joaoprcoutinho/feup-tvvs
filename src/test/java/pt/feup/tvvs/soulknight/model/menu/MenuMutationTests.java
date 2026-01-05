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

public class MenuMutationTests {

    private MainMenu mainMenu;

    @BeforeEach
    void setUp() {
        // Initialize MainMenu before each test
        mainMenu = new MainMenu();
    }

    /* -------------------------------------------------
     * Test Menu Option Handling
     * ------------------------------------------------- */
    @Test
    void getOptions_shouldReturnNonEmptyList() {
        List<Option> options = mainMenu.getOptions();
        assertNotNull(options);
        assertTrue(options.size() > 0, "Options list should not be empty");
    }

    @Test
    void nextOption_shouldCycleToNextOption() {
        mainMenu.nextOption();
        Option currentOption = mainMenu.getCurrentOption();
        assertNotNull(currentOption, "Current option should not be null after calling nextOption");
    }

    @Test
    void nextOption_shouldWrapAroundToZero() {
        // Navigate to last option
        int numOptions = mainMenu.getNumberOptions();
        for (int i = 0; i < numOptions; i++) {
            mainMenu.nextOption();
        }
        // Should be back at first option (index 0)
        assertTrue(mainMenu.isSelected(0), "Should wrap around to first option");
    }

    @Test
    void nextOption_shouldIncrementCurrentIndex() {
        assertTrue(mainMenu.isSelected(0), "Should start at index 0");
        mainMenu.nextOption();
        assertTrue(mainMenu.isSelected(1), "Should be at index 1 after nextOption");
    }

    @Test
    void previousOption_shouldCycleToPreviousOption() {
        mainMenu.previousOption();
        Option currentOption = mainMenu.getCurrentOption();
        assertNotNull(currentOption, "Current option should not be null after calling previousOption");
    }

    @Test
    void previousOption_shouldWrapAroundToLast() {
        // At index 0, calling previousOption should wrap to last
        mainMenu.previousOption();
        int lastIndex = mainMenu.getNumberOptions() - 1;
        assertTrue(mainMenu.isSelected(lastIndex), "Should wrap around to last option");
    }

    @Test
    void previousOption_shouldDecrementCurrentIndex() {
        mainMenu.nextOption(); // Move to index 1
        mainMenu.previousOption(); // Back to index 0
        assertTrue(mainMenu.isSelected(0), "Should be back at index 0");
    }

    @Test
    void getCurrentOption_shouldReturnCorrectOption() {
        Option currentOption = mainMenu.getCurrentOption();
        assertNotNull(currentOption, "Current option should not be null");
        // Ensure that the current option is a valid option
        assertTrue(mainMenu.getOptions().contains(currentOption), "Current option should be in the options list");
    }

    @Test
    void isSelectedStart_shouldReturnTrueForStartOption() {
        assertTrue(mainMenu.isSelectedStart(), "Start option should be selected initially");
    }

    @Test
    void isSelectedExit_shouldReturnTrueAfterNavigating() {
        // Navigate to exit option (Start -> Settings -> Exit)
        mainMenu.nextOption();
        mainMenu.nextOption();
        assertTrue(mainMenu.isSelectedExit(), "Exit option should be selected after navigating");
    }

    /* -------------------------------------------------
     * Test Particle Creation
     * ------------------------------------------------- */
    @Test
    void createParticles_shouldCreateParticles() {
        List<Particle> particles = mainMenu.createParticles();
        assertNotNull(particles, "Particles list should not be null");
        assertTrue(particles.size() > 0, "Particles list should not be empty");
    }

    @Test
    void createParticles_shouldHaveCorrectColor() {
        List<Particle> particles = mainMenu.createParticles();
        for (Particle particle : particles) {
            TextColor.RGB color = (TextColor.RGB) particle.getColor();
            // Verify if the green component of the color is within valid range [0, 255]
            assertTrue(color.getGreen() >= 0 && color.getGreen() <= 255, "Green color component should be within the range [0, 255]");
        }
    }

    @Test
    void createEntries_shouldCreateCorrectOptions() {
        List<Option> entries = mainMenu.createEntries();
        assertNotNull(entries, "Entries list should not be null");
        assertTrue(entries.size() > 0, "Entries list should not be empty");
        assertEquals(3, entries.size(), "There should be 3 options (Start, Settings, Exit)");
    }

    /* -------------------------------------------------
     * Test Edge Cases and Coverage
     * ------------------------------------------------- */
    @Test
    void getOptions_shouldReturnEmptyList_whenCreateEntriesReturnsEmpty() {
        MainMenu menuWithEmptyOptions = new MainMenu() {
            @Override
            protected List<Option> createEntries() {
                return List.of(); // Returning an empty list for options
            }
            @Override
            public List<Particle> createParticles() {
                return new ArrayList<>();
            }
        };

        List<Option> options = menuWithEmptyOptions.getOptions();
        assertTrue(options.isEmpty(), "Options list should be empty when createEntries returns empty list");
    }

    @Test
    void setParticles_shouldUpdateParticlesList() {
        List<Particle> newParticles = new ArrayList<>();
        mainMenu.setParticles(newParticles);
        assertEquals(newParticles, mainMenu.getParticles(), "Particles should be updated");
    }

    @Test
    void getInGame_shouldReturnFalseInitially() {
        assertFalse(mainMenu.getInGame(), "InGame should be false initially");
    }

    @Test
    void setInGame_shouldUpdateInGameStatus() {
        mainMenu.setInGame(true);
        assertTrue(mainMenu.getInGame(), "InGame should be true after setting");
    }

    @Test
    void setInGame_shouldToggleCorrectly() {
        mainMenu.setInGame(true);
        assertTrue(mainMenu.getInGame());
        mainMenu.setInGame(false);
        assertFalse(mainMenu.getInGame());
    }

    @Test
    void getNumberOptions_shouldReturnCorrectCount() {
        assertEquals(3, mainMenu.getNumberOptions(), "Should have 3 options");
    }

    @Test
    void isSelected_shouldReturnTrueForCurrentOption() {
        assertTrue(mainMenu.isSelected(0), "Option 0 should be selected initially");
        mainMenu.nextOption();
        assertTrue(mainMenu.isSelected(1), "Option 1 should be selected after nextOption");
    }

    @Test
    void isSelected_shouldReturnFalseForNonCurrentOption() {
        assertFalse(mainMenu.isSelected(1), "Option 1 should not be selected initially");
        assertFalse(mainMenu.isSelected(2), "Option 2 should not be selected initially");
    }
}
