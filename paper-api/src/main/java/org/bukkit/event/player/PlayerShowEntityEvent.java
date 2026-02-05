package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when a hidden entity is shown to a player.
 * <br>
 * This event is only called when the entity's visibility status is actually
 * changed.
 * <br>
 * This event is called regardless of whether the entity was within tracking
 * range.
 *
 * @see Player#showEntity(org.bukkit.plugin.Plugin, Entity)
 */
public interface PlayerShowEntityEvent extends PlayerEventNew {

    /**
     * Gets the entity which has been shown to the player.
     *
     * @return the shown entity
     */
    Entity getEntity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
