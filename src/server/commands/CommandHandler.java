package server.commands;

import server.lobbys.ChatClient;
import server.lobbys.ClientData;
import server.lobbys.Lobby;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CommandHandler {

    private final ClientData clientData;
    private final Commands commands;
    private final Lobby lobby;

    public CommandHandler(Lobby lobby, ClientData clientData) {
        this.clientData = clientData;
        this.commands = new Commands();
        this.lobby = lobby;

    }

    public String commandExecution(String message, ChatClient chatClient) throws IOException
    {
        String clientMessage = commands.extractMessage(message);
        String[] tempArr = message.split(" ");
        String response = null;
        switch (commands.whichCommand(message))
        {
            case "-h" -> response = helpRequest();
            case "-c" -> response = createRequest(chatClient, clientMessage);
            case "-j" -> response = joinRequest(chatClient, clientMessage);
            case "-f" -> response = fileRequest(chatClient, tempArr[1]);
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

    private String fileRequest(ChatClient chatClient, String size) throws IOException{
        int fileSize = Integer.parseInt(size);
        byte[] fileContent = new byte[fileSize];
        InputStream inputStream = chatClient.socket.getInputStream();
        inputStream.read(fileContent, 0, fileContent.length);

        String fileText = new String(fileContent, StandardCharsets.UTF_8);
        return "-f " + fileText;
    }

    private void quitRequest(ChatClient chatClient) throws IOException {
        this.lobby.removeFromChatRoom(chatClient);
        this.clientData.logoutChatClient(chatClient);
        chatClient.socket.close();

    }

}
