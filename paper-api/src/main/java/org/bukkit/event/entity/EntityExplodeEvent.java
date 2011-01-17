
package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 *
 * @author SpeaKeasY
 */
public class EntityExplodeEvent extends EntityEvent implements Cancellable {
    private boolean cancel;
    private List blocks;
    
    public EntityExplodeEvent (Type type, Entity what, List<Block> blocks) {
        super(type.ENTITY_EXPLODE, what);
        this.cancel = false;
        this.blocks = blocks;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
    
    /**
     * Returns the list of blocks that would have been removed or were
     * removed from the explosion event.
     */
    public List<Block> blockList() {
        return blocks;
    }

}
