package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityLightDetector;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EnderChest;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftDaylightDetector extends CraftBlockState implements EnderChest {

    private final CraftWorld world;
    private final TileEntityLightDetector detector;

    public CraftDaylightDetector(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        detector = (TileEntityLightDetector) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftDaylightDetector(final Material material, final TileEntityLightDetector te) {
        super(material);

        detector = te;
        world = null;
    }

    @Override
    public TileEntity getTileEntity() {
        return detector;
    }
}
