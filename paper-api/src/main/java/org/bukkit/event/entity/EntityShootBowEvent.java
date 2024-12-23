package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a LivingEntity shoots a bow firing an arrow
 *
 * @since 1.1.0
 */
public class EntityShootBowEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack bow;
    private final ItemStack consumable;
    private Entity projectile;
    private final EquipmentSlot hand;
    private final float force;
    private boolean consumeItem;
    private boolean cancelled;
    /**
     * @since 1.12.2
     */
    // Paper start
    @Deprecated
    public void setConsumeArrow(boolean consumeArrow) {
        this.setConsumeItem(consumeArrow);
    }

    /**
     * @since 1.12.2
     */
    @Deprecated
    public boolean getConsumeArrow() {
        return this.shouldConsumeItem();
    }

    /**
     * @since 1.12.2
     */
    @Nullable @Deprecated
    public ItemStack getArrowItem() {
        return this.getConsumable();
    }

    @Deprecated
    public EntityShootBowEvent(@NotNull final LivingEntity shooter, @Nullable final ItemStack bow, @NotNull final Entity projectile, final float force) {
        this(shooter, bow, new ItemStack(org.bukkit.Material.AIR), projectile, force);
    }

    @Deprecated
    public EntityShootBowEvent(@NotNull final LivingEntity shooter, @Nullable final ItemStack bow, @NotNull ItemStack arrowItem, @NotNull final Entity projectile, final float force) {
        this(shooter, bow, arrowItem, projectile, EquipmentSlot.HAND, force, true);
    }
    // Paper end
    public EntityShootBowEvent(@NotNull final LivingEntity shooter, @Nullable final ItemStack bow, @Nullable final ItemStack consumable, @NotNull final Entity projectile, @NotNull final EquipmentSlot hand, final float force, final boolean consumeItem) {
        super(shooter);
        this.bow = bow;
        this.consumable = consumable;
        this.projectile = projectile;
        this.hand = hand;
        this.force = force;
        this.consumeItem = consumeItem;
    }

    @NotNull
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) entity;
    }

    /**
     * Gets the bow ItemStack used to fire the arrow.
     *
     * @return the bow involved in this event
     */
    @Nullable
    public ItemStack getBow() {
        return bow;
    }

    /**
     * Get the ItemStack to be consumed in this event (if any).
     *
     * For instance, bows will consume an arrow ItemStack in a player's
     * inventory.
     *
     * @return the consumable item
     * @since 1.16.2
     */
    @Nullable
    public ItemStack getConsumable() {
        return consumable;
    }

    /**
     * Gets the projectile which will be launched by this event
     *
     * @return the launched projectile
     */
    @NotNull
    public Entity getProjectile() {
        return projectile;
    }

    /**
     * Replaces the projectile which will be launched
     *
     * @param projectile the new projectile
     */
    public void setProjectile(@NotNull Entity projectile) {
        this.projectile = projectile;
    }

    /**
     * Get the hand from which the bow was shot.
     *
     * @return the hand
     * @since 1.16.2
     */
    @NotNull
    public EquipmentSlot getHand() {
        return hand;
    }

    /**
     * Gets the force the arrow was launched with
     *
     * @return bow shooting force, up to 1.0
     */
    public float getForce() {
        return force;
    }

    /**
     * Set whether or not the consumable item should be consumed in this event.
     *
     * If set to false, it is recommended that a call to
     * {@link Player#updateInventory()} is made as the client may disagree with
     * the server's decision to not consume a consumable item.
     * <p>
     * This value is ignored for entities where items are not required
     * (skeletons, pillagers, etc.) or with crossbows (as no item is being
     * consumed).
     *
     * @param consumeItem whether or not to consume the item
     * @deprecated not currently functional
     * @since 1.16.2
     */
    @Deprecated(since = "1.20.5")
    public void setConsumeItem(boolean consumeItem) {
        this.consumeItem = consumeItem;
    }

    /**
     * Get whether or not the consumable item should be consumed in this event.
     *
     * @return true if consumed, false otherwise
     * @since 1.16.2
     */
    public boolean shouldConsumeItem() {
        return consumeItem;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
