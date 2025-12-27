package org.bukkit.event.player;

import java.net.InetAddress;
import io.papermc.paper.event.connection.PlayerConnectionValidateLoginEvent;
import org.bukkit.Warning;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Stores details for players attempting to log in.
 * <br>
 * Note that this event is called <i>early</i> in the player initialization
 * process. It is recommended that most options involving the Player
 * <i>entity</i> be postponed to the {@link PlayerJoinEvent} instead.
 *
 * @deprecated Use {@link PlayerConnectionValidateLoginEvent} to handle pre-login logic
 * (e.g. authentication or ban checks), or {@link io.papermc.paper.event.player.PlayerServerFullCheckEvent} to allow
 * players to bypass the server's maximum player limit.
 * Minecraft triggers this twice internally, using this event skips one of the validation checks done by the server.
 * Additionally, this event causes the full player entity to be created much earlier than it would be in Vanilla,
 * leaving it with mostly dysfunctional methods and state.
 */
@Warning(reason = "Listening to this event causes the player to be created early.")
@Deprecated(since = "1.21.6")
public class PlayerLoginEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String hostname;
    private final InetAddress address;
    private final InetAddress realAddress;
    private Result result = Result.ALLOWED;
    private Component message = Component.empty();

    @ApiStatus.Internal
    public PlayerLoginEvent(@NotNull final Player player, @NotNull final String hostname, @NotNull final InetAddress address, final @NotNull InetAddress realAddress) {
        super(player);
        this.hostname = hostname;
        this.address = address;
        this.realAddress = realAddress;
    }

    @ApiStatus.Internal
    public PlayerLoginEvent(@NotNull final Player player, @NotNull final String hostname, @NotNull final InetAddress address) {
        this(player, hostname, address, address);
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public PlayerLoginEvent(@NotNull final Player player, @NotNull String hostname, @NotNull final InetAddress address, @NotNull final Result result, @NotNull final String message, @NotNull final InetAddress realAddress) {
        this(player, hostname, address, realAddress);
        this.result = result;
        this.message = LegacyComponentSerializer.legacySection().deserialize(message);
    }

    @ApiStatus.Internal
    public PlayerLoginEvent(@NotNull final Player player, @NotNull String hostname, @NotNull final InetAddress address, @NotNull final Result result, @NotNull final net.kyori.adventure.text.Component message, @NotNull final InetAddress realAddress) {
        this(player, hostname, address, realAddress);
        this.result = result;
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
        return this.hostname;
    }

    /**
     * Gets the {@link InetAddress} for the Player associated with this event.
     * This method is provided as a workaround for player.getAddress()
     * returning {@code null} during PlayerLoginEvent.
     *
     * @return The address for this player. For legacy compatibility, this may
     *     be {@code null}.
     */
    @NotNull
    public InetAddress getAddress() {
        return this.address;
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
        return this.realAddress;
    }

    /**
     * Gets the current result of the login, as an enum
     *
     * @return Current Result of the login
     */
    @NotNull
    public Result getResult() {
        return this.result;
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
     * Gets the current kick message that will be used when the outcome is not allowed
     *
     * @return Current kick message
     */
    public @NotNull Component kickMessage() {
        return this.message;
    }

    /**
     * Sets the kick message to display when the outcome is not allowed
     *
     * @param message New kick message
     */
    public void kickMessage(@NotNull Component message) {
        this.message = message;
    }

    /**
     * Gets the current kick message that will be used when the outcome is not allowed
     *
     * @return Current kick message
     * @deprecated in favour of {@link #kickMessage()}
     */
    @NotNull
    @Deprecated // Paper
    public String getKickMessage() {
        return LegacyComponentSerializer.legacySection().serialize(this.message);
    }

    /**
     * Sets the kick message to display when the outcome is not allowed
     *
     * @param message New kick message
     * @deprecated in favour of {@link #kickMessage(Component)}
     */
    @Deprecated
    public void setKickMessage(@NotNull final String message) {
        this.message = LegacyComponentSerializer.legacySection().deserialize(message);
    }

    /**
     * Allows the player to log in
     */
    public void allow() {
        this.result = Result.ALLOWED;
        this.message = Component.empty();
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     * @deprecated in favour of {@link #disallow(Result, Component)}
     */
    @Deprecated
    public void disallow(@NotNull final Result result, @NotNull final String message) {
        this.result = result;
        this.message = LegacyComponentSerializer.legacySection().deserialize(message);
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     */
    public void disallow(@NotNull final Result result, @NotNull final Component message) {
        this.result = result;
        this.message = message;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
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
