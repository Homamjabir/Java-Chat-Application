/**
 * Handles the registration/login, storage of user credentials and logout
 * of all the server clients.
 */

package server.lobbys;

import java.util.ArrayList;

public class ClientData
{

    /**
     * Private class for the user credentials.
     */
    private static class UserCredentials {
        String username;
        String password;
        ChatClient chatClient;
    }

    ArrayList<UserCredentials> userCredentials;
    ArrayList<UserCredentials> loggedInClients;

    /**
     * Constructor
     */
    ClientData() {
        this.userCredentials = new ArrayList<>();
        this.loggedInClients = new ArrayList<>();
    }

    /**
     * Checks if the username already exists.
     * @param username The username to compare for.
     * @return Returns true if the username exists and false otherwise.
     */
    Boolean usernameExist(String username) {
        for(UserCredentials uc : userCredentials)
            if(uc.username.equals(username))
                return true;
        return false;
    }

    /**
     * Checks if the received username and password are valid.
     * @param username The username to look for.
     * @param password The password to look for.
     * @return Returns true if the supplied credentials are valid
     * and false otherwise.
     */
    Boolean isValidLoginCredential(ChatClient chatClient, String username, String password) {
        for(UserCredentials uc : userCredentials)
            if(uc.username.equals(username) && uc.password.equals(password)) {
                if(uc.chatClient.getIsLoggedIn())
                    return false;
                uc.chatClient = chatClient;
                uc.chatClient.setIsLoggedIn(true);
                return true;
            }
        return false;
    }

    /**
     * Adds the supplied credentials to the list.
     * @param chatClient The chat client which signed in.
     * @param username The username to enter.
     * @param password The password to enter.
     */
    void addLoginCredentials(ChatClient chatClient, String username, String password) {
        UserCredentials uc = new UserCredentials();
        uc.username = username;
        uc.password = password;
        uc.chatClient = chatClient;
        uc.chatClient.setIsLoggedIn(true);
        this.userCredentials.add(uc);
    }

    /**
     * Logs out the specified client from the chatroom
     * @param chatClient
     * @return
     */
    public Boolean logoutChatClient(ChatClient chatClient) {
        for(UserCredentials uc : userCredentials) {
            if(uc.chatClient.equals(chatClient))
                chatClient.setIsLoggedIn(false);
                uc.chatClient = null;
                return true;
        }
        return false;
    }



}
