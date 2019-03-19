package com.mdgriffin.distributedcomputingproject.client;

import com.mdgriffin.distributedcomputingproject.common.Message;

import java.io.IOException;

public interface ClientHandler {

    Message login (String username, String password) throws IOException;

    void upload(String path, String filename)  throws IOException;

    void download(String destinationPath, String filename, String newFileName)  throws IOException;

    void download(String destinationPath, String filename)  throws IOException;

    void list ()  throws IOException;

    void setSessionId(String sessionId);

    boolean isLoggedIn ();
}
