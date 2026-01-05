package pt.feup.tvvs.soulknight.view.elements.knight;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.knight.DashState;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.model.game.elements.knight.KnightState;
import pt.feup.tvvs.soulknight.view.elements.knight.knightStates.DashAnimation;
import pt.feup.tvvs.soulknight.view.sprites.Sprite;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import static org.mockito.Mockito.*;
import java.io.IOException;

public class KnightViewerWhiteBoxTest {
    private SpriteLoader spriteLoader;
    private KnightViewer knightViewer;
    private GUI gui;
    private Knight knight;

    @BeforeEach
    void setUp() throws IOException{
        spriteLoader = mock(SpriteLoader.class);
        gui = mock(GUI.class);
        knight = mock(Knight.class);
        Sprite mockSprite = mock(Sprite.class);
        when(spriteLoader.get(anyString())).thenReturn(mockSprite);
        knightViewer = new KnightViewer(spriteLoader);
    }

    @Test
    void testDraw() throws IOException {
        when(knight.getState()).thenReturn(mock(DashState.class));
        when(knight.isFacingRight()).thenReturn(true);
        when(knight.getPosition()).thenReturn(new Position(10, 20));
        knightViewer.draw(knight, gui, 0, 0, 0);
    }

    @Test
    void testDrawNoAnimation() throws IOException {
        when(knight.getState()).thenReturn(mock(KnightState.class));
        knightViewer.draw(knight, gui, 0, 0, 0);
    }
}