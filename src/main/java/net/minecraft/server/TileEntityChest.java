package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class TileEntityChest extends TileEntity implements IInventory {

    private ItemStack[] items = new ItemStack[27]; // CraftBukkit - 36 -> 27
    public boolean a;
    public TileEntityChest b;
    public TileEntityChest c;
    public TileEntityChest d;
    public TileEntityChest e;
    public float f;
    public float g;
    public int h;
    private int ticks;
    private int r = -1;
    private String s;

    public TileEntityChest() {}

    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

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

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

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
        return this.c() ? this.s : "container.chest";
    }

    public boolean c() {
        return this.s != null && this.s.length() > 0;
    }

    public void a(String s) {
        this.s = s;
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getList("Items");

        this.items = new ItemStack[this.getSize()];
        if (nbttagcompound.hasKey("CustomName")) {
            this.s = nbttagcompound.getString("CustomName");
        }

        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.items.length) {
                this.items[j] = ItemStack.createStack(nbttagcompound1);
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
        if (this.c()) {
            nbttagcompound.setString("CustomName", this.s);
        }
    }

    public int getMaxStackSize() {
        return maxStack; // CraftBukkit
    }

    public boolean a(EntityHuman entityhuman) {
        if (this.world == null) return true; // CraftBukkit
        return this.world.getTileEntity(this.x, this.y, this.z) != this ? false : entityhuman.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
    }

    public void i() {
        super.i();
        this.a = false;
    }

    private void a(TileEntityChest tileentitychest, int i) {
        if (tileentitychest.r()) {
            this.a = false;
        } else if (this.a) {
            switch (i) {
            case 0:
                if (this.e != tileentitychest) {
                    this.a = false;
                }
                break;

            case 1:
                if (this.d != tileentitychest) {
                    this.a = false;
                }
                break;

            case 2:
                if (this.b != tileentitychest) {
                    this.a = false;
                }
                break;

            case 3:
                if (this.c != tileentitychest) {
                    this.a = false;
                }
            }
        }
    }

    public void j() {
        if (!this.a) {
            this.a = true;
            this.b = null;
            this.c = null;
            this.d = null;
            this.e = null;
            if (this.a(this.x - 1, this.y, this.z)) {
                this.d = (TileEntityChest) this.world.getTileEntity(this.x - 1, this.y, this.z);
            }

            if (this.a(this.x + 1, this.y, this.z)) {
                this.c = (TileEntityChest) this.world.getTileEntity(this.x + 1, this.y, this.z);
            }

            if (this.a(this.x, this.y, this.z - 1)) {
                this.b = (TileEntityChest) this.world.getTileEntity(this.x, this.y, this.z - 1);
            }

            if (this.a(this.x, this.y, this.z + 1)) {
                this.e = (TileEntityChest) this.world.getTileEntity(this.x, this.y, this.z + 1);
            }

            if (this.b != null) {
                this.b.a(this, 0);
            }

            if (this.e != null) {
                this.e.a(this, 2);
            }

            if (this.c != null) {
                this.c.a(this, 1);
            }

            if (this.d != null) {
                this.d.a(this, 3);
            }
        }
    }

    private boolean a(int i, int j, int k) {
        Block block = Block.byId[this.world.getTypeId(i, j, k)];

        return block != null && block instanceof BlockChest ? ((BlockChest) block).a == this.l() : false;
    }

    public void h() {
        super.h();
        if (this.world == null) return; // CraftBukkit
        this.j();
        ++this.ticks;
        float f;

        if (!this.world.isStatic && this.h != 0 && (this.ticks + this.x + this.y + this.z) % 200 == 0) {
            this.h = 0;
            f = 5.0F;
            List list = this.world.a(EntityHuman.class, AxisAlignedBB.a().a((double) ((float) this.x - f), (double) ((float) this.y - f), (double) ((float) this.z - f), (double) ((float) (this.x + 1) + f), (double) ((float) (this.y + 1) + f), (double) ((float) (this.z + 1) + f)));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityHuman entityhuman = (EntityHuman) iterator.next();

                if (entityhuman.activeContainer instanceof ContainerChest) {
                    IInventory iinventory = ((ContainerChest) entityhuman.activeContainer).e();

                    if (iinventory == this || iinventory instanceof InventoryLargeChest && ((InventoryLargeChest) iinventory).a((IInventory) this)) {
                        ++this.h;
                    }
                }
            }
        }

        this.g = this.f;
        f = 0.1F;
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

    public boolean b(int i, int j) {
        if (i == 1) {
            this.h = j;
            return true;
        } else {
            return super.b(i, j);
        }
    }

    public void startOpen() {
        if (this.h < 0) {
            this.h = 0;
        }

        int oldPower = Math.max(0, Math.min(15, this.h)); // CraftBukkit - Get power before new viewer is added

        ++this.h;
        if (this.world == null) return; // CraftBukkit
        this.world.playNote(this.x, this.y, this.z, this.q().id, 1, this.h);

        // CraftBukkit start - Call redstone event
        if (this.q().id == Block.TRAPPED_CHEST.id) {
            int newPower = Math.max(0, Math.min(15, this.h));

            if (oldPower != newPower) {
                org.bukkit.craftbukkit.event.CraftEventFactory.callRedstoneChange(world, this.x, this.y, this.z, oldPower, newPower);
            }
        }
        // CraftBukkit end

        this.world.applyPhysics(this.x, this.y, this.z, this.q().id);
        this.world.applyPhysics(this.x, this.y - 1, this.z, this.q().id);
    }

    public void g() {
        if (this.q() != null && this.q() instanceof BlockChest) {
            int oldPower = Math.max(0, Math.min(15, this.h)); // CraftBukkit - Get power before new viewer is added

            --this.h;
            if (this.world == null) return; // CraftBukkit
            this.world.playNote(this.x, this.y, this.z, this.q().id, 1, this.h);

            // CraftBukkit start - Call redstone event
            if (this.q().id == Block.TRAPPED_CHEST.id) {
                int newPower = Math.max(0, Math.min(15, this.h));

                if (oldPower != newPower) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.callRedstoneChange(world, this.x, this.y, this.z, oldPower, newPower);
                }
            }
            // CraftBukkit end

            this.world.applyPhysics(this.x, this.y, this.z, this.q().id);
            this.world.applyPhysics(this.x, this.y - 1, this.z, this.q().id);
        }
    }

    public boolean b(int i, ItemStack itemstack) {
        return true;
    }

    public void w_() {
        super.w_();
        this.i();
        this.j();
    }

    public int l() {
        if (this.r == -1) {
            if (this.world == null || !(this.q() instanceof BlockChest)) {
                return 0;
            }

            this.r = ((BlockChest) this.q()).a;
        }

        return this.r;
    }
}
