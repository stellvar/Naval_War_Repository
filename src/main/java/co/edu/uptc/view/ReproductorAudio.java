package co.edu.uptc.view;

import co.edu.uptc.model.Configuracion;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.ByteArrayInputStream;
import java.util.EnumMap;
import java.util.Map;

/**
 * Reproduce música de fondo y efectos. Los sonidos se sintetizan en memoria,
 * por lo que no se necesitan archivos de audio. Si el sistema no tiene
 * dispositivo de audio, {@link #isDisponible()} devuelve false y el resto de
 * métodos no hacen nada.
 */
public class ReproductorAudio {

    public enum Efecto { DISPARO_AGUA, TOCADO, HUNDIDO, VICTORIA, DERROTA }

    private static final float FRECUENCIA = 44100f;

    /** Cada fila: {frecuenciaInicial, frecuenciaFinal, segundos}. */
    private static final double[][] MELODIA_FONDO = {
        {262, 262, 0.30}, {330, 330, 0.30}, {392, 392, 0.30}, {440, 440, 0.30},
        {392, 392, 0.30}, {330, 330, 0.30}, {294, 294, 0.30}, {262, 262, 0.60}
    };

    private final Map<Efecto, Clip> efectos = new EnumMap<>(Efecto.class);
    private Clip musica;
    private boolean disponible;
    private int volumenMusica = 50;
    private int volumenEfectos = 50;

    public ReproductorAudio() {
        try {
            for (Efecto efecto : Efecto.values()) {
                efectos.put(efecto, cargarClip(sintetizar(sonidoDe(efecto))));
            }
            musica = cargarClip(sintetizar(MELODIA_FONDO));
            disponible = true;
            setVolumenMusica(volumenMusica);
            setVolumenEfectos(volumenEfectos);
        } catch (Exception e) {
            disponible = false;
            System.out.println("Audio no disponible: " + e.getMessage());
        }
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void aplicarConfiguracion(Configuracion configuracion) {
        setVolumenMusica(configuracion.getVolumenMusica());
        setVolumenEfectos(configuracion.getVolumenEfectos());
    }

    public void setVolumenMusica(int volumen) {
        volumenMusica = limitar(volumen);
        aplicarVolumen(musica, volumenMusica);
    }

    public void setVolumenEfectos(int volumen) {
        volumenEfectos = limitar(volumen);
        for (Clip clip : efectos.values()) {
            aplicarVolumen(clip, volumenEfectos);
        }
    }

    public void reproducirMusica() {
        if (!disponible) {
            return;
        }
        musica.stop();
        musica.setFramePosition(0);
        musica.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void detenerMusica() {
        if (disponible) {
            musica.stop();
        }
    }

    public void reproducirEfecto(Efecto efecto) {
        if (!disponible) {
            return;
        }
        Clip clip = efectos.get(efecto);
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public void cerrar() {
        if (!disponible) {
            return;
        }
        musica.close();
        for (Clip clip : efectos.values()) {
            clip.close();
        }
        disponible = false;
    }

    private static Clip cargarClip(byte[] pcm) throws Exception {
        AudioFormat formato = new AudioFormat(FRECUENCIA, 16, 1, true, false);
        AudioInputStream flujo = new AudioInputStream(
                new ByteArrayInputStream(pcm), formato, pcm.length / 2);
        Clip clip = AudioSystem.getClip();
        clip.open(flujo);
        return clip;
    }

    private static void aplicarVolumen(Clip clip, int volumen) {
        if (clip == null || !clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            return;
        }
        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float decibelios = volumen == 0
                ? control.getMinimum()
                : (float) (20 * Math.log10(volumen / 100.0));
        control.setValue(Math.max(control.getMinimum(), Math.min(control.getMaximum(), decibelios)));
    }

    private static double[][] sonidoDe(Efecto efecto) {
        switch (efecto) {
            case DISPARO_AGUA:
                return new double[][]{{180, 60, 0.35}};
            case TOCADO:
                return new double[][]{{700, 300, 0.30}};
            case HUNDIDO:
                return new double[][]{{400, 50, 0.90}};
            case VICTORIA:
                return new double[][]{{523, 523, 0.15}, {659, 659, 0.15}, {784, 784, 0.15}, {1046, 1046, 0.45}};
            case DERROTA:
                return new double[][]{{392, 392, 0.25}, {330, 330, 0.25}, {262, 262, 0.50}};
            default:
                throw new IllegalArgumentException("Efecto desconocido: " + efecto);
        }
    }

    /** Genera audio PCM de 16 bits mono con tonos senoidales y envolvente para evitar clics. */
    private static byte[] sintetizar(double[][] segmentos) {
        int totalMuestras = 0;
        for (double[] s : segmentos) {
            totalMuestras += (int) (s[2] * FRECUENCIA);
        }
        byte[] pcm = new byte[totalMuestras * 2];
        int indice = 0;
        double fase = 0;
        for (double[] s : segmentos) {
            int n = (int) (s[2] * FRECUENCIA);
            for (int i = 0; i < n; i++) {
                double frecuencia = s[0] + (s[1] - s[0]) * i / n;
                fase += 2 * Math.PI * frecuencia / FRECUENCIA;
                double envolvente = Math.min(1.0, Math.min(i, n - i) / 300.0);
                short muestra = (short) (envolvente * 0.45 * Short.MAX_VALUE * Math.sin(fase));
                pcm[indice++] = (byte) (muestra & 0xff);
                pcm[indice++] = (byte) ((muestra >> 8) & 0xff);
            }
            fase %= 2 * Math.PI;
        }
        return pcm;
    }

    private static int limitar(int volumen) {
        return Math.max(0, Math.min(100, volumen));
    }
}
