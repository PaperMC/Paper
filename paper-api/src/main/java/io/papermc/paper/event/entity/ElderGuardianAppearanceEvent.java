package io.papermc.paper.event.entity;

import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;

/**
 * Is called when an {@link ElderGuardian} appears in front of a {@link Player}.
 */
public interface ElderGuardianAppearanceEvent extends EntityEvent, PlayerEvent, Cancellable {

    /**
     * Get the player affected by the guardian appearance.
     *
     * @return Player affected by the appearance
     * @deprecated use {@link #getPlayer()}
     */
    @Deprecated(forRemoval = true)
    default Player getAffectedPlayer() {
        return this.getPlayer();
    }

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
