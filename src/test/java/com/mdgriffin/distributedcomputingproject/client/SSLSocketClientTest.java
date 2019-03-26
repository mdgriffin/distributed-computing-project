package com.mdgriffin.distributedcomputingproject.client;

import java.io.IOException;

public class SSLSocketClientTest {

    public static void main(String[] args) {
        try {
            SSLSocketClient sslSocketClient = new SSLSocketClient("localhost", 9090);

            String firstMessage = sslSocketClient.receive();
            System.out.println(firstMessage);

            sslSocketClient.send("Hello Server");
            String response = sslSocketClient.receive();
            System.out.println(response);
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }


    }

}
