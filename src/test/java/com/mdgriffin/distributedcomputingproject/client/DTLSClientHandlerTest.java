package com.mdgriffin.distributedcomputingproject.client;

import com.mdgriffin.distributedcomputingproject.common.Message;

import java.io.IOException;

public class DTLSClientHandlerTest {

    public static void main(String[] args) {
        try {
            SSLSocketClientHandler dtlsClientHandler = new SSLSocketClientHandler("localhost", 9090);

            Message message = dtlsClientHandler.login("jdoe", "password123");
            System.out.println(message.toJson());

            boolean didUpload = dtlsClientHandler.upload("/Users/mdgri/Desktop/java_ssl_server.PNG", "java_ssl_server.PNG");
            System.out.println("Did upload" + didUpload);

            boolean didLogOut = dtlsClientHandler.logoff();
            System.out.println("Did Logout: " + didLogOut);
        } catch (IOException exc) {
            System.out.println("Client Exception:\n" + exc.getMessage());
        }
    }
}
