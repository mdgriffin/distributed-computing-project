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
import java.nio.file.AccessDeniedException;

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
    public void onBtnLogoffClick () {
        boolean loggedOff = false;

        try {
            loggedOff = clientHandler.logoff();
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }

        if (loggedOff) {
            context.changeToLogin();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to logoff");
            alert.showAndWait();
        }
    }

    @FXML
    public void onBtnUploadClick (ActionEvent event) {
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
            } catch (AccessDeniedException exc) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You do not have permission to upload this file");
                alert.showAndWait();
            } catch (Exception exc) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to upload file");
                alert.showAndWait();
            }
        }
    }

    public void loadFileList () {
        if (clientHandler != null) {
            try {
                files = FXCollections.observableArrayList(clientHandler.list());
                tblFileList.setItems(files);
            } catch (Exception exc) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to retrieve file listing");
                alert.showAndWait();
            }
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
                            FileDescription fileDescription = getTableView().getItems().get(getIndex());
                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setTitle("Select File");

                            File fileLocation = fileChooser.showSaveDialog(context.getStage());

                            if (fileLocation != null) {
                                String newFilename = fileLocation.getName();
                                String downloadPath = fileLocation.getPath().substring(0, fileLocation.getPath().lastIndexOf(File.separator)) + "/";

                                try {
                                    clientHandler.download(downloadPath, fileDescription.getFilename(), newFilename);
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "File Downloaded Successfully");
                                    alert.showAndWait();
                                } catch (Exception exc) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to download file");
                                    alert.showAndWait();
                                }
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
