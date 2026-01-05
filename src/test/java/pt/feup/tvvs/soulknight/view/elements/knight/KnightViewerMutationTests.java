package pt.feup.tvvs.soulknight.view.elements.knight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.knight.IdleState;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.view.elements.knight.knightStates.StateAnimation;
import pt.feup.tvvs.soulknight.view.sprites.Sprite;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static org.mockito.Mockito.*;

public class KnightViewerMutationTests {

    private KnightViewer viewer;
    private StateAnimation animation;
    private Sprite sprite;
    private GUI gui;
    private Knight knight;

    @BeforeEach
    void setUp() throws Exception {
        viewer = new KnightViewer(mock(SpriteLoader.class));

        animation = mock(StateAnimation.class);
        sprite = mock(Sprite.class);
        gui = mock(GUI.class);
        knight = mock(Knight.class);

        when(animation.getState()).thenReturn(IdleState.class);
        when(animation.getSprite(anyLong(), anyBoolean())).thenReturn(sprite);

        when(knight.getState()).thenReturn(new IdleState(knight));
        when(knight.isFacingRight()).thenReturn(true);

        when(knight.getPosition()).thenReturn(new Position(20, 30));

        injectSingleAnimation(viewer, animation);
    }

    /**
     * KILLS:
     * - sprite != null negated
     * - Sprite.draw() removed
     */
    @Test
    void draw_shouldDrawSprite_whenSpriteExists() throws IOException {
        viewer.draw(knight, gui, 100L, 0, 0);

        verify(sprite, times(1))
                .draw(eq(gui), anyInt(), anyInt());
    }

    /**
     * KILLS:
     * - integer subtraction replaced with addition
     */
    @Test
    void draw_shouldApplyCorrectOffsets() throws IOException {
        viewer.draw(knight, gui, 0L, 0, 0);

        // Expected: x = 20 - 4 = 16, y = 30 - 1 = 29
        verify(sprite).draw(gui, 16, 29);
    }

    /**
     * KILLS:
     * - findAnimationForState() returning null
     */
    @Test
    void draw_shouldUseAnimationMatchingState() throws IOException {
        viewer.draw(knight, gui, 50L, 0, 0);

        verify(animation, times(1))
                .getSprite(anyLong(), anyBoolean());
    }

    /* ---------------------------------------------------------------------- */
    /* Reflection helper                                                       */
    /* ---------------------------------------------------------------------- */

    @SuppressWarnings("unchecked")
    private void injectSingleAnimation(KnightViewer viewer, StateAnimation animation)
            throws Exception {

        Field animationsField = KnightViewer.class.getDeclaredField("animations");
        animationsField.setAccessible(true);

        List<StateAnimation> animations =
                (List<StateAnimation>) animationsField.get(viewer);

        animations.clear();
        animations.add(animation);
    }
}