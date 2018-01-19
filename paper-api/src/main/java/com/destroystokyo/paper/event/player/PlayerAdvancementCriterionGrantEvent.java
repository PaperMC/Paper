package com.destroystokyo.paper.event.player;

import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called after a player is granted a criteria in an advancement.
 * If cancelled the criteria will be revoked.
 */
@NullMarked
public class PlayerAdvancementCriterionGrantEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Advancement advancement;
    private final String criterion;
    private final AdvancementProgress advancementProgress;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerAdvancementCriterionGrantEvent(final Player player, final Advancement advancement, final String criterion) {
        super(player);
        this.advancement = advancement;
        this.criterion = criterion;
        this.advancementProgress = player.getAdvancementProgress(advancement);
    }

    /**
     * Get the advancement which has been affected.
     *
     * @return affected advancement
     */
    public Advancement getAdvancement() {
        return this.advancement;
    }

    /**
     * Get the criterion which has been granted.
     *
     * @return granted criterion
     */
    public String getCriterion() {
        return this.criterion;
    }

    /**
     * Gets the current AdvancementProgress.
     *
     * @return advancement progress
     */
    public AdvancementProgress getAdvancementProgress() {
        return this.advancementProgress;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
