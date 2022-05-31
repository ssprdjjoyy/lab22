package com.lab22.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyClient {
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8919;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean isAuthorized = false;
    private List<String> messagesQueue = new ArrayList<>();
    private String nickName;
    private boolean isConnected = false;
    private LoginCallback loginCallback = null;
    private ChatCallback chatCallback = null;

    public interface LoginCallback{
        void callback();
    }

    public interface ChatCallback{
        void callback();
    }

    public MyClient() {
        try {
            openConnection();
            isConnected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openConnection() throws IOException {
        socket = new Socket(SERVER_ADDR, SERVER_PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        setAuthorized(false);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) { // ожидание авторизации
                        String strFromServer = in.readUTF();
                        if (strFromServer.startsWith("/authok")) {
                            String[] parts = strFromServer.split("\\s");
                            nickName = parts[1];
                            setAuthorized(true);
                            if (loginCallback != null) {
                                loginCallback.callback();
                                loginCallback = null;
                            }
                            break;
                        }
                        messagesQueue.add(strFromServer);
                    }
                    while (true) { // чтение с сервера
                        try {
                            String strFromServer = in.readUTF();
                            if (strFromServer.equalsIgnoreCase("/end")) {
                                break;
                            }
                            messagesQueue.add(strFromServer);
                            if (chatCallback != null) {
                                chatCallback.callback();
                            }
                        }
                        catch (EOFException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }
    public void closeConnection() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }
    public void setAuthorized(boolean value) {
        isAuthorized = value;
    }
    public boolean getAuthorized() {
        return isAuthorized;
    }
    public List<String> getQueuedMessages() {
        List<String> result = new ArrayList(messagesQueue);
        messagesQueue.clear();
        return result;
    }

    public String getNickName() {
        return nickName;
    }

    public boolean isConnected() {
        return isConnected;
    }
    public void setLoginCallback(LoginCallback callback) {
        loginCallback = callback;
    }
    public void setChatCallback(ChatCallback callback) {
        chatCallback = callback;
    }
}