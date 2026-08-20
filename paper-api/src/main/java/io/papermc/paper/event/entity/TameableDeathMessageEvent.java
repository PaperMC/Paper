package io.papermc.paper.event.entity;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Tameable;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

/**
 * Called when a {@link Tameable} dies and sends a death message.
 */
public interface TameableDeathMessageEvent extends EntityEventNew, Cancellable {

    /**
     * Get the death message that appears to the owner of the tameable.
     *
     * @return Death message to appear
     */
    Component deathMessage();

    /**
     * Set the death message that appears to the owner of the tameable.
     *
     * @param deathMessage Death message to appear
     */
    void deathMessage(Component deathMessage);

    @Override
    Tameable getEntity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
