package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.CraftBlockStates;

public class BlockStateListPopulator extends DummyGeneratorAccess {

    private final LevelAccessor level;
    private final Map<BlockPos, CapturedBlock> blocks = new LinkedHashMap<>();

    private List<CraftBlockState> snapshots;

    public BlockStateListPopulator(LevelAccessor level) {
        this.level = level;
    }

    @Override
    public net.minecraft.world.level.block.state.BlockState getBlockState(BlockPos pos) {
        CapturedBlock block = this.blocks.get(pos);
        return block != null ? block.state() : this.level.getBlockState(pos);
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        CapturedBlock block = this.blocks.get(pos);
        return block != null ? block.state().getFluidState() : this.level.getFluidState(pos);
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        CapturedBlock block = this.blocks.get(pos);
        return block != null ? block.blockEntity() : this.level.getBlockEntity(pos);
    }

    @Override
    public boolean setBlock(BlockPos pos, net.minecraft.world.level.block.state.BlockState state, int flags, int recursionLeft) {
        pos = pos.immutable();
        // remove first to keep last updated order
        this.blocks.remove(pos);

        final BlockEntity newBlockEntity;
        if (state.getBlock() instanceof EntityBlock entityBlock) {
            // based on LevelChunk#setBlockState
            BlockEntity currentBlockEntity = this.getBlockEntity(pos);
            if (currentBlockEntity != null && currentBlockEntity.isValidBlockState(state)) {
                newBlockEntity = currentBlockEntity; // previous block entity is still valid for this block state
                currentBlockEntity.setBlockState(state);
            } else {
                newBlockEntity = entityBlock.newBlockEntity(pos, state); // create a new one when the block change
            }
        } else {
            newBlockEntity = null;
        }

        this.blocks.put(pos, new CapturedBlock(state, flags, newBlockEntity));
        return true;
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropBlock, Entity entity, int recursionLeft) {
        net.minecraft.world.level.block.state.BlockState blockState = this.getBlockState(pos);
        if (blockState.isAir()) {
            return false;
        }

        this.setBlock(pos, blockState.getFluidState().createLegacyBlock(), 3, recursionLeft); // capture block without the event
        return true;
    }

    @Override
    public ServerLevel getMinecraftWorld() {
        return this.level.getMinecraftWorld();
    }

    private void iterateSnapshots(Consumer<CraftBlockState> callback) {
        for (Map.Entry<BlockPos, CapturedBlock> entry : this.blocks.entrySet()) {
            CapturedBlock block = entry.getValue();
            CraftBlockState snapshot = CraftBlockStates.getBlockState(
                this.getMinecraftWorld().getWorld(), entry.getKey(), block.state(), block.blockEntity()
            );
            snapshot.setFlags(block.flags());
            snapshot.setWorldHandle(this.level);
            callback.accept(snapshot);
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
        for (CraftBlockState snapshot : this.getSnapshotBlocks()) {
            if (filter.test(snapshot)) {
                beforeRun.accept(snapshot);
                snapshot.place(snapshot.getFlags());
            }
        }
    }

    public List<CraftBlockState> getSnapshotBlocks() {
        if (this.snapshots == null) {
            List<CraftBlockState> snapshots = new ArrayList<>();
            this.iterateSnapshots(snapshots::add);
            this.snapshots = snapshots;
        }
        return snapshots;
    }

    // For tree generation

    @Override
    public ServerLevel getLevel() {
        return this.getMinecraftWorld();
    }

    @Override
    public int getMinY() {
        return this.level.getMinY();
    }

    @Override
    public int getHeight() {
        return this.level.getHeight();
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
        return this.level.dimensionType();
    }

    @Override
    public RegistryAccess registryAccess() {
        return this.level.registryAccess();
    }

    // Needed when a tree generates in water
    @Override
    public LevelData getLevelData() {
        return this.level.getLevelData();
    }

    @Override
    public long nextSubTickCount() {
        return this.level.nextSubTickCount();
    }

    // SPIGOT-7966: Needed for some tree generations
    @Override
    public RandomSource getRandom() {
        return this.level.getRandom();
    }

    @Override
    public <T extends BlockEntity> java.util.Optional<T> getBlockEntity(BlockPos pos, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
        BlockEntity blockEntity = this.getBlockEntity(pos);
        return blockEntity != null && blockEntity.getType() == type ? (java.util.Optional<T>) java.util.Optional.of(blockEntity) : java.util.Optional.empty();
    }

    @Override
    public BlockPos getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types heightmapType, BlockPos pos) {
        return this.level.getHeightmapPos(heightmapType, pos);
    }

    @Override
    public int getHeight(net.minecraft.world.level.levelgen.Heightmap.Types heightmapType, int x, int z) {
        return this.level.getHeight(heightmapType, x, z);
    }

    @Override
    public int getRawBrightness(BlockPos pos, int amount) {
        return this.level.getRawBrightness(pos, amount);
    }

    @Override
    public int getBrightness(net.minecraft.world.level.LightLayer lightType, BlockPos pos) {
        return this.level.getBrightness(lightType, pos);
    }
}
