package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

/**
 * Thrown whenever a LivingEntity dies
 */
public class EntityDeathEvent extends EntityEvent {
    private List<ItemStack> drops;
    private int dropExp = 0;

    public EntityDeathEvent(final Entity entity, final List<ItemStack> drops) {
        this(entity, drops, 0);
    }

    public EntityDeathEvent(final Entity what, final List<ItemStack> drops, int droppedExp) {
        super(Type.ENTITY_DEATH, what);
        this.drops = drops;
        this.dropExp = droppedExp;
    }

    /**
     * Gets how much EXP should be dropped from this death.
     * <p>
     * This does not indicate how much EXP should be taken from the entity in question,
     * merely how much should be created after its death.
     *
     * @return Amount of EXP to drop.
     */
    public int getDroppedExp() {
        return dropExp;
    }

    /**
     * Sets how much EXP should be dropped from this death.
     * <p>
     * This does not indicate how much EXP should be taken from the entity in question,
     * merely how much should be created after its death.
     *
     * @param exp Amount of EXP to drop.
     */
    public void setDroppedExp(int exp) {
        this.dropExp = exp;
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
