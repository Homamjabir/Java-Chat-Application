package server;

import java.util.ArrayList;

public class ChatRoom {

    private final ArrayList<ChatClient> clientsInLobby;
    private final String chatRoomName;

    ChatRoom(String chatRoomName) {
        this.chatRoomName = chatRoomName;
        this.clientsInLobby = new ArrayList<>();
    }

    void addClient(ChatClient chatClient) {
        clientsInLobby.add(chatClient);
    }

    ArrayList<ChatClient> getChatRoomClientsList() {
        return this.clientsInLobby;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }
}
