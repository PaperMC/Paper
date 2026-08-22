package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Called when a LivingEntity shoots a bow firing an arrow
 */
public interface EntityShootBowEvent extends EntityEvent, Cancellable {

    @Override
    LivingEntity getEntity();

    /**
     * Gets the bow ItemStack used to fire the arrow.
     *
     * @return the bow involved in this event
     */
    @Nullable ItemStack getBow();

    /**
     * Get the ItemStack to be consumed in this event (if any).
     * <p>
     * For instance, bows will consume an arrow ItemStack in a player's
     * inventory.
     *
     * @return the consumable item
     */
    @Nullable ItemStack getConsumable();

    /**
     * Gets the projectile which will be launched by this event
     *
     * @return the launched projectile
     */
    Entity getProjectile();

    /**
     * Replaces the projectile which will be launched
     *
     * @param projectile the new projectile
     */
    void setProjectile(Entity projectile);

    /**
     * Get the hand from which the bow was shot.
     *
     * @return the hand
     */
    EquipmentSlot getHand();

    /**
     * Gets the force the arrow was launched with
     *
     * @return bow shooting force, up to 1.0
     */
    float getForce();

    /**
     * Get whether the consumable item should be consumed in this event.
     *
     * @return {@code true} if consumed, {@code false} otherwise
     */
    boolean shouldConsumeItem();

    /**
     * Set whether the consumable item should be consumed in this event.
     * <p>
     * If set to {@code false}, it is recommended that a call to
     * {@link Player#updateInventory()} is made as the client may disagree with
     * the server's decision to not consume a consumable item.
     * <p>
     * This value is ignored for entities where items are not required
     * (skeletons, pillagers, etc.) or with crossbows (as no item is being
     * consumed).
     *
     * @param consumeItem whether to consume the item
     * @deprecated not currently functional
     */
    @Deprecated(since = "1.20.5")
    void setConsumeItem(boolean consumeItem);

    /**
     * @deprecated use {@link #getConsumable()}
     */
    @Nullable @Deprecated
    default ItemStack getArrowItem() {
        return this.getConsumable();
    }

    /**
     * @deprecated use {@link #shouldConsumeItem()}
     */
    @Deprecated
    default boolean getConsumeArrow() {
        return this.shouldConsumeItem();
    }

    /**
     * @deprecated not currently functional
     */
    @Deprecated
    default void setConsumeArrow(final boolean consumeArrow) {
        this.setConsumeItem(consumeArrow);
    }

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
