package io.papermc.paper.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.jspecify.annotations.Nullable;

/**
 * Called when the player is attempting to rename a mob
 */
public interface PlayerNameEntityEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the name to be given to the entity.
     *
     * @return the name
     */
    @Nullable Component getName();

    /**
     * Sets the name to be given to the entity.
     *
     * @param name the name
     */
    void setName(@Nullable Component name);

    /**
     * Gets the entity involved in this event.
     *
     * @return the entity
     */
    LivingEntity getEntity();

    /**
     * Sets the entity involved in this event.
     *
     * @param entity the entity
     */
    void setEntity(LivingEntity entity);

    /**
     * Gets whether this will set the mob to be persistent.
     *
     * @return persistent
     */
    boolean isPersistent();

    /**
     * Sets whether this will set the mob to be persistent.
     *
     * @param persistent persistent
     */
    void setPersistent(boolean persistent);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
