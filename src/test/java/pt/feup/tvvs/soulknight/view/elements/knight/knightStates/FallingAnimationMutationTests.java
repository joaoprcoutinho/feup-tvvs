package pt.feup.tvvs.soulknight.view.elements.knight.knightStates;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import pt.feup.tvvs.soulknight.model.game.elements.knight.FallingState;
import java.io.IOException;

public class FallingAnimationMutationTests {
    
    @Test
    void constructor_shouldSetInitialState() {
        FallingAnimation animation = new FallingAnimation(FallingState.class, 5);
        assertEquals(FallingState.class, animation.getState());
    }
    
    @Test
    void constructor_shouldSetInitialFrames() {
        FallingAnimation animation = new FallingAnimation(FallingState.class, 5);
        assertEquals(5, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetStateToFallingState() throws IOException {
        FallingAnimation animation = new FallingAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(FallingState.class, animation.getState());
    }
    
    @Test
    void loadAnimation_shouldSetFramesTo10() throws IOException {
        FallingAnimation animation = new FallingAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(10, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetAnimationWithBothLists() throws IOException {
        FallingAnimation animation = new FallingAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertNotNull(animation.getAnimation());
        assertNotNull(animation.getAnimation().getFirstList());
        assertNotNull(animation.getAnimation().getSecondList());
    }
    
    @Test
    void loadAnimation_shouldLoad6SpritesForEachDirection() throws IOException {
        FallingAnimation animation = new FallingAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(6, animation.getAnimation().getFirstList().size());
        assertEquals(6, animation.getAnimation().getSecondList().size());
    }
}