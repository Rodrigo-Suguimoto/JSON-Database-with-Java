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
    String requestType;
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
            try (Socket socket = new Socket(inetAddress, port)) {
                System.out.println("Client started!");
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                DataInputStream input = new DataInputStream(socket.getInputStream());

                String requestType = main.requestType;
                String key = main.key;
                String value = main.value;

                Request request = new Request(requestType);
                switch (requestType) {
                    case "exit":
                        request = new Request(requestType);
                        break;
                    case "get":
                    case "delete":
                        request = new Request(requestType, key);
                        break;
                    case "set":
                        request = new Request(requestType, key, value);
                        break;
                }

                String requestAsJson = Request.serializeToGson(request);
                System.out.println("Sent: " + requestAsJson);
                output.writeUTF(requestAsJson);

//                if (!requestType.equalsIgnoreCase("exit")) {
//                    String serverResponse = input.readUTF();
//                    System.out.println("Received: " + serverResponse);
//                }
            }
        } catch (IOException e) {
            System.err.println("Unexpected IO error: " + e.getMessage());
        }
    }
}
