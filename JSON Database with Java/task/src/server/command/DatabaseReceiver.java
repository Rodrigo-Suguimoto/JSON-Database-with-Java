package server.command;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.locks.Lock;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import server.JsonLookup;
import server.Main;

public class DatabaseReceiver {
    private final Map<String, JsonElement> database;
    private final Lock readLock;
    private final Lock writeLock;

    public DatabaseReceiver(Map<String, JsonElement> database, Lock readLock, Lock writeLock) {
        this.database = database;
        this.readLock = readLock;
        this.writeLock = writeLock;
    }

    public Map<String, JsonElement> get(List<String> keys) {
        Map<String, JsonElement> response = new HashMap<>();
        readLock.lock();
        try {
            if (!database.containsKey(keys.getFirst())) {
                response.put("response", new JsonPrimitive("ERROR"));
                response.put("reason", new JsonPrimitive("No such key"));
            }

            JsonElement root = database.get(keys.getFirst());
            JsonElement result = JsonLookup.getValueByKeys(root, keys.subList(1, keys.size()));

            if (result != null) {
                response.put("response", new JsonPrimitive("OK"));
                response.put("value", result);
            }
        } finally {
            readLock.unlock();
        }
        return response;
    }

    public Map<String, JsonElement> set(List<String> keys, JsonElement value) {
        Map<String, JsonElement> response = new HashMap<>();
        writeLock.lock();
        try {
            database.put(keys.getFirst(), value);
            Main.saveDatabase();
            response.put("response", new JsonPrimitive("OK"));
        } finally {
            writeLock.unlock();
        }
        return response;
    }

    public Map<String, JsonElement> delete(List<String> keys) {
        Map<String, JsonElement> response = new HashMap<>();
        writeLock.lock();
        try {
            for (String key : keys) {
                if (!database.containsKey(key)) {
                    response.put("response", new JsonPrimitive("ERROR"));
                    response.put("reason", new JsonPrimitive("No such key"));
                    return response;
                } else {
                    database.remove(key);
                    Main.saveDatabase();
                    response.put("response", new JsonPrimitive("OK"));
                }
            }
        } finally {
            writeLock.unlock();
        }
        return response;
    }

}
