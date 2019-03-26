package com.mdgriffin.distributedcomputingproject.server;

import com.mdgriffin.distributedcomputingproject.common.Message;
import org.apache.log4j.Logger;

import javax.net.ServerSocketFactory;
import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class SSLSocketServer implements Server {

    private int portNum;
    private RequestHandler requestHandler;

    private final Logger log = Logger.getLogger(getClass());

    public SSLSocketServer(int portNum, RequestHandler requestHandler) {
        System.setProperty("javax.net.ssl.keyStore", "./src/main/resources/ssl/server.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password123");
        System.setProperty("javax.net.ssl.trustStore", "./src/main/resources/ssl/trustedCerts.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password123");
        this.portNum = portNum;
        this.requestHandler = requestHandler;
    }

    /*
    private static ServerSocketFactory getServerSocketFactory() throws Exception {
        SSLServerSocketFactory ssf = null;
        // set up key manager to do server authentication
        SSLContext ctx;
        KeyManagerFactory kmf;
        KeyStore ks;
        char[] passphrase = "password123".toCharArray();

        ctx = SSLContext.getInstance("DTLS");
        kmf = KeyManagerFactory.getInstance("SunX509");
        ks = KeyStore.getInstance("JKS");

        ks.load(new FileInputStream("./src/main/resources/ssl/server.jks"), passphrase);
        kmf.init(ks, passphrase);
        ctx.init(kmf.getKeyManagers(), null, null);

        ssf = ctx.getServerSocketFactory();
        return ssf;
    }
    */

    public void listen () throws Exception {
        SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        ServerSocket serverSocket = ssf.createServerSocket(portNum);

        while (true) {
            try {
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                BufferedReader in = new BufferedReader((new InputStreamReader(socket.getInputStream())));
                String line = null;
                PrintStream out = new PrintStream(socket.getOutputStream());

                while (((line = in.readLine()) != null)) {
                    log.debug(line);
                    out.println(handleRequest(Message.fromJson(line)));
                    //out.flush();
                }

                in.close();
                out.close();
                socket.close();
            } catch (IOException exc) {
                log.debug(exc.getMessage());
            }
        }
    }

    private String handleRequest (Message message) throws IOException {
        String result = "";

        switch (message.getRequest()) {
            case LOGIN:
                result = requestHandler.login(message).toJson();
                break;
            case LIST:
                result = requestHandler.list(message).toJson();
                break;
            case UPLOAD:
                result = requestHandler.upload(message).toJson();
                break;
            case DOWNLOAD:
                result = requestHandler.download(message).toJson();
                break;
            case LOGOFF:
                result = requestHandler.logoff(message).toJson();
            default:
                break;
        }

        return result;
    }

}