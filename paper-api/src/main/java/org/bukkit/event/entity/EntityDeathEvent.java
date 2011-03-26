
package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

/**
 * Thrown whenever a LivingEntity dies
 */
public class EntityDeathEvent extends EntityEvent {
    private List<ItemStack> drops;

    public EntityDeathEvent(final Entity what, final List<ItemStack> drops) {
        super(Type.ENTITY_DEATH, what);
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
