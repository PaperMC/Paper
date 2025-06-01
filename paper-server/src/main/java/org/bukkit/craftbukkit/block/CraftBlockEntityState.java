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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.TileState;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CraftBlockEntityState<T extends BlockEntity> extends CraftBlockState implements TileState { // Paper - revert upstream's revert of the block state changes

    private final T blockEntity;
    private final T snapshot;
    public boolean snapshotDisabled; // Paper
    public static boolean DISABLE_SNAPSHOT = false; // Paper

    public CraftBlockEntityState(World world, T blockEntity) {
        super(world, blockEntity.getBlockPos(), blockEntity.getBlockState());

        this.blockEntity = blockEntity;

        try { // Paper - Show blockstate location if we failed to read it
        // Paper start
        this.snapshotDisabled = DISABLE_SNAPSHOT;
        if (DISABLE_SNAPSHOT) {
            this.snapshot = this.blockEntity;
        } else {
            this.snapshot = this.createSnapshot(blockEntity);
        }
        // copy block entity data:
        if (this.snapshot != null) {
            this.load(this.snapshot);
        }
        // Paper end
        // Paper start - Show blockstate location if we failed to read it
        } catch (Throwable thr) {
            if (thr instanceof ThreadDeath) {
                throw (ThreadDeath)thr;
            }
            throw new RuntimeException(
                world == null
                    ? "Failed to read non-placed BlockState"
                    : "Failed to read BlockState at: world: " + world.getName() + " location: (" + this.getX() + ", " + this.getY() + ", " + this.getZ() + ")",
                thr
            );
        }
        // Paper end - Show blockstate location if we failed to read it
    }

    protected CraftBlockEntityState(CraftBlockEntityState<T> state, Location location) {
        super(state, location);
        this.blockEntity = this.createSnapshot(state.snapshot);
        this.snapshot = this.blockEntity;
        this.loadData(state.getSnapshotNBT());
    }

    public void refreshSnapshot() {
        this.load(this.blockEntity);
    }

    private RegistryAccess getRegistryAccess() {
        LevelAccessor worldHandle = this.getWorldHandle();
        return (worldHandle != null) ? worldHandle.registryAccess() : CraftRegistry.getMinecraftRegistry();
    }

    private T createSnapshot(T from) {
        if (from == null) {
            return null;
        }

        CompoundTag tag = from.saveWithFullMetadata(this.getRegistryAccess());
        return (T) BlockEntity.loadStatic(this.getPosition(), this.getHandle(), tag, this.getRegistryAccess());
    }

    public Set<DataComponentType<?>> applyComponents(DataComponentMap datacomponentmap, DataComponentPatch datacomponentpatch) {
        Set<DataComponentType<?>> result = this.snapshot.applyComponentsSet(datacomponentmap, datacomponentpatch);
        this.load(this.snapshot);
        return result;
    }

    public DataComponentMap collectComponents() {
        return this.snapshot.collectComponents();
    }

    // Loads the specified data into the snapshot BlockEntity.
    public void loadData(CompoundTag tag) {
        this.snapshot.loadWithComponents(TagValueInput.createDiscarding(this.getRegistryAccess(), tag));
        this.load(this.snapshot);
    }

    // copies the BlockEntity-specific data, retains the position
    private void copyData(T from, T to) {
        CompoundTag tag = from.saveWithFullMetadata(this.getRegistryAccess());
        to.loadWithComponents(TagValueInput.createDiscarding(this.getRegistryAccess(), tag));
    }

    // gets the wrapped BlockEntity
    public T getBlockEntity() {
        return this.blockEntity;
    }

    // gets the cloned BlockEntity which is used to store the captured data
    protected T getSnapshot() {
        return this.snapshot;
    }

    // gets the current BlockEntity from the world at this position
    protected BlockEntity getBlockEntityFromWorld() {
        this.requirePlaced();

        return this.getWorldHandle().getBlockEntity(this.getPosition());
    }

    // gets the NBT data of the BlockEntity represented by this block state
    public CompoundTag getSnapshotNBT() {
        // update snapshot
        this.applyTo(this.snapshot);

        return this.snapshot.saveWithFullMetadata(this.getRegistryAccess());
    }

    // gets the packet data of the BlockEntity represented by this block state
    public CompoundTag getUpdateNBT() {
        // update snapshot
        this.applyTo(this.snapshot);

        return this.snapshot.getUpdateTag(this.getRegistryAccess());
    }

    // Paper start - properly save blockentity itemstacks
    public CompoundTag getSnapshotCustomNbtOnly() {
        this.applyTo(this.snapshot);
        final TagValueOutput output = TagValueOutput.createDiscardingWithContext(this.snapshot.saveCustomOnly(this.getRegistryAccess()), this.getRegistryAccess());
        this.snapshot.removeComponentsFromTag(output);
        if (!output.isEmpty()) {
            // have to include the "id" if it's going to have block entity data
            this.snapshot.saveId(output);
        }
        return output.buildResult();
    }
    // Paper end

    // copies the data of the given block entity to this block state
    protected void load(T blockEntity) {
        if (blockEntity != null && blockEntity != this.snapshot) {
            this.copyData(blockEntity, this.snapshot);
        }
    }

    // applies the BlockEntity data of this block state to the given BlockEntity
    protected void applyTo(T blockEntity) {
        if (blockEntity != null && blockEntity != this.snapshot) {
            this.copyData(this.snapshot, blockEntity);
        }
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced()) {
            this.getWorldHandle().getBlockEntity(this.getPosition(), this.blockEntity.getType()).ifPresent(blockEntity -> {
                this.applyTo((T) blockEntity);
                blockEntity.setChanged();
            });
        }

        return result;
    }

    @Override
    public boolean place(int flags) {
        if (super.place(flags)) {
            this.getWorldHandle().getBlockEntity(this.getPosition(), this.blockEntity.getType()).ifPresent(blockEntity -> {
                this.applyTo((T) blockEntity);
                blockEntity.setChanged();
            });
            return true;
        }

        return false;
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
