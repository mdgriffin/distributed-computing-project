package com.mdgriffin.distributedcomputingproject.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FTPApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ftp-app.fxml"));
        Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().add(getClass().getResource("/css/ftp-app.css").toString());

        stage.setTitle("FTP Express");
        stage.setScene(scene);
        stage.show();
    }

}
