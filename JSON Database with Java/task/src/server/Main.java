package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
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

                        String clientCommand = input.readUTF();
                        ParsedCommand parsedCommand = new ParsedCommand(clientCommand);

                        if (parsedCommand.getRequestType().equalsIgnoreCase("exit")) {
                            isRunning = false;
                        } else {
                            System.out.println("still running, baby");
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

class ParsedCommand {
    private String requestType;
    private Integer index;
    private String message;

    public ParsedCommand(String command) {
        String[] parts = command.split(" ", 3);
        if (parts.length < 1) {
            throw new IllegalArgumentException("Invalid command format");
        }

        this.requestType = parts[0];
        this.index = parts.length > 1 ? Integer.parseInt(parts[1]) : null;
        this.message = parts.length > 2 ? parts[2] : null;
    }

    public String getRequestType() {
        return requestType;
    }

    public Integer getIndex() {
        return index;
    }

    public String getMessage() {
        return message;
    }
}
