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

    @Test
    public void testMoveAtBoundary2() throws IOException {
        Enemies enemy = mock(Enemies.class);
        Position newPos = new Position(10, 10);
        when(enemy.moveMonster()).thenReturn(newPos);
        when(scene.getMonsters()).thenReturn(List.of(enemy));

        controller.move(game, GUI.ACTION.NULL, 10);
        verify(enemy).setPosition(newPos);
        reset(enemy);
        when(enemy.moveMonster()).thenReturn(newPos);

        controller.move(game, GUI.ACTION.NULL, 12);
        verify(enemy, never()).setPosition(any());
    }

    @Test
    public void testMoveAtBoundary3() throws IOException {
        Enemies enemy = mock(Enemies.class);
        Position newPos = new Position(10, 10);
        when(enemy.moveMonster()).thenReturn(newPos);
        when(scene.getMonsters()).thenReturn(List.of(enemy));

        controller.move(game, GUI.ACTION.NULL, 10);
        verify(enemy).setPosition(newPos);
        reset(enemy);
        when(enemy.moveMonster()).thenReturn(newPos);

        controller.move(game, GUI.ACTION.NULL, 13);
        verify(enemy).setPosition(newPos);
    }

    @Test
    public void testTimeSubtractionNotAddition() throws IOException {
        Enemies enemy = mock(Enemies.class);
        Position newPos = new Position(10, 10);
        when(enemy.moveMonster()).thenReturn(newPos);
        when(scene.getMonsters()).thenReturn(List.of(enemy));

        controller.move(game, GUI.ACTION.NULL, 100);
        verify(enemy).setPosition(newPos);
        reset(enemy);
        when(enemy.moveMonster()).thenReturn(newPos);

        controller.move(game, GUI.ACTION.NULL, 102);
        verify(enemy, never()).setPosition(any());
        
        reset(enemy);
        when(enemy.moveMonster()).thenReturn(newPos);

        controller.move(game, GUI.ACTION.NULL, 103);
        verify(enemy).setPosition(newPos);
    }

    @Test
    public void testLastMovementUpdated() throws IOException {
        Enemies enemy = mock(Enemies.class);
        Position newPos = new Position(10, 10);
        when(enemy.moveMonster()).thenReturn(newPos);
        when(scene.getMonsters()).thenReturn(List.of(enemy));

        controller.move(game, GUI.ACTION.NULL, 10);
        verify(enemy).setPosition(newPos);
        reset(enemy);
        when(enemy.moveMonster()).thenReturn(newPos);

        controller.move(game, GUI.ACTION.NULL, 11);
        verify(enemy, never()).setPosition(any());
    }

    @Test
    public void testMultipleEnemiesMoved() throws IOException {
        Enemies enemy1 = mock(Enemies.class);
        Enemies enemy2 = mock(Enemies.class);
        Position pos1 = new Position(10, 10);
        Position pos2 = new Position(20, 20);
        when(enemy1.moveMonster()).thenReturn(pos1);
        when(enemy2.moveMonster()).thenReturn(pos2);
        when(scene.getMonsters()).thenReturn(List.of(enemy1, enemy2));

        controller.move(game, GUI.ACTION.NULL, 10);

        verify(enemy1).setPosition(pos1);
        verify(enemy2).setPosition(pos2);
    }

    @Test
    public void testSetPositionCalledWithMoveMonsterResult() throws IOException {
        Enemies enemy = mock(Enemies.class);
        Position expectedPos = new Position(15, 25);
        when(enemy.moveMonster()).thenReturn(expectedPos);
        when(scene.getMonsters()).thenReturn(List.of(enemy));

        controller.move(game, GUI.ACTION.NULL, 10);

        verify(enemy).moveMonster();
        verify(enemy).setPosition(expectedPos);
    }

    @Test
    public void testEmptyEnemyList() throws IOException {
        when(scene.getMonsters()).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> controller.move(game, GUI.ACTION.NULL, 0));
    }

    @Test
    public void testConstructorSetsLastMovementToZero() throws IOException {
        Enemies enemy = mock(Enemies.class);
        Position newPos = new Position(10, 10);
        when(enemy.moveMonster()).thenReturn(newPos);
        when(scene.getMonsters()).thenReturn(List.of(enemy));

        controller.move(game, GUI.ACTION.NULL, 3);
        verify(enemy).setPosition(newPos);
    }

    @Test
    public void testGetModelReturnsScene() {
        assertEquals(scene, controller.getModel());
    }
}