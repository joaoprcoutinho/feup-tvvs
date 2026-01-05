package pt.feup.tvvs.soulknight.view.states;

import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

import static org.junit.jupiter.api.Assertions.*;
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

        when(viewerProvider.getParticleViewer()).thenReturn(mock(ParticleViewer.class));
        when(viewerProvider.getPlayerViewer()).thenReturn(mock(KnightViewer.class));
        when(viewerProvider.getTileViewer()).thenReturn(mock(TileViewer.class));
        when(viewerProvider.getSpikeViewer()).thenReturn(mock(SpikeViewer.class));
        when(viewerProvider.getTreeViewer()).thenReturn(mock(TreeViewer.class));
        when(viewerProvider.getOrbViewer()).thenReturn(mock(OrbViewer.class));
        when(viewerProvider.getRockViewer()).thenReturn(mock(RockViewer.class));
        when(viewerProvider.getMonsterViewer()).thenReturn(mock(MonsterViewer.class));

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
    public void draw_shouldRenderAllElementsAndFlush() throws IOException {
        gameViewer.draw(gui, 100L);

        verify(gui).cls();
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
        verify(gui).flush();
    }

    @Test
    public void testFlashActiveConditionAtBoundary() throws IOException {
        gameViewer.draw(gui, 19);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        boolean hasWhitePixel = colorCaptor.getAllValues().stream()
                .anyMatch(c -> c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255);
        assertTrue(hasWhitePixel, "Flash should produce white pixels at time 19");
    }

    @Test
    public void testFlashNotActiveAtBoundary() throws IOException {
        gameViewer.draw(gui, 20);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        boolean allWhite = colorCaptor.getAllValues().stream().allMatch(c -> c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255);
        assertFalse(allWhite);
    }

    @Test
    public void testAfterEffectActiveAtStart() throws IOException {
        gameViewer.draw(gui, 20);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testAfterEffectActiveAtEnd() throws IOException {
        gameViewer.draw(gui, 59);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testAfterEffectNotActiveAtBoundary() throws IOException {
        gameViewer.draw(gui, 60);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testAfterEffectFactorAtStart() throws IOException {
        gameViewer.draw(gui, 20);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        boolean hasBrightPixel = colorCaptor.getAllValues().stream().anyMatch(c -> c.getRed() > 200 && c.getGreen() > 200 && c.getBlue() > 200);
        assertTrue(hasBrightPixel);
    }

    @Test
    public void testAfterEffectFactorAtMiddle() throws IOException {
        gameViewer.draw(gui, 40);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testAfterEffectFactorAtEnd() throws IOException {
        gameViewer.draw(gui, 59);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testNormalGradientBackground() throws IOException {
        gameViewer.draw(gui, 100);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        boolean hasDarkPixel = colorCaptor.getAllValues().stream().anyMatch(c -> c.getRed() <= 153 && c.getGreen() <= 153 && c.getBlue() <= 153);
        assertTrue(hasDarkPixel);
    }

    @Test
    public void testBaseRed1AtTimeZero() throws IOException {
        gameViewer.draw(gui, 0);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testLoopBoundaries() throws IOException {
        gameViewer.draw(gui, 100);
        
        verify(gui, atLeastOnce()).drawPixel(eq(0), eq(0), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(239), eq(0), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(0), eq(119), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(239), eq(119), any(TextColor.RGB.class));
    }

    @Test
    public void testTimeModuloOperation() throws IOException {
        gameViewer.draw(gui, 800);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        boolean hasWhitePixel = colorCaptor.getAllValues().stream().anyMatch(c -> c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255);
        assertTrue(hasWhitePixel);
    }

    @Test
    public void testTimeModuloOperation819() throws IOException {
        gameViewer.draw(gui, 819);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        boolean hasWhitePixel = colorCaptor.getAllValues().stream().anyMatch(c -> c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255);
        assertTrue(hasWhitePixel);
    }

    @Test
    public void testInterpolationX() throws IOException {
        gameViewer.draw(gui, 100);
        
        verify(gui, atLeastOnce()).drawPixel(eq(0), anyInt(), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(239), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testColorReduction() throws IOException {
        gameViewer.draw(gui, 100);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        boolean hasDarkPixel = colorCaptor.getAllValues().stream().anyMatch(c -> c.getRed() < 150 && c.getGreen() < 150 && c.getBlue() < 150);
        assertTrue(hasDarkPixel);
    }

    @Test
    public void testPiDividedByThreeInGreen() throws IOException {
        gameViewer.draw(gui, 100);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testTwoPiDividedByThreeInBlue() throws IOException {
        gameViewer.draw(gui, 100);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testChangeRateMultiplication() throws IOException {
        gameViewer.draw(gui, 50);
        reset(gui);
        
        gameViewer.draw(gui, 150);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testPixelCount() throws IOException {
        gameViewer.draw(gui, 100);
        verify(gui, atLeast(28800)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testParticleElementsDrawn() throws IOException {
        ParticleViewer pv = viewerProvider.getParticleViewer();
        gameViewer.draw(gui, 100);
        verify(pv, times(5)).draw(any(Particle.class), eq(gui), eq(100L), anyInt(), anyInt());
    }

    @Test
    public void testKnightDrawn() throws IOException {
        KnightViewer kv = viewerProvider.getPlayerViewer();
        gameViewer.draw(gui, 100);
        verify(kv).draw(any(Knight.class), eq(gui), eq(100L), eq(10), eq(10));
    }

    @Test
    public void testMonsterDrawn() throws IOException {
        MonsterViewer mv = viewerProvider.getMonsterViewer();
        gameViewer.draw(gui, 100);
        verify(mv).draw(any(Enemies.class), eq(gui), eq(100L), eq(7), eq(7));
    }

    @Test
    public void testTilesDrawn() throws IOException {
        TileViewer tv = viewerProvider.getTileViewer();
        gameViewer.draw(gui, 100);
        verify(tv).draw(any(Tile.class), eq(gui), eq(0L), eq(1), eq(1));
    }

    @Test
    public void testNullElementsSkipped() throws IOException {
        Tile[][] tiles = new Tile[2][2];
        tiles[0][0] = mock(Tile.class);
        when(tiles[0][0].getPosition()).thenReturn(new Position(1, 1));

        when(scene.getTiles()).thenReturn(tiles);
        
        TileViewer tv = viewerProvider.getTileViewer();
        
        gameViewer.draw(gui, 100);
        
        verify(tv, times(1)).draw(any(Tile.class), eq(gui), eq(0L), eq(1), eq(1));
    }

    @Test
    public void testFlashCycleRepeat() throws IOException {
        gameViewer.draw(gui, 820);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        boolean allWhite = colorCaptor.getAllValues().stream().allMatch(c -> c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255);
        assertFalse(allWhite);
    }

    @Test
    public void testBaseRed2WithPiOffset() throws IOException {
        gameViewer.draw(gui, 0);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    public void testColorInterpolation() throws IOException {
        gameViewer.draw(gui, 79);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        long distinctColors = colorCaptor.getAllValues().stream().map(c -> c.getRed() + "," + c.getGreen() + "," + c.getBlue()).distinct().count();
        assertTrue(distinctColors > 1);
    }

    @Test
    public void testAfterEffectBlendCalculation() throws IOException {
        gameViewer.draw(gui, 30);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        boolean hasBrightNotWhite = colorCaptor.getAllValues().stream().anyMatch(c -> c.getRed() > 100 && c.getRed() < 255);
        assertTrue(hasBrightNotWhite);
    }
}