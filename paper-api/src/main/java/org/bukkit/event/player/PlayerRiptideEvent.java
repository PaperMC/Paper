package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
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

    public PlayerRiptideEvent(@NotNull final Player who, @NotNull final ItemStack item) {
        super(who);
        this.item = item;
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
