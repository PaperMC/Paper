package org.bukkit.event.player;

import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
// Paper start
import org.jetbrains.annotations.Nullable;
import net.kyori.adventure.text.Component;
// Paper end

/**
 * Called when a player has completed all criteria in an advancement.
 */
public class PlayerAdvancementDoneEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    //
    private final Advancement advancement;
    private Component message; // Paper - Add Adventure message

    public PlayerAdvancementDoneEvent(@NotNull Player who, @NotNull Advancement advancement) {
        // Paper start - Add Adventure message
        this(who, advancement, null);
    }
    public PlayerAdvancementDoneEvent(@NotNull Player who, @NotNull Advancement advancement, @Nullable Component message) {
        // Paper end
        super(who);
        this.advancement = advancement;
        this.message = message; // Paper - Add Adventure message
    }

    /**
     * Get the advancement which has been completed.
     *
     * @return completed advancement
     */
    @NotNull
    public Advancement getAdvancement() {
        return advancement;
    }

    // Paper start - Add Adventure message
    /**
     * Gets the message to send to all online players.
     * <p>
     * Will be null if the advancement does not announce to chat, for example if
     * it is a recipe unlock or a root advancement.
     *
     * @return The announcement message, or null
     */
    @Nullable
    public Component message() {
        return this.message;
    }

    /**
     * Sets the message to send to all online players.
     * <p>
     * If set to null the message will not be sent.
     *
     * @param message The new message
     */
    public void message(@Nullable Component message) {
        this.message = message;
    }
    // Paper end
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
