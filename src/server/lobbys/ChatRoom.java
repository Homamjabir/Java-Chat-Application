/**
 * Class that handle all a chatroom
 */

package server.lobbys;

import java.util.ArrayList;

public class ChatRoom
{

    private final ArrayList<ChatClient> clientsInLobby;
    private final String chatRoomName;

    /**
     * Constructor that creates the chatroom
     * @param chatRoomName Chatroom name
     */
    ChatRoom(String chatRoomName) {
        this.chatRoomName = chatRoomName;
        this.clientsInLobby = new ArrayList<>();
    }

    /**
     * Adds client
     * @param chatClient
     */
    void addClient(ChatClient chatClient) {
        clientsInLobby.add(chatClient);
    }

    /**
     * Removes client
     * @param chatClient
     */
    void removeFromRoom(ChatClient chatClient) {
        this.clientsInLobby.remove(chatClient);
    }

    /**
     * Return all clients in chatroom
     * @return
     */
    ArrayList<ChatClient> getChatRoomClientsList() {
        return this.clientsInLobby;
    }

    /**
     * Returns chatroom name
     * @return
     */
    public String getChatRoomName() {
        return chatRoomName;
    }
}
