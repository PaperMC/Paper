package io.papermc.paper.event.player;

import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * An event fired when a {@link Player} has an advancement revoked.
 */
@NullMarked
public final class PlayerAdvancementRevokeEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Advancement advancement;

    @ApiStatus.Internal
    public PlayerAdvancementRevokeEvent(final Player player, final Advancement advancement) {
        super(player);
        this.advancement = advancement;
    }

    /**
     * Get the advancement which has been revoked.
     *
     * @return revoked advancement
     */
    public Advancement getAdvancement() {
        return this.advancement;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
