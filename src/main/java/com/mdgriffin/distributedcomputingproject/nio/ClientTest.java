package com.mdgriffin.distributedcomputingproject.nio;

public class ClientTest {
    public static void main(String[] args) {
        try {
            NioSslClient client = new NioSslClient("TLSv1.2", "localhost", 9222);
            client.connect();
            client.write("Hello! I am a client!");
            client.read();
            client.shutdown();

            NioSslClient client2 = new NioSslClient("TLSv1.2", "localhost", 9222);
            NioSslClient client3 = new NioSslClient("TLSv1.2", "localhost", 9222);
            NioSslClient client4 = new NioSslClient("TLSv1.2", "localhost", 9222);

            client2.connect();
            client2.write("Hello! I am another client!");
            client2.read();
            client2.shutdown();

            client3.connect();
            client4.connect();
            client3.write("Hello from client3!!!");
            client4.write("Hello from client4!!!");
            client3.read();
            client4.read();
            client3.shutdown();
            client4.shutdown();
        } catch (Exception exc){
            System.out.println(exc.getMessage());
        }
    }
}
