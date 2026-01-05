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

    @Test
    public void testDrawPlayerStatsCallsDrawForHP() throws IOException {
        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);
        verify(textViewer).draw(eq("hp 100"), eq(4.0), eq(8.0), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    public void testDrawPlayerStatsCallsDrawForFPS() throws IOException {
        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);
        verify(textViewer).draw(eq("fps 60"), eq(4.0), eq(16.0), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    public void testDrawPlayerStatsCallsDrawForOrbs() throws IOException {
        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);
        verify(textViewer).draw(eq("Orbs 5"), eq(160.0), eq(8.0), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    public void testDrawPlayerStatsCallsAllDrawMethods() throws IOException {
        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);
        verify(textViewer, times(3)).draw(anyString(), anyDouble(), anyDouble(), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    public void testDrawPlayerStatsWithDifferentHP() throws IOException {
        when(player.getHP()).thenReturn(50);
        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);
        verify(textViewer).draw(eq("hp 50"), eq(4.0), eq(8.0), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    public void testDrawPlayerStatsWithDifferentFPS() throws IOException {
        when(gui.getFPS()).thenReturn(30);
        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);
        verify(textViewer).draw(eq("fps 30"), eq(4.0), eq(16.0), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    public void testDrawPlayerStatsWithDifferentOrbs() throws IOException {
        when(player.getOrbs()).thenReturn(10);
        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);
        verify(textViewer).draw(eq("Orbs 10"), eq(160.0), eq(8.0), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    public void testDrawPlayerStatsGetsPlayer() throws IOException {
        PlayerStatsViewer.drawPlayerStats(gui, 0, scene, textViewer);
        verify(scene).getPlayer();
    }
}