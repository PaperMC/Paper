package io.papermc.paper.event.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.checkerframework.common.value.qual.IntRange;

/**
 * Called when a Target Block is hit by a projectile.
 * <p>
 * Cancelling this event will stop the Target from emitting a redstone signal,
 * and in the case that the shooter is a player, will stop them from receiving
 * advancement criteria.
 */
public interface TargetHitEvent extends ProjectileHitEvent {

    /**
     * Gets the strength of the redstone signal to be emitted by the Target block
     *
     * @return the strength of the redstone signal to be emitted
     */
    @IntRange(from = 0, to = 15) int getSignalStrength();

    /**
     * Sets the strength of the redstone signal to be emitted by the Target block
     *
     * @param signalStrength the strength of the redstone signal to be emitted
     */
    void setSignalStrength(@IntRange(from = 0, to = 15) int signalStrength);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
