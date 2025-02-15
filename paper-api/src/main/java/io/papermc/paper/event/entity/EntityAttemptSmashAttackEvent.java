package io.papermc.paper.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an entity tries to perform a smash attack
 * <p>
 * Note: This event is marked as cancelled if the player can't normally perform the smash attack
 */
@NullMarked
public class EntityAttemptSmashAttackEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LivingEntity target;
    private final ItemStack weapon;
    private final boolean canSmashAttack;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityAttemptSmashAttackEvent(LivingEntity attacker, LivingEntity target, ItemStack weapon, boolean canSmashAttack) {
        super(attacker);
        this.target = target;
        this.weapon = weapon;
        this.canSmashAttack = canSmashAttack;
        this.cancelled = !canSmashAttack;
    }

    /**
     * @return The entity being attacked with a smash attack
     */
    public LivingEntity getTarget() {
        return target;
    }

    /**
     * @return The weapon being used to perform a smash attack
     */
    public ItemStack getWeapon() {
        return weapon.clone();
    }

    /**
     * @return If the entity can perform a smash attack before any modifications have been made by this event
     */
    public boolean canSmashAttack() {
        return canSmashAttack;
    }

    /**
     * Gets the cancellation state of this event.
     * <p>
     * This is equivalent to whether the player can perform the smash attack
     * @see #setCancelled(boolean)
     * @return boolean cancellation state
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not be
     * executed in the server, but will still pass to other plugins.
     * @see #isCancelled()
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
