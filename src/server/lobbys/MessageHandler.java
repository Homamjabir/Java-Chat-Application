/**
 * The class handles messages that the client sends
 */

package server.lobbys;

import server.commands.CommandHandler;
import server.commands.Commands;

import java.io.IOException;

public class MessageHandler
{
    private final Lobby lobby;
    private final Commands commands;
    private final CommandHandler commandHandler;
    private final ClientData clientData;

    /**
     * Constructor
     * @param lobby
     * @param clientData
     */
    MessageHandler(Lobby lobby, ClientData clientData) {
        this.clientData = clientData;
        this.lobby = lobby;
        this.commands = new Commands();
        this.commandHandler = new CommandHandler(lobby, clientData);
    }

    /**
     * Checks if client can register
     * @param chatClient Client to log in
     * @param username
     * @param password
     * @return
     */
    String registerRequest(ChatClient chatClient, String username, String password) {
        if(!clientData.usernameExist(username)) {
            clientData.addLoginCredentials(chatClient, username, password);
            return "You have successfully registered";
        }
        return "You have entered an already existing username. Please try again";

    }

    /**
     * Checks if client is able to log in
     * @param chatClient Client to log in
     * @param username
     * @param password
     * @return
     */
    String loginRequest(ChatClient chatClient, String username, String password) {
        if(clientData.isValidLoginCredential(chatClient, username, password)) {
            return "You have successfully logged in";
        } else {
            return "You have entered invalid login credentials. Please try again";
        }
    }

    /**
     * Sends message to distribution function
     * @param chatClient Client that sent message
     * @param message Message
     * @throws IOException
     */
    void sendMessage(ChatClient chatClient, String message) throws IOException {

        String response;

        if(commands.isACommand(message)) {
            response = commandHandler.commandExecution(message, chatClient);
            if(response.split(" ")[0].equals("-f"))
            {

                StringBuilder sb = new StringBuilder();
                String[] test = response.split(" ");
                for(String s : test) {
                    if(!s.equals("-f"))
                        sb.append(s).append(" ");
                }
                sendFile(chatClient, sb.toString());
            }
            else
                chatClient.outdata.println(response);
        } else {
            if(chatClient.getInLobby()) {
                this.lobby.distributeMsg(chatClient, "[" + chatClient.getUsername() + "] " + message);
            } else {
                chatClient.outdata.println("Please join or create a lobby before sending a message!");
            }
        }
    }

    /**
     * Sends file content to distribution function
     * @param chatClient Client that sent the file
     * @param fileContent File contents
     * @throws IOException
     */
    void sendFile(ChatClient chatClient, String fileContent) throws IOException
    {

        if(chatClient.getInLobby()) {

            this.lobby.distributeFile(chatClient, fileContent);
        } else {
            chatClient.outdata.println("Please join or create a lobby before sending a file!");
        }
    }



}
