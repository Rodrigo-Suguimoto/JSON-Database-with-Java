package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.Gson;
import server.command.*;
import shared.Request;

public class Main {

    private static volatile boolean isRunning = true;
    private static Map<String, JsonElement> database;
    private static final String pathToDb = "/Users/rodrigo.suguimoto/IdeaProjects/JSON-Database-with-Java/JSON Database with Java/task/src/server/data/db.json";

    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 23456;
        try {
            InetAddress inetAddress = InetAddress.getByName(address);
            try (ServerSocket server = new ServerSocket(port, 50, inetAddress)) {
                System.out.println("Server started!");

                database = loadDatabase();
                ReadWriteLock lock = new ReentrantReadWriteLock();
                Lock readLock = lock.readLock();
                Lock writeLock = lock.writeLock();
                ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

                while (isRunning) {
                    try {
                        Socket socket = server.accept();
                        executor.submit(() -> handleClient(socket, server, database, readLock, writeLock, executor));
                    } catch (IOException e) {
                        if (!isRunning) {
                            // Suppress the error if the server was intentionally stopped
                            break;
                        }
                        System.out.println("Error while handling client connection " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Unexpected IO error: " + e.getMessage());
        }
    }

    private static Map<String, JsonElement> loadDatabase() {
        try (FileReader reader = new FileReader(pathToDb)) {
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, JsonElement>>() {}.getType();
            Map<String, JsonElement> database = gson.fromJson(reader, mapType);
            return (database == null) ? new HashMap<>() : database;
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return null;
        }
    }

    public static void saveDatabase() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(pathToDb)) {
            gson.toJson(database, writer);
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket, ServerSocket server, Map<String, JsonElement> database,
                                     Lock readLock, Lock writeLock, ExecutorService executor) {
        try (socket;
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            Request clientCommand = Request.deserializeGson(input.readUTF());

            if (clientCommand.getType().equalsIgnoreCase("exit")) {
                isRunning = false;
                server.close();
                executor.shutdownNow();
                return;
            }

            DatabaseReceiver receiver = new DatabaseReceiver(database, readLock, writeLock);
            Command command = switch (clientCommand.getType().toLowerCase()) {
                case "get" -> new GetCommand(receiver, clientCommand.getKeys());
                case "set" -> new SetCommand(receiver, clientCommand.getKeys(), clientCommand.getValue());
                case "delete" -> new DeleteCommand(receiver, clientCommand.getKeys());
                default -> null;
            };

            Map<String, JsonElement> response = command.execute();
            String responseAsJson = new Gson().toJson(response);
            output.writeUTF(responseAsJson);

        } catch (IOException e) {
            System.err.println("Error while handling client connection: " + e.getMessage());
        }
    }
}
