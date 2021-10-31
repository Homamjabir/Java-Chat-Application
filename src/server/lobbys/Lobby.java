/**
 * The following class handles all clients connected to the server, the chatroom's,
 * and the communication between the clients
 */

package server.lobbys;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.ArrayList;

public class Lobby
{

    private final MessageHandler messageHandler;
    private final ArrayList<ChatRoom> chatRooms;
    private final ArrayList<ChatClient> chatClients;
    private final ArrayList<Thread> threads;

    /**
     * Constructor
     */
    public Lobby() {
        ClientData clientData = new ClientData();
        this.messageHandler = new MessageHandler(this, clientData);
        this.chatClients = new ArrayList<>();
        this.chatRooms = new ArrayList<>();
        this.threads = new ArrayList<>();
    }

    /**
     * Creates and adds the user to the chatroom and checks that no
     * other chatroom exists with the same name
     * @param chatClient Chat client to add
     * @param chatRoomName Chatroom name to create
     * @return Returns true if their already exists a chatroom with the same name and false otherwise
     */
    public boolean createChatRoom(ChatClient chatClient, String chatRoomName) {
        if(chatRoomExists(chatRoomName))
            return true;

        ChatRoom chatroom = new ChatRoom(chatRoomName);
        this.chatRooms.add(chatroom);
        chatroom.addClient(chatClient);
        chatClient.setChatRoom(chatroom);
        return false;
    }

    /**
     * Adds the chat client to the specified chatroom after checking that it already exists
     * @param chatClient Chat client to add
     * @param chatRoomName Chatroom name to join
     * @return Returns true if chatroom does not already exist and false otherwise
     */
    public boolean joinChatRoom(ChatClient chatClient, String chatRoomName) {
        ChatRoom chatroom;
        if ((chatroom = getChatRoom(chatRoomName)) == null ) {
            return true;
        }

        chatroom.addClient(chatClient);
        chatClient.setChatRoom(chatroom);

        return false;
    }

    /**
     * Checks if the chatroom exists
     * @param chatRoomName Chatroom name to look for
     * @return Returns true if it exists and false otherwise
     */
    private boolean chatRoomExists(String chatRoomName) {
        for(ChatRoom chatRoom : this.chatRooms)
            if(chatRoom.getChatRoomName().equals(chatRoomName))
                return true;
        return false;
    }

    /**
     * Returns the chatroom with the help of it name
     * @param chatRoomName Chatroom name to look for
     * @return Returns chatroom if it exists and null otherwise
     */
    ChatRoom getChatRoom(String chatRoomName) {
        System.out.println(this.chatRooms.size());

        for (ChatRoom c: this.chatRooms) {
            if (c.getChatRoomName().equals(chatRoomName)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Removes chat client from chatroom if client already is in chatroom
     * @param chatClient Chat client to remove from chatroom
     */
    public void removeFromChatRoom(ChatClient chatClient) {
        if(chatClient.getInLobby())
        {
            chatClient.getChatRoom().removeFromRoom(chatClient);
        }
    }

    /**
     * Sends string to every chat client that is in the specified clients chatroom
     * @param client Chat client that sent the message
     * @param msg Message sent by the chat client
     */
    void distributeMsg(ChatClient client, String msg) {
        for(ChatClient clients : client.getChatRoom().getChatRoomClientsList())
            if(!clients.equals(client))
                clients.sendMessage(msg);
    }

    /**
     * Sens a file contents to every chat that is in the specified clients chatroom
     * @param client
     * @param fileContent
     * @throws IOException
     */
    void distributeFile(ChatClient client, String fileContent) throws IOException {
        for(ChatClient clients : client.getChatRoom().getChatRoomClientsList()) {
            if(!clients.equals(client))
                clients.sendFile(fileContent);
        }
    }

    /**
     * Creates the ChatClient object with the sslSocket and starts its thread
     * @param sslSocket Chat clients sslSocket
     */
    public void addClient(SSLSocket sslSocket) {
        ChatClient chatClient = new ChatClient(sslSocket, this.messageHandler);
        Thread thread = new Thread(chatClient);
        this.threads.add(thread);
        thread.start();
        this.chatClients.add(chatClient);
        System.out.println("Number of connected users: " + this.chatClients.size());
    }
}
