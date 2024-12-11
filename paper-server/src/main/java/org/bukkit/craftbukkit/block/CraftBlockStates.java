package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType; // Paper
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.entity.CalibratedSculkSensorBlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.entity.CreakingHeartBlockEntity;
import net.minecraft.world.level.block.entity.DaylightDetectorBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.DropperBlockEntity;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
import net.minecraft.world.level.block.entity.SculkSensorBlockEntity;
import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftWorld;

public final class CraftBlockStates {

    private abstract static class BlockStateFactory<B extends CraftBlockState> {

        public final Class<B> blockStateType;

        public BlockStateFactory(Class<B> blockStateType) {
            this.blockStateType = blockStateType;
        }

        // The given world can be null for unplaced BlockStates.
        // If the world is not null and the given block data is a tile entity, the given tile entity is expected to not be null.
        // Otherwise, the given tile entity may or may not be null.
        // If the given tile entity is not null, its position and block data are expected to match the given block position and block data.
        // In some situations, such as during chunk generation, the tile entity's world may be null, even if the given world is not null.
        // If the tile entity's world is not null, it is expected to match the given world.
        public abstract B createBlockState(World world, BlockPos blockPosition, net.minecraft.world.level.block.state.BlockState blockData, BlockEntity tileEntity);
    }

    private static class BlockEntityStateFactory<T extends BlockEntity, B extends CraftBlockEntityState<T>> extends BlockStateFactory<B> {

        private final BiFunction<World, T, B> blockStateConstructor;
        private final BlockEntityType<? extends T> tileEntityConstructor; // Paper

        protected BlockEntityStateFactory(Class<B> blockStateType, BiFunction<World, T, B> blockStateConstructor, BlockEntityType<? extends T> tileEntityConstructor) { // Paper
            super(blockStateType);
            this.blockStateConstructor = blockStateConstructor;
            this.tileEntityConstructor = tileEntityConstructor;
        }

        @Override
        public final B createBlockState(World world, BlockPos blockPosition, net.minecraft.world.level.block.state.BlockState blockData, BlockEntity tileEntity) {
            if (world != null) {
                Preconditions.checkState(tileEntity != null, "Tile is null, asynchronous access? %s", CraftBlock.at(((CraftWorld) world).getHandle(), blockPosition));
            } else if (tileEntity == null) {
                tileEntity = this.createTileEntity(blockPosition, blockData);
            }
            return this.createBlockState(world, (T) tileEntity);
        }

        private T createTileEntity(BlockPos blockPosition, net.minecraft.world.level.block.state.BlockState blockData) {
            return this.tileEntityConstructor.create(blockPosition, blockData); // Paper
        }

        private B createBlockState(World world, T tileEntity) {
            return this.blockStateConstructor.apply(world, tileEntity);
        }
    }

    private static final Map<Material, BlockStateFactory<?>> FACTORIES = new HashMap<>();
    private static final BlockStateFactory<?> DEFAULT_FACTORY = new BlockStateFactory<CraftBlockState>(CraftBlockState.class) {
        @Override
        public CraftBlockState createBlockState(World world, BlockPos blockPosition, net.minecraft.world.level.block.state.BlockState blockData, BlockEntity tileEntity) {
            // Paper - revert upstream's revert of the block state changes. Block entities that have already had the block type set to AIR are still valid, upstream decided to ignore them
            Preconditions.checkState(tileEntity == null, "Unexpected BlockState for %s", CraftBlockType.minecraftToBukkit(blockData.getBlock()));
            return new CraftBlockState(world, blockPosition, blockData);
        }
    };
    // Paper start
    private static final Map<BlockEntityType<?>, BlockStateFactory<?>> FACTORIES_BY_BLOCK_ENTITY_TYPE = new HashMap<>();
    private static void register(BlockEntityType<?> type, BlockStateFactory<?> factory) {
        FACTORIES_BY_BLOCK_ENTITY_TYPE.put(type, factory);
    }
    // Paper end

