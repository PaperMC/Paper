package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.ChestBlock;
import org.bukkit.Location;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryDoubleChest extends CraftInventory implements DoubleChestInventory {
    public MenuProvider tile;
    private final CraftInventory left;
    private final CraftInventory right;

    public CraftInventoryDoubleChest(ChestBlock.DoubleInventory block) {
        super(block.inventorylargechest);
        this.tile = block;
        this.left = new CraftInventory(block.inventorylargechest.container1);
        this.right = new CraftInventory(block.inventorylargechest.container2);
    }

    public CraftInventoryDoubleChest(CompoundContainer largeChest) {
        super(largeChest);
        if (largeChest.container1 instanceof CompoundContainer) {
            this.left = new CraftInventoryDoubleChest((CompoundContainer) largeChest.container1);
        } else {
            this.left = new CraftInventory(largeChest.container1);
        }
        if (largeChest.container2 instanceof CompoundContainer) {
            this.right = new CraftInventoryDoubleChest((CompoundContainer) largeChest.container2);
        } else {
            this.right = new CraftInventory(largeChest.container2);
        }
    }

    @Override
    public Inventory getLeftSide() {
        return this.left;
    }

    @Override
    public Inventory getRightSide() {
        return this.right;
    }

    @Override
    public void setContents(ItemStack[] items) {
        Preconditions.checkArgument(items.length <= this.getInventory().getContainerSize(), "Invalid inventory size (%s); expected %s or less", items.length, this.getInventory().getContainerSize());
        ItemStack[] leftItems = new ItemStack[this.left.getSize()], rightItems = new ItemStack[this.right.getSize()];
        System.arraycopy(items, 0, leftItems, 0, Math.min(this.left.getSize(), items.length));
        this.left.setContents(leftItems);
        if (items.length >= this.left.getSize()) {
            System.arraycopy(items, this.left.getSize(), rightItems, 0, Math.min(this.right.getSize(), items.length - this.left.getSize()));
            this.right.setContents(rightItems);
        }
    }

    @Override
    public DoubleChest getHolder() {
        return new DoubleChest(this);
    }

    @Override
    public Location getLocation() {
        return this.getLeftSide().getLocation().add(this.getRightSide().getLocation()).multiply(0.5);
    }
}
