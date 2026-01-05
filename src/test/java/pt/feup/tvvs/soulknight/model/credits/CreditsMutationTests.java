package pt.feup.tvvs.soulknight.model.credits;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreditsMutationTests {
    
    private Knight player;
    private Credits credits;

    @BeforeEach
    void setUp() {
        player = mock(Knight.class);
    }

    @Test
    public void testScoreCalculationSubtractsDeaths() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(5);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        assertEquals(95, credits.getScore());
    }

    @Test
    public void testScoreWithZeroDeaths() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        
        assertEquals(100, credits.getScore());
    }

    @Test
    public void testScoreWithMoreDeathsThanEnergy() {
        when(player.getEnergy()).thenReturn(10);
        when(player.getNumberOfDeaths()).thenReturn(20);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        
        assertEquals(-10, credits.getScore());
    }

    @Test
    public void testGetDeathsReturnsActualValue() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(7);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        
        assertEquals(7, credits.getDeaths());
    }

    @Test
    public void testSecondsCalculationModulo() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 125000);
        
        credits = new Credits(player);
        
        assertEquals(5, credits.getSeconds());
    }

    @Test
    public void testMinutesCalculationDivision() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 125000);
        
        credits = new Credits(player);
        
        assertEquals(2, credits.getMinutes());
    }

    @Test
    public void testSecondsAtSixtySecondsBoundary() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 60000);
        
        credits = new Credits(player);
        
        assertEquals(0, credits.getSeconds());
        assertEquals(1, credits.getMinutes());
    }

    @Test
    public void testDurationSubtraction() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        long currentTime = System.currentTimeMillis();
        long birthTime = currentTime - 90000; // 90 seconds ago
        when(player.getBirthTime()).thenReturn(birthTime);
        
        credits = new Credits(player);
        
        assertEquals(30, credits.getSeconds());
        assertEquals(1, credits.getMinutes());
    }

    @Test
    public void testSetScoreActuallyChangesScore() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        int originalScore = credits.getScore();
        
        credits.setScore(200);
        
        assertEquals(200, credits.getScore());
        assertNotEquals(originalScore, credits.getScore());
    }

    @Test
    public void testGetScoreReturnsActualValue() {
        when(player.getEnergy()).thenReturn(50);
        when(player.getNumberOfDeaths()).thenReturn(10);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        
        int score = credits.getScore();
        assertEquals(40, score);
    }

    @Test
    public void testMessagesArrayInitialization() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        
        String[] messages = credits.getMessages();
        assertNotNull(messages);
        assertEquals(1, messages.length);
        assertEquals("Game Over!", messages[0]);
    }

    @Test
    public void testNamesArrayInitialization() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        
        String[] names = credits.getNames();
        assertNotNull(names);
        assertEquals(4, names.length);
        assertEquals("Made By:", names[0]);
        assertEquals("Andre Freitas", names[1]);
        assertEquals("Joao Furtado", names[2]);
        assertEquals("Joao Santos", names[3]);
    }

    @Test
    public void testSetMessagesChangesArray() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        String[] newMessages = {"Victory!", "Well Done!"};
        
        credits.setMessages(newMessages);
        
        assertArrayEquals(newMessages, credits.getMessages());
    }

    @Test
    public void testSetNamesChangesArray() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        String[] newNames = {"Developer 1", "Developer 2"};
        
        credits.setNames(newNames);
        
        assertArrayEquals(newNames, credits.getNames());
    }

    @Test
    public void testSecondsWithLargeDuration() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 305000);
        
        credits = new Credits(player);
        
        assertEquals(5, credits.getSeconds());
        assertEquals(5, credits.getMinutes());
    }

    @Test
    public void testTimeCalculationUsesCorrectDivisions() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 3665000);
        
        credits = new Credits(player);
        
        assertEquals(5, credits.getSeconds());
        assertEquals(61, credits.getMinutes());
    }
}