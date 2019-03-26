package com.mdgriffin.distributedcomputingproject.client;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;

public class SSLSocketClient {

    private String remoteHost;
    private int portNum;
    private SSLSocketFactory ssf;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public SSLSocketClient(String remoteHost, int portNum) throws IOException {
        // -Djavax.net.debug=all
        System.setProperty("javax.net.ssl.keyStore", "./src/main/resources/ssl/client.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password123");
        System.setProperty("javax.net.ssl.trustStore", "./src/main/resources/ssl/trustedCerts.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password123");

        this.remoteHost = remoteHost;
        this.portNum = portNum;
        this.ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
    }

    private void open () throws IOException {
        if (socket == null || socket.isClosed()) {
            this.socket = ssf.createSocket(remoteHost, portNum);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }

    public void send(String message) throws IOException {
        open();
        out.println(message);
    }

    public String receive () throws IOException {
        open();
        String receivedMessage = in.readLine();
        return receivedMessage;
    }

    public void close () throws IOException {
        out.close();
        in.close();
        socket.close();
    }
}
