package com.lab22.client;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.List;

public class ChatViewController {
    @FXML
    private TextArea txtChat;
    @FXML
    private TextField txtMsg;

    @FXML
    public void initialize() {
        if (ClientApplication.myClient != null && ClientApplication.myClient.isConnected()) {
            ClientApplication.myClient.setChatCallback(new MyClient.ChatCallback() {
                @Override
                public void callback() {
                    if (ClientApplication.myClient != null && ClientApplication.myClient.isConnected() && ClientApplication.myClient.getAuthorized()) {
                        onReceiveMessage(ClientApplication.myClient.getQueuedMessages());
                    }
                }
            });
        }
    }

    protected void onReceiveMessage(List<String> messages) {
        for (String msg : messages) {
            txtChat.appendText(msg + "\n");
        }
    }

    protected void sendMessage() {
        if (ClientApplication.myClient != null && ClientApplication.myClient.isConnected() && ClientApplication.myClient.getAuthorized()) {
            try {
                ClientApplication.myClient.sendMessage(txtMsg.getText());
            }
            catch (IOException e) {
                txtChat.appendText("Error: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
        txtMsg.setText("");
    }
    @FXML
    protected void onBtnSendClicked() {
        sendMessage();
    }
    @FXML
    protected void onTxtMsgKeyDown(KeyEvent key) {
        if (key.getCode() == KeyCode.ENTER)
            sendMessage();
    }
}