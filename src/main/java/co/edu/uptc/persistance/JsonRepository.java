package co.edu.uptc.persistance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
public class JsonRepository<T> {
    private final Gson gson;
    private final String filePath;
    private final Class<T> type;

    public JsonRepository(String filePath, Class<T> type) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.filePath = filePath;
        this.type = type;
    }

    public void writeFile(T data) {
        try(FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
        }catch (Exception e){
            System.out.println("" + e);
        }
    }

    public T readFile() {
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            System.out.println("" + e);
        }
        return null;
    }
}
