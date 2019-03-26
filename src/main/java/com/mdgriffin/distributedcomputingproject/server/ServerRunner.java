package com.mdgriffin.distributedcomputingproject.server;

import org.apache.log4j.Logger;

import java.io.IOException;

public class ServerRunner {

    private static final Logger log = Logger.getLogger(SSLSocketServer.class.getName());

    public static void main(String[] args) {
        try {
            SSLSocketServer dtlsServer = new SSLSocketServer(9090, new RequestHandlerImpl());
            dtlsServer.listen();
        } catch (IOException exc) {
            log.debug(exc.getMessage());
        } catch (Exception exc) {
            log.debug(exc.getMessage());
        }
    }

}