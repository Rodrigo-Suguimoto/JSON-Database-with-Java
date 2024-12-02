package server;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final int DATABASE_SIZE = 1000;
    private static final String[] database = new String[DATABASE_SIZE];

    public static void main(String[] args) {
        Arrays.fill(database, "");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("exit")) {
                break;
            }

            String[] parts = input.split(" ", 3);
            String command = parts[0];

            switch (command) {
                case "set":
                    int index = Integer.parseInt(parts[1]);
                    String text = parts[2];
                    set(index, text);
                    break;
                case "get":
                    index = Integer.parseInt(parts[1]);
                    System.out.println(server.Main.get(index));
                    break;
                case "delete":
                    index = Integer.parseInt(parts[1]);
                    System.out.println(delete(index));
                    break;
            }
        }
    }

    public static String set(int index, String text) {
        if (index < 1 || index > DATABASE_SIZE) {
            return "ERROR";
        }

        database[index - 1] = text; // Adjust for 0-based indexing
        return "OK";
    }

    public static String get(int index) {
        if (index < 1 || index > DATABASE_SIZE) {
            return "ERROR";
        }

        String textRetrieved = database[index - 1];
        if (textRetrieved.equals("")) {
            return "ERROR";
        } else {
            return textRetrieved;
        }
    }

    public static String delete(int index) {
        if (index < 1 || index > DATABASE_SIZE) {
            return "ERROR";
        }

        database[index - 1] = "";
        return "OK";
    }
}
