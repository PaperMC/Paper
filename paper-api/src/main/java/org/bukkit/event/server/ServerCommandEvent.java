package org.bukkit.event.server;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Server Command events
 */
@SuppressWarnings("serial")
public class ServerCommandEvent extends ServerEvent {
    private String command;
    private CommandSender sender;

    @Deprecated
    public ServerCommandEvent(ConsoleCommandSender console, String message) {
        this(Type.SERVER_COMMAND, console, message);
    }

    public ServerCommandEvent(Type type, CommandSender sender, String command) {
        super(type);
        this.command = command;
        this.sender = sender;
    }

    /**
     * Gets the command that the user is attempting to execute from the console
     *
     * @return Command the user is attempting to execute
     */
    public String getCommand() {
        return command;
    }

    /**
     * Sets the command that the server will execute
     *
     * @param message New message that the server will execute
     */
    public void setCommand(String message) {
        this.command = message;
    }

    /**
     * Get the command sender.
     *
     * @return The sender
     */
    public CommandSender getSender() {
        return sender;
    }
}
