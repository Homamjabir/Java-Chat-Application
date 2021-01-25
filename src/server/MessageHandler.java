package server;

import java.io.IOException;

public class MessageHandler
{
    private final ChatClient chatClient;
    private final Lobby lobby;
    private final Commands commands;
    private final CommandHandler commandHandler;
    private final ClientData clientData;

    MessageHandler(ChatClient chatClient, Lobby lobby, ClientData clientData) {
        this.clientData = clientData;
        this.lobby = lobby;
        this.chatClient = chatClient;
        this.commands = new Commands();
        this.commandHandler = new CommandHandler(lobby);
    }

    Boolean registerRequest(String username, String password) {
        if(!clientData.usernameExist(username)) {
            clientData.addLoginCredentials(username, password);
            chatClient.outdata.println("You have successfully registered");
            //this.chatClient.setIsLoggedIn(true);
            return true;
        }
        chatClient.outdata.println("You have entered an already existing username. Please try again");
        return false;

    }

    Boolean loginRequest(String username, String password)
    {
        if(clientData.isValidLoginCredential(username, password)) {
            chatClient.outdata.println("You have successfully logged in");
            //this.chatClient.setIsLoggedIn(true);
            return true;
        } else {
            chatClient.outdata.println("You have entered invalid login credentials. Please try again");
            return false;
        }
    }

    void sendMessage(String message) {

        String response;

        if(commands.isACommand(message)) {
            response = commandHandler.commandExecution(message, chatClient);
            chatClient.outdata.println(response);
        } else {
            if(chatClient.getInLobby()) {
                this.lobby.distributeMsg(chatClient, "[" + chatClient.getUsername() + "] " + message);
            } else {
                chatClient.outdata.println("Please join or create a lobby before sending a message!");
            }
        }
    }



}
