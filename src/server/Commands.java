package server;

import java.util.ArrayList;

public class Commands {

    ArrayList<String[]> commands;

    Commands() {
        this.commands = new ArrayList<>();
        this.commands.add(new String[]{"-h", "The following command displays all the available commands"});
        this.commands.add(new String[]{"-c", "The following command lets the user create and join a lobby"});
        this.commands.add(new String[]{"-j", "The following command lets the user join the specified lobby"});
        this.commands.add(new String[]{"-f", "The following command lets the user send a file"});
        this.commands.add(new String[]{"-q", "The following command exits the user from the application"});
    }

    boolean isACommand(String message) {
        return checkCommand(message) != null;
    }

    String whichCommand(String message) {
        String command;
        if((command = (checkCommand(message))) != null)
            return command;
        return null;
    }

    private String checkCommand(String message) {
        for(String[] serverCommands : commands)
            if(extractCommand(message).equals(serverCommands[0]))
                return serverCommands[0];

        return null;
    }

    private String extractCommand(String message) {
        return message.split(" ")[0];
    }

    public String extractMessage(String message) { return message.substring(message.indexOf(" ") + 1 ); }

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
