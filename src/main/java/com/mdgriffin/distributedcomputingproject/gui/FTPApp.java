package com.mdgriffin.distributedcomputingproject.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FTPApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ftp-app.fxml"));

        FTPController controllerRef = loader.getController();
        VBox vbox = loader.<VBox>load();

        Scene scene = new Scene(vbox);
        System.out.println("Loading File: " + getClass().getResource("/css/ftp-app.css").toString());
        scene.getStylesheets().add(getClass().getResource("/css/ftp-app.css").toString());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
