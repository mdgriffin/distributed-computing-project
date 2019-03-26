package com.mdgriffin.distributedcomputingproject.nio;

public class ServerTest {
    public static void main(String[] args) {
        try {
            NioSslServer server = new NioSslServer("TLSv1.2", "localhost", 9222);
            server.start();
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }
}
