package org.bukkit.event.entity;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a sheep's wool is dyed
 */
public class SheepDyeWoolEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private DyeColor color;
    private final Player player;

    @Deprecated
    public SheepDyeWoolEvent(@NotNull final Sheep sheep, @NotNull final DyeColor color) {
        this(sheep, color, null);
    }

    public SheepDyeWoolEvent(@NotNull final Sheep sheep, @NotNull final DyeColor color, @Nullable Player player) {
        super(sheep);
        this.cancel = false;
        this.color = color;
        this.player = player;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    @Override
    public Sheep getEntity() {
        return (Sheep) entity;
    }

    /**
     * Returns the player dyeing the sheep, if available.
     *
     * @return player or null
     */
    @Nullable
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the DyeColor the sheep is being dyed
     *
     * @return the DyeColor the sheep is being dyed
     */
    @NotNull
    public DyeColor getColor() {
        return color;
    }

    /**
     * Sets the DyeColor the sheep is being dyed
     *
     * @param color the DyeColor the sheep will be dyed
     */
    public void setColor(@NotNull DyeColor color) {
        this.color = color;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
