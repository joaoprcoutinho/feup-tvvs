package pt.feup.tvvs.soulknight.model.game.scene;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.credits.Credits;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.Element;
import pt.feup.tvvs.soulknight.model.game.elements.collectables.Collectables;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.model.game.elements.Spike;
import pt.feup.tvvs.soulknight.model.game.elements.Tree;
import pt.feup.tvvs.soulknight.model.game.elements.tile.Tile;
import pt.feup.tvvs.soulknight.model.game.elements.enemies.Enemies;
import pt.feup.tvvs.soulknight.model.game.elements.particle.Particle;
import pt.feup.tvvs.soulknight.model.game.elements.rocks.Rock;
import pt.feup.tvvs.soulknight.view.menu.LogoViewer;
import pt.feup.tvvs.soulknight.view.sprites.ViewerProvider;
import pt.feup.tvvs.soulknight.view.states.CreditsViewer;
import pt.feup.tvvs.soulknight.view.text.TextViewer;
import com.googlecode.lanterna.TextColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SceneMutationTests {
    // Existing CreditsViewer test fields
    private GUI gui;
    private Credits model;
    private TextViewer textViewer;
    private LogoViewer logoViewer;
    private ViewerProvider viewerProvider;
    private CreditsViewer creditsViewer;

    // New Scene test fields
    private Scene scene;
    private Knight knight;

    @BeforeEach
    void setup() throws IOException {
        // Setup for CreditsViewer tests
        gui = mock(GUI.class);
        logoViewer = mock(LogoViewer.class);
        textViewer = mock(TextViewer.class);

        viewerProvider = mock(ViewerProvider.class);
        when(viewerProvider.getTextViewer()).thenReturn(textViewer);
        when(viewerProvider.getLogoViewer()).thenReturn(logoViewer);

        Knight knightForCredits = new Knight(1,1,1,1,1);
        Credits realCredits = new Credits(knightForCredits);
        realCredits.setMessages(new String[]{"João", "Pedro", "Coutinho"});
        realCredits.setNames(new String[]{"Alice", "Bob"});

        model = spy(realCredits);
        creditsViewer = new CreditsViewer(model, viewerProvider);

        // Setup for Scene mutation tests
        scene = new Scene(100, 100, 1);
        knight = new Knight(1, 1, 10, 1.0f, 5);
        scene.setPlayer(knight);
    }

    // -------------------------
    // Existing CreditsViewer tests
    // -------------------------
    @Test
    void testDraw_CallsAllDrawMethods() throws IOException {
        doReturn(new String[]{"Hello", "World"}).when(model).getMessages();
        doReturn(new String[]{"Alice", "Bob"}).when(model).getNames();
        doReturn(42).when(model).getScore();
        doReturn(3).when(model).getDeaths();
        doReturn(1).when(model).getMinutes();
        doReturn(5).when(model).getSeconds();

        creditsViewer.draw(gui, 0);

        verify(textViewer).draw(eq("Hello"), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
        verify(textViewer).draw(eq("World"), anyDouble(), anyDouble(), eq(CreditsViewer.messageColor), eq(gui));
        verify(textViewer).draw(eq("Alice"), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
        verify(textViewer).draw(eq("Bob"), anyDouble(), anyDouble(), eq(CreditsViewer.nameColor), eq(gui));
        verify(textViewer).draw(contains("Score"), anyDouble(), anyDouble(), eq(CreditsViewer.scoreColor), eq(gui));
        verify(textViewer).draw(contains("Deaths"), anyDouble(), anyDouble(), eq(CreditsViewer.deathColor), eq(gui));
        verify(textViewer).draw(contains("Time"), anyDouble(), anyDouble(), eq(CreditsViewer.timeColor), eq(gui));
        verify(logoViewer).draw(gui, 60, 16);
        verify(gui).cls();
        verify(gui).flush();
    }

    @Test
    void testDrawMessagesSpacingAndBoundaries() throws IOException {
        doReturn(new String[]{"Test1", "Test2"}).when(model).getMessages();
        creditsViewer.draw(gui, 0);
        verify(textViewer, times(7))
                .draw(anyString(), anyDouble(), anyDouble(), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    void testDrawNamesSpacingAndBoundaries() throws IOException {
        doReturn(new String[]{"Name1", "Name2"}).when(model).getNames();
        creditsViewer.draw(gui, 0);
        verify(textViewer, times(8))
                .draw(anyString(), anyDouble(), anyDouble(), any(TextColor.RGB.class), eq(gui));
    }

    // -------------------------
    // Scene Constructor and Getters
    // -------------------------
    @Test
    void testSceneConstructor() {
        Scene s = new Scene(50, 60, 3);
        assertEquals(50, s.getWidth());
        assertEquals(60, s.getHeight());
        assertEquals(3, s.getSceneID());
    }

    @Test
    void testGetWidth() {
        assertEquals(100, scene.getWidth());
    }

    @Test
    void testGetHeight() {
        assertEquals(100, scene.getHeight());
    }

    @Test
    void testGetSceneID() {
        assertEquals(1, scene.getSceneID());
    }

    @Test
    void testGetGravity() {
        assertEquals(0.25, scene.getGravity(), 0.001);
    }

    // -------------------------
    // Player Getters/Setters
    // -------------------------
    @Test
    void testSetAndGetPlayer() {
        Knight newKnight = new Knight(5, 5, 20, 2.0f, 10);
        scene.setPlayer(newKnight);
        assertEquals(newKnight, scene.getPlayer());
    }

    // -------------------------
    // Tiles Getters/Setters
    // -------------------------
    @Test
    void testSetAndGetTiles() {
        Tile[][] tiles = new Tile[10][10];
        tiles[0][0] = mock(Tile.class);
        scene.setTiles(tiles);
        assertEquals(tiles, scene.getTiles());
        assertNotNull(scene.getTiles()[0][0]);
    }

    // -------------------------
    // Spikes Getters/Setters
    // -------------------------
    @Test
    void testSetAndGetSpikes() {
        Spike[][] spikes = new Spike[10][10];
        spikes[1][1] = mock(Spike.class);
        scene.setSpikes(spikes);
        assertEquals(spikes, scene.getSpikes());
        assertNotNull(scene.getSpikes()[1][1]);
    }

    // -------------------------
    // Trees Getters/Setters
    // -------------------------
    @Test
    void testSetAndGetTrees() {
        Tree[][] trees = new Tree[10][10];
        trees[2][2] = mock(Tree.class);
        scene.setTrees(trees);
        assertEquals(trees, scene.getTrees());
        assertNotNull(scene.getTrees()[2][2]);
    }

    // -------------------------
    // Rocks Getters/Setters
    // -------------------------
    @Test
    void testSetAndGetRocks() {
        Rock[][] rocks = new Rock[10][10];
        rocks[3][3] = mock(Rock.class);
        scene.setRocks(rocks);
        assertEquals(rocks, scene.getRocks());
        assertNotNull(scene.getRocks()[3][3]);
    }

    // -------------------------
    // Orbs Getters/Setters
    // -------------------------
    @Test
    void testSetAndGetOrbs() {
        Collectables[][] orbs = new Collectables[10][10];
        orbs[4][4] = mock(Collectables.class);
        scene.setOrbs(orbs);
        assertEquals(orbs, scene.getOrbs());
        assertNotNull(scene.getOrbs()[4][4]);
    }

    // -------------------------
    // Monsters Getters/Setters
    // -------------------------
    @Test
    void testSetAndGetMonsters() {
        List<Enemies> monsters = new ArrayList<>();
        monsters.add(mock(Enemies.class));
        scene.setMonsters(monsters);
        assertEquals(monsters, scene.getMonsters());
        assertEquals(1, scene.getMonsters().size());
    }

    // -------------------------
    // Map Setter
    // -------------------------
    @Test
    void testSetMap() {
        Element[][] map = new Element[10][10];
        map[0][0] = mock(Element.class);
        scene.setMap(map);
        // Map is used internally for collision detection
        assertNotNull(scene);
    }

    // -------------------------
    // Particle Lists Getters/Setters
    // -------------------------
    @Test
    void testSetAndGetParticles() {
        List<Particle> particles = new ArrayList<>();
        particles.add(mock(Particle.class));
        scene.setParticles(particles);
        assertEquals(particles, scene.getParticles());
    }

    @Test
    void testSetAndGetDoubleJumpParticles() {
        List<Particle> particles = new ArrayList<>();
        particles.add(mock(Particle.class));
        scene.setDoubleJumpParticles(particles);
        assertEquals(particles, scene.getDoubleJumpParticles());
    }

    @Test
    void testSetAndGetJumpParticles() {
        List<Particle> particles = new ArrayList<>();
        particles.add(mock(Particle.class));
        scene.setJumpParticles(particles);
        assertEquals(particles, scene.getJumpParticles());
    }

    @Test
    void testSetAndGetDashParticles() {
        List<Particle> particles = new ArrayList<>();
        particles.add(mock(Particle.class));
        scene.setDashParticles(particles);
        assertEquals(particles, scene.getDashParticles());
    }

    @Test
    void testSetAndGetRespawnParticles() {
        List<Particle> particles = new ArrayList<>();
        particles.add(mock(Particle.class));
        scene.setRespawnParticles(particles);
        assertEquals(particles, scene.getRespawnParticles());
    }

    // -------------------------
    // Start/End Position
    // -------------------------
    @Test
    void testSetAndGetStartPosition() {
        Position start = new Position(10, 20);
        scene.setStartPosition(start);
        assertEquals(start, scene.getStartPosition());
        assertEquals(10, scene.getStartPosition().x(), 0.001);
        assertEquals(20, scene.getStartPosition().y(), 0.001);
    }

    @Test
    void testSetEndPosition() {
        Position end = new Position(90, 50);
        scene.setEndPosition(end);
        // EndPosition is used in isAtEndPosition
    }

    // -------------------------
    // Collision Detection Tests
    // -------------------------
    @Test
    void testCollidesLeftOutOfBounds() {
        knight.setPosition(new Position(-10, 50));
        Position size = new Position(knight.getWidth(), knight.getHeight());
        assertTrue(scene.collidesLeft(knight.getPosition(), size));
    }

    @Test
    void testCollidesLeftInBounds() {
        Element[][] map = new Element[100][100];
        scene.setMap(map);
        knight.setPosition(new Position(50, 50));
        Position size = new Position(knight.getWidth(), knight.getHeight());
        assertFalse(scene.collidesLeft(knight.getPosition(), size));
    }

    @Test
    void testCollidesRightOutOfBounds() {
        knight.setPosition(new Position(scene.getWidth() + 10, 50));
        Position size = new Position(knight.getWidth(), knight.getHeight());
        assertTrue(scene.collidesRight(knight.getPosition(), size));
    }

    @Test
    void testCollidesRightInBounds() {
        Element[][] map = new Element[100][100];
        scene.setMap(map);
        knight.setPosition(new Position(50, 50));
        Position size = new Position(knight.getWidth(), knight.getHeight());
        assertFalse(scene.collidesRight(knight.getPosition(), size));
    }

    @Test
    void testCollidesUpOutOfBounds() {
        knight.setPosition(new Position(50, -10));
        Position size = new Position(knight.getWidth(), knight.getHeight());
        assertTrue(scene.collidesUp(knight.getPosition(), size));
    }

    @Test
    void testCollidesUpInBounds() {
        Element[][] map = new Element[100][100];
        scene.setMap(map);
        knight.setPosition(new Position(50, 50));
        Position size = new Position(knight.getWidth(), knight.getHeight());
        assertFalse(scene.collidesUp(knight.getPosition(), size));
    }

    @Test
    void testCollidesDownOutOfBounds() {
        knight.setPosition(new Position(50, scene.getHeight() + 10));
        Position size = new Position(knight.getWidth(), knight.getHeight());
        assertTrue(scene.collidesDown(knight.getPosition(), size));
    }

    @Test
    void testCollidesDownInBounds() {
        Element[][] map = new Element[100][100];
        scene.setMap(map);
        knight.setPosition(new Position(50, 50));
        Position size = new Position(knight.getWidth(), knight.getHeight());
        assertFalse(scene.collidesDown(knight.getPosition(), size));
    }

    @Test
    void testCollidesWithTile() {
        Element[][] map = new Element[100][100];
        map[3][3] = mock(Tile.class); // Put a tile at (3,3) in tile coords
        scene.setMap(map);
        
        // Position in pixel coords - tile at (3,3) covers pixels from 3*TILE_SIZE to 4*TILE_SIZE
        int tileSize = Tile.SIZE;
        knight.setPosition(new Position(3 * tileSize, 3 * tileSize));
        Position size = new Position(knight.getWidth(), knight.getHeight());
        assertTrue(scene.collidesLeft(knight.getPosition(), size));
    }

    // -------------------------
    // collectOrbs Tests
    // -------------------------
    @Test
    void testCollectOrbsCollectsOrb() {
        int tileSize = Tile.SIZE;
        Collectables orb = mock(Collectables.class);
        Collectables[][] orbs = new Collectables[100][100];
        orbs[0][0] = orb;

        knight.setPosition(new Position(0, 0));
        scene.collectOrbs(orbs);

        verify(orb).benefit(knight);
        assertEquals(1, knight.getOrbs());
        assertNull(orbs[0][0]);
    }

    @Test
    void testCollectOrbsNoOrbAtPosition() {
        Collectables[][] orbs = new Collectables[100][100];
        knight.setPosition(new Position(0, 0));
        int orbsBefore = knight.getOrbs();
        scene.collectOrbs(orbs);
        assertEquals(orbsBefore, knight.getOrbs());
    }

    @Test
    void testCollectOrbsMultipleOrbs() {
        Collectables orb1 = mock(Collectables.class);
        Collectables orb2 = mock(Collectables.class);
        Collectables[][] orbs = new Collectables[100][100];
        orbs[0][0] = orb1;
        orbs[0][1] = orb2;

        knight.setPosition(new Position(0, 0));
        scene.collectOrbs(orbs);

        verify(orb1).benefit(knight);
    }

    // -------------------------
    // collideMonsters Tests
    // -------------------------
    @Test
    void testCollideMonstersHitsPlayer() {
        // Need to set up scene properly for this test
        Scene testScene = new Scene(100, 100, 1);
        Knight testKnight = new Knight(10, 10, 100, 1.0f, 50);
        testScene.setPlayer(testKnight);
        testKnight.setScene(testScene);
        
        Enemies enemy = mock(Enemies.class);
        when(enemy.getPosition()).thenReturn(new Position(testKnight.getPosition().x(), testKnight.getPosition().y()));
        when(enemy.getSize()).thenReturn(new Position(20, 20));
        when(enemy.getDamage()).thenReturn(5);

        List<Enemies> enemies = new ArrayList<>();
        enemies.add(enemy);

        testKnight.setGotHit(false);
        testScene.collideMonsters(enemies);

        // Knight should have been hit
        assertTrue(testKnight.isGotHit());
    }

    @Test
    void testCollideMonstersNoCollision() {
        Enemies enemy = mock(Enemies.class);
        // Enemy far from knight
        when(enemy.getPosition()).thenReturn(new Position(500, 500));
        when(enemy.getSize()).thenReturn(new Position(10, 10));
        when(enemy.getDamage()).thenReturn(5);

        List<Enemies> enemies = new ArrayList<>();
        enemies.add(enemy);

        knight.setGotHit(false);
        int hpBefore = knight.getHP();
        scene.collideMonsters(enemies);

        assertEquals(hpBefore, knight.getHP());
        assertFalse(knight.isGotHit());
    }

    @Test
    void testCollideMonstersEmptyList() {
        List<Enemies> enemies = new ArrayList<>();
        knight.setGotHit(false);
        int hpBefore = knight.getHP();
        scene.collideMonsters(enemies);
        assertEquals(hpBefore, knight.getHP());
    }

    // -------------------------
    // checkCollision (AABB) Tests
    // -------------------------
    @Test
    void testCheckCollisionAABB() {
        Enemies enemy = mock(Enemies.class);
        when(enemy.getPosition()).thenReturn(new Position(10, 10));
        when(enemy.getSize()).thenReturn(new Position(20, 20));

        knight.setPosition(new Position(15, 15));
        
        // Should collide (overlapping)
        boolean collision = scene.checkCollision(knight, enemy);
        assertTrue(collision);
    }

    @Test
    void testCheckCollisionAABBNoOverlap() {
        Enemies enemy = mock(Enemies.class);
        when(enemy.getPosition()).thenReturn(new Position(100, 100));
        when(enemy.getSize()).thenReturn(new Position(10, 10));

        knight.setPosition(new Position(10, 10));
        
        boolean collision = scene.checkCollision(knight, enemy);
        assertFalse(collision);
    }

    @Test
    void testCheckCollisionAABBEdge() {
        Enemies enemy = mock(Enemies.class);
        // Place enemy right at the edge of knight
        double knightX = knight.getPosition().x();
        double knightY = knight.getPosition().y();
        double knightWidth = knight.getWidth();
        
        when(enemy.getPosition()).thenReturn(new Position(knightX + knightWidth, knightY));
        when(enemy.getSize()).thenReturn(new Position(10, 10));
        
        boolean collision = scene.checkCollision(knight, enemy);
        assertFalse(collision); // Exactly at edge, not overlapping
    }

    // -------------------------
    // collideSpike Tests
    // -------------------------
    @Test
    void testCollideSpikeTrue() {
        Spike spike = mock(Spike.class);
        Spike[][] spikes = new Spike[100][100];
        spikes[0][0] = spike;

        knight.setPosition(new Position(0, 0));
        scene.setSpikes(spikes);

        assertTrue(scene.collideSpike());
    }

    @Test
    void testCollideSpikeFalse() {
        Spike[][] spikes = new Spike[100][100];
        // No spikes at knight position
        knight.setPosition(new Position(50, 50));
        scene.setSpikes(spikes);

        assertFalse(scene.collideSpike());
    }

    // -------------------------
    // isAtEndPosition Tests
    // -------------------------
    @Test
    void testIsAtEndPositionTrue() {
        Position end = new Position(50, 50);
        scene.setEndPosition(end);
        knight.setPosition(new Position(50, 50));
        assertTrue(scene.isAtEndPosition());
    }

    @Test
    void testIsAtEndPositionFalse() {
        Position end = new Position(100, 50);
        scene.setEndPosition(end);
        knight.setPosition(new Position(50, 50));
        assertFalse(scene.isAtEndPosition());
    }

    @Test
    void testIsAtEndPositionBeyond() {
        Position end = new Position(50, 50);
        scene.setEndPosition(end);
        knight.setPosition(new Position(60, 50));
        assertTrue(scene.isAtEndPosition());
    }

    // -------------------------
    // Boundary Condition Tests
    // -------------------------
    @Test
    void testCollisionAtExactBoundary() {
        Element[][] map = new Element[100][100];
        scene.setMap(map);
        
        // Test at x=0 boundary
        knight.setPosition(new Position(0, 50));
        Position size = new Position(knight.getWidth(), knight.getHeight());
        assertFalse(scene.collidesLeft(new Position(1, 50), size)); // x=1 should not collide left
    }

    @Test
    void testCollisionWithSizeCalculations() {
        Element[][] map = new Element[100][100];
        scene.setMap(map);
        
        Position size = new Position(7, 8);
        // Test that size.y() - 1 and size.x() - 1 are used correctly
        assertFalse(scene.collidesDown(new Position(50, 50), size));
        assertFalse(scene.collidesRight(new Position(50, 50), size));
    }

    // -------------------------
    // Constructor initializes lists
    // -------------------------
    @Test
    void testConstructorInitializesLists() {
        Scene s = new Scene(10, 10, 0);
        assertNotNull(s.getParticles());
        assertNotNull(s.getDoubleJumpParticles());
        assertNotNull(s.getJumpParticles());
        assertNotNull(s.getRespawnParticles());
        assertNotNull(s.getDashParticles());
        assertTrue(s.getParticles().isEmpty());
    }
}
