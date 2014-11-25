package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.IBlockData;

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

    public void setTypeAndData(int x, int y, int z, Block block, int data, int light) {
        BlockState state = world.getBlockAt(x, y, z).getState();
        state.setTypeId(Block.getId(block));
        state.setRawData((byte) data);
        list.add(state);
    }
    public void setTypeId(int x, int y, int z, int type) {
        BlockState state = world.getBlockAt(x, y, z).getState();
        state.setTypeId(type);
        list.add(state);
    }

    public void setTypeUpdate(int x, int y, int z, Block block) {
        this.setType(x, y, z, block);
    }    
    
    public void setTypeUpdate(BlockPosition position, IBlockData data) { 
        setTypeAndData(position.getX(), position.getY(), position.getZ(), data.getBlock(), data.getBlock().toLegacyData(data), 0);
        
    }

    public void setType(int x, int y, int z, Block block) {
        BlockState state = world.getBlockAt(x, y, z).getState();
        state.setTypeId(Block.getId(block));
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
