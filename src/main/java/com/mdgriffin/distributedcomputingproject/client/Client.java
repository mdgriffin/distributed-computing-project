package com.mdgriffin.distributedcomputingproject.client;

import com.mdgriffin.distributedcomputingproject.common.*;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.NoSuchElementException;

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

        try {
            if (serverResponse.getResponse().equals(Response.SUCCESS)) {
                String sessionId = serverResponse.getHeaders().stream().filter(name -> name.getKey().equals("session_id")).findFirst().orElseThrow(() -> new NoSuchElementException()).getValue();

                System.out.println("Successfully Logged In with session_id of " + sessionId);
            } else {
                System.out.println("Failed to Login");
            }
        } catch (NoSuchElementException exc) {
            System.out.println("No session id returned from server");
        }
    }

}
