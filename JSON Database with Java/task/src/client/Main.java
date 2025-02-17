package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.InetAddress;
import java.net.Socket;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import shared.Request;

public class Main {
    @Parameter(names={"--type", "-t"})
    String type;
    @Parameter(names={"--key", "-k"})
    String key;
    @Parameter(names={"--value", "-v"})
    String value = "";
    @Parameter(names={"--input", "-in"})
    String input;

    public static void main(String... argv) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);

        String address = "127.0.0.1";
        int port = 23456;
        String type = main.type;
        String key = main.key;
        String value = main.value;
        String fileName = main.input;

        handleUserInput(type, key, value, fileName);
    }

    private static void handleUserInput(String type, String key, String value, String fileName) {
        String address = "127.0.0.1";
        int port = 23456;

        try {
            InetAddress inetAddress = InetAddress.getByName(address);
            try (Socket socket = new Socket(inetAddress, port);
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                 DataInputStream input = new DataInputStream(socket.getInputStream());
            ) {
                System.out.println("Client started!");

                if (fileName != null) {
                    try {
                        String pathToFile = System.getProperty("user.dir") + "/client/data/" + fileName;
                        String jsonRequest =  Files.readString(Paths.get(pathToFile));
                        System.out.println("Sent: " + jsonRequest);
                        output.writeUTF(jsonRequest);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                } else {
                    Request request = new Request(type);
                    request = switch (type) {
                        case "exit" -> new Request(type);
                        case "get", "delete" -> new Request(type, key);
                        case "set" -> new Request(type, key, value);
                        default -> request;
                    };

                    String requestAsJson = Request.serializeToGson(request);
                    System.out.println("Sent: " + requestAsJson);
                    output.writeUTF(requestAsJson);
                }

                try {
                    String serverResponse = input.readUTF();
                } catch (EOFException e) {
                    // Do nothing, silently handle the server closure
                }

            }
        } catch (IOException e) {
            System.err.println("Unexpected IO error: " + e.getMessage());
        }
    }
}
