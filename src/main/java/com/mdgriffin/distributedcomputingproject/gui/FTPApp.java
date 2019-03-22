package com.mdgriffin.distributedcomputingproject.gui;

import com.mdgriffin.distributedcomputingproject.client.ClientHandler;
import com.mdgriffin.distributedcomputingproject.client.ClientHandlerImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

import java.io.IOException;

public class FTPApp extends Application {

    private Scene loginScene;
    private Scene fileManagerScene;
    private LoginController loginController;
    private FileManagerController fileManagerController;
    private Stage stage;
    private ClientHandler clientHandler;
    private static final int SERVER_PORT_NUM = 9090;
    private static final String SERVER_HOSTNAME = "localhost";

    public FTPApp () {
        try {
            clientHandler = new ClientHandlerImpl(SERVER_HOSTNAME, SERVER_PORT_NUM);
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent loginParent = loginLoader.load();
        loginController = (LoginController) loginLoader.getController();
        loginController.setContext(this);
        loginScene = new Scene(loginParent, 500, 500);
        loginScene.getStylesheets().add(getClass().getResource("/css/ftp-app.css").toString());

        FXMLLoader fileManagerLoader = new FXMLLoader(getClass().getResource("/fxml/file-manager.fxml"));
        Parent fileManagerParent = fileManagerLoader.load();
        fileManagerController = (FileManagerController) fileManagerLoader.getController();
        fileManagerController.setContext(this);
        fileManagerScene = new Scene(fileManagerParent, 500, 500);
        fileManagerScene.getStylesheets().add(getClass().getResource("/css/ftp-app.css").toString());

        //new JMetro(JMetro.Style.LIGHT).applyTheme(loginScene);
        //new JMetro(JMetro.Style.LIGHT).applyTheme(fileManagerScene);

        stage.setTitle("FTP Express");
        stage.setScene(loginScene);
        stage.show();
    }

    /*
    public void changeToLogin () {
        if (stage != null && loginScene != null) {
            stage.setScene(loginScene);
        }
    }
    */

    public void changeToFileManager () {
        if (stage != null && fileManagerScene != null) {
            stage.setScene(fileManagerScene);
            fileManagerController.loadFileList();
        }
    }

    public ClientHandler getClientHandler () {
        return this.clientHandler;
    }

    public Stage getStage () {
        return stage;
    }

}
