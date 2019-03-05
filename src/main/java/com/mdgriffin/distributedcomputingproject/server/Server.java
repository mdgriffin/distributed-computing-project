package com.mdgriffin.distributedcomputingproject.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {

    private static final int PORT_NUM = 9090;
    private static final int MAX_LEN = 100;

    public static void main(String[] args) {
        System.out.println("Server Ready for connections");

        Server server = new Server();

        try {
            DatagramSocket datagramSocket = new DatagramSocket(PORT_NUM);

            while (true) {
                server.receiveMessage(datagramSocket);
            }
        } catch (SocketException exc) {
            System.out.println(exc.getMessage());
        }
    }

    private void receiveMessage (DatagramSocket datagramSocket) {
        try {
            byte[] receiveBuffer = new byte[MAX_LEN];
            DatagramPacket datagram = new DatagramPacket(receiveBuffer, MAX_LEN);
            datagramSocket.receive(datagram);
            String message = new String(receiveBuffer);

            System.out.println("Received Message: " + message);
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }
    }
}
