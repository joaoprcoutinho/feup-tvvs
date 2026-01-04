package pt.feup.tvvs.soulknight.view.menu;
            
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.gui.RescalableGUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.menu.Option;
import pt.feup.tvvs.soulknight.view.text.TextViewer;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class OptionViewerWhiteBoxTest {
    private TextViewer textViewer;
    private RescalableGUI gui;
    private OptionViewer optionViewer;
    private Option option;

    @BeforeEach
    void setUp() {
        textViewer = mock(TextViewer.class);
        gui = mock(RescalableGUI.class);
        option = mock(Option.class);
        optionViewer = new OptionViewer(textViewer);

        when(option.getPosition()).thenReturn(new Position(5, 5));
    }

    @Test
    void testDrawStartGame() {
        when(option.getType()).thenReturn(Option.Type.START_GAME);
        optionViewer.draw(option, gui, new TextColor.RGB(255, 255, 255));
        verify(textViewer).draw(eq("Start"), eq(5.0), eq(5.0), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    void testDrawSettings() {
        when(option.getType()).thenReturn(Option.Type.SETTINGS);
        optionViewer.draw(option, gui, new TextColor.RGB(255, 255, 255));
        verify(textViewer).draw(eq("Settings"), eq(5.0), eq(5.0), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    void testDrawExit() {
        when(option.getType()).thenReturn(Option.Type.EXIT);
        optionViewer.draw(option, gui, new TextColor.RGB(255, 255, 255));
        verify(textViewer).draw(eq("Exit"), eq(5.0), eq(5.0), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    void testDrawToMainMenu() {
        when(option.getType()).thenReturn(Option.Type.TO_MAIN_MENU);
        optionViewer.draw(option, gui, new TextColor.RGB(255, 255, 255));
        verify(textViewer).draw(eq("Go Back"), eq(5.0), eq(5.0), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    void testDrawResolutionAutomatic() {
        when(option.getType()).thenReturn(Option.Type.RESOLUTION);
        when(gui.getResolutionScale()).thenReturn(null);
        optionViewer.draw(option, gui, new TextColor.RGB(255, 255, 255));
        verify(textViewer).draw(eq("Resolution:   Automatic >"), eq(5.0), eq(5.0), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    void testDrawResolutionNotLast() {
        when(option.getType()).thenReturn(Option.Type.RESOLUTION);
        RescalableGUI.ResolutionScale scale = RescalableGUI.ResolutionScale.values()[0];
        when(gui.getResolutionScale()).thenReturn(scale);
        optionViewer.draw(option, gui, new TextColor.RGB(255, 255, 255));
        verify(textViewer).draw((contains("Resolution:")), eq(5.0), eq(5.0), any(TextColor.RGB.class), eq(gui));
    }

    @Test
    void testDrawResolutionLast() {
        when(option.getType()).thenReturn(Option.Type.RESOLUTION);
        RescalableGUI.ResolutionScale[] scales = RescalableGUI.ResolutionScale.values();
        when(gui.getResolutionScale()).thenReturn(scales[scales.length - 1]);
        optionViewer.draw(option, gui, new TextColor.RGB(255, 255, 255));
        verify(textViewer).draw((contains("Resolution:")), eq(5.0), eq(5.0), any(TextColor.RGB.class), eq(gui));
    }
}