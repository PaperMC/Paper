package org.bukkit.craftbukkit.block;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.inventory.CraftInventoryFurnace;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Recipe;

public abstract class CraftFurnace<T extends AbstractFurnaceBlockEntity> extends CraftContainer<T> implements Furnace {

    public CraftFurnace(World world, T blockEntity) {
        super(world, blockEntity);
    }

    protected CraftFurnace(CraftFurnace<T> state, Location location) {
        super(state, location);
    }

    @Override
    public FurnaceInventory getSnapshotInventory() {
        return new CraftInventoryFurnace(this.getSnapshot());
    }

    @Override
    public FurnaceInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryFurnace(this.getBlockEntity());
    }

    @Override
    public short getBurnTime() {
        return (short) this.getSnapshot().litTimeRemaining;
    }

    @Override
    public void setBurnTime(short burnTime) {
        this.getSnapshot().litTimeRemaining = burnTime;
        this.data = this.data.trySetValue(AbstractFurnaceBlock.LIT, burnTime > 0);
        // only try, block data might have changed to something different that would not allow this property
    }

    @Override
    public short getCookTime() {
        return (short) this.getSnapshot().cookingTimer;
    }

    @Override
    public void setCookTime(short cookTime) {
        this.getSnapshot().cookingTimer = cookTime;
    }

    @Override
    public int getCookTimeTotal() {
        return this.getSnapshot().cookingTotalTime;
    }

    @Override
    public void setCookTimeTotal(int cookTimeTotal) {
        this.getSnapshot().cookingTotalTime = cookTimeTotal;
    }

    @Override
    public Map<CookingRecipe<?>, Integer> getRecipesUsed() {
        ImmutableMap.Builder<CookingRecipe<?>, Integer> recipesUsed = ImmutableMap.builder();
        this.getSnapshot().recipesUsed.reference2IntEntrySet().fastForEach(entrySet -> {
            Recipe recipe = Bukkit.getRecipe(CraftNamespacedKey.fromMinecraft(entrySet.getKey().location()));
            if (recipe instanceof CookingRecipe<?> cookingRecipe) {
                recipesUsed.put(cookingRecipe, entrySet.getValue());
            }
        });

        return recipesUsed.build();
    }

    @Override
    public abstract CraftFurnace<T> copy();

    @Override
    public abstract CraftFurnace<T> copy(Location location);

    // Paper start - cook speed multiplier API
    @Override
    public double getCookSpeedMultiplier() {
        return this.getSnapshot().cookSpeedMultiplier;
    }

    @Override
    public void setCookSpeedMultiplier(double multiplier) {
        com.google.common.base.Preconditions.checkArgument(multiplier >= 0, "Furnace speed multiplier cannot be negative");
        com.google.common.base.Preconditions.checkArgument(multiplier <= 200, "Furnace speed multiplier cannot more than 200");
        T snapshot = this.getSnapshot();
        snapshot.cookSpeedMultiplier = multiplier;
        snapshot.cookingTotalTime = AbstractFurnaceBlockEntity.getTotalCookTime(this.isPlaced() ? this.world.getHandle() : null, snapshot, snapshot.recipeType, snapshot.cookSpeedMultiplier); // Update the snapshot's current total cook time to scale with the newly set multiplier
    }

    @Override
    public int getRecipeUsedCount(org.bukkit.NamespacedKey furnaceRecipe) {
        return this.getSnapshot().recipesUsed.getInt(io.papermc.paper.util.MCUtil.toResourceKey(net.minecraft.core.registries.Registries.RECIPE, furnaceRecipe));
    }

    @Override
    public boolean hasRecipeUsedCount(org.bukkit.NamespacedKey furnaceRecipe) {
        return this.getSnapshot().recipesUsed.containsKey(io.papermc.paper.util.MCUtil.toResourceKey(net.minecraft.core.registries.Registries.RECIPE, furnaceRecipe));
    }

    @Override
    public void setRecipeUsedCount(org.bukkit.inventory.CookingRecipe<?> furnaceRecipe, int count) {
        final var location = io.papermc.paper.util.MCUtil.toResourceKey(net.minecraft.core.registries.Registries.RECIPE, furnaceRecipe.getKey());
        java.util.Optional<net.minecraft.world.item.crafting.RecipeHolder<?>> nmsRecipe = (this.isPlaced() ? this.world.getHandle().recipeAccess() : net.minecraft.server.MinecraftServer.getServer().getRecipeManager()).byKey(location);
        com.google.common.base.Preconditions.checkArgument(nmsRecipe.isPresent() && nmsRecipe.get().value() instanceof net.minecraft.world.item.crafting.AbstractCookingRecipe, furnaceRecipe.getKey() + " is not recognized as a valid and registered furnace recipe");
        if (count > 0) {
            this.getSnapshot().recipesUsed.put(location, count);
        } else {
            this.getSnapshot().recipesUsed.removeInt(location);
        }
    }

    @Override
    public void setRecipesUsed(java.util.Map<org.bukkit.inventory.CookingRecipe<?>, Integer> recipesUsed) {
        this.getSnapshot().recipesUsed.clear();
        recipesUsed.forEach((recipe, integer) -> {
            if (integer != null) {
                this.setRecipeUsedCount(recipe, integer);
            }
        });
    }
    // Paper end
}
