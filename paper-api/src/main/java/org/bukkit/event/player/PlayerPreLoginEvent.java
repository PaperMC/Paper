package org.bukkit.event.player;

import java.net.InetAddress;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Warning;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Stores details for players attempting to log in
 * <p>
 * When this event is fired, the player's locale is not
 * available. Therefore, any translatable component will be
 * rendered with the default locale, {@link java.util.Locale#US}.
 * <p>
 * Consider rendering any translatable yourself with {@link net.kyori.adventure.translation.GlobalTranslator#render}
 * if the client's language is known.
 *
 * @deprecated This event causes synchronization from the login thread; {@link
 *     AsyncPlayerPreLoginEvent} is preferred to keep the secondary threads
 *     asynchronous.
 */
@Deprecated(since = "1.3.2")
@Warning(reason = "This event causes a login thread to synchronize with the main thread")
public class PlayerPreLoginEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String name;
    private final InetAddress ipAddress;
    private final UUID uniqueId;
    private Result result;
    private Component message;

    @ApiStatus.Internal
    @Deprecated(since = "1.7.5", forRemoval = true)
    public PlayerPreLoginEvent(@NotNull final String name, @NotNull final InetAddress ipAddress) {
        this(name, ipAddress, null);
    }

    @ApiStatus.Internal
    public PlayerPreLoginEvent(@NotNull final String name, @NotNull final InetAddress ipAddress, @NotNull final UUID uniqueId) {
        this.result = Result.ALLOWED;
        this.message = Component.empty();
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
    @NotNull
    public Component kickMessage() {
        return this.message;
    }

    /**
     * Sets the kick message to display when the outcome is not allowed
     *
     * @param message New kick message
     */
    public void kickMessage(@NotNull final Component message) {
        this.message = message;
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

    /**
     * Gets the current kick message that will be used when the outcome is not allowed
     *
     * @return Current kick message
     * @deprecated in favour of {@link #kickMessage()}
     */
    @Deprecated // Paper
    @NotNull
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
     * @deprecated in favour of {@link #disallow(org.bukkit.event.player.PlayerPreLoginEvent.Result, Component)}
     */
    @Deprecated // Paper
    public void disallow(@NotNull final Result result, @NotNull final String message) {
        this.result = result;
        this.message = LegacyComponentSerializer.legacySection().deserialize(message);
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    @NotNull
    public String getName() {
        return this.name;
    }

    /**
     * Gets the player IP address.
     *
     * @return The IP address
     */
    @NotNull
    public InetAddress getAddress() {
        return this.ipAddress;
    }

    /**
     * Gets the player's unique ID.
     *
     * @return The unique ID
     */
    @NotNull
    public UUID getUniqueId() {
        return this.uniqueId;
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
