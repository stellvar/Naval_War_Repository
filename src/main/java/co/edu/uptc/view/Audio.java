package co.edu.uptc.view;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public final class Audio {

    private static Clip musica;
    private static int volumenMusica = 50;   // 0..100
    private static int volumenEfectos = 50;  // 0..100

    private Audio() { }

    public static void iniciarMusica() {
        if (musica != null && musica.isRunning()) return;
        try (InputStream in = Audio.class.getResourceAsStream("/audio/musica.wav")) {
            if (in == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(in));
            musica = AudioSystem.getClip();
            musica.open(ais);
            aplicarVolumen(musica, volumenMusica);
            musica.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("No se pudo reproducir la música: " + e.getMessage());
        }
    }

    public static void efectoClic() {
        try (InputStream in = Audio.class.getResourceAsStream("/audio/click.wav")) {
            if (in == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(in));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            aplicarVolumen(clip, volumenEfectos);
            clip.addLineListener(ev -> {
                if (ev.getType() == LineEvent.Type.STOP) clip.close();
            });
            clip.start();
        } catch (Exception e) {
            System.err.println("No se pudo reproducir el efecto: " + e.getMessage());
        }
    }

    public static void setVolumenMusica(int v) {
        volumenMusica = Math.max(0, Math.min(100, v));
        if (musica != null) aplicarVolumen(musica, volumenMusica);
    }

    public static void setVolumenEfectos(int v) {
        volumenEfectos = Math.max(0, Math.min(100, v));
    }


    private static void aplicarVolumen(Clip clip, int v) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;
        FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float db = v == 0 ? gain.getMinimum() : (float) (20 * Math.log10(v / 100.0));
        gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), db)));
    }
}