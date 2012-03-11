package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.World;
import org.bukkit.block.BlockState;

public class BlockStateListPopulator {
    private final World world;
    private final List<BlockState> list;

    public BlockStateListPopulator(World world) {
        this(world, new ArrayList<BlockState>());
    }

    public BlockStateListPopulator(World world, List<BlockState> list) {
        this.world = world;
        this.list = list;
    }

    public void setTypeId(int x, int y, int z, int type) {
        BlockState state = world.getBlockAt(x, y, z).getState();
        state.setTypeId(type);
        list.add(state);
    }

    public void updateList() {
        for (BlockState state : list) {
            state.update(true);
        }
    }

    public List<BlockState> getList() {
        return list;
    }

    public World getWorld() {
        return world;
    }
}
