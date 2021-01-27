package server;

import java.io.IOException;

public class CommandHandler {

    private final ClientData clientData;
    private final Commands commands;
    private final Lobby lobby;

    CommandHandler(Lobby lobby, ClientData clientData) {
        this.clientData = clientData;
        this.commands = new Commands();
        this.lobby = lobby;
    }

    public String commandExecution(String message, ChatClient chatClient) throws IOException
    {
        String clientMessage = commands.extractMessage(message);
        String response = null;
        switch (commands.whichCommand(message))
        {
            case "-h" -> response = helpRequest();
            case "-c" -> response = createRequest(chatClient, clientMessage);
            case "-j" -> response = joinRequest(chatClient, clientMessage);
            case "-f" -> fileRequest();
            case "-q" -> quitRequest(chatClient);
        }
        return response;
    }

    private String helpRequest() {
        return commands.toString();
    }

    private String createRequest(ChatClient chatClient, String chatRoomName) {
        if(lobby.createChatRoom(chatClient, chatRoomName))
            return "Unable to create chatroom";

        chatClient.setInLobby(true);
        return "Chatroom \"" + chatRoomName +"\" was successfully created";
    }

    private String joinRequest(ChatClient chatClient, String chatRoomName) {
        if(lobby.joinChatRoom(chatClient, chatRoomName))
            return "Unable to join chatroom";

        chatClient.setInLobby(true);
        return "You have now entered the chatroom \"" + chatRoomName +"\"";
    }



    private void fileRequest() {

    }

    private void quitRequest(ChatClient chatClient) throws IOException
    {
        this.lobby.removeFromChatRoom(chatClient);
        this.clientData.logoutChatClient(chatClient);
        chatClient.socket.close();


    }


}
