package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Campfire;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import static io.papermc.paper.util.BoundChecker.requireRange;

public class CraftCampfire extends CraftBlockEntityState<CampfireBlockEntity> implements Campfire {

    public CraftCampfire(World world, CampfireBlockEntity blockEntity) {
        super(world, blockEntity);
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

    @Override
    public void stopCooking() {
        for (int i = 0; i < this.getSnapshot().stopCooking.length; ++i)
            this.stopCooking(i);
    }

    @Override
    public void startCooking() {
        for (int i = 0; i < this.getSnapshot().stopCooking.length; ++i)
            this.startCooking(i);
    }

    @Override
    public boolean stopCooking(int index) {
        requireRange(index, "index", 0, 3);
        boolean previous = this.isCookingDisabled(index);
        this.getSnapshot().stopCooking[index] = true;
        return previous;
    }

    @Override
    public boolean startCooking(int index) {
        requireRange(index, "index", 0, 3);
        boolean previous = this.isCookingDisabled(index);
        this.getSnapshot().stopCooking[index] = false;
        return previous;
    }

    @Override
    public boolean isCookingDisabled(int index) {
        requireRange(index, "index", 0, 3);
        return this.getSnapshot().stopCooking[index];
    }
}
