package com.mdgriffin.distributedcomputingproject.server;

import com.mdgriffin.distributedcomputingproject.common.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;

public class DTLSServer implements Server {

    public static void main(String[] args) {
        try {
            Server server = new DTLSServer(9090, new RequestHandlerImpl());
            server.listen();
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }

    private DatagramSocket datagramSocket;
    private SocketHelper socketHelper;
    private RequestHandler requestHandler;
    private boolean isListening = false;


    public DTLSServer(int portNum, RequestHandler requestHandler) throws SocketException {
        this.requestHandler = requestHandler;
        this.datagramSocket = new DatagramSocket(portNum);
        this.socketHelper = new SocketHelper(datagramSocket);
    }

    @Override
    public void listen() throws Exception {
        if (!isListening) {
            isListening = true;
            System.out.println("Server Running");
            while (true) {
                DatagramMessage receivedMessage = socketHelper.receive();
                System.out.println("Accepting new connection: " + receivedMessage.getMessage());

                if (receivedMessage.getMessage().charAt(0) == 'H') {
                    String clientAddress = receivedMessage.getAddress();
                    int clientPortNun = receivedMessage.getPortNum();
                    System.out.println("Client: " + receivedMessage.getMessage());

                    socketHelper.send(new DatagramMessage("READY", clientAddress, clientPortNun));
                    InetSocketAddress clientSocketAddr = new InetSocketAddress(clientAddress,  clientPortNun);
                    DTLSSocket dtlsSocket = new DTLSSocket(datagramSocket, clientSocketAddr, "Server");

                    // TODO: Add close method
                    // TODO: Use Thread Pool and create new Thread for each client
                    while (true) {
                        Message message = dtlsSocket.receive();
                        dtlsSocket.send(handleRequest(message));
                    }
                } else {
                    System.out.println("Request Status Incorrect");
                }
            }
        }
    }

    private Message handleRequest (Message requestMessage) throws IOException {
        Message responseMessage;

        switch (requestMessage.getRequest()) {
            case LOGIN:
                responseMessage = requestHandler.login(requestMessage);
                break;
            case LIST:
                responseMessage = requestHandler.list(requestMessage);
                break;
            case UPLOAD:
                responseMessage = requestHandler.upload(requestMessage);
                break;
            case DOWNLOAD:
                responseMessage = requestHandler.download(requestMessage);
                break;
            case LOGOFF:
                responseMessage = requestHandler.logoff(requestMessage);
                break;
            default:
                responseMessage = new Message(
                        requestMessage.getRequest(),
                        Response.ERROR,
                        Arrays.asList(new KeyValue("message", "Invalid Operation")),
                        ""
                );
                break;
        }

        return responseMessage;
    }
}
