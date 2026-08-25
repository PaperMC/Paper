package org.bukkit.event.world;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.BoundingBox;

/**
 * Called when a {@link Structure} is naturally generated in the world.
 */
public interface AsyncStructureSpawnEvent extends WorldEvent, Cancellable {

    /**
     * Get the structure reference that is generated.
     *
     * @return the structure
     */
    Structure getStructure();

    /**
     * Get the bounding box of the structure.
     *
     * @return the bounding box
     */
    BoundingBox getBoundingBox();

    /**
     * Get the x coordinate of the origin chunk of the structure.
     * <p>
     * <b>Note, it is not safe to attempt to retrieve or interact with this
     * chunk. This event is informative only!</b>
     *
     * @return the chunk x coordinate
     */
    int getChunkX();

    /**
     * Get the z coordinate of the origin chunk of the structure.
     * <p>
     * <b>Note, it is not safe to attempt to retrieve or interact with this
     * chunk. This event is informative only!</b>
     *
     * @return the chunk z coordinate
     */
    int getChunkZ();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
