package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player leaves a server
 */
public class PlayerQuitEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private net.kyori.adventure.text.Component quitMessage; // Paper

    @Deprecated // Paper
    public PlayerQuitEvent(@NotNull final Player who, @Nullable final String quitMessage) {
        super(who);
        this.quitMessage = quitMessage != null ? net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(quitMessage) : null; // Paper
    }
    // Paper start
    public PlayerQuitEvent(@NotNull final Player who, @Nullable final net.kyori.adventure.text.Component quitMessage) {
        super(who);
        this.quitMessage = quitMessage;
    }

    /**
     * Gets the quit message to send to all online players
     *
     * @return string quit message
     */
    public net.kyori.adventure.text.@Nullable Component quitMessage() {
        return quitMessage;
    }

    /**
     * Sets the quit message to send to all online players
     *
     * @param quitMessage quit message
     */
    public void quitMessage(net.kyori.adventure.text.@Nullable Component quitMessage) {
        this.quitMessage = quitMessage;
    }
    // Paper end

    /**
     * Gets the quit message to send to all online players
     *
     * @return string quit message
     * @deprecated in favour of {@link #quitMessage()}
     */
    @Nullable
    @Deprecated // Paper
    public String getQuitMessage() {
        return this.quitMessage == null ? null : net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(this.quitMessage); // Paper
    }

    /**
     * Sets the quit message to send to all online players
     *
     * @param quitMessage quit message
     * @deprecated in favour of {@link #quitMessage(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setQuitMessage(@Nullable String quitMessage) {
        this.quitMessage = quitMessage != null ? net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(quitMessage) : null; // Paper
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
}
