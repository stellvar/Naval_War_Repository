package co.edu.uptc.view;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public final class Audio {

    private static final String RUTA_MUSICA = "/audio/musica.wav";
    private static final String RUTA_CLIC = "/audio/efectos/click.wav";
    private static final String RUTA_EXPLOSION = "/audio/efectos/explosion.wav";
    private static final String RUTA_AGUA = "/audio/efectos/agua.wav";
    private static final String CLAVE_MANDO = "audio.mando";

    private static Clip musica;
    private static int volumenMusica = 50;   // 0..100
    private static int volumenEfectos = 50;  // 0..100
    private static boolean vozDeMando;

    private Audio() { }

    public static void iniciarMusica() {
        if (musica != null && musica.isRunning()) return;
        try (InputStream in = Audio.class.getResourceAsStream(RUTA_MUSICA)) {
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
        reproducirEfecto(RUTA_CLIC);
    }


    public static void efectoExplosion() {
        reproducirEfecto(RUTA_EXPLOSION);
    }


    public static void efectoAgua() {
        reproducirEfecto(RUTA_AGUA);
    }


    public static void vozDeMando() {
        if (vozDeMando) {
            reproducirEfecto(Textos.t(CLAVE_MANDO));
        }
    }

    private static void reproducirEfecto(String ruta) {
        try (InputStream in = Audio.class.getResourceAsStream(ruta)) {
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

    public static void setVozDeMando(boolean activa) {
        vozDeMando = activa;
    }


    private static void aplicarVolumen(Clip clip, int v) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;
        FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float db = v == 0 ? gain.getMinimum() : (float) (20 * Math.log10(v / 100.0));
        gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), db)));
    }
}
