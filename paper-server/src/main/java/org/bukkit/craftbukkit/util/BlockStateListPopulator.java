package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBlockState;

public class BlockStateListPopulator extends DummyGeneratorAccess {
    private final LevelAccessor world;
    private final Map<BlockPos, net.minecraft.world.level.block.state.BlockState> dataMap = new HashMap<>();
    private final Map<BlockPos, BlockEntity> entityMap = new HashMap<>();
    private final LinkedHashMap<BlockPos, CraftBlockState> blocks;

    public BlockStateListPopulator(LevelAccessor world) {
        this(world, new LinkedHashMap<>());
    }

    private BlockStateListPopulator(LevelAccessor world, LinkedHashMap<BlockPos, CraftBlockState> blocks) {
        this.world = world;
        this.blocks = blocks;
    }

    @Override
    public net.minecraft.world.level.block.state.BlockState getBlockState(BlockPos pos) {
        net.minecraft.world.level.block.state.BlockState state = this.dataMap.get(pos);
        return (state != null) ? state : this.world.getBlockState(pos);
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        net.minecraft.world.level.block.state.BlockState state = this.dataMap.get(pos);
        return (state != null) ? state.getFluidState() : this.world.getFluidState(pos);
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        // The contains is important to check for null values
        if (this.entityMap.containsKey(pos)) {
            return this.entityMap.get(pos);
        }

        return this.world.getBlockEntity(pos);
    }

    @Override
    public boolean setBlock(BlockPos pos, net.minecraft.world.level.block.state.BlockState state, int flags, int recursionLeft) {
        pos = pos.immutable();
        // remove first to keep insertion order
        this.blocks.remove(pos);

        this.dataMap.put(pos, state);
        if (state.hasBlockEntity()) {
            this.entityMap.put(pos, ((EntityBlock) state.getBlock()).newBlockEntity(pos, state));
        } else {
            this.entityMap.put(pos, null);
        }

        // use 'this' to ensure that the block state is the correct TileState
        CraftBlockState snapshot = (CraftBlockState) CraftBlock.at(this, pos).getState();
        snapshot.setFlags(flags);
        // set world handle to ensure that updated calls are done to the world and not to this populator
        snapshot.setWorldHandle(this.world);
        this.blocks.put(pos, snapshot);
        return true;
    }

    @Override
    public ServerLevel getMinecraftWorld() {
        return this.world.getMinecraftWorld();
    }

    public void refreshTiles() {
        for (CraftBlockState snapshot : this.blocks.values()) {
            if (snapshot instanceof CraftBlockEntityState) {
                ((CraftBlockEntityState<?>) snapshot).refreshSnapshot();
            }
        }
    }

    public void placeBlocks() {
        this.placeSomeBlocks($ -> true);
    }

    public void placeSomeBlocks(Predicate<? super BlockState> filter) {
        this.placeSomeBlocks($ -> {}, filter);
    }

    public void placeBlocks(Consumer<? super CraftBlockState> beforeRun) {
        this.placeSomeBlocks(beforeRun, $ -> true);
    }

    public void placeSomeBlocks(Consumer<? super CraftBlockState> beforeRun, Predicate<? super BlockState> filter) {
        for (CraftBlockState state : this.blocks.values()) {
            if (filter.test(state)) {
                beforeRun.accept(state);
                state.place(state.getFlags());
            }
        }
    }

    public List<CraftBlockState> getSnapshotBlocks() {
        return new ArrayList<>(this.blocks.values());
    }

    // For tree generation
    @Override
    public int getMinY() {
        return this.world.getMinY();
    }

    @Override
    public int getHeight() {
        return this.world.getHeight();
    }

    @Override
    public boolean isStateAtPosition(BlockPos pos, Predicate<net.minecraft.world.level.block.state.BlockState> state) {
        return state.test(this.getBlockState(pos));
    }

    @Override
    public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> state) {
        return state.test(this.getFluidState(pos));
    }

    @Override
    public DimensionType dimensionType() {
        return this.world.dimensionType();
    }

    @Override
    public RegistryAccess registryAccess() {
        return this.world.registryAccess();
    }

    // Needed when a tree generates in water
    @Override
    public LevelData getLevelData() {
        return this.world.getLevelData();
    }

    @Override
    public long nextSubTickCount() {
        return this.world.nextSubTickCount();
    }

    // SPIGOT-7966: Needed for some tree generations
    @Override
    public RandomSource getRandom() {
        return this.world.getRandom();
    }

    @Override
    public <T extends BlockEntity> java.util.Optional<T> getBlockEntity(BlockPos pos, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
        BlockEntity blockEntity = this.getBlockEntity(pos);
        return blockEntity != null && blockEntity.getType() == type ? (java.util.Optional<T>) java.util.Optional.of(blockEntity) : java.util.Optional.empty();
    }

    @Override
    public BlockPos getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types heightmap, BlockPos pos) {
        return this.world.getHeightmapPos(heightmap, pos);
    }

    @Override
    public int getHeight(net.minecraft.world.level.levelgen.Heightmap.Types heightmap, int x, int z) {
        return this.world.getHeight(heightmap, x, z);
    }

    @Override
    public int getRawBrightness(BlockPos pos, int amount) {
        return this.world.getRawBrightness(pos, amount);
    }

    @Override
    public int getBrightness(net.minecraft.world.level.LightLayer lightLayer, BlockPos pos) {
        return this.world.getBrightness(lightLayer, pos);
    }
}
