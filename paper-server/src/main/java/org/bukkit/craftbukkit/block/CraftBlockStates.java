package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftWorld;

public final class CraftBlockStates {

    private abstract static class BlockStateFactory<B extends CraftBlockState> {

        public final Class<B> blockStateType;

        public BlockStateFactory(Class<B> blockStateType) {
            this.blockStateType = blockStateType;
        }

        // The given world can be null for unplaced BlockStates.
        // If the world is not null and the given block data is a block entity, the given block entity is expected to not be null.
        // Otherwise, the given block entity may or may not be null.
        // If the given block entity is not null, its position and block data are expected to match the given block position and block data.
        // In some situations, such as during chunk generation, the block entity's world may be null, even if the given world is not null.
        // If the block entity's world is not null, it is expected to match the given world.
        public abstract B createBlockState(World world, BlockPos pos, net.minecraft.world.level.block.state.BlockState state, BlockEntity blockEntity);
    }

    private static class BlockEntityStateFactory<T extends BlockEntity, B extends CraftBlockEntityState<T>> extends BlockStateFactory<B> {

        private final BiFunction<World, T, B> blockStateConstructor;
        private final BlockEntityType<? extends T> blockEntityType;

        protected BlockEntityStateFactory(Class<B> blockStateType, BiFunction<World, T, B> blockStateConstructor, BlockEntityType<? extends T> blockEntityType) {
            super(blockStateType);
            this.blockStateConstructor = blockStateConstructor;
            this.blockEntityType = blockEntityType;
        }

        @Override
        public final B createBlockState(World world, BlockPos pos, net.minecraft.world.level.block.state.BlockState state, BlockEntity blockEntity) {
            if (world != null) {
                Preconditions.checkState(blockEntity != null, "Block entity is null, asynchronous access? %s", CraftBlock.at(((CraftWorld) world).getHandle(), pos));
            } else if (blockEntity == null) {
                blockEntity = this.createBlockEntity(pos, state);
            }
            return this.createBlockState(world, (T) blockEntity);
        }

        private T createBlockEntity(BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
            return this.blockEntityType.create(pos, state);
        }

        private B createBlockState(World world, T blockEntity) {
            return this.blockStateConstructor.apply(world, blockEntity);
        }
    }

    private static final Map<Material, BlockStateFactory<?>> FACTORIES = new HashMap<>();
    private static final BlockStateFactory<?> DEFAULT_FACTORY = new BlockStateFactory<>(CraftBlockState.class) {
        @Override
        public CraftBlockState createBlockState(World world, BlockPos pos, net.minecraft.world.level.block.state.BlockState state, BlockEntity blockEntity) {
            // Paper - revert upstream's revert of the block state changes. Block entities that have already had the block type set to AIR are still valid, upstream decided to ignore them
            Preconditions.checkState(blockEntity == null, "Unexpected BlockState for %s", CraftBlockType.minecraftToBukkit(state.getBlock()));
            return new CraftBlockState(world, pos, state);
        }
    };

    private static final Map<BlockEntityType<?>, BlockStateFactory<?>> FACTORIES_BY_BLOCK_ENTITY_TYPE = new HashMap<>();
    private static void register(BlockEntityType<?> type, BlockStateFactory<?> factory) {
        FACTORIES_BY_BLOCK_ENTITY_TYPE.put(type, factory);
    }

