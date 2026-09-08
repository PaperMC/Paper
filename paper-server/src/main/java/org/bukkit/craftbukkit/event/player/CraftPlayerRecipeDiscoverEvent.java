package org.bukkit.craftbukkit.event.player;

import net.minecraft.world.item.crafting.RecipeHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;

public class CraftPlayerRecipeDiscoverEvent extends CraftPlayerEvent implements PlayerRecipeDiscoverEvent {

    private final NamespacedKey recipe;
    private boolean cancelled;
    private boolean showNotification;

    public CraftPlayerRecipeDiscoverEvent(final Player player, final NamespacedKey recipe, final boolean showNotification) {
        super(player);
        this.recipe = recipe;
        this.showNotification = showNotification;
    }

    public CraftPlayerRecipeDiscoverEvent(final net.minecraft.world.entity.player.Player player, final RecipeHolder<?> recipe) {
        this((Player) player.getBukkitEntity(), CraftNamespacedKey.fromMinecraft(recipe.id().identifier()), recipe.value().showNotification());
    }

    @Override
    public NamespacedKey getRecipe() {
        return this.recipe;
    }

    @Override
    public boolean shouldShowNotification() {
        return this.showNotification;
    }

    @Override
    public void shouldShowNotification(final boolean showNotification) {
        this.showNotification = showNotification;
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
        return PlayerRecipeDiscoverEvent.getHandlerList();
    }
}
