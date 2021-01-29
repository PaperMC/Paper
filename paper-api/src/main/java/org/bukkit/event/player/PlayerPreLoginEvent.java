package org.bukkit.event.player;

import java.net.InetAddress;
import java.util.UUID;
import org.bukkit.Warning;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
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
    private static final HandlerList handlers = new HandlerList();
    private Result result;
    private net.kyori.adventure.text.Component message; // Paper
    private final String name;
    private final InetAddress ipAddress;
    private final UUID uniqueId;

    @Deprecated(since = "1.7.5")
    public PlayerPreLoginEvent(@NotNull final String name, @NotNull final InetAddress ipAddress) {
        this(name, ipAddress, null);
    }

    public PlayerPreLoginEvent(@NotNull final String name, @NotNull final InetAddress ipAddress, @NotNull final UUID uniqueId) {
        this.result = Result.ALLOWED;
        this.message = net.kyori.adventure.text.Component.empty(); // Paper
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

    // Paper start
    /**
     * Gets the current kick message that will be used if getResult() !=
     * Result.ALLOWED
     *
     * @return Current kick message
     */
    @NotNull
    public net.kyori.adventure.text.Component kickMessage() {
        return message;
    }

    /**
     * Sets the kick message to display if getResult() != Result.ALLOWED
     *
     * @param message New kick message
     */
    public void kickMessage(@NotNull final net.kyori.adventure.text.Component message) {
        this.message = message;
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     */
    public void disallow(@NotNull final Result result, @NotNull final net.kyori.adventure.text.Component message) {
        this.result = result;
        this.message = message;
    }
    // Paper end
    /**
     * Gets the current kick message that will be used if getResult() !=
     * Result.ALLOWED
     *
     * @return Current kick message
     * @deprecated in favour of {@link #kickMessage()}
     */
    @Deprecated // Paper
    @NotNull
    public String getKickMessage() {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(this.message); // Paper
    }

    /**
     * Sets the kick message to display if getResult() != Result.ALLOWED
     *
     * @param message New kick message
     * @deprecated in favour of {@link #kickMessage(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setKickMessage(@NotNull final String message) {
        this.message = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(message); // Paper
    }

    /**
     * Allows the player to log in
     */
    public void allow() {
        result = Result.ALLOWED;
        message = net.kyori.adventure.text.Component.empty(); // Paper
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     * @deprecated in favour of {@link #disallow(org.bukkit.event.player.PlayerPreLoginEvent.Result, net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void disallow(@NotNull final Result result, @NotNull final String message) {
        this.result = result;
        this.message = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(message); // Paper
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

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
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
