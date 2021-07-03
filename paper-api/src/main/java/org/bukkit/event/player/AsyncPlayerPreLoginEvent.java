package org.bukkit.event.player;

import java.net.InetAddress;
import java.util.UUID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Stores details for players attempting to log in.
 * <p>
 * This event is asynchronous, and not run using main thread.
 */
public class AsyncPlayerPreLoginEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Result result;
    private String message;
    private final String name;
    private final InetAddress ipAddress;
    private final UUID uniqueId;

    @Deprecated
    public AsyncPlayerPreLoginEvent(@NotNull final String name, @NotNull final InetAddress ipAddress) {
        this(name, ipAddress, null);
    }

    public AsyncPlayerPreLoginEvent(@NotNull final String name, @NotNull final InetAddress ipAddress, @NotNull final UUID uniqueId) {
        super(true);
        this.result = Result.ALLOWED;
        this.message = "";
        this.name = name;
        this.ipAddress = ipAddress;
        this.uniqueId = uniqueId;
    }

    /**
     * Gets the current result of the login, as an enum
     *
     * @return Current Result of the login
     */
    @NotNull
    public Result getLoginResult() {
        return result;
    }

    /**
     * Gets the current result of the login, as an enum
     *
     * @return Current Result of the login
     * @see #getLoginResult()
     * @deprecated This method uses a deprecated enum from {@link
     *     PlayerPreLoginEvent}
     */
    @Deprecated
    @NotNull
    public PlayerPreLoginEvent.Result getResult() {
        return result == null ? null : result.old();
    }

    /**
     * Sets the new result of the login, as an enum
     *
     * @param result New result to set
     */
    public void setLoginResult(@NotNull final Result result) {
        this.result = result;
    }

    /**
     * Sets the new result of the login, as an enum
     *
     * @param result New result to set
     * @see #setLoginResult(Result)
     * @deprecated This method uses a deprecated enum from {@link
     *     PlayerPreLoginEvent}
     */
    @Deprecated
    public void setResult(@NotNull final PlayerPreLoginEvent.Result result) {
        this.result = result == null ? null : Result.valueOf(result.name());
    }

    /**
     * Gets the current kick message that will be used if getResult() !=
     * Result.ALLOWED
     *
     * @return Current kick message
     */
    @NotNull
    public String getKickMessage() {
        return message;
    }

    /**
     * Sets the kick message to display if getResult() != Result.ALLOWED
     *
     * @param message New kick message
     */
    public void setKickMessage(@NotNull final String message) {
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
    public void disallow(@NotNull final Result result, @NotNull final String message) {
        this.result = result;
        this.message = message;
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     * @see #disallow(Result, String)
     * @deprecated This method uses a deprecated enum from {@link
     *     PlayerPreLoginEvent}
     */
    @Deprecated
    public void disallow(@NotNull final PlayerPreLoginEvent.Result result, @NotNull final String message) {
        this.result = result == null ? null : Result.valueOf(result.name());
        this.message = message;
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Gets the player IP address.
     *
     * @return The IP address
     */
    @NotNull
    public InetAddress getAddress() {
        return ipAddress;
    }

    /**
     * Gets the player's unique ID.
     *
     * @return The unique ID
     */
    @NotNull
    public UUID getUniqueId() {
        return uniqueId;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
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
         * The player is not allowed to log in, due to them not being on the
         * white list
         */
        KICK_WHITELIST,
        /**
         * The player is not allowed to log in, for reasons undefined
         */
        KICK_OTHER;

        @Deprecated
        @NotNull
        private PlayerPreLoginEvent.Result old() {
            return PlayerPreLoginEvent.Result.valueOf(name());
        }
    }
}
