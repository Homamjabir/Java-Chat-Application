package server;

import java.util.ArrayList;

public class ClientData {

    private static class UserCredentials {
        String username;
        String password;
        boolean isLoggedIn = false;
    }

    ArrayList<UserCredentials> userCredentials;
    ArrayList<UserCredentials> loggedInClients;

    ClientData() {
        this.userCredentials = new ArrayList<>();
        this.loggedInClients = new ArrayList<>();
    }

    Boolean usernameExist(String username) {
        for(UserCredentials uc : userCredentials)
            if(uc.username.equals(username))
                return true;
        return false;
    }

    Boolean isValidLoginCredential(String username, String password) {
        for(UserCredentials uc : userCredentials)
            if(uc.username.equals(username) && uc.password.equals(password)) {
                if(uc.isLoggedIn)
                    return false;
                return true;
            }
        return false;
    }

    void addLoginCredentials(String username, String password) {
        UserCredentials uc = new UserCredentials();
        uc.username = username;
        uc.password = password;
        uc.isLoggedIn = true;
        this.userCredentials.add(uc);
    }



}
