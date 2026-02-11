package io.papermc.paper.event.entity;

import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

/**
 * Is called when an {@link ElderGuardian} appears in front of a {@link Player}.
 */
public interface ElderGuardianAppearanceEvent extends EntityEventNew, Cancellable {

    /**
     * Get the player affected by the guardian appearance.
     *
     * @return Player affected by the appearance
     */
    Player getAffectedPlayer();

    /**
     * The elder guardian playing the effect.
     *
     * @return The elder guardian
     */
    @Override
    ElderGuardian getEntity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
