package org.bukkit.craftbukkit.block;

import net.minecraft.server.ChestLock;
import net.minecraft.server.ITileInventory;
import net.minecraft.server.TileEntityContainer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Lockable;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftContainer extends CraftBlockState implements Lockable {

    private final ITileInventory container;

    public CraftContainer(Block block) {
        super(block);

        container = (TileEntityContainer) ((CraftWorld) block.getWorld()).getTileEntityAt(block.getX(), block.getY(), block.getZ());
    }

    public CraftContainer(final Material material, ITileInventory tileEntity) {
        super(material);

        container = tileEntity;
    }

    @Override
    public boolean isLocked() {
        return container.isLocked();
    }

    @Override
    public String getLock() {
        return container.getLock().getKey();
    }

    @Override
    public void setLock(String key) {
        container.setLock(key == null ? ChestLock.a : new ChestLock(key));
    }
}
