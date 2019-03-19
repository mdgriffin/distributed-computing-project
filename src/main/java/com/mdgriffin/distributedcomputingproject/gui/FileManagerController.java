package com.mdgriffin.distributedcomputingproject.gui;

import com.mdgriffin.distributedcomputingproject.common.FileDescription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class FileManagerController {

    @FXML
    private TableView<FileDescription> tblFileList;
    private FTPApp context;

    public ObservableList<FileDescription> files;

    public FileManagerController () {
        files = FXCollections.observableArrayList(
                new FileDescription("file1.txt", 34),
                new FileDescription("file5.txt", 135),
                new FileDescription("file3.txt", 467),
                new FileDescription("file4.txt", 454),
                new FileDescription("file2.txt", 256)
        );
    }

    public void setContext(FTPApp context) {
        this.context = context;
    }

    @FXML private void initialize() {
        tblFileList.setItems(files);
    }
}
