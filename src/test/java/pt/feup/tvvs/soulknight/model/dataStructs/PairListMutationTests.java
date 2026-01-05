package pt.feup.tvvs.soulknight.model.dataStructs;
            
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class PairListMutationTests {
    
    @Test
    public void constructor_shouldSetFirstList() {
        List<String> first = List.of("a", "b");
        List<String> second = List.of("c", "d");
        PairList<String> pairList = new PairList<>(first, second);
        
        assertEquals(first, pairList.getFirstList());
    }

    @Test
    public void constructor_shouldSetSecondList() {
        List<String> first = List.of("a", "b");
        List<String> second = List.of("c", "d");
        PairList<String> pairList = new PairList<>(first, second);
        
        assertEquals(second, pairList.getSecondList());
    }

    @Test
    public void getFirstList_shouldNotReturnSecondList() {
        List<String> first = List.of("a", "b");
        List<String> second = List.of("c", "d");
        PairList<String> pairList = new PairList<>(first, second);
        
        assertNotEquals(second, pairList.getFirstList());
    }

    @Test
    public void getSecondList_shouldNotReturnFirstList() {
        List<String> first = List.of("a", "b");
        List<String> second = List.of("c", "d");
        PairList<String> pairList = new PairList<>(first, second);
        
        assertNotEquals(first, pairList.getSecondList());
    }

    @Test
    public void getFirstList_shouldReturnSameInstance() {
        List<String> first = new ArrayList<>(List.of("a", "b"));
        List<String> second = new ArrayList<>(List.of("c", "d"));
        PairList<String> pairList = new PairList<>(first, second);
        
        assertSame(first, pairList.getFirstList());
    }

    @Test
    public void getSecondList_shouldReturnSameInstance() {
        List<String> first = new ArrayList<>(List.of("a", "b"));
        List<String> second = new ArrayList<>(List.of("c", "d"));
        PairList<String> pairList = new PairList<>(first, second);
        
        assertSame(second, pairList.getSecondList());
    }
}