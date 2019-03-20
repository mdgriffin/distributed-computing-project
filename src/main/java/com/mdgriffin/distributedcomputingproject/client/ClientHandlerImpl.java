package com.mdgriffin.distributedcomputingproject.client;

import com.mdgriffin.distributedcomputingproject.common.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.file.AccessDeniedException;
import java.util.*;

public class ClientHandlerImpl implements ClientHandler {

    //private String username;
    //private String password;
    private String hostname;
    private int portnum;
    private String sessionId;
    private SocketHelper socketHelper;

    public ClientHandlerImpl(String hostname, int portNum) throws SocketException {
        //this.username = username;
        //this.password = password;
        this.hostname = hostname;
        this.portnum = portNum;

        DatagramSocket socket = new DatagramSocket();
        socketHelper = new SocketHelper(socket);
    }

    @Override
    public void download (String destinationPath, String filename, String newFileName) throws IOException {
        if (isLoggedIn()) {
            FileSystem fs = new FileSystemImpl(destinationPath);

            socketHelper.send(new DatagramMessage(new Message(
                    Request.DOWNLOAD,
                    null,
                    Arrays.asList(
                        // TODO: Retrieve username server side using session key
                        //new KeyValue("username", username),
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
                String message = serverResponse.getHeaderValue("message");
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
                            //new KeyValue("username", username),
                            new KeyValue("session_id", sessionId),
                            new KeyValue("filename", filename)
                    ),
                    fileContents
            ).toJson(), hostname, portnum));

            Message serverResponse = Message.fromJson(socketHelper.receive().getMessage());
            String message = serverResponse.getHeaderValue("message");

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
    public List<FileDescription> list () throws IOException {
        if (!isLoggedIn()) {
            throw new AccessDeniedException("Must be logged in!");
        }

        socketHelper.send(new DatagramMessage(new Message(
                Request.LIST,
                null,
                Arrays.asList(
                    new KeyValue("session_id", sessionId)
                ),
                ""
        ).toJson(), hostname, portnum));

        Message serverResponse = Message.fromJson(socketHelper.receive().getMessage());

        if (serverResponse.getResponse().equals(Response.SUCCESS)) {
            return CSVUtil.csvToFileList(serverResponse.getBody());
        } else {
            throw new IOException();
        }
    }

    @Override
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public Message login (String username, String password) throws IOException {
        socketHelper.send(new DatagramMessage(new Message(
                Request.LOGIN,
                null,
                Arrays.asList(
                        new KeyValue("username", username),
                        new KeyValue("password", password)
                ),
                ""
        ).toJson(), hostname, portnum));

        return Message.fromJson(socketHelper.receive().getMessage());
    }

    @Override
    public boolean isLoggedIn () {
        return sessionId != null;
    }
}
