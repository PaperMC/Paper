package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

@SuppressWarnings("serial")
public class EndermanPickupEvent extends EntityEvent implements Cancellable {

    private boolean cancel;
    private Block block;

    public EndermanPickupEvent(Entity what, Block block) {
        super(Type.ENDERMAN_PICKUP, what);
        this.block = block;
        this.cancel = false;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Get the block that the enderman is going to pick up.
     *
     * @return block the enderman is about to pick up
     */
    public Block getBlock() {
        return block;
    }
}
