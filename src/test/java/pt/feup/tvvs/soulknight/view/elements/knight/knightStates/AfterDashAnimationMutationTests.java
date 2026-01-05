package pt.feup.tvvs.soulknight.view.elements.knight.knightStates;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import pt.feup.tvvs.soulknight.model.game.elements.knight.AfterDashState;
import java.io.IOException;

public class AfterDashAnimationMutationTests {
    
    @Test
    void constructor_shouldSetInitialState() {
        AfterDashAnimation animation = new AfterDashAnimation(AfterDashState.class, 5);
        assertEquals(AfterDashState.class, animation.getState());
    }
    
    @Test
    void constructor_shouldSetInitialFrames() {
        AfterDashAnimation animation = new AfterDashAnimation(AfterDashState.class, 5);
        assertEquals(5, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetStateToAfterDashState() throws IOException {
        AfterDashAnimation animation = new AfterDashAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(AfterDashState.class, animation.getState());
    }
    
    @Test
    void loadAnimation_shouldSetFramesTo10() throws IOException {
        AfterDashAnimation animation = new AfterDashAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(10, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetAnimationWithBothLists() throws IOException {
        AfterDashAnimation animation = new AfterDashAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertNotNull(animation.getAnimation());
        assertNotNull(animation.getAnimation().getFirstList());
        assertNotNull(animation.getAnimation().getSecondList());
    }
    
    @Test
    void loadAnimation_shouldLoad5SpritesForEachDirection() throws IOException {
        AfterDashAnimation animation = new AfterDashAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(5, animation.getAnimation().getFirstList().size());
        assertEquals(5, animation.getAnimation().getSecondList().size());
    }
}