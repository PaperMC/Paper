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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.SaplingBlock;
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
import org.bukkit.block.BlockState;
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

public class CraftBlock implements Block {
    private final net.minecraft.world.level.LevelAccessor world;
    private final BlockPos position;

    public CraftBlock(LevelAccessor world, BlockPos position) {
        this.world = world;
        this.position = position.immutable();
    }

    public static CraftBlock at(LevelAccessor world, BlockPos position) {
        return new CraftBlock(world, position);
    }

    public net.minecraft.world.level.block.state.BlockState getNMS() {
        return this.world.getBlockState(this.position);
    }

    public net.minecraft.world.level.material.FluidState getNMSFluid() {
        return this.world.getFluidState(this.position);
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public LevelAccessor getHandle() {
        return this.world;
    }

    @Override
    public World getWorld() {
        return this.world.getMinecraftWorld().getWorld();
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
            loc.setYaw(0);
            loc.setPitch(0);
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
        if (applyPhysics) {
            this.setData(data, net.minecraft.world.level.block.Block.UPDATE_ALL);
        } else {
            this.setData(data, net.minecraft.world.level.block.Block.UPDATE_CLIENTS);
        }
    }

    private void setData(final byte data, int flags) {
        this.world.setBlock(this.position, CraftMagicNumbers.getBlock(this.getType(), data), flags);
    }

    @Override
    public byte getData() {
        net.minecraft.world.level.block.state.BlockState state = this.world.getBlockState(this.position);
        return CraftMagicNumbers.toLegacyData(state);
    }

    @Override
    public BlockData getBlockData() {
        return CraftBlockData.fromData(this.getNMS());
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
        return CraftBlock.setBlockState(this.world, this.position, this.getNMS(), state, applyPhysics);
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
        return this.world.getBlockState(this.position).getBukkitMaterial(); // Paper - optimise getType calls
    }

    @Override
    public byte getLightLevel() {
        return (byte) this.world.getMinecraftWorld().getMaxLocalRawBrightness(this.position);
    }

    @Override
    public byte getLightFromSky() {
        return (byte) this.world.getBrightness(LightLayer.SKY, this.position);
    }

    @Override
    public byte getLightFromBlocks() {
        return (byte) this.world.getBrightness(LightLayer.BLOCK, this.position);
    }

    public Block getFace(final BlockFace face) {
        return this.getRelative(face, 1);
    }

    public Block getFace(final BlockFace face, final int distance) {
        return this.getRelative(face, distance);
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
        return "CraftBlock{pos=" + this.position + ",type=" + this.getType() + ",data=" + this.getNMS() + ",fluid=" + this.getNMSFluid() + '}';
    }

    public static BlockFace notchToBlockFace(Direction notch) {
        if (notch == null) {
            return BlockFace.SELF;
        }
        switch (notch) {
            case DOWN:
                return BlockFace.DOWN;
            case UP:
                return BlockFace.UP;
            case NORTH:
                return BlockFace.NORTH;
            case SOUTH:
                return BlockFace.SOUTH;
            case WEST:
                return BlockFace.WEST;
            case EAST:
                return BlockFace.EAST;
            default:
                return BlockFace.SELF;
        }
    }

    public static Direction blockFaceToNotch(BlockFace face) {
        if (face == null) {
            return null;
        }
        switch (face) {
            case DOWN:
                return Direction.DOWN;
            case UP:
                return Direction.UP;
            case NORTH:
                return Direction.NORTH;
            case SOUTH:
                return Direction.SOUTH;
            case WEST:
                return Direction.WEST;
            case EAST:
                return Direction.EAST;
            default:
                return null;
        }
    }

    @Override
    public BlockState getState() {
        return CraftBlockStates.getBlockState(this);
    }

    // Paper start
    @Override
    public BlockState getState(boolean useSnapshot) {
        return CraftBlockStates.getBlockState(this, useSnapshot);
    }
    // Paper end

    @Override
    public Biome getBiome() {
        return this.getWorld().getBiome(this.getX(), this.getY(), this.getZ());
    }

    // Paper start
    @Override
    public Biome getComputedBiome() {
        return this.getWorld().getComputedBiome(this.getX(), this.getY(), this.getZ());
    }
    // Paper end

    @Override
    public void setBiome(Biome bio) {
        this.getWorld().setBiome(this.getX(), this.getY(), this.getZ(), bio);
    }

    @Override
    public double getTemperature() {
        return this.world.getBiome(this.position).value().getTemperature(this.position, this.world.getSeaLevel());
    }

    @Override
    public double getHumidity() {
        return this.getWorld().getHumidity(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public boolean isBlockPowered() {
        return this.world.getMinecraftWorld().getDirectSignalTo(this.position) > 0;
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        return this.world.getMinecraftWorld().hasNeighborSignal(this.position);
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
        return this.world.getMinecraftWorld().hasSignal(this.position, CraftBlock.blockFaceToNotch(face));
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        int power = this.world.getMinecraftWorld().getSignal(this.position, CraftBlock.blockFaceToNotch(face));

        Block relative = this.getRelative(face);
        if (relative.getType() == Material.REDSTONE_WIRE) {
            return Math.max(power, relative.getData()) > 0; // todo remove legacy usage
        }

        return power > 0;
    }

    @Override
    public int getBlockPower(BlockFace face) {
        int power = 0;
        net.minecraft.world.level.Level world = this.world.getMinecraftWorld();
        int x = this.getX();
        int y = this.getY();
        int z = this.getZ();
        if ((face == BlockFace.DOWN || face == BlockFace.SELF) && world.hasSignal(new BlockPos(x, y - 1, z), Direction.DOWN)) power = CraftBlock.getPower(power, world.getBlockState(new BlockPos(x, y - 1, z)));
        if ((face == BlockFace.UP || face == BlockFace.SELF) && world.hasSignal(new BlockPos(x, y + 1, z), Direction.UP)) power = CraftBlock.getPower(power, world.getBlockState(new BlockPos(x, y + 1, z)));
        if ((face == BlockFace.EAST || face == BlockFace.SELF) && world.hasSignal(new BlockPos(x + 1, y, z), Direction.EAST)) power = CraftBlock.getPower(power, world.getBlockState(new BlockPos(x + 1, y, z)));
        if ((face == BlockFace.WEST || face == BlockFace.SELF) && world.hasSignal(new BlockPos(x - 1, y, z), Direction.WEST)) power = CraftBlock.getPower(power, world.getBlockState(new BlockPos(x - 1, y, z)));
        if ((face == BlockFace.NORTH || face == BlockFace.SELF) && world.hasSignal(new BlockPos(x, y, z - 1), Direction.NORTH)) power = CraftBlock.getPower(power, world.getBlockState(new BlockPos(x, y, z - 1)));
        if ((face == BlockFace.SOUTH || face == BlockFace.SELF) && world.hasSignal(new BlockPos(x, y, z + 1), Direction.SOUTH)) power = CraftBlock.getPower(power, world.getBlockState(new BlockPos(x, y, z + 1)));
        return power > 0 ? power : (face == BlockFace.SELF ? this.isBlockIndirectlyPowered() : this.isBlockFaceIndirectlyPowered(face)) ? 15 : 0;
    }

    private static int getPower(int power, net.minecraft.world.level.block.state.BlockState state) {
        if (!state.is(Blocks.REDSTONE_WIRE)) {
            return power;
        } else {
            return Math.max(state.getValue(RedStoneWireBlock.POWER), power);
        }
    }

    @Override
    public int getBlockPower() {
        return this.getBlockPower(BlockFace.SELF);
    }

    @Override
    public boolean isEmpty() {
        return this.getNMS().isAir();
    }

    @Override
    public boolean isLiquid() {
        return this.getNMS().liquid();
    }

    // Paper start
    @Override
    public boolean isBuildable() {
        return this.getNMS().isSolid(); // This is in fact isSolid, despite the fact that isSolid below returns blocksMotion
    }
    @Override
    public boolean isBurnable() {
        return this.getNMS().ignitedByLava();
    }
    @Override
    public boolean isReplaceable() {
        return this.getNMS().canBeReplaced();
    }
    @Override
    public boolean isSolid() {
        return this.getNMS().blocksMotion();
    }

    @Override
    public boolean isCollidable() {
        return getNMS().getBlock().hasCollision;
    }
    // Paper end

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(this.getNMS().getPistonPushReaction().ordinal());
    }

    @Override
    public boolean breakNaturally() {
        return this.breakNaturally(null);
    }

    @Override
    public boolean breakNaturally(ItemStack item) {
        // Paper start
        return this.breakNaturally(item, false);
    }

    @Override
    public boolean breakNaturally(boolean triggerEffect, boolean dropExperience) {
        return this.breakNaturally(null, triggerEffect, dropExperience);
    }

    @Override
    public boolean breakNaturally(ItemStack item, boolean triggerEffect, boolean dropExperience) {
        // Paper end
        // Order matters here, need to drop before setting to air so skulls can get their data
        net.minecraft.world.level.block.state.BlockState state = this.getNMS();
        net.minecraft.world.level.block.Block block = state.getBlock();
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        boolean result = false;

        // Modelled off Player#hasCorrectToolForDrops
        if (block != Blocks.AIR && (item == null || !state.requiresCorrectToolForDrops() || nmsItem.isCorrectToolForDrops(state))) {
            net.minecraft.world.level.block.Block.dropResources(state, this.world.getMinecraftWorld(), this.position, this.world.getBlockEntity(this.position), null, nmsItem, false); // Paper - Properly handle xp dropping
            // Paper start - improve Block#breakNaturally
            if (triggerEffect) {
                if (state.getBlock() instanceof net.minecraft.world.level.block.BaseFireBlock) {
                    this.world.levelEvent(net.minecraft.world.level.block.LevelEvent.SOUND_EXTINGUISH_FIRE, this.position, 0);
                } else {
                    this.world.levelEvent(net.minecraft.world.level.block.LevelEvent.PARTICLES_DESTROY_BLOCK, this.position, net.minecraft.world.level.block.Block.getId(state));
                }
            }
            if (dropExperience) block.popExperience(this.world.getMinecraftWorld(), this.position, block.getExpDrop(state, this.world.getMinecraftWorld(), this.position, nmsItem, true));
            // Paper end
            result = true;
        }

        // SPIGOT-6778: Directly call setBlock instead of setBlockState, so that the block entity is not removed and custom remove logic is run.
        // Paper start - improve breakNaturally
        boolean destroyed = this.world.removeBlock(this.position, false);
        if (destroyed) {
            block.destroy(this.world, this.position, state);
        }
        if (result) {
            // special cases
            if (block instanceof net.minecraft.world.level.block.IceBlock iceBlock) {
                iceBlock.afterDestroy(this.world.getMinecraftWorld(), this.position, nmsItem);
            } else if (block instanceof net.minecraft.world.level.block.TurtleEggBlock turtleEggBlock) {
                turtleEggBlock.decreaseEggs(this.world.getMinecraftWorld(), this.position, state);
            }
        }
        return destroyed && result;
        // Paper end
    }

    @Override
    public boolean applyBoneMeal(BlockFace face) {
        Direction direction = CraftBlock.blockFaceToNotch(face);
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
            List<BlockState> states = new ArrayList<>(world.capturedBlockStates.values());
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
                for (BlockState state : states) {
                    CraftBlockState craftBlockState = (CraftBlockState) state;
                    craftBlockState.place(craftBlockState.getFlags());
                    world.checkCapturedTreeStateForObserverNotify(this.position, craftBlockState); // Paper - notify observers even if grow failed
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
        net.minecraft.world.level.block.state.BlockState state = this.getNMS();
        net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(item);

        // Modelled off Player#hasCorrectToolForDrops
        if (item == null || CraftBlockData.isPreferredTool(state, nms)) {
            return net.minecraft.world.level.block.Block.getDrops(state, this.world.getMinecraftWorld(), this.position, this.world.getBlockEntity(this.position), entity == null ? null : ((CraftEntity) entity).getHandle(), nms)
                    .stream().map(CraftItemStack::asBukkitCopy).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean isPreferredTool(ItemStack item) {
        net.minecraft.world.level.block.state.BlockState state = this.getNMS();
        net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(item);
        return CraftBlockData.isPreferredTool(state, nms);
    }

    @Override
    public float getBreakSpeed(Player player) {
        Preconditions.checkArgument(player != null, "player cannot be null");
        return this.getNMS().getDestroyProgress(((CraftPlayer) player).getHandle(), this.world, this.position);
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
        return this.getNMS().getCollisionShape(this.world, this.position).isEmpty();
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

        HitResult hitResult = this.world.clip(new ClipContext(startPos, endPos, ClipContext.Block.OUTLINE, CraftFluidCollisionMode.toFluid(fluidCollisionMode), CollisionContext.empty()), this.position);
        return CraftRayTraceResult.convertFromInternal(this.world, hitResult);
    }

    @Override
    public BoundingBox getBoundingBox() {
        VoxelShape shape = this.getNMS().getShape(this.world, this.position);

        if (shape.isEmpty()) {
            return new BoundingBox(); // Return an empty bounding box if the block has no dimension
        }

        AABB aabb = shape.bounds();
        return new BoundingBox(this.getX() + aabb.minX, this.getY() + aabb.minY, this.getZ() + aabb.minZ, this.getX() + aabb.maxX, this.getY() + aabb.maxY, this.getZ() + aabb.maxZ);
    }

    @Override
    public org.bukkit.util.VoxelShape getCollisionShape() {
        VoxelShape shape = this.getNMS().getCollisionShape(this.world, this.position);
        return new CraftVoxelShape(shape);
    }

    @Override
    public boolean canPlace(BlockData data) {
        Preconditions.checkArgument(data != null, "BlockData cannot be null");
        net.minecraft.world.level.block.state.BlockState iblockdata = ((CraftBlockData) data).getState();
        net.minecraft.world.level.Level world = this.world.getMinecraftWorld();

        return iblockdata.canSurvive(world, this.position);
    }

    @Override
    public String getTranslationKey() {
        return this.getNMS().getBlock().getDescriptionId();
    }

    @Override
    public boolean isSuffocating() {
        return this.getNMS().isSuffocating(this.world, this.position);
    }

    // Paper start
    @Override
    public com.destroystokyo.paper.block.BlockSoundGroup getSoundGroup() {
        return new com.destroystokyo.paper.block.CraftBlockSoundGroup(getNMS().getBlock().defaultBlockState().getSoundType());
    }

    @Override
    public org.bukkit.SoundGroup getBlockSoundGroup() {
        return org.bukkit.craftbukkit.CraftSoundGroup.getSoundGroup(this.getNMS().getSoundType());
    }

    @Override
    public String translationKey() {
        return this.getNMS().getBlock().getDescriptionId();
    }

    public boolean isValidTool(ItemStack itemStack) {
        return getDrops(itemStack).size() != 0;
    }

    @Override
    public void tick() {
        final ServerLevel level = this.world.getMinecraftWorld();
        this.getNMS().tick(level, this.position, level.random);
    }


    @Override
    public void fluidTick() {
        this.getNMSFluid().tick(this.world.getMinecraftWorld(), this.position, this.getNMS());
    }

    @Override
    public void randomTick() {
        final ServerLevel level = this.world.getMinecraftWorld();
        this.getNMS().randomTick(level, this.position, level.random);
    }
    // Paper end
}
