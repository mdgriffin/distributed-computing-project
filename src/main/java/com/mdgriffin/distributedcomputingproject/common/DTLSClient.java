package com.mdgriffin.distributedcomputingproject.common;

import com.mdgriffin.distributedcomputingproject.client.ClientHandler;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.util.List;

public class DTLSClient implements ClientHandler {

    private String remoteHost;
    private int portNum;

    public static void main(String[] args) {
        DTLSClient dtlsClient = new DTLSClient("127.0.0.1", 9090);
        dtlsClient.openConnection();
    }

    public DTLSClient (String remoteHost, int portNum) {
        // -Djavax.net.debug=all
        System.setProperty("javax.net.ssl.keyStore", "./src/main/resources/ssl/client.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password123");
        System.setProperty("javax.net.ssl.trustStore", "./src/main/resources/ssl/trustedCerts.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password123");

        this.remoteHost = remoteHost;
        this.portNum = portNum;
    }

    private void openConnection () {
        try {
            SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();

            Socket socket = ssf.createSocket(remoteHost, portNum);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Hello Server");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String x = in.readLine();

            System.out.println(x);

            out.close();
            in.close();
            socket.close();
        } catch (IOException exc) {
            System.out.println("IOException");
            System.out.println(exc.getMessage());
        }
    }

    private void send() {

    }


    private void receive () {

    }

    @Override
    public Message login(String username, String password) throws IOException {
        return null;
    }

    @Override
    public boolean upload(String path, String filename) throws IOException {
        return false;
    }

    @Override
    public void download(String destinationPath, String filename, String newFileName) throws IOException {

    }

    @Override
    public void download(String destinationPath, String filename) throws IOException {

    }

    @Override
    public List<FileDescription> list() throws IOException {
        return null;
    }

    @Override
    public void setSessionId(String sessionId) {

    }

    @Override
    public boolean logoff() {
        return false;
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }
}
