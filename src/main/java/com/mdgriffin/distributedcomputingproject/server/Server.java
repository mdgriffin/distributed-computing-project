package com.mdgriffin.distributedcomputingproject.server;

import com.mdgriffin.distributedcomputingproject.common.*;

import javax.security.auth.login.AccountNotFoundException;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    private static final int PORT_NUM = 9090;
    private DatagramSocket datagramSocket;
    private SocketHelper socketHelper;
    private Authentication authentication;
    private Map<String, Session> sessions;
    private static final String ROOT_DIRECTORY = "/DC_TEST/";


    public static void main(String[] args) {
        Server server = new Server();
    }

    public Server () {
        System.out.println("Server Ready for connections");
        authentication = new ListAuthentication();
        sessions = new HashMap<>();
        listen();
    }

    private void listen () {
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
                    case LIST:
                        handleList(receivedMessage, message);
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

    private void handleList (DatagramMessage datagramMessage, Message message) throws IOException {
        try {
            String username = message.getHeaders().stream().filter(name -> name.getKey().equals("username")).findFirst().orElseThrow(() -> new InvalidParameterException()).getValue();
            String sessionId = message.getHeaders().stream().filter(name -> name.getKey().equals("session_id")).findFirst().orElseThrow(() -> new InvalidParameterException()).getValue();

            if (!authentication.hasActiveSession(username, sessionId)) {
                throw new InvalidParameterException();
            }

            FileSystem fs = new FileSystemImpl(ROOT_DIRECTORY + username);
            List<String> userFiles = fs.listDirectory("");

            socketHelper.send(new DatagramMessage(new Message(
                    message.getRequest(),
                    Response.SUCCESS,
                    null,
                    userFiles.toString()
            ).toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
        } catch (InvalidParameterException exc) {
            socketHelper.send(new DatagramMessage(new Message(
                    message.getRequest(),
                    Response.ERROR,
                    Arrays.asList(new KeyValue("message", "Must supply valid username and session ID")),
                    ""
            ).toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
        }
    }

    private void handleLogin (DatagramMessage datagramMessage, Message message) throws IOException {
        try {
            String username = message.getHeaders().stream().filter(name -> name.getKey().equals("username")).findFirst().orElseThrow(() -> new InvalidParameterException()).getValue();
            String password = message.getHeaders().stream().filter(name -> name.getKey().equals("password")).findFirst().orElseThrow(() -> new InvalidParameterException()).getValue();
            Session session  = authentication.login(username, password);

            socketHelper.send(new DatagramMessage(new Message(
                message.getRequest(),
                Response.SUCCESS,
                Arrays.asList(
                    new KeyValue("message", "You have successfully logged in"),
                    new KeyValue("session_id", session.getId())
                ),
                ""
            ).toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
        } catch (InvalidParameterException exc) {
            socketHelper.send(new DatagramMessage(new Message(
                message.getRequest(),
                Response.ERROR,
                Arrays.asList(new KeyValue("message", "Must supply username and password to login")),
                ""
            ).toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
        } catch (AccountNotFoundException exc) {
            socketHelper.send(new DatagramMessage(new Message(
                    message.getRequest(),
                    Response.DENIED,
                    Arrays.asList(new KeyValue("message", "Username, Password combination invalid")),
                    ""
            ).toJson(), datagramMessage.getAddress(), datagramMessage.getPortNum()));
        }
    }

}
