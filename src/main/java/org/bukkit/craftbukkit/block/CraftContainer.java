package org.bukkit.craftbukkit.block;

import net.minecraft.server.ChestLock;
import net.minecraft.server.TileEntityContainer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;

public abstract class CraftContainer<T extends TileEntityContainer> extends CraftBlockEntityState<T> implements Container {

    public CraftContainer(Block block, Class<T> tileEntityClass) {
        super(block, tileEntityClass);
    }

    public CraftContainer(final Material material, T tileEntity) {
        super(material, tileEntity);
    }

    @Override
    public boolean isLocked() {
        return this.getSnapshot().isLocked();
    }

    @Override
    public String getLock() {
        return this.getSnapshot().getLock().getKey();
    }

    @Override
    public void setLock(String key) {
        this.getSnapshot().setLock(key == null ? ChestLock.a : new ChestLock(key));
    }
}
