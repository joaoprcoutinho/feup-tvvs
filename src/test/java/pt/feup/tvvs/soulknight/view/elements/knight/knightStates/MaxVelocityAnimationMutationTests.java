package pt.feup.tvvs.soulknight.view.elements.knight.knightStates;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import pt.feup.tvvs.soulknight.model.game.elements.knight.MaxVelocityState;
import java.io.IOException;

public class MaxVelocityAnimationMutationTests {
    
    @Test
    void constructor_shouldSetInitialState() {
        MaxVelocityAnimation animation = new MaxVelocityAnimation(MaxVelocityState.class, 5);
        assertEquals(MaxVelocityState.class, animation.getState());
    }
    
    @Test
    void constructor_shouldSetInitialFrames() {
        MaxVelocityAnimation animation = new MaxVelocityAnimation(MaxVelocityState.class, 5);
        assertEquals(5, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetStateToMaxVelocityState() throws IOException {
        MaxVelocityAnimation animation = new MaxVelocityAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(MaxVelocityState.class, animation.getState());
    }
    
    @Test
    void loadAnimation_shouldSetFramesTo10() throws IOException {
        MaxVelocityAnimation animation = new MaxVelocityAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(10, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetAnimationWithBothLists() throws IOException {
        MaxVelocityAnimation animation = new MaxVelocityAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertNotNull(animation.getAnimation());
        assertNotNull(animation.getAnimation().getFirstList());
        assertNotNull(animation.getAnimation().getSecondList());
    }
    
    @Test
    void loadAnimation_shouldLoad3SpritesForEachDirection() throws IOException {
        MaxVelocityAnimation animation = new MaxVelocityAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(3, animation.getAnimation().getFirstList().size());
        assertEquals(3, animation.getAnimation().getSecondList().size());
    }
}