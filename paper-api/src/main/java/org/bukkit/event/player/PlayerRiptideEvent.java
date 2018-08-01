package org.bukkit.event.player;

import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * This event is fired when the player activates the riptide enchantment, using
 * their trident to propel them through the air.
 * <br>
 * N.B. the riptide action is currently performed client side, so manipulating
 * the player in this event may have undesired effects
 *
 * @deprecated draft API
 */
@Deprecated
@Warning(false)
public class PlayerRiptideEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final ItemStack item;

    public PlayerRiptideEvent(final Player who, final ItemStack item) {
        super(who);
        this.item = item;
    }

    /**
     * Gets the item containing the used enchantment.
     *
     * @return held enchanted item
     */
    public ItemStack getItem() {
        return item;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
