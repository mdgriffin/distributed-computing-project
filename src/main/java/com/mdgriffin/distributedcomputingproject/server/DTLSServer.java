package com.mdgriffin.distributedcomputingproject.server;

import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;

public class DTLSServer {

    private int portNum;

    public static void main(String[] args) {
        try {
            DTLSServer dtlsServer = new DTLSServer(9090);
            dtlsServer.listen();
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }
    }

    public DTLSServer (int portNum) {
        System.setProperty("javax.net.ssl.keyStore", "./src/main/resources/ssl/server.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password123");
        System.setProperty("javax.net.ssl.trustStore", "./src/main/resources/ssl/trustedCerts.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password123");
        this.portNum = portNum;
    }


    public void listen () throws IOException {
        SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        ServerSocket serverSocket = ssf.createServerSocket(portNum);

        while (true) {
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            BufferedReader in = new BufferedReader((new InputStreamReader(socket.getInputStream())));
            String line = null;
            PrintStream out = new PrintStream(socket.getOutputStream());

            while(((line = in.readLine()) != null)) {
                System.out.println(line);
                out.println("Hi, Client");
            }
            in.close();
            out.close();
            socket.close();
        }
    }

}
