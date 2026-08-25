package org.bukkit.craftbukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkUnloadEvent;

public class CraftChunkUnloadEvent extends CraftChunkEvent implements ChunkUnloadEvent {

    private boolean saveChunk;

    public CraftChunkUnloadEvent(final Chunk chunk, final boolean save) {
        super(chunk);
        this.saveChunk = save;
    }

    @Override
    public boolean isSaveChunk() {
        return this.saveChunk;
    }

    @Override
    public void setSaveChunk(final boolean saveChunk) {
        this.saveChunk = saveChunk;
    }

    @Override
    public HandlerList getHandlers() {
        return ChunkUnloadEvent.getHandlerList();
    }
}
