package shared;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Request {
    private String type;

    @SerializedName("key") // Maps "key" in JSON to "keys" in Java
    @JsonAdapter(StringListTypeAdapter.class)
    private List<String> keys;

    private JsonElement value = null;

    // No-arg constructor needed for Gson
    public Request() {}

    // Constructor for a single key and a JsonElement value
    public Request(String type, String key, JsonElement value) {
        this.type = type;
        this.keys = new ArrayList<>();
        this.keys.add(key);
        this.value = value;
    }

    // Overloaded Constructor for a single key and a String value
    public Request(String type, String key, String value) {
        this(type, key, new JsonPrimitive(value));
    }

    // Constructor for multiple keys and a JsonElement value
    public Request(String type, List<String> keys, JsonElement value) {
        this.type = type;
        this.keys = keys;
        this.value = value;
    }

    // Overloaded Constructor for multiple keys and a String value
    public Request(String type, List<String> keys, String value) {
        this(type, keys, new JsonPrimitive(value));
    }

    // Constructor for a single key and no value
    public Request(String type, String key) {
        this.type = type;
        this.keys = new ArrayList<>();
        this.keys.add(key);
    }

    // Constructor for multiple keys and no value
    public Request(String type, List<String> keys) {
        this.type = type;
        this.keys = keys;
    }

    public Request(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public List<String> getKeys() {
        return keys;
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