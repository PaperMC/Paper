package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerRecipeBookClickEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerRecipeBookClickEvent extends CraftPlayerEvent implements PlayerRecipeBookClickEvent {

    private NamespacedKey recipe;
    private boolean makeAll;

    private boolean cancelled;

    public PaperPlayerRecipeBookClickEvent(final Player player, final NamespacedKey recipe, final boolean makeAll) {
        super(player);
        this.recipe = recipe;
        this.makeAll = makeAll;
    }

    @Override
    public NamespacedKey getRecipe() {
        return this.recipe;
    }

    @Override
    public void setRecipe(final NamespacedKey recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean isMakeAll() {
        return this.makeAll;
    }

    @Override
    public void setMakeAll(final boolean makeAll) {
        this.makeAll = makeAll;
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
        return PlayerRecipeBookClickEvent.getHandlerList();
    }
}
