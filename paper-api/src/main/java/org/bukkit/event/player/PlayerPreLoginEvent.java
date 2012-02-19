package org.bukkit.event.player;

import java.net.InetAddress;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Stores details for players attempting to log in
 */
public class PlayerPreLoginEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Result result;
    private String message;
    private final String name;
    private final InetAddress ipAddress;

    public PlayerPreLoginEvent(final String name, final InetAddress ipAddress) {
        this.result = Result.ALLOWED;
        this.message = "";
        this.name = name;
        this.ipAddress = ipAddress;
    }

    /**
     * Gets the current result of the login, as an enum
     *
     * @return Current Result of the login
     */
    public Result getResult() {
        return result;
    }

    /**
     * Sets the new result of the login, as an enum
     *
     * @param result New result to set
     */
    public void setResult(final Result result) {
        this.result = result;
    }

    /**
     * Gets the current kick message that will be used if getResult() != Result.ALLOWED
     *
     * @return Current kick message
     */
    public String getKickMessage() {
        return message;
    }

    /**
     * Sets the kick message to display if getResult() != Result.ALLOWED
     *
     * @param message New kick message
     */
    public void setKickMessage(final String message) {
        this.message = message;
    }

    /**
     * Allows the player to log in
     */
    public void allow() {
        result = Result.ALLOWED;
        message = "";
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     */
    public void disallow(final Result result, final String message) {
        this.result = result;
        this.message = message;
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player IP address.
     *
     * @return The IP address
     */
    public InetAddress getAddress() {
        return ipAddress;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Basic kick reasons for communicating to plugins
     */
    public enum Result {

        /**
         * The player is allowed to log in
         */
        ALLOWED,
        /**
         * The player is not allowed to log in, due to the server being full
         */
        KICK_FULL,
        /**
         * The player is not allowed to log in, due to them being banned
         */
        KICK_BANNED,
        /**
         * The player is not allowed to log in, due to them not being on the white list
         */
        KICK_WHITELIST,
        /**
         * The player is not allowed to log in, for reasons undefined
         */
        KICK_OTHER
    }
}
