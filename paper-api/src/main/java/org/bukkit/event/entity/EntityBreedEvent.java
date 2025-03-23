package org.bukkit.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when one Entity breeds with another Entity.
 */
public class EntityBreedEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LivingEntity mother;
    private final LivingEntity father;
    private final LivingEntity breeder;
    private final ItemStack bredWith;
    private int experience;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityBreedEvent(@NotNull LivingEntity child, @NotNull LivingEntity mother, @NotNull LivingEntity father, @Nullable LivingEntity breeder, @Nullable ItemStack bredWith, int experience) {
        super(child);

        this.mother = mother;
        this.father = father;
        this.breeder = breeder; // Breeder can be null in the case of spontaneous conception
        this.bredWith = bredWith;
        this.experience = experience;
    }

    @NotNull
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    /**
     * Gets the parent creating this entity.
     *
     * @return The "birth" parent
     */
    @NotNull
    public LivingEntity getMother() {
        return this.mother;
    }

    /**
     * Gets the other parent of the newly born entity.
     *
     * @return the other parent
     */
    @NotNull
    public LivingEntity getFather() {
        return this.father;
    }

    /**
     * Gets the Entity responsible for breeding. Breeder is {@code null} for spontaneous
     * conception.
     *
     * @return The Entity who initiated breeding.
     */
    @Nullable
    public LivingEntity getBreeder() {
        return this.breeder;
    }

    /**
     * The ItemStack that was used to initiate breeding, if present.
     *
     * @return ItemStack used to initiate breeding.
     */
    @Nullable
    public ItemStack getBredWith() {
        return this.bredWith;
    }

    /**
     * Get the amount of experience granted by breeding.
     *
     * @return experience amount
     */
    public int getExperience() {
        return this.experience;
    }

    /**
     * Set the amount of experience granted by breeding.
     *
     * @param experience experience amount
     */
    public void setExperience(int experience) {
        Preconditions.checkArgument(experience >= 0, "Experience cannot be negative");
        this.experience = experience;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
