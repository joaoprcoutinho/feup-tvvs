package pt.feup.tvvs.soulknight.sound;
            
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import javax.sound.sampled.Clip;
import java.io.IOException;

public class SoundPlayerMutationTests {
    private MenuSoundPlayer soundPlayer;
    private Clip clip;

    @BeforeEach
    void setUp() {
        clip = mock(Clip.class);
        soundPlayer = new MenuSoundPlayer(clip);
    }

    @Test
    void constructor_shouldSetSound() {
        assertEquals(clip, soundPlayer.getSound(), "Constructor should set the clip");
    }

    @Test
    void start_shouldSetMicrosecondPositionToZero() {
        soundPlayer.start();
        verify(clip).setMicrosecondPosition(0);
    }

    @Test
    void start_shouldCallStart() {
        soundPlayer.start();
        verify(clip).start();
    }

    @Test
    void start_shouldLoopContinuously() {
        soundPlayer.start();
        verify(clip).loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Test
    void start_shouldCallMethodsInCorrectOrder() {
        soundPlayer.start();
        var inOrder = inOrder(clip);
        inOrder.verify(clip).setMicrosecondPosition(0);
        inOrder.verify(clip).start();
        inOrder.verify(clip).loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Test
    void stop_shouldCallStopOnClip() {
        soundPlayer.stop();
        verify(clip).stop();
    }

    @Test
    void setSound_shouldUpdateClip() {
        Clip newClip = mock(Clip.class);
        soundPlayer.setSound(newClip);
        assertEquals(newClip, soundPlayer.getSound(), "Should update to new clip");
    }

    @Test
    void getSound_shouldReturnCurrentClip() {
        assertEquals(clip, soundPlayer.getSound(), "Should return current clip");
    }
}