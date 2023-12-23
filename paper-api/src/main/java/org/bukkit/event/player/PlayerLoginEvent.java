package org.bukkit.event.player;

import java.net.InetAddress;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Stores details for players attempting to log in.
 * <br>
 * Note that this event is called <i>early</i> in the player initialization
 * process. It is recommended that most options involving the Player
 * <i>entity</i> be postponed to the {@link PlayerJoinEvent} instead.
 */
public class PlayerLoginEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final InetAddress address;
    private final InetAddress realAddress;
    private final String hostname;
    private Result result = Result.ALLOWED;
    private String message = "";

    /**
     * This constructor defaults message to an empty string, and result to
     * ALLOWED
     *
     * @param player The {@link Player} for this event
     * @param hostname The hostname that was used to connect to the server
     * @param address The address the player used to connect, provided for
     *     timing issues
     * @param realAddress the actual, unspoofed connecting address
     */
    public PlayerLoginEvent(@NotNull final Player player, @NotNull final String hostname, @NotNull final InetAddress address, final @NotNull InetAddress realAddress) {
        super(player);
        this.hostname = hostname;
        this.address = address;
        this.realAddress = realAddress;
    }

    /**
     * This constructor defaults message to an empty string, and result to
     * ALLOWED
     *
     * @param player The {@link Player} for this event
     * @param hostname The hostname that was used to connect to the server
     * @param address The address the player used to connect, provided for
     *     timing issues
     */
    public PlayerLoginEvent(@NotNull final Player player, @NotNull final String hostname, @NotNull final InetAddress address) {
        this(player, hostname, address, address);
    }

    /**
     * This constructor pre-configures the event with a result and message
     *
     * @param player The {@link Player} for this event
     * @param hostname The hostname that was used to connect to the server
     * @param address The address the player used to connect, provided for
     *     timing issues
     * @param result The result status for this event
     * @param message The message to be displayed if result denies login
     * @param realAddress the actual, unspoofed connecting address
     */
    public PlayerLoginEvent(@NotNull final Player player, @NotNull String hostname, @NotNull final InetAddress address, @NotNull final Result result, @NotNull final String message, @NotNull final InetAddress realAddress) {
        this(player, hostname, address, realAddress);
        this.result = result;
        this.message = message;
    }

    /**
     * Gets the current result of the login, as an enum
     *
     * @return Current Result of the login
     */
    @NotNull
    public Result getResult() {
        return result;
    }

    /**
     * Sets the new result of the login, as an enum
     *
     * @param result New result to set
     */
    public void setResult(@NotNull final Result result) {
        this.result = result;
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
     * Gets the hostname that the player used to connect to the server, or
     * blank if unknown
     *
     * @return The hostname
     */
    @NotNull
    public String getHostname() {
        return hostname;
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
     * Gets the {@link InetAddress} for the Player associated with this event.
     * This method is provided as a workaround for player.getAddress()
     * returning null during PlayerLoginEvent.
     *
     * @return The address for this player. For legacy compatibility, this may
     *     be null.
     */
    @NotNull
    public InetAddress getAddress() {
        return address;
    }

    /**
     * Gets the connection address of this player, regardless of whether it has
     * been spoofed or not.
     *
     * @return the player's connection address
     * @see #getAddress()
     */
    @NotNull
    public InetAddress getRealAddress() {
        return realAddress;
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
        KICK_OTHER
    }
}
