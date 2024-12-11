package org.bukkit.craftbukkit.block;

import java.util.Set;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.TileState;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CraftBlockEntityState<T extends BlockEntity> extends CraftBlockState implements TileState { // Paper - revert upstream's revert of the block state changes

    private final T tileEntity;
    private final T snapshot;
    public boolean snapshotDisabled; // Paper
    public static boolean DISABLE_SNAPSHOT = false; // Paper

    public CraftBlockEntityState(World world, T tileEntity) {
        super(world, tileEntity.getBlockPos(), tileEntity.getBlockState());

        this.tileEntity = tileEntity;

        try { // Paper - Show blockstate location if we failed to read it
        // Paper start
        this.snapshotDisabled = DISABLE_SNAPSHOT;
        if (DISABLE_SNAPSHOT) {
            this.snapshot = this.tileEntity;
        } else {
            this.snapshot = this.createSnapshot(tileEntity);
        }
        // copy tile entity data:
        if (this.snapshot != null) {
            this.load(this.snapshot);
        }
        // Paper end
        // Paper start - Show blockstate location if we failed to read it
        } catch (Throwable thr) {
            if (thr instanceof ThreadDeath) {
                throw (ThreadDeath)thr;
            }
            throw new RuntimeException("Failed to read BlockState at: world: " + this.getWorld().getName() + " location: (" + this.getX() + ", " + this.getY() + ", " + this.getZ() + ")", thr);
        }
        // Paper end - Show blockstate location if we failed to read it
    }

    protected CraftBlockEntityState(CraftBlockEntityState<T> state, Location location) {
        super(state, location);
        this.tileEntity = this.createSnapshot(state.snapshot);
        this.snapshot = this.tileEntity;
        this.loadData(state.getSnapshotNBT());
    }

    public void refreshSnapshot() {
        this.load(this.tileEntity);
    }

    private RegistryAccess getRegistryAccess() {
        LevelAccessor worldHandle = this.getWorldHandle();
        return (worldHandle != null) ? worldHandle.registryAccess() : MinecraftServer.getDefaultRegistryAccess();
    }

    private T createSnapshot(T tileEntity) {
        if (tileEntity == null) {
            return null;
        }

        CompoundTag nbtTagCompound = tileEntity.saveWithFullMetadata(this.getRegistryAccess());
        T snapshot = (T) BlockEntity.loadStatic(this.getPosition(), this.getHandle(), nbtTagCompound, this.getRegistryAccess());

        return snapshot;
    }

    public Set<DataComponentType<?>> applyComponents(DataComponentMap datacomponentmap, DataComponentPatch datacomponentpatch) {
        Set<DataComponentType<?>> result = this.snapshot.applyComponentsSet(datacomponentmap, datacomponentpatch);
        this.load(this.snapshot);
        return result;
    }

    public DataComponentMap collectComponents() {
        return this.snapshot.collectComponents();
    }

    // Loads the specified data into the snapshot TileEntity.
    public void loadData(CompoundTag nbtTagCompound) {
        this.snapshot.loadWithComponents(nbtTagCompound, this.getRegistryAccess());
        this.load(this.snapshot);
    }

    // copies the TileEntity-specific data, retains the position
    private void copyData(T from, T to) {
        CompoundTag nbtTagCompound = from.saveWithFullMetadata(this.getRegistryAccess());
        to.loadWithComponents(nbtTagCompound, this.getRegistryAccess());
    }

    // gets the wrapped TileEntity
    public T getTileEntity() {
        return this.tileEntity;
    }

    // gets the cloned TileEntity which is used to store the captured data
    protected T getSnapshot() {
        return this.snapshot;
    }

    // gets the current TileEntity from the world at this position
    protected BlockEntity getTileEntityFromWorld() {
        this.requirePlaced();

        return this.getWorldHandle().getBlockEntity(this.getPosition());
    }

    // gets the NBT data of the TileEntity represented by this block state
    public CompoundTag getSnapshotNBT() {
        // update snapshot
        this.applyTo(this.snapshot);

        return this.snapshot.saveWithFullMetadata(this.getRegistryAccess());
    }

    public CompoundTag getItemNBT() {
        // update snapshot
        this.applyTo(this.snapshot);

        // See TileEntity#saveToItem
        CompoundTag nbt = this.snapshot.saveCustomOnly(this.getRegistryAccess());
        this.snapshot.removeComponentsFromTag(nbt);
        return nbt;
    }

    public void addEntityType(CompoundTag nbt) {
        BlockEntity.addEntityType(nbt, this.snapshot.getType());
    }

    // gets the packet data of the TileEntity represented by this block state
    public CompoundTag getUpdateNBT() {
        // update snapshot
        this.applyTo(this.snapshot);

        return this.snapshot.getUpdateTag(this.getRegistryAccess());
    }

    // Paper start - properly save blockentity itemstacks
    public CompoundTag getSnapshotCustomNbtOnly() {
        this.applyTo(this.snapshot);
        final CompoundTag nbt = this.snapshot.saveCustomOnly(this.getRegistryAccess());
        this.snapshot.removeComponentsFromTag(nbt);
        if (!nbt.isEmpty()) {
            // have to include the "id" if it's going to have block entity data
            this.snapshot.saveId(nbt);
        }
        return nbt;
    }
    // Paper end

    // copies the data of the given tile entity to this block state
    protected void load(T tileEntity) {
        if (tileEntity != null && tileEntity != this.snapshot) {
            this.copyData(tileEntity, this.snapshot);
        }
    }

    // applies the TileEntity data of this block state to the given TileEntity
    protected void applyTo(T tileEntity) {
        if (tileEntity != null && tileEntity != this.snapshot) {
            this.copyData(this.snapshot, tileEntity);
        }
    }

    protected boolean isApplicable(BlockEntity tileEntity) {
        return tileEntity != null && this.tileEntity.getClass() == tileEntity.getClass();
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced()) {
            BlockEntity tile = this.getTileEntityFromWorld();

            if (this.isApplicable(tile)) {
                this.applyTo((T) tile);
                tile.setChanged();
            }
        }

        return result;
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return this.getSnapshot().persistentDataContainer;
    }

    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket(@NotNull Location location) {
        return new ClientboundBlockEntityDataPacket(CraftLocation.toBlockPosition(location), this.snapshot.getType(), this.getUpdateNBT());
    }

    @Override
    public abstract CraftBlockEntityState<T> copy(); // Paper - make abstract

    @Override
    public abstract CraftBlockEntityState<T> copy(Location location); // Paper - make abstract

    // Paper start
    @Override
    public boolean isSnapshot() {
        return !this.snapshotDisabled;
    }
    // Paper end
}
