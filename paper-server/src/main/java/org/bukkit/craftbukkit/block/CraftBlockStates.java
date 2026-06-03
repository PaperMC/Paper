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
import net.minecraft.world.level.block.entity.BlockEntityTypes;
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
        register(BlockEntityTypes.BANNER, CraftBanner.class, CraftBanner::new);
        register(BlockEntityTypes.BARREL, CraftBarrel.class, CraftBarrel::new);
        register(BlockEntityTypes.BEACON, CraftBeacon.class, CraftBeacon::new);
        register(BlockEntityTypes.BEEHIVE, CraftBeehive.class, CraftBeehive::new);
        register(BlockEntityTypes.BELL, CraftBell.class, CraftBell::new);
        register(BlockEntityTypes.BLAST_FURNACE, CraftBlastFurnace.class, CraftBlastFurnace::new);
        register(BlockEntityTypes.BREWING_STAND, CraftBrewingStand.class, CraftBrewingStand::new);
        register(BlockEntityTypes.BRUSHABLE_BLOCK, CraftBrushableBlock.class, CraftBrushableBlock::new);
        register(BlockEntityTypes.CALIBRATED_SCULK_SENSOR, CraftCalibratedSculkSensor.class, CraftCalibratedSculkSensor::new);
        register(BlockEntityTypes.CAMPFIRE, CraftCampfire.class, CraftCampfire::new);
        register(BlockEntityTypes.CHEST, CraftChest.class, CraftChest::new);
        register(BlockEntityTypes.CHISELED_BOOKSHELF, CraftChiseledBookshelf.class, CraftChiseledBookshelf::new);
        register(BlockEntityTypes.COMMAND_BLOCK, CraftCommandBlock.class, CraftCommandBlock::new);
        register(BlockEntityTypes.COMPARATOR, CraftComparator.class, CraftComparator::new);
        register(BlockEntityTypes.CONDUIT, CraftConduit.class, CraftConduit::new);
        register(BlockEntityTypes.COPPER_GOLEM_STATUE, CraftCopperGolemStatue.class, CraftCopperGolemStatue::new);
        register(BlockEntityTypes.CRAFTER, CraftCrafter.class, CraftCrafter::new);
        register(BlockEntityTypes.CREAKING_HEART, CraftCreakingHeart.class, CraftCreakingHeart::new);
        register(BlockEntityTypes.DAYLIGHT_DETECTOR, CraftDaylightDetector.class, CraftDaylightDetector::new);
        register(BlockEntityTypes.DECORATED_POT, CraftDecoratedPot.class, CraftDecoratedPot::new);
        register(BlockEntityTypes.DISPENSER, CraftDispenser.class, CraftDispenser::new);
        register(BlockEntityTypes.DROPPER, CraftDropper.class, CraftDropper::new);
        register(BlockEntityTypes.ENCHANTING_TABLE, CraftEnchantingTable.class, CraftEnchantingTable::new);
        register(BlockEntityTypes.END_GATEWAY, CraftEndGateway.class, CraftEndGateway::new);
        register(BlockEntityTypes.END_PORTAL, CraftEndPortal.class, CraftEndPortal::new);
        register(BlockEntityTypes.ENDER_CHEST, CraftEnderChest.class, CraftEnderChest::new);
        register(BlockEntityTypes.FURNACE, CraftFurnaceFurnace.class, CraftFurnaceFurnace::new);
        register(BlockEntityTypes.HANGING_SIGN, CraftHangingSign.class, CraftHangingSign::new);
        register(BlockEntityTypes.HOPPER, CraftHopper.class, CraftHopper::new);
        register(BlockEntityTypes.JIGSAW, CraftJigsaw.class, CraftJigsaw::new);
        register(BlockEntityTypes.JUKEBOX, CraftJukebox.class, CraftJukebox::new);
        register(BlockEntityTypes.LECTERN, CraftLectern.class, CraftLectern::new);
        register(BlockEntityTypes.MOB_SPAWNER, CraftCreatureSpawner.class, CraftCreatureSpawner::new);
        register(BlockEntityTypes.PISTON, CraftMovingPiston.class, CraftMovingPiston::new);
        register(BlockEntityTypes.POTENT_SULFUR, CraftPotentSulfur.class, CraftPotentSulfur::new);
        register(BlockEntityTypes.SCULK_CATALYST, CraftSculkCatalyst.class, CraftSculkCatalyst::new);
        register(BlockEntityTypes.SCULK_SENSOR, CraftSculkSensor.class, CraftSculkSensor::new);
        register(BlockEntityTypes.SCULK_SHRIEKER, CraftSculkShrieker.class, CraftSculkShrieker::new);
        register(BlockEntityTypes.SHELF, CraftShelf.class, CraftShelf::new);
        register(BlockEntityTypes.SHULKER_BOX, CraftShulkerBox.class, CraftShulkerBox::new);
        register(BlockEntityTypes.SIGN, CraftSign.class, CraftSign::new);
        register(BlockEntityTypes.SKULL, CraftSkull.class, CraftSkull::new);
        register(BlockEntityTypes.SMOKER, CraftSmoker.class, CraftSmoker::new);
        register(BlockEntityTypes.STRUCTURE_BLOCK, CraftStructureBlock.class, CraftStructureBlock::new);
        register(BlockEntityTypes.TEST_BLOCK, CraftTestBlock.class, CraftTestBlock::new);
        register(BlockEntityTypes.TEST_INSTANCE_BLOCK, CraftTestInstanceBlock.class, CraftTestInstanceBlock::new);
        register(BlockEntityTypes.TRAPPED_CHEST, CraftChest.class, CraftChest::new);
        register(BlockEntityTypes.TRIAL_SPAWNER, CraftTrialSpawner.class, CraftTrialSpawner::new);
        register(BlockEntityTypes.VAULT, CraftVault.class, CraftVault::new);
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
        net.minecraft.world.level.block.state.BlockState state = craftBlock.getBlockState();
        BlockEntity blockEntity = craftBlock.getLevel().getBlockEntity(pos);
        boolean prev = CraftBlockEntityState.DISABLE_SNAPSHOT;
        CraftBlockEntityState.DISABLE_SNAPSHOT = !useSnapshot;
        try {
            CraftBlockState blockState = CraftBlockStates.getBlockState(world, pos, state, blockEntity);
            blockState.setWorldHandle(craftBlock.getLevel()); // Inject the block's level accessor
            return blockState;
        } finally {
            CraftBlockEntityState.DISABLE_SNAPSHOT = prev;
        }
    }

    @Deprecated
    public static BlockState getBlockState(BlockPos pos, Material material, @Nullable CompoundTag blockEntityTag) {
        return CraftBlockStates.getBlockState(CraftRegistry.getMinecraftRegistry(), pos, material, blockEntityTag);
    }

    public static BlockState getBlockState(LevelReader level, BlockPos pos, Material material, @Nullable CompoundTag blockEntityTag) {
        return CraftBlockStates.getBlockState(level.registryAccess(), pos, material, blockEntityTag);
    }

    public static BlockState getBlockState(RegistryAccess registryAccess, BlockPos pos, Material material, @Nullable CompoundTag blockEntityTag) {
        Preconditions.checkNotNull(material, "material is null");
        net.minecraft.world.level.block.state.BlockState state = CraftBlockType.bukkitToMinecraft(material).defaultBlockState();
        return CraftBlockStates.getBlockState(registryAccess, pos, state, blockEntityTag);
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
    public static CraftBlockState getBlockState(LevelAccessor level, BlockPos pos) {
        return new CraftBlockState(CraftBlock.at(level, pos));
    }

    @Nullable
    public static BlockEntityType<?> getBlockEntityType(final Material material) {
        final BlockStateFactory<?> factory = org.bukkit.craftbukkit.block.CraftBlockStates.FACTORIES.get(material);
        return factory instanceof final BlockEntityStateFactory<?,?> blockEntityStateFactory ? blockEntityStateFactory.blockEntityType : null;
    }

    private CraftBlockStates() {
    }
}
