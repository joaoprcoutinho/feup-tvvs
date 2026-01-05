package pt.feup.tvvs.soulknight.model.dataStructs;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class VectorMutationTests {
    
    @Test
    public void constructor_shouldSetXValue() {
        Vector v = new Vector(10.5, 20.5);
        assertEquals(10.5, v.x(), 0.001);
    }

    @Test
    public void constructor_shouldSetYValue() {
        Vector v = new Vector(10.5, 20.5);
        assertEquals(20.5, v.y(), 0.001);
    }

    @Test
    public void x_shouldReturnCorrectValue() {
        Vector v = new Vector(15.0, 25.0);
        assertEquals(15.0, v.x(), 0.001);
    }

    @Test
    public void y_shouldReturnCorrectValue() {
        Vector v = new Vector(15.0, 25.0);
        assertEquals(25.0, v.y(), 0.001);
    }

    @Test
    public void x_shouldNotReturnY() {
        Vector v = new Vector(10.0, 20.0);
        assertNotEquals(v.y(), v.x());
    }

    @Test
    public void y_shouldNotReturnX() {
        Vector v = new Vector(10.0, 20.0);
        assertNotEquals(v.x(), v.y());
    }

    @Test
    public void equals_shouldReturnTrueForSameObject() {
        Vector v = new Vector(10.0, 20.0);
        assertTrue(v.equals(v));
    }

    @Test
    public void equals_shouldReturnTrueForEqualValues() {
        Vector v1 = new Vector(10.0, 20.0);
        Vector v2 = new Vector(10.0, 20.0);
        assertTrue(v1.equals(v2));
    }

    @Test
    public void equals_shouldReturnFalseForDifferentX() {
        Vector v1 = new Vector(10.0, 20.0);
        Vector v2 = new Vector(15.0, 20.0);
        assertFalse(v1.equals(v2));
    }

    @Test
    public void equals_shouldReturnFalseForDifferentY() {
        Vector v1 = new Vector(10.0, 20.0);
        Vector v2 = new Vector(10.0, 25.0);
        assertFalse(v1.equals(v2));
    }

    @Test
    public void equals_shouldReturnFalseForNonVector() {
        Vector v = new Vector(10.0, 20.0);
        assertFalse(v.equals("not a vector"));
    }

    @Test
    public void equals_shouldReturnFalseForNull() {
        Vector v = new Vector(10.0, 20.0);
        assertFalse(v.equals(null));
    }
}