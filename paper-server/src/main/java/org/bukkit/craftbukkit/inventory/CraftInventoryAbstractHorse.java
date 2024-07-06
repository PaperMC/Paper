package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryAbstractHorse extends CraftInventory implements AbstractHorseInventory {

    // Paper start - combine both horse inventories
    private final Container bodyArmor;
    public CraftInventoryAbstractHorse(Container inventory, final Container bodyArmor) {
        super(inventory);
        this.bodyArmor = bodyArmor;
        // Paper end - combine both horse inventories
    }

    @Override
    public ItemStack getSaddle() {
        return this.getItem(net.minecraft.world.entity.animal.horse.AbstractHorse.INV_SLOT_SADDLE); // Paper
    }

    @Override
    public void setSaddle(ItemStack stack) {
        this.setItem(net.minecraft.world.entity.animal.horse.AbstractHorse.INV_SLOT_SADDLE, stack); // Paper
    }

    // Paper start - combine both horse inventories
    public Container getMainInventory() {
        return this.inventory;
    }

    public Container getArmorInventory() {
        return this.bodyArmor;
    }

    public ItemStack getArmor() {
        return this.getItem(net.minecraft.world.inventory.HorseInventoryMenu.SLOT_BODY_ARMOR);
    }

    public void setArmor(ItemStack armor) {
        this.setItem(net.minecraft.world.inventory.HorseInventoryMenu.SLOT_BODY_ARMOR, armor);
    }

    @Override
    public int getSize() {
        return this.getMainInventory().getContainerSize() + this.getArmorInventory().getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return this.getMainInventory().isEmpty() && this.getArmorInventory().isEmpty();
    }

    @Override
    public ItemStack[] getContents() {
        ItemStack[] items = new ItemStack[this.getSize()];

        items[net.minecraft.world.entity.animal.horse.AbstractHorse.INV_SLOT_SADDLE] = this.getSaddle();
        items[net.minecraft.world.inventory.HorseInventoryMenu.SLOT_BODY_ARMOR] = this.getArmor();

        for (int i = net.minecraft.world.inventory.HorseInventoryMenu.SLOT_BODY_ARMOR + 1; i < items.length; i++) {
            net.minecraft.world.item.ItemStack item = this.getMainInventory().getItem(i - 1);
            items[i] = item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        }

        return items;
    }

    @Override
    public void setContents(ItemStack[] items) {
        com.google.common.base.Preconditions.checkArgument(items.length <= this.getSize(), "Invalid inventory size (%s); expected %s or less", items.length, this.getSize());

        this.setSaddle(org.apache.commons.lang3.ArrayUtils.get(items, net.minecraft.world.entity.animal.horse.AbstractHorse.INV_SLOT_SADDLE));
        this.setArmor(org.apache.commons.lang3.ArrayUtils.get(items, net.minecraft.world.inventory.HorseInventoryMenu.SLOT_BODY_ARMOR));

        for (int i = net.minecraft.world.inventory.HorseInventoryMenu.SLOT_BODY_ARMOR + 1; i < this.getSize(); i++) {
            net.minecraft.world.item.ItemStack item = i >= items.length ? net.minecraft.world.item.ItemStack.EMPTY : CraftItemStack.asNMSCopy(items[i]);
            this.getMainInventory().setItem(i - 1, item);
        }
    }

    @Override
    public ItemStack getItem(final int index) {
        if (index == net.minecraft.world.inventory.HorseInventoryMenu.SLOT_BODY_ARMOR) {
            final net.minecraft.world.item.ItemStack item = this.getArmorInventory().getItem(0);
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        } else {
            int shiftedIndex = index;
            if (index > net.minecraft.world.inventory.HorseInventoryMenu.SLOT_BODY_ARMOR) {
                shiftedIndex--;
            }

            final net.minecraft.world.item.ItemStack item = this.getMainInventory().getItem(shiftedIndex);
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(final int index, final ItemStack item) {
        if (index == net.minecraft.world.inventory.HorseInventoryMenu.SLOT_BODY_ARMOR) {
            this.getArmorInventory().setItem(0, CraftItemStack.asNMSCopy(item));
        } else {
            int shiftedIndex = index;
            if (index > net.minecraft.world.inventory.HorseInventoryMenu.SLOT_BODY_ARMOR) {
                shiftedIndex--;
            }
            this.getMainInventory().setItem(shiftedIndex, CraftItemStack.asNMSCopy(item));
        }
    }
    // Paper end - combine both horse inventories
}
