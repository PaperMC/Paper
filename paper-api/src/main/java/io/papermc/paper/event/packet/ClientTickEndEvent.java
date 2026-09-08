package io.papermc.paper.event.packet;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Called when a {@code minecraft:client_tick_end} packet is received by the server.
 */
public interface ClientTickEndEvent extends PlayerEvent {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
