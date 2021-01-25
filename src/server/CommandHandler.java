package server;

public class CommandHandler {

    private final Commands commands;
    private final Lobby lobby;

    CommandHandler(Lobby lobby) {
        this.commands = new Commands();
        this.lobby = lobby;
    }

    public String commandExecution(String message, ChatClient chatClient) {
        String clientMessage = commands.extractMessage(message);
        String response = null;
        switch (commands.whichCommand(message)) {
            case "-h":
                response = helpRequest();
                break;
            case "-c":
                response = createRequest(chatClient, clientMessage);
                break;
            case "-j":
                response = joinRequest(chatClient, clientMessage);
            case "-f":
                fileRequest();
            case "-q":
                quitRequest(chatClient);
                break;
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

    private void quitRequest(ChatClient chatClient) {
        chatClient.outdata.println("Hejsan");

    }


}
