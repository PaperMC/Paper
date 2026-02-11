package io.papermc.paper.event.entity;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityEventNew;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Called when two entities mate and the mating process results in a fertilization.
 * Fertilization differs from normal breeding, as represented by the {@link EntityBreedEvent}, as
 * it does not result in the immediate creation of the child entity in the world.
 * <p>
 * An example of this would be:
 * <ul>
 * <li>A frog being marked as "is_pregnant" and laying {@link Material#FROGSPAWN} later.</li>
 * <li>Sniffers producing the {@link Material#SNIFFER_EGG} item, which needs to be placed before it can begin to hatch.</li>
 * <li>A turtle being marked with "HasEgg" and laying a {@link Material#TURTLE_EGG} later.</li>
 * </ul>
 * <p>
 * The event hence only exposes the two parent entities in the fertilization process and cannot provide the child entity, as it will only exist at a later point in time.
 */
public interface EntityFertilizeEggEvent extends EntityEventNew, Cancellable {

    @Override
    LivingEntity getEntity();

    /**
     * Provides the entity in the fertilization process that will eventually be responsible for "creating" offspring,
     * may that be by setting a block that later hatches or dropping an egg that has to be placed.
     *
     * @return The "mother" entity.
     */
    LivingEntity getMother();

    /**
     * Provides the "father" entity in the fertilization process that is not responsible for initiating the offspring
     * creation.
     *
     * @return the other parent
     */
    LivingEntity getFather();

    /**
     * Gets the Entity responsible for fertilization. Breeder is {@code null} for spontaneous
     * conception.
     *
     * @return The Entity who initiated fertilization.
     */
    @Nullable Player getBreeder();

    /**
     * The ItemStack that was used to initiate fertilization, if present.
     *
     * @return ItemStack used to initiate fertilization.
     */
    @Nullable ItemStack getBredWith();

    /**
     * Get the amount of experience granted by fertilization.
     *
     * @return experience amount
     */
    int getExperience();

    /**
     * Set the amount of experience granted by fertilization.
     * If the amount is negative or zero, no experience will be dropped.
     *
     * @param experience experience amount
     */
    void setExperience(int experience);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
