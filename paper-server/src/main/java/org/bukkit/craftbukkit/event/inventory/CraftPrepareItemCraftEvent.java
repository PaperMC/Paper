package org.bukkit.craftbukkit.event.inventory;

import java.util.Optional;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;
import org.jspecify.annotations.Nullable;

public class CraftPrepareItemCraftEvent extends CraftInventoryEvent implements PrepareItemCraftEvent {

    private final boolean repair;
    private final CraftingInventory matrix;

    public CraftPrepareItemCraftEvent(final CraftingInventory matrix, final InventoryView view, final boolean isRepair) {
        super(view);
        this.matrix = matrix;
        this.repair = isRepair;
    }

    public CraftPrepareItemCraftEvent(
        final CraftingContainer container, final Container resultSlots, final ItemStack result, final AbstractContainerMenu menu, final Optional<RecipeHolder<CraftingRecipe>> recipe
    ) {
        final CraftInventoryCrafting matrix = new CraftInventoryCrafting(container, resultSlots);
        matrix.setResult(CraftItemStack.asCraftMirror(result));
        this(matrix, menu.getBukkitView(), recipe.map(RecipeHolder::value).orElse(null) instanceof RepairItemRecipe);
    }

    @Override
    public @Nullable Recipe getRecipe() {
        return this.matrix.getRecipe();
    }

    @Override
    public CraftingInventory getInventory() {
        return this.matrix;
    }

    @Override
    public boolean isRepair() {
        return this.repair;
    }

    @Override
    public HandlerList getHandlers() {
        return PrepareItemCraftEvent.getHandlerList();
    }
}