    static {
        // Paper start - simplify
        register(BlockEntityType.SIGN, CraftSign.class, CraftSign::new);
        register(BlockEntityType.HANGING_SIGN, CraftHangingSign.class, CraftHangingSign::new);
        register(BlockEntityType.SKULL, CraftSkull.class, CraftSkull::new);
        register(BlockEntityType.COMMAND_BLOCK, CraftCommandBlock.class, CraftCommandBlock::new);
        register(BlockEntityType.BANNER, CraftBanner.class, CraftBanner::new);
        register(BlockEntityType.SHULKER_BOX, CraftShulkerBox.class, CraftShulkerBox::new);
        register(BlockEntityType.BED, CraftBed.class, CraftBed::new);
        register(BlockEntityType.BEEHIVE, CraftBeehive.class, CraftBeehive::new);
        register(BlockEntityType.CAMPFIRE, CraftCampfire.class, CraftCampfire::new);
        register(BlockEntityType.BARREL, CraftBarrel.class, CraftBarrel::new);
        register(BlockEntityType.BEACON, CraftBeacon.class, CraftBeacon::new);
        register(BlockEntityType.BELL, CraftBell.class, CraftBell::new);
        register(BlockEntityType.BLAST_FURNACE, CraftBlastFurnace.class, CraftBlastFurnace::new);
        register(BlockEntityType.BREWING_STAND, CraftBrewingStand.class, CraftBrewingStand::new);
        register(BlockEntityType.CHEST, CraftChest.class, CraftChest::new);
        register(BlockEntityType.CHISELED_BOOKSHELF, CraftChiseledBookshelf.class, CraftChiseledBookshelf::new);
        register(BlockEntityType.COMPARATOR, CraftComparator.class, CraftComparator::new);
        register(BlockEntityType.CONDUIT, CraftConduit.class, CraftConduit::new);
        register(BlockEntityType.CREAKING_HEART, CraftCreakingHeart.class, CraftCreakingHeart::new);
        register(BlockEntityType.DAYLIGHT_DETECTOR, CraftDaylightDetector.class, CraftDaylightDetector::new);
        register(BlockEntityType.DECORATED_POT, CraftDecoratedPot.class, CraftDecoratedPot::new);
        register(BlockEntityType.DISPENSER, CraftDispenser.class, CraftDispenser::new);
        register(BlockEntityType.DROPPER, CraftDropper.class, CraftDropper::new);
        register(BlockEntityType.ENCHANTING_TABLE, CraftEnchantingTable.class, CraftEnchantingTable::new);
        register(BlockEntityType.ENDER_CHEST, CraftEnderChest.class, CraftEnderChest::new);
        register(BlockEntityType.END_GATEWAY, CraftEndGateway.class, CraftEndGateway::new);
        register(BlockEntityType.END_PORTAL, CraftEndPortal.class, CraftEndPortal::new);
        register(BlockEntityType.FURNACE, CraftFurnaceFurnace.class, CraftFurnaceFurnace::new);
        register(BlockEntityType.HOPPER, CraftHopper.class, CraftHopper::new);
        register(BlockEntityType.JIGSAW, CraftJigsaw.class, CraftJigsaw::new);
        register(BlockEntityType.JUKEBOX, CraftJukebox.class, CraftJukebox::new);
        register(BlockEntityType.LECTERN, CraftLectern.class, CraftLectern::new);
        register(BlockEntityType.PISTON, CraftMovingPiston.class, CraftMovingPiston::new);
        register(BlockEntityType.SCULK_CATALYST, CraftSculkCatalyst.class, CraftSculkCatalyst::new);
        register(BlockEntityType.SCULK_SENSOR, CraftSculkSensor.class, CraftSculkSensor::new);
        register(BlockEntityType.SCULK_SHRIEKER, CraftSculkShrieker.class, CraftSculkShrieker::new);
        register(BlockEntityType.CALIBRATED_SCULK_SENSOR, CraftCalibratedSculkSensor.class, CraftCalibratedSculkSensor::new);
        register(BlockEntityType.SMOKER, CraftSmoker.class, CraftSmoker::new);
        register(BlockEntityType.MOB_SPAWNER, CraftCreatureSpawner.class, CraftCreatureSpawner::new);
        register(BlockEntityType.STRUCTURE_BLOCK, CraftStructureBlock.class, CraftStructureBlock::new);
        register(BlockEntityType.BRUSHABLE_BLOCK, CraftBrushableBlock.class, CraftBrushableBlock::new); // note: spigot still uses CraftSuspiciousSand impl for that block type
        register(BlockEntityType.TRAPPED_CHEST, CraftChest.class, CraftChest::new);
        register(BlockEntityType.CRAFTER, CraftCrafter.class, CraftCrafter::new);
        register(BlockEntityType.TRIAL_SPAWNER, CraftTrialSpawner.class, CraftTrialSpawner::new);
        register(BlockEntityType.VAULT, CraftVault.class, CraftVault::new);
        // Paper end
    }

