package pt.feup.tvvs.soulknight.view.elements.knight.knightStates;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import pt.feup.tvvs.soulknight.model.game.elements.knight.IdleState;
import java.io.IOException;

public class IdleAnimationMutationTests {
    
    @Test
    void constructor_shouldSetInitialState() {
        IdleAnimation animation = new IdleAnimation(IdleState.class, 5);
        assertEquals(IdleState.class, animation.getState());
    }
    
    @Test
    void constructor_shouldSetInitialFrames() {
        IdleAnimation animation = new IdleAnimation(IdleState.class, 5);
        assertEquals(5, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetStateToIdleState() throws IOException {
        IdleAnimation animation = new IdleAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(IdleState.class, animation.getState());
    }
    
    @Test
    void loadAnimation_shouldSetFramesTo8() throws IOException {
        IdleAnimation animation = new IdleAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(8, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetAnimationWithBothLists() throws IOException {
        IdleAnimation animation = new IdleAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertNotNull(animation.getAnimation());
        assertNotNull(animation.getAnimation().getFirstList());
        assertNotNull(animation.getAnimation().getSecondList());
    }
    
    @Test
    void loadAnimation_shouldLoad16SpritesForEachDirection() throws IOException {
        IdleAnimation animation = new IdleAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(16, animation.getAnimation().getFirstList().size());
        assertEquals(16, animation.getAnimation().getSecondList().size());
    }
}