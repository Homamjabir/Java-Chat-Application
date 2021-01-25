package server;

import java.io.*;
import java.net.Socket;

public class ChatClient implements Runnable {

    PrintStream outdata;
    BufferedReader indata;
    private ChatRoom chatRoom;
    private String username;
    private Boolean inLobby = false;
    private Boolean isLoggedIn = false;
    private final MessageHandler messageHandler;
    public Socket socket;

    /**
     * Constructor.
     */
    ChatClient(Socket socket, Lobby lobby, ClientData clientData) {
        this.socket = socket;

        this.messageHandler = new MessageHandler(this, lobby, clientData);

        try {
            InputStream input = socket.getInputStream();
            this.indata = new BufferedReader(new InputStreamReader(input));
            this.outdata = new PrintStream(socket.getOutputStream());

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Returns the username and password from the client.
     * @return Username and password in the form of a string array.
     * @throws IOException
     */
    private String[] getUserData() throws IOException
    {
        String[] arr = new String[2];
        this.outdata.println("Enter username: ");
        arr[0] = this.indata.readLine();
        this.outdata.println("Enter password: ");
        arr[1] = this.indata.readLine();
        return arr;
    }

    public void run() {
        try {
            this.outdata.println("You have now entered the server!");
            while (!this.isLoggedIn)
            {
                this.outdata.println("Enter 1 to register and 2 to login");
                String[] userData;
                switch (this.indata.readLine())
                {
                    case "1" -> {
                        userData = getUserData();
                        if (messageHandler.registerRequest(userData[0], userData[1]))
                            this.isLoggedIn = true;
                    }
                    case "2" -> {
                        userData = getUserData();
                        if (messageHandler.loginRequest(userData[0], userData[1]))
                            this.isLoggedIn = true;
                    }
                }
            }

            this.outdata.println("Enter your display name");
            this.username = this.indata.readLine();
            this.outdata.println("Welcome " + this.username + "!");

            this.outdata.println("To get a list of the available commands, please write \"-h\"");
            String message;

            while (true) {
                message = this.indata.readLine();
                messageHandler.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setInLobby(Boolean inLobby) {
        this.inLobby = inLobby;
    }

    boolean getInLobby() {
        return this.inLobby;
    }

    String getUsername() {
        return this.username;
    }

    void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    ChatRoom getChatRoom() {
        return this.chatRoom;
    }

    /*void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    Boolean getIsLoggedIn() {
        return this.isLoggedIn;
    }*/

    void sendMessage(String message) {
        this.outdata.println(message);
    }
}
