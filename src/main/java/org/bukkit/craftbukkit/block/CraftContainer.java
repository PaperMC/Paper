package org.bukkit.craftbukkit.block;

import net.minecraft.server.ChestLock;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityContainer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Lockable;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftContainer extends CraftBlockState implements Lockable {

    private final TileEntityContainer container;

    public CraftContainer(Block block) {
        super(block);

        container = (TileEntityContainer) ((CraftWorld) block.getWorld()).getTileEntityAt(block.getX(), block.getY(), block.getZ());
    }

    public CraftContainer(final Material material, TileEntity tileEntity) {
        super(material);

        container = (TileEntityContainer) tileEntity;
    }

    @Override
    public boolean isLocked() {
        return container.x_(); // PAIL: isLocked
    }

    @Override
    public String getLock() {
        return container.y_().b(); // PAIL: getLock, getKey
    }

    @Override
    public void setLock(String key) {
        container.a(key == null ? ChestLock.a : new ChestLock(key)); // PAIL: setLock
    }
}
