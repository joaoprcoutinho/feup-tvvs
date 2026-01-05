package pt.feup.tvvs.soulknight.controller.game;
            
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.Game;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.dataStructs.Vector;
import pt.feup.tvvs.soulknight.model.game.elements.knight.IdleState;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.model.game.elements.knight.KnightState;
import pt.feup.tvvs.soulknight.model.game.elements.knight.RespawnState;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;

import static org.mockito.Mockito.*;
import java.io.IOException;

public class PlayerControllerWhiteBoxTests {
    private Scene scene;
    private PlayerController controller;
    private Game game;
    private Knight knight;

    @BeforeEach
    void setup() throws IOException {
        scene = mock(Scene.class);
        controller = new PlayerController(scene);
        game = mock(Game.class);
        knight = mock(Knight.class);
        when(scene.getPlayer()).thenReturn(knight);
        when(knight.updatePosition()).thenReturn(new Position(0,0));
        when(knight.getNextState()).thenReturn(mock(KnightState.class));
    }

    @Test
    public void testMoveLeft() throws IOException {
        when(knight.getState()).thenReturn(mock(KnightState.class));
        when(knight.moveLeft()).thenReturn(new Vector(1,0));
        controller.move(game, GUI.ACTION.LEFT, 0);
        verify(knight).setVelocity(new Vector(1,0));
        verify(knight).setFacingRight(false);
    }

    @Test
    public void testMoveRight() throws IOException {
        when(knight.getState()).thenReturn(mock(KnightState.class));
        when(knight.moveRight()).thenReturn(new Vector(1,0));
        controller.move(game, GUI.ACTION.RIGHT, 0);
        verify(knight).setVelocity(new Vector(1,0));
        verify(knight).setFacingRight(true);
    }

    @Test
    public void testMoveJump() throws IOException {
        when(knight.getState()).thenReturn(mock(KnightState.class));
        when(knight.jump()).thenReturn(new Vector(1,0));
        controller.move(game, GUI.ACTION.JUMP, 0);
        verify(knight).setVelocity(new Vector(1,0));
    }

    @Test
    public void testMoveDash() throws IOException {
        when(knight.getState()).thenReturn(mock(KnightState.class));
        when(knight.dash()).thenReturn(new Vector(1,0));
        controller.move(game, GUI.ACTION.DASH, 0);
        verify(knight).setVelocity(new Vector(1,0));
    }

    @Test
    public void testMoveKill() throws IOException {
        when(knight.getState()).thenReturn(mock(KnightState.class));
        controller.move(game, GUI.ACTION.KILL, 0);
        verify(knight).setState(any(RespawnState.class));
    }

    @Test
    public void testMoveDefault() throws IOException {
        when(knight.getState()).thenReturn(mock(KnightState.class));
        when(knight.updateVelocity()).thenReturn(new Vector(1,0));
        controller.move(game, GUI.ACTION.UP, 0);
        verify(knight).setVelocity(new Vector(1,0));

    }

    @Test
    public void testKnightStateNull() throws IOException{
        when(knight.getState()).thenReturn(null);
        controller.move(game, GUI.ACTION.UP, 0);
        verify(knight).setState(any(IdleState.class));
    }

    @AfterEach
    void afterTest() {
        verify(knight).setPosition(new Position(0,0));
        verify(knight).setScene(scene);
        verify(knight, atLeastOnce()).setState(any(KnightState.class));
    }

}