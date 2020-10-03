package net.minecraft.server;

import org.bukkit.Location;
import org.bukkit.inventory.InventoryHolder;

public class InventoryEnderChest extends InventorySubcontainer {

    private TileEntityEnderChest a;
    // CraftBukkit start
    private final EntityHuman owner;

    public InventoryHolder getBukkitOwner() {
        return owner.getBukkitEntity();
    }

    @Override
    public Location getLocation() {
        return this.a != null ? new Location(this.a.getWorld().getWorld(), this.a.getPosition().getX(), this.a.getPosition().getY(), this.a.getPosition().getZ()) : null;
    }

    public InventoryEnderChest(EntityHuman owner) {
        super(27);
        this.owner = owner;
        // CraftBukkit end
    }

    public void a(TileEntityEnderChest tileentityenderchest) {
        this.a = tileentityenderchest;
    }

    @Override
    public void a(NBTTagList nbttaglist) {
        int i;

        for (i = 0; i < this.getSize(); ++i) {
            this.setItem(i, ItemStack.b);
        }

        for (i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.getSize()) {
                this.setItem(j, ItemStack.a(nbttagcompound));
            }
        }

    }

    @Override
    public NBTTagList g() {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.getSize(); ++i) {
            ItemStack itemstack = this.getItem(i);

            if (!itemstack.isEmpty()) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                nbttagcompound.setByte("Slot", (byte) i);
                itemstack.save(nbttagcompound);
                nbttaglist.add(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return this.a != null && !this.a.a(entityhuman) ? false : super.a(entityhuman);
    }

    @Override
    public void startOpen(EntityHuman entityhuman) {
        if (this.a != null) {
            this.a.d();
        }

        super.startOpen(entityhuman);
    }

    @Override
    public void closeContainer(EntityHuman entityhuman) {
        if (this.a != null) {
            this.a.f();
        }

        super.closeContainer(entityhuman);
        this.a = null;
    }
}
