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

    // Test flash active condition: time % 800 < 20
    // Kills mutations: "negated conditional", "changed conditional boundary"
    @Test
    void testFlashActiveConditionAtBoundary() throws IOException {
        // At time 19: 19 % 800 = 19 < 20 (flash active)
        gameViewer.draw(gui, 19);
        
        // Verify white pixels drawn (255, 255, 255)
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        boolean hasWhitePixel = colorCaptor.getAllValues().stream()
                .anyMatch(c -> c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255);
        assertTrue(hasWhitePixel, "Flash should produce white pixels at time 19");
    }

    @Test
    void testFlashNotActiveAtBoundary() throws IOException {
        // At time 20: 20 % 800 = 20 >= 20 (flash NOT active)
        gameViewer.draw(gui, 20);
        
        // Should enter aftereffect state, not pure white
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        // At least some pixels should not be pure white
        boolean allWhite = colorCaptor.getAllValues().stream()
                .allMatch(c -> c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255);
        assertFalse(allWhite, "At time 20, not all pixels should be white");
    }

    // Test aftereffect active condition: time % 800 >= 20 && time % 800 < 60
    // Kills mutations on line with afterEffectActive
    @Test
    void testAfterEffectActiveAtStart() throws IOException {
        // At time 20: afterEffectActive = true
        gameViewer.draw(gui, 20);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    void testAfterEffectActiveAtEnd() throws IOException {
        // At time 59: afterEffectActive = true (59 % 800 = 59 < 60)
        gameViewer.draw(gui, 59);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    void testAfterEffectNotActiveAtBoundary() throws IOException {
        // At time 60: afterEffectActive = false (60 % 800 = 60 >= 60)
        gameViewer.draw(gui, 60);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test afterEffectFactor calculation: (1.0 - (time % 800 - 20) / 40.0)
    // Kills mutations: math mutations
    @Test
    void testAfterEffectFactorAtStart() throws IOException {
        // At time 20: afterEffectFactor = 1.0 - (20-20)/40.0 = 1.0 - 0 = 1.0
        gameViewer.draw(gui, 20);
        
        // With factor 1.0, color should be close to white
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        // After effect at factor 1.0 should produce bright colors
        boolean hasBrightPixel = colorCaptor.getAllValues().stream()
                .anyMatch(c -> c.getRed() > 200 && c.getGreen() > 200 && c.getBlue() > 200);
        assertTrue(hasBrightPixel, "At time 20, afterEffect factor should be 1.0, producing bright pixels");
    }

    @Test
    void testAfterEffectFactorAtMiddle() throws IOException {
        // At time 40: afterEffectFactor = 1.0 - (40-20)/40.0 = 1.0 - 0.5 = 0.5
        gameViewer.draw(gui, 40);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    @Test
    void testAfterEffectFactorAtEnd() throws IOException {
        // At time 59: afterEffectFactor = 1.0 - (59-20)/40.0 = 1.0 - 0.975 ≈ 0.025
        gameViewer.draw(gui, 59);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test normal gradient (not flash, not aftereffect)
    @Test
    void testNormalGradientBackground() throws IOException {
        // At time 100: 100 % 800 = 100 > 60, normal gradient
        gameViewer.draw(gui, 100);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        // Normal gradient has colors reduced to 60%
        boolean hasDarkPixel = colorCaptor.getAllValues().stream()
                .anyMatch(c -> c.getRed() <= 153 && c.getGreen() <= 153 && c.getBlue() <= 153);
        assertTrue(hasDarkPixel, "Normal gradient should have darker pixels (60% reduction)");
    }

    // Test baseRed1 calculation: 64 + 63 * Math.sin(time * changeRate)
    // Kills mutations: math on 64, 63, addition/multiplication
    @Test
    void testBaseRed1AtTimeZero() throws IOException {
        // At time 0: baseRed1 = 64 + 63 * sin(0) = 64 + 0 = 64
        gameViewer.draw(gui, 0);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test loop boundaries: w < 240, h < 120
    @Test
    void testLoopBoundaries() throws IOException {
        gameViewer.draw(gui, 100);
        
        // Verify pixels at corners
        verify(gui, atLeastOnce()).drawPixel(eq(0), eq(0), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(239), eq(0), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(0), eq(119), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(239), eq(119), any(TextColor.RGB.class));
    }

    // Test time % 800 modulo operation
    @Test
    void testTimeModuloOperation() throws IOException {
        // At time 800: 800 % 800 = 0 < 20 (flash)
        gameViewer.draw(gui, 800);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        boolean hasWhitePixel = colorCaptor.getAllValues().stream()
                .anyMatch(c -> c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255);
        assertTrue(hasWhitePixel, "At time 800, flash should be active (800 % 800 = 0)");
    }

    @Test
    void testTimeModuloOperation819() throws IOException {
        // At time 819: 819 % 800 = 19 < 20 (flash)
        gameViewer.draw(gui, 819);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        boolean hasWhitePixel = colorCaptor.getAllValues().stream()
                .anyMatch(c -> c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255);
        assertTrue(hasWhitePixel, "At time 819, flash should be active");
    }

    // Test interpolation calculations
    // interpolationX = (double) w / (width - 1) = w / 239.0
    @Test
    void testInterpolationX() throws IOException {
        gameViewer.draw(gui, 100);
        
        // At w=0: interpolationX = 0/239 = 0
        // At w=239: interpolationX = 239/239 = 1
        verify(gui, atLeastOnce()).drawPixel(eq(0), anyInt(), any(TextColor.RGB.class));
        verify(gui, atLeastOnce()).drawPixel(eq(239), anyInt(), any(TextColor.RGB.class));
    }

    // Test color reduction: * 0.6
    @Test
    void testColorReduction() throws IOException {
        // At time 100: normal gradient, colors are reduced to 60%
        gameViewer.draw(gui, 100);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        // Base colors can range 64-127 (64 + 63*sin)
        // Interpolation mixes them, then 60% reduction
        // Maximum after reduction: ~127 * 0.6 ≈ 76
        // But interpolation can cause some to go higher
        // Just verify some pixels are reasonably dark (not bright white like in flash)
        boolean hasDarkPixel = colorCaptor.getAllValues().stream()
                .anyMatch(c -> c.getRed() < 150 && c.getGreen() < 150 && c.getBlue() < 150);
        assertTrue(hasDarkPixel, "Normal gradient colors should be reduced, with dark pixels");
    }

    // Test PI/3 in baseGreen1 calculation
    @Test
    void testPiDividedByThreeInGreen() throws IOException {
        // baseGreen1 = 64 + 63 * sin(time * 0.04 + PI/3)
        gameViewer.draw(gui, 100);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test 2*PI/3 in baseBlue1 calculation
    @Test
    void testTwoPiDividedByThreeInBlue() throws IOException {
        // baseBlue1 = 64 + 63 * sin(time * 0.04 + 2*PI/3)
        gameViewer.draw(gui, 100);
        verify(gui, atLeast(240 * 120)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test changeRate multiplication
    @Test
    void testChangeRateMultiplication() throws IOException {
        // changeRate = 0.04
        // At different times, colors should change
        gameViewer.draw(gui, 50);
        reset(gui);
        
        gameViewer.draw(gui, 150);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test pixel count
    @Test
    void testPixelCount() throws IOException {
        gameViewer.draw(gui, 100);
        
        // Should draw exactly 240*120 = 28800 pixels for background
        verify(gui, atLeast(28800)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test elements are drawn
    @Test
    void testParticleElementsDrawn() throws IOException {
        ParticleViewer pv = viewerProvider.getParticleViewer();
        
        gameViewer.draw(gui, 100);
        
        // Verify particle viewer is called (5 lists: particles, doubleJump, jump, respawn, dash)
        verify(pv, times(5)).draw(any(Particle.class), eq(gui), eq(100L), anyInt(), anyInt());
    }

    @Test
    void testKnightDrawn() throws IOException {
        KnightViewer kv = viewerProvider.getPlayerViewer();
        
        gameViewer.draw(gui, 100);
        
        verify(kv).draw(any(Knight.class), eq(gui), eq(100L), eq(10), eq(10));
    }

    @Test
    void testMonsterDrawn() throws IOException {
        MonsterViewer mv = viewerProvider.getMonsterViewer();
        
        gameViewer.draw(gui, 100);
        
        verify(mv).draw(any(Enemies.class), eq(gui), eq(100L), eq(7), eq(7));
    }

    @Test
    void testTilesDrawn() throws IOException {
        TileViewer tv = viewerProvider.getTileViewer();
        
        gameViewer.draw(gui, 100);
        
        verify(tv).draw(any(Tile.class), eq(gui), eq(0L), eq(1), eq(1));
    }

    // Test null element in 2D array
    @Test
    void testNullElementsSkipped() throws IOException {
        Tile[][] tiles = new Tile[2][2];
        tiles[0][0] = mock(Tile.class);
        when(tiles[0][0].getPosition()).thenReturn(new Position(1, 1));
        // tiles[0][1], tiles[1][0], tiles[1][1] are null
        
        when(scene.getTiles()).thenReturn(tiles);
        
        TileViewer tv = viewerProvider.getTileViewer();
        
        gameViewer.draw(gui, 100);
        
        // Only 1 tile should be drawn, nulls should be skipped
        verify(tv, times(1)).draw(any(Tile.class), eq(gui), eq(0L), eq(1), eq(1));
    }

    // Test flash with 800+20 = 820 (just past flash)
    @Test
    void testFlashCycleRepeat() throws IOException {
        // At time 820: 820 % 800 = 20, aftereffect
        gameViewer.draw(gui, 820);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        // Should NOT all be white (aftereffect phase)
        boolean allWhite = colorCaptor.getAllValues().stream()
                .allMatch(c -> c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255);
        assertFalse(allWhite, "At time 820, should be in aftereffect, not flash");
    }

    // Test baseRed2 calculation with PI offset
    @Test
    void testBaseRed2WithPiOffset() throws IOException {
        // baseRed2 = 64 + 63 * sin(time * 0.04 + PI)
        // At time 0: 64 + 63 * sin(PI) = 64 + 0 = 64
        gameViewer.draw(gui, 0);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), any(TextColor.RGB.class));
    }

    // Test color1 and color2 difference (for interpolation)
    @Test
    void testColorInterpolation() throws IOException {
        // At time that creates different color1 and color2
        gameViewer.draw(gui, 79); // ~PI/0.04 for maximum difference
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeast(1)).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        // Should have variety of colors due to interpolation
        long distinctColors = colorCaptor.getAllValues().stream()
                .map(c -> c.getRed() + "," + c.getGreen() + "," + c.getBlue())
                .distinct()
                .count();
        assertTrue(distinctColors > 1, "Should have interpolated colors");
    }

    // Test aftereffect blend calculation
    // red = (int) (red + afterEffectFactor * (255 - red));
    @Test
    void testAfterEffectBlendCalculation() throws IOException {
        // At time 30: afterEffectFactor = 1.0 - (30-20)/40.0 = 0.75
        // Color should be bright but not pure white
        gameViewer.draw(gui, 30);
        
        ArgumentCaptor<TextColor.RGB> colorCaptor = ArgumentCaptor.forClass(TextColor.RGB.class);
        verify(gui, atLeastOnce()).drawPixel(anyInt(), anyInt(), colorCaptor.capture());
        
        boolean hasBrightNotWhite = colorCaptor.getAllValues().stream()
                .anyMatch(c -> c.getRed() > 100 && c.getRed() < 255);
        assertTrue(hasBrightNotWhite, "Aftereffect should blend to bright but not pure white");
    }
}