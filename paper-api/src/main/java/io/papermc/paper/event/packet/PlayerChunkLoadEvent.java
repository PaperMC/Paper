package io.papermc.paper.event.packet;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkEvent;

/**
 * Is called when a {@link Player} receives a {@link Chunk}
 * <p>
 * Can for example be used for spawning a fake entity when the player receives a chunk.
 * <p>
 * Should only be used for packet/clientside related stuff.
 * Not intended for modifying server side state.
 */
public interface PlayerChunkLoadEvent extends ChunkEvent {

    Player getPlayer();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
