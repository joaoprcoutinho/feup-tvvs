package pt.feup.tvvs.soulknight.view.states;

import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.model.game.scene.Scene;
import pt.feup.tvvs.soulknight.view.text.TextViewer;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PlayerStatsViewerMutationTests {
    private GUI gui;
    private Scene scene;
    private TextViewer textViewer;
    private Knight player;

    @BeforeEach
    void setup() {
        gui = mock(GUI.class);
        scene = mock(Scene.class);
        textViewer = mock(TextViewer.class);
        player = mock(Knight.class);

        when(scene.getPlayer()).thenReturn(player);
        when(player.getHP()).thenReturn(100);
        when(player.getOrbs()).thenReturn(5);
        when(gui.getFPS()).thenReturn(60);
    }

    // Test that textViewer.draw is called for HP (kills VoidMethodCall mutation on line 24)
    @Test
    void testDrawPlayerStatsCallsDrawForHP() throws IOException {
        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);

        verify(textViewer).draw(eq("hp 100"), eq(4.0), eq(8.0), any(TextColor.RGB.class), eq(gui));
    }

    // Test that textViewer.draw is called for FPS (kills VoidMethodCall mutation on line 25)
    @Test
    void testDrawPlayerStatsCallsDrawForFPS() throws IOException {
        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);

        verify(textViewer).draw(eq("fps 60"), eq(4.0), eq(16.0), any(TextColor.RGB.class), eq(gui));
    }

    // Test that textViewer.draw is called for Orbs (kills VoidMethodCall mutation on line 26)
    @Test
    void testDrawPlayerStatsCallsDrawForOrbs() throws IOException {
        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);

        verify(textViewer).draw(eq("Orbs 5"), eq(160.0), eq(8.0), any(TextColor.RGB.class), eq(gui));
    }

    // Test all three draw calls are made
    @Test
    void testDrawPlayerStatsCallsAllDrawMethods() throws IOException {
        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);

        // Verify all 3 textViewer.draw calls are made
        verify(textViewer, times(3)).draw(anyString(), anyDouble(), anyDouble(), any(TextColor.RGB.class), eq(gui));
    }

    // Test with different HP values
    @Test
    void testDrawPlayerStatsWithDifferentHP() throws IOException {
        when(player.getHP()).thenReturn(50);

        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);

        verify(textViewer).draw(eq("hp 50"), eq(4.0), eq(8.0), any(TextColor.RGB.class), eq(gui));
    }

    // Test with different FPS values
    @Test
    void testDrawPlayerStatsWithDifferentFPS() throws IOException {
        when(gui.getFPS()).thenReturn(30);

        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);

        verify(textViewer).draw(eq("fps 30"), eq(4.0), eq(16.0), any(TextColor.RGB.class), eq(gui));
    }

    // Test with different Orbs values
    @Test
    void testDrawPlayerStatsWithDifferentOrbs() throws IOException {
        when(player.getOrbs()).thenReturn(10);

        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);

        verify(textViewer).draw(eq("Orbs 10"), eq(160.0), eq(8.0), any(TextColor.RGB.class), eq(gui));
    }

    // Test that scene.getPlayer() is called
    @Test
    void testDrawPlayerStatsGetsPlayer() throws IOException {
        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);

        verify(scene).getPlayer();
    }
}