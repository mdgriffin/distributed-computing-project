package com.mdgriffin.distributedcomputingproject.common;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DTLSServer {
    public static void main(String[] args) {
        try {
            try {
                DatagramSocket serverSocket = new DatagramSocket(9090);
                InetSocketAddress clientSocketAddr = new InetSocketAddress("localhost",  7777);
                DTLSSocket dtlsSocket = new DTLSSocket(serverSocket, clientSocketAddr, "Server");

                Message message = dtlsSocket.receive();
                System.out.println(message.toJson());

                dtlsSocket.send(new Message(
                    Request.LOGIN,
                    null,
                    Arrays.asList(new KeyValue("message", "Hello From Client")),
                    ""
                ));
            } catch (Exception exc) {
                System.out.println(exc);
            }
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }
}
