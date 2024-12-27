package client;

import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 23456;
        try (Socket socket = new Socket(InetAddress.getByName(address), port)) {
            System.out.println("Client started!");
        }
    }
}
