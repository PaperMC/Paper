package org.bukkit.craftbukkit;

import net.minecraft.server.Block;

import org.bukkit.BlockChangeDelegate;

public class CraftBlockChangeDelegate {
    private final BlockChangeDelegate delegate;

    public CraftBlockChangeDelegate(BlockChangeDelegate delegate) {
        this.delegate = delegate;
    }

    public BlockChangeDelegate getDelegate() {
        return delegate;
    }

    public Block getType(int x, int y, int z) {
        return Block.e(this.delegate.getTypeId(x, y, z));
    }

    public void setTypeAndData(int x, int y, int z, Block block, int data, int light) {
        delegate.setRawTypeIdAndData(x, y, z, Block.b(block), data);
    }

    public boolean isEmpty(int x, int y, int z) {
        return delegate.isEmpty(x, y, z);
    }
}
