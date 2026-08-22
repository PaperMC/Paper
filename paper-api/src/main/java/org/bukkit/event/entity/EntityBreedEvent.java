package org.bukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jspecify.annotations.Nullable;

/**
 * Called when one Entity breeds with another Entity.
 */
public interface EntityBreedEvent extends EntityEvent, Cancellable {

    @Override
    LivingEntity getEntity();

    /**
     * Gets the parent creating this entity.
     *
     * @return The "birth" parent
     */
    LivingEntity getMother();

    /**
     * Gets the other parent of the newly born entity.
     *
     * @return the other parent
     */
    LivingEntity getFather();

    /**
     * Gets the Entity responsible for breeding. Breeder is {@code null} for spontaneous
     * conception.
     *
     * @return The Entity who initiated breeding.
     */
    @Nullable LivingEntity getBreeder();

    /**
     * The item that was used to initiate breeding, if present.
     *
     * @return item used to initiate breeding.
     */
    @Nullable ItemStack getBredWith();

    /**
     * Get the amount of experience granted by breeding.
     *
     * @return experience amount
     */
    @NonNegative int getExperience();

    /**
     * Set the amount of experience granted by breeding.
     *
     * @param experience experience amount
     */
    void setExperience(@NonNegative int experience);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
