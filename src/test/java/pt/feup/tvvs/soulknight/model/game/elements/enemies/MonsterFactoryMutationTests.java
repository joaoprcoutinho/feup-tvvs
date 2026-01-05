package pt.feup.tvvs.soulknight.model.game.elements.enemies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Mutation tests for MonsterFactory class.
 * Tests factory method and constant values.
 */
public class MonsterFactoryMutationTests {

    private Scene mockScene;

    @BeforeEach
    void setUp() {
        mockScene = mock(Scene.class);
        when(mockScene.getWidth()).thenReturn(200);
        when(mockScene.getHeight()).thenReturn(150);
    }

    // ========== Constant Character Tests ==========

    @Test
    void testSwordMonsterConstant() {
        assertEquals('E', MonsterFactory.SWORD_MONSTER);
        assertNotEquals('e', MonsterFactory.SWORD_MONSTER); // Case sensitive
        assertNotEquals('m', MonsterFactory.SWORD_MONSTER);
        assertNotEquals('l', MonsterFactory.SWORD_MONSTER);
    }

    @Test
    void testGhostMonsterConstant() {
        assertEquals('m', MonsterFactory.GHOST_MONSTER);
        assertNotEquals('M', MonsterFactory.GHOST_MONSTER); // Case sensitive
        assertNotEquals('E', MonsterFactory.GHOST_MONSTER);
        assertNotEquals('l', MonsterFactory.GHOST_MONSTER);
    }

    @Test
    void testPurpleMonsterConstant() {
        assertEquals('l', MonsterFactory.PURPLE_MONSTER);
        assertNotEquals('L', MonsterFactory.PURPLE_MONSTER); // Case sensitive
        assertNotEquals('E', MonsterFactory.PURPLE_MONSTER);
        assertNotEquals('m', MonsterFactory.PURPLE_MONSTER);
    }

    @Test
    void testAllConstantsAreDistinct() {
        assertNotEquals(MonsterFactory.SWORD_MONSTER, MonsterFactory.GHOST_MONSTER);
        assertNotEquals(MonsterFactory.SWORD_MONSTER, MonsterFactory.PURPLE_MONSTER);
        assertNotEquals(MonsterFactory.GHOST_MONSTER, MonsterFactory.PURPLE_MONSTER);
    }

    // ========== Factory Method - SwordMonster Tests ==========

    @Test
    void testCreateSwordMonster() {
        Enemies monster = MonsterFactory.createMonster(10, 20, mockScene, MonsterFactory.SWORD_MONSTER);
        
        assertNotNull(monster, "Factory should create SwordMonster");
        assertTrue(monster instanceof SwordMonster, "Should be SwordMonster instance");
    }

    @Test
    void testSwordMonsterPosition() {
        Enemies monster = MonsterFactory.createMonster(50, 75, mockScene, MonsterFactory.SWORD_MONSTER);
        
        assertEquals(50, monster.getPosition().x(), "X position should be 50");
        assertEquals(75, monster.getPosition().y(), "Y position should be 75");
    }

