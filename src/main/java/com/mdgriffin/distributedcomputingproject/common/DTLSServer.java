package com.mdgriffin.distributedcomputingproject.common;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DTLSServer {
    public static void main(String[] args) {
        try {
            try {
                DatagramSocket serverSocket = new DatagramSocket(9090);
                InetSocketAddress clientSocketAddr = new InetSocketAddress("localhost",  7777);
                DTLSSocket dtlsSocket = new DTLSSocket(serverSocket, clientSocketAddr, "Server");
                ByteBuffer bf = dtlsSocket.receive();
                String s = StandardCharsets.UTF_8.decode(bf).toString();
                System.out.println("Server Receiving");
                System.out.println(s);

                String message = "Hello from Server";
                ByteBuffer messageBuffer = ByteBuffer.allocate(message.getBytes().length);
                messageBuffer.put(message.getBytes());
                messageBuffer.flip();

                dtlsSocket.send(messageBuffer, clientSocketAddr);
            } catch (Exception exc) {
                System.out.println(exc);
            }
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }
}
