package com.mdgriffin.distributedcomputingproject.server;

import com.mdgriffin.distributedcomputingproject.common.Message;
import org.apache.log4j.Logger;

import javax.net.ServerSocketFactory;
import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.KeyStore;

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
        char[] passphrase = "password123".toCharArray();

        SSLContext ctx = SSLContext.getInstance("DTLS");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("JKS");

        ks.load(new FileInputStream("./src/main/resources/ssl/server.jks"), passphrase);
        kmf.init(ks, passphrase);

        KeyStore ts = KeyStore.getInstance("JKS");
        ts.load(new FileInputStream("./src/main/resources/ssl/trustedCerts.jks"), passphrase);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ts);

        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return ctx.getServerSocketFactory();
    }
    */

    public void listen () throws Exception {
        SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        //SSLServerSocketFactory ssf = (SSLServerSocketFactory) getServerSocketFactory();
        ServerSocket serverSocket = ssf.createServerSocket(portNum);

        log.debug("Server Listening");

        while (true) {
            try {
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                log.debug("Server Accepted New Client Connection");
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