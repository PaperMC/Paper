package org.bukkit.command;


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
     * Checks if this sender is a player
     *
     * @return true if they are
     * @deprecated Use instanceof
     */
    @Deprecated
    public boolean isPlayer();
}