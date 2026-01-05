import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.controller.menu.ParticleMenuController;
import pt.feup.tvvs.soulknight.gui.BufferedImageGUI;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.menu.MainMenu;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class BlackBoxTests {
    private ParticleMenuController controller;
    private BufferedImage buffer;
    private Graphics2D mockGraphics;
    private BufferedImageGUI gui;

    @BeforeEach
    public void setUp() {
        controller = new ParticleMenuController(new MainMenu());
        buffer = mock(BufferedImage.class);
        mockGraphics = mock(Graphics2D.class);
        when(buffer.createGraphics()).thenReturn(mockGraphics);
        gui = new BufferedImageGUI(buffer);
    }

    @Test
    public void testWrapPosition_X_InRange_Y_InRange() {
        Position p = controller.wrapPosition(50, 20);
        assertEquals(50, p.x(), 0.0001);
        assertEquals(20, p.y(), 0.0001);
    }

    @Test
    public void testWrapPosition_X_Negative_Y_InRange() {
        Position p = controller.wrapPosition(-5, 40);
        assertEquals(219, p.x(), 0.0001); // screenWidth = 220
        assertEquals(40, p.y(), 0.0001);
    }

    @Test
    public void testWrapPosition_X_TooHigh_Y_InRange() {
        Position p = controller.wrapPosition(300, 40);
        assertEquals(1, p.x(), 0.0001);
        assertEquals(40, p.y(), 0.0001);
    }

    @Test
    public void testWrapPosition_X_InRange_Y_Negative() {
        Position p = controller.wrapPosition(50, -3);
        assertEquals(50, p.x(), 0.0001);
        assertEquals(109, p.y(), 0.0001); // screenHeight = 110
    }

    @Test
    public void testWrapPosition_X_InRange_Y_TooHigh() {
        Position p = controller.wrapPosition(50, 200);
        assertEquals(50, p.x(), 0.0001);
        assertEquals(1, p.y(), 0.0001);
    }

    @Test
    public void testWrapPosition_X_Negative_Y_Negative() {
        Position p = controller.wrapPosition(-1, -1);
        assertEquals(219, p.x(), 0.0001);
        assertEquals(109, p.y(), 0.0001);
    }

    @Test
    public void testWrapPosition_X_TooHigh_Y_TooHigh() {
        Position p = controller.wrapPosition(250, 500);
        assertEquals(1, p.x(), 0.0001);
        assertEquals(1, p.y(), 0.0001);
    }

    @Test
    public void testWrapPosition_X_EqualsMinus1() {
        Position p = controller.wrapPosition(-1, 10);
        assertEquals(219, p.x(), 0.0001);
    }

    @Test
    public void testWrapPosition_X_Equals0() {
        Position p = controller.wrapPosition(0, 10);
        assertEquals(0, p.x(), 0.0001);
    }

    @Test
    public void testWrapPosition_X_Equals1() {
        Position p = controller.wrapPosition(1, 10);
        assertEquals(1, p.x(), 0.0001);
    }

    @Test
    public void testWrapPosition_X_EqualsWidthMinus1() {
        Position p = controller.wrapPosition(219, 10);
        assertEquals(219, p.x(), 0.0001);
    }

    @Test
    public void testWrapPosition_X_EqualsWidth() {
        Position p = controller.wrapPosition(220, 10);
        assertEquals(1, p.x(), 0.0001);
    }

    @Test
    public void testWrapPosition_X_EqualsWidthPlus1() {
        Position p = controller.wrapPosition(221, 10);
        assertEquals(1, p.x(), 0.0001);
    }

    @Test
    public void testWrapPosition_Y_EqualsMinus1() {
        Position p = controller.wrapPosition(10, -1);
        assertEquals(109, p.y(), 0.0001);
    }

    @Test
    public void testWrapPosition_Y_Equals0() {
        Position p = controller.wrapPosition(10, 0);
        assertEquals(0, p.y(), 0.0001);
    }

    @Test
    public void testWrapPosition_Y_Equals1() {
        Position p = controller.wrapPosition(10, 1);
        assertEquals(1, p.y(), 0.0001);
    }

    @Test
    public void testWrapPosition_Y_EqualsHeightMinus1() {
        Position p = controller.wrapPosition(10, 109);
        assertEquals(109, p.y(), 0.0001);
    }

    @Test
    public void testWrapPosition_Y_EqualsHeight() {
        Position p = controller.wrapPosition(10, 110);
        assertEquals(1, p.y(), 0.0001);
    }

    @Test
    public void testWrapPosition_Y_EqualsHeightPlus1() {
        Position p = controller.wrapPosition(10, 111);
        assertEquals(1, p.y(), 0.0001);
    }

    @Test
    public void testDrawText_ValidInput() {
        TextColor.RGB color = new TextColor.RGB(100, 150, 200);
        String text = "Hello";

        gui.drawText(50, 50, color, text);

        verify(mockGraphics).setColor(new Color(100, 150, 200));
        verify(mockGraphics).drawString(text, 50, 50);
        verify(mockGraphics).dispose();
    }

    @Test
    public void testDrawText_EmptyString() {
        TextColor.RGB color = new TextColor.RGB(10, 20, 30);
        String text = "";

        gui.drawText(10, 10, color, text);

        verify(mockGraphics).drawString(text, 10, 10);
    }

    @Test
    public void testDrawText_NullString() {
        TextColor.RGB color = new TextColor.RGB(0, 0, 0);
        String text = null;

        gui.drawText(0, 0, color, text);

        assertDoesNotThrow(() -> gui.drawText(0, 0, color, null));
    }

    @Test
    public void testDrawText_X_Negative_Y_InRange() {
        gui.drawText(-1, 50, new TextColor.RGB(50, 50, 50), "Edge");
        verify(mockGraphics).drawString("Edge", -1, 50);
    }

    @Test
    public void testDrawText_X_Zero_Y_InRange() {
        gui.drawText(0, 50, new TextColor.RGB(50, 50, 50), "Edge");
        verify(mockGraphics).drawString("Edge", 0, 50);
    }

    @Test
    public void testDrawText_X_Positive_Y_InRange() {
        gui.drawText(1, 50, new TextColor.RGB(50, 50, 50), "Edge");
        verify(mockGraphics).drawString("Edge", 1, 50);
    }

    @Test
    public void testDrawText_X_WidthMinus1_Y_InRange() {
        gui.drawText(219, 50, new TextColor.RGB(50, 50, 50), "Edge");
        verify(mockGraphics).drawString("Edge", 219, 50);
    }

    @Test
    public void testDrawText_X_Width_Y_InRange() {
        gui.drawText(220, 50, new TextColor.RGB(50, 50, 50), "Edge");
        verify(mockGraphics).drawString("Edge", 220, 50);
    }

    @Test
    public void testDrawText_X_WidthPlus1_Y_InRange() {
        gui.drawText(221, 50, new TextColor.RGB(50, 50, 50), "Edge");
        verify(mockGraphics).drawString("Edge", 221, 50);
    }

    @Test
    public void testDrawText_Y_Negative_X_InRange() {
        gui.drawText(50, -1, new TextColor.RGB(50, 50, 50), "Edge");
        verify(mockGraphics).drawString("Edge", 50, -1);
    }

    @Test
    public void testDrawText_Y_Zero_X_InRange() {
        gui.drawText(50, 0, new TextColor.RGB(50, 50, 50), "Edge");
        verify(mockGraphics).drawString("Edge", 50, 0);
    }

    @Test
    public void testDrawText_Y_Positive_X_InRange() {
        gui.drawText(50, 1, new TextColor.RGB(50, 50, 50), "Edge");
        verify(mockGraphics).drawString("Edge", 50, 1);
    }

    @Test
    public void testDrawText_Y_HeightMinus1_X_InRange() {
        gui.drawText(50, 109, new TextColor.RGB(50, 50, 50), "Edge");
        verify(mockGraphics).drawString("Edge", 50, 109);
    }

    @Test
    public void testDrawText_Y_Height_X_InRange() {
        gui.drawText(50, 110, new TextColor.RGB(50, 50, 50), "Edge");
        verify(mockGraphics).drawString("Edge", 50, 110);
    }

    @Test
    public void testDrawText_Y_HeightPlus1_X_InRange() {
        gui.drawText(50, 111, new TextColor.RGB(50, 50, 50), "Edge");
        verify(mockGraphics).drawString("Edge", 50, 111);
    }
}
