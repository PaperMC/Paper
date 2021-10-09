package org.bukkit.craftbukkit.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.level.block.entity.TileEntity;
import org.bukkit.World;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataContainer;

public abstract class CraftBlockEntityState<T extends TileEntity> extends CraftBlockState implements TileState {

    private final T tileEntity;
    private final T snapshot;

    public CraftBlockEntityState(World world, T tileEntity) {
        super(world, tileEntity.getPosition(), tileEntity.getBlock());

        this.tileEntity = tileEntity;

        // copy tile entity data:
        this.snapshot = this.createSnapshot(tileEntity);
        this.load(snapshot);
    }

    public void refreshSnapshot() {
        this.load(tileEntity);
    }

    private T createSnapshot(T tileEntity) {
        if (tileEntity == null) {
            return null;
        }

        NBTTagCompound nbtTagCompound = tileEntity.save(new NBTTagCompound());
        T snapshot = (T) TileEntity.create(getPosition(), getHandle(), nbtTagCompound);

        return snapshot;
    }

    // copies the TileEntity-specific data, retains the position
    private void copyData(T from, T to) {
        NBTTagCompound nbtTagCompound = from.save(new NBTTagCompound());
        to.load(nbtTagCompound);
    }

    // gets the wrapped TileEntity
    protected T getTileEntity() {
        return tileEntity;
    }

    // gets the cloned TileEntity which is used to store the captured data
    protected T getSnapshot() {
        return snapshot;
    }

    // gets the current TileEntity from the world at this position
    protected TileEntity getTileEntityFromWorld() {
        requirePlaced();

        return getWorldHandle().getTileEntity(this.getPosition());
    }

    // gets the NBT data of the TileEntity represented by this block state
    public NBTTagCompound getSnapshotNBT() {
        // update snapshot
        applyTo(snapshot);

        return snapshot.save(new NBTTagCompound());
    }

    // copies the data of the given tile entity to this block state
    protected void load(T tileEntity) {
        if (tileEntity != null && tileEntity != snapshot) {
            copyData(tileEntity, snapshot);
        }
    }

    // applies the TileEntity data of this block state to the given TileEntity
    protected void applyTo(T tileEntity) {
        if (tileEntity != null && tileEntity != snapshot) {
            copyData(snapshot, tileEntity);
        }
    }

    protected boolean isApplicable(TileEntity tileEntity) {
        return this.tileEntity.getClass() == tileEntity.getClass();
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced()) {
            TileEntity tile = getTileEntityFromWorld();

            if (isApplicable(tile)) {
                applyTo((T) tile);
                tile.update();
            }
        }

        return result;
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return this.getSnapshot().persistentDataContainer;
    }
}
