import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private Clip backgroundMusic;
    private boolean isMuted = false;
    private float masterVolume = 1.0f; // 0.0 à 1.0
    private Map<String, Float> soundEffectsVolumes = new HashMap<>();

    // Joue un effet sonore avec son volume propre
    public void playSoundEffect(String name) {
        if (isMuted) return;

        new Thread(() -> {
            try {
                URL soundUrl = getClass().getResource("./Sounds/" + name + ".wav");
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundUrl);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);

                // Applique le volume spécifique + volume master
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float effectVolume = soundEffectsVolumes.getOrDefault(name, 1.0f);
                float totalVolume = masterVolume * effectVolume;
                float dB = calculateDecibels(totalVolume);
                gainControl.setValue(dB);

                clip.start();
            } catch (Exception e) {
                System.err.println("Error playing sound effect " + name + ": " + e.getMessage());
            }
        }).start();
    }
    // Charge un son avec son volume spécifique
    public void loadSoundEffect(String name, float volume) {
        soundEffectsVolumes.put(name, volume);
        // Pré-charge le son pour meilleure performance
    }

    private float calculateDecibels(float volume) {
        // Protection contre les valeurs extrêmes
        volume = Math.max(0.0001f, Math.min(1.0f, volume));
        return (float) (Math.log10(volume) * 20);
    }


    public void toggleMute() {
        isMuted = !isMuted;
        if (backgroundMusic != null) {
            if (isMuted) {
                backgroundMusic.stop();
            } else {
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
    }

    // Définit le volume global
    public void setMasterVolume(float volume) {
        this.masterVolume = volume;
        if (backgroundMusic != null) {
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(calculateDecibels(volume));
        }
    }
}
