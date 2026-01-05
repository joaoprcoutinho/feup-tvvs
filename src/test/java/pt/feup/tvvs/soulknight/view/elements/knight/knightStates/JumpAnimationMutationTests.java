package pt.feup.tvvs.soulknight.view.elements.knight.knightStates;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import pt.feup.tvvs.soulknight.model.game.elements.knight.JumpState;
import java.io.IOException;

public class JumpAnimationMutationTests {
    
    @Test
    void constructor_shouldSetInitialState() {
        JumpAnimation animation = new JumpAnimation(JumpState.class, 5);
        assertEquals(JumpState.class, animation.getState());
    }
    
    @Test
    void constructor_shouldSetInitialFrames() {
        JumpAnimation animation = new JumpAnimation(JumpState.class, 5);
        assertEquals(5, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetStateToJumpState() throws IOException {
        JumpAnimation animation = new JumpAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(JumpState.class, animation.getState());
    }
    
    @Test
    void loadAnimation_shouldSetFramesTo6() throws IOException {
        JumpAnimation animation = new JumpAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(6, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetAnimationWithBothLists() throws IOException {
        JumpAnimation animation = new JumpAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertNotNull(animation.getAnimation());
        assertNotNull(animation.getAnimation().getFirstList());
        assertNotNull(animation.getAnimation().getSecondList());
    }
    
    @Test
    void loadAnimation_shouldLoad1SpriteForEachDirection() throws IOException {
        JumpAnimation animation = new JumpAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(1, animation.getAnimation().getFirstList().size());
        assertEquals(1, animation.getAnimation().getSecondList().size());
    }
}