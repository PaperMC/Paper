package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private final LinkedHashMap<BlockPos, CraftBlockState> list;

    public BlockStateListPopulator(LevelAccessor world) {
        this(world, new LinkedHashMap<>());
    }

    private BlockStateListPopulator(LevelAccessor world, LinkedHashMap<BlockPos, CraftBlockState> list) {
        this.world = world;
        this.list = list;
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
    public boolean setBlock(BlockPos pos, net.minecraft.world.level.block.state.BlockState state, int flags) {
        pos = pos.immutable();
        // remove first to keep insertion order
        this.list.remove(pos);

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
        this.list.put(pos, snapshot);
        return true;
    }

    @Override
    public ServerLevel getMinecraftWorld() {
        return this.world.getMinecraftWorld();
    }

    public void refreshTiles() {
        for (CraftBlockState snapshot : this.list.values()) {
            if (snapshot instanceof CraftBlockEntityState) {
                ((CraftBlockEntityState<?>) snapshot).refreshSnapshot();
            }
        }
    }

    public void updateList() {
        for (CraftBlockState state : this.list.values()) {
            state.place(state.getFlags());
        }
    }

    public Set<BlockPos> getBlocks() {
        return this.list.keySet();
    }

    public List<CraftBlockState> getList() {
        return new ArrayList<>(this.list.values());
    }

    public LevelAccessor getWorld() {
        return this.world;
    }

    // For tree generation
    @Override
    public int getMinY() {
        return this.getWorld().getMinY();
    }

    @Override
    public int getHeight() {
        return this.getWorld().getHeight();
    }

    @Override
    public boolean isStateAtPosition(BlockPos pos, Predicate<net.minecraft.world.level.block.state.BlockState> state) {
        return state.test(this.getBlockState(pos));
    }

    @Override
    public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> state) {
        return state.test(this.getFluidState(pos)); // Paper - fix
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
        return world.getHeightmapPos(heightmap, pos);
    }

    @Override
    public int getHeight(net.minecraft.world.level.levelgen.Heightmap.Types heightmap, int x, int z) {
        return world.getHeight(heightmap, x, z);
    }

    @Override
    public int getRawBrightness(BlockPos pos, int ambientDarkness) {
        return world.getRawBrightness(pos, ambientDarkness);
    }

    @Override
    public int getBrightness(net.minecraft.world.level.LightLayer type, BlockPos pos) {
        return world.getBrightness(type, pos);
    }
}
