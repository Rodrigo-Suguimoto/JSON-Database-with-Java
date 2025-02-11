package server.command;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import server.Main;

public class DatabaseReceiver {
    private final Map<String, String> database;
    private final Lock readLock;
    private final Lock writeLock;

    public DatabaseReceiver(Map<String, String> database, Lock readLock, Lock writeLock) {
        this.database = database;
        this.readLock = readLock;
        this.writeLock = writeLock;
    }

    public Map<String, String> get(String key) {
        Map<String, String> response = new HashMap<>();
        readLock.lock();
        try {
            if (database.containsKey(key)) {
                response.put("response", "OK");
                response.put("value", database.get(key));
            } else {
                response.put("response", "ERROR");
                response.put("reason", "No such key");
            }
        } finally {
            readLock.unlock();
        }
        return response;
    }

    public Map<String, String> set(String key, String value) {
        Map<String, String> response = new HashMap<>();
        writeLock.lock();
        try {
            database.put(key, value);
            Main.saveDatabase();
            response.put("response", "OK");
        } finally {
            writeLock.unlock();
        }
        return response;
    }

    public Map<String, String> delete(String key) {
        Map<String, String> response = new HashMap<>();
        writeLock.lock();
        try {
            if (database.containsKey(key)) {
                database.remove(key);
                Main.saveDatabase();
                response.put("response", "OK");
            } else {
                response.put("response", "ERROR");
                response.put("reason", "No such key");
            }
        } finally {
            writeLock.unlock();
        }
        return response;
    }
}
