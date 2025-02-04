package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    public static void main(String... argv) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);

        String address = "127.0.0.1";
        int port = 23456;
        try {
            InetAddress inetAddress = InetAddress.getByName(address);
            try (Socket socket = new Socket(inetAddress, port);
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                 DataInputStream input = new DataInputStream(socket.getInputStream());
            ) {
                System.out.println("Client started!");

                String type = main.type;
                String key = main.key;
                String value = main.value;

                Request request = new Request(type);
                switch (type) {
                    case "exit":
                        request = new Request(type);
                        break;
                    case "get":
                    case "delete":
                        request = new Request(type, key);
                        break;
                    case "set":
                        request = new Request(type, key, value);
                        break;
                }

                String requestAsJson = Request.serializeToGson(request);
                System.out.println("Sent: " + requestAsJson);
                output.writeUTF(requestAsJson);

                if (!type.equalsIgnoreCase("exit")) {
                    String serverResponse = input.readUTF();
                    System.out.println("Received: " + serverResponse);
                }
            }
        } catch (IOException e) {
            System.err.println("Unexpected IO error: " + e.getMessage());
        }
    }
}
