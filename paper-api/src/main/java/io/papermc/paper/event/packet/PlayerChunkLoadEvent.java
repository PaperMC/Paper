package io.papermc.paper.event.packet;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Is called when a {@link Player} receives a {@link Chunk}
 * <p>
 * Can for example be used for spawning a fake entity when the player receives a chunk.
 * <p>
 * Should only be used for packet/clientside related stuff.
 * Not intended for modifying server side state.
 */
@NullMarked
public class PlayerChunkLoadEvent extends ChunkEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;

    @ApiStatus.Internal
    public PlayerChunkLoadEvent(final Chunk chunk, final Player player) {
        super(chunk);
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
