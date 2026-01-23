package org.bukkit.event.player;

import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

/**
 * Represents an event that is called when a player right clicks an entity that
 * also contains the location where the entity was clicked.
 * <br>
 * Note that the client may sometimes spuriously send this packet in addition to {@link PlayerInteractEntityEvent}.
 * Users are advised to listen to this (parent) class unless specifically required.
 * <br>
 * Note that interacting with Armor Stands fires this event only and not its parent and as such users are expressly required
 * to listen to this event for that scenario.
 */
public interface PlayerInteractAtEntityEvent extends PlayerInteractEntityEvent {

    // todo javadocs?
    Vector getClickedPosition();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
