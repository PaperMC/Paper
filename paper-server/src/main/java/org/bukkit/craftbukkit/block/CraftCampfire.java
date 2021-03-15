package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityCampfire;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Campfire;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class CraftCampfire extends CraftBlockEntityState<TileEntityCampfire> implements Campfire {

    public CraftCampfire(Block block) {
        super(block, TileEntityCampfire.class);
    }

    public CraftCampfire(Material material, TileEntityCampfire te) {
        super(material, te);
    }

    @Override
    public int getSize() {
        return getSnapshot().getItems().size();
    }

    @Override
    public ItemStack getItem(int index) {
        net.minecraft.world.item.ItemStack item = getSnapshot().getItems().get(index);
        return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        getSnapshot().getItems().set(index, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public int getCookTime(int index) {
        return getSnapshot().cookingTimes[index];
    }

    @Override
    public void setCookTime(int index, int cookTime) {
        getSnapshot().cookingTimes[index] = cookTime;
    }

    @Override
    public int getCookTimeTotal(int index) {
        return getSnapshot().cookingTotalTimes[index];
    }

    @Override
    public void setCookTimeTotal(int index, int cookTimeTotal) {
        getSnapshot().cookingTotalTimes[index] = cookTimeTotal;
    }
}
