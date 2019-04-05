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

                byte[] message = dtlsSocket.receive();
                System.out.println(new String(message));

                dtlsSocket.send("Hello From Server".getBytes(), clientSocketAddr);
            } catch (Exception exc) {
                System.out.println(exc);
            }
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }
}
