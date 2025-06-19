package io.papermc.paper.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;


/**
 * Called when the player attacks an entity.
 * <p>
 * This event is not called if {@link PrePlayerAttackEntityEvent} is cancelled.
 * <p>
 * Cancelling this event results in an attack with no damage, like when hitting an invulnerable entity. To cancel the damage completely, use {@link PrePlayerAttackEntityEvent}.
 */
public class PlayerAttackEntityEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity attacked;
    private boolean hasExtraKnockback;
    private PlayerAttackType type;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerAttackEntityEvent(final Player player, final Entity attacked, final boolean hasExtraKnockback, final PlayerAttackType type) {
        super(player);
        this.attacked = attacked;
        this.hasExtraKnockback = hasExtraKnockback;
        this.type = type;
    }

    /**
     * Gets the entity that was attacked in this event.
     *
     * @return entity that was attacked
     */
    public @NotNull Entity getAttacked() {
        return this.attacked;
    }

    /**
     * Gets whether the attack has extra knockback.
     * <p>
     * A value of true indicates that the knockback of this attack be increased by the equivalent of a level of the knockback enchantment. This occurs in vanilla when a player attacks while they are sprinting and their attack cooldown completion is greater than 90%
     *
     * @return whether the attack has extra knockback
     */
    public boolean hasExtraKnockback() {
        return this.hasExtraKnockback;
    }

    /**
     * Sets whether the attack has extra knockback.
     * <p>
     * A value of true indicates that the knockback of this attack be increased by the equivalent of a level of the knockback enchantment. This occurs in vanilla when a player attacks while they are sprinting and their attack cooldown completion is greater than 90%
     *
     * @param hasExtraKnockback whether the attack has extra knockback
     */
    public void setExtraKnockback(final boolean hasExtraKnockback) {
        this.hasExtraKnockback = hasExtraKnockback;
    }

    /**
     * Gets the type of the attack.
     *
     * @return type of the attack
     */
    public @NotNull PlayerAttackType getType() {
        return this.type;
    }

    /**
     * Sets the type of the attack.
     *
     * @param type type of the attack
     */
    public void setType(final @NotNull PlayerAttackType type) {
        this.type = type;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum PlayerAttackType {
        NORMAL, CRIT, SWEEP
    }
}
