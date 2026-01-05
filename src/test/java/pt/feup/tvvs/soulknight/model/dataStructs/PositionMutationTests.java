package pt.feup.tvvs.soulknight.model.dataStructs;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class PositionMutationTests {
    
    @Test
    void constructor_shouldSetXValue() {
        Position p = new Position(10.0, 20.0);
        assertEquals(10.0, p.x(), 0.001);
    }

    @Test
    void constructor_shouldSetYValue() {
        Position p = new Position(10.0, 20.0);
        assertEquals(20.0, p.y(), 0.001);
    }

    @Test
    void getLeft_shouldDecrementX() {
        Position p = new Position(10.0, 20.0);
        Position left = p.getLeft();
        assertEquals(9.0, left.x(), 0.001);
    }

    @Test
    void getLeft_shouldNotChangeY() {
        Position p = new Position(10.0, 20.0);
        Position left = p.getLeft();
        assertEquals(20.0, left.y(), 0.001);
    }

    @Test
    void getRight_shouldIncrementX() {
        Position p = new Position(10.0, 20.0);
        Position right = p.getRight();
        assertEquals(11.0, right.x(), 0.001);
    }

    @Test
    void getRight_shouldNotChangeY() {
        Position p = new Position(10.0, 20.0);
        Position right = p.getRight();
        assertEquals(20.0, right.y(), 0.001);
    }

    @Test
    void getUp_shouldDecrementY() {
        Position p = new Position(10.0, 20.0);
        Position up = p.getUp();
        assertEquals(19.0, up.y(), 0.001);
    }

    @Test
    void getUp_shouldNotChangeX() {
        Position p = new Position(10.0, 20.0);
        Position up = p.getUp();
        assertEquals(10.0, up.x(), 0.001);
    }

    @Test
    void getDown_shouldIncrementY() {
        Position p = new Position(10.0, 20.0);
        Position down = p.getDown();
        assertEquals(21.0, down.y(), 0.001);
    }

    @Test
    void getDown_shouldNotChangeX() {
        Position p = new Position(10.0, 20.0);
        Position down = p.getDown();
        assertEquals(10.0, down.x(), 0.001);
    }

    @Test
    void equals_shouldReturnTrueForSameObject() {
        Position p = new Position(10.0, 20.0);
        assertTrue(p.equals(p));
    }

    @Test
    void equals_shouldReturnTrueForEqualValues() {
        Position p1 = new Position(10.0, 20.0);
        Position p2 = new Position(10.0, 20.0);
        assertTrue(p1.equals(p2));
    }

    @Test
    void equals_shouldReturnFalseForDifferentX() {
        Position p1 = new Position(10.0, 20.0);
        Position p2 = new Position(15.0, 20.0);
        assertFalse(p1.equals(p2));
    }

    @Test
    void equals_shouldReturnFalseForDifferentY() {
        Position p1 = new Position(10.0, 20.0);
        Position p2 = new Position(10.0, 25.0);
        assertFalse(p1.equals(p2));
    }

    @Test
    void equals_shouldReturnFalseForNonPosition() {
        Position p = new Position(10.0, 20.0);
        assertFalse(p.equals("not a position"));
    }
}