package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class CraftBlockState implements BlockState {

    protected final CraftWorld world;
    private final BlockPos position;
    protected net.minecraft.world.level.block.state.BlockState block;
    @net.minecraft.world.level.block.Block.UpdateFlags
    protected int capturedFlags; // todo move out of this class
    private WeakReference<LevelAccessor> weakLevel;

    protected CraftBlockState(final Block block) {
        this(block.getWorld(), ((CraftBlock) block).getPosition(), ((CraftBlock) block).getBlockState());
        this.capturedFlags = net.minecraft.world.level.block.Block.UPDATE_ALL;

        this.setWorldHandle(((CraftBlock) block).getLevel());
    }

    @Deprecated
    protected CraftBlockState(final Block block, @net.minecraft.world.level.block.Block.UpdateFlags int capturedFlags) {
        this(block);
        this.capturedFlags = capturedFlags;
    }

    // world can be null for non-placed BlockStates.
    protected CraftBlockState(@Nullable World world, BlockPos pos, net.minecraft.world.level.block.state.BlockState block) {
        this.world = (CraftWorld) world;
        this.position = pos;
        this.block = block;
    }

    // Creates an unplaced copy of the given CraftBlockState at the given location
    protected CraftBlockState(CraftBlockState from, @Nullable Location location) {
        if (location == null) {
            this.world = null;
            this.position = from.getPosition().immutable();
        } else {
            this.world = (CraftWorld) location.getWorld();
            this.position = CraftLocation.toBlockPosition(location);
        }
        this.block = from.block;
        this.capturedFlags = from.capturedFlags;
        this.setWorldHandle(from.getWorldHandle());
    }

    public void setWorldHandle(LevelAccessor level) {
        if (level instanceof net.minecraft.world.level.Level) {
            this.weakLevel = null;
        } else {
            this.weakLevel = new WeakReference<>(level);
        }
    }

    // Returns null if weakLevel is not available and the BlockState is not placed.
    // If this returns a Level instead of only a LevelAccessor, this implies that this BlockState is placed.
    @Nullable
    public LevelAccessor getWorldHandle() {
        if (this.weakLevel == null) {
            return this.isPlaced() ? this.world.getHandle() : null;
        }

        LevelAccessor level = this.weakLevel.get();
        if (level == null) {
            this.weakLevel = null;
            return this.isPlaced() ? this.world.getHandle() : null;
        }

        return level;
    }

    protected final boolean isWorldGeneration() {
        LevelAccessor level = this.getWorldHandle();
        return level != null && !(level instanceof net.minecraft.world.level.Level);
    }

    protected final void ensureNoWorldGeneration() {
        Preconditions.checkState(!this.isWorldGeneration(), "This operation is not supported during world generation!");
    }

    @Override
    public World getWorld() {
        this.requirePlaced();
        return this.world;
    }

    @Override
    public int getX() {
        return this.position.getX();
    }

    @Override
    public int getY() {
        return this.position.getY();
    }

    @Override
    public int getZ() {
        return this.position.getZ();
    }

    @Override
    public Chunk getChunk() {
        this.requirePlaced();
        return this.world.getChunkAt(this.getX() >> 4, this.getZ() >> 4);
    }

    public void setBlock(net.minecraft.world.level.block.state.BlockState block) {
        this.block = block;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public net.minecraft.world.level.block.state.BlockState getHandle() {
        return this.block;
    }

    @Override
    public BlockData getBlockData() {
        return this.block.asBlockData();
    }

    @Override
    public void setBlockData(BlockData data) {
        // todo this is weird for block entities since the old methods are still available but not the new might be better to have wither
        Preconditions.checkArgument(data != null, "BlockData cannot be null");
        this.block = ((CraftBlockData) data).getState();
    }

    @Override
    public void setData(final MaterialData data) {
        Material mat = CraftMagicNumbers.getMaterial(this.block).getItemType();

        if (mat != null) {
            Preconditions.checkArgument((data.getClass() == mat.getData()) || (data.getClass() == MaterialData.class), "Provided data is not of type %s, found %s", mat.getData().getName(), data.getClass().getName());
        }
        this.block = CraftMagicNumbers.getBlock(data);
    }

    @Override
    public MaterialData getData() {
        return CraftMagicNumbers.getMaterial(this.block);
    }

    @Override
    public void setType(final Material type) {
        Preconditions.checkArgument(type != null, "Material cannot be null");
        Preconditions.checkArgument(type.isBlock(), "Material must be a block!");

        if (this.getType() != type) {
            this.block = CraftBlockType.bukkitToMinecraft(type).defaultBlockState();
        }
    }

    @Override
    public Material getType() {
        return this.block.getBukkitMaterial();
    }

    public void setFlags(@net.minecraft.world.level.block.Block.UpdateFlags int flags) {
        this.capturedFlags = flags;
    }

    public @net.minecraft.world.level.block.Block.UpdateFlags int getFlags() {
        return this.capturedFlags;
    }

    @Override
    public byte getLightLevel() {
        return this.getBlock().getLightLevel();
    }

    @Override
    public CraftBlock getBlock() {
        this.requirePlaced();
        return CraftBlock.at(this.getWorldHandle(), this.position);
    }

    @Override
    public boolean update() {
        return this.update(false);
    }

    @Override
    public boolean update(boolean force) {
        return this.update(force, true);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        if (!this.isPlaced()) {
            return true;
        }
        LevelAccessor access = this.getWorldHandle();
        CraftBlock block = this.getBlock();

        if (block.getType() != this.getType()) {
            if (!force) {
                return false;
            }
        }

        net.minecraft.world.level.block.state.BlockState newBlock = this.block;
        block.setBlockState(newBlock, applyPhysics);
        if (access instanceof net.minecraft.world.level.Level) {
            this.world.getHandle().sendBlockUpdated(
                this.position,
                block.getBlockState(),
                newBlock,
                net.minecraft.world.level.block.Block.UPDATE_ALL
            );
        }

        // Update levers etc
        if (false && applyPhysics && this.getData() instanceof Attachable) { // Call does not map to new API
            this.world.getHandle().updateNeighborsAt(this.position.relative(CraftBlock.blockFaceToNotch(((Attachable) this.getData()).getAttachedFace())), newBlock.getBlock());
        }

        return true;
    }

    // used when the flags matter for non API usage
    public boolean place(@net.minecraft.world.level.block.Block.UpdateFlags int flags) {
        if (!this.isPlaced()) {
            return false;
        }

        return this.getWorldHandle().setBlock(this.position, this.block, flags);
    }

    // used to revert a block placement due to an event being cancelled for example
    public boolean revertPlace() {
        return this.place(
            net.minecraft.world.level.block.Block.UPDATE_CLIENTS |
            net.minecraft.world.level.block.Block.UPDATE_KNOWN_SHAPE |
            net.minecraft.world.level.block.Block.UPDATE_SUPPRESS_DROPS |
            net.minecraft.world.level.block.Block.UPDATE_SKIP_ON_PLACE |
            net.minecraft.world.level.block.Block.UPDATE_SKIP_BLOCK_ENTITY_SIDEEFFECTS
        );
    }

    @Override
    public byte getRawData() {
        return CraftMagicNumbers.toLegacyData(this.block);
    }

    @Override
    public Location getLocation() {
        return CraftLocation.toBukkit(this.position, this.world);
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(this.world);
            loc.setX(this.getX());
            loc.setY(this.getY());
            loc.setZ(this.getZ());
            loc.setRotation(0, 0);
        }

        return loc;
    }

    @Override
    public void setRawData(byte data) {
        this.block = CraftMagicNumbers.getBlock(this.getType(), data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        final CraftBlockState other = (CraftBlockState) obj;
        return Objects.equals(this.world, other.world) &&
            Objects.equals(this.position, other.position) &&
            Objects.equals(this.block, other.block);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.world != null ? this.world.hashCode() : 0);
        hash = 73 * hash + (this.position != null ? this.position.hashCode() : 0);
        hash = 73 * hash + (this.block != null ? this.block.hashCode() : 0);
        return hash;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.requirePlaced();
        this.world.getBlockMetadata().setMetadata(this.getBlock(), metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        this.requirePlaced();
        return this.world.getBlockMetadata().getMetadata(this.getBlock(), metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        this.requirePlaced();
        return this.world.getBlockMetadata().hasMetadata(this.getBlock(), metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.requirePlaced();
        this.world.getBlockMetadata().removeMetadata(this.getBlock(), metadataKey, owningPlugin);
    }

    @Override
    public boolean isPlaced() {
        return this.world != null;
    }

    protected void requirePlaced() {
        Preconditions.checkState(this.isPlaced(), "The blockState must be placed to call this method");
    }

    @Override
    public CraftBlockState copy() {
        return new CraftBlockState(this, null);
    }

    @Override
    public BlockState copy(Location location) {
        return new CraftBlockState(this, location);
    }

    @Override
    public boolean isCollidable() {
        return this.block.getBlock().hasCollision;
    }

    @Override
    public java.util.Collection<org.bukkit.inventory.ItemStack> getDrops(org.bukkit.inventory.ItemStack tool, org.bukkit.entity.Entity entity) {
        this.requirePlaced();
        net.minecraft.world.item.ItemStack item = org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(tool);

        // Modelled off Player#hasCorrectToolForDrops
        if (tool == null || !this.block.requiresCorrectToolForDrops() || item.isCorrectToolForDrops(this.block)) {
            return net.minecraft.world.level.block.Block.getDrops(
                this.block,
                this.world.getHandle(),
                this.position,
                this.world.getHandle().getBlockEntity(this.position), entity == null ? null :
                    ((org.bukkit.craftbukkit.entity.CraftEntity) entity).getHandle(), item
            ).stream().map(org.bukkit.craftbukkit.inventory.CraftItemStack::asBukkitCopy).toList();
        } else {
            return java.util.Collections.emptyList();
        }
    }

    @Override
    public boolean isSuffocating() {
        this.requirePlaced();
        return this.block.isSuffocating(this.getWorldHandle(), this.position);
    }
}
