package org.bukkit.craftbukkit.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.ChatComponentText;

import net.minecraft.server.IChatBaseComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NonNullList;

public class CraftInventoryCustom extends CraftInventory {
    public CraftInventoryCustom(InventoryHolder owner, InventoryType type) {
        super(new MinecraftInventory(owner, type));
    }

    public CraftInventoryCustom(InventoryHolder owner, InventoryType type, String title) {
        super(new MinecraftInventory(owner, type, title));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size) {
        super(new MinecraftInventory(owner, size));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size, String title) {
        super(new MinecraftInventory(owner, size, title));
    }

    static class MinecraftInventory implements IInventory {
        private final NonNullList<ItemStack> items;
        private int maxStack = MAX_STACK;
        private final List<HumanEntity> viewers;
        private final String title;
        private InventoryType type;
        private final InventoryHolder owner;

        public MinecraftInventory(InventoryHolder owner, InventoryType type) {
            this(owner, type.getDefaultSize(), type.getDefaultTitle());
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, InventoryType type, String title) {
            this(owner, type.getDefaultSize(), title);
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, int size) {
            this(owner, size, "Chest");
        }

        public MinecraftInventory(InventoryHolder owner, int size, String title) {
            Validate.notNull(title, "Title cannot be null");
            this.items = NonNullList.a(size, ItemStack.a);
            this.title = title;
            this.viewers = new ArrayList<HumanEntity>();
            this.owner = owner;
            this.type = InventoryType.CHEST;
        }

        public int getSize() {
            return items.size();
        }

        public ItemStack getItem(int i) {
            return items.get(i);
        }

        public ItemStack splitStack(int i, int j) {
            ItemStack stack = this.getItem(i);
            ItemStack result;
            if (stack == ItemStack.a) return stack;
            if (stack.getCount() <= j) {
                this.setItem(i, ItemStack.a);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, j);
                stack.subtract(j);
            }
            this.update();
            return result;
        }

        public ItemStack splitWithoutUpdate(int i) {
            ItemStack stack = this.getItem(i);
            ItemStack result;
            if (stack == ItemStack.a) return stack;
            if (stack.getCount() <= 1) {
                this.setItem(i, null);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, 1);
                stack.subtract(1);
            }
            return result;
        }

        public void setItem(int i, ItemStack itemstack) {
            items.set(i, itemstack);
            if (itemstack != ItemStack.a && this.getMaxStackSize() > 0 && itemstack.getCount() > this.getMaxStackSize()) {
                itemstack.setCount(this.getMaxStackSize());
            }
        }

        public int getMaxStackSize() {
            return maxStack;
        }

        public void setMaxStackSize(int size) {
            maxStack = size;
        }

        public void update() {}

        public boolean a(EntityHuman entityhuman) {
            return true;
        }

        public List<ItemStack> getContents() {
            return items;
        }

        public void onOpen(CraftHumanEntity who) {
            viewers.add(who);
        }

        public void onClose(CraftHumanEntity who) {
            viewers.remove(who);
        }

        public List<HumanEntity> getViewers() {
            return viewers;
        }

        public InventoryType getType() {
            return type;
        }

        public InventoryHolder getOwner() {
            return owner;
        }

        public boolean b(int i, ItemStack itemstack) {
            return true;
        }

        @Override
        public void startOpen(EntityHuman entityHuman) {

        }

        @Override
        public void closeContainer(EntityHuman entityHuman) {

        }

        @Override
        public int getProperty(int i) {
            return 0;
        }

        @Override
        public void setProperty(int i, int j) {
        }

        @Override
        public int h() {
            return 0;
        }

        @Override
        public void clear() {
            items.clear();
        }

        @Override
        public String getName() {
            return title;
        }

        @Override
        public boolean hasCustomName() {
            return title != null;
        }

        @Override
        public IChatBaseComponent getScoreboardDisplayName() {
            return new ChatComponentText(title);
        }

        @Override
        public Location getLocation() {
            return null;
        }

        @Override
        public boolean x_() {
            Iterator iterator = this.items.iterator();

            ItemStack itemstack;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                itemstack = (ItemStack) iterator.next();
            } while (itemstack.isEmpty());

            return false;
        }
    }
}
