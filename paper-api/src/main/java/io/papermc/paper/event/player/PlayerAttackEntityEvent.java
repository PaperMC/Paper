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
    private PlayerAttackType type;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerAttackEntityEvent(final Player player, final Entity attacked, final PlayerAttackType type) {
        super(player);
        this.attacked = attacked;
        this.type = type;
    }

    /**
     * Gets the entity that was attacked in this event.
     *
     * @return entity that was attacked
     */
    public Entity getAttacked() {
        return this.attacked;
    }

    /**
     * Gets the type of the attack.
     *
     * @return type of the attack
     */
    public PlayerAttackType getType() {
        return this.type;
    }

    /**
     * Sets the type of the attack.
     *
     * @param type type of the attack
     */
    public void setType(final PlayerAttackType type) {
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

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum PlayerAttackType {
        NORMAL, KNOCKBACK, CRIT, SWEEP
    }
}
