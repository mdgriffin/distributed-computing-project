package com.mdgriffin.distributedcomputingproject.client;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.KeyStore;

public class SSLSocketClient {

    private String remoteHost;
    private int portNum;
    private SSLSocketFactory ssf;
    private SSLSocket socket;
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
        //this.ssf = getSocketFactory();
    }

    /*
    private static SSLSocketFactory getSocketFactory() throws Exception {
        SSLSocketFactory ssf = null;
        // set up key manager to do server authentication
        SSLContext ctx;
        KeyManagerFactory kmf;
        KeyStore ks;
        char[] passphrase = "password123".toCharArray();

        ctx = SSLContext.getInstance("DTLS");
        kmf = KeyManagerFactory.getInstance("SunX509");
        ks = KeyStore.getInstance("JKS");

        ks.load(new FileInputStream("./src/main/resources/ssl/client.jks"), passphrase);
        kmf.init(ks, passphrase);
        ctx.init(kmf.getKeyManagers(), null, null);

        //ssf = ctx.getServerSocketFactory();
        ssf = ctx.getSocketFactory();
        return ssf;
    }
    */

    private void open () throws IOException {
        if (socket == null || socket.isClosed()) {
            this.socket = (SSLSocket) ssf.createSocket(remoteHost, portNum);
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
