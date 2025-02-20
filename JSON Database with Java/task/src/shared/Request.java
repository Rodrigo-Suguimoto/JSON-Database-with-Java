package shared;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

public class Request {
    private final String type;
    private List<String> key;
    private JsonElement value = null;

    // Constructor for a single key and a JsonElement value
    public Request(String type, String key, JsonElement value) {
        this.type = type;
        this.key = new ArrayList<>();
        this.key.add(key);
        this.value = value;
    }

    // Overloaded Constructor for a single key and a String value
    public Request(String type, String key, String value) {
        this(type, key, new JsonPrimitive(value));
    }

    // Constructor for multiple keys and a JsonElement value
    public Request(String type, List<String> key, JsonElement value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    // Overloaded Constructor for multiple keys and a String value
    public Request(String type, List<String> key, String value) {
        this(type, key, new JsonPrimitive(value));
    }

    public Request(String type, String key) {
        this.type = type;
        this.key = new ArrayList<>();
        this.key.add(key);
    }

    public Request(String type, List<String> key) {
        this.type = type;
        this.key = key;
    }

    public Request(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public List<String> getKey() {
        return key;
    }

    public JsonElement getValue() {
        return value;
    }

    public static String serializeToGson(Request request) {
        return new Gson().toJson(request);
    }

    public static Request deserializeGson(String json) {
        return new Gson().fromJson(json, Request.class);
    }
}