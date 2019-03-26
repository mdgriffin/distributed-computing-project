package com.mdgriffin.distributedcomputingproject.server;

import com.mdgriffin.distributedcomputingproject.common.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class SocketServer implements Server {

    private DatagramSocket datagramSocket;
    private SocketHelper socketHelper;
    private RequestHandler requestHandler;
    private boolean isListening = false;

    public SocketServer(int portNum, RequestHandler requestHandler) throws SocketException {
        System.out.println("SocketServer Ready for connections");
        this.requestHandler = requestHandler;
        this.datagramSocket = new DatagramSocket(portNum);
        this.socketHelper = new SocketHelper(datagramSocket);
    }

    public void listen () {
        if (!isListening) {
            isListening = true;
            try {
                while (true) {
                    DatagramMessage receivedMessage = socketHelper.receive();
                    Message message = Message.fromJson(receivedMessage.getMessage());
                    socketHelper.send(new DatagramMessage(handleRequest(message), receivedMessage.getAddress(), receivedMessage.getPortNum()));
                }
            } catch (Exception exc) {

                System.out.println(exc);
            }
        }
    }

    private String handleRequest (Message requestMessage) throws IOException {
        String responseMessage;

        switch (requestMessage.getRequest()) {
            case LOGIN:
                responseMessage = requestHandler.login(requestMessage).toJson();
                break;
            case LIST:
                responseMessage = requestHandler.list(requestMessage).toJson();
                break;
            case UPLOAD:
                responseMessage = requestHandler.upload(requestMessage).toJson();
                break;
            case DOWNLOAD:
                responseMessage = requestHandler.download(requestMessage).toJson();
                break;
            case LOGOFF:
                responseMessage = requestHandler.logoff(requestMessage).toJson();
            default:
                responseMessage = new Message(
                    requestMessage.getRequest(),
                    Response.ERROR,
                    Arrays.asList(new KeyValue("message", "Invalid Operation")),
                    ""
                ).toJson();
                break;
        }

        return responseMessage;
    }

}