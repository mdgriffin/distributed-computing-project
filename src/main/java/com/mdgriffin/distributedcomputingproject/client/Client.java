package com.mdgriffin.distributedcomputingproject.client;

import com.mdgriffin.distributedcomputingproject.common.SocketHelper;

import java.io.*;
import java.net.*;

public class Client {

    private static final int SERVER_PORT_NUM = 9090;

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();
            SocketHelper socketHelper = new SocketHelper(socket);

            socketHelper.send("localhost", SERVER_PORT_NUM, "Hello From Client!");
        } catch (IOException exc) {
            System.out.println(exc);
        }
    }

    /*
    private void sendMessage (DatagramSocket socket, String message) {
        try {
            byte[ ] sendBuffer = message.getBytes( );
            InetAddress serverHost = InetAddress.getByName("localhost");

            DatagramPacket packet = new DatagramPacket(sendBuffer, sendBuffer.length, serverHost, 9090);
            socket.send(packet);
        } catch (UnknownHostException exc) {
            System.out.println(exc);
        } catch (IOException exc) {
            System.out.println(exc);
        }
    }
    */
}
