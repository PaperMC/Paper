package org.bukkit.craftbukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkPopulateEvent;

public class CraftChunkPopulateEvent extends CraftChunkEvent implements ChunkPopulateEvent {

    public CraftChunkPopulateEvent(final Chunk chunk) {
        super(chunk);
    }

    @Override
    public HandlerList getHandlers() {
        return ChunkPopulateEvent.getHandlerList();
    }
}