    static {
        // Start generate - CraftBlockEntityStates
        // @GeneratedFrom 1.21.6-pre1
        register(BlockEntityType.BANNER, CraftBanner.class, CraftBanner::new);
        register(BlockEntityType.BARREL, CraftBarrel.class, CraftBarrel::new);
        register(BlockEntityType.BEACON, CraftBeacon.class, CraftBeacon::new);
        register(BlockEntityType.BED, CraftBed.class, CraftBed::new);
        register(BlockEntityType.BEEHIVE, CraftBeehive.class, CraftBeehive::new);
        register(BlockEntityType.BELL, CraftBell.class, CraftBell::new);
        register(BlockEntityType.BLAST_FURNACE, CraftBlastFurnace.class, CraftBlastFurnace::new);
        register(BlockEntityType.BREWING_STAND, CraftBrewingStand.class, CraftBrewingStand::new);
        register(BlockEntityType.BRUSHABLE_BLOCK, CraftBrushableBlock.class, CraftBrushableBlock::new);
        register(BlockEntityType.CALIBRATED_SCULK_SENSOR, CraftCalibratedSculkSensor.class, CraftCalibratedSculkSensor::new);
        register(BlockEntityType.CAMPFIRE, CraftCampfire.class, CraftCampfire::new);
        register(BlockEntityType.CHEST, CraftChest.class, CraftChest::new);
        register(BlockEntityType.CHISELED_BOOKSHELF, CraftChiseledBookshelf.class, CraftChiseledBookshelf::new);
        register(BlockEntityType.COMMAND_BLOCK, CraftCommandBlock.class, CraftCommandBlock::new);
        register(BlockEntityType.COMPARATOR, CraftComparator.class, CraftComparator::new);
        register(BlockEntityType.CONDUIT, CraftConduit.class, CraftConduit::new);
        register(BlockEntityType.CRAFTER, CraftCrafter.class, CraftCrafter::new);
        register(BlockEntityType.CREAKING_HEART, CraftCreakingHeart.class, CraftCreakingHeart::new);
        register(BlockEntityType.DAYLIGHT_DETECTOR, CraftDaylightDetector.class, CraftDaylightDetector::new);
        register(BlockEntityType.DECORATED_POT, CraftDecoratedPot.class, CraftDecoratedPot::new);
        register(BlockEntityType.DISPENSER, CraftDispenser.class, CraftDispenser::new);
        register(BlockEntityType.DROPPER, CraftDropper.class, CraftDropper::new);
        register(BlockEntityType.ENCHANTING_TABLE, CraftEnchantingTable.class, CraftEnchantingTable::new);
        register(BlockEntityType.END_GATEWAY, CraftEndGateway.class, CraftEndGateway::new);
        register(BlockEntityType.END_PORTAL, CraftEndPortal.class, CraftEndPortal::new);
        register(BlockEntityType.ENDER_CHEST, CraftEnderChest.class, CraftEnderChest::new);
        register(BlockEntityType.FURNACE, CraftFurnaceFurnace.class, CraftFurnaceFurnace::new);
        register(BlockEntityType.HANGING_SIGN, CraftHangingSign.class, CraftHangingSign::new);
        register(BlockEntityType.HOPPER, CraftHopper.class, CraftHopper::new);
        register(BlockEntityType.JIGSAW, CraftJigsaw.class, CraftJigsaw::new);
        register(BlockEntityType.JUKEBOX, CraftJukebox.class, CraftJukebox::new);
        register(BlockEntityType.LECTERN, CraftLectern.class, CraftLectern::new);
        register(BlockEntityType.MOB_SPAWNER, CraftCreatureSpawner.class, CraftCreatureSpawner::new);
        register(BlockEntityType.PISTON, CraftMovingPiston.class, CraftMovingPiston::new);
        register(BlockEntityType.SCULK_CATALYST, CraftSculkCatalyst.class, CraftSculkCatalyst::new);
        register(BlockEntityType.SCULK_SENSOR, CraftSculkSensor.class, CraftSculkSensor::new);
        register(BlockEntityType.SCULK_SHRIEKER, CraftSculkShrieker.class, CraftSculkShrieker::new);
        register(BlockEntityType.SHULKER_BOX, CraftShulkerBox.class, CraftShulkerBox::new);
        register(BlockEntityType.SIGN, CraftSign.class, CraftSign::new);
        register(BlockEntityType.SKULL, CraftSkull.class, CraftSkull::new);
        register(BlockEntityType.SMOKER, CraftSmoker.class, CraftSmoker::new);
        register(BlockEntityType.STRUCTURE_BLOCK, CraftStructureBlock.class, CraftStructureBlock::new);
        register(BlockEntityType.TEST_BLOCK, CraftTestBlock.class, CraftTestBlock::new);
        register(BlockEntityType.TEST_INSTANCE_BLOCK, CraftTestInstanceBlock.class, CraftTestInstanceBlock::new);
        register(BlockEntityType.TRAPPED_CHEST, CraftChest.class, CraftChest::new);
        register(BlockEntityType.TRIAL_SPAWNER, CraftTrialSpawner.class, CraftTrialSpawner::new);
        register(BlockEntityType.VAULT, CraftVault.class, CraftVault::new);
        // End generate - CraftBlockEntityStates
    }

