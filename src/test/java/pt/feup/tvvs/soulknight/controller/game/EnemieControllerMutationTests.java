package pt.feup.tvvs.soulknight.controller.game;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.Game;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.enemies.Enemies;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EnemieControllerMutationTests {

    private Scene scene;
    private Game game;
    private EnemieController controller;

    @BeforeEach
    void setUp() {
        scene = mock(Scene.class);
        game = mock(Game.class);
        controller = new EnemieController(scene);
    }

    // Test time - lastMovement > 2 conditional boundary
    // Kills mutation: "changed conditional boundary" (> vs >=)
    @Test
    void testMoveAtBoundary2() throws IOException {
        Enemies enemy = mock(Enemies.class);
        Position newPos = new Position(10, 10);
        when(enemy.moveMonster()).thenReturn(newPos);
        when(scene.getMonsters()).thenReturn(List.of(enemy));

        // First call at time 10 to trigger movement (10 - 0 = 10 > 2)
        controller.move(game, GUI.ACTION.NULL, 10);
        verify(enemy).setPosition(newPos);
        reset(enemy);
        when(enemy.moveMonster()).thenReturn(newPos);

        // At time 12: 12 - 10 = 2, NOT > 2, so should NOT move
        controller.move(game, GUI.ACTION.NULL, 12);
        verify(enemy, never()).setPosition(any());
    }

    @Test
    void testMoveAtBoundary3() throws IOException {
        Enemies enemy = mock(Enemies.class);
        Position newPos = new Position(10, 10);
        when(enemy.moveMonster()).thenReturn(newPos);
        when(scene.getMonsters()).thenReturn(List.of(enemy));

        // First call at time 10 to trigger movement (10 - 0 = 10 > 2)
        controller.move(game, GUI.ACTION.NULL, 10);
        verify(enemy).setPosition(newPos);
        reset(enemy);
        when(enemy.moveMonster()).thenReturn(newPos);

        // At time 13: 13 - 10 = 3 > 2, so SHOULD move
        controller.move(game, GUI.ACTION.NULL, 13);
        verify(enemy).setPosition(newPos);
    }

    // Test time - lastMovement subtraction (not addition)
    // Kills mutation: "Replaced long subtraction with addition"
    @Test
    void testTimeSubtractionNotAddition() throws IOException {
        Enemies enemy = mock(Enemies.class);
        Position newPos = new Position(10, 10);
        when(enemy.moveMonster()).thenReturn(newPos);
        when(scene.getMonsters()).thenReturn(List.of(enemy));

        // First call at time 100
        controller.move(game, GUI.ACTION.NULL, 100);
        verify(enemy).setPosition(newPos);
        reset(enemy);
        when(enemy.moveMonster()).thenReturn(newPos);

        // At time 102: 102 - 100 = 2, NOT > 2
        controller.move(game, GUI.ACTION.NULL, 102);
        verify(enemy, never()).setPosition(any());
        
        reset(enemy);
        when(enemy.moveMonster()).thenReturn(newPos);

        // At time 103: 103 - 100 = 3 > 2, SHOULD move
        // If addition was used: 103 + 100 = 203 > 2, would always move
        controller.move(game, GUI.ACTION.NULL, 103);
        verify(enemy).setPosition(newPos);
    }

    // Test lastMovement is updated
    @Test
    void testLastMovementUpdated() throws IOException {
        Enemies enemy = mock(Enemies.class);
        Position newPos = new Position(10, 10);
        when(enemy.moveMonster()).thenReturn(newPos);
        when(scene.getMonsters()).thenReturn(List.of(enemy));

        // First call at time 10 (10 - 0 = 10 > 2, triggers movement)
        controller.move(game, GUI.ACTION.NULL, 10);
        verify(enemy).setPosition(newPos);
        reset(enemy);
        when(enemy.moveMonster()).thenReturn(newPos);

        // lastMovement should be updated to 10
        // At time 11: 11 - 10 = 1, NOT > 2, should NOT move
        controller.move(game, GUI.ACTION.NULL, 11);
        verify(enemy, never()).setPosition(any());
    }

    // Test multiple enemies are moved
    @Test
    void testMultipleEnemiesMoved() throws IOException {
        Enemies enemy1 = mock(Enemies.class);
        Enemies enemy2 = mock(Enemies.class);
        Position pos1 = new Position(10, 10);
        Position pos2 = new Position(20, 20);
        when(enemy1.moveMonster()).thenReturn(pos1);
        when(enemy2.moveMonster()).thenReturn(pos2);
        when(scene.getMonsters()).thenReturn(List.of(enemy1, enemy2));

        // Use time > 2 to trigger movement (10 - 0 = 10 > 2)
        controller.move(game, GUI.ACTION.NULL, 10);

        verify(enemy1).setPosition(pos1);
        verify(enemy2).setPosition(pos2);
    }

    // Test setPosition is called with moveMonster result
    @Test
    void testSetPositionCalledWithMoveMonsterResult() throws IOException {
        Enemies enemy = mock(Enemies.class);
        Position expectedPos = new Position(15, 25);
        when(enemy.moveMonster()).thenReturn(expectedPos);
        when(scene.getMonsters()).thenReturn(List.of(enemy));

        // Use time > 2 to trigger movement (10 - 0 = 10 > 2)
        controller.move(game, GUI.ACTION.NULL, 10);

        verify(enemy).moveMonster();
        verify(enemy).setPosition(expectedPos);
    }

    // Test empty enemy list
    @Test
    void testEmptyEnemyList() throws IOException {
        when(scene.getMonsters()).thenReturn(new ArrayList<>());
        
        // Should not throw
        assertDoesNotThrow(() -> controller.move(game, GUI.ACTION.NULL, 0));
    }

    // Test constructor sets lastMovement to 0
    @Test
    void testConstructorSetsLastMovementToZero() throws IOException {
        Enemies enemy = mock(Enemies.class);
        Position newPos = new Position(10, 10);
        when(enemy.moveMonster()).thenReturn(newPos);
        when(scene.getMonsters()).thenReturn(List.of(enemy));

        // At time 3: 3 - 0 = 3 > 2 (lastMovement was initialized to 0)
        controller.move(game, GUI.ACTION.NULL, 3);
        verify(enemy).setPosition(newPos);
    }

    // Test getModel returns scene
    @Test
    void testGetModelReturnsScene() {
        assertEquals(scene, controller.getModel());
    }
}