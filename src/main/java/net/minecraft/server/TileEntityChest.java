package net.minecraft.server;

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
    private int q;

    // CraftBukkit start
    public ItemStack[] getContents() {
        return this.items;
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

    public void setItem(int i, ItemStack itemstack) {
        this.items[i] = itemstack;
        if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
            itemstack.count = this.getMaxStackSize();
        }

        this.update();
    }

    public String getName() {
        return "Chest";
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.l("Items");

        this.items = new ItemStack[this.getSize()];

        for (int i = 0; i < nbttaglist.c(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.a(i);
            int j = nbttagcompound1.c("Slot") & 255;

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

                nbttagcompound1.a("Slot", (byte) i);
                this.items[i].b(nbttagcompound1);
                nbttaglist.a((NBTBase) nbttagcompound1);
            }
        }

        nbttagcompound.a("Items", (NBTBase) nbttaglist);
    }

    public int getMaxStackSize() {
        return 64;
    }

    public boolean a(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.x, this.y, this.z) != this ? false : entityhuman.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
    }

    public void g() {
        super.g();
        this.a = false;
    }

    public void h() {
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
                this.b.g();
            }

            if (this.e != null) {
                this.e.g();
            }

            if (this.c != null) {
                this.c.g();
            }

            if (this.d != null) {
                this.d.g();
            }
        }
    }

    public void h_() {
        super.h_();
        this.h();
        if (++this.q % 20 * 4 == 0) {
            this.world.playNote(this.x, this.y, this.z, 1, this.h);
        }

        this.g = this.f;
        float f = 0.1F;
        double d0;
        double d1;

        if (this.h > 0 && this.f == 0.0F && this.b == null && this.d == null) {
            d0 = (double) this.x + 0.5D;
            d1 = (double) this.z + 0.5D;
            if (this.e != null) {
                d1 += 0.5D;
            }

            if (this.c != null) {
                d0 += 0.5D;
            }

            this.world.makeSound(d0, (double) this.y + 0.5D, d1, "random.door_open", 1.0F, this.world.random.nextFloat() * 0.1F + 0.9F);
        }

        if (this.h == 0 && this.f > 0.0F || this.h > 0 && this.f < 1.0F) {
            if (this.h > 0) {
                this.f += f;
            } else {
                this.f -= f;
            }

            if (this.f > 1.0F) {
                this.f = 1.0F;
            }

            if (this.f < 0.0F) {
                this.f = 0.0F;
                if (this.b == null && this.d == null) {
                    d0 = (double) this.x + 0.5D;
                    d1 = (double) this.z + 0.5D;
                    if (this.e != null) {
                        d1 += 0.5D;
                    }

                    if (this.c != null) {
                        d0 += 0.5D;
                    }

                    this.world.makeSound(d0, (double) this.y + 0.5D, d1, "random.door_close", 1.0F, this.world.random.nextFloat() * 0.1F + 0.9F);
                }
            }
        }
    }

    public void b(int i, int j) {
        if (i == 1) {
            this.h = j;
        }
    }

    public void e() {
        ++this.h;
        this.world.playNote(this.x, this.y, this.z, 1, this.h);
    }

    public void t_() {
        --this.h;
        this.world.playNote(this.x, this.y, this.z, 1, this.h);
    }

    public void i() {
        this.g();
        this.h();
        super.i();
    }
}
