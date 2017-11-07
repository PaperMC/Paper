package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.persistence.PersistentDataContainer;

public class CraftBlockEntityState<T extends TileEntity> extends CraftBlockState implements TileState {

    private final Class<T> tileEntityClass;
    private final T tileEntity;
    private final T snapshot;

    public CraftBlockEntityState(Block block, Class<T> tileEntityClass) {
        super(block);

        this.tileEntityClass = tileEntityClass;

        // get tile entity from block:
        CraftWorld world = (CraftWorld) this.getWorld();
        this.tileEntity = tileEntityClass.cast(world.getHandle().getTileEntity(this.getPosition()));
        Preconditions.checkState(this.tileEntity != null, "Tile is null, asynchronous access? " + block);

        // Paper start
        this.snapshotDisabled = DISABLE_SNAPSHOT;
        if (DISABLE_SNAPSHOT) {
            this.snapshot = this.tileEntity;
        } else {
            this.snapshot = this.createSnapshot(this.tileEntity);
        }
        // copy tile entity data:
        if(this.snapshot != null) {
            this.load(this.snapshot);
        }
        // Paper end
    }

    public final boolean snapshotDisabled; // Paper
    public static boolean DISABLE_SNAPSHOT = false; // Paper

    public CraftBlockEntityState(Material material, T tileEntity) {
        super(material);

        this.tileEntityClass = (Class<T>) tileEntity.getClass();
        this.tileEntity = tileEntity;
        // Paper start
        this.snapshotDisabled = DISABLE_SNAPSHOT;
        if (DISABLE_SNAPSHOT) {
            this.snapshot = this.tileEntity;
        } else {
            this.snapshot = this.createSnapshot(this.tileEntity);
        }
        // copy tile entity data:
        if(this.snapshot != null) {
            this.load(this.snapshot);
        }
        // Paper end
    }

    private T createSnapshot(T tileEntity) {
        if (tileEntity == null) {
            return null;
        }

        NBTTagCompound nbtTagCompound = tileEntity.save(new NBTTagCompound());
        T snapshot = (T) TileEntity.create(getHandle(), nbtTagCompound);

        return snapshot;
    }

    // copies the TileEntity-specific data, retains the position
    private void copyData(T from, T to) {
        BlockPosition pos = to.getPosition();
        NBTTagCompound nbtTagCompound = from.save(new NBTTagCompound());
        to.load(getHandle(), nbtTagCompound);

        // reset the original position:
        to.setPosition(pos);
    }

    // gets the wrapped TileEntity
    public T getTileEntity() { // Paper - protected -> public
        return tileEntity;
    }

    // gets the cloned TileEntity which is used to store the captured data
    protected T getSnapshot() {
        return snapshot;
    }

    // gets the current TileEntity from the world at this position
    protected TileEntity getTileEntityFromWorld() {
        requirePlaced();

        return ((CraftWorld) this.getWorld()).getHandle().getTileEntity(this.getPosition());
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
        return tileEntityClass.isInstance(tileEntity);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced()) {
            TileEntity tile = getTileEntityFromWorld();

            if (isApplicable(tile)) {
                applyTo(tileEntityClass.cast(tile));
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
