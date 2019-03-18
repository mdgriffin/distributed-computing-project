package com.mdgriffin.distributedcomputingproject.client;

import java.io.IOException;

public interface ClientHandler {

    void login () throws IOException;

    void upload(String path, String filename)  throws IOException;

    void download(String destinationPath, String filename, String newFileName)  throws IOException;

    void download(String destinationPath, String filename)  throws IOException;

    void list ()  throws IOException;

    boolean isLoggedIn ();
}
