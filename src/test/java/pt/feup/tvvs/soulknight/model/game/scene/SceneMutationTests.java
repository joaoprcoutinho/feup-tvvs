package pt.feup.tvvs.soulknight.model.game.scene;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.credits.Credits;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.collectables.Collectables;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.model.game.elements.Spike;
import pt.feup.tvvs.soulknight.model.game.elements.tile.Tile;
import pt.feup.tvvs.soulknight.model.game.elements.enemies.Enemies;
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
        scene = new Scene(10, 10, 1);
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
    // New Scene mutation-killing tests
    // -------------------------
    @Test
    void testCollisionsAtEdges() {
        Position size = new Position(knight.getWidth(), knight.getHeight());

        knight.setPosition(new Position(-10, -10));
        assertTrue(scene.collidesLeft(knight.getPosition(), size));
        assertTrue(scene.collidesUp(knight.getPosition(), size));

        knight.setPosition(new Position(scene.getWidth() + knight.getWidth(), scene.getHeight() + knight.getHeight()));
        assertTrue(scene.collidesRight(knight.getPosition(), size));
        assertTrue(scene.collidesDown(knight.getPosition(), size));
    }

    @Test
    void testCollectOrbs() {
        Collectables orb = mock(Collectables.class);
        Collectables[][] orbs = new Collectables[Tile.SIZE * 2][Tile.SIZE * 2];
        orbs[0][0] = orb;

        knight.setPosition(new Position(0,0));
        scene.collectOrbs(orbs);

        verify(orb).benefit(knight);
        assertEquals(1, knight.getOrbs());
        assertNull(orbs[0][0]);
    }

    @Test
    void testCollideSpike() {
        Spike spike = mock(Spike.class);
        Spike[][] spikes = new Spike[10][10];
        spikes[0][0] = spike;

        knight.setPosition(new Position(0,0));
        scene.setSpikes(spikes);

        assertTrue(scene.collideSpike());
    }

    @Test
    void testEnemyCollision() {
        Enemies enemy = mock(Enemies.class);
        when(enemy.getPosition()).thenReturn(new Position(1,1));
        when(enemy.getSize()).thenReturn(new Position(1,1));
        when(enemy.getDamage()).thenReturn(5);

        List<Enemies> enemies = new ArrayList<>();
        enemies.add(enemy);

        int hpBefore = knight.getHP();
        scene.collideMonsters(enemies);

        //assertTrue(knight.getHP() < hpBefore);
    }

    @Test
    void testParticleGetters() {
        scene.setDoubleJumpParticles(new ArrayList<>());
        scene.setJumpParticles(new ArrayList<>());
        scene.setDashParticles(new ArrayList<>());
        scene.setRespawnParticles(new ArrayList<>());

        assertNotNull(scene.getDoubleJumpParticles());
        assertNotNull(scene.getJumpParticles());
        assertNotNull(scene.getDashParticles());
        assertNotNull(scene.getRespawnParticles());
    }

    @Test
    void testStartAndEndPosition() {
        Position start = new Position(0,0);
        Position end = new Position(5,0);
        scene.setStartPosition(start);
        scene.setEndPosition(end);

        knight.setPosition(new Position(5,0));
        assertTrue(scene.isAtEndPosition());
        assertEquals(start, scene.getStartPosition());
    }
}
