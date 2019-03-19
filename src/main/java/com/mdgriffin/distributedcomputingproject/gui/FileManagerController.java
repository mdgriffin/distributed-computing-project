package com.mdgriffin.distributedcomputingproject.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class FileManagerController {

    @FXML
    private TableView<FTPFile> tblFileList;
    private FTPApp context;

    public ObservableList<FTPFile> files;

    public FileManagerController () {
        files = FXCollections.observableArrayList(
                new FTPFile("file1.txt", 34),
                new FTPFile("file5.txt", 135),
                new FTPFile("file3.txt", 467),
                new FTPFile("file4.txt", 454),
                new FTPFile("file2.txt", 256)
        );
    }

    public void setContext(FTPApp context) {
        this.context = context;
    }

    @FXML private void initialize() {
        tblFileList.setItems(files);
    }
}
