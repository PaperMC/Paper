package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;
// CraftBukkit end

public class TileEntityBrewingStand extends TileEntity implements IInventory {

    public ItemStack[] items = new ItemStack[4]; // CraftBukkit private -> public
    public int brewTime; // CraftBukkit private -> public
    private int c;
    private int d;

    public TileEntityBrewingStand() {}

    // CraftBukkit start
    public List<HumanEntity> transaction = new ArrayList<HumanEntity>();

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public ItemStack[] getContents() {
        return this.items;
    }
    // CraftBukkit end

    public String getName() {
        return "container.brewing";
    }

    public int getSize() {
        return this.items.length;
    }

    public void q_() {
        if (this.brewTime > 0) {
            --this.brewTime;
            if (this.brewTime == 0) {
                this.p();
                this.update();
            } else if (!this.o()) {
                this.brewTime = 0;
                this.update();
            } else if (this.d != this.items[3].id) {
                this.brewTime = 0;
                this.update();
            }
        } else if (this.o()) {
            this.brewTime = 400;
            this.d = this.items[3].id;
        }

        int i = this.n();

        if (i != this.c) {
            this.c = i;
            this.world.setData(this.x, this.y, this.z, i);
        }

        super.q_();
    }

    public int i() {
        return this.brewTime;
    }

    private boolean o() {
        if (this.items[3] != null && this.items[3].count > 0) {
            ItemStack itemstack = this.items[3];

            if (!Item.byId[itemstack.id].n()) {
                return false;
            } else {
                boolean flag = false;

                for (int i = 0; i < 3; ++i) {
                    if (this.items[i] != null && this.items[i].id == Item.POTION.id) {
                        int j = this.items[i].getData();
                        int k = this.b(j, itemstack);

                        if (!ItemPotion.c(j) && ItemPotion.c(k)) {
                            flag = true;
                            break;
                        }

                        List list = Item.POTION.b(j);
                        List list1 = Item.POTION.b(k);

                        if ((j <= 0 || list != list1) && (list == null || !list.equals(list1) && list1 != null) && j != k) {
                            flag = true;
                            break;
                        }
                    }
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    private void p() {
        if (this.o()) {
            ItemStack itemstack = this.items[3];

            // CraftBukkit start - fire BREW event
            BrewEvent event = new BrewEvent(world.getWorld().getBlockAt(x, y, z), (BrewerInventory) this.getOwner().getInventory());
            Bukkit.getPluginManager().callEvent(event);
            if(event.isCancelled()) return;
            // CraftBukkit end

            for (int i = 0; i < 3; ++i) {
                if (this.items[i] != null && this.items[i].id == Item.POTION.id) {
                    int j = this.items[i].getData();
                    int k = this.b(j, itemstack);
                    List list = Item.POTION.b(j);
                    List list1 = Item.POTION.b(k);

                    if ((j <= 0 || list != list1) && (list == null || !list.equals(list1) && list1 != null)) {
                        if (j != k) {
                            this.items[i].setData(k);
                        }
                    } else if (!ItemPotion.c(j) && ItemPotion.c(k)) {
                        this.items[i].setData(k);
                    }
                }
            }

            if (Item.byId[itemstack.id].k()) {
                this.items[3] = new ItemStack(Item.byId[itemstack.id].j());
            } else {
                --this.items[3].count;
                if (this.items[3].count <= 0) {
                    this.items[3] = null;
                }
            }
        }
    }

    private int b(int i, ItemStack itemstack) {
        return itemstack == null ? i : (Item.byId[itemstack.id].n() ? PotionBrewer.a(i, Item.byId[itemstack.id].m()) : i);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getList("Items");

        this.items = new ItemStack[this.getSize()];

        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.items.length) {
                this.items[b0] = ItemStack.a(nbttagcompound1);
            }
        }

        this.brewTime = nbttagcompound.getShort("BrewTime");
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setShort("BrewTime", (short) this.brewTime);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.setByte("Slot", (byte) i);
                this.items[i].save(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.set("Items", nbttaglist);
    }

    public ItemStack getItem(int i) {
        return i >= 0 && i < this.items.length ? this.items[i] : null;
    }

    public ItemStack splitStack(int i, int j) {
        if (i >= 0 && i < this.items.length) {
            ItemStack itemstack = this.items[i];

            this.items[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public ItemStack splitWithoutUpdate(int i) {
        if (i >= 0 && i < this.items.length) {
            ItemStack itemstack = this.items[i];

            this.items[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        if (i >= 0 && i < this.items.length) {
            this.items[i] = itemstack;
        }
    }

    public int getMaxStackSize() {
        return 1;
    }

    public boolean a(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.x, this.y, this.z) != this ? false : entityhuman.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
    }

    public void f() {}

    public void g() {}

    public int n() {
        int i = 0;

        for (int j = 0; j < 3; ++j) {
            if (this.items[j] != null) {
                i |= 1 << j;
            }
        }

        return i;
    }
}
