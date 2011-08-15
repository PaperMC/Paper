package org.bukkit.event.player;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a player shears an entity
 */
public class PlayerShearEntityEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final Entity what;
    private final List<ItemStack> items;

    public PlayerShearEntityEvent(final Player who, final Entity what, final List<ItemStack> drops) {
        super(who);
        this.cancel = false;
        this.what = what;
        this.items = drops;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the entity the player is shearing
     *
     * @return the entity the player is shearing
     */
    public Entity getEntity() {
        return what;
    }

    /**
     * Get the items that will drop as a result of the shearing. To change the
     * items dropped, change the contents of this list.
     * @return The list of items.
     */
    public List<ItemStack> getDrops() {
        return items;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
