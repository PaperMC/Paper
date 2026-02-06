package com.destroystokyo.paper.event.player;

import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.jspecify.annotations.NullMarked;

/**
 * Called after a player is granted a criteria in an advancement.
 * If cancelled the criteria will be revoked.
 */
@NullMarked
public interface PlayerAdvancementCriterionGrantEvent extends PlayerEventNew, Cancellable {

    /**
     * Get the advancement which has been affected.
     *
     * @return affected advancement
     */
    Advancement getAdvancement();

    /**
     * Get the criterion which has been granted.
     *
     * @return granted criterion
     */
    String getCriterion();

    /**
     * Gets the current {@link AdvancementProgress}.
     *
     * @return advancement progress
     */
    AdvancementProgress getAdvancementProgress();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
