package com.mdgriffin.distributedcomputingproject.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FTPApp extends Application {

    private Scene loginScene;
    private Scene fileManagerScene;
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    // TODO: https://stackoverflow.com/questions/12804664/how-to-swap-screens-in-a-javafx-application-in-the-controller-class

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent loginParent = loginLoader.load();
        LoginController loginController = (LoginController) loginLoader.getController();
        loginController.setContext(this);
        loginScene = new Scene(loginParent, 500, 500);
        loginScene.getStylesheets().add(getClass().getResource("/css/ftp-app.css").toString());

        FXMLLoader fileManagerLoader = new FXMLLoader(getClass().getResource("/fxml/file-manager.fxml"));
        Parent fileManagerParent = fileManagerLoader.load();
        FileManagerController fileManagerController = (FileManagerController) fileManagerLoader.getController();
        fileManagerController.setContext(this);
        fileManagerScene = new Scene(fileManagerParent, 500, 500);
        fileManagerScene.getStylesheets().add(getClass().getResource("/css/ftp-app.css").toString());

        stage.setTitle("FTP Express");
        stage.setScene(loginScene);
        stage.show();
    }

    public void changeToLogin () {
        if (stage != null && loginScene != null) {
            stage.setScene(loginScene);
        }
    }

    public void changeToFileManager () {
        if (stage != null && fileManagerScene != null) {
            stage.setScene(fileManagerScene);
        }
    }

}
