package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a {@link Structure} is naturally generated in the world.
 */
public class AsyncStructureSpawnEvent extends WorldEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private final Structure structure;
    private final BoundingBox boundingBox;

    private final int chunkX, chunkZ;

    public AsyncStructureSpawnEvent(@NotNull World world, @NotNull Structure structure, @NotNull BoundingBox boundingBox, int chunkX, int chunkZ) {
        super(world, true);
        this.structure = structure;
        this.boundingBox = boundingBox;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    /**
     * Get the structure reference that is generated.
     *
     * @return the structure
     */
    @NotNull
    public Structure getStructure() {
        return structure;
    }

    /**
     * Get the bounding box of the structure.
     *
     * @return the bounding box
     */
    @NotNull
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /**
     * Get the x coordinate of the origin chunk of the structure.
     *
     * <b>Note, it is not safe to attempt to retrieve or interact with this
     * chunk. This event is informative only!</b>
     *
     * @return the chunk x coordinate
     */
    public int getChunkX() {
        return chunkX;
    }

    /**
     * Get the z coordinate of the origin chunk of the structure.
     *
     * <b>Note, it is not safe to attempt to retrieve or interact with this
     * chunk. This event is informative only!</b>
     *
     * @return the chunk z coordinate
     */
    public int getChunkZ() {
        return chunkZ;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
