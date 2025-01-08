package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Main {
    @Parameter(names={"--type", "-t"})
    String requestType;
    @Parameter(names={"--index", "-i"})
    int index;
    @Parameter(names={"--message", "-m"})
    String message = "";

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
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                String requestType = main.requestType;
                int index = main.index;
                String message = main.message;

                ClientRequest request;
                if (message.equals("")) {
                    request = new ClientRequest(requestType, index);
                } else {
                    request = new ClientRequest(requestType, index, message);
                }

                System.out.println("Sent: " + request.getTextToSend());

//                System.out.println("Received: " + input.readUTF());
            }
        } catch (IOException e) {
            System.err.println("Unexpected IO error: " + e.getMessage());
        }
    }
}

class ClientRequest {
    private String requestType;
    private int index;
    private String message;
    private String textToSend;

    public ClientRequest(String requestType, int index, String message) {
        this.requestType = requestType;
        this.index = index;
        this.message = message;
        this.textToSend = String.format("%s %d %s", requestType, index, message);
    }

    public ClientRequest(String requestType, int index) {
        this(requestType, index, "");
        this.textToSend = String.format("%s %s", requestType, index);
    }

    public String getRequestType() {
        return requestType;
    }

    public int getIndex() {
        return index;
    }

    public String getMessage() {
        return message;
    }

    public String getTextToSend() {
        return textToSend;
    }
}
