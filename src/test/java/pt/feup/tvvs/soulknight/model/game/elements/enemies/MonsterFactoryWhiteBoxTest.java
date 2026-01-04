package pt.feup.tvvs.soulknight.model.game.elements.enemies;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class MonsterFactoryWhiteBoxTest {
    private Scene scene;

    @BeforeEach
    void setUp() {
        scene = mock(Scene.class);
        when(scene.getWidth()).thenReturn(100);
        when(scene.getHeight()).thenReturn(100);
    }

    @Test
    void testCreateSwordMonster() {
        Enemies enemy = MonsterFactory.createMonster(0, 0, scene, MonsterFactory.SWORD_MONSTER);
        assertEquals(MonsterFactory.SWORD_MONSTER, enemy.getChar());
    }

    @Test
    void testCreateGhostMonster() {
        Enemies enemy = MonsterFactory.createMonster(5, 5, scene, MonsterFactory.GHOST_MONSTER);
        assertEquals(MonsterFactory.GHOST_MONSTER, enemy.getChar());
    }

    @Test
    void testCreatePurpleMonster() {
        Enemies enemy = MonsterFactory.createMonster(10, 10, scene, MonsterFactory.PURPLE_MONSTER);
        assertEquals(MonsterFactory.PURPLE_MONSTER, enemy.getChar());
    }

    @Test
    void testCreateMonsterInvalidTypeReturnsNull() {
        Enemies enemy = MonsterFactory.createMonster(0, 0, scene, 'Z');
        assertNull(enemy);
    }
}