
package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

/**
 * Thrown whenever a LivingEntity dies
 */
public class EntityDeathEvent extends EntityEvent {
    private List<ItemStack> drops;

    public EntityDeathEvent(final Type type, final Entity what, final List<ItemStack> drops) {
        super(type, what);
        this.drops = drops;
    }

    /**
     * Sets what items will be dropped when this entity dies
     *
     * @param drops Items to drop when the entity dies
     */
    public void setDrops(final List<ItemStack> drops) {
        this.drops = drops;
    }

    /**
     * Gets all the items which will drop when the entity dies
     *
     * @return Items to drop when the entity dies
     */
    public List<ItemStack> getDrops() {
        return drops;
    }
}
