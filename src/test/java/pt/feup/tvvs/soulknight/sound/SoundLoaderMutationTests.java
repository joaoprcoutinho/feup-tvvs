package pt.feup.tvvs.soulknight.sound;
            
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import java.io.IOException;

public class SoundLoaderMutationTests {
    private SoundLoader soundLoader;
    private AudioInputStream audioInput;
    private Clip musicClip;

    @BeforeEach
    void setUp() {
        soundLoader = new SoundLoader();
        audioInput = mock(AudioInputStream.class);
        musicClip = mock(Clip.class);
    }

    @Test
    public void loadSound_shouldReturnClipOnSuccess() throws Exception {
        Clip result = soundLoader.loadSound(audioInput, musicClip);
        assertEquals(musicClip, result);
    }

    @Test
    public void loadSound_shouldCallOpenOnClip() throws Exception {
        soundLoader.loadSound(audioInput, musicClip);
        verify(musicClip).open(audioInput);
    }

    @Test
    public void loadSound_shouldThrowExceptionOnError() throws Exception {
        doThrow(new RuntimeException("Test error")).when(musicClip).open(audioInput);
        
        Exception exception = assertThrows(Exception.class, () -> {
            soundLoader.loadSound(audioInput, musicClip);
        });
        
        assertEquals("Unable to load sound file!", exception.getMessage());
    }

    @Test
    public void loadSound_shouldNotReturnNull() throws Exception {
        Clip result = soundLoader.loadSound(audioInput, musicClip);
        assertNotNull(result);
    }
}