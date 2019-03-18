package com.mdgriffin.distributedcomputingproject.client;

import java.io.*;

public class Client {

    private static final int SERVER_PORT_NUM = 9090;
    private static final String SERVER_HOSTNAME = "localhost";
    private static final String USERNAME = "jdoe";
    private static final String PASSWORD = "password123";
    private static final String ROOT_DIRECTORY = "/DC_Temp/DC_Client/";

    public static void main(String[] args) {
        Client client = new Client();
    }

    public Client () {
        try {
            ClientHandler clientHandler = new ClientHandlerImpl(USERNAME, PASSWORD, SERVER_HOSTNAME, SERVER_PORT_NUM);
            clientHandler.login();
            clientHandler.list();
            clientHandler.upload(ROOT_DIRECTORY, "user_upload_01.txt");
            clientHandler.download( ROOT_DIRECTORY,"user_upload_01.txt", "user_download_02.txt");
        } catch (IOException exc) {
            System.out.println(exc);
        }
    }

}
