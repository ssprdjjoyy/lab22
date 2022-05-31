package com.lab22.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {
    static public MyClient myClient = null;
    static public ClientApplication app = null;
    static private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        app = this;
        this.stage = stage;
        myClient = new MyClient();

        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("auth-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Chat Client App");
        stage.setScene(scene);
        stage.show();
    }

    public void openChat() {
        try {
            Parent root = FXMLLoader.load(ClientApplication.class.getResource("chat-view.fxml"));
            stage.getScene().setRoot(root);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}