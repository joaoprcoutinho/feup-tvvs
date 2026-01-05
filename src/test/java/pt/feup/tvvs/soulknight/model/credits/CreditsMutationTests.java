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

    // Test score calculation: energy - deaths (subtraction, not addition)
    @Test
    void testScoreCalculationSubtractsDeaths() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(5);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        
        // Score = 100 - 5 = 95 (tests subtraction operator)
        assertEquals(95, credits.getScore());
    }

    // Test score with zero deaths
    @Test
    void testScoreWithZeroDeaths() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        
        assertEquals(100, credits.getScore());
    }

    // Test score with more deaths than energy (negative score)
    @Test
    void testScoreWithMoreDeathsThanEnergy() {
        when(player.getEnergy()).thenReturn(10);
        when(player.getNumberOfDeaths()).thenReturn(20);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        
        assertEquals(-10, credits.getScore());
    }

    // Test deaths getter returns actual value
    @Test
    void testGetDeathsReturnsActualValue() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(7);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        
        assertEquals(7, credits.getDeaths());
    }

    // Test seconds calculation using modulo 60
    @Test
    void testSecondsCalculationModulo() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        // 125 seconds = 2 minutes and 5 seconds
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 125000);
        
        credits = new Credits(player);
        
        // Seconds = (125000 / 1000) % 60 = 125 % 60 = 5
        assertEquals(5, credits.getSeconds());
    }

    // Test minutes calculation using division
    @Test
    void testMinutesCalculationDivision() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        // 125 seconds = 2 minutes and 5 seconds
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 125000);
        
        credits = new Credits(player);
        
        // Minutes = (125000 / 1000) / 60 = 125 / 60 = 2
        assertEquals(2, credits.getMinutes());
    }

    // Test seconds at boundary (exactly 60 seconds)
    @Test
    void testSecondsAtSixtySecondsBoundary() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        // Exactly 60 seconds = 1 minute, 0 seconds
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 60000);
        
        credits = new Credits(player);
        
        assertEquals(0, credits.getSeconds());
        assertEquals(1, credits.getMinutes());
    }

    // Test time calculation with duration subtraction
    @Test
    void testDurationSubtraction() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        long currentTime = System.currentTimeMillis();
        long birthTime = currentTime - 90000; // 90 seconds ago
        when(player.getBirthTime()).thenReturn(birthTime);
        
        credits = new Credits(player);
        
        // 90 seconds = 1 minute, 30 seconds
        assertEquals(30, credits.getSeconds());
        assertEquals(1, credits.getMinutes());
    }

    // Test setScore mutates the score field
    @Test
    void testSetScoreActuallyChangesScore() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        int originalScore = credits.getScore();
        
        credits.setScore(200);
        
        assertEquals(200, credits.getScore());
        assertNotEquals(originalScore, credits.getScore());
    }

    // Test getScore returns actual score value
    @Test
    void testGetScoreReturnsActualValue() {
        when(player.getEnergy()).thenReturn(50);
        when(player.getNumberOfDeaths()).thenReturn(10);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        
        int score = credits.getScore();
        assertEquals(40, score);
    }

    // Test messages initialization
    @Test
    void testMessagesArrayInitialization() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        
        String[] messages = credits.getMessages();
        assertNotNull(messages);
        assertEquals(1, messages.length);
        assertEquals("Game Over!", messages[0]);
    }

    // Test names array initialization
    @Test
    void testNamesArrayInitialization() {
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

    // Test setMessages changes the messages array
    @Test
    void testSetMessagesChangesArray() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        String[] newMessages = {"Victory!", "Well Done!"};
        
        credits.setMessages(newMessages);
        
        assertArrayEquals(newMessages, credits.getMessages());
    }

    // Test setNames changes the names array
    @Test
    void testSetNamesChangesArray() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 10000);
        
        credits = new Credits(player);
        String[] newNames = {"Developer 1", "Developer 2"};
        
        credits.setNames(newNames);
        
        assertArrayEquals(newNames, credits.getNames());
    }

    // Test seconds calculation with large duration
    @Test
    void testSecondsWithLargeDuration() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        // 305 seconds = 5 minutes and 5 seconds
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 305000);
        
        credits = new Credits(player);
        
        assertEquals(5, credits.getSeconds());
        assertEquals(5, credits.getMinutes());
    }

    // Test time calculation verifies both divisions are used
    @Test
    void testTimeCalculationUsesCorrectDivisions() {
        when(player.getEnergy()).thenReturn(100);
        when(player.getNumberOfDeaths()).thenReturn(0);
        // 3665 seconds = 61 minutes, 5 seconds
        when(player.getBirthTime()).thenReturn(System.currentTimeMillis() - 3665000);
        
        credits = new Credits(player);
        
        // Tests both: (duration / 1000) % 60 and (duration / 1000) / 60
        assertEquals(5, credits.getSeconds());
        assertEquals(61, credits.getMinutes());
    }
}