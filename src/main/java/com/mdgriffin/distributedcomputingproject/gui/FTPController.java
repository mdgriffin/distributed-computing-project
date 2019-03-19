package com.mdgriffin.distributedcomputingproject.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class FTPController {

    @FXML private Label lblUsername;
    @FXML private TableView<FTPFile> tblFileList;

    public ObservableList<FTPFile> files;

    public FTPController () {
        files = FXCollections.observableArrayList(
            new FTPFile("file1.txt", 34),
            new FTPFile("file5.txt", 135),
            new FTPFile("file3.txt", 467),
            new FTPFile("file4.txt", 454),
            new FTPFile("file2.txt", 256)
        );
    }

    @FXML private void initialize() {
        tblFileList.setItems(files);
    }

    public void onBtnLoginClick () {
        lblUsername.setText("A new label value");
    }

}
