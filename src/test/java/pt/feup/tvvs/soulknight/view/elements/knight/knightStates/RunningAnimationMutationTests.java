package pt.feup.tvvs.soulknight.view.elements.knight.knightStates;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import pt.feup.tvvs.soulknight.model.game.elements.knight.RunningState;
import java.io.IOException;

public class RunningAnimationMutationTests {
    
    @Test
    void constructor_shouldSetInitialState() {
        RunningAnimation animation = new RunningAnimation(RunningState.class, 5);
        assertEquals(RunningState.class, animation.getState());
    }
    
    @Test
    void constructor_shouldSetInitialFrames() {
        RunningAnimation animation = new RunningAnimation(RunningState.class, 5);
        assertEquals(5, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetStateToRunningState() throws IOException {
        RunningAnimation animation = new RunningAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(RunningState.class, animation.getState());
    }
    
    @Test
    void loadAnimation_shouldSetFramesTo5() throws IOException {
        RunningAnimation animation = new RunningAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(5, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetAnimationWithBothLists() throws IOException {
        RunningAnimation animation = new RunningAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertNotNull(animation.getAnimation());
        assertNotNull(animation.getAnimation().getFirstList());
        assertNotNull(animation.getAnimation().getSecondList());
    }
    
    @Test
    void loadAnimation_shouldLoad5SpritesForEachDirection() throws IOException {
        RunningAnimation animation = new RunningAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(5, animation.getAnimation().getFirstList().size());
        assertEquals(5, animation.getAnimation().getSecondList().size());
    }
}