package pt.feup.tvvs.soulknight.view.elements.knight.knightStates;
            
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import pt.feup.tvvs.soulknight.model.dataStructs.PairList;
import pt.feup.tvvs.soulknight.model.game.elements.knight.IdleState;
import pt.feup.tvvs.soulknight.view.sprites.Sprite;
import java.io.IOException;
import java.util.List;

public class StateAnimationMutationTests {
    
    // Concrete implementation for testing abstract class
    private static class TestStateAnimation extends StateAnimation {
        public TestStateAnimation(Class state, int frames) {
            super(state, frames);
        }
        
        @Override
        public void loadAnimation(String path) throws IOException {
            // Empty for testing
        }
    }
    
    @Test
    void constructor_shouldSetState() {
        StateAnimation animation = new TestStateAnimation(IdleState.class, 5);
        assertEquals(IdleState.class, animation.getState());
    }
    
    @Test
    void constructor_shouldSetFrames() {
        StateAnimation animation = new TestStateAnimation(IdleState.class, 8);
        assertEquals(8, animation.getFrames());
    }
    
    @Test
    void setFrames_shouldUpdateFrames() {
        StateAnimation animation = new TestStateAnimation(IdleState.class, 5);
        animation.setFrames(10);
        assertEquals(10, animation.getFrames());
    }
    
    @Test
    void setState_shouldUpdateState() {
        StateAnimation animation = new TestStateAnimation(IdleState.class, 5);
        animation.setState(String.class);
        assertEquals(String.class, animation.getState());
    }
    
    @Test
    void setAnimation_shouldUpdateAnimation() {
        StateAnimation animation = new TestStateAnimation(IdleState.class, 5);
        PairList<Sprite> pairList = new PairList<>(List.of(), List.of());
        animation.setAnimation(pairList);
        assertEquals(pairList, animation.getAnimation());
    }
    
    @Test
    void getAnimation_shouldReturnNull_whenNotSet() {
        StateAnimation animation = new TestStateAnimation(IdleState.class, 5);
        assertNull(animation.getAnimation());
    }
    
    @Test
    void getSprite_shouldReturnNull_whenFramesIsZero() {
        StateAnimation animation = new TestStateAnimation(IdleState.class, 0);
        assertNull(animation.getSprite(100L, true));
    }
    
    @Test
    void getSprite_shouldReturnRightSprite_whenFacingRight() {
        StateAnimation animation = new TestStateAnimation(IdleState.class, 10);
        Sprite rightSprite = mock(Sprite.class);
        Sprite leftSprite = mock(Sprite.class);
        animation.setAnimation(new PairList<>(List.of(rightSprite), List.of(leftSprite)));
        
        Sprite result = animation.getSprite(0L, true);
        
        assertSame(rightSprite, result);
    }
    
    @Test
    void getSprite_shouldReturnLeftSprite_whenFacingLeft() {
        StateAnimation animation = new TestStateAnimation(IdleState.class, 10);
        Sprite rightSprite = mock(Sprite.class);
        Sprite leftSprite = mock(Sprite.class);
        animation.setAnimation(new PairList<>(List.of(rightSprite), List.of(leftSprite)));
        
        Sprite result = animation.getSprite(0L, false);
        
        assertSame(leftSprite, result);
    }
    
    @Test
    void getSprite_shouldCycleFramesCorrectly() {
        StateAnimation animation = new TestStateAnimation(IdleState.class, 30);
        Sprite sprite0 = mock(Sprite.class);
        Sprite sprite1 = mock(Sprite.class);
        animation.setAnimation(new PairList<>(List.of(sprite0, sprite1), List.of()));
        
        // With frames=30, animationFrameTime = 30/30 = 1
        // tick=0 -> frameIndex = (0/1) % 2 = 0
        assertSame(sprite0, animation.getSprite(0L, true));
        // tick=1 -> frameIndex = (1/1) % 2 = 1
        assertSame(sprite1, animation.getSprite(1L, true));
        // tick=2 -> frameIndex = (2/1) % 2 = 0
        assertSame(sprite0, animation.getSprite(2L, true));
    }
}