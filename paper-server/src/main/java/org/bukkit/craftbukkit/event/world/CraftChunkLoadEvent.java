package org.bukkit.craftbukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkLoadEvent;

public class CraftChunkLoadEvent extends CraftChunkEvent implements ChunkLoadEvent {

    private final boolean newChunk;

    public CraftChunkLoadEvent(final Chunk chunk, final boolean newChunk) {
        super(chunk);
        this.newChunk = newChunk;
    }

    @Override
    public boolean isNewChunk() {
        return this.newChunk;
    }

    @Override
    public HandlerList getHandlers() {
        return ChunkLoadEvent.getHandlerList();
    }
}
