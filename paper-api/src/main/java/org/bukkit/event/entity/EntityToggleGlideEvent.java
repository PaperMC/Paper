package org.bukkit.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Sent when an entity's gliding status is toggled with an Elytra.
 * Examples of when this event would be called:
 * <ul>
 *     <li>Player presses the jump key while in midair and using an Elytra</li>
 *     <li>Player lands on ground while they are gliding (with an Elytra)</li>
 * </ul>
 * This can be visually estimated by the animation in which a player turns horizontal.
 */
public interface EntityToggleGlideEvent extends EntityEventNew, Cancellable {

    /**
     * Returns {@code true} if the entity is now gliding or
     * {@code false} if the entity stops gliding.
     *
     * @return new gliding state
     */
    boolean isGliding();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
