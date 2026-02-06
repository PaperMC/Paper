package com.destroystokyo.paper.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when the server is calculating what chunks to try to spawn monsters in every Monster Spawn Tick event
 */
@NullMarked
public interface PlayerNaturallySpawnCreaturesEvent extends PlayerEvent, Cancellable {

    /**
     * @return The radius of chunks around this player to be included in natural spawn selection
     */
    byte getSpawnRadius();

    /**
     * @param radius The radius of chunks around this player to be included in natural spawn selection
     */
    void setSpawnRadius(byte radius);

    /**
     * @return If this player's chunks will be excluded from natural spawns
     */
    @Override
    boolean isCancelled();

    /**
     * @param cancel {@code true} if you wish to cancel this event, and not include this player's chunks for natural spawning
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
