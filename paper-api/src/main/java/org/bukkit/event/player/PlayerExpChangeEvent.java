package org.bukkit.event.player;

import org.bukkit.entity.Entity; // Paper
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable; // Paper

/**
 * Called when a players experience changes naturally
 */
public class PlayerExpChangeEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    // Paper start
    @Nullable
    private final Entity source;
    private int exp;

    public PlayerExpChangeEvent(@NotNull final Player player, final int expAmount) {
          this(player, null, expAmount);
    }

    public PlayerExpChangeEvent(@NotNull final Player player, @Nullable final Entity sourceEntity, final int expAmount) {
         super(player);
         source = sourceEntity;
         exp = expAmount;
    }

    /**
     * Get the source that provided the experience.
     *
     * @return The source of the experience
     */
    @Nullable
    public Entity getSource() {
        return source;
    }
    // Paper end

    /**
     * Get the amount of experience the player will receive
     *
     * @return The amount of experience
     */
    public int getAmount() {
        return exp;
    }

    /**
     * Set the amount of experience the player will receive
     *
     * @param amount The amount of experience to set
     */
    public void setAmount(int amount) {
        exp = amount;
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
