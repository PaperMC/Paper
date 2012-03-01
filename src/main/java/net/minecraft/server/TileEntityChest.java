package net.minecraft.server;

// CraftBukkit start
import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class TileEntityChest extends TileEntity implements IInventory {

    private ItemStack[] items = new ItemStack[27]; // CraftBukkit
    public boolean a = false;
    public TileEntityChest b;
    public TileEntityChest c;
    public TileEntityChest d;
    public TileEntityChest e;
    public float f;
    public float g;
    public int h;
    private int ticks;

    // CraftBukkit start
    public List<HumanEntity> transaction = new ArrayList<HumanEntity>();

    public ItemStack[] getContents() {
        return this.items;
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
    // CraftBukkit end

    public TileEntityChest() {}

    public int getSize() {
        return 27;
    }

    public ItemStack getItem(int i) {
        return this.items[i];
    }

    public ItemStack splitStack(int i, int j) {
        if (this.items[i] != null) {
            ItemStack itemstack;

            if (this.items[i].count <= j) {
                itemstack = this.items[i];
                this.items[i] = null;
                this.update();
                return itemstack;
            } else {
                itemstack = this.items[i].a(j);
                if (this.items[i].count == 0) {
                    this.items[i] = null;
                }

                this.update();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    public ItemStack splitWithoutUpdate(int i) {
        if (this.items[i] != null) {
            ItemStack itemstack = this.items[i];

            this.items[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        this.items[i] = itemstack;
        if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
            itemstack.count = this.getMaxStackSize();
        }

        this.update();
    }

    public String getName() {
        return "container.chest";
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getList("Items");

        this.items = new ItemStack[this.getSize()];

        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.items.length) {
                this.items[j] = ItemStack.a(nbttagcompound1);
            }
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
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

    public int getMaxStackSize() {
        return 64;
    }

    public boolean a(EntityHuman entityhuman) {
        if (this.world == null) return true; // CraftBukkit
        return this.world.getTileEntity(this.x, this.y, this.z) != this ? false : entityhuman.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
    }

    public void h() {
        super.h();
        this.a = false;
    }

    public void i() {
        if (!this.a) {
            this.a = true;
            this.b = null;
            this.c = null;
            this.d = null;
            this.e = null;
            if (this.world.getTypeId(this.x - 1, this.y, this.z) == Block.CHEST.id) {
                this.d = (TileEntityChest) this.world.getTileEntity(this.x - 1, this.y, this.z);
            }

            if (this.world.getTypeId(this.x + 1, this.y, this.z) == Block.CHEST.id) {
                this.c = (TileEntityChest) this.world.getTileEntity(this.x + 1, this.y, this.z);
            }

            if (this.world.getTypeId(this.x, this.y, this.z - 1) == Block.CHEST.id) {
                this.b = (TileEntityChest) this.world.getTileEntity(this.x, this.y, this.z - 1);
            }

            if (this.world.getTypeId(this.x, this.y, this.z + 1) == Block.CHEST.id) {
                this.e = (TileEntityChest) this.world.getTileEntity(this.x, this.y, this.z + 1);
            }

            if (this.b != null) {
                this.b.h();
            }

            if (this.e != null) {
                this.e.h();
            }

            if (this.c != null) {
                this.c.h();
            }

            if (this.d != null) {
                this.d.h();
            }
        }
    }

    // CraftBukkit start
    private TileEntityChest getTileEntity(int x, int y, int z) {
        if (this.world == null) return null; // CraftBukkit
        TileEntity entity = this.world.getTileEntity(x, y, z);

        if (entity instanceof TileEntityChest) {
            return (TileEntityChest) entity;
        } else {
            String name = "null";
            if (entity != null) {
                name = entity.toString();
            }
            world.getServer().getLogger().severe("Block at " + x + "," + y + "," + z + " is a chest but has a " + name);
            return null;
        }
    }
    // CraftBukkit end

    public void q_() {
        super.q_();
        if (this.world == null) return; // CraftBukkit
        this.i();
        if (++this.ticks % (20 * 4) == 0) { // CraftBukkit
            this.world.playNote(this.x, this.y, this.z, 1, this.h);
        }

        this.g = this.f;
        float f = 0.1F;
        double d0;

        if (this.h > 0 && this.f == 0.0F && this.b == null && this.d == null) {
            double d1 = (double) this.x + 0.5D;

            d0 = (double) this.z + 0.5D;
            if (this.e != null) {
                d0 += 0.5D;
            }

            if (this.c != null) {
                d1 += 0.5D;
            }

            this.world.makeSound(d1, (double) this.y + 0.5D, d0, "random.chestopen", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
        }

        if (this.h == 0 && this.f > 0.0F || this.h > 0 && this.f < 1.0F) {
            float f1 = this.f;

            if (this.h > 0) {
                this.f += f;
            } else {
                this.f -= f;
            }

            if (this.f > 1.0F) {
                this.f = 1.0F;
            }

            float f2 = 0.5F;

            if (this.f < f2 && f1 >= f2 && this.b == null && this.d == null) {
                d0 = (double) this.x + 0.5D;
                double d2 = (double) this.z + 0.5D;

                if (this.e != null) {
                    d2 += 0.5D;
                }

                if (this.c != null) {
                    d0 += 0.5D;
                }

                this.world.makeSound(d0, (double) this.y + 0.5D, d2, "random.chestclosed", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }

            if (this.f < 0.0F) {
                this.f = 0.0F;
            }
        }
    }

    public void b(int i, int j) {
        if (i == 1) {
            this.h = j;
        }
    }

    public void f() {
        ++this.h;
        if (this.world == null) return; // CraftBukkit
        this.world.playNote(this.x, this.y, this.z, 1, this.h);
    }

    public void g() {
        --this.h;
        if (this.world == null) return; // CraftBukkit
        this.world.playNote(this.x, this.y, this.z, 1, this.h);
    }

    public void j() {
        this.h();
        this.i();
        super.j();
    }
}
