package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftFluidCollisionMode;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftRayTraceResult;
import org.bukkit.craftbukkit.util.CraftVoxelShape;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jspecify.annotations.Nullable;

public class CraftBlock implements Block {
    private final net.minecraft.world.level.LevelAccessor level;
    private final BlockPos position;

    private CraftBlock(LevelAccessor level, BlockPos position) {
        this.level = level;
        this.position = position.immutable();
    }

    public static CraftBlock at(LevelAccessor level, BlockPos position) {
        return new CraftBlock(level, position);
    }

    public net.minecraft.world.level.block.state.BlockState getBlockState() {
        return this.level.getBlockState(this.position);
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public LevelAccessor getLevel() {
        return this.level;
    }

    @Override
    public World getWorld() {
        return this.level.getMinecraftWorld().getWorld();
    }

    public CraftWorld getCraftWorld() {
        return (CraftWorld) this.getWorld();
    }

    @Override
    public Location getLocation() {
        return CraftLocation.toBukkit(this.position, this.getWorld());
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(this.getWorld());
            loc.setX(this.position.getX());
            loc.setY(this.position.getY());
            loc.setZ(this.position.getZ());
            loc.setRotation(0, 0);
        }

        return loc;
    }

    public BlockVector getVector() {
        return new BlockVector(this.getX(), this.getY(), this.getZ());
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
        return this.getWorld().getChunkAt(this);
    }

    public void setData(final byte data) {
        this.setData(data, net.minecraft.world.level.block.Block.UPDATE_ALL);
    }

    public void setData(final byte data, boolean applyPhysics) {
        this.setData(data, net.minecraft.world.level.block.Block.UPDATE_CLIENTS | (applyPhysics ? net.minecraft.world.level.block.Block.UPDATE_NEIGHBORS : 0));
    }

    private void setData(final byte data, @net.minecraft.world.level.block.Block.UpdateFlags int flags) {
        this.level.setBlock(this.position, CraftMagicNumbers.getBlock(this.getType(), data), flags);
    }

    @Override
    public byte getData() {
        return CraftMagicNumbers.toLegacyData(this.getBlockState());
    }

    @Override
    public BlockData getBlockData() {
        return this.getBlockState().asBlockData();
    }

    @Override
    public void setType(final Material type) {
        this.setType(type, true);
    }

    @Override
    public void setType(Material type, boolean applyPhysics) {
        Preconditions.checkArgument(type != null, "Material cannot be null");
        this.setBlockData(type.createBlockData(), applyPhysics);
    }

    @Override
    public void setBlockData(BlockData data) {
        this.setBlockData(data, true);
    }

    @Override
    public void setBlockData(BlockData data, boolean applyPhysics) {
        Preconditions.checkArgument(data != null, "BlockData cannot be null");
        this.setBlockState(((CraftBlockData) data).getState(), applyPhysics);
    }

    boolean setBlockState(final net.minecraft.world.level.block.state.BlockState state, final boolean applyPhysics) {
        return setBlockState(this.level, this.position, this.getBlockState(), state, applyPhysics);
    }

    public static boolean setBlockState(LevelAccessor world, BlockPos pos, net.minecraft.world.level.block.state.BlockState oldState, net.minecraft.world.level.block.state.BlockState newState, boolean applyPhysics) {
        // SPIGOT-611: need to do this to prevent glitchiness. Easier to handle this here (like /setblock) than to fix weirdness in block entity cleanup
        if (oldState.hasBlockEntity() && newState.getBlock() != oldState.getBlock()) { // SPIGOT-3725 remove old block entity if block changes
            // SPIGOT-4612: faster - just clear tile
            if (world instanceof net.minecraft.world.level.Level) {
                ((net.minecraft.world.level.Level) world).removeBlockEntity(pos);
            } else {
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
            }
        }

        if (applyPhysics) {
            return world.setBlock(pos, newState, net.minecraft.world.level.block.Block.UPDATE_ALL);
        } else {
            boolean success = world.setBlock(pos, newState,
                    net.minecraft.world.level.block.Block.UPDATE_CLIENTS |
                    net.minecraft.world.level.block.Block.UPDATE_KNOWN_SHAPE |
                    net.minecraft.world.level.block.Block.UPDATE_SKIP_ON_PLACE);
            if (success && world instanceof net.minecraft.world.level.Level) {
                world.getMinecraftWorld().sendBlockUpdated(
                    pos,
                    oldState,
                    newState,
                    net.minecraft.world.level.block.Block.UPDATE_ALL
                );
            }
            return success;
        }
    }

