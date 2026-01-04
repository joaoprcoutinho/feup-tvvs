package pt.feup.tvvs.soulknight.view.elements.collectables;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.collectables.Collectables;
import pt.feup.tvvs.soulknight.view.sprites.Sprite;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class OrbViewerWhiteBoxTest {
    private SpriteLoader spriteLoader;
    private Sprite healthSprite;
    private Sprite energySprite;
    private Sprite speedSprite;
    private GUI gui;
    private Collectables collectable;

    @BeforeEach
    void setUp() throws IOException {
        spriteLoader = mock(SpriteLoader.class);
        healthSprite = mock(Sprite.class);
        energySprite = mock(Sprite.class);
        speedSprite = mock(Sprite.class);
        gui = mock(GUI.class);
        collectable = mock(Collectables.class);

        when(spriteLoader.get("sprites/collectables/health.png")).thenReturn(healthSprite);
        when(spriteLoader.get("sprites/collectables/energy.png")).thenReturn(energySprite);
        when(spriteLoader.get("sprites/collectables/speed.png")).thenReturn(speedSprite);
    }

    @Test
    void constructorLoadsAllSprites() throws IOException {
        new OrbViewer(spriteLoader);

        verify(spriteLoader).get("sprites/collectables/health.png");
        verify(spriteLoader).get("sprites/collectables/energy.png");
        verify(spriteLoader).get("sprites/collectables/speed.png");
    }

    @Test
    void drawsHealthOrb() throws IOException {
        OrbViewer viewer = new OrbViewer(spriteLoader);

        when(collectable.getChar()).thenReturn('h');
        when(collectable.getPosition()).thenReturn(new Position(5, 10));

        viewer.draw(collectable, gui, 0L, 0, 0);

        verify(healthSprite).draw(gui, 5, 10);
        verifyNoInteractions(energySprite, speedSprite);
    }

    @Test
    void drawsEnergyOrb() throws IOException {
        OrbViewer viewer = new OrbViewer(spriteLoader);

        when(collectable.getChar()).thenReturn('e');
        when(collectable.getPosition()).thenReturn(new Position(15, 20));

        viewer.draw(collectable, gui, 0L, 0, 0);

        verify(energySprite).draw(gui, 15, 20);
        verifyNoInteractions(healthSprite, speedSprite);
    }

    @Test
    void drawsSpeedOrb() throws IOException {
        OrbViewer viewer = new OrbViewer(spriteLoader);

        when(collectable.getChar()).thenReturn('s');
        when(collectable.getPosition()).thenReturn(new Position(30, 40));

        viewer.draw(collectable, gui, 0L, 0, 0);

        verify(speedSprite).draw(gui, 30, 40);
        verifyNoInteractions(healthSprite, energySprite);
    }

    @Test
    void throwsExceptionForInvalidCharacter() throws IOException {
        OrbViewer viewer = new OrbViewer(spriteLoader);

        when(collectable.getChar()).thenReturn('x');
        when(collectable.getPosition()).thenReturn(new Position(0, 0));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> viewer.draw(collectable, gui, 0L, 0, 0)
        );

        assertEquals("No sprite for character: x", exception.getMessage());
    }
}