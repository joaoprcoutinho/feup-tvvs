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
import static org.mockito.Mockito.*;

public class BlackBoxTests {
    private ParticleMenuController controller;
    private BufferedImage buffer;
    private Graphics2D mockGraphics;
    private BufferedImageGUI gui;

    @BeforeEach
    void setUp() {
        controller = new ParticleMenuController(new MainMenu());
        buffer = mock(BufferedImage.class);

        // Mock Graphics2D for drawText
        mockGraphics = mock(Graphics2D.class);
        when(buffer.createGraphics()).thenReturn(mockGraphics);

        // Initialize GUI
        gui = new BufferedImageGUI(buffer);
    }

    // -----------------------------
    //  EQUIVALENCE CLASS PARTITIONING
    // -----------------------------

    @Test
    void testECP_X_InRange_Y_InRange() {
        Position p = controller.wrapPosition(50, 20);
        assertEquals(50, p.x(), 0.0001);
        assertEquals(20, p.y(), 0.0001);
    }

    @Test
    void testECP_X_Negative_Y_InRange() {
        Position p = controller.wrapPosition(-5, 40);
        assertEquals(219, p.x(), 0.0001); // screenWidth = 220
        assertEquals(40, p.y(), 0.0001);
    }

    @Test
    void testECP_X_TooHigh_Y_InRange() {
        Position p = controller.wrapPosition(300, 40);
        assertEquals(1, p.x(), 0.0001);
        assertEquals(40, p.y(), 0.0001);
    }

    @Test
    void testECP_X_InRange_Y_Negative() {
        Position p = controller.wrapPosition(50, -3);
        assertEquals(50, p.x(), 0.0001);
        assertEquals(109, p.y(), 0.0001); // screenHeight = 110
    }

    @Test
    void testECP_X_InRange_Y_TooHigh() {
        Position p = controller.wrapPosition(50, 200);
        assertEquals(50, p.x(), 0.0001);
        assertEquals(1, p.y(), 0.0001);
    }

    @Test
    void testECP_X_Negative_Y_Negative() {
        Position p = controller.wrapPosition(-1, -1);
        assertEquals(219, p.x(), 0.0001);
        assertEquals(109, p.y(), 0.0001);
    }

    @Test
    void testECP_X_TooHigh_Y_TooHigh() {
        Position p = controller.wrapPosition(250, 500);
        assertEquals(1, p.x(), 0.0001);
        assertEquals(1, p.y(), 0.0001);
    }

    // -----------------------------
    //  BOUNDARY VALUE ANALYSIS
    // -----------------------------

    @Test
    void testBVA_xEqualsMinus1() {
        Position p = controller.wrapPosition(-1, 10);
        assertEquals(219, p.x(), 0.0001);
    }

    @Test
    void testBVA_xEquals0() {
        Position p = controller.wrapPosition(0, 10);
        assertEquals(0, p.x(), 0.0001);
    }

    @Test
    void testBVA_xEquals1() {
        Position p = controller.wrapPosition(1, 10);
        assertEquals(1, p.x(), 0.0001);
    }

    @Test
    void testBVA_xEqualsWMinus1() {
        Position p = controller.wrapPosition(219, 10);
        assertEquals(219, p.x(), 0.0001);
    }

    @Test
    void testBVA_xEqualsW() {
        Position p = controller.wrapPosition(220, 10);
        assertEquals(1, p.x(), 0.0001);
    }

    @Test
    void testBVA_xEqualsWPlus1() {
        Position p = controller.wrapPosition(221, 10);
        assertEquals(1, p.x(), 0.0001);
    }

    @Test
    void testBVA_yEqualsMinus1() {
        Position p = controller.wrapPosition(10, -1);
        assertEquals(109, p.y(), 0.0001);
    }

    @Test
    void testBVA_yEquals0() {
        Position p = controller.wrapPosition(10, 0);
        assertEquals(0, p.y(), 0.0001);
    }

    @Test
    void testBVA_yEquals1() {
        Position p = controller.wrapPosition(10, 1);
        assertEquals(1, p.y(), 0.0001);
    }

    @Test
    void testBVA_yEqualsHMinus1() {
        Position p = controller.wrapPosition(10, 109);
        assertEquals(109, p.y(), 0.0001);
    }

    @Test
    void testBVA_yEqualsH() {
        Position p = controller.wrapPosition(10, 110);
        assertEquals(1, p.y(), 0.0001);
    }

    @Test
    void testBVA_yEqualsHPlus1() {
        Position p = controller.wrapPosition(10, 111);
        assertEquals(1, p.y(), 0.0001);
    }

    @Test
    void testDrawText_ValidInput() {
        TextColor.RGB color = new TextColor.RGB(100, 150, 200);
        String text = "Hello";

        gui.drawText(50, 50, color, text);

        verify(mockGraphics).setColor(new Color(100, 150, 200));
        verify(mockGraphics).drawString(text, 50, 50);
        verify(mockGraphics).dispose();
    }

    @Test
    void testDrawText_EmptyString() {
        TextColor.RGB color = new TextColor.RGB(10, 20, 30);
        String text = "";

        gui.drawText(10, 10, color, text);

        verify(mockGraphics).drawString(text, 10, 10);
    }

    @Test
    void testDrawText_NullString() {
        TextColor.RGB color = new TextColor.RGB(0, 0, 0);
        String text = null;

        try {
            gui.drawText(0, 0, color, text);
        } catch (NullPointerException e) {
            // Expected if drawString does not allow null
        }
    }
    // -------------------------------
    // BOUNDARY VALUE ANALYSIS
    // -------------------------------

    @Test
    void testDrawText_XY_Boundaries() {
        TextColor.RGB color = new TextColor.RGB(50, 50, 50);
        String text = "Edge";

        int[] xValues = {-1, 0, 1, 219, 220, 221};
        int[] yValues = {-1, 0, 1, 109, 110, 111};

        for (int x : xValues) {
            for (int y : yValues) {
                gui.drawText(x, y, color, text);
                verify(mockGraphics).drawString(text, x, y);
            }
        }
    }

    @Test
    void testDrawText_ColorComponentBoundaries() {
        String text = "Boundary";
        int[] components = {0, 1, 254, 255};

        for (int r : components) {
            for (int g : components) {
                for (int b : components) {
                    reset(mockGraphics);
                    TextColor.RGB color = new TextColor.RGB(r, g, b);
                    gui.drawText(10, 10, color, text);
                    verify(mockGraphics).setColor(any(Color.class));
                    verify(mockGraphics).drawString(text, 10, 10);
                }
            }
        }
    }
}
