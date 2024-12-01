package client;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        server.Main server = new server.Main();

        String input;
        do {
            input = scanner.nextLine();
            String[] parts = input.split(" ");
            String command = parts[0];
        } while (!input.equals("exit"));

    }
}
