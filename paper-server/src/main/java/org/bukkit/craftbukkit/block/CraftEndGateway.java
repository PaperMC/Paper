package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityEndGateway;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EndGateway;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftEndGateway extends CraftBlockState implements EndGateway {

    private TileEntityEndGateway gateway;

    public CraftEndGateway(Block block) {
        super(block);

        CraftWorld world = (CraftWorld) block.getWorld();
        gateway = (TileEntityEndGateway) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftEndGateway(final Material material, TileEntityEndGateway te) {
        super(material);
        this.gateway = te;
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            gateway.update();
        }

        return result;
    }

    @Override
    public TileEntityEndGateway getTileEntity() {
        return gateway;
    }
}
