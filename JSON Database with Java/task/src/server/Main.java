package server;

import java.util.Arrays;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is running...");

            // Wait for a client to connect
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        }


        String[] database = new String[1000];
        Arrays.fill(database, "");
    }
}
