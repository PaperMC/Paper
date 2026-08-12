package org.bukkit.craftbukkit.inventory;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryFurnace extends CraftInventory implements FurnaceInventory {
    public CraftInventoryFurnace(AbstractFurnaceBlockEntity inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getResult() {
        return this.getItem(2);
    }

    @Override
    public ItemStack getFuel() {
        return this.getItem(1);
    }

    @Override
    public ItemStack getSmelting() {
        return this.getItem(0);
    }

    @Override
    public void setFuel(ItemStack stack) {
        this.setItem(1, stack);
    }

    @Override
    public void setResult(ItemStack stack) {
        this.setItem(2, stack);
    }

    @Override
    public void setSmelting(ItemStack stack) {
        this.setItem(0, stack);
    }

    @Override
    public boolean isFuel(ItemStack stack) {
        return stack != null && !stack.isEmpty() && CraftItemStack.asNMSCopy(stack).has(DataComponents.COOKING_FUEL); // TODO - snapshot - replace with Paper DataComponents
    }

    @Override
    public boolean canSmelt(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        net.minecraft.server.level.ServerLevel world = ((org.bukkit.craftbukkit.CraftWorld) org.bukkit.Bukkit.getWorlds().getFirst()).getHandle();
        SingleRecipeInput input = new SingleRecipeInput(CraftItemStack.asNMSCopy(stack));
        return ((AbstractFurnaceBlockEntity) this.inventory).quickCheck.getRecipeFor(input, world).isPresent();
    }

    @Override
    public Furnace getHolder() {
        return (Furnace) this.inventory.getOwner();
    }
}
