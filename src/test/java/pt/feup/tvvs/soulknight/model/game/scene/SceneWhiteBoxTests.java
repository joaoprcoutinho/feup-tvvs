package pt.feup.tvvs.soulknight.model.game.scene;
            
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.Element;
import pt.feup.tvvs.soulknight.model.game.elements.Spike;
import pt.feup.tvvs.soulknight.model.game.elements.Tree;
import pt.feup.tvvs.soulknight.model.game.elements.collectables.Collectables;
import pt.feup.tvvs.soulknight.model.game.elements.enemies.Enemies;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.model.game.elements.rocks.Rock;
import pt.feup.tvvs.soulknight.model.game.elements.tile.Tile;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class SceneWhiteBoxTests {
    private Scene scene;
    private Knight knight;

    @BeforeEach
    void setUp() {
        scene = new Scene(20, 20, 1);
        knight = mock(Knight.class);

        when(knight.getPosition()).thenReturn(new Position(5, 5));
        when(knight.getWidth()).thenReturn(1);
        when(knight.getHeight()).thenReturn(1);

        scene.setPlayer(knight);
    }

    @Test
    public void testConstructor() {
        assertEquals(20, scene.getWidth());
        assertEquals(20, scene.getHeight());
        assertEquals(1, scene.getSceneID());
        assertEquals(knight, scene.getPlayer());
    }

    @Test
    public void testSetAndGetTiles() {
        Tile[][] tiles = new Tile[20][20];
        scene.setTiles(tiles);
        assertSame(tiles, scene.getTiles());
    }

    @Test
    public void testSetAndGetTree() {
        Tree[][] trees = new Tree[20][20];
        scene.setTrees(trees);
        assertSame(trees, scene.getTrees());
    }

    @Test
    public void testSetAndGetRock() {
        Rock[][] rocks = new Rock[20][20];
        scene.setRocks(rocks);
        assertSame(rocks, scene.getRocks());
    }

    @Test
    public void testCollidesUpTrue() {
        Element[][] map = new Element[20][20];
        map[0][0] = mock(Element.class);
        scene.setMap(map);

        Position position = new Position(0, 0);
        Position size = new Position(Tile.SIZE, Tile.SIZE);

        assertTrue(scene.collidesUp(position, size));
    }

    @Test
    public void testCollidesUpFalse() {
        Element[][] map = new Element[20][20];
        scene.setMap(map);

        Position position = new Position(5, 5);
        Position size = new Position(2, 2);

        assertFalse(scene.collidesUp(position, size));
    }

    @Test
    public void testCollidesDownTrue() {
        Element[][] map = new Element[20][20];
        map[0][0] = mock(Element.class);
        scene.setMap(map);

        Position position = new Position(0, 0);
        Position size = new Position(Tile.SIZE, Tile.SIZE);

        assertTrue(scene.collidesDown(position, size));
    }

    @Test
    public void testCollidesDownFalse() {
        Element[][] map = new Element[20][20];
        scene.setMap(map);

        Position position = new Position(5, 5);
        Position size = new Position(2, 2);

        assertFalse(scene.collidesDown(position, size));
    }

    @Test
    public void testCollisionOutsideScene() {
        Element[][] map = new Element[20][20];
        scene.setMap(map);

        boolean result = scene.collidesLeft(new Position(-1, 0), new Position(2, 2));
        assertTrue(result);

        assertTrue(scene.collidesLeft(
                new Position(scene.getWidth() - 1, 0),
                new Position(2, 1)
        ));

        assertTrue(scene.collidesUp(
                new Position(0, -1),
                new Position(1, 1)
        ));

        assertTrue(scene.collidesDown(
                new Position(0, scene.getHeight() - 1),
                new Position(1, 2)
        ));
    }

    @Test
    public void testCollisionWithMapElement() {
        Element[][] map = new Element[20][20];
        map[0][0] = mock(Element.class);
        scene.setMap(map);

        boolean result = scene.collidesLeft(new Position(0, 0), new Position(Tile.SIZE, Tile.SIZE));
        assertTrue(result);
    }

    @Test
    public void testNoCollisionWithEmptyMap() {
        Element[][] map = new Element[20][20];
        scene.setMap(map);

        boolean result = scene.collidesRight(new Position(5, 5), new Position(2, 2));
        assertFalse(result);
    }

    @Test
    public void testCollectOrbs() {
        Collectables[][] orbs = new Collectables[20][20];
        Collectables orb = mock(Collectables.class);
        orbs[0][0] = orb;

        when(knight.getPosition()).thenReturn(new Position(0, 0));
        when(knight.getWidth()).thenReturn(1);
        when(knight.getHeight()).thenReturn(1);

        scene.collectOrbs(orbs);

        verify(orb).benefit(knight);
        verify(knight).addOrbs();
        assertNull(orbs[0][0]);
        scene.setOrbs(orbs);
        assertEquals(orbs, scene.getOrbs());
    }

    @Test
    public void testCollideMonstersHit() {
        Enemies enemy = mock(Enemies.class);
        when(enemy.getPosition()).thenReturn(new Position(5, 5));
        when(enemy.getSize()).thenReturn(new Position(2, 2));
        when(enemy.getDamage()).thenReturn(1);

        List<Enemies> enemies = List.of(enemy);

        scene.collideMonsters(enemies);

        verify(knight).PlayerHit(1);
        scene.setMonsters(enemies);
        assertEquals(enemies, scene.getMonsters());
    }

    @Test
    public void testCollideMonstersNoCollision() {
        Enemies enemy = mock(Enemies.class);

        when(knight.getPosition()).thenReturn(new Position(0, 0));
        when(knight.getWidth()).thenReturn(1);
        when(knight.getHeight()).thenReturn(1);

        when(enemy.getPosition()).thenReturn(new Position(10, 10));
        when(enemy.getSize()).thenReturn(new Position(2, 2));

        scene.collideMonsters(List.of(enemy));

        verify(knight, never()).PlayerHit(anyInt());
    }

    @Test
    public void testCollideSpikeTrue() {
        Spike[][] spikes = new Spike[20][20];
        spikes[0][0] = mock(Spike.class);
        scene.setSpikes(spikes);

        when(knight.getPosition()).thenReturn(new Position(0, 0));
        when(knight.getWidth()).thenReturn(Tile.SIZE);
        when(knight.getHeight()).thenReturn(Tile.SIZE);

        assertTrue(scene.collideSpike());
        assertEquals(spikes, scene.getSpikes());
    }

    @Test
    public void testIsAtEndPositionTrueAndFalse() {
        scene.setEndPosition(new Position(10, 0));

        when(knight.getPosition()).thenReturn(new Position(5, 0));
        assertFalse(scene.isAtEndPosition());

        when(knight.getPosition()).thenReturn(new Position(10, 0));
        assertTrue(scene.isAtEndPosition());
    }

    @Test
    public void testParticlesLists() {
        assertNotNull(scene.getParticles());
        scene.setParticles(new ArrayList<>());
        assertNotNull(scene.getParticles());

        scene.setJumpParticles(new ArrayList<>());
        assertNotNull(scene.getJumpParticles());

        scene.setDoubleJumpParticles(new ArrayList<>());
        assertNotNull(scene.getDoubleJumpParticles());

        scene.setDashParticles(new ArrayList<>());
        assertNotNull(scene.getDashParticles());

        scene.setRespawnParticles(new ArrayList<>());
        assertNotNull(scene.getRespawnParticles());
    }

    @Test
    public void testStartPosition() {
        Position start = new Position(1, 1);
        scene.setStartPosition(start);
        assertEquals(start, scene.getStartPosition());
    }

    @Test
    public void testGravityGetter() {
        assertEquals(0.25, scene.getGravity(), 0.001);
    }

    @Test
    public void testCollisionFails_playerLeftOfEnemy() {
        Enemies enemy = mock(Enemies.class);

        when(knight.getPosition()).thenReturn(new Position(0, 0));
        when(knight.getWidth()).thenReturn(1);
        when(knight.getHeight()).thenReturn(1);

        when(enemy.getPosition()).thenReturn(new Position(3, 0));
        when(enemy.getSize()).thenReturn(new Position(2, 2));

        assertFalse(scene.checkCollision(knight, enemy));
    }

    @Test
    public void testCollisionFails_playerRightOfEnemy() {
        Enemies enemy = mock(Enemies.class);

        when(knight.getPosition()).thenReturn(new Position(5, 0));
        when(knight.getWidth()).thenReturn(1);

        when(enemy.getPosition()).thenReturn(new Position(0, 0));
        when(enemy.getSize()).thenReturn(new Position(2, 2));

        assertFalse(scene.checkCollision(knight, enemy));
    }

    @Test
    public void testCollisionFails_playerAboveEnemy() {
        Enemies enemy = mock(Enemies.class);

        when(knight.getPosition()).thenReturn(new Position(0, 0));
        when(knight.getHeight()).thenReturn(1);

        when(enemy.getPosition()).thenReturn(new Position(0, 3));
        when(enemy.getSize()).thenReturn(new Position(2, 2));

        assertFalse(scene.checkCollision(knight, enemy));
    }

    @Test
    public void testCollisionFails_playerBelowEnemy() {
        Enemies enemy = mock(Enemies.class);

        when(knight.getPosition()).thenReturn(new Position(0, 5));
        when(knight.getHeight()).thenReturn(1);

        when(enemy.getPosition()).thenReturn(new Position(0, 0));
        when(enemy.getSize()).thenReturn(new Position(2, 2));

        assertFalse(scene.checkCollision(knight, enemy));
    }
}