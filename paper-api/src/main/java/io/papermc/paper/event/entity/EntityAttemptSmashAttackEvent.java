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
 */
@NullMarked
public class EntityAttemptSmashAttackEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LivingEntity target;
    private final ItemStack weapon;

    private Result result;

    @ApiStatus.Internal
    public EntityAttemptSmashAttackEvent(LivingEntity attacker, LivingEntity target, ItemStack weapon) {
        super(attacker);
        this.target = target;
        this.weapon = weapon;
        this.result = Result.DEFAULT;
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
     * Gets the result of this event.
     *
     * @return the result
     * @see #setResult(Result)
     */
    public Result getResult() {
        return result;
    }

    /**
     * Sets the result of this event. {@link Result#DEFAULT} is the default
     * allowing the vanilla logic to check if the entity can perform a smash attack. Set to {@link Result#ALLOW}
     * or {@link Result#DENY} to override that behavior.
     * @param result the result of this event
     */
    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * Gets the cancellation state of this event.
     * @see #setResult(Result)
     * @see #setCancelled(boolean)
     * @return boolean cancellation state
     */
    @Override
    public boolean isCancelled() {
        return getResult() == Result.DENY;
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not be
     * executed in the server, but will still pass to other plugins.
     * <p>
     * Canceling this event will prevent the smash attack.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        setResult(cancel ? Result.DENY : Result.ALLOW);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
