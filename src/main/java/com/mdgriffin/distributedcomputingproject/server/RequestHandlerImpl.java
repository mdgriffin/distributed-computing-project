package com.mdgriffin.distributedcomputingproject.server;

import com.mdgriffin.distributedcomputingproject.common.*;

import javax.security.auth.login.AccountNotFoundException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

public class RequestHandlerImpl implements RequestHandler {

    private Authentication authentication;
    private static final String ROOT_DIRECTORY = "/DC_TEST/";

    public RequestHandlerImpl () {
        authentication = new ListAuthentication();
    }

    @Override
    public Message login(Message message) {
        try {
            String username = message.getHeaders().stream().filter(name -> name.getKey().equals("username")).findFirst().orElseThrow(() -> new InvalidParameterException()).getValue();
            String password = message.getHeaders().stream().filter(name -> name.getKey().equals("password")).findFirst().orElseThrow(() -> new InvalidParameterException()).getValue();
            Session session  = authentication.login(username, password);

            return new Message(
                    message.getRequest(),
                    Response.SUCCESS,
                    Arrays.asList(
                            new KeyValue("message", "You have successfully logged in"),
                            new KeyValue("session_id", session.getId())
                    ),
                    ""
            );
        } catch (InvalidParameterException exc) {
            return new Message(
                    message.getRequest(),
                    Response.ERROR,
                    Arrays.asList(new KeyValue("message", "Must supply username and password to login")),
                    ""
            );
        } catch (AccountNotFoundException exc) {
            return new Message(
                    message.getRequest(),
                    Response.DENIED,
                    Arrays.asList(new KeyValue("message", "Username, Password combination invalid")),
                    ""
            );
        }
    }

    @Override
    public Message list(Message message) {
        try {
            String username = message.getHeaders().stream().filter(name -> name.getKey().equals("username")).findFirst().orElseThrow(() -> new InvalidParameterException()).getValue();
            String sessionId = message.getHeaders().stream().filter(name -> name.getKey().equals("session_id")).findFirst().orElseThrow(() -> new InvalidParameterException()).getValue();

            if (!authentication.hasActiveSession(username, sessionId)) {
                throw new InvalidParameterException();
            }

            FileSystem fs = new FileSystemImpl(ROOT_DIRECTORY + username);
            List<String> userFiles = fs.listDirectory("");

            return new Message(
                    message.getRequest(),
                    Response.SUCCESS,
                    null,
                    userFiles.toString()
            );
        } catch (InvalidParameterException exc) {
            return new Message(
                    message.getRequest(),
                    Response.ERROR,
                    Arrays.asList(new KeyValue("message", "Must supply valid username and session ID")),
                    ""
            );
        }
    }

}