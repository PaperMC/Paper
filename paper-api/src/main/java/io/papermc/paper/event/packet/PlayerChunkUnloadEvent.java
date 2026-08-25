package io.papermc.paper.event.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.world.ChunkEvent;

/**
 * Is called when a {@link Player} receives a chunk unload packet.
 * <p>
 * Should only be used for packet/clientside related stuff.
 * Not intended for modifying server side.
 */
public interface PlayerChunkUnloadEvent extends ChunkEvent, PlayerEvent {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
