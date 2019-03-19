package com.mdgriffin.distributedcomputingproject.gui;

import com.mdgriffin.distributedcomputingproject.client.ClientHandler;
import com.mdgriffin.distributedcomputingproject.common.Message;
import com.mdgriffin.distributedcomputingproject.common.Response;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private Label lblLoginError;
    private FTPApp context;

    public LoginController() { }

    public void setContext (FTPApp context) {
        this.context = context;
    }

    @FXML
    private void onBtnLoginClick (ActionEvent event) {
        ClientHandler clientHandler = context.getClientHandler();
        String username  = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.length() == 0 || password.length() == 0) {
            lblLoginError.setText("Please provide a value for username and password");
        } else {
            try {
                Message loginMessage = clientHandler.login(username, password);

                if (loginMessage.getResponse().equals(Response.SUCCESS)) {
                    clientHandler.setSessionId(loginMessage.getHeaderValue("session_id"));
                    if (context != null) {
                        context.changeToFileManager();
                    }
                } else if (loginMessage.getResponse().equals(Response.DENIED)) {
                    lblLoginError.setText("Error Logging In");
                } else {
                    lblLoginError.setText("Error Logging In");
                }
            } catch (IOException exc) {
                lblLoginError.setText("Error Logging In");
            }
        }

    }

}
