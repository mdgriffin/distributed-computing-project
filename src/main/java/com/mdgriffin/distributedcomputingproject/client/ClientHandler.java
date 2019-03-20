package com.mdgriffin.distributedcomputingproject.client;

import com.mdgriffin.distributedcomputingproject.common.FileDescription;
import com.mdgriffin.distributedcomputingproject.common.Message;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ClientHandler {

    Message login (String username, String password) throws IOException;

    boolean upload(String path, String filename)  throws IOException;

    void download(String destinationPath, String filename, String newFileName)  throws IOException;

    void download(String destinationPath, String filename)  throws IOException;

    List<FileDescription> list ()  throws IOException;

    void setSessionId(String sessionId);

    boolean isLoggedIn ();
}
