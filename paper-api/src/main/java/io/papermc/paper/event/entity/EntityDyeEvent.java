package io.papermc.paper.event.entity;

import org.bukkit.DyeColor;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when an entity is dyed. Currently, this is called for {@link Sheep}
 * being dyed, and {@link Wolf}/{@link Cat} collars being dyed.
 */
@NullMarked
public class EntityDyeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final @Nullable Player player;
    private DyeColor dyeColor;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityDyeEvent(final Entity entity, final DyeColor dyeColor, final @Nullable Player player) {
        super(entity);
        this.dyeColor = dyeColor;
        this.player = player;
    }

    /**
     * Gets the DyeColor the entity is being dyed
     *
     * @return the DyeColor the entity is being dyed
     */
    public DyeColor getColor() {
        return this.dyeColor;
    }

    /**
     * Sets the DyeColor the entity is being dyed
     *
     * @param dyeColor the DyeColor the entity will be dyed
     */
    public void setColor(final DyeColor dyeColor) {
        this.dyeColor = dyeColor;
    }

    /**
     * Returns the player dyeing the entity, if available.
     *
     * @return player or {@code null}
     */
    public @Nullable Player getPlayer() {
        return this.player;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
