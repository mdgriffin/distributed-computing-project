package com.mdgriffin.distributedcomputingproject.client;

import com.mdgriffin.distributedcomputingproject.common.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.file.AccessDeniedException;
import java.util.*;

public class SocketClientHandler implements ClientHandler {

    private String hostname;
    private int portnum;
    private String sessionId;
    private SocketHelper socketHelper;

    public SocketClientHandler(String hostname, int portNum) throws SocketException {
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

    public static class DTLSClient implements ClientHandler {

        private String sessionId;
        private DTLSSocket dtlsSocket;

        public DTLSClient (String serverHostname, int serverPortnum) throws Exception {
            DatagramSocket socket = new DatagramSocket(7777);
            InetSocketAddress serverSocketAddress = new InetSocketAddress("localhost",  9090);
            SocketHelper socketHelper = new SocketHelper(socket);

            socketHelper.send(new DatagramMessage("HELLO", serverHostname, serverPortnum));
            DatagramMessage serverInitResponse = socketHelper.receive();

            if (serverInitResponse.getMessage().charAt(0) == 'R') {
                dtlsSocket = new DTLSSocket(socket, serverSocketAddress, "Client");
            } else {
                throw new SocketException("Server Declined Hello");
            }
        }

        @Override
        public void download (String destinationPath, String filename, String newFileName) throws Exception {
            if (!isLoggedIn()) {
                throw new AccessDeniedException("Must be logged in to download files");
            }

            FileSystem fs = new FileSystemImpl(destinationPath);

            dtlsSocket.send(new Message(
                    Request.DOWNLOAD,
                    null,
                    Arrays.asList(
                            new KeyValue("session_id", sessionId),
                            new KeyValue("filename", filename)
                    ),
                    null
            ));

            Message serverMessage = dtlsSocket.receive();
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
        public void download(String destinationPath, String filename) throws Exception {
            download(destinationPath, filename, filename);
        }

        @Override
        public boolean upload (String path, String filename) throws Exception {
            if (!isLoggedIn()) {
                throw new AccessDeniedException("Must be logged in to upload files");
            }

            FileSystem fs = new FileSystemImpl("");
            String fileContents = Base64.getEncoder().encodeToString(fs.readFile(path));

            dtlsSocket.send(new Message(
                    Request.UPLOAD,
                    null,
                    Arrays.asList(
                            new KeyValue("session_id", sessionId),
                            new KeyValue("filename", filename)
                    ),
                    fileContents
            ));

            Message serverMessage = dtlsSocket.receive();
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
        public List<FileDescription> list () throws Exception {
            if (!isLoggedIn()) {
                throw new AccessDeniedException("Must be logged in!");
            }

            dtlsSocket.send(new Message(
                    Request.LIST,
                    null,
                    Arrays.asList(
                            new KeyValue("session_id", sessionId)
                    ),
                    ""
            ));

            //Message serverMessage = Message.fromJson(socketHelper.receive().getMessage());
            Message serverMessage = dtlsSocket.receive();
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
        public boolean logoff() throws Exception {
            if (isLoggedIn()) {
                try {
                    dtlsSocket.send(new Message(
                            Request.LOGOFF,
                            null,
                            Arrays.asList(
                                    new KeyValue("session_id", sessionId)
                            ),
                            ""
                    ));

                    Message serverMessage = dtlsSocket.receive();

                    if (serverMessage.getResponse().equals(Response.SUCCESS)) {
                        return true;
                    }
                } catch (IOException exc) {
                }
            }

            return false;
        }

        @Override
        public Message login (String username, String password) throws Exception {
            dtlsSocket.send(new Message(
                    Request.LOGIN,
                    null,
                    Arrays.asList(
                            new KeyValue("username", username),
                            new KeyValue("password", password)
                    ),
                    ""
            ));

            Message serverMessage = dtlsSocket.receive();

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
}
