package pt.feup.tvvs.soulknight.model.game.elements;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TreeMutationTests {
    
    @Test
    public void getChar_shouldReturnSmallTree() {
        Tree tree = new Tree(10, 20, 't');
        assertEquals('t', tree.getChar());
    }

    @Test
    public void getChar_shouldReturnMediumTree() {
        Tree tree = new Tree(15, 25, 'T');
        assertEquals('T', tree.getChar());
    }

    @Test
    public void constructor_shouldSetPosition() {
        Tree tree = new Tree(100, 200, 't');
        assertEquals(100, tree.getPosition().x());
        assertEquals(200, tree.getPosition().y());
    }
}