package com.mdgriffin.distributedcomputingproject.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginController {

    @FXML private Label lblUsername;
    private FTPApp context;

    public LoginController() {
    }

    public void setContext (FTPApp context) {
        this.context = context;
    }

    @FXML
    private void onBtnLoginClick (ActionEvent event) {
        lblUsername.setText("A new label value");

        if (context != null) {
            context.changeToFileManager();
        }
    }

}
