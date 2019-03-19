package com.mdgriffin.distributedcomputingproject.server;

import com.mdgriffin.distributedcomputingproject.common.*;

import javax.security.auth.login.AccountNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class RequestHandlerImpl implements RequestHandler {

    private Authentication authentication;
    private static final String ROOT_DIRECTORY = "/DC_Temp/DC_Server/";

    public RequestHandlerImpl () {
        authentication = new ListAuthentication();
    }

    @Override
    public Message login(Message message) {
        try {
            String username = message.getHeaderValue("username");
            String password = message.getHeaderValue("password");
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
            String username = message.getHeaderValue("username");
            String sessionId = message.getHeaderValue("session_id");

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

    @Override
    public Message upload (Message message) {
        try {
            String username = message.getHeaderValue("username");
            String sessionId = message.getHeaderValue("session_id");
            String filename = message.getHeaderValue("filename");
            FileSystem fs = new FileSystemImpl(ROOT_DIRECTORY);

            if (!authentication.hasActiveSession(username, sessionId)) {
                throw new InvalidParameterException();
            }

            if (!fs.directoryExists(username)) {
                fs.createDirectory(username);
            }

            fs.saveFile(username + "/" + filename, Base64.getDecoder().decode(message.getBody()));

            return new Message(
                    message.getRequest(),
                    Response.SUCCESS,
                    Arrays.asList(new KeyValue("message", "Successfully uploaded file")),
                    ""
            );
        } catch (InvalidParameterException exc) {
            return new Message(
                    message.getRequest(),
                    Response.ERROR,
                    Arrays.asList(new KeyValue("message", "Must supply valid username and session ID")),
                    ""
            );
        } catch (IOException exc) {
            return new Message(
                    message.getRequest(),
                    Response.ERROR,
                    Arrays.asList(new KeyValue("message", "Error Saving File")),
                    ""
            );
        }
    }

    @Override
    public Message download(Message message) {
        try {
            String username = message.getHeaderValue("username");
            String sessionId = message.getHeaderValue("session_id");
            String filename = message.getHeaderValue("filename");
            FileSystem fs = new FileSystemImpl(ROOT_DIRECTORY + username + "/");

            if (!authentication.hasActiveSession(username, sessionId)) {
                throw new InvalidParameterException();
            }

            if (!fs.fileExists(filename)) {
                throw new FileNotFoundException();
            }

            // Encode Base64
            String fileContents = Base64.getEncoder().encodeToString(fs.readFile(filename));

            return new Message(
                    message.getRequest(),
                    Response.SUCCESS,
                    Arrays.asList(
                        new KeyValue("filename", filename)
                    ),
                    fileContents
            );
        } catch (InvalidParameterException exc) {
            // TODO: Add builder to simplify message creation
            return new Message(
                message.getRequest(),
                // TODO: Change to DENIED
                Response.ERROR,
                Arrays.asList(new KeyValue("message", "Must supply valid username and session ID")),
                ""
            );
        } catch (FileNotFoundException exc) {
            return new Message(
                    message.getRequest(),
                    Response.ERROR,
                    Arrays.asList(new KeyValue("message", "File Not Found!")),
                    ""
            );
        } catch (IOException exc) {
            return new Message(
                    message.getRequest(),
                    Response.ERROR,
                    Arrays.asList(new KeyValue("message", "Error Retrieving File")),
                    ""
            );
        }
    }

}