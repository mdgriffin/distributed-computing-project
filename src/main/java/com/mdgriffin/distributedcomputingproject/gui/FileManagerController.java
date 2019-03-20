package com.mdgriffin.distributedcomputingproject.gui;

import com.mdgriffin.distributedcomputingproject.client.ClientHandler;
import com.mdgriffin.distributedcomputingproject.common.FileDescription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Callback;

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

    @FXML private void initialize() {
        addButtonToTable();
    }

    @FXML
    public void onBtnUploadClick (ActionEvent event) {
        System.out.println("On button click");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

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

    /**
     * Adapted from: https://riptutorial.com/javafx/example/27946/add-button-to-tableview
     * Author: riptutorial.com
     * Date: NA
     * Date Accessed: 20/03/2019
     */

    private void addButtonToTable() {
        TableColumn<FileDescription, Void> colBtn = new TableColumn("");

        Callback<TableColumn<FileDescription, Void>, TableCell<FileDescription, Void>> cellFactory = new Callback<TableColumn<FileDescription, Void>, TableCell<FileDescription, Void>>() {
            @Override
            public TableCell<FileDescription, Void> call(final TableColumn<FileDescription, Void> param) {
                final TableCell<FileDescription, Void> cell = new TableCell<FileDescription, Void>() {

                    private final Button btn = new Button("Download");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            FileDescription data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);
                            // TODO: Implement Download
                            // 1. Open file picker with destination to download
                            // 2. Check that filename contains extension
                            // Call download on client handler

                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setTitle("Select File");

                            File fileLocation = fileChooser.showSaveDialog(context.getStage());

                            if (fileLocation != null) {
                                System.out.println("Filename:" + fileLocation.getName());
                                System.out.println("Location" + fileLocation.getPath());
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        tblFileList.getColumns().add(colBtn);
    }
}