    private static void register(Material blockType, BlockStateFactory<?> factory) {
        CraftBlockStates.FACTORIES.put(blockType, factory);
    }

    private static <T extends BlockEntity, B extends CraftBlockEntityState<T>> void register(
            net.minecraft.world.level.block.entity.BlockEntityType<? extends T> blockEntityType,
            Class<B> blockStateType,
            BiFunction<World, T, B> blockStateConstructor
    ) {
        BlockStateFactory<B> factory = new BlockEntityStateFactory<>(blockStateType, blockStateConstructor, blockEntityType);
        for (net.minecraft.world.level.block.Block block : blockEntityType.validBlocks) {
            CraftBlockStates.register(CraftBlockType.minecraftToBukkit(block), factory);
        }
        CraftBlockStates.register(blockEntityType, factory);
    }

    private static BlockStateFactory<?> getFactory(Material material) {
        return CraftBlockStates.FACTORIES.getOrDefault(material, CraftBlockStates.DEFAULT_FACTORY);
    }

    private static BlockStateFactory<?> getFactory(Material material, BlockEntityType<?> type) {
        if (type != null) {
            return CraftBlockStates.FACTORIES_BY_BLOCK_ENTITY_TYPE.getOrDefault(type, getFactory(material));
        } else {
            return getFactory(material);
        }
    }

    public static Class<? extends CraftBlockState> getBlockStateType(Material material) {
        Preconditions.checkNotNull(material, "material is null");
        return CraftBlockStates.getFactory(material).blockStateType;
    }

    public static BlockEntity createNewBlockEntity(Material material) {
        BlockStateFactory<?> factory = CraftBlockStates.getFactory(material);

        if (factory instanceof BlockEntityStateFactory) {
            return ((BlockEntityStateFactory<?, ?>) factory).createBlockEntity(BlockPos.ZERO, CraftBlockType.bukkitToMinecraft(material).defaultBlockState());
        }

        return null;
    }

    public static Class<? extends CraftBlockState> getBlockStateType(BlockEntityType<?> blockEntityType) {
        Preconditions.checkNotNull(blockEntityType, "blockEntityType is null");
        return CraftBlockStates.getFactory(null, blockEntityType).blockStateType;
    }

    public static BlockState getBlockState(Block block) {
        return CraftBlockStates.getBlockState(block, true);
    }

    public static BlockState getBlockState(Block block, boolean useSnapshot) {
        Preconditions.checkNotNull(block, "block is null");
        CraftBlock craftBlock = (CraftBlock) block;
        CraftWorld world = (CraftWorld) block.getWorld();
        BlockPos pos = craftBlock.getPosition();
        net.minecraft.world.level.block.state.BlockState state = craftBlock.getNMS();
        BlockEntity blockEntity = craftBlock.getHandle().getBlockEntity(pos);
        boolean prev = CraftBlockEntityState.DISABLE_SNAPSHOT;
        CraftBlockEntityState.DISABLE_SNAPSHOT = !useSnapshot;
        try {
            CraftBlockState blockState = CraftBlockStates.getBlockState(world, pos, state, blockEntity);
            blockState.setWorldHandle(craftBlock.getHandle()); // Inject the block's generator access
            return blockState;
        } finally {
            CraftBlockEntityState.DISABLE_SNAPSHOT = prev;
        }
    }

