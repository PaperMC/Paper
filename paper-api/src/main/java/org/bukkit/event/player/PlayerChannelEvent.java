package org.bukkit.event.player;

import org.bukkit.event.HandlerList;

/**
 * This event is called after a player registers or unregisters a new plugin
 * channel.
 */
public interface PlayerChannelEvent extends PlayerEvent {

    String getChannel();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
