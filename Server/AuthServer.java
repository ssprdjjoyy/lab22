package com.lab22.Server;

public interface AuthService {
    void start();
    String getNickByLoginPass(String login, String pass);
    int getUserIDByLoginPass(String login, String pass);
    boolean nicknameIsUsed(String nickname);
    String changeNickname(int userID, String newNickname);
    void addUser(String login, String pass, String nick);
    void deleteUser(String nick);
    void stop();
}