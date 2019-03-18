package com.mdgriffin.distributedcomputingproject.client;

import com.mdgriffin.distributedcomputingproject.common.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Base64;
import java.util.NoSuchElementException;

public class ClientHandlerImpl implements ClientHandler {

    private String username;
    private String password;
    private String hostname;
    private int portnum;
    private String sessionId;
    private SocketHelper socketHelper;

    public ClientHandlerImpl(String username, String password, String hostname, int portNum) throws SocketException {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.portnum = portNum;

        DatagramSocket socket = new DatagramSocket();
        socketHelper = new SocketHelper(socket);
    }

    @Override
    public void download (String filename, String newFileName, String destinationPath) throws IOException {
        if (isLoggedIn()) {
            FileSystem fs = new FileSystemImpl(destinationPath);

            socketHelper.send(new DatagramMessage(new Message(
                    Request.DOWNLOAD,
                    null,
                    Arrays.asList(
                            new KeyValue("username", username),
                            new KeyValue("session_id", sessionId),
                            new KeyValue("filename", filename)
                    ),
                    null
            ).toJson(), hostname, portnum));

            Message serverResponse = Message.fromJson(socketHelper.receive().getMessage());

            if (serverResponse.getResponse().equals(Response.SUCCESS)) {
                // decode file contents
                byte[] fileContents = Base64.getDecoder().decode(serverResponse.getBody().getBytes());
                fs.saveFile(newFileName, fileContents);

                System.out.println("Successfully downloaded file");
            } else {
                String message = serverResponse.getHeaders().stream().filter(name -> name.getKey().equals("message")).findFirst().orElseThrow(() -> new NoSuchElementException()).getValue();
                System.out.println("Download Failed: " + message);
            }
        } else {
            System.out.println("Must be logged in to download files");
        }
    }

    @Override
    public void download(String destinationPath, String filename) throws IOException {
        download(destinationPath, filename, filename);
    }

    @Override
    public void upload (String path, String filename) throws IOException {
        if (isLoggedIn()) {
            FileSystem fs = new FileSystemImpl(path);
            String fileContents = Base64.getEncoder().encodeToString(fs.readFile(filename));

            socketHelper.send(new DatagramMessage(new Message(
                    Request.UPLOAD,
                    null,
                    Arrays.asList(
                            new KeyValue("username", username),
                            new KeyValue("session_id", sessionId),
                            new KeyValue("filename", filename)
                    ),
                    fileContents
            ).toJson(), hostname, portnum));

            Message serverResponse = Message.fromJson(socketHelper.receive().getMessage());
            String message = serverResponse.getHeaders().stream().filter(name -> name.getKey().equals("message")).findFirst().orElseThrow(() -> new NoSuchElementException()).getValue();

            if (serverResponse.getResponse().equals(Response.SUCCESS)) {
                System.out.println("Got Response from Server: " + message);
            } else {
                System.out.println("Upload Response: " + serverResponse.getResponse() + "\nError Message: " + message);
            }
        } else {
            System.out.println("Must be logged in to upload files");
        }
    }

    @Override
    public void list () throws IOException {
        if (isLoggedIn()) {
            socketHelper.send(new DatagramMessage(new Message(
                    Request.LIST,
                    null,
                    Arrays.asList(
                            new KeyValue("username", username),
                            new KeyValue("session_id", sessionId)
                    ),
                    ""
            ).toJson(), hostname, portnum));

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

    @Override
    public void login () throws IOException {
        socketHelper.send(new DatagramMessage(new Message(
                Request.LOGIN,
                null,
                Arrays.asList(
                        new KeyValue("username", username),
                        new KeyValue("password", password)
                ),
                ""
        ).toJson(), hostname, portnum));

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

    @Override
    public boolean isLoggedIn () {
        return sessionId != null;
    }
}
