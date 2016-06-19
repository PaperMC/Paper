package org.bukkit.event.entity;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called when one Entity breeds with another Entity.
 */
public class EntityBreedEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    //
    private final LivingEntity mother;
    private final LivingEntity father;
    private final LivingEntity breeder;
    private final ItemStack bredWith;
    private int experience;
    //
    private boolean cancel;

    public EntityBreedEvent(LivingEntity child, LivingEntity mother, LivingEntity father, LivingEntity breeder, ItemStack bredWith, int experience) {
        super(child);

        Validate.notNull(child, "Cannot have null child");
        Validate.notNull(mother, "Cannot have null mother");
        Validate.notNull(father, "Cannot have null father");

        // Breeder can be null in the case of spontaneous conception
        this.mother = mother;
        this.father = father;
        this.breeder = breeder;
        this.bredWith = bredWith;

        setExperience(experience);
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) entity;
    }

    /**
     * Gets the parent creating this entity.
     *
     * @return The "birth" parent
     */
    public LivingEntity getMother() {
        return mother;
    }

    /**
     * Gets the other parent of the newly born entity.
     *
     * @return the other parent
     */
    public LivingEntity getFather() {
        return father;
    }

    /**
     * Gets the Entity responsible for breeding. Breeder is null for spontaneous
     * conception.
     *
     * @return The Entity who initiated breeding.
     */
    public LivingEntity getBreeder() {
        return breeder;
    }

    /**
     * The ItemStack that was used to initiate breeding, if present.
     *
     * @return ItemStack used to initiate breeding.
     */
    public ItemStack getBredWith() {
        return bredWith;
    }

    /**
     * Get the amount of experience granted by breeding.
     *
     * @return experience amount
     */
    public int getExperience() {
        return experience;
    }

    /**
     * Set the amount of experience granted by breeding.
     *
     * @param experience experience amount
     */
    public void setExperience(int experience) {
        Validate.isTrue(experience >= 0, "Experience cannot be negative");
        this.experience = experience;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
