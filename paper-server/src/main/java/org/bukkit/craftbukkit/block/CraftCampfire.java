package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Campfire;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class CraftCampfire extends CraftBlockEntityState<CampfireBlockEntity> implements Campfire {

    public CraftCampfire(World world, CampfireBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftCampfire(CraftCampfire state, Location location) {
        super(state, location);
    }

    @Override
    public int getSize() {
        return this.getSnapshot().getItems().size();
    }

    @Override
    public ItemStack getItem(int index) {
        net.minecraft.world.item.ItemStack item = this.getSnapshot().getItems().get(index);
        return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        this.getSnapshot().getItems().set(index, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public int getCookTime(int index) {
        return this.getSnapshot().cookingProgress[index];
    }

    @Override
    public void setCookTime(int index, int cookTime) {
        this.getSnapshot().cookingProgress[index] = cookTime;
    }

    @Override
    public int getCookTimeTotal(int index) {
        return this.getSnapshot().cookingTime[index];
    }

    @Override
    public void setCookTimeTotal(int index, int cookTimeTotal) {
        this.getSnapshot().cookingTime[index] = cookTimeTotal;
    }

    @Override
    public CraftCampfire copy() {
        return new CraftCampfire(this, null);
    }

    @Override
    public CraftCampfire copy(Location location) {
        return new CraftCampfire(this, location);
    }
}
