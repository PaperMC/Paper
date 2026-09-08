package io.papermc.paper.event.player;

import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.StonecutterInventory;
import org.bukkit.inventory.StonecuttingRecipe;

public class PaperPlayerStonecutterRecipeSelectEvent extends CraftPlayerEvent implements PlayerStonecutterRecipeSelectEvent {

    private final StonecutterInventory stonecutterInventory;
    private StonecuttingRecipe stonecuttingRecipe;

    private boolean cancelled;

    public PaperPlayerStonecutterRecipeSelectEvent(final Player player, final StonecutterInventory stonecutterInventory, final StonecuttingRecipe stonecuttingRecipe) {
        super(player);
        this.stonecutterInventory = stonecutterInventory;
        this.stonecuttingRecipe = stonecuttingRecipe;
    }

    public PaperPlayerStonecutterRecipeSelectEvent(final net.minecraft.world.entity.player.Player player, final StonecutterMenu menu, final RecipeHolder<?> recipe) {
        this((Player) player.getBukkitEntity(), menu.getBukkitView().getTopInventory(), (StonecuttingRecipe) recipe.toBukkitRecipe());
    }

    @Override
    public StonecutterInventory getStonecutterInventory() {
        return this.stonecutterInventory;
    }

    @Override
    public StonecuttingRecipe getStonecuttingRecipe() {
        return this.stonecuttingRecipe;
    }

    @Override
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
        return PlayerStonecutterRecipeSelectEvent.getHandlerList();
    }
}
