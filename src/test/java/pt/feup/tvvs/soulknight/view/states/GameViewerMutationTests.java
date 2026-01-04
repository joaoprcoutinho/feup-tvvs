package pt.feup.tvvs.soulknight.view.states;

import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
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
import pt.feup.tvvs.soulknight.view.sprites.ViewerProvider;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

public class GameViewerMutationTests {
    private GUI gui;
    private Scene scene;
    private ViewerProvider viewerProvider;
    private GameViewer gameViewer;

    @BeforeEach
    void setup() throws IOException {
        gui = mock(GUI.class);
        scene = mock(Scene.class);
        viewerProvider = mock(ViewerProvider.class);

        // Concrete viewer mocks (IMPORTANT)
        when(viewerProvider.getParticleViewer()).thenReturn(mock(ParticleViewer.class));
        when(viewerProvider.getPlayerViewer()).thenReturn(mock(KnightViewer.class));
        when(viewerProvider.getTileViewer()).thenReturn(mock(TileViewer.class));
        when(viewerProvider.getSpikeViewer()).thenReturn(mock(SpikeViewer.class));
        when(viewerProvider.getTreeViewer()).thenReturn(mock(TreeViewer.class));
        when(viewerProvider.getOrbViewer()).thenReturn(mock(OrbViewer.class));
        when(viewerProvider.getRockViewer()).thenReturn(mock(RockViewer.class));
        when(viewerProvider.getMonsterViewer()).thenReturn(mock(MonsterViewer.class));

        // --- Scene data (NON-null, NON-empty) ---

        Particle particle = mock(Particle.class);
        when(particle.getPosition()).thenReturn(new Position(5, 5));
        when(scene.getParticles()).thenReturn(List.of(particle));
        when(scene.getDoubleJumpParticles()).thenReturn(List.of(particle));
        when(scene.getJumpParticles()).thenReturn(List.of(particle));
        when(scene.getRespawnParticles()).thenReturn(List.of(particle));
        when(scene.getDashParticles()).thenReturn(List.of(particle));

        Tile[][] tiles = new Tile[1][1];
        tiles[0][0] = mock(Tile.class);
        when(tiles[0][0].getPosition()).thenReturn(new Position(1, 1));
        when(scene.getTiles()).thenReturn(tiles);

        Spike[][] spikes = new Spike[1][1];
        spikes[0][0] = mock(Spike.class);
        when(spikes[0][0].getPosition()).thenReturn(new Position(2, 2));
        when(scene.getSpikes()).thenReturn(spikes);

        Tree[][] trees = new Tree[1][1];
        trees[0][0] = mock(Tree.class);
        when(trees[0][0].getPosition()).thenReturn(new Position(3, 3));
        when(scene.getTrees()).thenReturn(trees);

        Collectables[][] orbs = new Collectables[1][1];
        orbs[0][0] = mock(Collectables.class);
        when(orbs[0][0].getPosition()).thenReturn(new Position(4, 4));
        when(scene.getOrbs()).thenReturn(orbs);

        Rock[][] rocks = new Rock[1][1];
        rocks[0][0] = mock(Rock.class);
        when(rocks[0][0].getPosition()).thenReturn(new Position(6, 6));
        when(scene.getRocks()).thenReturn(rocks);

        Knight knight = mock(Knight.class);
        when(knight.getPosition()).thenReturn(new Position(10, 10));
        when(scene.getPlayer()).thenReturn(knight);

        Enemies monster = mock(Enemies.class);
        when(monster.getPosition()).thenReturn(new Position(7, 7));
        when(scene.getMonsters()).thenReturn(List.of(monster));

        gameViewer = new GameViewer(scene, viewerProvider);
    }

    @Test
    void draw_shouldRenderAllElementsAndFlush() throws IOException {
        gameViewer.draw(gui, 100L);

        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui).flush();
    }
}