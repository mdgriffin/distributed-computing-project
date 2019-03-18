package com.mdgriffin.distributedcomputingproject.client;

import java.io.*;

public class Client {

    private static final int SERVER_PORT_NUM = 9090;
    private static final String SERVER_HOSTNAME = "localhost";
    private static final String USERNAME = "jdoe";
    private static final String PASSWORD = "password123";
    private static final String ROOT_DIRECTORY = "/DC_Temp/DC_Client/";

    private ClientHandler clientHandler;

    public static void main(String[] args) {
        try {
            Client client = new Client(new ClientHandlerImpl(USERNAME, PASSWORD, SERVER_HOSTNAME, SERVER_PORT_NUM));
            client.start();
        } catch (IOException exc) {
            System.out.println("Issue connecting to client: " + exc.getMessage());
        }
    }

    public Client (ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void start() {
        try {
            clientHandler.login();
            clientHandler.list();
            clientHandler.upload(ROOT_DIRECTORY, "user_upload_01.txt");
            clientHandler.download( ROOT_DIRECTORY,"user_upload_01.txt", "user_download_02.txt");
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }
    }

}
