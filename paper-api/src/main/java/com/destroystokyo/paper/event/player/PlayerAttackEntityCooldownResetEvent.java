package com.destroystokyo.paper.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;

/**
 * Called when processing a player's attack on an entity when the player's attack strength cooldown is reset.
 *
 * @deprecated In modern Minecraft, this does not properly represent spear attacks and additionally does not
 * cover the wide range of ways that the cooldown may be reset.
 * <p>
 * Additionally, the cancellation of event is misleading as the client will reset the cooldown manually.
 * So there is no way for the server to control this.
 */
@Deprecated(since = "26.1", forRemoval = true)
public interface PlayerAttackEntityCooldownResetEvent extends PlayerEventNew, Cancellable {

    /**
     * Returns the entity attacked by the player
     *
     * @return the entity attacked by the player
     */
    Entity getAttackedEntity();

    /**
     * Get the value of the players cooldown attack strength when they initiated the attack
     *
     * @return returns the original player cooldown value
     */
    float getCooledAttackStrength();

    /**
     * {@inheritDoc}
     * <p>
     * If an attack cooldown event is cancelled, the players attack strength will remain at the same value instead of being reset.
     */
    @Override
    boolean isCancelled();

    /**
     * {@inheritDoc}
     * <p>
     * Cancelling this event will prevent the target player from having their cooldown reset from attacking this entity
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
