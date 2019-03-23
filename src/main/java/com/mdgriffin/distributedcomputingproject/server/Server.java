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
    private boolean isListening = false;

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
        if (!isListening) {
            isListening = true;
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
                            handleDownload(receivedMessage, message);
                            break;
                        case LOGOFF:
                            handleLogoff(receivedMessage, message);
                        default:
                            break;
                    }
                }
            } catch (Exception exc) {

                System.out.println(exc);
            }
        }
    }

    private void handleDownload (DatagramMessage datagramMessage, Message message) throws IOException {
        socketHelper.send(new DatagramMessage(requestHandler.download(message).toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
    }

    private void handleUpload (DatagramMessage datagramMessage, Message message) throws IOException {
        socketHelper.send(new DatagramMessage(requestHandler.upload(message).toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
    }

    private void handleList (DatagramMessage datagramMessage, Message message) throws IOException {
        socketHelper.send(new DatagramMessage(requestHandler.list(message).toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
    }

    private void handleLogin (DatagramMessage datagramMessage, Message message) throws IOException {
        socketHelper.send(new DatagramMessage(requestHandler.login(message).toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
    }

    private void handleLogoff (DatagramMessage datagramMessage, Message message) throws IOException {
        socketHelper.send(new DatagramMessage(requestHandler.logoff(message).toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
    }

}
