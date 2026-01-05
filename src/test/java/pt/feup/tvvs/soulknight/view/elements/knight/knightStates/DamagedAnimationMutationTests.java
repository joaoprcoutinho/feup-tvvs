package pt.feup.tvvs.soulknight.view.elements.knight.knightStates;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import pt.feup.tvvs.soulknight.model.game.elements.knight.DamagedState;
import java.io.IOException;

public class DamagedAnimationMutationTests {
    
    @Test
    void constructor_shouldSetInitialState() {
        DamagedAnimation animation = new DamagedAnimation(DamagedState.class, 5);
        assertEquals(DamagedState.class, animation.getState());
    }
    
    @Test
    void constructor_shouldSetInitialFrames() {
        DamagedAnimation animation = new DamagedAnimation(DamagedState.class, 5);
        assertEquals(5, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetStateToDamagedState() throws IOException {
        DamagedAnimation animation = new DamagedAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(DamagedState.class, animation.getState());
    }
    
    @Test
    void loadAnimation_shouldSetFramesTo4() throws IOException {
        DamagedAnimation animation = new DamagedAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(4, animation.getFrames());
    }
    
    @Test
    void loadAnimation_shouldSetAnimationWithBothLists() throws IOException {
        DamagedAnimation animation = new DamagedAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertNotNull(animation.getAnimation());
        assertNotNull(animation.getAnimation().getFirstList());
        assertNotNull(animation.getAnimation().getSecondList());
    }
    
    @Test
    void loadAnimation_shouldLoad2SpritesForEachDirection() throws IOException {
        DamagedAnimation animation = new DamagedAnimation(null, 0);
        animation.loadAnimation("sprites/knight");
        assertEquals(2, animation.getAnimation().getFirstList().size());
        assertEquals(2, animation.getAnimation().getSecondList().size());
    }
}