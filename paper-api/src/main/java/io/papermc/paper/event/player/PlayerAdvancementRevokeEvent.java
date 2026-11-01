package io.papermc.paper.event.player;

import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * An event fired when a {@link Player} has an advancement revoked
 */
@NullMarked
public final class PlayerAdvancementRevokeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancelled;

    private final Advancement advancement;

    @ApiStatus.Internal
    public PlayerAdvancementRevokeEvent(final Player player, Advancement advancement) {
        super(player);
        this.advancement = advancement;
    }

    /**
     * Gets the advancement associated with this event.
     *
     * @return the advancement being revoked
     */
    public Advancement getAdvancement() {
        return this.advancement;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
