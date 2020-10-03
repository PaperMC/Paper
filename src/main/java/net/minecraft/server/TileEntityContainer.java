package net.minecraft.server;

import javax.annotation.Nullable;

public abstract class TileEntityContainer extends TileEntity implements IInventory, ITileInventory, INamableTileEntity {

    public ChestLock chestLock;
    public IChatBaseComponent customName;

    protected TileEntityContainer(TileEntityTypes<?> tileentitytypes) {
        super(tileentitytypes);
        this.chestLock = ChestLock.a;
    }

    @Override
    public void load(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
        super.load(iblockdata, nbttagcompound);
        this.chestLock = ChestLock.b(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
            this.customName = IChatBaseComponent.ChatSerializer.a(nbttagcompound.getString("CustomName"));
        }

    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);
        this.chestLock.a(nbttagcompound);
        if (this.customName != null) {
            nbttagcompound.setString("CustomName", IChatBaseComponent.ChatSerializer.a(this.customName));
        }

        return nbttagcompound;
    }

    public void setCustomName(IChatBaseComponent ichatbasecomponent) {
        this.customName = ichatbasecomponent;
    }

    @Override
    public IChatBaseComponent getDisplayName() {
        return this.customName != null ? this.customName : this.getContainerName();
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        return this.getDisplayName();
    }

    @Nullable
    @Override
    public IChatBaseComponent getCustomName() {
        return this.customName;
    }

    protected abstract IChatBaseComponent getContainerName();

    public boolean e(EntityHuman entityhuman) {
        return a(entityhuman, this.chestLock, this.getScoreboardDisplayName());
    }

    public static boolean a(EntityHuman entityhuman, ChestLock chestlock, IChatBaseComponent ichatbasecomponent) {
        if (!entityhuman.isSpectator() && !chestlock.a(entityhuman.getItemInMainHand())) {
            entityhuman.a((IChatBaseComponent) (new ChatMessage("container.isLocked", new Object[]{ichatbasecomponent})), true);
            entityhuman.a(SoundEffects.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return false;
        } else {
            return true;
        }
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerinventory, EntityHuman entityhuman) {
        return this.e(entityhuman) ? this.createContainer(i, playerinventory) : null;
    }

    protected abstract Container createContainer(int i, PlayerInventory playerinventory);
}
