package com.mdgriffin.distributedcomputingproject.server;

import com.mdgriffin.distributedcomputingproject.common.DatagramMessage;
import com.mdgriffin.distributedcomputingproject.common.SocketHelper;

import java.io.IOException;
import java.net.DatagramSocket;

public class Server {

    private static final int PORT_NUM = 9090;

    public static void main(String[] args) {
        System.out.println("Server Ready for connections");

        try {
            DatagramSocket datagramSocket = new DatagramSocket(PORT_NUM);
            SocketHelper socketHelper = new SocketHelper(datagramSocket);

            while (true) {
                DatagramMessage receivedMessage = socketHelper.receive();
                System.out.println(receivedMessage.getMessage());

                socketHelper.send(new DatagramMessage("Response from server", receivedMessage.getAddress(), receivedMessage.getPortNum()));
            }
        } catch (IOException exc) {
            System.out.println(exc);
        }
    }

}
