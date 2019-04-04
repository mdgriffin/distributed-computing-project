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
            //InetSocketAddress clientSocketAddr = new InetSocketAddress(InetAddress.getLocalHost(), 7777);
            DTLSSocket dtlsSocket = new DTLSSocket(clientSocket, serverSocketAddr, "Client");
            String message = "Hello from Client";
            ByteBuffer bf = ByteBuffer.allocate(message.getBytes().length);
            bf.put(message.getBytes());
            bf.flip();

            dtlsSocket.send(bf, serverSocketAddr);
            ByteBuffer received = dtlsSocket.receive();

            String s = StandardCharsets.UTF_8.decode(received).toString();
            System.out.println("Client: Receiving message");
            System.out.println(s);
        } catch (Exception exc) {
            System.out.println(exc);
        }

    }
}
