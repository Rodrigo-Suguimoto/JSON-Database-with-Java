package server;

import java.net.InetAddress;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 23456;
        ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
    }

}
