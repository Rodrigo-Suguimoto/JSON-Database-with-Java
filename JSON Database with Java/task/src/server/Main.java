package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
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
                        System.out.println(clientCommand.getRequestType());

//                        if (parsedCommand.getRequestType().equalsIgnoreCase("exit")) {
//                            isRunning = false;
//                        } else {
//                            String response = "ERROR";
//                            if (parsedCommand.getIndex() != null && parsedCommand.getIndex() > 0 && parsedCommand.getIndex() <= 1000) {
//                                switch(parsedCommand.getRequestType()) {
//                                    case "get":
//                                        response = database.getOrDefault(parsedCommand.getIndex(), "ERROR");
//                                        break;
//                                    case "set":
//                                        database.put(parsedCommand.getIndex(), parsedCommand.getMessage());
//                                        response = "OK";
//                                        break;
//                                    case "delete":
//                                        database.remove(parsedCommand.getIndex());
//                                        response = "OK";
//                                        break;
//                                }
//                            }
//                        }

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
