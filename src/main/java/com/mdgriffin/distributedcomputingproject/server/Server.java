package com.mdgriffin.distributedcomputingproject.server;

import com.mdgriffin.distributedcomputingproject.common.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Arrays;

public class Server {

    private static final int PORT_NUM = 9090;
    private DatagramSocket datagramSocket;
    private SocketHelper socketHelper;

    public static void main(String[] args) {
        Server server = new Server();
    }

    public Server () {
        System.out.println("Server Ready for connections");

        try {
            this.datagramSocket = new DatagramSocket(PORT_NUM);
            this.socketHelper = new SocketHelper(datagramSocket);

            while (true) {
                DatagramMessage receivedMessage = socketHelper.receive();
                Message message = Message.fromJson(receivedMessage.getMessage());

                switch (message.getRequest()) {
                    case LOGIN:
                        handleLogin(receivedMessage, message);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException exc) {
            System.out.println(exc);
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }

    private void handleLogin (DatagramMessage datagramMessage, Message message) throws IOException {
        socketHelper.send(new DatagramMessage(new Message(
                null,
                Response.SUCCESS,
                Arrays.asList(new KeyValue("message", "You have successfully logged in")),
                ""
        ).toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
    }

}
