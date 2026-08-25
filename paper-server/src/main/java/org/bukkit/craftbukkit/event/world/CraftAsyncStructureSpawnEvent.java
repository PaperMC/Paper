package org.bukkit.craftbukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.AsyncStructureSpawnEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.BoundingBox;

public class CraftAsyncStructureSpawnEvent extends CraftWorldEvent implements AsyncStructureSpawnEvent {

    private final Structure structure;
    private final BoundingBox boundingBox;

    private final int chunkX, chunkZ;

    private boolean cancelled;

    public CraftAsyncStructureSpawnEvent(final World world, final Structure structure, final BoundingBox boundingBox, final int chunkX, final int chunkZ) {
        super(world, true);
        this.structure = structure;
        this.boundingBox = boundingBox;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    public Structure getStructure() {
        return this.structure;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox.clone();
    }

    @Override
    public int getChunkX() {
        return this.chunkX;
    }

    @Override
    public int getChunkZ() {
        return this.chunkZ;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return AsyncStructureSpawnEvent.getHandlerList();
    }
}
