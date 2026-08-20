package org.bukkit.event.player;

import org.bukkit.event.HandlerList;

/**
 * Called when a player empties a bucket
 */
public interface PlayerBucketEmptyEvent extends PlayerBucketEvent {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