    private static void register(Material blockType, BlockStateFactory<?> factory) {
        CraftBlockStates.FACTORIES.put(blockType, factory);
    }

    private static <T extends BlockEntity, B extends CraftBlockEntityState<T>> void register(
            net.minecraft.world.level.block.entity.BlockEntityType<? extends T> blockEntityType, // Paper
            Class<B> blockStateType,
            BiFunction<World, T, B> blockStateConstructor // Paper
    ) {
        // Paper start
        BlockStateFactory<B> factory = new BlockEntityStateFactory<>(blockStateType, blockStateConstructor, blockEntityType); // Paper
        for (net.minecraft.world.level.block.Block block : blockEntityType.validBlocks) {
            CraftBlockStates.register(CraftBlockType.minecraftToBukkit(block), factory);
        }
        CraftBlockStates.register(blockEntityType, factory);
        // Paper end
    }

    private static BlockStateFactory<?> getFactory(Material material) {
        return CraftBlockStates.FACTORIES.getOrDefault(material, CraftBlockStates.DEFAULT_FACTORY);
    }

    // Paper start
    private static BlockStateFactory<?> getFactory(Material material, BlockEntityType<?> type) {
        if (type != null) {
            return CraftBlockStates.FACTORIES_BY_BLOCK_ENTITY_TYPE.getOrDefault(type, getFactory(material));
        } else {
            return getFactory(material);
        }
    }
    // Paper end

    public static Class<? extends CraftBlockState> getBlockStateType(Material material) {
        Preconditions.checkNotNull(material, "material is null");
        return CraftBlockStates.getFactory(material).blockStateType;
    }

    public static BlockEntity createNewTileEntity(Material material) {
        BlockStateFactory<?> factory = CraftBlockStates.getFactory(material);

        if (factory instanceof BlockEntityStateFactory) {
            return ((BlockEntityStateFactory<?, ?>) factory).createTileEntity(BlockPos.ZERO, CraftBlockType.bukkitToMinecraft(material).defaultBlockState());
        }

        return null;
    }

    // Paper start
    public static Class<? extends CraftBlockState> getBlockStateType(BlockEntityType<?> blockEntityType) {
        Preconditions.checkNotNull(blockEntityType, "blockEntityType is null");
        return CraftBlockStates.getFactory(null, blockEntityType).blockStateType;
    }
    // Paper end

    public static BlockState getBlockState(Block block) {
        // Paper start
        return CraftBlockStates.getBlockState(block, true);
    }
    public static BlockState getBlockState(Block block, boolean useSnapshot) {
        // Paper end
        Preconditions.checkNotNull(block, "block is null");
        CraftBlock craftBlock = (CraftBlock) block;
        CraftWorld world = (CraftWorld) block.getWorld();
        BlockPos blockPosition = craftBlock.getPosition();
        net.minecraft.world.level.block.state.BlockState blockData = craftBlock.getNMS();
        BlockEntity tileEntity = craftBlock.getHandle().getBlockEntity(blockPosition);
        // Paper start - block state snapshots
        boolean prev = CraftBlockEntityState.DISABLE_SNAPSHOT;
        CraftBlockEntityState.DISABLE_SNAPSHOT = !useSnapshot;
        try {
        // Paper end
        CraftBlockState blockState = CraftBlockStates.getBlockState(world, blockPosition, blockData, tileEntity);
        blockState.setWorldHandle(craftBlock.getHandle()); // Inject the block's generator access
        return blockState;
        // Paper start
        } finally {
            CraftBlockEntityState.DISABLE_SNAPSHOT = prev;
        }
        // Paper end
    }

