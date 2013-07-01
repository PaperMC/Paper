package net.minecraft.server;

// CraftBukkit start
import java.util.List;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
// CraftBukkit end

public abstract class EntityMinecartContainer extends EntityMinecartAbstract implements IInventory {

    private ItemStack[] items = new ItemStack[27]; // CraftBukkit - 36 -> 27
    private boolean b = true;

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

    public InventoryHolder getOwner() {
        org.bukkit.entity.Entity cart = getBukkitEntity();
        if(cart instanceof InventoryHolder) return (InventoryHolder) cart;
        return null;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public EntityMinecartContainer(World world) {
        super(world);
    }

    public EntityMinecartContainer(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public void a(DamageSource damagesource) {
        super.a(damagesource);

        for (int i = 0; i < this.getSize(); ++i) {
            ItemStack itemstack = this.getItem(i);

            if (itemstack != null) {
                float f = this.random.nextFloat() * 0.8F + 0.1F;
                float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                float f2 = this.random.nextFloat() * 0.8F + 0.1F;

                while (itemstack.count > 0) {
                    int j = this.random.nextInt(21) + 10;

                    if (j > itemstack.count) {
                        j = itemstack.count;
                    }

                    itemstack.count -= j;
                    EntityItem entityitem = new EntityItem(this.world, this.locX + (double) f, this.locY + (double) f1, this.locZ + (double) f2, new ItemStack(itemstack.id, j, itemstack.getData()));
                    float f3 = 0.05F;

                    entityitem.motX = (double) ((float) this.random.nextGaussian() * f3);
                    entityitem.motY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
                    entityitem.motZ = (double) ((float) this.random.nextGaussian() * f3);
                    this.world.addEntity(entityitem);
                }
            }
        }
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
                return itemstack;
            } else {
                itemstack = this.items[i].a(j);
                if (this.items[i].count == 0) {
                    this.items[i] = null;
                }

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
    }

    public void update() {}

    public boolean a(EntityHuman entityhuman) {
        return this.dead ? false : entityhuman.e(this) <= 64.0D;
    }

    public void startOpen() {}

    public void g() {}

    public boolean b(int i, ItemStack itemstack) {
        return true;
    }

    public String getName() {
        return this.c() ? this.t() : "container.minecart";
    }

    public int getMaxStackSize() {
        return 64;
    }

    public void b(int i) {
        this.b = false;
        super.b(i);
    }

    public void die() {
        if (this.b) {
            for (int i = 0; i < this.getSize(); ++i) {
                ItemStack itemstack = this.getItem(i);

                if (itemstack != null) {
                    float f = this.random.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.random.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.count > 0) {
                        int j = this.random.nextInt(21) + 10;

                        if (j > itemstack.count) {
                            j = itemstack.count;
                        }

                        itemstack.count -= j;
                        EntityItem entityitem = new EntityItem(this.world, this.locX + (double) f, this.locY + (double) f1, this.locZ + (double) f2, new ItemStack(itemstack.id, j, itemstack.getData()));

                        if (itemstack.hasTag()) {
                            entityitem.getItemStack().setTag((NBTTagCompound) itemstack.getTag().clone());
                        }

                        float f3 = 0.05F;

                        entityitem.motX = (double) ((float) this.random.nextGaussian() * f3);
                        entityitem.motY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
                        entityitem.motZ = (double) ((float) this.random.nextGaussian() * f3);
                        this.world.addEntity(entityitem);
                    }
                }
            }
        }

        super.die();
    }

    protected void b(NBTTagCompound nbttagcompound) {
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

    protected void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getList("Items");

        this.items = new ItemStack[this.getSize()];

        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.items.length) {
                this.items[j] = ItemStack.createStack(nbttagcompound1);
            }
        }
    }

    public boolean c(EntityHuman entityhuman) {
        if (!this.world.isStatic) {
            entityhuman.openContainer(this);
        }

        return true;
    }

    protected void h() {
        int i = 15 - Container.b((IInventory) this);
        float f = 0.98F + (float) i * 0.001F;

        this.motX *= (double) f;
        this.motY *= 0.0D;
        this.motZ *= (double) f;
    }
}
