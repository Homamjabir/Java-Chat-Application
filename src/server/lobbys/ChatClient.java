/**
 * The following class creates a new chat client object
 * which handles sending both initials messages like asking for a
 * registraion or login and the latter stage of sending
 *
 */

package server.lobbys;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
     * Constructor
     */
    ChatClient(Socket socket, MessageHandler messageHandler) {
        this.socket = socket;
        this.messageHandler = messageHandler;

        try {
            InputStream input = socket.getInputStream();
            this.indata = new BufferedReader(new InputStreamReader(input));
            this.outdata = new PrintStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets called when the thread starts and handles the data that it sent between the server and client
     */
    public void run() {
        try {
            initialCommunication();
            messageCommunication();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialCommunication() throws IOException
    {
        String response;

        this.outdata.println("You have now entered the server!");
        while (!this.isLoggedIn)
        {
            this.outdata.println("Enter 1 to register and 2 to login");
            String[] userData;
            switch (this.indata.readLine())
            {
                case "1" -> {
                    userData = getUserData();
                    response = messageHandler.registerRequest(this, userData[0], userData[1]);
                    this.outdata.println(response);
                }
                case "2" -> {
                    userData = getUserData();
                    response = messageHandler.loginRequest(this, userData[0], userData[1]);
                    this.outdata.println(response);
                }
                default -> {
                    this.outdata.println("Incorrect input, please follow the instructions bellow!");
                }
            }
        }

        this.outdata.println("Enter your display name");
        this.username = this.indata.readLine();
        this.outdata.println("Welcome " + this.username + "!");
        this.outdata.println("To get a list of the available commands, please write \"-h\"");
    }


    private void messageCommunication() throws IOException {
        String message;

        while (true) {
            message = this.indata.readLine();
            messageHandler.sendMessage(this, message);
        }
    }

    private String[] getUserData() throws IOException
    {
        String[] arr = new String[2];
        this.outdata.println("Enter username: ");
        arr[0] = this.indata.readLine();
        this.outdata.println("Enter password: ");
        arr[1] = this.indata.readLine();
        return arr;
    }


    public void setInLobby(Boolean inLobby) {
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

    void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    Boolean getIsLoggedIn() {
        return this.isLoggedIn;
    }

    void sendMessage(String message) {
        this.outdata.println(message);
    }

    /**
     * Converts a string to bytes and sends it to the client
     * @param fileContent File content that is sent.
     * @throws IOException
     */
    void sendFile(String fileContent) throws IOException {
        byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        sendMessage("-f123123 " + fileContentBytes.length);
        OutputStream outputStream = this.socket.getOutputStream();
        outputStream.write(fileContentBytes, 0, fileContentBytes.length);
        outputStream.flush();
    }



}
