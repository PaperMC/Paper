package io.papermc.paper.event.entity;

import org.bukkit.DyeColor;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.jspecify.annotations.Nullable;

/**
 * Called when an entity is dyed. Currently, this is called for {@link Sheep}
 * being dyed, and {@link Wolf}/{@link Cat} collars being dyed.
 */
public interface EntityDyeEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the DyeColor the entity is being dyed
     *
     * @return the DyeColor the entity is being dyed
     */
    DyeColor getColor();

    /**
     * Sets the DyeColor the entity is being dyed
     *
     * @param dyeColor the DyeColor the entity will be dyed
     */
    void setColor(DyeColor dyeColor);

    /**
     * Returns the player dyeing the entity, if available.
     *
     * @return player or {@code null}
     */
    @Nullable Player getPlayer();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
