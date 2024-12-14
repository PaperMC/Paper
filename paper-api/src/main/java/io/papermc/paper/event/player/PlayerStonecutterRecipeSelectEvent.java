package io.papermc.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.StonecutterInventory;
import org.bukkit.inventory.StonecuttingRecipe;
import org.jspecify.annotations.NullMarked;

/**
 * @since 1.16.5
 */
@NullMarked
public class PlayerStonecutterRecipeSelectEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final StonecutterInventory stonecutterInventory;
    private StonecuttingRecipe stonecuttingRecipe;

    private boolean cancelled;

    public PlayerStonecutterRecipeSelectEvent(final Player player, final StonecutterInventory stonecutterInventory, final StonecuttingRecipe stonecuttingRecipe) {
        super(player);
        this.stonecutterInventory = stonecutterInventory;
        this.stonecuttingRecipe = stonecuttingRecipe;
    }

    public StonecutterInventory getStonecutterInventory() {
        return this.stonecutterInventory;
    }

    public StonecuttingRecipe getStonecuttingRecipe() {
        return this.stonecuttingRecipe;
    }

    public void setStonecuttingRecipe(final StonecuttingRecipe stonecuttingRecipe) {
        this.stonecuttingRecipe = stonecuttingRecipe;
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
