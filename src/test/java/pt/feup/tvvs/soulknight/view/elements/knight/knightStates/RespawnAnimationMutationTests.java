package pt.feup.tvvs.soulknight.view.elements.knight.knightStates;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class RespawnAnimationMutationTests {
    
    @Test
    void constructor_shouldSetInitialState() {
        RespawnAnimation animation = new RespawnAnimation(String.class, 5);
        assertEquals(String.class, animation.getState());
    }
    
    @Test
    void constructor_shouldSetInitialFrames() {
        RespawnAnimation animation = new RespawnAnimation(String.class, 5);
        assertEquals(5, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetStateToRunningAnimationClass() {
        RespawnAnimation animation = new RespawnAnimation(null, 0);
        animation.loadAnimation("any/path");
        assertEquals(RunningAnimation.class, animation.getState());
    }
    
    @Test
    void loadAnimation_shouldSetFramesTo1() {
        RespawnAnimation animation = new RespawnAnimation(null, 0);
        animation.loadAnimation("any/path");
        assertEquals(1, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetAnimationWithNullLists() {
        RespawnAnimation animation = new RespawnAnimation(null, 0);
        animation.loadAnimation("any/path");
        assertNotNull(animation.getAnimation());
        assertNull(animation.getAnimation().getFirstList());
        assertNull(animation.getAnimation().getSecondList());
    }
}