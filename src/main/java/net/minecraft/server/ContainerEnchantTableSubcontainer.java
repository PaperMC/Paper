package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
// CraftBukkit end

public class ContainerEnchantTableSubcontainer implements IInventory {
    private String a;
    private int b;
    private ItemStack[] c;
    private List d;

    // CraftBukkit start
    public List<HumanEntity> transaction = new ArrayList<HumanEntity>();

    public ItemStack[] getContents() {
        return this.c;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public InventoryHolder getOwner() {
        return null; // TODO: Enchanting tables don't really have an owner? Maybe they should?
    }
    // CraftBukkit end

    public ContainerEnchantTableSubcontainer(String s, int i) {
        this.a = s;
        this.b = i;
        this.c = new ItemStack[i];
    }

    public ItemStack getItem(int i) {
        return this.c[i];
    }

    public ItemStack splitStack(int i, int j) {
        if (this.c[i] != null) {
            ItemStack itemstack;

            if (this.c[i].count <= j) {
                itemstack = this.c[i];
                this.c[i] = null;
                this.update();
                return itemstack;
            } else {
                itemstack = this.c[i].a(j);
                if (this.c[i].count == 0) {
                    this.c[i] = null;
                }

                this.update();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        this.c[i] = itemstack;
        if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
            itemstack.count = this.getMaxStackSize();
        }

        this.update();
    }

    public int getSize() {
        return this.b;
    }

    public String getName() {
        return this.a;
    }

    public int getMaxStackSize() {
        return 64;
    }

    public void update() {
        if (this.d != null) {
            for (int i = 0; i < this.d.size(); ++i) {
                ((IInventoryListener) this.d.get(i)).a(this);
            }
        }
    }

    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    public void f() {}

    public void g() {}
}
