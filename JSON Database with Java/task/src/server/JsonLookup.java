package server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

public class JsonLookup {
    public static JsonElement getValueByKeys(JsonElement root, List<String> keys) {
        JsonElement current = root;

        for (String key : keys) {
            if (current == null || !current.isJsonObject()) {
                return null; // Key path does not exist
            }

            JsonObject obj = current.getAsJsonObject();
            current = obj.get(key);
        }

        return current;
    }
}
