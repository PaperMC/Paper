package org.bukkit.event.player;

import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player has completed all criteria in an advancement.
 */
public class PlayerAdvancementDoneEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    //
    private final Advancement advancement;

    public PlayerAdvancementDoneEvent(@NotNull Player who, @NotNull Advancement advancement) {
        super(who);
        this.advancement = advancement;
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
