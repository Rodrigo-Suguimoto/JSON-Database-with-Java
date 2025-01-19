package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import shared.Request;

public class Main {
    private static final Map<String, String> database = new HashMap<>();

    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 23456;
        try {
            InetAddress inetAddress = InetAddress.getByName(address);
            try (ServerSocket server = new ServerSocket(port, 50, inetAddress)) {
                System.out.println("Server started!");
                boolean isRunning = true;
                while (isRunning) {
                    try (Socket socket = server.accept()) {
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                        Request clientCommand = Request.deserializeGson(input.readUTF());

                        if (clientCommand.getType().equalsIgnoreCase("exit")) {
                            isRunning = false;
                        } else {
                            String response = "ERROR";
                            Map<String, String> fullResponse = new HashMap<>();
                            switch(clientCommand.getType()) {
                                case "get":
                                    if (database.containsKey(clientCommand.getKey())) {
                                        response = "OK";
                                        fullResponse.put("response", response);
                                        fullResponse.put("value", database.get(clientCommand.getKey()));
                                    } else {
                                        fullResponse.put("response", response);
                                        fullResponse.put("reason", "No such key");
                                    }

                                    break;
                                case "set":
                                    database.put(clientCommand.getKey(), clientCommand.getValue());
                                    response = "OK";
                                    fullResponse.put("response", response);
                                    break;
                                case "delete":
                                    if (database.containsKey(clientCommand.getKey())) {
                                        database.remove(clientCommand.getKey());
                                        response = "OK";
                                        fullResponse.put("response", response);
                                    } else {
                                        fullResponse.put("response", response);
                                        fullResponse.put("reason", "No such key");
                                    }

                                    break;
                            }

                            String fullResponseAsJson = new Gson().toJson(fullResponse);
                            output.writeUTF(fullResponseAsJson);
                        }
                    } catch (IOException e) {
                        System.out.println("Error while handling client connection " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Unexpected IO error: " + e.getMessage());
        }
    }
}
