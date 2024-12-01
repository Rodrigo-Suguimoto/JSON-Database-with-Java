package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345)) {
            System.out.println("Connected to server!");
        } catch (IOException e) {
            System.err.println("Client exception: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);

        String userCommand;
        do {
            userCommand = scanner.nextLine();
        } while (!userCommand.equals("exit"));

    }
}
