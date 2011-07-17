package org.bukkit.event.world;

import org.bukkit.block.Block;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Called when the world attempts to create a matching end to a portal
 */
public class PortalCreateEvent extends WorldEvent implements Cancellable {
    private boolean cancel = false;
    private ArrayList<Block> blocks = new ArrayList<Block>();

    public PortalCreateEvent(final Collection<Block> blocks, final World world) {
        super(Type.PORTAL_CREATE, world);
        this.blocks.addAll(blocks);
    }

    /**
     * Gets an array list of all the blocks associated with the created portal
     *
     * @return array list of all the blocks associated with the created portal
     */
    public ArrayList<Block> getBlocks() {
        return this.blocks;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
