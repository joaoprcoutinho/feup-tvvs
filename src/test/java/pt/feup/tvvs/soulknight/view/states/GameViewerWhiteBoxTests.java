package pt.feup.tvvs.soulknight.view.states;

import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.credits.Credits;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.Spike;
import pt.feup.tvvs.soulknight.model.game.elements.Tree;
import pt.feup.tvvs.soulknight.model.game.elements.collectables.Collectables;
import pt.feup.tvvs.soulknight.model.game.elements.enemies.Enemies;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.model.game.elements.particle.Particle;
import pt.feup.tvvs.soulknight.model.game.elements.rocks.Rock;
import pt.feup.tvvs.soulknight.model.game.elements.tile.Tile;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;
import pt.feup.tvvs.soulknight.view.elements.collectables.OrbViewer;
import pt.feup.tvvs.soulknight.view.elements.knight.KnightViewer;
import pt.feup.tvvs.soulknight.view.elements.monsters.MonsterViewer;
import pt.feup.tvvs.soulknight.view.elements.particle.ParticleViewer;
import pt.feup.tvvs.soulknight.view.elements.rocks.RockViewer;
import pt.feup.tvvs.soulknight.view.elements.spike.SpikeViewer;
import pt.feup.tvvs.soulknight.view.elements.tile.TileViewer;
import pt.feup.tvvs.soulknight.view.elements.tree.TreeViewer;
import pt.feup.tvvs.soulknight.view.menu.LogoViewer;
import pt.feup.tvvs.soulknight.view.sprites.ViewerProvider;
import pt.feup.tvvs.soulknight.view.text.TextViewer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameViewerWhiteBoxTests {
    private GameViewer gameViewer;
    private ParticleViewer particleViewer;
    private Scene scene;
    private ViewerProvider viewerProvider;
    private GUI gui;
    
    @BeforeEach
    void setUp() throws IOException {
        scene = mock(Scene.class);
        gui = mock(GUI.class);
        viewerProvider = mock(ViewerProvider.class);

        particleViewer = mock(ParticleViewer.class);
        KnightViewer knightViewer = mock(KnightViewer.class);
        TileViewer tileViewer = mock(TileViewer.class);
        SpikeViewer spikeViewer = mock(SpikeViewer.class);
        TreeViewer treeViewer = mock(TreeViewer.class);
        OrbViewer orbViewer = mock(OrbViewer.class);
        RockViewer rockViewer = mock(RockViewer.class);
        MonsterViewer monsterViewer = mock(MonsterViewer.class);

        when(viewerProvider.getParticleViewer()).thenReturn(particleViewer);
        when(viewerProvider.getPlayerViewer()).thenReturn(knightViewer);
        when(viewerProvider.getTileViewer()).thenReturn(tileViewer);
        when(viewerProvider.getSpikeViewer()).thenReturn(spikeViewer);
        when(viewerProvider.getTreeViewer()).thenReturn(treeViewer);
        when(viewerProvider.getOrbViewer()).thenReturn(orbViewer);
        when(viewerProvider.getRockViewer()).thenReturn(rockViewer);
        when(viewerProvider.getMonsterViewer()).thenReturn(monsterViewer);

        Knight knight = mock(Knight.class);
        when(knight.getPosition()).thenReturn(new Position(10.0, 20.0));

        Particle particle = mock(Particle.class);
        when(particle.getPosition()).thenReturn(new Position(5.0, 5.0));

        Enemies enemies = mock((Enemies.class));
        when(enemies.getPosition()).thenReturn(new Position(5.0, 5.0));

        when(scene.getPlayer()).thenReturn(knight);
        when(scene.getParticles()).thenReturn(List.of(particle));
        when(scene.getDoubleJumpParticles()).thenReturn(List.of(particle));
        when(scene.getJumpParticles()).thenReturn(List.of(particle));
        when(scene.getRespawnParticles()).thenReturn(List.of(particle));
        when(scene.getDashParticles()).thenReturn(List.of(particle));
        when(scene.getMonsters()).thenReturn(List.of(enemies));

        Spike spike = mock(Spike.class);
        Tile tile = mock(Tile.class);
        Tree tree = mock(Tree.class);
        Collectables collectables = mock(Collectables.class);
        Rock rock = mock(Rock.class);

        Spike[][] spikes = new Spike[][]{{spike}};
        Tile[][] tiles = new Tile[][]{{tile}};
        Tree[][] trees = new Tree[][]{{tree}};
        Collectables[][] orbs = new Collectables[][]{{collectables}};
        Rock[][] rocks = new Rock[][]{{rock}};

        when(spike.getPosition()).thenReturn(new Position(20, 20));
        when(tile.getPosition()).thenReturn(new Position(25, 25));
        when(tree.getPosition()).thenReturn(new Position(30, 30));
        when(collectables.getPosition()).thenReturn(new Position(35, 35));
        when(rock.getPosition()).thenReturn(new Position(40, 40));

        when(scene.getSpikes()).thenReturn(spikes);
        when(scene.getTiles()).thenReturn(tiles);
        when(scene.getTrees()).thenReturn(trees);
        when(scene.getOrbs()).thenReturn(orbs);
        when(scene.getRocks()).thenReturn(rocks);

        gameViewer = new GameViewer(scene, viewerProvider);
    }

    @Test
    public void testDraw() throws IOException {
        gameViewer.draw(gui, 100L);
        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui).flush();
    }

    @Test
    public void testDrawWithNull() throws IOException {
        Tile[][] arrayWithNulls = new Tile[][]{{null, null}};
        when(scene.getTiles()).thenReturn(arrayWithNulls);
        gameViewer.draw(gui, 100L);
        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui).flush();
    }

    @Test
    public void testDrawWithIOException() throws IOException {
        Particle particle = mock(Particle.class);
        when(particle.getPosition()).thenReturn(new Position(5.0, 5.0));
        doThrow(new IOException("Test exception")).when(particleViewer)
                .draw(any(), any(), anyLong(), anyInt(), anyInt());

        List<Particle> elements = List.of(particle);

        assertDoesNotThrow(() -> gameViewer.drawElements(gui, elements, particleViewer, 100L));
    }

    @Test
    public void testDrawFlashActive() throws IOException {
        gameViewer.draw(gui, 10L);

        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui).flush();
    }

    @Test
    public void testDrawAfterEffectActive() throws IOException {
        gameViewer.draw(gui, 40L);

        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui).flush();
    }
}
