package com.mdgriffin.distributedcomputingproject.client;

import com.mdgriffin.distributedcomputingproject.common.*;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Client {

    private static final int SERVER_PORT_NUM = 9090;

    public static void main(String[] args) {
        Client client = new Client();
    }

    public Client () {
        try {
            login();
        } catch (IOException exc) {
            System.out.println(exc);
        }
    }

    private void login () throws IOException {
        DatagramSocket socket = new DatagramSocket();
        SocketHelper socketHelper = new SocketHelper(socket);

        socketHelper.send(new DatagramMessage(new Message(
                Request.LOGIN,
                null,
                Arrays.asList(
                        new KeyValue("username", "jdoe"),
                        new KeyValue("password", "password123")
                ),
                ""
        ).toJson(), "localhost", SERVER_PORT_NUM));

        DatagramMessage receivedMessage = socketHelper.receive();

        Message serverResponse = Message.fromJson(receivedMessage.getMessage());

        if (serverResponse.getResponse().equals(Response.SUCCESS)) {
            System.out.println("Successfully Logged In");
        } else {
            System.out.println("Failed to Login");
        }
    }

}
