package com.mdgriffin.distributedcomputingproject.gui;

import com.mdgriffin.distributedcomputingproject.client.ClientHandler;
import com.mdgriffin.distributedcomputingproject.common.FileDescription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class FileManagerController {

    @FXML
    private TableView<FileDescription> tblFileList;
    private FTPApp context;
    private ObservableList<FileDescription> files;
    private ClientHandler clientHandler;

    public FileManagerController () {}

    public void setContext(FTPApp context) {
        this.context = context;
        this.clientHandler = context.getClientHandler();
    }

    /*
    @FXML private void initialize() {
    }
    */

    @FXML
    public void onBtnUploadClick (ActionEvent event) {
        System.out.println("On button click");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(context.getStage());

        if (selectedFile != null) {
            try {
                clientHandler.upload(selectedFile.getPath(), selectedFile.getName());
                loadFileList();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "File uploaded successfully");
                alert.showAndWait();
                System.out.println("Path: " + selectedFile.getPath());
            } catch (IOException exc) {
                System.out.println("Exception: " + exc.getMessage());
            }
        }
    }

    public void loadFileList () {
        if (clientHandler != null) {
            try {
                files = FXCollections.observableArrayList(clientHandler.list());
                tblFileList.setItems(files);
            } catch (IOException exc) {
                System.out.println(exc.getMessage());
            }
        } else {
            System.out.println("Client Handler Not Set!");
        }
    }
}
