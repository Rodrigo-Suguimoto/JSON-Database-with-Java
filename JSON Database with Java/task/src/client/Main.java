package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;

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

                ClientRequest request = new ClientRequest(requestType);
                switch (requestType) {
                    case "exit":
                        request = new ClientRequest(requestType);
                        break;
                    case "get":
                    case "delete":
                        request = new ClientRequest(requestType, key);
                        break;
                    case "set":
                        request = new ClientRequest(requestType, key, value);
                        break;
                }

                String requestAsJson = new RequestAsJson(request).getRequestAsJson();
                System.out.println(requestAsJson);

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

class ClientRequest {
    private String requestType;
    private String key;
    private String value;

    public ClientRequest(String requestType, String key, String value) {
        this.requestType = requestType;
        this.key = key;
        this.value = value;
    }

    public ClientRequest(String requestType, String key) {
        this(requestType, key, "");
    }

    public ClientRequest(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}

class RequestAsJson {
    private String requestAsJson;

    public RequestAsJson(ClientRequest request) {
        this.requestAsJson = new Gson().toJson(request);
    }

    public String getRequestAsJson() {
        return requestAsJson;
    }
}
