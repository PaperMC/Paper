package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player joins a server
 *
 * @since 1.0.0 R1
 */
public class PlayerJoinEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    // Paper start
    private net.kyori.adventure.text.Component joinMessage;
    public PlayerJoinEvent(@NotNull final Player playerJoined, @Nullable final net.kyori.adventure.text.Component joinMessage) {
        super(playerJoined);
        this.joinMessage = joinMessage;
    }

    @Deprecated // Paper end
    public PlayerJoinEvent(@NotNull final Player playerJoined, @Nullable final String joinMessage) {
        super(playerJoined);
        this.joinMessage = joinMessage != null ? net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(joinMessage) : null; // Paper end
    }

    // Paper start
    /**
     * Gets the join message to send to all online players
     *
     * @return string join message. Can be null
     * @since 1.16.5
     */
    public net.kyori.adventure.text.@Nullable Component joinMessage() {
        return this.joinMessage;
    }

    /**
     * Sets the join message to send to all online players
     *
     * @param joinMessage join message. If null, no message will be sent
     * @since 1.16.5
     */
    public void joinMessage(net.kyori.adventure.text.@Nullable Component joinMessage) {
        this.joinMessage = joinMessage;
    }
    // Paper end

    /**
     * Gets the join message to send to all online players
     *
     * @return string join message. Can be null
     * @deprecated in favour of {@link #joinMessage()}
     */
    @Nullable
    @Deprecated // Paper
    public String getJoinMessage() {
        return this.joinMessage == null ? null : net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(this.joinMessage); // Paper
    }

    /**
     * Sets the join message to send to all online players
     *
     * @param joinMessage join message. If null, no message will be sent
     * @deprecated in favour of {@link #joinMessage(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setJoinMessage(@Nullable String joinMessage) {
        this.joinMessage = joinMessage != null ? net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(joinMessage) : null; // Paper
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
