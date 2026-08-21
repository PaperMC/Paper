package org.bukkit.event.entity;

import org.bukkit.entity.EnderDragon;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when an EnderDragon switches controller phase.
 */
public interface EnderDragonChangePhaseEvent extends EntityEventNew, Cancellable {

    @Override
    EnderDragon getEntity();

    /**
     * Gets the current phase that the dragon is in. This method will return null
     * when a dragon is first spawned and hasn't yet been assigned a phase.
     *
     * @return the current dragon phase
     */
    EnderDragon.@Nullable Phase getCurrentPhase();

    /**
     * Gets the new phase that the dragon will switch to.
     *
     * @return the new dragon phase
     */
    EnderDragon.Phase getNewPhase();

    /**
     * Sets the new phase for the ender dragon.
     *
     * @param newPhase the new dragon phase
     */
    void setNewPhase(EnderDragon.Phase newPhase);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
