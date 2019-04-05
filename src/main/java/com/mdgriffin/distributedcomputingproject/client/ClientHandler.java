package com.mdgriffin.distributedcomputingproject.client;

import com.mdgriffin.distributedcomputingproject.common.FileDescription;
import com.mdgriffin.distributedcomputingproject.common.Message;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ClientHandler {

    Message login (String username, String password) throws Exception;

    boolean upload(String path, String filename)  throws Exception;

    void download(String destinationPath, String filename, String newFileName)  throws Exception;

    void download(String destinationPath, String filename)  throws Exception;

    List<FileDescription> list ()  throws Exception;

    void setSessionId(String sessionId);

    boolean logoff () throws Exception;

    boolean isLoggedIn ();
}
