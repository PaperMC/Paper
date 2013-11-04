package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class TileEntityBeacon extends TileEntity implements IInventory {

    public static final MobEffectList[][] a = new MobEffectList[][] { { MobEffectList.FASTER_MOVEMENT, MobEffectList.FASTER_DIG}, { MobEffectList.RESISTANCE, MobEffectList.JUMP}, { MobEffectList.INCREASE_DAMAGE}, { MobEffectList.REGENERATION}};
    private boolean k;
    private int l = -1;
    private int m;
    private int n;
    private ItemStack inventorySlot;
    private String p;
    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public ItemStack[] getContents() {
        return new ItemStack[] { this.inventorySlot };
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

    public void h() {
        if (this.world.getTime() % 80L == 0L) {
            this.y();
            this.x();
        }
    }

    private void x() {
        if (this.k && this.l > 0 && !this.world.isStatic && this.m > 0) {
            double d0 = (double) (this.l * 10 + 10);
            byte b0 = 0;

            if (this.l >= 4 && this.m == this.n) {
                b0 = 1;
            }

            AxisAlignedBB axisalignedbb = AxisAlignedBB.a().a((double) this.x, (double) this.y, (double) this.z, (double) (this.x + 1), (double) (this.y + 1), (double) (this.z + 1)).grow(d0, d0, d0);

            axisalignedbb.e = (double) this.world.getHeight();
            List list = this.world.a(EntityHuman.class, axisalignedbb);
            Iterator iterator = list.iterator();

            EntityHuman entityhuman;

            while (iterator.hasNext()) {
                entityhuman = (EntityHuman) iterator.next();
                entityhuman.addEffect(new MobEffect(this.m, 180, b0, true));
            }

            if (this.l >= 4 && this.m != this.n && this.n > 0) {
                iterator = list.iterator();

                while (iterator.hasNext()) {
                    entityhuman = (EntityHuman) iterator.next();
                    entityhuman.addEffect(new MobEffect(this.n, 180, 0, true));
                }
            }
        }
    }

    private void y() {
        int i = this.l;

        if (!this.world.i(this.x, this.y + 1, this.z)) {
            this.k = false;
            this.l = 0;
        } else {
            this.k = true;
            this.l = 0;

            for (int j = 1; j <= 4; this.l = j++) {
                int k = this.y - j;

                if (k < 0) {
                    break;
                }

                boolean flag = true;

                for (int l = this.x - j; l <= this.x + j && flag; ++l) {
                    for (int i1 = this.z - j; i1 <= this.z + j; ++i1) {
                        Block block = this.world.getType(l, k, i1);

                        if (block != Blocks.EMERALD_BLOCK && block != Blocks.GOLD_BLOCK && block != Blocks.DIAMOND_BLOCK && block != Blocks.IRON_BLOCK) {
                            flag = false;
                            break;
                        }
                    }
                }

                if (!flag) {
                    break;
                }
            }

            if (this.l == 0) {
                this.k = false;
            }
        }

        if (!this.world.isStatic && this.l == 4 && i < this.l) {
            Iterator iterator = this.world.a(EntityHuman.class, AxisAlignedBB.a().a((double) this.x, (double) this.y, (double) this.z, (double) this.x, (double) (this.y - 4), (double) this.z).grow(10.0D, 5.0D, 10.0D)).iterator();

            while (iterator.hasNext()) {
                EntityHuman entityhuman = (EntityHuman) iterator.next();

                entityhuman.a((Statistic) AchievementList.K);
            }
        }
    }

    public int j() {
        return this.m;
    }

    public int k() {
        return this.n;
    }

    public int l() {
        return this.l;
    }

    public void d(int i) {
        this.m = 0;

        for (int j = 0; j < this.l && j < 3; ++j) {
            MobEffectList[] amobeffectlist = a[j];
            int k = amobeffectlist.length;

            for (int l = 0; l < k; ++l) {
                MobEffectList mobeffectlist = amobeffectlist[l];

                if (mobeffectlist.id == i) {
                    this.m = i;
                    return;
                }
            }
        }
    }

    public void e(int i) {
        this.n = 0;
        if (this.l >= 4) {
            for (int j = 0; j < 4; ++j) {
                MobEffectList[] amobeffectlist = a[j];
                int k = amobeffectlist.length;

                for (int l = 0; l < k; ++l) {
                    MobEffectList mobeffectlist = amobeffectlist[l];

                    if (mobeffectlist.id == i) {
                        this.n = i;
                        return;
                    }
                }
            }
        }
    }

    public Packet getUpdatePacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.b(nbttagcompound);
        return new PacketPlayOutTileEntityData(this.x, this.y, this.z, 3, nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.m = nbttagcompound.getInt("Primary");
        this.n = nbttagcompound.getInt("Secondary");
        this.l = nbttagcompound.getInt("Levels");
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("Primary", this.m);
        nbttagcompound.setInt("Secondary", this.n);
        nbttagcompound.setInt("Levels", this.l);
    }

    public int getSize() {
        return 1;
    }

    public ItemStack getItem(int i) {
        return i == 0 ? this.inventorySlot : null;
    }

    public ItemStack splitStack(int i, int j) {
        if (i == 0 && this.inventorySlot != null) {
            if (j >= this.inventorySlot.count) {
                ItemStack itemstack = this.inventorySlot;

                this.inventorySlot = null;
                return itemstack;
            } else {
                this.inventorySlot.count -= j;
                return new ItemStack(this.inventorySlot.getItem(), j, this.inventorySlot.getData());
            }
        } else {
            return null;
        }
    }

    public ItemStack splitWithoutUpdate(int i) {
        if (i == 0 && this.inventorySlot != null) {
            ItemStack itemstack = this.inventorySlot;

            this.inventorySlot = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        if (i == 0) {
            this.inventorySlot = itemstack;
        }
    }

    public String getInventoryName() {
        return this.k_() ? this.p : "container.beacon";
    }

    public boolean k_() {
        return this.p != null && this.p.length() > 0;
    }

    public void a(String s) {
        this.p = s;
    }

    public int getMaxStackSize() {
        return maxStack; // CraftBukkit
    }

    public boolean a(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.x, this.y, this.z) != this ? false : entityhuman.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
    }

    public void startOpen() {}

    public void l_() {}

    public boolean b(int i, ItemStack itemstack) {
        return itemstack.getItem() == Items.EMERALD || itemstack.getItem() == Items.DIAMOND || itemstack.getItem() == Items.GOLD_INGOT || itemstack.getItem() == Items.IRON_INGOT;
    }
}
