package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a players experience changes naturally
 */
public class PlayerExpChangeEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Nullable
    private final Entity source;
    private int exp;

    @ApiStatus.Internal
    public PlayerExpChangeEvent(@NotNull final Player player, final int expAmount) {
        this(player, null, expAmount);
    }

    @ApiStatus.Internal
    public PlayerExpChangeEvent(@NotNull final Player player, @Nullable final Entity sourceEntity, final int expAmount) {
        super(player);
        this.source = sourceEntity;
        this.exp = expAmount;
    }

    /**
     * Get the source that provided the experience.
     *
     * @return The source of the experience
     */
    @Nullable
    public Entity getSource() {
        return this.source;
    }

    /**
     * Get the amount of experience the player will receive
     *
     * @return The amount of experience
     */
    public int getAmount() {
        return this.exp;
    }

    /**
     * Set the amount of experience the player will receive
     *
     * @param amount The amount of experience to set
     */
    public void setAmount(int amount) {
        this.exp = amount;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
