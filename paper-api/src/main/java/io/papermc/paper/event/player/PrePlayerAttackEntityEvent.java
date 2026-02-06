package io.papermc.paper.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Called when the player tries to attack an entity.
 * <p>
 * This occurs before any of the damage logic, so cancelling this event
 * will prevent any sort of sounds from being played when attacking.
 * <p>
 * This event will fire as cancelled for certain entities, with {@link PrePlayerAttackEntityEvent#willAttack()} being false
 * to indicate that this entity will not actually be attacked.
 * <p>
 * Note: there may be other factors (invulnerability, etc.) that will prevent this entity from being attacked that this event will not cover
 */
public interface PrePlayerAttackEntityEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the entity that was attacked in this event.
     *
     * @return entity that was attacked
     */
    Entity getAttacked();

    /**
     * Gets if this entity will be attacked normally.
     * Entities like falling sand will return {@code false} because
     * their entity type does not allow them to be attacked.
     * <p>
     * Note: there may be other factors (invulnerability, etc.) that will prevent this entity from being attacked that this event will not cover
     *
     * @return if the entity will actually be attacked
     */
    boolean willAttack();

    /**
     * {@inheritDoc}
     * <p>
     * Sets if this attack should be cancelled, note if {@link PrePlayerAttackEntityEvent#willAttack()} returns {@code false}
     * this event will always be cancelled.
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
