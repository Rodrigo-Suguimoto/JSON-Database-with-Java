package client;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String input;
        do {
            input = scanner.nextLine();
            String[] parts = input.split(" ");
            String command = parts[0];

            switch (command) {
                case "set":
                    if (parts.length >= 3) {
                        int index = Integer.parseInt(parts[1]);
                        String text = "";

                        for (int i = 2; i < parts.length; i++) {
                            text += parts[i] + " ";
                        }

                        text = text.trim(); // Remove white space
                        String serverResponse = server.Main.set(index, text);

                        System.out.println(serverResponse);
                    } else {
                        System.out.println("ERROR");
                    }
                    break;
                case "get":
                    if (parts.length == 2) {
                        int index = Integer.parseInt(parts[1]);
                        System.out.println(server.Main.get(index));
                    } else {
                        System.out.println("ERROR");
                    }
                    break;
                case "delete":
                    if (parts.length == 2) {
                        int index = Integer.parseInt(parts[1]);
                        System.out.println(server.Main.delete(index));
                    } else {
                        System.out.println("ERROR");
                    }
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("ERROR");
                    break;
            }
        } while (!input.equals("exit"));
    }
}
