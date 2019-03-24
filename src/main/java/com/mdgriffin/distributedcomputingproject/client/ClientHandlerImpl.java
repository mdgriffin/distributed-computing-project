package com.mdgriffin.distributedcomputingproject.client;

import com.mdgriffin.distributedcomputingproject.common.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.file.AccessDeniedException;
import java.util.*;

public class ClientHandlerImpl implements ClientHandler {

    private String hostname;
    private int portnum;
    private String sessionId;
    private SocketHelper socketHelper;

    public ClientHandlerImpl(String hostname, int portNum) throws SocketException {
        this.hostname = hostname;
        this.portnum = portNum;

        DatagramSocket socket = new DatagramSocket();
        socketHelper = new SocketHelper(socket);
    }

    @Override
    public void download (String destinationPath, String filename, String newFileName) throws IOException {
        if (!isLoggedIn()) {
            throw new AccessDeniedException("Must be logged in to download files");
        }

        FileSystem fs = new FileSystemImpl(destinationPath);

        socketHelper.send(new DatagramMessage(new Message(
                Request.DOWNLOAD,
                null,
                Arrays.asList(
                    new KeyValue("session_id", sessionId),
                    new KeyValue("filename", filename)
                ),
                null
        ).toJson(), hostname, portnum));

        Message serverMessage = Message.fromJson(socketHelper.receive().getMessage());
        Response serverResponse = serverMessage.getResponse();
        String message = serverMessage.getHeaderValue("message");

        if (serverResponse.equals(Response.SUCCESS)) {
            byte[] fileContents = Base64.getDecoder().decode(serverMessage.getBody().getBytes());
            fs.saveFile(newFileName, fileContents);
        } else if (serverResponse.equals(Response.DENIED)) {
            throw new AccessDeniedException(message);
        } else {
            throw new IOException(message);
        }
    }

    @Override
    public void download(String destinationPath, String filename) throws IOException {
        download(destinationPath, filename, filename);
    }

    @Override
    public boolean upload (String path, String filename) throws IOException {
        if (!isLoggedIn()) {
            throw new AccessDeniedException("Must be logged in to upload files");
        }

        FileSystem fs = new FileSystemImpl("");
        String fileContents = Base64.getEncoder().encodeToString(fs.readFile(path));

        socketHelper.send(new DatagramMessage(new Message(
            Request.UPLOAD,
            null,
            Arrays.asList(
                new KeyValue("session_id", sessionId),
                new KeyValue("filename", filename)
            ),
            fileContents
        ).toJson(), hostname, portnum));

        Message serverMessage = Message.fromJson(socketHelper.receive().getMessage());
        String message = serverMessage.getHeaderValue("message");
        Response serverResponse = serverMessage.getResponse();

        if (serverResponse.equals(Response.DENIED)) {
            throw new AccessDeniedException(message);
        } else if (!serverResponse.equals(Response.SUCCESS)) {
            throw new IOException(message);
        }

        return true;
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

        Message serverMessage = Message.fromJson(socketHelper.receive().getMessage());
        Response serverResponse = serverMessage.getResponse();

        if (serverResponse.equals(Response.DENIED)) {
            throw new AccessDeniedException("Must be logged in to access server");
        } else if (!serverResponse.equals(Response.SUCCESS)) {
            throw new IOException();
        }

        return CSVUtil.csvToFileList(serverMessage.getBody());
    }

    @Override
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean logoff() {
        if (isLoggedIn()) {
            try {
                socketHelper.send(new DatagramMessage(new Message(
                    Request.LOGOFF,
                    null,
                    Arrays.asList(
                        new KeyValue("session_id", sessionId)
                    ),
                    ""
                ).toJson(), hostname, portnum));

                Message serverMessage = Message.fromJson(socketHelper.receive().getMessage());

                if (serverMessage.getResponse().equals(Response.SUCCESS)) {
                    return true;
                }
            } catch (IOException exc) {
            }
        }

        return false;
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

        Message serverMessage = Message.fromJson(socketHelper.receive().getMessage());

        if (serverMessage.getResponse().equals(Response.SUCCESS)) {
            setSessionId(serverMessage.getHeaderValue("session_id"));
        }

        return serverMessage;
    }

    @Override
    public boolean isLoggedIn () {
        return sessionId != null;
    }
}
