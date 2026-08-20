package org.bukkit.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when a player has completed all criteria in an advancement.
 */
public interface PlayerAdvancementDoneEvent extends PlayerEventNew {

    /**
     * Get the advancement which has been completed.
     *
     * @return completed advancement
     */
    Advancement getAdvancement();

    /**
     * Gets the message to send to all online players.
     * <p>
     * Will be {@code null} if the advancement does not announce to chat, for example if
     * it is a recipe unlock or a root advancement.
     *
     * @return The announcement message, or {@code null}
     */
    @Nullable Component message();

    /**
     * Sets the message to send to all online players.
     * <p>
     * If set to {@code null} the message will not be sent.
     *
     * @param message The new message
     */
    void message(@Nullable Component message);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
