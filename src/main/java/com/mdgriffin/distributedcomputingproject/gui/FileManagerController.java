package com.mdgriffin.distributedcomputingproject.gui;

import com.mdgriffin.distributedcomputingproject.client.ClientHandler;
import com.mdgriffin.distributedcomputingproject.common.FileDescription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.io.IOException;

public class FileManagerController {

    @FXML
    private TableView<FileDescription> tblFileList;
    private FTPApp context;
    private ObservableList<FileDescription> files;
    private ClientHandler clientHandler;

    public FileManagerController () {

        /*
        files = FXCollections.observableArrayList(
                new FileDescription("file1.txt", 34),
                new FileDescription("file5.txt", 135),
                new FileDescription("file3.txt", 467),
                new FileDescription("file4.txt", 454),
                new FileDescription("file2.txt", 256)
        );
        */
    }

    public void setContext(FTPApp context) {
        this.context = context;
        this.clientHandler = context.getClientHandler();
    }

    /*
    @FXML private void initialize() {
    }
    */

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
