package ee.taltech.todo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for JSON serialization and deserialization.
 *
 * Uses Gson library with custom adapters for LocalDateTime.
 *
 * @author ToDo Application
 * @version 1.0
 */
public class JsonUtil {

    private static final Gson gson;

    static {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                                new JsonPrimitive(src.format(formatter)))
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->
                                LocalDateTime.parse(json.getAsString(), formatter))
                .create();
    }

    /**
     * Converts object to JSON string.
     *
     * @param object The object to serialize
     * @return JSON string
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * Converts JSON string to object.
     *
     * @param json  The JSON string
     * @param clazz The class to deserialize to
     * @param <T>   The type
     * @return Deserialized object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * Gets the Gson instance.
     *
     * @return Gson instance
     */
    public static Gson getGson() {
        return gson;
    }
}
