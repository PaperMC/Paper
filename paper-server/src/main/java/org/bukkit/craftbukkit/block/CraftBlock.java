package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.IRegistry;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.item.ItemBoneMeal;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.ItemActionContext;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.GeneratorAccess;
import net.minecraft.world.level.RayTrace;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.block.BlockRedstoneWire;
import net.minecraft.world.level.block.BlockTileEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.MovingObjectPosition;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang.Validate;
import org.bukkit.Chunk;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Registry;
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
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.CraftRayTraceResult;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class CraftBlock implements Block {
    private final net.minecraft.world.level.GeneratorAccess world;
    private final BlockPosition position;

    public CraftBlock(GeneratorAccess world, BlockPosition position) {
        this.world = world;
        this.position = position.immutableCopy();
    }

    public static CraftBlock at(GeneratorAccess world, BlockPosition position) {
        return new CraftBlock(world, position);
    }

    private net.minecraft.world.level.block.Block getNMSBlock() {
        return getNMS().getBlock();
    }

    public net.minecraft.world.level.block.state.IBlockData getNMS() {
        return world.getType(position);
    }

    public BlockPosition getPosition() {
        return position;
    }

    @Override
    public World getWorld() {
        return world.getMinecraftWorld().getWorld();
    }

    public CraftWorld getCraftWorld() {
        return (CraftWorld) getWorld();
    }

    @Override
    public Location getLocation() {
        return new Location(getWorld(), position.getX(), position.getY(), position.getZ());
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(position.getX());
            loc.setY(position.getY());
            loc.setZ(position.getZ());
            loc.setYaw(0);
            loc.setPitch(0);
        }

        return loc;
    }

    public BlockVector getVector() {
        return new BlockVector(getX(), getY(), getZ());
    }

    @Override
    public int getX() {
        return position.getX();
    }

    @Override
    public int getY() {
        return position.getY();
    }

    @Override
    public int getZ() {
        return position.getZ();
    }

    @Override
    public Chunk getChunk() {
        return getWorld().getChunkAt(this);
    }

    public void setData(final byte data) {
        setData(data, 3);
    }

    public void setData(final byte data, boolean applyPhysics) {
        if (applyPhysics) {
            setData(data, 3);
        } else {
            setData(data, 2);
        }
    }

    private void setData(final byte data, int flag) {
        world.setTypeAndData(position, CraftMagicNumbers.getBlock(getType(), data), flag);
    }

    @Override
    public byte getData() {
        IBlockData blockData = world.getType(position);
        return CraftMagicNumbers.toLegacyData(blockData);
    }

    @Override
    public BlockData getBlockData() {
        return CraftBlockData.fromData(getNMS());
    }

    @Override
    public void setType(final Material type) {
        setType(type, true);
    }

    @Override
    public void setType(Material type, boolean applyPhysics) {
        Preconditions.checkArgument(type != null, "Material cannot be null");
        setBlockData(type.createBlockData(), applyPhysics);
    }

    @Override
    public void setBlockData(BlockData data) {
        setBlockData(data, true);
    }

    @Override
    public void setBlockData(BlockData data, boolean applyPhysics) {
        Preconditions.checkArgument(data != null, "BlockData cannot be null");
        setTypeAndData(((CraftBlockData) data).getState(), applyPhysics);
    }

    public boolean setTypeAndData(final IBlockData blockData, final boolean applyPhysics) {
        // SPIGOT-611: need to do this to prevent glitchiness. Easier to handle this here (like /setblock) than to fix weirdness in tile entity cleanup
        if (!blockData.isAir() && blockData.getBlock() instanceof BlockTileEntity && blockData.getBlock() != getNMSBlock()) {
            // SPIGOT-4612: faster - just clear tile
            if (world instanceof net.minecraft.world.level.World) {
                ((net.minecraft.world.level.World) world).removeTileEntity(position);
            } else {
                world.setTypeAndData(position, Blocks.AIR.getBlockData(), 0);
            }
        }

        if (applyPhysics) {
            return world.setTypeAndData(position, blockData, 3);
        } else {
            IBlockData old = world.getType(position);
            boolean success = world.setTypeAndData(position, blockData, 2 | 16 | 1024); // NOTIFY | NO_OBSERVER | NO_PLACE (custom)
            if (success) {
                world.getMinecraftWorld().notify(
                        position,
                        old,
                        blockData,
                        3
                );
            }
            return success;
        }
    }

    @Override
    public Material getType() {
        return CraftMagicNumbers.getMaterial(world.getType(position).getBlock());
    }

    @Override
    public byte getLightLevel() {
        return (byte) world.getMinecraftWorld().getLightLevel(position);
    }

    @Override
    public byte getLightFromSky() {
        return (byte) world.getBrightness(EnumSkyBlock.SKY, position);
    }

    @Override
    public byte getLightFromBlocks() {
        return (byte) world.getBrightness(EnumSkyBlock.BLOCK, position);
    }

    public Block getFace(final BlockFace face) {
        return getRelative(face, 1);
    }

    public Block getFace(final BlockFace face, final int distance) {
        return getRelative(face, distance);
    }

    @Override
    public Block getRelative(final int modX, final int modY, final int modZ) {
        return getWorld().getBlockAt(getX() + modX, getY() + modY, getZ() + modZ);
    }

    @Override
    public Block getRelative(BlockFace face) {
        return getRelative(face, 1);
    }

    @Override
    public Block getRelative(BlockFace face, int distance) {
        return getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
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
        return "CraftBlock{pos=" + position + ",type=" + getType() + ",data=" + getNMS() + ",fluid=" + world.getFluid(position) + '}';
    }

    public static BlockFace notchToBlockFace(EnumDirection notch) {
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

    public static EnumDirection blockFaceToNotch(BlockFace face) {
        switch (face) {
            case DOWN:
                return EnumDirection.DOWN;
            case UP:
                return EnumDirection.UP;
            case NORTH:
                return EnumDirection.NORTH;
            case SOUTH:
                return EnumDirection.SOUTH;
            case WEST:
                return EnumDirection.WEST;
            case EAST:
                return EnumDirection.EAST;
            default:
                return null;
        }
    }

    @Override
    public BlockState getState() {
        Material material = getType();

        switch (material) {
            case ACACIA_SIGN:
            case ACACIA_WALL_SIGN:
            case BIRCH_SIGN:
            case BIRCH_WALL_SIGN:
            case CRIMSON_SIGN:
            case CRIMSON_WALL_SIGN:
            case DARK_OAK_SIGN:
            case DARK_OAK_WALL_SIGN:
            case JUNGLE_SIGN:
            case JUNGLE_WALL_SIGN:
            case OAK_SIGN:
            case OAK_WALL_SIGN:
            case SPRUCE_SIGN:
            case SPRUCE_WALL_SIGN:
            case WARPED_SIGN:
            case WARPED_WALL_SIGN:
                return new CraftSign(this);
            case CHEST:
            case TRAPPED_CHEST:
                return new CraftChest(this);
            case FURNACE:
                return new CraftFurnaceFurnace(this);
            case DISPENSER:
                return new CraftDispenser(this);
            case DROPPER:
                return new CraftDropper(this);
            case END_GATEWAY:
                return new CraftEndGateway(this);
            case HOPPER:
                return new CraftHopper(this);
            case SPAWNER:
                return new CraftCreatureSpawner(this);
            case JUKEBOX:
                return new CraftJukebox(this);
            case BREWING_STAND:
                return new CraftBrewingStand(this);
            case CREEPER_HEAD:
            case CREEPER_WALL_HEAD:
            case DRAGON_HEAD:
            case DRAGON_WALL_HEAD:
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
            case SKELETON_SKULL:
            case SKELETON_WALL_SKULL:
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
            case ZOMBIE_HEAD:
            case ZOMBIE_WALL_HEAD:
                return new CraftSkull(this);
            case COMMAND_BLOCK:
            case CHAIN_COMMAND_BLOCK:
            case REPEATING_COMMAND_BLOCK:
                return new CraftCommandBlock(this);
            case BEACON:
                return new CraftBeacon(this);
            case BLACK_BANNER:
            case BLACK_WALL_BANNER:
            case BLUE_BANNER:
            case BLUE_WALL_BANNER:
            case BROWN_BANNER:
            case BROWN_WALL_BANNER:
            case CYAN_BANNER:
            case CYAN_WALL_BANNER:
            case GRAY_BANNER:
            case GRAY_WALL_BANNER:
            case GREEN_BANNER:
            case GREEN_WALL_BANNER:
            case LIGHT_BLUE_BANNER:
            case LIGHT_BLUE_WALL_BANNER:
            case LIGHT_GRAY_BANNER:
            case LIGHT_GRAY_WALL_BANNER:
            case LIME_BANNER:
            case LIME_WALL_BANNER:
            case MAGENTA_BANNER:
            case MAGENTA_WALL_BANNER:
            case ORANGE_BANNER:
            case ORANGE_WALL_BANNER:
            case PINK_BANNER:
            case PINK_WALL_BANNER:
            case PURPLE_BANNER:
            case PURPLE_WALL_BANNER:
            case RED_BANNER:
            case RED_WALL_BANNER:
            case WHITE_BANNER:
            case WHITE_WALL_BANNER:
            case YELLOW_BANNER:
            case YELLOW_WALL_BANNER:
                return new CraftBanner(this);
            case STRUCTURE_BLOCK:
                return new CraftStructureBlock(this);
            case SHULKER_BOX:
            case WHITE_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case LIGHT_GRAY_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case BLACK_SHULKER_BOX:
                return new CraftShulkerBox(this);
            case ENCHANTING_TABLE:
                return new CraftEnchantingTable(this);
            case ENDER_CHEST:
                return new CraftEnderChest(this);
            case DAYLIGHT_DETECTOR:
                return new CraftDaylightDetector(this);
            case COMPARATOR:
                return new CraftComparator(this);
            case BLACK_BED:
            case BLUE_BED:
            case BROWN_BED:
            case CYAN_BED:
            case GRAY_BED:
            case GREEN_BED:
            case LIGHT_BLUE_BED:
            case LIGHT_GRAY_BED:
            case LIME_BED:
            case MAGENTA_BED:
            case ORANGE_BED:
            case PINK_BED:
            case PURPLE_BED:
            case RED_BED:
            case WHITE_BED:
            case YELLOW_BED:
                return new CraftBed(this);
            case CONDUIT:
                return new CraftConduit(this);
            case BARREL:
                return new CraftBarrel(this);
            case BELL:
                return new CraftBell(this);
            case BLAST_FURNACE:
                return new CraftBlastFurnace(this);
            case CAMPFIRE:
            case SOUL_CAMPFIRE:
                return new CraftCampfire(this);
            case JIGSAW:
                return new CraftJigsaw(this);
            case LECTERN:
                return new CraftLectern(this);
            case SMOKER:
                return new CraftSmoker(this);
            case BEEHIVE:
            case BEE_NEST:
                return new CraftBeehive(this);
            case SCULK_SENSOR:
                return new CraftSculkSensor(this);
            default:
                TileEntity tileEntity = world.getTileEntity(position);
                if (tileEntity != null) {
                    // block with unhandled TileEntity:
                    return new CraftBlockEntityState<TileEntity>(this, (Class<TileEntity>) tileEntity.getClass());
                } else {
                    // Block without TileEntity:
                    return new CraftBlockState(this);
                }
        }
    }

    @Override
    public Biome getBiome() {
        return getWorld().getBiome(getX(), getY(), getZ());
    }

    @Override
    public void setBiome(Biome bio) {
        getWorld().setBiome(getX(), getY(), getZ(), bio);
    }

    public static Biome biomeBaseToBiome(IRegistry<BiomeBase> registry, BiomeBase base) {
        if (base == null) {
            return null;
        }

        Biome biome = Registry.BIOME.get(CraftNamespacedKey.fromMinecraft(registry.getKey(base)));
        return (biome == null) ? Biome.CUSTOM : biome;
    }

    public static BiomeBase biomeToBiomeBase(IRegistry<BiomeBase> registry, Biome bio) {
        if (bio == null || bio == Biome.CUSTOM) {
            return null;
        }

        return registry.get(CraftNamespacedKey.toMinecraft(bio.getKey()));
    }

    @Override
    public double getTemperature() {
        return world.getBiome(position).getAdjustedTemperature(position);
    }

    @Override
    public double getHumidity() {
        return getWorld().getHumidity(getX(), getY(), getZ());
    }

    @Override
    public boolean isBlockPowered() {
        return world.getMinecraftWorld().getBlockPower(position) > 0;
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        return world.getMinecraftWorld().isBlockIndirectlyPowered(position);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CraftBlock)) {
            return false;
        }
        CraftBlock other = (CraftBlock) o;

        return this.position.equals(other.position) && this.getWorld().equals(other.getWorld());
    }

    @Override
    public int hashCode() {
        return this.position.hashCode() ^ this.getWorld().hashCode();
    }

    @Override
    public boolean isBlockFacePowered(BlockFace face) {
        return world.getMinecraftWorld().isBlockFacePowered(position, blockFaceToNotch(face));
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        int power = world.getMinecraftWorld().getBlockFacePower(position, blockFaceToNotch(face));

        Block relative = getRelative(face);
        if (relative.getType() == Material.REDSTONE_WIRE) {
            return Math.max(power, relative.getData()) > 0;
        }

        return power > 0;
    }

    @Override
    public int getBlockPower(BlockFace face) {
        int power = 0;
        net.minecraft.world.level.World world = this.world.getMinecraftWorld();
        int x = getX();
        int y = getY();
        int z = getZ();
        if ((face == BlockFace.DOWN || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(x, y - 1, z), EnumDirection.DOWN)) power = getPower(power, world.getType(new BlockPosition(x, y - 1, z)));
        if ((face == BlockFace.UP || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(x, y + 1, z), EnumDirection.UP)) power = getPower(power, world.getType(new BlockPosition(x, y + 1, z)));
        if ((face == BlockFace.EAST || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(x + 1, y, z), EnumDirection.EAST)) power = getPower(power, world.getType(new BlockPosition(x + 1, y, z)));
        if ((face == BlockFace.WEST || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(x - 1, y, z), EnumDirection.WEST)) power = getPower(power, world.getType(new BlockPosition(x - 1, y, z)));
        if ((face == BlockFace.NORTH || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(x, y, z - 1), EnumDirection.NORTH)) power = getPower(power, world.getType(new BlockPosition(x, y, z - 1)));
        if ((face == BlockFace.SOUTH || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(x, y, z + 1), EnumDirection.SOUTH)) power = getPower(power, world.getType(new BlockPosition(x, y, z + 1)));
        return power > 0 ? power : (face == BlockFace.SELF ? isBlockIndirectlyPowered() : isBlockFaceIndirectlyPowered(face)) ? 15 : 0;
    }

    private static int getPower(int i, IBlockData iblockdata) {
        if (!iblockdata.a(Blocks.REDSTONE_WIRE)) {
            return i;
        } else {
            int j = iblockdata.get(BlockRedstoneWire.POWER);

            return j > i ? j : i;
        }
    }

    @Override
    public int getBlockPower() {
        return getBlockPower(BlockFace.SELF);
    }

    @Override
    public boolean isEmpty() {
        return getNMS().isAir();
    }

    @Override
    public boolean isLiquid() {
        return getNMS().getMaterial().isLiquid();
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(getNMS().getPushReaction().ordinal());
    }

    @Override
    public boolean breakNaturally() {
        return breakNaturally(null);
    }

    @Override
    public boolean breakNaturally(ItemStack item) {
        // Order matters here, need to drop before setting to air so skulls can get their data
        net.minecraft.world.level.block.state.IBlockData iblockdata = this.getNMS();
        net.minecraft.world.level.block.Block block = iblockdata.getBlock();
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        boolean result = false;

        // Modelled off EntityHuman#hasBlock
        if (block != Blocks.AIR && (item == null || !iblockdata.isRequiresSpecialTool() || nmsItem.canDestroySpecialBlock(iblockdata))) {
            net.minecraft.world.level.block.Block.dropItems(iblockdata, world.getMinecraftWorld(), position, world.getTileEntity(position), null, nmsItem);
            result = true;
        }

        return setTypeAndData(Blocks.AIR.getBlockData(), true) && result;
    }

    @Override
    public boolean applyBoneMeal(BlockFace face) {
        EnumDirection direction = blockFaceToNotch(face);
        ItemActionContext context = new ItemActionContext(getCraftWorld().getHandle(), null, EnumHand.MAIN_HAND, Items.BONE_MEAL.createItemStack(), new MovingObjectPositionBlock(Vec3D.ZERO, direction, getPosition(), false));

        return ItemBoneMeal.applyBonemeal(context) == EnumInteractionResult.SUCCESS;
    }

    @Override
    public Collection<ItemStack> getDrops() {
        return getDrops(null);
    }

    @Override
    public Collection<ItemStack> getDrops(ItemStack item) {
        return getDrops(item, null);
    }

    @Override
    public Collection<ItemStack> getDrops(ItemStack item, Entity entity) {
        IBlockData iblockdata = getNMS();
        net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(item);

        // Modelled off EntityHuman#hasBlock
        if (item == null || isPreferredTool(iblockdata, nms)) {
            return net.minecraft.world.level.block.Block.getDrops(iblockdata, (WorldServer) world.getMinecraftWorld(), position, world.getTileEntity(position), entity == null ? null : ((CraftEntity) entity).getHandle(), nms)
                    .stream().map(CraftItemStack::asBukkitCopy).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean isPreferredTool(ItemStack item) {
        IBlockData iblockdata = getNMS();
        net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(item);
        return isPreferredTool(iblockdata, nms);
    }

    @Override
    public float getBreakSpeed(Player player) {
        Preconditions.checkArgument(player != null, "player cannot be null");
        return getNMS().getDamage(((CraftPlayer) player).getHandle(), world, position);
    }

    private boolean isPreferredTool(IBlockData iblockdata, net.minecraft.world.item.ItemStack nmsItem) {
        return !iblockdata.isRequiresSpecialTool() || nmsItem.canDestroySpecialBlock(iblockdata);
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        getCraftWorld().getBlockMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return getCraftWorld().getBlockMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return getCraftWorld().getBlockMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        getCraftWorld().getBlockMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public boolean isPassable() {
        return this.getNMS().getCollisionShape(world, position).isEmpty();
    }

    @Override
    public RayTraceResult rayTrace(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode) {
        Validate.notNull(start, "Start location is null!");
        Validate.isTrue(this.getWorld().equals(start.getWorld()), "Start location is from different world!");
        start.checkFinite();

        Validate.notNull(direction, "Direction is null!");
        direction.checkFinite();
        Validate.isTrue(direction.lengthSquared() > 0, "Direction's magnitude is 0!");

        Validate.notNull(fluidCollisionMode, "Fluid collision mode is null!");
        if (maxDistance < 0.0D) {
            return null;
        }

        Vector dir = direction.clone().normalize().multiply(maxDistance);
        Vec3D startPos = new Vec3D(start.getX(), start.getY(), start.getZ());
        Vec3D endPos = new Vec3D(start.getX() + dir.getX(), start.getY() + dir.getY(), start.getZ() + dir.getZ());

        MovingObjectPosition nmsHitResult = world.rayTraceBlock(new RayTrace(startPos, endPos, RayTrace.BlockCollisionOption.OUTLINE, CraftFluidCollisionMode.toNMS(fluidCollisionMode), null), position);
        return CraftRayTraceResult.fromNMS(this.getWorld(), nmsHitResult);
    }

    @Override
    public BoundingBox getBoundingBox() {
        VoxelShape shape = getNMS().getShape(world, position);

        if (shape.isEmpty()) {
            return new BoundingBox(); // Return an empty bounding box if the block has no dimension
        }

        AxisAlignedBB aabb = shape.getBoundingBox();
        return new BoundingBox(getX() + aabb.minX, getY() + aabb.minY, getZ() + aabb.minZ, getX() + aabb.maxX, getY() + aabb.maxY, getZ() + aabb.maxZ);
    }
}
