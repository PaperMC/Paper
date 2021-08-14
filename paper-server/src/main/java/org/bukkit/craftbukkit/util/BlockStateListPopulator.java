package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.GeneratorAccess;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.dimension.DimensionManager;
import net.minecraft.world.level.material.Fluid;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;

public class BlockStateListPopulator extends DummyGeneratorAccess {
    private final GeneratorAccess world;
    private final LinkedHashMap<BlockPosition, CraftBlockState> list;

    public BlockStateListPopulator(GeneratorAccess world) {
        this(world, new LinkedHashMap<>());
    }

    public BlockStateListPopulator(GeneratorAccess world, LinkedHashMap<BlockPosition, CraftBlockState> list) {
        this.world = world;
        this.list = list;
    }

    @Override
    public IBlockData getType(BlockPosition bp) {
        CraftBlockState state = list.get(bp);
        return (state != null) ? state.getHandle() : world.getType(bp);
    }

    @Override
    public Fluid getFluid(BlockPosition bp) {
        CraftBlockState state = list.get(bp);
        return (state != null) ? state.getHandle().getFluid() : world.getFluid(bp);
    }

    @Override
    public boolean setTypeAndData(BlockPosition position, IBlockData data, int flag) {
        CraftBlockState state = (CraftBlockState) CraftBlock.at(world, position).getState();
        state.setFlag(flag);
        state.setData(data);
        // remove first to keep insertion order
        list.remove(position);
        list.put(position.immutableCopy(), state);
        return true;
    }

    public void updateList() {
        for (BlockState state : list.values()) {
            state.update(true);
        }
    }

    public Set<BlockPosition> getBlocks() {
        return list.keySet();
    }

    public List<CraftBlockState> getList() {
        return new ArrayList<>(list.values());
    }

    public GeneratorAccess getWorld() {
        return world;
    }

    // For tree generation
    @Override
    public int getMinBuildHeight() {
        return getWorld().getMinBuildHeight();
    }

    @Override
    public int getHeight() {
        return getWorld().getHeight();
    }

    @Override
    public boolean a(BlockPosition blockposition, Predicate<IBlockData> predicate) {
        return predicate.test(getType(blockposition));
    }

    @Override
    public DimensionManager getDimensionManager() {
        return world.getDimensionManager();
    }
}
