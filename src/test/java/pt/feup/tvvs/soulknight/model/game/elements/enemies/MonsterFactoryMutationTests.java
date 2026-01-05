package pt.feup.tvvs.soulknight.model.game.elements.enemies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MonsterFactoryMutationTests {

    private Scene mockScene;

    @BeforeEach
    void setUp() {
        mockScene = mock(Scene.class);
        when(mockScene.getWidth()).thenReturn(200);
        when(mockScene.getHeight()).thenReturn(150);
    }

    @Test
    public void testSwordMonsterConstant() {
        assertEquals('E', MonsterFactory.SWORD_MONSTER);
        assertNotEquals('e', MonsterFactory.SWORD_MONSTER);
        assertNotEquals('m', MonsterFactory.SWORD_MONSTER);
        assertNotEquals('l', MonsterFactory.SWORD_MONSTER);
    }

    @Test
    public void testGhostMonsterConstant() {
        assertEquals('m', MonsterFactory.GHOST_MONSTER);
        assertNotEquals('M', MonsterFactory.GHOST_MONSTER);
        assertNotEquals('E', MonsterFactory.GHOST_MONSTER);
        assertNotEquals('l', MonsterFactory.GHOST_MONSTER);
    }

    @Test
    public void testPurpleMonsterConstant() {
        assertEquals('l', MonsterFactory.PURPLE_MONSTER);
        assertNotEquals('L', MonsterFactory.PURPLE_MONSTER);
        assertNotEquals('E', MonsterFactory.PURPLE_MONSTER);
        assertNotEquals('m', MonsterFactory.PURPLE_MONSTER);
    }

    @Test
    public void testAllConstantsAreDistinct() {
        assertNotEquals(MonsterFactory.SWORD_MONSTER, MonsterFactory.GHOST_MONSTER);
        assertNotEquals(MonsterFactory.SWORD_MONSTER, MonsterFactory.PURPLE_MONSTER);
        assertNotEquals(MonsterFactory.GHOST_MONSTER, MonsterFactory.PURPLE_MONSTER);
    }

    @Test
    public void testCreateSwordMonster() {
        Enemies monster = MonsterFactory.createMonster(10, 20, mockScene, MonsterFactory.SWORD_MONSTER);
        
        assertNotNull(monster);
        assertTrue(monster instanceof SwordMonster);
    }

    @Test
    public void testSwordMonsterPosition() {
        Enemies monster = MonsterFactory.createMonster(50, 75, mockScene, MonsterFactory.SWORD_MONSTER);
        
        assertEquals(50, monster.getPosition().x());
        assertEquals(75, monster.getPosition().y());
    }

    @Test
    public void testSwordMonsterDamage() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.SWORD_MONSTER);
        
        assertEquals(20, monster.getDamage());
        assertNotEquals(0, monster.getDamage());
        assertNotEquals(10, monster.getDamage());
        assertNotEquals(5, monster.getDamage());
    }

    @Test
    public void testSwordMonsterSize() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.SWORD_MONSTER);
        
        assertEquals(8, monster.getSize().x());
        assertEquals(8, monster.getSize().y());
    }

    @Test
    public void testSwordMonsterSymbol() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.SWORD_MONSTER);
        assertEquals('E', monster.getChar());
    }

    @Test
    public void testCreateGhostMonster() {
        Enemies monster = MonsterFactory.createMonster(10, 20, mockScene, MonsterFactory.GHOST_MONSTER);
        
        assertNotNull(monster);
        assertTrue(monster instanceof GhostMonster);
    }

    @Test
    public void testGhostMonsterPosition() {
        Enemies monster = MonsterFactory.createMonster(100, 80, mockScene, MonsterFactory.GHOST_MONSTER);
        
        assertEquals(100, monster.getPosition().x());
        assertEquals(80, monster.getPosition().y());
    }

    @Test
    public void testGhostMonsterDamage() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.GHOST_MONSTER);
        
        assertEquals(10, monster.getDamage());
        assertNotEquals(0, monster.getDamage());
        assertNotEquals(20, monster.getDamage());
        assertNotEquals(1, monster.getDamage());
    }

    @Test
    public void testGhostMonsterSize() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.GHOST_MONSTER);
        
        assertEquals(2, monster.getSize().x());
        assertEquals(2, monster.getSize().y());
    }

    @Test
    public void testGhostMonsterSymbol() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.GHOST_MONSTER);
        
        assertEquals('m', monster.getChar());
    }

    @Test
    public void testCreatePurpleMonster() {
        Enemies monster = MonsterFactory.createMonster(10, 20, mockScene, MonsterFactory.PURPLE_MONSTER);
        
        assertNotNull(monster);
        assertTrue(monster instanceof PurpleMonster);
    }

    @Test
    public void testPurpleMonsterPosition() {
        Enemies monster = MonsterFactory.createMonster(150, 120, mockScene, MonsterFactory.PURPLE_MONSTER);
        
        assertEquals(150, monster.getPosition().x());
        assertEquals(120, monster.getPosition().y());
    }

    @Test
    public void testPurpleMonsterDamage() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.PURPLE_MONSTER);
        
        assertEquals(5, monster.getDamage());
        assertNotEquals(0, monster.getDamage());
        assertNotEquals(10, monster.getDamage());
        assertNotEquals(25, monster.getDamage());
    }

    @Test
    public void testPurpleMonsterSize() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.PURPLE_MONSTER);
        
        assertEquals(8, monster.getSize().x());
        assertEquals(9, monster.getSize().y());
    }

    @Test
    public void testPurpleMonsterSymbol() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.PURPLE_MONSTER);
        
        assertEquals('l', monster.getChar());
    }

    @Test
    public void testCreateMonsterWithInvalidType() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, 'X');
        assertNull(monster);
    }

    @Test
    public void testCreateMonsterWithVariousInvalidTypes() {
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, 'A'));
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, 'Z'));
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, '0'));
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, ' '));
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, '\0'));
    }

    @Test
    public void testCreateMonsterCaseSensitive() {
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, 'e'));
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, 'M'));
        assertNull(MonsterFactory.createMonster(0, 0, mockScene, 'L'));
    }

    @Test
    public void testSwordMonsterHP() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.SWORD_MONSTER);
        assertNotNull(monster);
    }

    @Test
    public void testGhostMonsterHP() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.GHOST_MONSTER);
        assertNotNull(monster);
    }

    @Test
    public void testPurpleMonsterHP() {
        Enemies monster = MonsterFactory.createMonster(0, 0, mockScene, MonsterFactory.PURPLE_MONSTER);
        assertNotNull(monster);
    }

    @Test
    public void testSceneIsPassedCorrectly() {
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

    @Test
    public void testCreateMonstersAtDifferentPositions() {
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

    @Test
    public void testFactoryCreatesIndependentInstances() {
        Enemies monster1 = MonsterFactory.createMonster(10, 20, mockScene, MonsterFactory.SWORD_MONSTER);
        Enemies monster2 = MonsterFactory.createMonster(10, 20, mockScene, MonsterFactory.SWORD_MONSTER);
        
        assertNotSame(monster1, monster2);
        
        monster1.setVelocity(new pt.feup.tvvs.soulknight.model.dataStructs.Vector(99, 99));
        assertNotEquals(monster1.getVelocity().x(), monster2.getVelocity().x());
    }
}