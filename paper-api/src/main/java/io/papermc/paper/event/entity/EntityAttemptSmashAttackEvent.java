package io.papermc.paper.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.bukkit.inventory.ItemStack;

/**
 * Called when an entity attempts to perform a smash attack.
 */
public interface EntityAttemptSmashAttackEvent extends EntityEventNew {

    /**
     * Yields the target of the attempted smash attack.
     *
     * @return the target entity
     */
    LivingEntity getTarget();

    /**
     * Yields a copy of the itemstack used in the smash attack attempt.
     *
     * @return the itemstack
     */
    ItemStack getWeapon();

    /**
     * Yields the original result the server computed.
     *
     * @return {@code true} if this attempt would have been successful by vanilla's logic, {@code false} otherwise.
     */
    boolean getOriginalResult();

    /**
     * Yields the effective result of this event.
     * The result may take one of three values:
     *
     * <ul>
     *     <li>{@link Result#ALLOW}: The attempt will succeed.</li>
     *     <li>{@link Result#DENY}: The attempt will fail.</li>
     *     <li>{@link Result#DEFAULT}: The attempt will succeed if {@link #getOriginalResult()} is {@code true} and fail otherwise.</li>
     * </ul>
     *
     * @return the result.
     */
    Result getResult();

    /**
     * Configures a new result for this event.
     * The passes result may take one of three values:
     *
     * <ul>
     *     <li>{@link Result#ALLOW}: The attempt will succeed.</li>
     *     <li>{@link Result#DENY}: The attempt will fail.</li>
     *     <li>{@link Result#DEFAULT}: The attempt will succeed if {@link #getOriginalResult()} is {@code true} and fail otherwise.</li>
     * </ul>
     *
     * @param result the new result of the event.
     */
    void setResult(Result result);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
