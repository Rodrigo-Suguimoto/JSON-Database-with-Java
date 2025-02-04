package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.Gson;
import shared.Request;

public class Main {

    private static volatile boolean isRunning = true;

    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 23456;
        try {
            InetAddress inetAddress = InetAddress.getByName(address);
            try (ServerSocket server = new ServerSocket(port, 50, inetAddress)) {
                System.out.println("Server started!");

                Map<String, String> database = loadDatabase();
                ReadWriteLock lock = new ReentrantReadWriteLock();
                Lock readLock = lock.readLock();
                Lock writeLock = lock.writeLock();
                ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

                while (isRunning) {
                    try {
                        Socket socket = server.accept();
                        executor.submit(() -> handleClient(socket, database, readLock, writeLock, executor));
                    } catch (IOException e) {
                        System.out.println("Error while handling client connection " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Unexpected IO error: " + e.getMessage());
        }
    }

    private static Map<String, String> loadDatabase() {
        String pathToDb = System.getProperty("user.dir") + "/server/data/db.json";
        try (FileReader reader = new FileReader(pathToDb)) {
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, String>>() {}.getType();
            return gson.fromJson(reader, mapType);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return null;
        }
    }

    private static void handleClient(Socket socket, Map<String, String> database,
                                     Lock readLock, Lock writeLock, ExecutorService executor) {
        try (socket;
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {

            Request clientCommand = Request.deserializeGson(input.readUTF());

            if (clientCommand.getType().equalsIgnoreCase("exit")) {
                isRunning = false;
                executor.shutdown();
                return;
            }

            String response = "ERROR";
            Map<String, String> fullResponse = new HashMap<>();

            switch(clientCommand.getType()) {
                case "get":
                    System.out.println("this is running correctly");
                    readLock.lock();
                    System.out.println(database.containsKey(clientCommand.getKey()));
                    try {
                        if (database.containsKey(clientCommand.getKey())) {
                            response = "OK";
                            fullResponse.put("response", response);
                            fullResponse.put("value", database.get(clientCommand.getKey()));
                        } else {
                            fullResponse.put("response", response);
                            fullResponse.put("reason", "No such key");
                            System.out.println("this is not running. Why?");
                        }

                        System.out.println("I am trying to understand why this doesn't run");
                    } finally {
                        readLock.unlock();
                    }
                    break;
                case "set":
                    writeLock.lock();
                    try {
                        database.put(clientCommand.getKey(), clientCommand.getValue());
                        response = "OK";
                        fullResponse.put("response", response);
                    } finally {
                        writeLock.unlock();
                    }
                    break;
                case "delete":
                    writeLock.lock();
                    try {
                        if (database.containsKey(clientCommand.getKey())) {
                            database.remove(clientCommand.getKey());
                            response = "OK";
                            fullResponse.put("response", response);
                        } else {
                            fullResponse.put("response", response);
                            fullResponse.put("reason", "No such key");
                        }
                    } finally {
                        writeLock.unlock();
                    }
                    break;
            }

            String fullResponseAsJson = new Gson().toJson(fullResponse);
            output.writeUTF(fullResponseAsJson);

        } catch (IOException e) {
            System.err.println("Error while handling client connection: " + e.getMessage());
        }
    }
}
