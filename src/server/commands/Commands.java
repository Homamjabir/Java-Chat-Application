/**
 * Class that handles all commands in the server
 */

package server.commands;

import java.util.ArrayList;

public class Commands {

    ArrayList<String[]> commands;

    /**
     * Constructor
     */
    public Commands() {
        this.commands = new ArrayList<>();
        this.commands.add(new String[]{"-h", "The following command displays all the available commands. Example -h"});
        this.commands.add(new String[]{"-c", "The following command lets the user create and join a lobby. Example -c chatroom1"});
        this.commands.add(new String[]{"-j", "The following command lets the user join the specified lobby. Example -j chatroom1"});
        this.commands.add(new String[]{"-f", "The following command lets the user send a file. Example -f"});
        this.commands.add(new String[]{"-q", "The following command exits the user from the application. Example -q"});
    }

    /**
     * Checks if the received string is a command
     * @param message Message to check
     * @return Returns true if its a command and false otherwise
     */
    public boolean isACommand(String message) {
        return checkCommand(message) != null;
    }

    /**
     * Returns which command the chat client has sent
     * @param message Message to check
     * @return Returns true if its a command and null otherwise
     */
    String whichCommand(String message) {
        String command;
        if((command = (checkCommand(message))) != null)
            return command;
        return null;
    }

    /**
     * Return the command if it matches the existing ones and null otherwise
     * @param message Message to check
     * @return Returns command or null
     */
    private String checkCommand(String message) {
        for(String[] serverCommands : commands)
            if(extractCommand(message).equals(serverCommands[0]))
                return serverCommands[0];

        return null;
    }

    /**
     * Extracts command from message
     * @param message Message to extract command from
     * @return Return the command
     */
    private String extractCommand(String message) {
        return message.split(" ")[0];
    }

    /**
     * Extracts the message from the client string
     * @param message The client string
     * @return The message to return
     */
    public String extractMessage(String message) { return message.substring(message.indexOf(" ") + 1 ); }

    /**
     * Returns all the commands in the form of a string
     * @return String to return
     */
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("These are the available commands:\n");

        for(String[] serverCommands : commands) {
            stringBuilder.append("\n");
            stringBuilder.append(serverCommands[0]);
            stringBuilder.append(" ");
            stringBuilder.append(serverCommands[1]);
        }

        return stringBuilder.toString();
    }

}
