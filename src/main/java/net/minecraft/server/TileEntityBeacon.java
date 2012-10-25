package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import java.util.List;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class TileEntityBeacon extends TileEntity implements IInventory {

    public static final MobEffectList[][] a = new MobEffectList[][] { { MobEffectList.FASTER_MOVEMENT, MobEffectList.FASTER_DIG}, { MobEffectList.RESISTANCE, MobEffectList.JUMP}, { MobEffectList.INCREASE_DAMAGE}, { MobEffectList.REGENERATION}};
    private boolean d;
    private int e = -1;
    private int f;
    private int g;
    private ItemStack h;
    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public ItemStack[] getContents() {
        return null;
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

    public TileEntityBeacon() {}

    public void g() {
        if (this.world.getTime() % 80L == 0L) {
            this.u();
            this.t();
        }
    }

    private void t() {
        if (this.d && this.e > 0 && !this.world.isStatic && this.f > 0) {
            double d0 = (double) (this.e * 8 + 8);
            byte b0 = 0;

            if (this.e >= 4 && this.f == this.g) {
                b0 = 1;
            }

            AxisAlignedBB axisalignedbb = AxisAlignedBB.a().a((double) this.x, (double) this.y, (double) this.z, (double) (this.x + 1), (double) (this.y + 1), (double) (this.z + 1)).grow(d0, d0, d0);
            List list = this.world.a(EntityHuman.class, axisalignedbb);
            Iterator iterator = list.iterator();

            EntityHuman entityhuman;

            while (iterator.hasNext()) {
                entityhuman = (EntityHuman) iterator.next();
                entityhuman.addEffect(new MobEffect(this.f, 180, b0, true));
            }

            if (this.e >= 4 && this.f != this.g && this.g > 0) {
                iterator = list.iterator();

                while (iterator.hasNext()) {
                    entityhuman = (EntityHuman) iterator.next();
                    entityhuman.addEffect(new MobEffect(this.g, 180, 0, true));
                }
            }
        }
    }

    private void u() {
        if (!this.world.j(this.x, this.y + 1, this.z)) {
            this.d = false;
            this.e = 0;
        } else {
            this.d = true;
            this.e = 0;

            for (int i = 1; i <= 4; this.e = i++) {
                int j = this.y - i;

                if (j < 1) {
                    break;
                }

                boolean flag = true;

                for (int k = this.x - i; k <= this.x + i && flag; ++k) {
                    for (int l = this.z - i; l <= this.z + i; ++l) {
                        int i1 = this.world.getTypeId(k, j, l);

                        if (i1 != Block.EMERALD_BLOCK.id && i1 != Block.GOLD_BLOCK.id && i1 != Block.DIAMOND_BLOCK.id && i1 != Block.IRON_BLOCK.id) {
                            flag = false;
                            break;
                        }
                    }
                }

                if (!flag) {
                    break;
                }
            }

            if (this.e == 0) {
                this.d = false;
            }
        }
    }

    public int i() {
        return this.f;
    }

    public int j() {
        return this.g;
    }

    public int k() {
        return this.e;
    }

    public void d(int i) {
        this.f = 0;

        for (int j = 0; j < this.e && j < 3; ++j) {
            MobEffectList[] amobeffectlist = a[j];
            int k = amobeffectlist.length;

            for (int l = 0; l < k; ++l) {
                MobEffectList mobeffectlist = amobeffectlist[l];

                if (mobeffectlist.id == i) {
                    this.f = i;
                    return;
                }
            }
        }
    }

    public void e(int i) {
        this.g = 0;
        if (this.e >= 4) {
            for (int j = 0; j < 4; ++j) {
                MobEffectList[] amobeffectlist = a[j];
                int k = amobeffectlist.length;

                for (int l = 0; l < k; ++l) {
                    MobEffectList mobeffectlist = amobeffectlist[l];

                    if (mobeffectlist.id == i) {
                        this.g = i;
                        return;
                    }
                }
            }
        }
    }

    public Packet l() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.b(nbttagcompound);
        return new Packet132TileEntityData(this.x, this.y, this.z, 3, nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.f = nbttagcompound.getInt("Primary");
        this.g = nbttagcompound.getInt("Secondary");
        this.e = nbttagcompound.getInt("Levels");
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("Primary", this.f);
        nbttagcompound.setInt("Secondary", this.g);
        nbttagcompound.setInt("Levels", this.e);
    }

    public int getSize() {
        return 1;
    }

    public ItemStack getItem(int i) {
        return i == 0 ? this.h : null;
    }

    public ItemStack splitStack(int i, int j) {
        if (i == 0 && this.h != null) {
            if (j >= this.h.count) {
                ItemStack itemstack = this.h;

                this.h = null;
                return itemstack;
            } else {
                this.h.count -= j;
                return new ItemStack(this.h.id, j, this.h.getData());
            }
        } else {
            return null;
        }
    }

    public ItemStack splitWithoutUpdate(int i) {
        if (i == 0 && this.h != null) {
            ItemStack itemstack = this.h;

            this.h = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        if (i == 0) {
            this.h = itemstack;
        }
    }

    public String getName() {
        return "container.beacon";
    }

    public int getMaxStackSize() {
        return maxStack; // CraftBukkit
    }

    public boolean a(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.x, this.y, this.z) != this ? false : entityhuman.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
    }

    public void startOpen() {}

    public void f() {}
}
