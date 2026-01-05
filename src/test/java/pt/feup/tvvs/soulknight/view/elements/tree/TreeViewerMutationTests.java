package pt.feup.tvvs.soulknight.view.elements.tree;
            
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import pt.feup.tvvs.soulknight.gui.GUI;
import pt.feup.tvvs.soulknight.model.game.elements.Tree;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.view.sprites.Sprite;
import pt.feup.tvvs.soulknight.view.sprites.SpriteLoader;
import java.io.IOException;

public class TreeViewerMutationTests {
    private SpriteLoader spriteLoader;
    private Sprite smallTreeSprite;
    private Sprite mediumTreeSprite;
    private GUI gui;

    @BeforeEach
    void setUp() throws IOException {
        spriteLoader = mock(SpriteLoader.class);
        smallTreeSprite = mock(Sprite.class);
        mediumTreeSprite = mock(Sprite.class);
        gui = mock(GUI.class);
        
        when(spriteLoader.get("sprites/Ambient/SmallTree.png")).thenReturn(smallTreeSprite);
        when(spriteLoader.get("sprites/Ambient/MediumTree.png")).thenReturn(mediumTreeSprite);
    }

    @Test
    public void draw_shouldUseSmallTreeSprite() throws IOException {
        TreeViewer viewer = new TreeViewer(spriteLoader);
        Tree tree = mock(Tree.class);
        when(tree.getChar()).thenReturn('t');
        when(tree.getPosition()).thenReturn(new Position(10, 20));
        
        viewer.draw(tree, gui, 0, 0, 0);
        
        verify(smallTreeSprite).draw(gui, 10, 20);
    }

    @Test
    public void draw_shouldUseMediumTreeSprite() throws IOException {
        TreeViewer viewer = new TreeViewer(spriteLoader);
        Tree tree = mock(Tree.class);
        when(tree.getChar()).thenReturn('T');
        when(tree.getPosition()).thenReturn(new Position(30, 40));
        
        viewer.draw(tree, gui, 0, 0, 0);
        
        verify(mediumTreeSprite).draw(gui, 30, 40);
    }

    @Test
    public void draw_shouldUseCorrectXPosition() throws IOException {
        TreeViewer viewer = new TreeViewer(spriteLoader);
        Tree tree = mock(Tree.class);
        when(tree.getChar()).thenReturn('t');
        when(tree.getPosition()).thenReturn(new Position(100, 200));
        
        viewer.draw(tree, gui, 0, 0, 0);
        
        verify(smallTreeSprite).draw(eq(gui), eq(100), anyInt());
    }

    @Test
    public void draw_shouldUseCorrectYPosition() throws IOException {
        TreeViewer viewer = new TreeViewer(spriteLoader);
        Tree tree = mock(Tree.class);
        when(tree.getChar()).thenReturn('t');
        when(tree.getPosition()).thenReturn(new Position(100, 200));
        
        viewer.draw(tree, gui, 0, 0, 0);
        
        verify(smallTreeSprite).draw(eq(gui), anyInt(), eq(200));
    }
}