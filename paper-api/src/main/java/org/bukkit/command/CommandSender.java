package org.bukkit.command;

import org.bukkit.Server;


public interface CommandSender {
    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     */
    public void sendMessage(String message);

    /**
     * Checks if this sender is currently op
     *
     * @return true if they are
     */
    public boolean isOp();

    /**
     * Returns the server instance that this command is running on
     *
     * @return Server instance
     */
    public Server getServer();
}