    @Deprecated
    public static BlockState getBlockState(BlockPos pos, Material material, @Nullable CompoundTag blockEntityTag) {
        return CraftBlockStates.getBlockState(CraftRegistry.getMinecraftRegistry(), pos, material, blockEntityTag);
    }

    public static BlockState getBlockState(LevelReader world, BlockPos pos, Material material, @Nullable CompoundTag blockEntityTag) {
        return CraftBlockStates.getBlockState(world.registryAccess(), pos, material, blockEntityTag);
    }

    public static BlockState getBlockState(RegistryAccess registry, BlockPos pos, Material material, @Nullable CompoundTag blockEntityTag) {
        Preconditions.checkNotNull(material, "material is null");
        net.minecraft.world.level.block.state.BlockState blockData = CraftBlockType.bukkitToMinecraft(material).defaultBlockState();
        return CraftBlockStates.getBlockState(registry, pos, blockData, blockEntityTag);
    }

    @Deprecated
    public static BlockState getBlockState(net.minecraft.world.level.block.state.BlockState state, @Nullable CompoundTag blockEntityTag) {
        return CraftBlockStates.getBlockState(CraftRegistry.getMinecraftRegistry(), BlockPos.ZERO, state, blockEntityTag);
    }

    public static BlockState getBlockState(LevelReader level, BlockPos blockPosition, net.minecraft.world.level.block.state.BlockState state, @Nullable CompoundTag blockEntityTag) {
        return CraftBlockStates.getBlockState(level.registryAccess(), blockPosition, state, blockEntityTag);
    }

    public static BlockState getBlockState(RegistryAccess registry, BlockPos pos, net.minecraft.world.level.block.state.BlockState state, @Nullable CompoundTag blockEntityTag) {
        Preconditions.checkNotNull(pos, "pos is null");
        Preconditions.checkNotNull(state, "state is null");
        BlockEntity blockEntity = (blockEntityTag == null) ? null : BlockEntity.loadStatic(pos, state, blockEntityTag, registry); // todo create block entity from the state
        return CraftBlockStates.getBlockState(null, pos, state, blockEntity);
    }

    // See BlockStateFactory#createBlockState(World, BlockPos, BlockState, BlockEntity)
    public static CraftBlockState getBlockState(World world, BlockPos pos, net.minecraft.world.level.block.state.BlockState state, BlockEntity blockEntity) {
        Material material = CraftBlockType.minecraftToBukkit(state.getBlock());
        BlockStateFactory<?> factory;
        // For some types of BlockEntity blocks (e.g. moving pistons), Minecraft may in some situations (e.g. when using Block#setType or the
        // setBlock command) not create a corresponding BlockEntity in the world. We return a normal BlockState in this case.
        if (world != null && blockEntity == null && CraftBlockStates.isBlockEntityOptional(material)) {
            factory = CraftBlockStates.DEFAULT_FACTORY;
        } else {
            factory = CraftBlockStates.getFactory(material, blockEntity != null ? blockEntity.getType() : null); // Paper
        }
        return factory.createBlockState(world, pos, state, blockEntity);
    }

    public static boolean isBlockEntityOptional(Material material) {
        return material == Material.MOVING_PISTON;
    }

    // This ignores block entity data.
    public static CraftBlockState getBlockState(LevelAccessor world, BlockPos pos) {
        return new CraftBlockState(CraftBlock.at(world, pos));
    }

    @Nullable
    public static BlockEntityType<?> getBlockEntityType(final Material material) {
        final BlockStateFactory<?> factory = org.bukkit.craftbukkit.block.CraftBlockStates.FACTORIES.get(material);
        return factory instanceof final BlockEntityStateFactory<?,?> blockEntityStateFactory ? blockEntityStateFactory.blockEntityType : null;
    }

    private CraftBlockStates() {
    }
}