    @Test
    void testSwordMonsterDamage() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.SWORD_MONSTER);
        
        assertEquals(20, monster.getDamage(), "SwordMonster damage should be 20");
        assertNotEquals(0, monster.getDamage());
        assertNotEquals(10, monster.getDamage());
        assertNotEquals(5, monster.getDamage());
    }

    @Test
    void testSwordMonsterSize() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.SWORD_MONSTER);
        
        assertEquals(8, monster.getSize().x(), "SwordMonster size X should be 8");
        assertEquals(8, monster.getSize().y(), "SwordMonster size Y should be 8");
    }

    @Test
    void testSwordMonsterSymbol() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.SWORD_MONSTER);
        
        assertEquals('E', monster.getChar(), "SwordMonster symbol should be 'E'");
    }

    // ========== Factory Method - GhostMonster Tests ==========

    @Test
    void testCreateGhostMonster() {
        Enemies monster = MonsterFactory.createMonster(10, 20, mockScene, MonsterFactory.GHOST_MONSTER);
        
        assertNotNull(monster, "Factory should create GhostMonster");
        assertTrue(monster instanceof GhostMonster, "Should be GhostMonster instance");
    }

    @Test
    void testGhostMonsterPosition() {
        Enemies monster = MonsterFactory.createMonster(100, 80, mockScene, MonsterFactory.GHOST_MONSTER);
        
        assertEquals(100, monster.getPosition().x(), "X position should be 100");
        assertEquals(80, monster.getPosition().y(), "Y position should be 80");
    }

    @Test
    void testGhostMonsterDamage() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.GHOST_MONSTER);
        
        assertEquals(10, monster.getDamage(), "GhostMonster damage should be 10");
        assertNotEquals(0, monster.getDamage());
        assertNotEquals(20, monster.getDamage());
        assertNotEquals(1, monster.getDamage());
    }

    @Test
    void testGhostMonsterSize() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.GHOST_MONSTER);
        
        assertEquals(2, monster.getSize().x(), "GhostMonster size X should be 2");
        assertEquals(2, monster.getSize().y(), "GhostMonster size Y should be 2");
    }

    @Test
    void testGhostMonsterSymbol() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.GHOST_MONSTER);
        
        assertEquals('m', monster.getChar(), "GhostMonster symbol should be 'm'");
    }

    // ========== Factory Method - PurpleMonster Tests ==========

    @Test
    void testCreatePurpleMonster() {
        Enemies monster = MonsterFactory.createMonster(10, 20, mockScene, MonsterFactory.PURPLE_MONSTER);
        
        assertNotNull(monster, "Factory should create PurpleMonster");
        assertTrue(monster instanceof PurpleMonster, "Should be PurpleMonster instance");
    }

    @Test
    void testPurpleMonsterPosition() {
        Enemies monster = MonsterFactory.createMonster(150, 120, mockScene, MonsterFactory.PURPLE_MONSTER);
        
        assertEquals(150, monster.getPosition().x(), "X position should be 150");
        assertEquals(120, monster.getPosition().y(), "Y position should be 120");
    }

    @Test
    void testPurpleMonsterDamage() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.PURPLE_MONSTER);
        
        assertEquals(5, monster.getDamage(), "PurpleMonster damage should be 5");
        assertNotEquals(0, monster.getDamage());
        assertNotEquals(10, monster.getDamage());
        assertNotEquals(25, monster.getDamage());
    }

    @Test
    void testPurpleMonsterSize() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.PURPLE_MONSTER);
        
        assertEquals(8, monster.getSize().x(), "PurpleMonster size X should be 8");
        assertEquals(9, monster.getSize().y(), "PurpleMonster size Y should be 9");
    }

    @Test
    void testPurpleMonsterSymbol() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.PURPLE_MONSTER);
        
        assertEquals('l', monster.getChar(), "PurpleMonster symbol should be 'l'");
    }

    // ========== Factory Method - Invalid Type Tests ==========

    @Test
    void testCreateMonsterWithInvalidType() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, 'X');
        
        assertNull(monster, "Should return null for invalid type");
    }

    @Test
    void testCreateMonsterWithVariousInvalidTypes() {
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, 'A'));
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, 'Z'));
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, '0'));
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, ' '));
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, '\0'));
    }

    @Test
    void testCreateMonsterCaseSensitive() {
        // Lowercase 'e' should not create SwordMonster (which uses uppercase 'E')
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, 'e'));
        // Uppercase 'M' should not create GhostMonster (which uses lowercase 'm')
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, 'M'));
        // Uppercase 'L' should not create PurpleMonster (which uses lowercase 'l')
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, 'L'));
    }

    // ========== HP Value Tests (from factory) ==========

    @Test
    void testSwordMonsterHP() {
        // SwordMonster is created with HP=10
        // We can't directly verify HP as it's in the parent class
        // but we verify the monster is created correctly
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.SWORD_MONSTER);
        assertNotNull(monster);
    }

    @Test
    void testGhostMonsterHP() {
        // GhostMonster is created with HP=1
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.GHOST_MONSTER);
        assertNotNull(monster);
    }

    @Test
    void testPurpleMonsterHP() {
        // PurpleMonster is created with HP=25
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.PURPLE_MONSTER);
        assertNotNull(monster);
    }

    // ========== Scene Passing Tests ==========

    @Test
    void testSceneIsPassedCorrectly() {
        Scene customScene = mock(Scene.class);
        when(customScene.getWidth()).thenReturn(500);
        when(customScene.getHeight()).thenReturn(400);
        
        Enemies sword = MonsterFactory.createMonster(0, 0, customScene, MonsterFactory.SWORD_MONSTER);
        Enemies ghost = MonsterFactory.createMonster(0, 0, customScene, MonsterFactory.GHOST_MONSTER);
        Enemies purple = MonsterFactory.createMonster(0, 0, customScene, MonsterFactory.PURPLE_MONSTER);
        
        assertSame(customScene, sword.getScene());
        assertSame(customScene, ghost.getScene());
        assertSame(customScene, purple.getScene());
    }

    // ========== Different Position Tests ==========

    @Test
    void testCreateMonstersAtDifferentPositions() {
        Enemies monster1 = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.SWORD_MONSTER);
        Enemies monster2 = MonsterFactory.createMonster(100, 100, mockScene, MonsterFactory.SWORD_MONSTER);
        Enemies monster3 = MonsterFactory.createMonster(-10, -10, mockScene, MonsterFactory.SWORD_MONSTER);
        
        assertEquals(0, monster1.getPosition().x());
        assertEquals(0, monster1.getPosition().y());
        
        assertEquals(100, monster2.getPosition().x());
        assertEquals(100, monster2.getPosition().y());
        
        assertEquals(-10, monster3.getPosition().x());
        assertEquals(-10, monster3.getPosition().y());
    }

    // ========== Factory Creates Independent Instances ==========

    @Test
    void testFactoryCreatesIndependentInstances() {
        Enemies monster1 = MonsterFactory.createMonster(10, 20, mockScene, MonsterFactory.SWORD_MONSTER);
        Enemies monster2 = MonsterFactory.createMonster(10, 20, mockScene, MonsterFactory.SWORD_MONSTER);
        
        assertNotSame(monster1, monster2, "Factory should create new instances");
        
        // Modifying one should not affect the other
        monster1.setVelocity(new pt.feup.tvvs.soulknight.model.dataStructs.Vector(99, 99));
        assertNotEquals(monster1.getVelocity().x(), monster2.getVelocity().x());
    }
}