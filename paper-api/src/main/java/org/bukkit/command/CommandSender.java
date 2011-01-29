package org.bukkit.command;


public interface CommandSender {
    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     */
    public void sendMessage(String message);

    /**
     * Checks if this player is currently op
     *
     * @return true if they are
     */
    public boolean isOp();

}