package net.minecraft.server;

import java.util.concurrent.Callable;

// CraftBukkit start
import java.util.List;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class PlayerInventory implements IInventory {

    public ItemStack[] items = new ItemStack[36];
    public ItemStack[] armor = new ItemStack[4];
    public int itemInHandIndex;
    public EntityHuman player;
    private ItemStack g;
    public boolean e;

    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public ItemStack[] getContents() {
        return this.items;
    }

    public ItemStack[] getArmorContents() {
        return this.armor;
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

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return this.player.getBukkitEntity();
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public PlayerInventory(EntityHuman entityhuman) {
        this.player = entityhuman;
    }

    public ItemStack getItemInHand() {
        return this.itemInHandIndex < 9 && this.itemInHandIndex >= 0 ? this.items[this.itemInHandIndex] : null;
    }

    public static int getHotbarSize() {
        return 9;
    }

    private int g(int i) {
        for (int j = 0; j < this.items.length; ++j) {
            if (this.items[j] != null && this.items[j].id == i) {
                return j;
            }
        }

        return -1;
    }

    private int firstPartial(ItemStack itemstack) {
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null && this.items[i].id == itemstack.id && this.items[i].isStackable() && this.items[i].count < this.items[i].getMaxStackSize() && this.items[i].count < this.getMaxStackSize() && (!this.items[i].usesData() || this.items[i].getData() == itemstack.getData()) && ItemStack.equals(this.items[i], itemstack)) {
                return i;
            }
        }

        return -1;
    }

    // CraftBukkit start - Watch method above! :D
    public int canHold(ItemStack itemstack) {
        int remains = itemstack.count;
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] == null) return itemstack.count;

            // Taken from firstPartial(ItemStack)
            if (this.items[i] != null && this.items[i].id == itemstack.id && this.items[i].isStackable() && this.items[i].count < this.items[i].getMaxStackSize() && this.items[i].count < this.getMaxStackSize() && (!this.items[i].usesData() || this.items[i].getData() == itemstack.getData())) {
                remains -= (this.items[i].getMaxStackSize() < this.getMaxStackSize() ? this.items[i].getMaxStackSize() : this.getMaxStackSize()) - this.items[i].count;
            }
            if (remains <= 0) return itemstack.count;
        }
        return itemstack.count - remains;
    }
    // CraftBukkit end

    public int j() {
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] == null) {
                return i;
            }
        }

        return -1;
    }

    public int b(int i, int j) {
        int k = 0;

        int l;
        ItemStack itemstack;

        for (l = 0; l < this.items.length; ++l) {
            itemstack = this.items[l];
            if (itemstack != null && (i <= -1 || itemstack.id == i) && (j <= -1 || itemstack.getData() == j)) {
                k += itemstack.count;
                this.items[l] = null;
            }
        }

        for (l = 0; l < this.armor.length; ++l) {
            itemstack = this.armor[l];
            if (itemstack != null && (i <= -1 || itemstack.id == i) && (j <= -1 || itemstack.getData() == j)) {
                k += itemstack.count;
                this.armor[l] = null;
            }
        }

        if (this.g != null) {
            if (i > -1 && this.g.id != i) {
                return k;
            }

            if (j > -1 && this.g.getData() != j) {
                return k;
            }

            k += this.g.count;
            this.setCarried((ItemStack) null);
        }

        return k;
    }

    private int e(ItemStack itemstack) {
        int i = itemstack.id;
        int j = itemstack.count;
        int k;

        if (itemstack.getMaxStackSize() == 1) {
            k = this.j();
            if (k < 0) {
                return j;
            } else {
                if (this.items[k] == null) {
                    this.items[k] = ItemStack.b(itemstack);
                }

                return 0;
            }
        } else {
            k = this.firstPartial(itemstack);
            if (k < 0) {
                k = this.j();
            }

            if (k < 0) {
                return j;
            } else {
                if (this.items[k] == null) {
                    this.items[k] = new ItemStack(i, 0, itemstack.getData());
                    if (itemstack.hasTag()) {
                        this.items[k].setTag((NBTTagCompound) itemstack.getTag().clone());
                    }
                }

                int l = j;

                if (j > this.items[k].getMaxStackSize() - this.items[k].count) {
                    l = this.items[k].getMaxStackSize() - this.items[k].count;
                }

                if (l > this.getMaxStackSize() - this.items[k].count) {
                    l = this.getMaxStackSize() - this.items[k].count;
                }

                if (l == 0) {
                    return j;
                } else {
                    j -= l;
                    this.items[k].count += l;
                    this.items[k].c = 5;
                    return j;
                }
            }
        }
    }

    public void k() {
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                this.items[i].a(this.player.world, this.player, i, this.itemInHandIndex == i);
            }
        }
    }

    public boolean d(int i) {
        int j = this.g(i);

        if (j < 0) {
            return false;
        } else {
            if (--this.items[j].count <= 0) {
                this.items[j] = null;
            }

            return true;
        }
    }

    public boolean e(int i) {
        int j = this.g(i);

        return j >= 0;
    }

    public boolean pickup(ItemStack itemstack) {
        if (itemstack == null) {
            return false;
        } else if (itemstack.count == 0) {
            return false;
        } else {
            try {
                int i;

                if (itemstack.i()) {
                    i = this.j();
                    if (i >= 0) {
                        this.items[i] = ItemStack.b(itemstack);
                        this.items[i].c = 5;
                        itemstack.count = 0;
                        return true;
                    } else if (this.player.abilities.canInstantlyBuild) {
                        itemstack.count = 0;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    do {
                        i = itemstack.count;
                        itemstack.count = this.e(itemstack);
                    } while (itemstack.count > 0 && itemstack.count < i);

                    if (itemstack.count == i && this.player.abilities.canInstantlyBuild) {
                        itemstack.count = 0;
                        return true;
                    } else {
                        return itemstack.count < i;
                    }
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Adding item to inventory");
                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Item being added");

                crashreportsystemdetails.a("Item ID", Integer.valueOf(itemstack.id));
                crashreportsystemdetails.a("Item data", Integer.valueOf(itemstack.getData()));
                crashreportsystemdetails.a("Item name", (Callable) (new CrashReportItemName(this, itemstack)));
                throw new ReportedException(crashreport);
            }
        }
    }

    public ItemStack splitStack(int i, int j) {
        ItemStack[] aitemstack = this.items;

        if (i >= this.items.length) {
            aitemstack = this.armor;
            i -= this.items.length;
        }

        if (aitemstack[i] != null) {
            ItemStack itemstack;

            if (aitemstack[i].count <= j) {
                itemstack = aitemstack[i];
                aitemstack[i] = null;
                return itemstack;
            } else {
                itemstack = aitemstack[i].a(j);
                if (aitemstack[i].count == 0) {
                    aitemstack[i] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    public ItemStack splitWithoutUpdate(int i) {
        ItemStack[] aitemstack = this.items;

        if (i >= this.items.length) {
            aitemstack = this.armor;
            i -= this.items.length;
        }

        if (aitemstack[i] != null) {
            ItemStack itemstack = aitemstack[i];

            aitemstack[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        ItemStack[] aitemstack = this.items;

        if (i >= aitemstack.length) {
            i -= aitemstack.length;
            aitemstack = this.armor;
        }

        aitemstack[i] = itemstack;
    }

    public float a(Block block) {
        float f = 1.0F;

        if (this.items[this.itemInHandIndex] != null) {
            f *= this.items[this.itemInHandIndex].a(block);
        }

        return f;
    }

    public NBTTagList a(NBTTagList nbttaglist) {
        int i;
        NBTTagCompound nbttagcompound;

        for (i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                this.items[i].save(nbttagcompound);
                nbttaglist.add(nbttagcompound);
            }
        }

        for (i = 0; i < this.armor.length; ++i) {
            if (this.armor[i] != null) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) (i + 100));
                this.armor[i].save(nbttagcompound);
                nbttaglist.add(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    public void b(NBTTagList nbttaglist) {
        this.items = new ItemStack[36];
        this.armor = new ItemStack[4];

        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) nbttaglist.get(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.createStack(nbttagcompound);

            if (itemstack != null) {
                if (j >= 0 && j < this.items.length) {
                    this.items[j] = itemstack;
                }

                if (j >= 100 && j < this.armor.length + 100) {
                    this.armor[j - 100] = itemstack;
                }
            }
        }
    }

    public int getSize() {
        return this.items.length + 4;
    }

    public ItemStack getItem(int i) {
        ItemStack[] aitemstack = this.items;

        if (i >= aitemstack.length) {
            i -= aitemstack.length;
            aitemstack = this.armor;
        }

        return aitemstack[i];
    }

    public String getName() {
        return "container.inventory";
    }

    public boolean c() {
        return false;
    }

    public int getMaxStackSize() {
        return maxStack;
    }

    public boolean b(Block block) {
        if (block.material.isAlwaysDestroyable()) {
            return true;
        } else {
            ItemStack itemstack = this.getItem(this.itemInHandIndex);

            return itemstack != null ? itemstack.b(block) : false;
        }
    }

    public ItemStack f(int i) {
        return this.armor[i];
    }

    public int l() {
        int i = 0;

        for (int j = 0; j < this.armor.length; ++j) {
            if (this.armor[j] != null && this.armor[j].getItem() instanceof ItemArmor) {
                int k = ((ItemArmor) this.armor[j].getItem()).c;

                i += k;
            }
        }

        return i;
    }

    public void a(float f) {
        f /= 4.0F;
        if (f < 1.0F) {
            f = 1.0F;
        }

        for (int i = 0; i < this.armor.length; ++i) {
            if (this.armor[i] != null && this.armor[i].getItem() instanceof ItemArmor) {
                this.armor[i].damage((int) f, this.player);
                if (this.armor[i].count == 0) {
                    this.armor[i] = null;
                }
            }
        }
    }

    public void m() {
        int i;

        for (i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                this.player.a(this.items[i], true);
                this.items[i] = null;
            }
        }

        for (i = 0; i < this.armor.length; ++i) {
            if (this.armor[i] != null) {
                this.player.a(this.armor[i], true);
                this.armor[i] = null;
            }
        }
    }

    public void update() {
        this.e = true;
    }

    public void setCarried(ItemStack itemstack) {
        this.g = itemstack;
    }

    public ItemStack getCarried() {
        // CraftBukkit start
        if (this.g != null && this.g.count == 0) {
            this.setCarried(null);
        }
        // CraftBukkit end
        return this.g;
    }

    public boolean a(EntityHuman entityhuman) {
        return this.player.dead ? false : entityhuman.e(this.player) <= 64.0D;
    }

    public boolean c(ItemStack itemstack) {
        int i;

        for (i = 0; i < this.armor.length; ++i) {
            if (this.armor[i] != null && this.armor[i].doMaterialsMatch(itemstack)) {
                return true;
            }
        }

        for (i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null && this.items[i].doMaterialsMatch(itemstack)) {
                return true;
            }
        }

        return false;
    }

    public void startOpen() {}

    public void g() {}

    public boolean b(int i, ItemStack itemstack) {
        return true;
    }

    public void b(PlayerInventory playerinventory) {
        int i;

        for (i = 0; i < this.items.length; ++i) {
            this.items[i] = ItemStack.b(playerinventory.items[i]);
        }

        for (i = 0; i < this.armor.length; ++i) {
            this.armor[i] = ItemStack.b(playerinventory.armor[i]);
        }

        this.itemInHandIndex = playerinventory.itemInHandIndex;
    }
}
