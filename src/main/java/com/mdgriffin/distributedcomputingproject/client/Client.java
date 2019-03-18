package com.mdgriffin.distributedcomputingproject.client;

import com.mdgriffin.distributedcomputingproject.common.*;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.NoSuchElementException;

public class Client {

    private static final int SERVER_PORT_NUM = 9090;
    private static final String USERNAME = "jdoe";
    private static final String PASSWORD = "password123";
    private static final String ROOT_DIRECTORY = "/DC_Temp/DC_Client/";

    // TODO: Wrap in optional
    private String sessionId;

    public static void main(String[] args) {
        Client client = new Client();
    }

    public Client () {
        try {
            login();
            listFiles();
            upload();
            download();
        } catch (IOException exc) {
            System.out.println(exc);
        }
    }

    private void download () throws IOException {
        if (isLoggedIn()) {
            DatagramSocket socket = new DatagramSocket();
            SocketHelper socketHelper = new SocketHelper(socket);
            FileSystem fs = new FileSystemImpl(ROOT_DIRECTORY);
            String filename = "user_upload_01.txt";
            String savedFiledName = "user_download_01.txt";

            socketHelper.send(new DatagramMessage(new Message(
                    Request.DOWNLOAD,
                    null,
                    Arrays.asList(
                            new KeyValue("username", USERNAME),
                            new KeyValue("session_id", sessionId),
                            new KeyValue("filename", filename)
                    ),
                    null
            ).toJson(), "localhost", SERVER_PORT_NUM));

            Message serverResponse = Message.fromJson(socketHelper.receive().getMessage());
            // decode file contents
            byte[] fileContents = Base64.getDecoder().decode(serverResponse.getBody().getBytes());
            fs.saveFile(savedFiledName, fileContents);

            System.out.println("Successfully downloaded file");
        } else {
            System.out.println("Must be logged in to download files");
        }
    }

    private void upload () throws IOException {
        if (isLoggedIn()) {
            DatagramSocket socket = new DatagramSocket();
            SocketHelper socketHelper = new SocketHelper(socket);
            FileSystem fs = new FileSystemImpl(ROOT_DIRECTORY);
            String filename = "user_upload_01.txt";
            String fileContents = Base64.getEncoder().encodeToString(fs.readFile(filename));

            socketHelper.send(new DatagramMessage(new Message(
                    Request.UPLOAD,
                    null,
                    Arrays.asList(
                        new KeyValue("username", USERNAME),
                        new KeyValue("session_id", sessionId),
                        new KeyValue("filename", filename)
                    ),
                    fileContents
            ).toJson(), "localhost", SERVER_PORT_NUM));

            Message serverResponse = Message.fromJson(socketHelper.receive().getMessage());
            String message = serverResponse.getHeaders().stream().filter(name -> name.getKey().equals("message")).findFirst().orElseThrow(() -> new NoSuchElementException()).getValue();

            if (serverResponse.getResponse().equals(Response.SUCCESS)) {
                System.out.println("Got Response from Server:\n" + message);
            } else {
                System.out.println("Upload Response: " + serverResponse.getResponse());
            }
        } else {
            System.out.println("Must be logged in to upload files");
        }
    }

    private void listFiles () throws IOException {
        if (isLoggedIn()) {
            // TODO DatagramSocket creation could be moved to SocketHelper
            DatagramSocket socket = new DatagramSocket();
            // TODO: Initialize Socket Helper with port number and address, removing the need for passing the datagram
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
        } else {
            System.out.println("Must be logged in to list files");
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
