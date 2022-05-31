package com.lab22.client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class AuthViewController {
    @FXML
    protected TextField txtName;
    @FXML
    protected TextField txtPassword;
    @FXML
    protected Label lblError;
    @FXML
    protected void onSubmit()
    {
        try {
            if (ClientApplication.myClient != null && ClientApplication.myClient.isConnected()) {
                ClientApplication.myClient.setLoginCallback(new MyClient.LoginCallback() {
                    @Override
                    public void callback() {
                        onLogin();
                    }
                });
                ClientApplication.myClient.sendMessage("/auth " + txtName.getText() + " " + txtPassword.getText());
            }
        }
        catch (IOException exception) {
            lblError.setText(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void onLogin() {
        if (ClientApplication.myClient != null && ClientApplication.myClient.isConnected() && ClientApplication.myClient.getAuthorized()) {
            ClientApplication.app.openChat();
        }
    }
    @FXML
    protected void onTxtLoginKeyDown(KeyEvent key) {
        if (key.getCode() == KeyCode.ENTER)
            this.txtPassword.requestFocus();
    }
    @FXML
    protected void onTxtPasswordKeyDown(KeyEvent key) {
        if (key.getCode() == KeyCode.ENTER)
            onSubmit();
    }
}