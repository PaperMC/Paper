package org.bukkit.event.player;

import org.bukkit.event.HandlerList;

/**
 * Called when a player fills a bucket
 */
public interface PlayerBucketFillEvent extends PlayerBucketEvent {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
