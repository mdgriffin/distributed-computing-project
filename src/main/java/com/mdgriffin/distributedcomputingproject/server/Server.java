package com.mdgriffin.distributedcomputingproject.server;

import com.mdgriffin.distributedcomputingproject.common.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {

    private static final int PORT_NUM = 9090;
    private DatagramSocket datagramSocket;
    private SocketHelper socketHelper;
    private RequestHandler requestHandler;

    public static void main(String[] args) {
        try {
            Server server = new Server(new RequestHandlerImpl());
            server.listen();
        } catch (IOException exc) {
            System.out.println("Error connecting to socket");
        }
    }

    public Server (RequestHandler requestHandler) throws SocketException {
        System.out.println("Server Ready for connections");
        this.requestHandler = requestHandler;
        this.datagramSocket = new DatagramSocket(PORT_NUM);
        this.socketHelper = new SocketHelper(datagramSocket);
    }

    public void listen () {
        // TODO: What if listen was called multiple times, add boolean to indicate isListening
        try {
            while (true) {
                DatagramMessage receivedMessage = socketHelper.receive();
                Message message = Message.fromJson(receivedMessage.getMessage());

                switch (message.getRequest()) {
                    case LOGIN:
                        handleLogin(receivedMessage, message);
                        break;
                    case LIST:
                        handleList(receivedMessage, message);
                        break;
                    case UPLOAD:
                        handleUpload(receivedMessage, message);
                        break;
                    case DOWNLOAD:
                        handleDownload(receivedMessage,  message);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }

    private void handleDownload (DatagramMessage datagramMessage, Message message) throws IOException {
        Message result = requestHandler.download(message);
        socketHelper.send(new DatagramMessage(result.toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
    }

    private void handleUpload (DatagramMessage datagramMessage, Message message) throws IOException {
        Message result = requestHandler.upload(message);
        socketHelper.send(new DatagramMessage(result.toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
    }

    private void handleList (DatagramMessage datagramMessage, Message message) throws IOException {
        Message result = requestHandler.list(message);
        socketHelper.send(new DatagramMessage(result.toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
    }

    private void handleLogin (DatagramMessage datagramMessage, Message message) throws IOException {
        Message result = requestHandler.login(message);
        socketHelper.send(new DatagramMessage(result.toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
    }

}