    @Deprecated
    public static BlockState getBlockState(BlockPos blockPosition, Material material, @Nullable CompoundTag blockEntityTag) {
        return CraftBlockStates.getBlockState(MinecraftServer.getDefaultRegistryAccess(), blockPosition, material, blockEntityTag);
    }

    public static BlockState getBlockState(LevelReader world, BlockPos blockPosition, Material material, @Nullable CompoundTag blockEntityTag) {
        return CraftBlockStates.getBlockState(world.registryAccess(), blockPosition, material, blockEntityTag);
    }

    public static BlockState getBlockState(RegistryAccess registry, BlockPos blockPosition, Material material, @Nullable CompoundTag blockEntityTag) {
        Preconditions.checkNotNull(material, "material is null");
        net.minecraft.world.level.block.state.BlockState blockData = CraftBlockType.bukkitToMinecraft(material).defaultBlockState();
        return CraftBlockStates.getBlockState(registry, blockPosition, blockData, blockEntityTag);
    }

    @Deprecated
    public static BlockState getBlockState(net.minecraft.world.level.block.state.BlockState blockData, @Nullable CompoundTag blockEntityTag) {
        return CraftBlockStates.getBlockState(MinecraftServer.getDefaultRegistryAccess(), BlockPos.ZERO, blockData, blockEntityTag);
    }

    public static BlockState getBlockState(LevelReader world, BlockPos blockPosition, net.minecraft.world.level.block.state.BlockState blockData, @Nullable CompoundTag blockEntityTag) {
        return CraftBlockStates.getBlockState(world.registryAccess(), blockPosition, blockData, blockEntityTag);
    }

    public static BlockState getBlockState(RegistryAccess registry, BlockPos blockPosition, net.minecraft.world.level.block.state.BlockState blockData, @Nullable CompoundTag blockEntityTag) {
        Preconditions.checkNotNull(blockPosition, "blockPosition is null");
        Preconditions.checkNotNull(blockData, "blockData is null");
        BlockEntity tileEntity = (blockEntityTag == null) ? null : BlockEntity.loadStatic(blockPosition, blockData, blockEntityTag, registry);
        return CraftBlockStates.getBlockState(null, blockPosition, blockData, tileEntity);
    }

    // See BlockStateFactory#createBlockState(World, BlockPosition, IBlockData, TileEntity)
    public static CraftBlockState getBlockState(World world, BlockPos blockPosition, net.minecraft.world.level.block.state.BlockState blockData, BlockEntity tileEntity) {
        Material material = CraftBlockType.minecraftToBukkit(blockData.getBlock());
        BlockStateFactory<?> factory;
        // For some types of TileEntity blocks (eg. moving pistons), Minecraft may in some situations (eg. when using Block#setType or the
        // setBlock command) not create a corresponding TileEntity in the world. We return a normal BlockState in this case.
        if (world != null && tileEntity == null && CraftBlockStates.isTileEntityOptional(material)) {
            factory = CraftBlockStates.DEFAULT_FACTORY;
        } else {
            factory = CraftBlockStates.getFactory(material, tileEntity != null ? tileEntity.getType() : null); // Paper
        }
        return factory.createBlockState(world, blockPosition, blockData, tileEntity);
    }

    public static boolean isTileEntityOptional(Material material) {
        return material == Material.MOVING_PISTON;
    }

    // This ignores tile entity data.
    public static CraftBlockState getBlockState(LevelAccessor world, BlockPos pos) {
        return new CraftBlockState(CraftBlock.at(world, pos));
    }

    // This ignores tile entity data.
    public static CraftBlockState getBlockState(LevelAccessor world, BlockPos pos, int flag) {
        return new CraftBlockState(CraftBlock.at(world, pos), flag);
    }

    // Paper start
    @Nullable
    public static BlockEntityType<?> getBlockEntityType(final Material material) {
        final BlockStateFactory<?> factory = org.bukkit.craftbukkit.block.CraftBlockStates.FACTORIES.get(material);
        return factory instanceof final BlockEntityStateFactory<?,?> blockEntityStateFactory ? blockEntityStateFactory.tileEntityConstructor : null;
    }
    // Paper end

    private CraftBlockStates() {
    }
}
