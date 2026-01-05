package pt.feup.tvvs.soulknight.view.elements.knight.knightStates;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import pt.feup.tvvs.soulknight.model.game.elements.knight.DashState;
import java.io.IOException;

public class DashAnimationMutationTests {
    
    @Test
    void constructor_shouldSetInitialState() {
        DashAnimation animation = new DashAnimation(DashState.class, 5);
        assertEquals(DashState.class, animation.getState());
    }
    
    @Test
    void constructor_shouldSetInitialFrames() {
        DashAnimation animation = new DashAnimation(DashState.class, 5);
        assertEquals(5, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetStateToDashState() throws IOException {
        DashAnimation animation = new DashAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(DashState.class, animation.getState());
    }
    
    @Test
    void loadAnimation_shouldSetFramesTo10() throws IOException {
        DashAnimation animation = new DashAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(10, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetAnimationWithBothLists() throws IOException {
        DashAnimation animation = new DashAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertNotNull(animation.getAnimation());
        assertNotNull(animation.getAnimation().getFirstList());
        assertNotNull(animation.getAnimation().getSecondList());
    }
    
    @Test
    void loadAnimation_shouldLoad5SpritesForEachDirection() throws IOException {
        DashAnimation animation = new DashAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(5, animation.getAnimation().getFirstList().size());
        assertEquals(5, animation.getAnimation().getSecondList().size());
    }
}