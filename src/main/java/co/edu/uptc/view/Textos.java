package co.edu.uptc.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/* textos de i18n en.properties (UTF-8). */
public final class Textos {

    private static final Properties ES = cargar("es");
    private static final Properties EN = cargar("en");

    private static boolean ingles = false;

    private Textos() {
    }

    private static Properties cargar(String idioma) {
        Properties props = new Properties();
        String ruta = "/i18n/textos_" + idioma + ".properties";
        try (InputStream in = Textos.class.getResourceAsStream(ruta)) {
            if (in == null) {
                System.err.println("No se encontró " + ruta);
            } else {
                props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer " + ruta + ": " + e.getMessage());
        }
        return props;
    }

    public static String getIdioma() {
        return ingles ? "en" : "es";
    }

    public static void setIdioma(String codigo) {
        ingles = "en".equalsIgnoreCase(codigo);
    }

    /* devuelve el texto de la clave  */
    public static String t(String clave, Object... args) {
        Properties actual = ingles ? EN : ES;
        Properties otro = ingles ? ES : EN;
        String texto = actual.getProperty(clave, otro.getProperty(clave, clave));
        for (int i = 0; i < args.length; i++) {
            texto = texto.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return texto;
    }
}