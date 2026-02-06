package io.papermc.paper.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.bukkit.inventory.StonecutterInventory;
import org.bukkit.inventory.StonecuttingRecipe;

public interface PlayerStonecutterRecipeSelectEvent extends PlayerEventNew, Cancellable { // todo javadocs?

    StonecutterInventory getStonecutterInventory();

    StonecuttingRecipe getStonecuttingRecipe();

    void setStonecuttingRecipe(StonecuttingRecipe stonecuttingRecipe);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
