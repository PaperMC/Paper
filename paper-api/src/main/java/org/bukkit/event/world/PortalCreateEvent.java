package org.bukkit.event.world;

import org.bukkit.block.Block;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import java.util.ArrayList;

/**
 * Called when the world attempts to create a matching end to a portal
 */
public class PortalCreateEvent extends WorldEvent implements Cancellable {
    private boolean cancel = false;
    private ArrayList<Block> blocks = new ArrayList<Block>();

    public PortalCreateEvent(final ArrayList<Block> blocks, final World world) {
        super(Type.PORTAL_CREATE, world);
        this.blocks = blocks;
    }

    /**
     * Gets an array list of all the blocks associated with the created portal
     *
     * @return array list of all the blocks associated with the created portal
     */
    public ArrayList<Block> getBlocks() {
        return this.blocks;
    }

    /**
     * Gets the cancellation state of this event. A canceled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is canceled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
