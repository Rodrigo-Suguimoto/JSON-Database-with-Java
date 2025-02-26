package server.command;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.locks.Lock;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        String topLevelKey = keys.getFirst();
        writeLock.lock();
        try {
            if (keys.size() == 1) {
                database.put(topLevelKey, value);
                Main.saveDatabase();
                response.put("response", new JsonPrimitive("OK"));
                return response;
            }
            JsonElement current = database.get(topLevelKey);
            System.out.println(current);
            for (int i = 1; i < keys.size(); i++) {
                if (current == null) {
                    System.out.println("is this running?");
                } else {
                    current = current.getAsJsonObject().get(keys.get(i));
                    System.out.println(current);
                }
            }
        } finally {
            writeLock.unlock();
        }
        return response;
    }

    public Map<String, JsonElement> delete(List<String> keys) {
        Map<String, JsonElement> response = new HashMap<>();
        String topLevelKey = keys.getFirst();
        writeLock.lock();

        try {
            if (database.containsKey(topLevelKey)) {
                if (keys.size() == 1) {
                    database.remove(topLevelKey);
                    Main.saveDatabase();
                    response.put("response", new JsonPrimitive("OK"));
                    return response;
                }
            } else {
                return deleteResponseError();
            }

            JsonElement current = database.get(topLevelKey);
            for (int i = 1; i < keys.size() - 1; i++) {
                if (current == null || !current.isJsonObject()) {
                    return deleteResponseError();
                }
                current = current.getAsJsonObject().get(keys.get(i));
            }

            if (current != null && current.isJsonObject()) {
                JsonObject parentObj = current.getAsJsonObject();
                String keyToRemove = keys.getLast();

                if (parentObj.has(keyToRemove)) {
                    parentObj.remove(keyToRemove);
                    Main.saveDatabase();
                    response.put("response", new JsonPrimitive("OK"));
                    return response;
                } else {
                    return deleteResponseError();
                }
            }
        } finally {
            writeLock.unlock();
        }
        return response;
    }

    private Map<String, JsonElement> deleteResponseError() {
        Map<String, JsonElement> response = new HashMap<>();
        response.put("response", new JsonPrimitive("ERROR"));
        response.put("reason", new JsonPrimitive("No such key"));
        return response;
    }

}
