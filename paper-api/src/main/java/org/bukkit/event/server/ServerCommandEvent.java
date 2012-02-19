package org.bukkit.event.server;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

/**
 * Server Command events
 */
public class ServerCommandEvent extends ServerEvent {
    private static final HandlerList handlers = new HandlerList();
    private String command;
    private final CommandSender sender;

    public ServerCommandEvent(final CommandSender sender, final String command) {
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