    @Override
    public Material getType() {
        return this.getBlockState().getBukkitMaterial();
    }

    @Override
    public byte getLightLevel() {
        return (byte) this.level.getMinecraftWorld().getMaxLocalRawBrightness(this.position);
    }

    @Override
    public byte getLightFromSky() {
        return (byte) this.level.getBrightness(LightLayer.SKY, this.position);
    }

    @Override
    public byte getLightFromBlocks() {
        return (byte) this.level.getBrightness(LightLayer.BLOCK, this.position);
    }

    @Override
    public Block getRelative(final int modX, final int modY, final int modZ) {
        return this.getWorld().getBlockAt(this.getX() + modX, this.getY() + modY, this.getZ() + modZ);
    }

    @Override
    public Block getRelative(BlockFace face) {
        return this.getRelative(face, 1);
    }

    @Override
    public Block getRelative(BlockFace face, int distance) {
        return this.getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }

    @Override
    public BlockFace getFace(final Block block) {
        BlockFace[] values = BlockFace.values();

        for (BlockFace face : values) {
            if ((this.getX() + face.getModX() == block.getX()) && (this.getY() + face.getModY() == block.getY()) && (this.getZ() + face.getModZ() == block.getZ())) {
                return face;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        net.minecraft.world.level.block.state.BlockState state = this.getBlockState();
        return "CraftBlock{pos=" + this.position + ", type=" + this.getType() + ", data=" + state + ", fluid=" + state.getFluidState() + '}';
    }

    public static BlockFace notchToBlockFace(@Nullable Direction direction) {
        return switch (direction) {
            case DOWN -> BlockFace.DOWN;
            case UP -> BlockFace.UP;
            case NORTH -> BlockFace.NORTH;
            case SOUTH -> BlockFace.SOUTH;
            case WEST -> BlockFace.WEST;
            case EAST -> BlockFace.EAST;
            case null -> BlockFace.SELF;
        };
    }

    public static @Nullable Direction blockFaceToNotch(@Nullable BlockFace face) {
        return switch (face) {
            case DOWN -> Direction.DOWN;
            case UP -> Direction.UP;
            case NORTH -> Direction.NORTH;
            case SOUTH -> Direction.SOUTH;
            case WEST -> Direction.WEST;
            case EAST -> Direction.EAST;
            case null, default -> null;
        };
    }

    @Override
    public org.bukkit.block.BlockState getState() {
        return CraftBlockStates.getBlockState(this);
    }

    @Override
    public org.bukkit.block.BlockState getState(boolean useSnapshot) {
        return CraftBlockStates.getBlockState(this, useSnapshot);
    }

    @Override
    public Biome getBiome() {
        return this.getWorld().getBiome(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public Biome getComputedBiome() {
        return this.getWorld().getComputedBiome(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public void setBiome(Biome bio) {
        this.getWorld().setBiome(this.getX(), this.getY(), this.getZ(), bio);
    }

    @Override
    public double getTemperature() {
        return this.level.getBiome(this.position).value().getTemperature(this.position, this.level.getSeaLevel());
    }

    @Override
    public double getHumidity() {
        return this.getWorld().getHumidity(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public boolean isBlockPowered() {
        return this.level.getMinecraftWorld().getDirectSignalTo(this.position) > Redstone.SIGNAL_MIN;
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        return this.level.getMinecraftWorld().hasNeighborSignal(this.position);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CraftBlock other)) {
            return false;
        }

        return this.position.equals(other.position) && this.getWorld().equals(other.getWorld());
    }

    @Override
    public int hashCode() {
        return this.position.hashCode() ^ this.getWorld().hashCode();
    }

    @Override
    public boolean isBlockFacePowered(BlockFace face) {
        Direction direction = blockFaceToNotch(face);
        Preconditions.checkArgument(direction != null, face + " is not a valid cartesian face");
        return this.level.getMinecraftWorld().hasSignal(this.position, direction);
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        Direction direction = blockFaceToNotch(face);
        Preconditions.checkArgument(direction != null, face + " is not a valid cartesian face");
        if (this.level.getMinecraftWorld().hasSignal(this.position, direction)) {
            return true;
        }

        BlockState state = this.level.getBlockState(this.position.relative(direction));
        if (state.hasProperty(RedStoneWireBlock.POWER)) {
            return state.getValue(RedStoneWireBlock.POWER) > Redstone.SIGNAL_MIN;
        }

        return false;
    }

    @Override
    public int getBlockPower(BlockFace face) {
        Preconditions.checkArgument(face != null, face + " cannot be null");

        net.minecraft.world.level.Level level = this.level.getMinecraftWorld();
        boolean searchSelfIncluded = face == BlockFace.SELF;
        var onlyFace = blockFaceToNotch(face);

        int power = Redstone.SIGNAL_NONE;
        for (Direction direction : SignalGetter.DIRECTIONS) {
            if (!searchSelfIncluded && direction != onlyFace) {
                continue;
            }

            BlockPos neighborPos = this.position.relative(direction);
            if (level.hasSignal(neighborPos, direction)) {
                BlockState state = level.getBlockState(neighborPos);
                if (state.hasProperty(RedStoneWireBlock.POWER)) {
                    power = Math.max(state.getValue(RedStoneWireBlock.POWER), power);
                    if (power == Redstone.SIGNAL_MAX) {
                        return power;
                    }
                }
            }
        }

        if (power != Redstone.SIGNAL_NONE) {
            return power;
        }

        if (searchSelfIncluded) {
            return this.isBlockIndirectlyPowered() ? Redstone.SIGNAL_MAX : Redstone.SIGNAL_MIN;
        }
        return this.isBlockFaceIndirectlyPowered(face) ? Redstone.SIGNAL_MAX : Redstone.SIGNAL_MIN;
    }

    @Override
    public boolean isEmpty() {
        return this.getBlockState().isAir();
    }

    @Override
    public boolean isLiquid() {
        return this.getBlockState().liquid();
    }

    @Override
    public boolean isBuildable() {
        return this.getBlockState().isSolid(); // This is in fact isSolid, despite the fact that isSolid below returns blocksMotion
    }
    @Override
    public boolean isBurnable() {
        return this.getBlockState().ignitedByLava();
    }
    @Override
    public boolean isReplaceable() {
        return this.getBlockState().canBeReplaced();
    }
    @Override
    public boolean isSolid() {
        return this.getBlockState().blocksMotion();
    }

    @Override
    public boolean isCollidable() {
        return getBlockState().getBlock().hasCollision;
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(this.getBlockState().getPistonPushReaction().ordinal());
    }

    @Override
    public boolean breakNaturally() {
        return this.breakNaturally(null);
    }

    @Override
    public boolean breakNaturally(ItemStack item) {
        return this.breakNaturally(item, false);
    }

    @Override
    public boolean breakNaturally(boolean triggerEffect, boolean dropExperience) {
        return this.breakNaturally(null, triggerEffect, dropExperience);
    }

    @Override
    public boolean breakNaturally(ItemStack item, boolean triggerEffect, boolean dropExperience) {
        return this.breakNaturally(item, triggerEffect, dropExperience, false);
    }

    @Override
    public boolean breakNaturally(ItemStack item, boolean triggerEffect, boolean dropExperience, boolean forceEffect) {
        // Order matters here, need to drop before setting to air so skulls can get their data
        BlockState state = this.getBlockState();
        net.minecraft.world.level.block.Block block = state.getBlock();
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        boolean result = false;

        // Modelled off Player#hasCorrectToolForDrops
        if (!state.isAir() && (item == null || !state.requiresCorrectToolForDrops() || nmsItem.isCorrectToolForDrops(state))) {
            net.minecraft.world.level.block.Block.dropResources(state, this.level.getMinecraftWorld(), this.position, this.level.getBlockEntity(this.position), null, nmsItem, false);
            if (dropExperience) block.popExperience(this.level.getMinecraftWorld(), this.position, block.getExpDrop(state, this.level.getMinecraftWorld(), this.position, nmsItem, true));
            result = true;
        }

        if ((result && triggerEffect) || (forceEffect && !state.isAir())) {
            if (state.getBlock() instanceof net.minecraft.world.level.block.BaseFireBlock) {
                this.level.levelEvent(LevelEvent.SOUND_EXTINGUISH_FIRE, this.position, 0);
            } else {
                this.level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, this.position, net.minecraft.world.level.block.Block.getId(state));
            }
        }

        boolean destroyed = this.level.removeBlock(this.position, false);
        if (destroyed) {
            block.destroy(this.level, this.position, state);
        }
        if (result) {
            // special cases
            if (block instanceof net.minecraft.world.level.block.IceBlock iceBlock) {
                iceBlock.afterDestroy(this.level.getMinecraftWorld(), this.position, nmsItem);
            } else if (block instanceof net.minecraft.world.level.block.TurtleEggBlock turtleEggBlock) {
                turtleEggBlock.decreaseEggs(this.level.getMinecraftWorld(), this.position, state);
            }
        }
        return destroyed && result;
    }

    @Override
    public boolean applyBoneMeal(BlockFace face) {
        Direction direction = blockFaceToNotch(face);
        Preconditions.checkArgument(direction != null, face + " is not a valid cartesian face");

        BlockFertilizeEvent event = null;
        ServerLevel world = this.getCraftWorld().getHandle();
        UseOnContext context = new UseOnContext(world, null, InteractionHand.MAIN_HAND, Items.BONE_MEAL.getDefaultInstance(), new BlockHitResult(Vec3.ZERO, direction, this.getPosition(), false));

        // SPIGOT-6895: Call StructureGrowEvent and BlockFertilizeEvent
        world.captureTreeGeneration = true;
        InteractionResult result = BoneMealItem.applyBonemeal(context);
        world.captureTreeGeneration = false;

        if (!world.capturedBlockStates.isEmpty()) {
            TreeType treeType = SaplingBlock.treeType;
            SaplingBlock.treeType = null;
            List<org.bukkit.block.BlockState> states = new ArrayList<>(world.capturedBlockStates.values());
            world.capturedBlockStates.clear();
            StructureGrowEvent structureEvent = null;

            if (treeType != null) {
                structureEvent = new StructureGrowEvent(this.getLocation(), treeType, true, null, states);
                Bukkit.getPluginManager().callEvent(structureEvent);
            }

            event = new BlockFertilizeEvent(CraftBlock.at(world, this.getPosition()), null, states);
            event.setCancelled(structureEvent != null && structureEvent.isCancelled());
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                for (org.bukkit.block.BlockState state : states) {
                    CraftBlockState craftBlockState = (CraftBlockState) state;
                    craftBlockState.place(craftBlockState.getFlags());
                    world.checkCapturedTreeStateForObserverNotify(this.position, craftBlockState);
                }
            }
        }

        return result == InteractionResult.SUCCESS && (event == null || !event.isCancelled());
    }

    @Override
    public Collection<ItemStack> getDrops() {
        return this.getDrops(null);
    }

    @Override
    public Collection<ItemStack> getDrops(ItemStack item) {
        return this.getDrops(item, null);
    }

    @Override
    public Collection<ItemStack> getDrops(ItemStack item, Entity entity) {
        BlockState state = this.getBlockState();
        net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(item);

        // Modelled off Player#hasCorrectToolForDrops
        if (item == null || CraftBlockData.isPreferredTool(state, nms)) {
            return net.minecraft.world.level.block.Block.getDrops(state, this.level.getMinecraftWorld(), this.position, this.level.getBlockEntity(this.position), entity == null ? null : ((CraftEntity) entity).getHandle(), nms)
                    .stream().map(CraftItemStack::asBukkitCopy).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean isPreferredTool(ItemStack item) {
        return CraftBlockData.isPreferredTool(this.getBlockState(), CraftItemStack.asNMSCopy(item));
    }

    @Override
    public float getBreakSpeed(Player player) {
        Preconditions.checkArgument(player != null, "player cannot be null");
        return this.getBlockState().getDestroyProgress(((CraftPlayer) player).getHandle(), this.level, this.position);
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.getCraftWorld().getBlockMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.getCraftWorld().getBlockMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return this.getCraftWorld().getBlockMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.getCraftWorld().getBlockMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public boolean isPassable() {
        return this.getBlockState().getCollisionShape(this.level, this.position).isEmpty();
    }

    @Override
    public RayTraceResult rayTrace(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode) {
        Preconditions.checkArgument(start != null, "Location start cannot be null");
        Preconditions.checkArgument(this.getWorld().equals(start.getWorld()), "Location start cannot be a different world");
        start.checkFinite();

        Preconditions.checkArgument(direction != null, "Vector direction cannot be null");
        direction.checkFinite();
        Preconditions.checkArgument(direction.lengthSquared() > 0, "Direction's magnitude (%s) must be greater than 0", direction.lengthSquared());

        Preconditions.checkArgument(fluidCollisionMode != null, "FluidCollisionMode cannot be null");
        if (maxDistance < 0.0D) {
            return null;
        }

        Vector dir = direction.clone().normalize().multiply(maxDistance);
        Vec3 startPos = CraftLocation.toVec3(start);
        Vec3 endPos = startPos.add(dir.getX(), dir.getY(), dir.getZ());

        HitResult hitResult = this.level.clip(new ClipContext(startPos, endPos, ClipContext.Block.OUTLINE, CraftFluidCollisionMode.toFluid(fluidCollisionMode), CollisionContext.empty()), this.position);
        return CraftRayTraceResult.convertFromInternal(this.level, hitResult);
    }

    @Override
    public BoundingBox getBoundingBox() {
        VoxelShape shape = this.getBlockState().getShape(this.level, this.position);

        if (shape.isEmpty()) {
            return new BoundingBox(); // Return an empty bounding box if the block has no dimension
        }

        AABB aabb = shape.bounds();
        return new BoundingBox(this.getX() + aabb.minX, this.getY() + aabb.minY, this.getZ() + aabb.minZ, this.getX() + aabb.maxX, this.getY() + aabb.maxY, this.getZ() + aabb.maxZ);
    }

    @Override
    public org.bukkit.util.VoxelShape getCollisionShape() {
        VoxelShape shape = this.getBlockState().getCollisionShape(this.level, this.position);
        return new CraftVoxelShape(shape);
    }

    @Override
    public boolean canPlace(BlockData data) {
        Preconditions.checkArgument(data != null, "BlockData cannot be null");

        BlockState state = ((CraftBlockData) data).getState();
        return state.canSurvive(this.level.getMinecraftWorld(), this.position);
    }

    @Override
    public String getTranslationKey() {
        return this.getBlockState().getBlock().getDescriptionId();
    }

    @Override
    public boolean isSuffocating() {
        return this.getBlockState().isSuffocating(this.level, this.position);
    }

    @Override
    public com.destroystokyo.paper.block.BlockSoundGroup getSoundGroup() {
        return new com.destroystokyo.paper.block.CraftBlockSoundGroup(getBlockState().getBlock().defaultBlockState().getSoundType());
    }

    @Override
    public org.bukkit.SoundGroup getBlockSoundGroup() {
        return org.bukkit.craftbukkit.CraftSoundGroup.getSoundGroup(this.getBlockState().getSoundType());
    }

    @Override
    public String translationKey() {
        return this.getBlockState().getBlock().getDescriptionId();
    }

    public boolean isValidTool(ItemStack tool) {
        return !this.getDrops(tool).isEmpty();
    }

    @Override
    public void tick() {
        final ServerLevel level = this.level.getMinecraftWorld();
        this.getBlockState().tick(level, this.position, level.getRandom());
    }


    @Override
    public void fluidTick() {
        this.level.getFluidState(this.position).tick(this.level.getMinecraftWorld(), this.position, this.getBlockState());
    }

    @Override
    public void randomTick() {
        final ServerLevel level = this.level.getMinecraftWorld();
        this.getBlockState().randomTick(level, this.position, level.getRandom());
    }
}
