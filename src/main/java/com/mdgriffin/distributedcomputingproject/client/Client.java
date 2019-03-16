package com.mdgriffin.distributedcomputingproject.client;

import com.mdgriffin.distributedcomputingproject.common.*;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class Client {

    private static final int SERVER_PORT_NUM = 9090;
    private static final String USERNAME = "jdoe";
    private static final String PASSWORD = "password123";

    private String sessionId;

    public static void main(String[] args) {
        Client client = new Client();
    }

    public Client () {
        try {
            login();
            listFiles();
        } catch (IOException exc) {
            System.out.println(exc);
        }
    }

    private void listFiles () throws IOException {
        if (isLoggedIn()) {
            // TODO DatagramSocket creation could be moved to SocketHelper
            DatagramSocket socket = new DatagramSocket();
            SocketHelper socketHelper = new SocketHelper(socket);

            socketHelper.send(new DatagramMessage(new Message(
                    Request.LIST,
                    null,
                    Arrays.asList(
                        new KeyValue("username", USERNAME),
                        new KeyValue("session_id", sessionId)
                    ),
                    ""
            ).toJson(), "localhost", SERVER_PORT_NUM));

            Message serverResponse = Message.fromJson(socketHelper.receive().getMessage());

            if (serverResponse.getResponse().equals(Response.SUCCESS)) {
                System.out.println("Got Response from Server:\n" + serverResponse.getBody());
            } else {
                System.out.println("Login Response: " + serverResponse.getResponse());
            }
        }
    }

    private void login () throws IOException {
        DatagramSocket socket = new DatagramSocket();
        SocketHelper socketHelper = new SocketHelper(socket);

        socketHelper.send(new DatagramMessage(new Message(
                Request.LOGIN,
                null,
                Arrays.asList(
                    new KeyValue("username", USERNAME),
                    new KeyValue("password", PASSWORD)
                ),
                ""
        ).toJson(), "localhost", SERVER_PORT_NUM));

        Message serverResponse = Message.fromJson(socketHelper.receive().getMessage());

        try {
            if (serverResponse.getResponse().equals(Response.SUCCESS)) {
                sessionId = serverResponse.getHeaders().stream().filter(name -> name.getKey().equals("session_id")).findFirst().orElseThrow(() -> new NoSuchElementException()).getValue();

                System.out.println("Successfully Logged In with session_id of " + sessionId);
            } else {
                System.out.println("Failed to Login");
            }
        } catch (NoSuchElementException exc) {
            System.out.println("No session id returned from server");
        }
    }

    private boolean isLoggedIn () {
        return sessionId != null;
    }

}
