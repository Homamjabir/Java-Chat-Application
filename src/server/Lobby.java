package server;

import java.util.ArrayList;

public class Lobby {

    private final ArrayList<ChatRoom> chatRooms;

    Lobby() {
        ArrayList<ChatClient> clients = new ArrayList<>();
        this.chatRooms = new ArrayList<>();
    }

    boolean createChatRoom(ChatClient chatClient, String chatRoomName) {
        if(chatRoomExists(chatRoomName))
            return true;

        ChatRoom chatroom = new ChatRoom(chatRoomName);
        this.chatRooms.add(chatroom);
        chatroom.addClient(chatClient);
        chatClient.setChatRoom(chatroom);
        printLobbys();
        return false;
    }

    boolean joinChatRoom(ChatClient chatClient, String chatRoomName) {
        ChatRoom chatroom;
        printLobbys();

        if ((chatroom = getChatRoom(chatRoomName)) == null )
        {
            return true;
        }

        this.chatRooms.add(chatroom);
        chatroom.addClient(chatClient);
        chatClient.setChatRoom(chatroom);

        return false;
    }

    private boolean chatRoomExists(String chatRoomName) {
        for(ChatRoom chatRoom : this.chatRooms)
            if(chatRoom.getChatRoomName().equals(chatRoomName))
                return true;
        return false;
    }

    public ChatRoom getChatRoom(String chatRoomName) {
        System.out.println(this.chatRooms.size());

        for (ChatRoom c: this.chatRooms)
        {
            if (c.getChatRoomName().equals(chatRoomName))
            {

                return c;
            }
        }
        return null;
    }

    private void printLobbys() {
        for(ChatRoom c : this.chatRooms)
            System.out.println("Chatroom name: " + c.getChatRoomName());
    }

    void distributeMsg(ChatClient clients, String msg) {
        for(ChatClient client : clients.getChatRoom().getChatRoomClientsList())
            client.sendMessage(msg);
    }




}
