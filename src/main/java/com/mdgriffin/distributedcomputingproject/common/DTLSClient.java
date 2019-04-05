package com.mdgriffin.distributedcomputingproject.common;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DTLSClient {
    public static void main(String[] args) {
        try {
            DatagramSocket clientSocket = new DatagramSocket(7777);
            InetSocketAddress serverSocketAddr = new InetSocketAddress("localhost", 9090);
            DTLSSocket dtlsSocket = new DTLSSocket(clientSocket, serverSocketAddr, "Client");
            dtlsSocket.send("Hello from Client".getBytes(), serverSocketAddr);

            byte[] response = dtlsSocket.receive();
            System.out.println(new String(response));
        } catch (Exception exc) {
            System.out.println(exc);
        }

    }
}
