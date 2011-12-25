package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

@SuppressWarnings("serial")
public class EntityCombustByBlockEvent extends EntityCombustEvent {

    private Block combuster;

    public EntityCombustByBlockEvent(Block combuster, Entity combustee, int duration) {
        super(combustee, duration);
        this.combuster = combuster;
    }

    /**
     * The combuster can be lava or a block that is on fire.
     *
     * WARNING: block may be null.
     *
     * @return the Block that set the combustee alight.
     */
    public Block getCombuster() {
        return combuster;
    }
}
