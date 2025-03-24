package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This event is fired when the player activates the riptide enchantment, using
 * their trident to propel them through the air.
 * <br>
 * N.B. the riptide action is currently performed client side, so manipulating
 * the player in this event may have undesired effects.
 */
public class PlayerRiptideEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final ItemStack item;
    private final Vector velocity;

    @ApiStatus.Internal
    public PlayerRiptideEvent(@NotNull final Player who, @NotNull final ItemStack item, @NotNull Vector velocity) {
        super(who);
        this.item = item;
        this.velocity = velocity;
    }

    @Deprecated(since = "1.20.4", forRemoval = true)
    public PlayerRiptideEvent(@NotNull final Player who, @NotNull final ItemStack item) {
        this(who, item, new Vector());
    }

    /**
     * Gets the item containing the used enchantment.
     *
     * @return held enchanted item
     */
    @NotNull
    public ItemStack getItem() {
        return item;
    }

    /**
     * Get the velocity applied to the player as a result of this riptide.
     *
     * @return the riptide velocity
     */
    @NotNull
    public Vector getVelocity() {
        return velocity.clone();
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
