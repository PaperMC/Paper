package io.papermc.paper.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/// Called when an entity attempts to perform a smash attack.
@NullMarked
public class EntityAttemptSmashAttackEvent extends EntityEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LivingEntity target;
    private final ItemStack weapon;
    private final boolean originalResult;
    private Result result = Result.DEFAULT;

    @ApiStatus.Internal
    public EntityAttemptSmashAttackEvent(
        final LivingEntity attacker,
        final LivingEntity target,
        final ItemStack weapon,
        final boolean originalResult
    ) {
        super(attacker);
        this.target = target;
        this.weapon = weapon;
        this.originalResult = originalResult;
    }

    /// Yields the target of the attempted smash attack.
    ///
    /// @return the target entity
    public LivingEntity getTarget() {
        return target;
    }

    /// Yields a copy of the itemstack used in the smash attack attempt.
    ///
    /// @return the itemstack
    public ItemStack getWeapon() {
        return weapon.clone();
    }

    /// Yields the original result the server computed.
    ///
    /// @return `true` if this attempt would have been successful by vanilla's logic, `false` otherwise.
    public boolean getOriginalResult() {
        return originalResult;
    }

    /// Yields the effective result of this event.
    /// The result may take one of three values:
    ///
    ///   - [Result#ALLOW]: The attempt will succeed.
    ///   - [Result#DENY]: The attempt will fail.
    ///   - [Result#DEFAULT]: The attempt will succeed if [#getOriginalResult()] is `true` and fail otherwise.
    ///
    /// @return the result.
    public Result getResult() {
        return this.result;
    }

    /// Configures a new result for this event.
    /// The passes result may take one of three values:
    ///
    ///   - [Result#ALLOW]: The attempt will succeed.
    ///   - [Result#DENY]: The attempt will fail.
    ///   - [Result#DEFAULT]: The attempt will succeed if [#getOriginalResult()] is `true` and fail otherwise.
    ///
    /// @param result the new result of the event.
    public void setResult(final Result result) {
        this.result = result;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
