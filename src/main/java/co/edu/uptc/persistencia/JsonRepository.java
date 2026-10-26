package co.edu.uptc.persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

public class JsonRepository<T> {

    public static final String CARPETA_DATOS = "data";

    private final Gson gson;
    private final File archivo;
    private final Class<T> type;

    public JsonRepository(String filePath, Class<T> type) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.archivo = new File(filePath);
        this.type = type;
    }

    public boolean writeFile(T data) {
        File padre = archivo.getAbsoluteFile().getParentFile();
        if (padre != null && !padre.exists()) {
            padre.mkdirs();
        }
        try (Writer writer = new FileWriter(archivo)) {
            gson.toJson(data, writer);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar " + archivo + ": " + e.getMessage());
            return false;
        }
    }

    public T readFile() {
        if (!archivo.exists()) {
            return null;
        }
        try (Reader reader = new FileReader(archivo)) {
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            System.err.println("Error al leer " + archivo + ": " + e.getMessage());
            return null;
        }
    }

    public boolean existe() {
        return archivo.exists();
    }

    public void borrar() {
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}
