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
        mainMenu = new MainMenu();
    }

    @Test
    public void getOptions_shouldReturnNonEmptyList() {
        List<Option> options = mainMenu.getOptions();
        assertNotNull(options);
        assertTrue(options.size() > 0);
    }

    @Test
    public void nextOption_shouldCycleToNextOption() {
        mainMenu.nextOption();
        Option currentOption = mainMenu.getCurrentOption();
        assertNotNull(currentOption);
    }

    @Test
    public void nextOption_shouldWrapAroundToZero() {
        int numOptions = mainMenu.getNumberOptions();
        for (int i = 0; i < numOptions; i++) {
            mainMenu.nextOption();
        }
        assertTrue(mainMenu.isSelected(0));
    }

    @Test
    public void nextOption_shouldIncrementCurrentIndex() {
        assertTrue(mainMenu.isSelected(0));
        mainMenu.nextOption();
        assertTrue(mainMenu.isSelected(1));
    }

    @Test
    public void previousOption_shouldCycleToPreviousOption() {
        mainMenu.previousOption();
        Option currentOption = mainMenu.getCurrentOption();
        assertNotNull(currentOption);
    }

    @Test
    public void previousOption_shouldWrapAroundToLast() {
        mainMenu.previousOption();
        int lastIndex = mainMenu.getNumberOptions() - 1;
        assertTrue(mainMenu.isSelected(lastIndex));
    }

    @Test
    public void previousOption_shouldDecrementCurrentIndex() {
        mainMenu.nextOption();
        mainMenu.previousOption();
        assertTrue(mainMenu.isSelected(0));
    }

    @Test
    public void getCurrentOption_shouldReturnCorrectOption() {
        Option currentOption = mainMenu.getCurrentOption();
        assertNotNull(currentOption);
        assertTrue(mainMenu.getOptions().contains(currentOption));
    }

    @Test
    public void isSelectedStart_shouldReturnTrueForStartOption() {
        assertTrue(mainMenu.isSelectedStart());
    }

    @Test
    public void isSelectedExit_shouldReturnTrueAfterNavigating() {
        mainMenu.nextOption();
        mainMenu.nextOption();
        assertTrue(mainMenu.isSelectedExit());
    }

    @Test
    public void createParticles_shouldCreateParticles() {
        List<Particle> particles = mainMenu.createParticles();
        assertNotNull(particles);
        assertTrue(particles.size() > 0);
    }

    @Test
    public void createParticles_shouldHaveCorrectColor() {
        List<Particle> particles = mainMenu.createParticles();
        for (Particle particle : particles) {
            TextColor.RGB color = particle.getColor();
            assertTrue(color.getGreen() >= 0 && color.getGreen() <= 255);
        }
    }

    @Test
    public void createEntries_shouldCreateCorrectOptions() {
        List<Option> entries = mainMenu.createEntries();
        assertNotNull(entries);
        assertTrue(entries.size() > 0);
        assertEquals(3, entries.size());
    }

    @Test
    public void getOptions_shouldReturnEmptyList_whenCreateEntriesReturnsEmpty() {
        MainMenu menuWithEmptyOptions = new MainMenu() {
            @Override
            protected List<Option> createEntries() {
                return List.of();
            }
            @Override
            public List<Particle> createParticles() {
                return new ArrayList<>();
            }
        };

        List<Option> options = menuWithEmptyOptions.getOptions();
        assertTrue(options.isEmpty());
    }

    @Test
    public void setParticles_shouldUpdateParticlesList() {
        List<Particle> newParticles = new ArrayList<>();
        mainMenu.setParticles(newParticles);
        assertEquals(newParticles, mainMenu.getParticles());
    }

    @Test
    public void getInGame_shouldReturnFalseInitially() {
        assertFalse(mainMenu.getInGame());
    }

    @Test
    public void setInGame_shouldUpdateInGameStatus() {
        mainMenu.setInGame(true);
        assertTrue(mainMenu.getInGame());
    }

    @Test
    public void setInGame_shouldToggleCorrectly() {
        mainMenu.setInGame(true);
        assertTrue(mainMenu.getInGame());
        mainMenu.setInGame(false);
        assertFalse(mainMenu.getInGame());
    }

    @Test
    public void getNumberOptions_shouldReturnCorrectCount() {
        assertEquals(3, mainMenu.getNumberOptions());
    }

    @Test
    public void isSelected_shouldReturnTrueForCurrentOption() {
        assertTrue(mainMenu.isSelected(0));
        mainMenu.nextOption();
        assertTrue(mainMenu.isSelected(1));
    }

    @Test
    public void isSelected_shouldReturnFalseForNonCurrentOption() {
        assertFalse(mainMenu.isSelected(1));
        assertFalse(mainMenu.isSelected(2));
    }
}
