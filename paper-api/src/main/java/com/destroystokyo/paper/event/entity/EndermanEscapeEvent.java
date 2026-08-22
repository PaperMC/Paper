package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Enderman;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

public interface EndermanEscapeEvent extends EntityEventNew, Cancellable {

    @Override
    Enderman getEntity();

    /**
     * Gets the reason the enderman is trying to escape.
     *
     * @return The reason
     */
    Reason getReason();

    /**
     * Cancels the escape.
     * <p>
     * If this escape normally had resulted in damage avoidance such as indirect,
     * the enderman will now take damage. However, this does not change the Enderman's
     * innate immunities or damage behavior like arrows where the damage never happens.
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum Reason {
        /**
         * The enderman has stopped attacking and ran away
         */
        RUNAWAY,
        /**
         * The enderman has teleported away due to indirect damage (ranged)
         */
        INDIRECT,
        /**
         * The enderman has teleported away due to a critical hit
         */
        CRITICAL_HIT,
        /**
         * The enderman has teleported away due to the player staring at it during combat
         */
        STARE,
        /**
         * Specific case for {@link #CRITICAL_HIT} where the enderman is taking damage by drowning (ex: rain)
         */
        DROWN
    }
}
