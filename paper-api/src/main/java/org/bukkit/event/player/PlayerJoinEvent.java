package org.bukkit.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when a player joins a server and has been placed in loaded chunks.
 *
 * <p>Teleporting the player during this event will result in undefined behavior.
 * Use {@link io.papermc.paper.event.player.AsyncPlayerSpawnLocationEvent} to change the spawn location.</p>
 */
public interface PlayerJoinEvent extends PlayerEvent {

    /**
     * Gets the join message to send to all online players
     *
     * @return string join message. Can be {@code null}
     */
    @Nullable Component joinMessage();

    /**
     * Sets the join message to send to all online players
     *
     * @param joinMessage join message. If {@code null}, no message will be sent
     */
    void joinMessage(@Nullable Component joinMessage);

    /**
     * Gets the join message to send to all online players
     *
     * @return string join message. Can be {@code null}
     * @deprecated in favour of {@link #joinMessage()}
     */
    @Deprecated
    @Nullable String getJoinMessage();

    /**
     * Sets the join message to send to all online players
     *
     * @param joinMessage join message. If {@code null}, no message will be sent
     * @deprecated in favour of {@link #joinMessage(Component)}
     */
    @Deprecated
    void setJoinMessage(@Nullable String joinMessage);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
